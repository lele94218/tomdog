package com.terryx.tomdog.connector.http;


import com.terryx.tomdog.connector.HttpResponseBase;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author taoranxue on 7/20/17 2:57 PM.
 */
public class HttpResponseImpl extends HttpResponseBase {

    /**
     * Associated HTTP response stream.
     */
    protected HttpResponseStream responseStream;


    /**
     * True if chunking is allowed.
     */
    protected boolean allowChunking;

    /**
     * True if chunking is allowed.
     */
    public boolean isChunkingAllowed() {
        return allowChunking;
    }


    /**
     * Set the chunking flag.
     */
    void setAllowChunking(boolean allowChunking) {
        this.allowChunking = allowChunking;
    }

    /**
     * Tests is the connection will be closed after the processing of the
     * request.
     */
    public boolean isCloseConnection() {
        String connectionValue = (String) getHeader("Connection");
        return (connectionValue != null
                && connectionValue.equals("close"));
    }

    /**
     * Removes the specified header.
     *
     * @param name Name of the header to remove
     * @param value Value to remove
     */
    public void removeHeader(String name, String value) {

        if (isCommitted())
            return;

        if (included)
            return;     // Ignore any call from an included servlet

        synchronized (headers) {
            ArrayList values = (ArrayList) headers.get(name);
            if ((values != null) && (!values.isEmpty())) {
                values.remove(value);
                if (values.isEmpty())
                    headers.remove(name);
            }
        }

    }

    /**
     * Perform whatever actions are required to flush and close the output
     * stream or writer, in a single operation.
     *
     * @exception IOException if an input/output error occurs
     */
    public void finishResponse() throws IOException {

        if (getStatus() < HttpServletResponse.SC_BAD_REQUEST) {
            if ((!isStreamInitialized()) && (getContentLength() == -1)
                    && (getStatus() >= 200)
                    && (getStatus() != SC_NOT_MODIFIED)
                    && (getStatus() != SC_NO_CONTENT))
                setContentLength(0);
        } else {
            setHeader("Connection", "close");
        }
        super.finishResponse();

    }

    /**
     * Has stream been created ?
     */
    public boolean isStreamInitialized() {
        return (responseStream != null);
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

        if (responseStream != null)
            responseStream.checkChunking(this);

    }

    /**
     * Create and return a ServletOutputStream to write the content
     * associated with this Response.
     *
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream createOutputStream() throws IOException {
        responseStream = new HttpResponseStream(this);
        return (responseStream);

    }
}
