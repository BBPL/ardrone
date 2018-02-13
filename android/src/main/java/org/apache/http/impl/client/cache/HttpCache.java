package org.apache.http.impl.client.cache;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.cache.HttpCacheEntry;

interface HttpCache {
    HttpResponse cacheAndReturnResponse(HttpHost httpHost, HttpRequest httpRequest, HttpResponse httpResponse, Date date, Date date2) throws IOException;

    void flushCacheEntriesFor(HttpHost httpHost, HttpRequest httpRequest) throws IOException;

    void flushInvalidatedCacheEntriesFor(HttpHost httpHost, HttpRequest httpRequest) throws IOException;

    void flushInvalidatedCacheEntriesFor(HttpHost httpHost, HttpRequest httpRequest, HttpResponse httpResponse);

    HttpCacheEntry getCacheEntry(HttpHost httpHost, HttpRequest httpRequest) throws IOException;

    Map<String, Variant> getVariantCacheEntriesWithEtags(HttpHost httpHost, HttpRequest httpRequest) throws IOException;

    void reuseVariantEntryFor(HttpHost httpHost, HttpRequest httpRequest, Variant variant) throws IOException;

    HttpCacheEntry updateCacheEntry(HttpHost httpHost, HttpRequest httpRequest, HttpCacheEntry httpCacheEntry, HttpResponse httpResponse, Date date, Date date2) throws IOException;

    HttpCacheEntry updateVariantCacheEntry(HttpHost httpHost, HttpRequest httpRequest, HttpCacheEntry httpCacheEntry, HttpResponse httpResponse, Date date, Date date2, String str) throws IOException;
}
