package com.terryx.tomdog.connector.http;

import com.terryx.tomdog.Constants;
import com.terryx.tomdog.util.RequestUtil;
import org.apache.catalina.util.CookieTools;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/**
 * @author taoranxue on 6/27/17 4:38 PM.
 * @deprecated
 */
public class OldHttpResponse implements HttpServletResponse {
    private static final int BUFFER_SIZE = 1024;
    private OldHttpRequest request;
    private OutputStream output;
    private PrintWriter writer;
    protected byte[] buffer = new byte[BUFFER_SIZE];
    protected int bufferCount = 0;
    /**
     * Has this response been committed yet?
     */
    protected boolean committed = false;
    /**
     * The actual number of bytes written to this Response.
     */
    protected int contentCount = 0;
    /**
     * The content length associated with this Response.
     */
    protected int contentLength = -1;
    /**
     * The content type associated with this Response.
     */
    protected String contentType = null;

    /**
     * The character encoding associated with this Response.
     */
    protected String encoding = null;
    /**
     * The HTTP status code associated with this Response.
     */
    protected int status = HttpServletResponse.SC_OK;

    /**
     * The error message set by <code>sendError()</code>.
     */
    protected String message = getStatusMessage(HttpServletResponse.SC_OK);

    /**
     * The HTTP headers explicitly added via addHeader(), but not including
     * those to be added with setContentLength(), setContentType(), and so on.
     * This collection is keyed by the header name, and the elements are
     * ArrayLists containing the associated values that have been set.
     */
    protected HashMap headers = new HashMap();

    /**
     * The set of Cookies associated with this Response.
     */
    protected ArrayList cookies = new ArrayList();

    public OldHttpResponse(OutputStream output) {
        this.output = output;
    }

    protected String getStatusMessage(int status) {
        switch (status) {
            case SC_OK:
                return ("OK");
            case SC_ACCEPTED:
                return ("Accepted");
            case SC_BAD_GATEWAY:
                return ("Bad Gateway");
            case SC_BAD_REQUEST:
                return ("Bad Request");
            case SC_CONFLICT:
                return ("Conflict");
            case SC_CONTINUE:
                return ("Continue");
            case SC_CREATED:
                return ("Created");
            case SC_EXPECTATION_FAILED:
                return ("Expectation Failed");
            case SC_FORBIDDEN:
                return ("Forbidden");
            case SC_GATEWAY_TIMEOUT:
                return ("Gateway Timeout");
            case SC_GONE:
                return ("Gone");
            case SC_HTTP_VERSION_NOT_SUPPORTED:
                return ("HTTP Version Not Supported");
            case SC_INTERNAL_SERVER_ERROR:
                return ("Internal Server Error");
            case SC_LENGTH_REQUIRED:
                return ("Length Required");
            case SC_METHOD_NOT_ALLOWED:
                return ("Method Not Allowed");
            case SC_MOVED_PERMANENTLY:
                return ("Moved Permanently");
            case SC_MOVED_TEMPORARILY:
                return ("Moved Temporarily");
            case SC_MULTIPLE_CHOICES:
                return ("Multiple Choices");
            case SC_NO_CONTENT:
                return ("No Content");
            case SC_NON_AUTHORITATIVE_INFORMATION:
                return ("Non-Authoritative Information");
            case SC_NOT_ACCEPTABLE:
                return ("Not Acceptable");
            case SC_NOT_FOUND:
                return ("Not Found");
            case SC_NOT_IMPLEMENTED:
                return ("Not Implemented");
            case SC_NOT_MODIFIED:
                return ("Not Modified");
            case SC_PARTIAL_CONTENT:
                return ("Partial Content");
            case SC_PAYMENT_REQUIRED:
                return ("Payment Required");
            case SC_PRECONDITION_FAILED:
                return ("Precondition Failed");
            case SC_PROXY_AUTHENTICATION_REQUIRED:
                return ("Proxy Authentication Required");
            case SC_REQUEST_ENTITY_TOO_LARGE:
                return ("Request Entity Too Large");
            case SC_REQUEST_TIMEOUT:
                return ("Request Timeout");
            case SC_REQUEST_URI_TOO_LONG:
                return ("Request URI Too Long");
            case SC_REQUESTED_RANGE_NOT_SATISFIABLE:
                return ("Requested Range Not Satisfiable");
            case SC_RESET_CONTENT:
                return ("Reset Content");
            case SC_SEE_OTHER:
                return ("See Other");
            case SC_SERVICE_UNAVAILABLE:
                return ("Service Unavailable");
            case SC_SWITCHING_PROTOCOLS:
                return ("Switching Protocols");
            case SC_UNAUTHORIZED:
                return ("Unauthorized");
            case SC_UNSUPPORTED_MEDIA_TYPE:
                return ("Unsupported Media Type");
            case SC_USE_PROXY:
                return ("Use Proxy");
            case 207:       // WebDAV
                return ("Multi-Status");
            case 422:       // WebDAV
                return ("Unprocessable Entity");
            case 423:       // WebDAV
                return ("Locked");
            case 507:       // WebDAV
                return ("Insufficient Storage");
            default:
                return ("HTTP Response Status " + status);
        }
    }

