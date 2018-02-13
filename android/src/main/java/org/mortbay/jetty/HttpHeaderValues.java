package org.mortbay.jetty;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import org.mortbay.io.Buffer;
import org.mortbay.io.BufferCache;
import org.mortbay.log.Log;

public class HttpHeaderValues extends BufferCache {
    public static final String BYTES = "bytes";
    public static final Buffer BYTES_BUFFER = CACHE.add(BYTES, 9);
    public static final int BYTES_ORDINAL = 9;
    public static final HttpHeaderValues CACHE = new HttpHeaderValues();
    public static final String CHUNKED = "chunked";
    public static final Buffer CHUNKED_BUFFER = CACHE.add("chunked", 2);
    public static final int CHUNKED_ORDINAL = 2;
    public static final String CLOSE = "close";
    public static final Buffer CLOSE_BUFFER = CACHE.add(CLOSE, 1);
    public static final int CLOSE_ORDINAL = 1;
    public static final String CONTINUE = "100-continue";
    public static final Buffer CONTINUE_BUFFER = CACHE.add("100-continue", 6);
    public static final int CONTINUE_ORDINAL = 6;
    public static final String GZIP = "gzip";
    public static final Buffer GZIP_BUFFER = CACHE.add(GZIP, 3);
    public static final int GZIP_ORDINAL = 3;
    public static final String IDENTITY = "identity";
    public static final Buffer IDENTITY_BUFFER = CACHE.add("identity", 4);
    public static final int IDENTITY_ORDINAL = 4;
    public static final String KEEP_ALIVE = "keep-alive";
    public static final Buffer KEEP_ALIVE_BUFFER = CACHE.add(KEEP_ALIVE, 5);
    public static final int KEEP_ALIVE_ORDINAL = 5;
    public static final String NO_CACHE = "no-cache";
    public static final Buffer NO_CACHE_BUFFER = CACHE.add("no-cache", 10);
    public static final int NO_CACHE_ORDINAL = 10;
    public static final String PROCESSING = "102-processing";
    public static final Buffer PROCESSING_BUFFER = CACHE.add(PROCESSING, 7);
    public static final int PROCESSING_ORDINAL = 7;
    public static final String TE = "TE";
    public static final Buffer TE_BUFFER = CACHE.add("TE", 8);
    public static final int TE_ORDINAL = 8;
    static Class class$org$mortbay$jetty$HttpHeaderValues;

    static {
        Throwable e;
        CACHE.add(GZIP, 100);
        CACHE.add("gzip,deflate", 101);
        int i = 103;
        CACHE.add("deflate", 102);
        try {
            Class class$;
            if (class$org$mortbay$jetty$HttpHeaderValues == null) {
                class$ = class$("org.mortbay.jetty.HttpHeaderValues");
                class$org$mortbay$jetty$HttpHeaderValues = class$;
            } else {
                class$ = class$org$mortbay$jetty$HttpHeaderValues;
            }
            InputStream resourceAsStream = class$.getResourceAsStream("/org/mortbay/jetty/useragents");
            if (resourceAsStream != null) {
                LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(resourceAsStream));
                String readLine = lineNumberReader.readLine();
                while (readLine != null) {
                    try {
                        CACHE.add(readLine, i);
                        readLine = lineNumberReader.readLine();
                        i++;
                    } catch (Exception e2) {
                        e = e2;
                    }
                }
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            Log.ignore(e);
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }
}
