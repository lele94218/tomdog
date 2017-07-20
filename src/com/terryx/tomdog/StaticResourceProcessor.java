package com.terryx.tomdog;


import com.terryx.tomdog.connector.http.HttpRequestImpl;
import com.terryx.tomdog.connector.http.HttpResponseImpl;

/**
 * @author taoranxue on 6/27/17 8:09 PM.
 */
public class StaticResourceProcessor {
    public void process(HttpRequestImpl request, HttpResponseImpl response) {
        try {
//            response.sendStaticResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
