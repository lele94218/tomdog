package com.terryx.tomdog.connector;

import com.terryx.tomdog.HttpRequest;
import com.terryx.tomdog.util.Enumerator;
import com.terryx.tomdog.util.ParameterMap;
import com.terryx.tomdog.util.RequestUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author taoranxue on 7/13/17 6:58 PM.
 */
public class HttpRequestBase extends RequestBase implements HttpRequest, HttpServletRequest {
    // ----------------------------------------------------- Instance Variables


    /**
     * The authentication type used for this request.
     */
    protected String authType = null;


    /**
     * The context path for this request.
     */
    protected String contextPath = "";


    /**
     * The set of cookies associated with this Request.
     */
    protected ArrayList cookies = new ArrayList();


    /**
     * An empty collection to use for returning empty Enumerations.  Do not
     * add any elements to this collection!
     */
    protected static ArrayList empty = new ArrayList();


    /**
     * The set of SimpleDateFormat formats to use in getDateHeader().
     */
    protected SimpleDateFormat formats[] = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
            new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US),
            new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US)
    };


    /**
     * The facade associated with this request.
     */
    protected HttpRequestFacade facade = new HttpRequestFacade(this);


    /**
     * The HTTP headers associated with this Request, keyed by name.  The
     * values are ArrayLists of the corresponding header values.
     */
    protected HashMap headers = new HashMap();


    /**
     * Descriptive information about this HttpRequest implementation.
     */
    protected static final String info =
            "org.apache.catalina.connector.HttpRequestBase/1.0";


    /**
     * The request method associated with this Request.
     */
    protected String method = null;


    /**
     * The parsed parameters for this request.  This is populated only if
     * parameter information is requested via one of the
     * <code>getParameter()</code> family of method calls.  The key is the
     * parameter name, while the value is a String array of values for this
     * parameter.
     * <p>
     * <strong>IMPLEMENTATION NOTE</strong> - Once the parameters for a
     * particular request are parsed and stored here, they are not modified.
     * Therefore, application level access to the parameters need not be
     * synchronized.
     */
    protected ParameterMap parameters = null;


    /**
     * Have the parameters for this request been parsed yet?
     */
    protected boolean parsed = false;


    /**
     * The path information for this request.
     */
    protected String pathInfo = null;


    /**
     * The query string for this request.
     */
    protected String queryString = null;


    /**
     * Was the requested session ID received in a cookie?
     */
    protected boolean requestedSessionCookie = false;


    /**
     * The requested session ID (if any) for this request.
     */
    protected String requestedSessionId = null;


    /**
     * Was the requested session ID received in a URL?
     */
    protected boolean requestedSessionURL = false;


    /**
     * The request URI associated with this request.
     */
    protected String requestURI = null;


    /**
     * The decoded request URI associated with this request.
     */
    protected String decodedRequestURI = null;


    /**
     * Was this request received on a secure channel?
     */
    protected boolean secure = false;


    /**
     * The servlet path for this request.
     */
    protected String servletPath = null;