    public OutputStream getStream() {
        return this.output;
    }

    protected String getProtocol() {
        return request.getProtocol();
    }


    public String getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    /**
     * Send the HTTP response headers, if this has not already occurred.
     */
    protected void sendHeaders() throws IOException {
        if (isCommitted())
            return;
//        System.out.println("sending header...." + getProtocol());

        // Prepare a suitable output writer
        OutputStreamWriter osr = null;
        try {
            osr = new OutputStreamWriter(getStream(), getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            osr = new OutputStreamWriter(getStream());
        }
        final PrintWriter outputWriter = new PrintWriter(osr);
        // Send the "Status:" header
        outputWriter.print(this.getProtocol());
        outputWriter.print(" ");
        outputWriter.print(status);
        if (message != null) {
            outputWriter.print(" ");
            outputWriter.print(message);
        }
        outputWriter.print("\r\n");
        // Send the content-length and content-type headers (if any)
        System.out.println(getContentType());
        if (getContentType() != null) {
            outputWriter.print("Content-Type: " + getContentType() + "\r\n");
        }
        if (getContentLength() >= 0) {
            outputWriter.print("Content-Length: " + getContentLength() + "\r\n");
        }
        // Send all specified headers (if any)
        synchronized (headers) {
            Iterator names = headers.keySet().iterator();
            while (names.hasNext()) {
                String name = (String) names.next();
                ArrayList values = (ArrayList) headers.get(name);
                Iterator items = values.iterator();
                while (items.hasNext()) {
                    String value = (String) items.next();
                    outputWriter.print(name);
                    outputWriter.print(": ");
                    outputWriter.print(value);
                    outputWriter.print("\r\n");
                }
            }
        }
        // Add the session ID cookie if necessary
/*    HttpServletRequest hreq = (HttpServletRequest) request.getRequest();
    HttpSession session = hreq.getSession(false);
    if ((session != null) && session.isNew() && (getContext() != null)
      && getContext().getCookies()) {
      Cookie cookie = new Cookie("JSESSIONID", session.getId());
      cookie.setMaxAge(-1);
      String contextPath = null;
      if (context != null)
        contextPath = context.getPath();
      if ((contextPath != null) && (contextPath.length() > 0))
        cookie.setPath(contextPath);
      else

      cookie.setPath("/");
      if (hreq.isSecure())
        cookie.setSecure(true);
      addCookie(cookie);
    }
*/
        // Send all specified cookies (if any)
        synchronized (cookies) {
            Iterator items = cookies.iterator();
            while (items.hasNext()) {
                Cookie cookie = (Cookie) items.next();
                outputWriter.print(CookieTools.getCookieHeaderName(cookie));
                outputWriter.print(": ");
                outputWriter.print(CookieTools.getCookieHeaderValue(cookie));
                outputWriter.print("\r\n");
            }
        }

        // Send a terminating blank line to mark the end of the headers
        outputWriter.print("\r\n");
        outputWriter.flush();

        committed = true;
    }

    public void setRequest(OldHttpRequest request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
//            System.out.println(Constants.WEB_ROOT + "--" + Thread.currentThread().getName());
            File file = new File(Constants.WEB_ROOT, request.getUri());
            if (file.exists()) {
                StringBuffer responseMessage = new StringBuffer("HTTP/1.1 200 OK\r\n")
                        .append("Content-Type: text/html\r\n");
                StringBuffer content = new StringBuffer();
                fis = new FileInputStream(file);
                int total = 0;
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    total += ch;
//                    output.write(bytes, 0, ch);
                    content.append(new String(bytes));
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
                responseMessage.append("Content-Length: ")
                        .append(total)
                        .append("\r\n")
                        .append("\r\n")
                        .append(content.toString());
                output.write(responseMessage.toString().getBytes());

            } else {
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 23\r\n" +
                        "\r\n" +
                        "<h1>File Not Found</h1>";
                output.write(errorMessage.getBytes());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) fis.close();
        }
    }

