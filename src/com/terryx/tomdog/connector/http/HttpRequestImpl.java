package com.terryx.tomdog.connector.http;

import com.terryx.tomdog.connector.HttpRequestBase;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Implementation of <b>HttpRequest</b> specific to the HTTP connector.
 *
 * @author taoranxue on 7/20/17 2:56 PM.
 */
public class HttpRequestImpl extends HttpRequestBase {

    /**
     * The InetAddress of the remote client of ths request.
     */
    protected InetAddress inet = null;


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

    // ------------------------------------------------- ServletRequest Methods


    /**
     * Return the Internet Protocol (IP) address of the client that sent
     * this request.
     */
    public String getRemoteAddr() {

        return (inet.getHostAddress());

    }


    /**
     * Return the fully qualified name of the client that sent this request,
     * or the IP address of the client if the name cannot be determined.
     */
    public String getRemoteHost() {

        if (connector.getEnableLookups())
            return (inet.getHostName());
        else
            return (getRemoteAddr());
    }

    /**
     * [Package Private] Return the InetAddress of the remote client of
     * this request.
     */
    InetAddress getInet() {

        return (inet);

    }

    /**
     * [Package Private] Set the InetAddress of the remote client of
     * this request.
     *
     * @param inet The new InetAddress
     */
    void setInet(InetAddress inet) {

        this.inet = inet;

    }
}
