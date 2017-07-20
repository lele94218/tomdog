package com.terryx.tomdog;

import com.terryx.tomdog.connector.http.OldHttpRequest;
import com.terryx.tomdog.connector.http.OldHttpResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author taoranxue on 6/27/17 4:13 PM.
 * @deprecated
 */
public class HttpServer {
    public static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
    private boolean shutdown = false;

    public void await() {
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
            InputStream input = null;
            OutputStream output = null;
            try {
                socket = serverSocket.accept();
                input = socket.getInputStream();
                output = socket.getOutputStream();

                OldHttpRequest httpRequest = new OldHttpRequest(input);
                httpRequest.parse();

                OldHttpResponse httpResponse = new OldHttpResponse(output);
                httpResponse.setRequest(httpRequest);


                /* Check if it is a servlet or static resources */
                if (httpRequest.getUri().startsWith("/servlet/")) {
                    ServletProcessor processor = new ServletProcessor();
//                    processor.process(httpRequest, httpResponse);
                } else {
                    StaticResourceProcessor processor = new StaticResourceProcessor();
//                    processor.process(httpRequest, httpResponse);
                }
//                httpResponse.sendStaticResource();

                socket.close();
                shutdown = httpRequest.getUri().equals(SHUTDOWN_COMMAND);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public static void main(String args[]) {
        HttpServer server = new HttpServer();
        server.await();
    }
}
