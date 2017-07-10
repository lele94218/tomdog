package com.terryx.tomdog.connector.http;


import com.terryx.tomdog.util.ParameterMap;
import com.terryx.tomdog.util.RequestUtil;
import org.apache.catalina.util.Enumerator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

/**
 * @author taoranxue on 6/27/17 4:35 PM.
 */
public class HttpRequest implements HttpServletRequest {
    private InputStream input;


    /**
     * The set of cookies associated with this Request.
     */
    protected ArrayList cookies = new ArrayList();
    /**
     * The content type associated with this request.
     */
    protected String contentType = null;

    /**
     * The HTTP headers associated with this Request, keyed by name.  The
     * values are ArrayLists of the corresponding header values.
     */
    protected HashMap headers = new HashMap();

    /**
     * The request URI associated with this request.
     */
    protected String requestURI = null;

    /**
     * Have the parameters for this request been parsed yet?
     */
    protected boolean parsed = false;

    /**
     * The query string for this request.
     */
    protected String queryString = null;

    /**
     * The requested session ID (if any) for this request.
     */
    protected String requestedSessionId = null;

    /**
     * Was the requested session ID received in a URL?
     */
    protected boolean requestedSessionURL = false;

    /**
     * The request method associated with this Request.
     */
    protected String method = null;


    /**
     * The protocol name and version associated with this Request.
     */
    protected String protocol = null;


    /**
     * Was the requested session ID received in a cookie?
     */
    protected boolean requestedSessionCookie = false;

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

    public HttpRequest(InputStream input) {
        this.input = input;
    }

    @Deprecated
    public void parse() {
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = input.read(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; ++j) {
            request.append((char) buffer[j]);
        }
        System.out.println(request.toString());
        requestURI = parseUri(request.toString());
    }

    @Deprecated
    private String parseUri(String s) {
        int index1, index2;
        index1 = s.indexOf(' ');
        if (index1 != -1) {
            index2 = s.indexOf(' ', index1 + 1);
            if (index2 > index1) {
                return s.substring(index1 + 1, index2);
            }
        }
        return null;
    }

    public String getUri() {
        return requestURI;
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
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
    public Enumeration getParameterNames() {
        parseParameters();
        return (new Enumerator(parameters.keySet()));
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
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
    public Enumeration getHeaders(String name) {
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
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
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
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
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
        return null;
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

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setRequestedSessionId(String requestedSessionId) {
        this.requestedSessionId = requestedSessionId;
    }

    public void setRequestedSessionURL(boolean requestedSessionURL) {
        this.requestedSessionURL = requestedSessionURL;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

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

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setRequestedSessionCookie(boolean requestedSessionCookie) {
        this.requestedSessionCookie = requestedSessionCookie;
    }

    /**
     * Add a Cookie to the set of Cookies associated with this Request.
     *
     * @param cookie The new cookie
     */
    public void addCookie(Cookie cookie) {
        synchronized (cookies) {
            cookies.add(cookie);
        }
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
}
