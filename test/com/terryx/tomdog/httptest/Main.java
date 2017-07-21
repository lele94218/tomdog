package com.terryx.tomdog.httptest;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author taoranxue on 7/20/17 3:52 PM.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CloseableHttpClient httpclient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet("http://localhost:8080/servlet/ModernServlet");
                        CloseableHttpResponse response1 = httpclient.execute(httpGet);
                        try {
                            System.out.println(response1.getStatusLine());
//                            HttpEntity entity1 = response1.getEntity();
//                            System.out.println(EntityUtils.toString(entity1));
//                            EntityUtils.consume(entity1);
                        } finally {
                            response1.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Thread.sleep(100);
        }
    }
}
