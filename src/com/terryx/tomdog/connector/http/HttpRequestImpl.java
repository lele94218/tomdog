package com.terryx.tomdog.connector.http;

import com.terryx.tomdog.connector.HttpRequestBase;

import java.io.IOException;

/**
 * Implementation of <b>HttpRequest</b> specific to the HTTP connector.
 *
 * @author taoranxue on 7/20/17 2:56 PM.
 */
public class HttpRequestImpl extends HttpRequestBase {
    /**
     * Perform whatever actions are required to flush and close the input
     * stream or reader, in a single operation.
     *
     * @exception IOException if an input/output error occurs
     */
    public void finishRequest() throws IOException {

        // If neither a reader or an is have been opened, do it to consume
        // request bytes, if any
        if ((reader == null) && (stream == null) && (getContentLength() != 0)
                && (getProtocol() != null) && (getProtocol().equals("HTTP/1.1")))
            getInputStream();

        super.finishRequest();

    }
}