    /**
     * Write the specified byte to our output stream, flushing if necessary.
     *
     * @param b The byte to be written
     * @throws IOException if an input/output error occurs
     */
    public void write(int b) throws IOException {
        if (bufferCount >= buffer.length)
            flushBuffer();
        buffer[bufferCount++] = (byte) b;
        contentCount++;
    }

    /**
     * Write <code>b.length</code> bytes from the specified byte array
     * to our output stream.  Flush the output stream as necessary.
     *
     * @param b The byte array to be written
     * @throws IOException if an input/output error occurs
     */
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    /**
     * Write <code>len</code> bytes from the specified byte array, starting
     * at the specified offset, to our output stream.  Flush the output
     * stream as necessary.
     *
     * @param b   The byte array containing the bytes to be written
     * @param off Zero-relative starting offset of the bytes to be written
     * @param len The number of bytes to be written
     * @throws IOException if an input/output error occurs
     */
    public void write(byte b[], int off, int len) throws IOException {
        // If the whole thing fits in the buffer, just put it there
        if (len == 0)
            return;
        if (len <= (buffer.length - bufferCount)) {
            System.arraycopy(b, off, buffer, bufferCount, len);
            bufferCount += len;
            contentCount += len;
            return;
        }

        // Flush the buffer and start writing full-buffer-size chunks
        flushBuffer();
        int iterations = len / buffer.length;
        int leftoverStart = iterations * buffer.length;
        int leftoverLen = len - leftoverStart;
        for (int i = 0; i < iterations; i++)
            write(b, off + (i * buffer.length), buffer.length);

        // Write the remainder (guaranteed to fit in the buffer)
        if (leftoverLen > 0)
            write(b, off + leftoverStart, leftoverLen);
    }


    /**
     * call this method to send headers and response to the output
     */
    public void finishResponse() {
        // sendHeaders();
        // Flush and close the appropriate output mechanism
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

    @Override
    public String getCharacterEncoding() {
        if (encoding == null) {
            return ("ISO-8859-1");
        } else {
            return (encoding);
        }
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
//        System.out.println("get writer: " + output.toString());
//        writer = new PrintWriter(output, true);
//        ResponseStream newStream = new ResponseStream(this);
//        newStream.setCommit(false);
//        OutputStreamWriter osr = new OutputStreamWriter(newStream, getCharacterEncoding());
//        writer = new ResponseWriter(osr);
//        return writer;
        return null;
    }

    @Override
    public void setContentLength(int length) {
        if (isCommitted())
            return;
        this.contentLength = length;

    }

    @Override
    public void setContentType(String type) {
        if (isCommitted())
            return;


        this.contentType = type;
        if (type.indexOf(';') >= 0) {
            encoding = RequestUtil.parseCharacterEncoding(type);
            if (encoding == null)
                encoding = "ISO-8859-1";
        } else {
            if (encoding != null)
                this.contentType = type + ";charset=" + encoding;
        }
    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

        if (!isCommitted())
            sendHeaders();

        if (bufferCount > 0) {
            try {
                output.write(buffer, 0, bufferCount);
            } finally {
                bufferCount = 0;
            }
        }

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return (committed);
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public boolean containsHeader(String s) {
        return false;
    }

    @Override
    public String encodeURL(String s) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String s) {
        return null;
    }

    @Override
    public String encodeUrl(String s) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String s) {
        return null;
    }

    @Override
    public void sendError(int i, String s) throws IOException {

    }

    @Override
    public void sendError(int i) throws IOException {

    }

    @Override
    public void sendRedirect(String s) throws IOException {

    }

    @Override
    public void setDateHeader(String s, long l) {

    }

    @Override
    public void addDateHeader(String s, long l) {

    }

    @Override
    public void setHeader(String s, String s1) {

    }

    @Override
    public void addHeader(String s, String s1) {

    }

    @Override
    public void setIntHeader(String s, int i) {

    }

    @Override
    public void addIntHeader(String s, int i) {

    }

    @Override
    public void setStatus(int i) {

    }

    @Override
    public void setStatus(int i, String s) {

    }
}
