package com.terryx.tomdog.core;

import com.terryx.tomdog.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author taoranxue on 8/3/17 3:02 PM.
 */
public class SimpleContextValve implements Valve, Contained {

    protected Container container;

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public void invoke(Request request, Response response, ValveContext valveContext)
            throws IOException, ServletException {
        // Validate the request and response object types
        if (!(request.getRequest() instanceof HttpServletRequest) ||
                !(response.getResponse() instanceof HttpServletResponse)) {
            return;
        }

        // Disallow any direct access to resources under WEB-INF or META-INF
        HttpServletRequest hreq = (HttpServletRequest) request.getRequest();
        String contextPath = hreq.getContextPath();
        String requestURI = ((HttpRequest) request).getDecodedRequestURI();
        String relativeURI =
                requestURI.substring(contextPath.length()).toUpperCase();

        if (relativeURI.equals("/META-INF") ||
                relativeURI.equals("/WEB-INF") ||
                relativeURI.startsWith("/META-INF/") ||
                relativeURI.startsWith("/WEB-INF/")) {
            notFound((HttpServletResponse) response.getResponse());
            return;
        }

        Context context = (Context) getContainer();

        // Select the Wrapper to be used for this Request
        Wrapper wrapper = null;
        try {
            wrapper = (Wrapper) context.map(request, true);
        } catch (IllegalArgumentException e) {
            badRequest(requestURI, (HttpServletResponse) response.getResponse());
            return;
        }
        if (wrapper == null) {
            notFound((HttpServletResponse) response.getResponse());
            return;
        }

        // Ask this Wrapper to process this Request
        response.setContext(context);
        wrapper.invoke(request, response);
    }

    // -------------------------------------------------------- Private Methods


    /**
     * Report a "bad request" error for the specified resource.  FIXME:  We
     * should really be using the error reporting settings for this web
     * application, but currently that code runs at the wrapper level rather
     * than the context level.
     *
     * @param requestURI The request URI for the requested resource
     * @param response   The response we are creating
     */
    private void badRequest(String requestURI, HttpServletResponse response) {

        try {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, requestURI);
        } catch (IllegalStateException e) {
            ;
        } catch (IOException e) {
            ;
        }

    }

    /**
     * Report a "not found" error for the specified resource.  FIXME:  We
     * should really be using the error reporting settings for this web
     * application, but currently that code runs at the wrapper level rather
     * than the context level.
     *
     * @param response The response we are creating
     */
    private void notFound(HttpServletResponse response) {

        try {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (IllegalStateException e) {
            ;
        } catch (IOException e) {
            ;
        }

    }
}
