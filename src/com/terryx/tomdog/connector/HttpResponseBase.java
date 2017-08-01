package com.terryx.tomdog.connector;

import com.terryx.tomdog.HttpResponse;
import org.apache.catalina.util.CookieTools;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author taoranxue on 7/13/17 7:37 PM.
 */
public class HttpResponseBase
        extends ResponseBase
        implements HttpResponse, HttpServletResponse {
    // ----------------------------------------------------- Instance Variables


    /**
     * The set of Cookies associated with this Response.
     */
    protected ArrayList cookies = new ArrayList();


    /**
     * The date format we will use for creating date headers.
     */
    protected final SimpleDateFormat format =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);


    /**
     * The facade associated with this response.
     */
    protected HttpResponseFacade facade = new HttpResponseFacade(this);


    /**
     * The HTTP headers explicitly added via addHeader(), but not including
     * those to be added with setContentLength(), setContentType(), and so on.
     * This collection is keyed by the header name, and the elements are
     * ArrayLists containing the associated values that have been set.
     */
    protected HashMap headers = new HashMap();


    /**
     * Descriptive information about this HttpResponse implementation.
     */
    protected static final String info =
            "org.apache.catalina.connector.HttpResponseBase/1.0";


    /**
     * The error message set by <code>sendError()</code>.
     */
    protected String message = getStatusMessage(HttpServletResponse.SC_OK);


    /**
     * The HTTP status code associated with this Response.
     */
    protected int status = HttpServletResponse.SC_OK;


    /**
     * The time zone with which to construct date headers.
     */
    protected static final TimeZone zone = TimeZone.getTimeZone("GMT");

    /**
     * Return the <code>ServletResponse</code> for which this object
     * is the facade.
     */
    public ServletResponse getResponse() {

        return (facade);

    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public String[] getHeaderNames() {
        return new String[0];
    }

    @Override
    public String[] getHeaderValues(String name) {
        return new String[0];
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public void reset(int status, String message) {

    }

    @Override
    public void setSuspended(boolean suspended) {

    }

    @Override
    public void sendAcknowledgement() throws IOException {

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

    /**
     * Set the specified header to the specified value.
     *
     * @param name  Name of the header to set
     * @param value Value to be set
     */
    @Override
    public void setHeader(String name, String value) {
        if (isCommitted())
            return;

        if (included)
            return;     // Ignore any call from an included servlet

        ArrayList values = new ArrayList();
        values.add(value);
        synchronized (headers) {
            headers.put(name, values);
        }

        String match = name.toLowerCase();
        if (match.equals("content-length")) {
            int contentLength = -1;
            try {
                contentLength = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                ;
            }
            if (contentLength >= 0)
                setContentLength(contentLength);
        } else if (match.equals("content-type")) {
            setContentType(value);
        }
    }

    /**
     * Set the content length (in bytes) for this Response.
     *
     * @param length The new content length
     */
    public void setContentLength(int length) {

        if (isCommitted())
            return;

        if (included)
            return;     // Ignore any call from an included servlet

        super.setContentLength(length);

    }

    /**
     * Set the content type for this Response.
     *
     * @param type The new content type
     */
    public void setContentType(String type) {

        if (isCommitted())
            return;

        if (included)
            return;     // Ignore any call from an included servlet

        super.setContentType(type);

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

        if (included)
            return;     // Ignore any call from an included servlet

        this.status = status;
        this.message = message;
    }

    @Override
    public void setStatus(int i, String s) {

    }

    /**
     * Flush the buffer and commit this response.  If this is the first output,
     * send the HTTP headers prior to the user data.
     *
     * @throws IOException if an input/output error occurs
     */
    public void flushBuffer() throws IOException {

        if (!isCommitted())
            sendHeaders();

        super.flushBuffer();
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
//        System.out.println(getContentType());
        if (getContentType() != null) {
            outputWriter.print("Content-Type: " + getContentType() + "\r\n");
        }
        if (getContentLength() >= 0) {
            System.out.println(getContentLength() + "-----------");
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

    /**
     * Return the HTTP protocol version implemented by this response
     * object. (This method must be overridden by subclasses of this
     * as to correctly return the highest HTTP version number supported
     * as specified in Section 3.1 of RFC-2616).
     *
     * @return A string in the form of &quot;HTTP/1.0&quot; ...
     */
    protected String getProtocol() {
        return (request.getRequest().getProtocol());
    }
}
