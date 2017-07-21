package com.terryx.tomdog.net;

import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * @author taoranxue on 7/21/17 9:51 AM.
 */
public final class DefaultServerSocketFactory implements ServerSocketFactory {
    @Override
    public ServerSocket createSocket(int port) throws Exception {
        return (new ServerSocket(port));
    }

    @Override
    public ServerSocket createSocket(int port, int backlog) throws Exception {
        return (new ServerSocket(port, backlog));
    }

    @Override
    public ServerSocket createSocket(int port, int backlog, InetAddress ifAddress) throws Exception {
        return (new ServerSocket(port, backlog, ifAddress));
    }
}
