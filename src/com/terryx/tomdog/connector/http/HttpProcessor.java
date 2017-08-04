package com.terryx.tomdog.connector.http;

import com.terryx.tomdog.util.RequestUtil;
import com.terryx.tomdog.util.StringManager;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Implementation of a request processor (and its associated thread) that may
 * be used by an HttpConnector to process individual requests.  The connector
 * will allocate a processor from its pool, assign a particular socket to it,
 * and the processor will then execute the processing required to complete
 * the request.  When the processor is completed, it will recycle itself.
 *
 * @author taoranxue on 6/28/17 4:43 PM.
 */
public class HttpProcessor implements Runnable {
    private HttpConnector connector;
    private HttpRequestImpl request;
    private HttpResponseImpl response;
    private HttpRequestLine requestLine = new HttpRequestLine();


    /**
     * Server information string for this server.
     */
    private static final String SERVER_INFO =
//            ServerInfo.getServerInfo() + " (HTTP/1.1 Connector)";
            "TerryX Tomdog/0.1.1 (HTTP/1.1 Connector)";

    /**
     * Is there a new socket available?
     */
    private boolean available = false;

    /**
     * The socket we are currently processing a request for.  This object
     * is used for inter-thread communication only.
     */
    private Socket socket = null;

    /**
     * The shutdown signal to our background thread
     */
    private boolean stopped = false;

    /**
     * The name to register for the background thread.
     */
    private String threadName = null;

    /**
     * The background thread.
     */
    private Thread thread = null;

    /**
     * The identifier of this processor, unique per connector.
     */
    private int id = 0;

    /**
     * Keep alive indicator.
     */
    private boolean keepAlive = false;


    /**
     * HTTP/1.1 client.
     */
    private boolean http11 = true;


    /**
     * Ack string when pipelining HTTP requests.
     */
    private static final byte[] ack =
            (new String("HTTP/1.1 100 Continue\r\n\r\n")).getBytes();

    /**
     * Processor state
     */
    private int status = Constants.PROCESSOR_IDLE;

    /**
     * True if the client has asked to recieve a request acknoledgement. If so
     * the server will send a preliminary 100 Continue response just after it
     * has successfully parsed the request headers, and before starting
     * reading the request entity body.
     */
    private boolean sendAck = false;

    protected StringManager sm = StringManager.getManager("com.terryx.tomdog.connector.http");

    public HttpProcessor(HttpConnector connector, int id) {
        super();
        this.connector = connector;
//        this.debug = connector.getDebug();
        this.id = id;
//        this.proxyName = connector.getProxyName();
//        this.proxyPort = connector.getProxyPort();
        this.request = (HttpRequestImpl) connector.createRequest();
        this.response = (HttpResponseImpl) connector.createResponse();
//        this.serverPort = connector.getPort();
        this.threadName = "HttpProcessor[" + connector.getPort() + "][" + id + "]";
    }


