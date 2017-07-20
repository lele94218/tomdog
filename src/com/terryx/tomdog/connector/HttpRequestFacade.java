package com.terryx.tomdog.connector;

import com.terryx.tomdog.HttpRequest;
import com.terryx.tomdog.connector.RequestFacade;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;


/**
 * Facade class that wraps a Catalina-internal <b>HttpRequest</b>
 * object.  All methods are delegated to the wrapped request.
 *
 * @author Remy Maucherat
 * @version $Revision: 466595 $ $Date: 2006-10-21 23:24:41 +0100 (Sat, 21 Oct 2006) $
 */

public final class HttpRequestFacade
        extends RequestFacade
        implements HttpServletRequest {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a wrapper for the specified request.
     *
     * @param request The request to be wrapped
     */
    public HttpRequestFacade(HttpRequest request) {
        super(request);
    }


    // --------------------------------------------- HttpServletRequest Methods


    public String getAuthType() {
        return ((HttpServletRequest) request).getAuthType();
    }


    public Cookie[] getCookies() {
        return ((HttpServletRequest) request).getCookies();
    }


    public long getDateHeader(String name) {
        return ((HttpServletRequest) request).getDateHeader(name);
    }


    public String getHeader(String name) {
        return ((HttpServletRequest) request).getHeader(name);
    }


    public Enumeration getHeaders(String name) {
        return ((HttpServletRequest) request).getHeaders(name);
    }


    public Enumeration getHeaderNames() {
        return ((HttpServletRequest) request).getHeaderNames();
    }


    public int getIntHeader(String name) {
        return ((HttpServletRequest) request).getIntHeader(name);
    }


    public String getMethod() {
        return ((HttpServletRequest) request).getMethod();
    }


    public String getPathInfo() {
        return ((HttpServletRequest) request).getPathInfo();
    }


    public String getPathTranslated() {
        return ((HttpServletRequest) request).getPathTranslated();
    }


    public String getContextPath() {
        return ((HttpServletRequest) request).getContextPath();
    }


    public String getQueryString() {
        return ((HttpServletRequest) request).getQueryString();
    }


    public String getRemoteUser() {
        return ((HttpServletRequest) request).getRemoteUser();
    }


    public boolean isUserInRole(String role) {
        return ((HttpServletRequest) request).isUserInRole(role);
    }


    public java.security.Principal getUserPrincipal() {
        return ((HttpServletRequest) request).getUserPrincipal();
    }


    public String getRequestedSessionId() {
        return ((HttpServletRequest) request).getRequestedSessionId();
    }


    public String getRequestURI() {
        return ((HttpServletRequest) request).getRequestURI();
    }


    public StringBuffer getRequestURL() {
        return ((HttpServletRequest) request).getRequestURL();
    }


    public String getServletPath() {
        return ((HttpServletRequest) request).getServletPath();
    }


    public HttpSession getSession(boolean create) {
//        HttpSession session =
//                ((HttpServletRequest) request).getSession(create);
//        if (session == null)
//            return null;
//        else
//            return new StandardSessionFacade(session);
        return null;
    }


    public HttpSession getSession() {
        return getSession(true);
    }


    public boolean isRequestedSessionIdValid() {
        return ((HttpServletRequest) request).isRequestedSessionIdValid();
    }


    public boolean isRequestedSessionIdFromCookie() {
        return ((HttpServletRequest) request).isRequestedSessionIdFromCookie();
    }


    public boolean isRequestedSessionIdFromURL() {
        return ((HttpServletRequest) request).isRequestedSessionIdFromURL();
    }


    public boolean isRequestedSessionIdFromUrl() {
        return ((HttpServletRequest) request).isRequestedSessionIdFromURL();
    }


}