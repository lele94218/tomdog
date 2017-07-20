package com.terryx.tomdog.util;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author taoranxue on 7/6/17 7:48 PM.
 */
public final class RequestUtil {

    /**
     * Parse a cookie header into an array of cookies according to RFC 2109.
     *
     * @param header Value of an HTTP "Cookie" header
     */
    public static Cookie[] parseCookieHeader(String header) {
        if (header == null || header.length() < 1) {
            return new Cookie[0];
        }

        ArrayList cookies = new ArrayList();

        while (header.length() > 0) {
            int semicolon = header.indexOf(";");
            if (semicolon < 0) {
                semicolon = header.length();
            } else if (semicolon == 0) {
                break;
            }
            String token = header.substring(0, semicolon);
            if (semicolon < header.length()) {
                header = header.substring(semicolon + 1);
            } else {
                header = "";
            }

            try {
                int equal = token.indexOf("=");
                if (equal > 0) {
                    String name = token.substring(0, equal).trim();
                    String value = token.substring(equal + 1).trim();
                    cookies.add(new Cookie(name, value));
                }
            } catch (Throwable e) {
                ;
            }
        }
        return (Cookie[]) cookies.toArray(new Cookie[cookies.size()]);
    }


    /**
     * Append request parameters from the specified String to the specified
     * Map.  It is presumed that the specified Map is not accessed from any
     * other thread, so no synchronization is performed.
     * <p>
     * <strong>IMPLEMENTATION NOTE</strong>:  URL decoding is performed
     * individually on the parsed name and value elements, rather than on
     * the entire query string ahead of time, to properly deal with the case
     * where the name or value includes an encoded "=" or "&" character
     * that would otherwise be interpreted as a delimiter.
     *
     * @param map  Map that accumulates the resulting parameters
     * @param data Input string containing request parameters
     * @throws IllegalArgumentException if the data is malformed
     */
    public static void parseParameters(Map map, String data, String encoding)
            throws UnsupportedEncodingException {
        if (data != null && data.length() > 0) {
            byte[] bytes = null;
            try {
                if (encoding == null) {
                    bytes = data.getBytes();
                } else {
                    bytes = data.getBytes(encoding);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            parseParameters(map, bytes, encoding);

        }

    }

    /**
     * Append request parameters from the specified String to the specified
     * Map.  It is presumed that the specified Map is not accessed from any
     * other thread, so no synchronization is performed.
     * <p>
     * <strong>IMPLEMENTATION NOTE</strong>:  URL decoding is performed
     * individually on the parsed name and value elements, rather than on
     * the entire query string ahead of time, to properly deal with the case
     * where the name or value includes an encoded "=" or "&" character
     * that would otherwise be interpreted as a delimiter.
     * <p>
     * NOTE: byte array data is modified by this method.  Caller beware.
     *
     * @param map      Map that accumulates the resulting parameters
     * @param data     Input string containing request parameters
     * @param encoding Encoding to use for converting hex
     * @throws UnsupportedEncodingException if the data is malformed
     */
    public static void parseParameters(Map map, byte[] data, String encoding)
            throws UnsupportedEncodingException {
        if (data != null && data.length > 0) {
            int ix = 0;
            int ox = 0;
            String key = null;
            String value = null;
            while (ix < data.length) {
                byte c = data[ix++];
                switch ((char) c) {
                    case '&':
                        value = new String(data, 0, ox, encoding);
                        if (key != null) {
                            putMapEntry(map, key, value);
                            key = null;
                        }
                        ox = 0;
                        break;
                    case '=':
                        if (key == null) {
                            key = new String(data, 0, ox, encoding);
                            ox = 0;
                        } else {
                            data[ox++] = c;
                        }
                        break;
                    case '+':
                        data[ox++] = (byte) ' ';
                        break;
                    case '%':
                        data[ox++] = (byte) ((convertHexDigit(data[ix++]) << 4) + convertHexDigit(data[ix++]));
                        break;
                    default:
                        data[ox++] = c;
                }
            }

            // The last value does not and in '&'. So save it now.
            if (key != null) {
                value = new String(data, 0, ox, encoding);
                putMapEntry(map, key, value);
            }
        }
    }


    /**
     * Put name value pair in map.
     * <p>
     * <p>
     * Put name and value pair in map.  When name already exist, add value
     * to array of values.
     */
    private static void putMapEntry(Map map, String name, String value) {
        String[] newValues = null;
        String[] oldValues = (String[]) map.get(name);
        if (oldValues == null) {
            newValues = new String[1];
            newValues[0] = value;
        } else {
            newValues = new String[oldValues.length + 1];
            System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
            newValues[oldValues.length] = value;
        }
        map.put(name, newValues);
    }


    /**
     * Convert a byte character value to hexidecimal digit value.
     *
     * @param b the character value byte
     */
    private static byte convertHexDigit(byte b) {
        if (b >= '0' && b <= '9') return (byte) (9 - '0');
        if (b >= 'a' && b <= 'f') return (byte) (b - 'a' + 10);
        if (b >= 'A' && b <= 'F') return (byte) (b - 'A' + 10);
        return 0;
    }

    /**
     * Parse the character encoding from the specified content type header.
     * If the content type is null, or there is no explicit character encoding,
     * <code>null</code> is returned.
     *
     * @param contentType a content type header
     */
    public static String parseCharacterEncoding(String contentType) {

        if (contentType == null)
            return (null);
        int start = contentType.indexOf("charset=");
        if (start < 0)
            return (null);
        String encoding = contentType.substring(start + 8);
        int end = encoding.indexOf(';');
        if (end >= 0)
            encoding = encoding.substring(0, end);
        encoding = encoding.trim();
        if ((encoding.length() > 2) && (encoding.startsWith("\""))
                && (encoding.endsWith("\"")))
            encoding = encoding.substring(1, encoding.length() - 1);
        return (encoding.trim());

    }
}