    /**
     * Process an incoming HTTP request on the Socket that has been assigned
     * to this Processor.  Any exceptions that occur during processing must be
     * swallowed and dealt with.
     *
     * @param socket The socket on which we are connected to the client
     */
    public void process(Socket socket) {
        boolean ok = true;
        boolean finishResponse = true;
        SocketInputStream input = null;
        OutputStream output = null;

        // Construct and initialize the objects we will need
        try {
            input = new SocketInputStream(socket.getInputStream(),
                    connector.getBufferSize());
        } catch (Exception e) {
//            log("process.create", e);
            ok = false;
        }

        keepAlive = true;

        while (!stopped && ok && keepAlive) {

            finishResponse = true;

            try {
                request.setStream(input);
                request.setResponse(response);
                output = socket.getOutputStream();
                response.setStream(output);
                response.setRequest(request);
                ((HttpServletResponse) response.getResponse()).setHeader
                        ("Server", SERVER_INFO);
            } catch (Exception e) {
//                log("process.create", e);
                e.printStackTrace();
                ok = false;
            }

            // Parse the incoming request
            try {
                if (ok) {
                    parseConnection(socket);
                    parseRequest(input, output);

                    if (!request.getRequest().getProtocol()
                            .startsWith("HTTP/0")) {
                        parseHeaders(input);
                    }

                    if (http11) {
                        // Sending a request acknowledge back to the client if
                        // requested.
                        ackRequest(output);
                        // If the protocol is HTTP/1.1, chunking is allowed.
//                        if (connector.isChunkingAllowed())
//                            response.setAllowChunking(true);
                    }
                }

            } catch (EOFException e) {
                // It's very likely to be a socket disconnect on either the
                // client or the server
//                e.printStackTrace();
                ok = false;
                finishResponse = false;
            } catch (Exception e) {
                e.printStackTrace();
            }




            // Ask our Container to process this request
            try {
//                ((HttpServletResponse) response).setHeader
//                        ("Date", FastHttpDateFormat.getCurrentDate());
                if (ok) {
                    connector.getContainer().invoke(request, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Finish up the handling of the request
            if (finishResponse) {
                try {
                    response.finishResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                    ok = false;
                } catch (Throwable e) {
//                    log("process.invoke", e);
                    e.printStackTrace();
                    ok = false;
                }

                try {
                    request.finishRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                    ok = false;
                } catch (Throwable e) {
//                    log("process.invoke", e);
                    e.printStackTrace();
                    ok = false;
                }

                try {
                    if (output != null)
                        output.flush();
                } catch (IOException e) {
                    ok = false;
                }
            }

            // We have to check if the connection closure has been requested
            // by the application or the response stream (in case of HTTP/1.0
            // and keep-alive).
            if ("close".equals(response.getHeader("Connection"))) {
                keepAlive = false;
            }

//            System.out.println("Ok---");
            // End of request processing
            status = Constants.PROCESSOR_IDLE;

            // Recycling the request and the response objects
            request.recycle();
            response.recycle();

        }

        try {
//                shutdownInput(input);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
//                log("process.invoke", e);
        }
        socket = null;


//        SocketInputStream input = null;
//        OutputStream output = null;
//
//        try {
//            input = new SocketInputStream(socket.getInputStream(), 2048);
//            output = socket.getOutputStream();
//
//            request.setStream(input);
//            response.setStream(output);
//            response.setRequest(request);
//            response.setHeader("Server", "Tomdog Server Container");
//
//            parseRequest(input, output);
//            parseHeaders(input);
//
//
//            if (request.getRequestURI().startsWith("/servlet/")) {
//                ServletProcessor processor = new ServletProcessor();
//                processor.process(request, response);
//            } else {
//                StaticResourceProcessor processor = new StaticResourceProcessor();
//                processor.process(request, response);
//            }
//
//            socket.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Parse and record the connection parameters related to this request.
     *
     * @param socket The socket on which we are connected
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a parsing error occurs
     */
    private void parseConnection(Socket socket)
            throws IOException, ServletException {

//        if (debug >= 2)
//            log("  parseConnection: address=" + socket.getInetAddress() +
//                    ", port=" + connector.getPort());
        ((HttpRequestImpl) request).setInet(socket.getInetAddress());
//        if (proxyPort != 0)
//            request.setServerPort(proxyPort);
//        else
//            request.setServerPort(serverPort);
        request.setSocket(socket);

    }

    /**
     * Parse the incoming HTTP request and set the corresponding HTTP request
     * properties.
     *
     * @param input  The input stream attached to our socket
     * @param output The output stream of the socket
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a parsing error occurs
     */
    private void parseRequest(SocketInputStream input, OutputStream output) throws IOException, ServletException {
        input.readRequestLine(requestLine);


        // When the previous method returns, we're actually processing a
        // request
        status = Constants.PROCESSOR_ACTIVE;


        String method = new String(requestLine.method, 0, requestLine.methodEnd);
        String uri = null;
        String protocol = new String(requestLine.protocol, 0, requestLine.protocolEnd);

        if (protocol.length() == 0)
            protocol = "HTTP/0.9";

        // Now check if the connection should be kept alive after parsing the
        // request.
        if (protocol.equals("HTTP/1.1")) {
            http11 = true;
            sendAck = false;
        } else {
            http11 = false;
            sendAck = false;
            // For HTTP/1.0, connection are not persistent by default,
            // unless specified with a Connection: Keep-Alive header.
            keepAlive = false;
        }

        if (method.length() < 1) {
            throw new ServletException("Missing HTTP request method");
        } else if (requestLine.uriEnd < 1) {
            throw new ServletException("Missing HTTP URI");
        }

        /* e.g. http://my.app.com/index.jsp;jsessionid=ababab?username=root */
        int question = requestLine.indexOf("?");

        if (question > 0) {
            request.setQueryString(new String(requestLine.uri, question + 1, requestLine.uriEnd - question - 1));
            uri = new String(requestLine.uri, 0, question);
        } else {
            request.setQueryString(null);
            uri = new String(requestLine.uri, 0, requestLine.uriEnd);
        }

        /* uri = http://my.app.com/index.jsp */

        /* checking for absolute URI.  */
        if (!uri.startsWith("/")) {
            int pos = uri.indexOf("://");
            if (pos != -1) {
                pos = uri.indexOf("/", pos + 3);
                if (pos == -1) {
                    uri = "";
                } else {
                    uri = uri.substring(pos);
                }
            }
        }

        /* uri = /index.jsp;jsessionid=ababab */

        /* parse Session ID */
        String match = ";jsessionid=";
        int semicolon = uri.indexOf(match);
        if (semicolon > 0) {
            String rest = uri.substring(semicolon + match.length());
            int semicolon2 = rest.indexOf(";");
            if (semicolon2 > 0) {
                request.setRequestedSessionId(rest.substring(0, semicolon2));
                rest = rest.substring(semicolon2);
            } else {
                request.setRequestedSessionId(rest);
                rest = "";
            }
            request.setRequestedSessionURL(true);
            uri = uri.substring(0, semicolon) + rest;
        } else {
            request.setRequestedSessionId(null);
            request.setRequestedSessionURL(false);
        }

        /* Normalize URI */
        String normalizeUri = normalize(uri);
        request.setMethod(method);
        request.setProtocol(protocol);

        if (normalizeUri != null) {
            request.setRequestURI(normalizeUri);
        } else {
            request.setRequestURI(uri);
        }

        if (normalizeUri == null) {
            throw new ServletException("Invalid URI '" + uri + "'");
        }

    }

    /**
     * Send a confirmation that a request has been processed when pipelining.
     * HTTP/1.1 100 Continue is sent back to the client.
     *
     * @param output Socket output stream
     */
    private void ackRequest(OutputStream output)
            throws IOException {
        if (sendAck)
            output.write(ack);
    }

    /**
     * Return a context-relative path, beginning with a "/", that represents
     * the canonical version of the specified path after ".." and "." elements
     * are resolved out.  If the specified path attempts to go outside the
     * boundaries of the current context (i.e. too many ".." path elements
     * are present), return <code>null</code> instead.
     *
     * @param uri Path to be normalized
     */
    private String normalize(String uri) throws IOException, ServletException {
        if (uri == null)
            return null;
        // Create a place for the normalized path
        String normalized = uri;

        // Normalize "/%7E" and "/%7e" at the beginning to "/~"
        if (normalized.startsWith("/%7E") || normalized.startsWith("/%7e"))
            normalized = "/~" + normalized.substring(4);

        // Prevent encoding '%', '/', '.' and '\', which are special reserved
        // characters
        if ((normalized.indexOf("%25") >= 0)
                || (normalized.indexOf("%2F") >= 0)
                || (normalized.indexOf("%2E") >= 0)
                || (normalized.indexOf("%5C") >= 0)
                || (normalized.indexOf("%2f") >= 0)
                || (normalized.indexOf("%2e") >= 0)
                || (normalized.indexOf("%5c") >= 0)) {
            return null;
        }

        if (normalized.equals("/."))
            return "/";

        // Normalize the slashes and add leading slash if necessary
        if (normalized.indexOf('\\') >= 0)
            normalized = normalized.replace('\\', '/');
        if (!normalized.startsWith("/"))
            normalized = "/" + normalized;

        // Resolve occurrences of "//" in the normalized path
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 1);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null);  // Trying to go outside our context
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                    normalized.substring(index + 3);
        }

        // Declare occurrences of "/..." (three or more dots) to be invalid
        // (on some Windows platforms this walks the directory tree!!!)
        if (normalized.indexOf("/...") >= 0)
            return (null);

        // Return the normalized path that we have completed
        return (normalized);
    }

    /**
     * This method is the simplified version of the similar method in
     * org.apache.catalina.connector.http.HttpProcessor.
     * However, this method only parses some "easy" headers, such as
     * "cookie", "content-length", and "content-type", and ignore other headers.
     *
     * @param input The input stream connected to our socket
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a parsing error occurs
     */
    private void parseHeaders(SocketInputStream input) throws IOException, ServletException {
        for (; ; ) {
            HttpHeader header = new HttpHeader();
            input.readHeader(header);

            if (header.nameEnd == 0) {
                if (header.valueEnd == 0) {
                    return;
                } else {
                    throw new ServletException(sm.getString("httpProcessor.parseHeaders.colon"));
                }
            }

            String name = new String(header.name, 0, header.nameEnd);
            String value = new String(header.value, 0, header.valueEnd);

            request.addHeader(name, value);

            if ("cookie".equals(name)) {
                //process cookie
                Cookie[] cookies = RequestUtil.parseCookieHeader(value);
                for (int i = 0; i < cookies.length; ++i) {
                    if ("jsessionId".equals(cookies[i].getName())) {
                        // override anything requested in URL
                        if (!request.isRequestedSessionIdFromCookie()) {
                            request.setRequestedSessionId(cookies[i].getValue());
                            request.setRequestedSessionCookie(true);
                            request.setRequestedSessionURL(false);
                        }
                    }
                    request.addCookie(cookies[i]);
                }
            } else if ("content-length".equals(name)) {
                int n = -1;
                try {
                    n = Integer.parseInt(value);
                } catch (Exception e) {
                    throw new ServletException(sm.getString("httpProcessor.parseHeaders.contentLength"));
                }
            } else if ("content-type".equals(name)) {
                request.setContentType(name);
            } else if (header.equals(DefaultHeaders.CONNECTION_NAME)) {
                if (header.valueEquals
                        (DefaultHeaders.CONNECTION_CLOSE_VALUE)) {
                    keepAlive = false;
                    response.setHeader("Connection", "close");
                }
            }
        }

    }

    /**
     * The background thread that listens for incoming TCP/IP connections and
     * hands them off to an appropriate processor.
     */
    @Override
    public void run() {
        // Process requests until we receive a shutdown signal
        while (!stopped) {

            // Wait for the next socket to be assigned
            Socket socket = await();
            if (socket == null)
                continue;

            // Process the request from this socket
            try {
                process(socket);
            } catch (Throwable t) {
//                log("process.invoke", t);
                t.printStackTrace();
            }

            // Finish up this request
            connector.recycle(this);

        }

        // Tell threadStop() we have shut ourselves down successfully
//        synchronized (threadSync) {
//            threadSync.notifyAll();
//        }
    }

    /**
     * Process an incoming TCP/IP connection on the specified socket.  Any
     * exception that occurs during processing must be logged and swallowed.
     * <b>NOTE</b>:  This method is called from our Connector's thread.  We
     * must assign it to our own thread so that multiple simultaneous
     * requests can be handled.
     *
     * @param socket TCP socket to process
     */
    synchronized void assign(Socket socket) {

        // Wait for the Processor to get the previous Socket
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        }

        // Store the newly available Socket and notify our thread
        this.socket = socket;
        available = true;
        notifyAll();

//        if ((debug >= 1) && (socket != null))
//            log(" An incoming request is being assigned");
    }

    /**
     * Start the background processing thread.
     */
    void threadStart() {

//        log(sm.getString("httpProcessor.starting"));

        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();

//        if (debug >= 1)
//            log(" Background thread has been started");

    }

    /**
     * Await a newly assigned Socket from our Connector, or <code>null</code>
     * if we are supposed to shut down.
     */
    private synchronized Socket await() {

        // Wait for the Connector to provide a new Socket
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        }

        // Notify the Connector that we have received this Socket
        Socket socket = this.socket;
        available = false;
        notifyAll();

//        if ((debug >= 1) && (socket != null))
//            log("  The incoming request has been awaited");

        return (socket);

    }
}
