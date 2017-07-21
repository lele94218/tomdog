package com.terryx.tomdog.connector.http;

import com.terryx.tomdog.Connector;
import com.terryx.tomdog.Request;
import com.terryx.tomdog.Response;
import com.terryx.tomdog.net.DefaultServerSocketFactory;
import com.terryx.tomdog.net.ServerSocketFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import java.util.Vector;

/**
 * @author taoranxue on 6/28/17 4:39 PM.
 */
public final class HttpConnector implements Connector, Runnable {

    /**
     * The server socket through which we listen for incoming TCP connections.
     */
    private ServerSocket serverSocket = null;

    /**
     * The server socket factory for this component.
     */
    private ServerSocketFactory factory = null;

    /**
     * The IP address on which to bind, if any.  If <code>null</code>, all
     * addresses on the server will be bound.
     */
    private String address = null;

    /**
     * The port number on which we listen for HTTP requests.
     */
    private int port = 8080;

    /**
     * The accept count for this Connector.
     */
    private int acceptCount = 10;

    /**
     * The set of processors that have been created but are not currently
     * being used to process a request.
     */
    private Stack processors = new Stack();

    /**
     * The minimum number of processors to start at initialization time.
     */
    protected int minProcessors = 5;


    /**
     * The maximum number of processors allowed, or <0 for unlimited.
     */
    private int maxProcessors = 20;

    /**
     * The current number of processors that have been created.
     */
    private int curProcessors = 0;

    /**
     * The set of processors that have ever been created.
     */
    private Vector created = new Vector();

    private boolean shutdown = false;
    private String scheme = "http";

    public String getScheme() {
        return scheme;
    }

    @Override
    public void setScheme(String scheme) {

    }

    @Override
    public boolean getSecure() {
        return false;
    }

    @Override
    public void setSecure(boolean secure) {

    }

    @Override
    public Request createRequest() {
        return null;
    }

    @Override
    public Response createResponse() {
        return null;
    }


    @Override
    public void run() {
        try {
            serverSocket = open();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(1);
//        }

        while (!shutdown) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            // Hand this socket off to an appropriate processor
            HttpProcessor processor = createProcessor();
            if (processor == null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    ;
                }
                continue;
            }
            //            if (debug >= 3)
            //                log("run: Assigning socket to processor " + processor);
            processor.assign(socket);

            // The processor will recycle itself when it finishes

//            HttpProcessor processor = new HttpProcessor(this);
//            processor.process(socket);
        }

        // Notify the threadStop() method that we have shut ourselves down
        //        if (debug >= 3)
        //            log("run: Notifying threadStop() that we have shut down");
//        synchronized (threadSync) {
//            threadSync.notifyAll();
//        }
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }


    /**
     * Open and return the server socket for this Connector.  If an IP
     * address has been specified, the socket will be opened only on that
     * address; otherwise it will be opened on all addresses.
     */
    private ServerSocket open() throws Exception {
        // Acquire the server socket factory for this Connector
        ServerSocketFactory factory = getFactory();

        // If no address is specified, open a connection on all addresses
        if (address == null) {
            return (factory.createSocket(port, acceptCount));
        }

        throw new Exception("Not implement");
    }

    @Override
    public boolean getEnableLookups() {
        return false;
    }

    @Override
    public void setEnableLookups(boolean enableLookups) {

    }

    /**
     * Return the server socket factory used by this Container.
     */
    @Override
    public ServerSocketFactory getFactory() {
        if (this.factory == null) {
            synchronized (this) {
                this.factory = new DefaultServerSocketFactory();
            }
        }
        return (this.factory);
    }

    @Override
    public void setFactory(com.terryx.tomdog.net.ServerSocketFactory factory) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public int getRedirectPort() {
        return 0;
    }

    @Override
    public void setRedirectPort(int redirectPort) {

    }

    /**
     * Return the port number on which we listen for HTTP requests.
     */
    public int getPort() {

        return (this.port);

    }


    /**
     * Set the port number on which we listen for HTTP requests.
     *
     * @param port The new port number
     */
    public void setPort(int port) {

        this.port = port;

    }

    /**
     * Recycle the specified Processor so that it can be used again.
     *
     * @param processor The processor to be recycled
     */
    void recycle(HttpProcessor processor) {

        //        if (debug >= 2)
        //            log("recycle: Recycling processor " + processor);
        processors.push(processor);

    }

    /**
     * Create (or allocate) and return an available processor for use in
     * processing a specific HTTP request, if possible.  If the maximum
     * allowed processors have already been created and are in use, return
     * <code>null</code> instead.
     */
    private HttpProcessor createProcessor() {

        synchronized (processors) {
            if (processors.size() > 0) {
                // if (debug >= 2)
                // log("createProcessor: Reusing existing processor");
                return ((HttpProcessor) processors.pop());
            }
            if ((maxProcessors > 0) && (curProcessors < maxProcessors)) {
                // if (debug >= 2)
                // log("createProcessor: Creating new processor");
                return (newProcessor());
            } else {
                if (maxProcessors < 0) {
                    // if (debug >= 2)
                    // log("createProcessor: Creating new processor");
                    return (newProcessor());
                } else {
                    // if (debug >= 2)
                    // log("createProcessor: Cannot create new processor");
                    return (null);
                }
            }
        }

    }


    /**
     * Create and return a new processor suitable for processing HTTP
     * requests and returning the corresponding responses.
     */
    private HttpProcessor newProcessor() {

        //        if (debug >= 2)
        //            log("newProcessor: Creating new processor");
        HttpProcessor processor = new HttpProcessor(this, curProcessors++);
//        if (processor instanceof Lifecycle) {
//            try {
//                ((Lifecycle) processor).start();
//            } catch (LifecycleException e) {
//                log("newProcessor", e);
//                return (null);
//            }
//        }
        processor.threadStart();
        created.addElement(processor);
        return (processor);

    }

}
