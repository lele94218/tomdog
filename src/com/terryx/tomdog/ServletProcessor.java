package com.terryx.tomdog;

import com.terryx.tomdog.connector.HttpRequestFacade;
import com.terryx.tomdog.connector.HttpResponseFacade;
import com.terryx.tomdog.connector.http.HttpRequestImpl;
import com.terryx.tomdog.connector.http.HttpResponseImpl;

import javax.servlet.Servlet;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * @author taoranxue on 6/27/17 8:09 PM.
 * @deprecated
 */
public class ServletProcessor {
    public void process(HttpRequestImpl httpRequest, HttpResponseImpl httpResponse) {
        String uri = httpRequest.getRequestURI();
        String servletName = uri.substring(uri.lastIndexOf('/') + 1);
        URLClassLoader loader = null;
        try {
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);
            String repository = new URL("file", null, classPath.getCanonicalPath() + File.separator).toString();
//            System.out.println("--->" + repository);
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Class myClass = null;
        try {
//            System.out.println("--->" + servletName);
            myClass = loader.loadClass(servletName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Servlet servlet = null;

        HttpRequestFacade requestWrapper = new HttpRequestFacade(httpRequest);
        HttpResponseFacade responseWrapper = new HttpResponseFacade(httpResponse);
        try {
            servlet = (Servlet) myClass.newInstance();
            servlet.service(requestWrapper, responseWrapper);
            httpResponse.finishResponse();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
