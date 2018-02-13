package org.apache.http.client.cache;

import java.io.IOException;

public class HttpCacheEntrySerializationException extends IOException {
    private static final long serialVersionUID = 9219188365878433519L;

    public HttpCacheEntrySerializationException(String str) {
    }

    public HttpCacheEntrySerializationException(String str, Throwable th) {
        super(str);
        initCause(th);
    }
}
