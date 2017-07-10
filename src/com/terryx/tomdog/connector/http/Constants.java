package com.terryx.tomdog.connector.http;

import java.io.File;

/**
 * @author taoranxue on 7/9/17 7:59 PM.
 */
public class Constants {
    public static final String WEB_ROOT =
            System.getProperty("user.dir") + File.separator + "webroot";
    public static final String Package = "com.terryx.tomdog.connector.http";
    public static final int DEFAULT_CONNECTION_TIMEOUT = 60000;
    public static final int PROCESSOR_IDLE = 0;
    public static final int PROCESSOR_ACTIVE = 1;
}
