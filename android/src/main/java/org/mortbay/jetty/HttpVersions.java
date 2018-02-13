package org.mortbay.jetty;

import org.mortbay.io.Buffer;
import org.mortbay.io.BufferCache;

public class HttpVersions {
    public static final BufferCache CACHE = new BufferCache();
    public static final String HTTP_0_9 = "";
    public static final Buffer HTTP_0_9_BUFFER = CACHE.add(HTTP_0_9, 9);
    public static final int HTTP_0_9_ORDINAL = 9;
    public static final String HTTP_1_0 = "HTTP/1.0";
    public static final Buffer HTTP_1_0_BUFFER = CACHE.add(HTTP_1_0, 10);
    public static final int HTTP_1_0_ORDINAL = 10;
    public static final String HTTP_1_1 = "HTTP/1.1";
    public static final Buffer HTTP_1_1_BUFFER = CACHE.add(HTTP_1_1, 11);
    public static final int HTTP_1_1_ORDINAL = 11;
}