//    /**
//     * The currently active session for this request.
//     */
//    protected Session session = null;


    /**
     * The Principal who has been authenticated for this Request.
     */
    protected Principal userPrincipal = null;


    /**
     * Return the <code>ServletRequest</code> for which this object
     * is the facade.  This method must be implemented by a subclass.
     */
    public ServletRequest getRequest() {

        return (facade);

    }

    @Override
    public void addCookie(Cookie cookie) {
        synchronized (cookies) {
            cookies.add(cookie);
        }
    }

    @Override
    public void addHeader(String name, String value) {
        name = name.toLowerCase();
        synchronized (headers) {
            ArrayList values = (ArrayList) headers.get(name);
            if (values == null) {
                values = new ArrayList();
                headers.put(name, values);
            }
            values.add(value);
        }

    }

    @Override
    public void addParameter(String name, String[] values) {

    }

    @Override
    public void clearCookies() {

    }

    @Override
    public void clearHeaders() {

    }

    @Override
    public void clearLocales() {

    }

    @Override
    public void clearParameters() {

    }

    @Override
    public void setAuthType(String type) {

    }

    @Override
    public void setContextPath(String path) {

    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public void setQueryString(String query) {
        this.queryString = query;
    }

    @Override
    public void setPathInfo(String path) {
        this.pathInfo = path;
    }

    @Override
    public void setRequestedSessionCookie(boolean flag) {
        this.requestedSessionCookie = false;
    }

    @Override
    public void setRequestedSessionId(String id) {
        this.requestedSessionId = id;
    }

    @Override
    public void setRequestedSessionURL(boolean flag) {
        this.requestedSessionURL = flag;
    }

    @Override
    public void setRequestURI(String uri) {
        this.requestURI = uri;
    }

    @Override
    public void setDecodedRequestURI(String uri) {
        this.decodedRequestURI = uri;
    }

    @Override
    public String getDecodedRequestURI() {

        if (decodedRequestURI == null)
            decodedRequestURI = RequestUtil.URLDecode(getRequestURI());

        return decodedRequestURI;
    }

    @Override
    public void setServletPath(String path) {

    }

    @Override
    public void setUserPrincipal(Principal principal) {

    }

    @Override
    public String getParameter(String name) {
        parseParameters();
        String values[] = (String[]) parameters.get(name);
        if (values != null)
            return (values[0]);
        else
            return null;
    }

    @Override
    public Map getParameterMap() {
        return null;
    }

    @Override
    public Enumeration getParameterNames() {
        parseParameters();
        return (new Enumerator(parameters.keySet()));
    }

    @Override
    public String[] getParameterValues(String name) {
        return new String[0];
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String name) {
        name = name.toLowerCase();
        synchronized (headers) {
            ArrayList values = (ArrayList) headers.get(name);
            if (values != null) {
                return ((String) values.get(0));
            } else {
                return null;
            }
        }
    }

    @Override
    public Enumeration getHeaders(String s) {
        synchronized (headers) {
            return (new Enumerator(headers.keySet()));
        }
    }

    @Override
    public Enumeration getHeaderNames() {
        synchronized (headers) {
            return (new Enumerator(headers.keySet()));
        }
    }

    @Override
    public int getIntHeader(String s) {
        return 0;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return userPrincipal;
    }

    @Override
    public String getRequestedSessionId() {
        return requestedSessionId;
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    @Override
    public HttpSession getSession(boolean b) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    /**
     * Parse the parameters of this request, if it has not already occurred.
     * If parameters are present in both the query string and the request
     * content, they are merged.
     */
    protected void parseParameters() {
        if (parsed) return;

        ParameterMap results = parameters;

        if (parameters == null)
            results = new ParameterMap();

        results.setLocked(false);

        String encoding = getCharacterEncoding();
        if (encoding == null)
            encoding = "ISO-8859-1";

        // Parse any parameters specified in the query string
        String queryString = getQueryString();
        try {
            RequestUtil.parseParameters(results, queryString, encoding);
        } catch (UnsupportedEncodingException e) {
            ;
        }

        // Parse any parameters specified in the input stream
        String contentType = getContentType();
        if (contentType == null)
            contentType = "";
        int semicolon = contentType.indexOf(';');
        if (semicolon >= 0) {
            contentType = contentType.substring(0, semicolon).trim();
        } else {
            contentType = contentType.trim();
        }

        if ("POST".equals(getMethod()) && getContentLength() > 0 && "application/x-www-form-urlencoded".equals(contentType)) {
            try {
                int max = getContentLength();
                int len = 0;
                byte buf[] = new byte[getContentLength()];
                ServletInputStream is = getInputStream();
                while (len < max) {
                    int next = is.readLine(buf, len, max - len);
                    if (next < 0) {
                        break;
                    }
                    len += next;
                }
                is.close();
                if (len < max) {
                    throw new RuntimeException("Content length mismatch");
                }
                RequestUtil.parseParameters(results, buf, encoding);
            } catch (UnsupportedEncodingException e) {
                ;
            } catch (IOException e) {
                throw new RuntimeException("Content read fail");
            }
        }

        // Store the final results
        results.setLocked(true);
        parsed = true;
        parameters = results;
    }

    /**
     * Release all object references, and initialize instance variables, in
     * preparation for reuse of this object.
     */
    public void recycle() {

        super.recycle();
        authType = null;
        contextPath = "";
        cookies.clear();
        headers.clear();
        method = null;
        if (parameters != null) {
            parameters.setLocked(false);
            parameters.clear();
        }
        parsed = false;
        pathInfo = null;
        queryString = null;
        requestedSessionCookie = false;
        requestedSessionId = null;
        requestedSessionURL = false;
        requestURI = null;
        decodedRequestURI = null;
        secure = false;
        servletPath = null;
//        session = null;
        userPrincipal = null;

    }
}
