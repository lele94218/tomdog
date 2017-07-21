package com.terryx.tomdog.net;

import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Interface that describes the common characteristics of factory classes
 * that create server sockets which may be required by a Connector.  A concrete
 * implementation of this interface will be assigned to a Connector
 * via the <code>setFactory()</code> method.
 *
 * @author taoranxue on 7/21/17 9:46 AM.
 */
public interface ServerSocketFactory {
    /**
     * Returns a server socket which uses all network interfaces on
     * the host, and is bound to a the specified port.  The socket is
     * configured with the socket options (such as accept timeout)
     * given to this factory.
     *
     * @param port the port to listen to
     * @throws Exception
     */
    public ServerSocket createSocket(int port)
            throws Exception;

    /**
     * Returns a server socket which uses all network interfaces on
     * the host, is bound to a the specified port, and uses the
     * specified connection backlog.  The socket is configured with
     * the socket options (such as accept timeout) given to this factory.
     *
     * @param port    the port to listen to
     * @param backlog how many connections are queued
     * @throws Exception
     */
    public ServerSocket createSocket(int port, int backlog)
            throws Exception;

    /**
     * Returns a server socket which uses only the specified network
     * interface on the local host, is bound to a the specified port,
     * and uses the specified connection backlog.  The socket is configured
     * with the socket options (such as accept timeout) given to this factory.
     *
     * @param port      the port to listen to
     * @param backlog   how many connections are queued
     * @param ifAddress the network interface address to use
     * @throws Exception
     */
    public ServerSocket createSocket(int port, int backlog,
                                     InetAddress ifAddress)
            throws Exception;
}
