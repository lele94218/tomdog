package com.terryx.tomdog;

import com.terryx.tomdog.connector.http.HttpRequest;
import com.terryx.tomdog.connector.http.HttpResponse;

/**
 * @author taoranxue on 6/27/17 8:09 PM.
 */
public class StaticResourceProcessor {
    public void process(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            httpResponse.sendStaticResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
