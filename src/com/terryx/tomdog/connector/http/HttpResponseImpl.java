package com.terryx.tomdog.connector.http;


import com.terryx.tomdog.connector.HttpResponseBase;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author taoranxue on 7/20/17 2:57 PM.
 */
public class HttpResponseImpl extends HttpResponseBase {

//    /**
//     * Associated HTTP response stream.
//     */
//    protected HttpResponseStream responseStream;

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
//        return (responseStream != null);
        return false;
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

//        if (responseStream != null)
//            responseStream.checkChunking(this);

    }
}
