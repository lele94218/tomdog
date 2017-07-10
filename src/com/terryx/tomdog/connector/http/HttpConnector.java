package com.terryx.tomdog.connector.http;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author taoranxue on 6/28/17 4:39 PM.
 */
public class HttpConnector implements Runnable {

    private boolean shutdown = false;
    private String scheme = "http";

    public String getScheme() {
        return scheme;
    }


    @Override
    public void run() {
        ServerSocket serverSocket = null;
        int port = 8080;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!shutdown) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            HttpProcessor processor = new HttpProcessor(this);
            processor.process(socket);
        }
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }
}
