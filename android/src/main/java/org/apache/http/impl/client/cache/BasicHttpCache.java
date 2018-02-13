package org.apache.http.impl.client.cache;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.cache.HttpCacheUpdateCallback;
import org.apache.http.client.cache.Resource;
import org.apache.http.client.cache.ResourceFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHttpResponse;
import org.mortbay.jetty.HttpStatus;

class BasicHttpCache implements HttpCache {
    private final CacheEntryUpdater cacheEntryUpdater;
    private final CacheInvalidator cacheInvalidator;
    private final Log log;
    private final long maxObjectSizeBytes;
    private final ResourceFactory resourceFactory;
    private final CachedHttpResponseGenerator responseGenerator;
    private final HttpCacheStorage storage;
    private final CacheKeyGenerator uriExtractor;

    public BasicHttpCache() {
        this(new CacheConfig());
    }

    public BasicHttpCache(ResourceFactory resourceFactory, HttpCacheStorage httpCacheStorage, CacheConfig cacheConfig) {
        this.log = LogFactory.getLog(getClass());
        this.resourceFactory = resourceFactory;
        this.uriExtractor = new CacheKeyGenerator();
        this.cacheEntryUpdater = new CacheEntryUpdater(resourceFactory);
        this.maxObjectSizeBytes = cacheConfig.getMaxObjectSize();
        this.responseGenerator = new CachedHttpResponseGenerator();
        this.storage = httpCacheStorage;
        this.cacheInvalidator = new CacheInvalidator(this.uriExtractor, this.storage);
    }

    public BasicHttpCache(CacheConfig cacheConfig) {
        this(new HeapResourceFactory(), new BasicHttpCacheStorage(cacheConfig), cacheConfig);
    }

    private void addVariantWithEtag(String str, String str2, Map<String, Variant> map) throws IOException {
        HttpCacheEntry entry = this.storage.getEntry(str2);
        if (entry != null) {
            Header firstHeader = entry.getFirstHeader("ETag");
            if (firstHeader != null) {
                map.put(firstHeader.getValue(), new Variant(str, str2, entry));
            }
        }
    }

    public HttpResponse cacheAndReturnResponse(HttpHost httpHost, HttpRequest httpRequest, HttpResponse httpResponse, Date date, Date date2) throws IOException {
        SizeLimitedResponseReader responseReader = getResponseReader(httpRequest, httpResponse);
        responseReader.readResponse();
        if (responseReader.isLimitReached()) {
            return responseReader.getReconstructedResponse();
        }
        Resource resource = responseReader.getResource();
        if (isIncompleteResponse(httpResponse, resource)) {
            return generateIncompleteResponseError(httpResponse, resource);
        }
        HttpCacheEntry httpCacheEntry = new HttpCacheEntry(date, date2, httpResponse.getStatusLine(), httpResponse.getAllHeaders(), resource);
        storeInCache(httpHost, httpRequest, httpCacheEntry);
        return this.responseGenerator.generateResponse(httpCacheEntry);
    }

    HttpCacheEntry doGetUpdatedParentEntry(String str, HttpCacheEntry httpCacheEntry, HttpCacheEntry httpCacheEntry2, String str2, String str3) throws IOException {
        if (httpCacheEntry != null) {
            httpCacheEntry2 = httpCacheEntry;
        }
        Resource copy = this.resourceFactory.copy(str, httpCacheEntry2.getResource());
        Map hashMap = new HashMap(httpCacheEntry2.getVariantMap());
        hashMap.put(str2, str3);
        return new HttpCacheEntry(httpCacheEntry2.getRequestDate(), httpCacheEntry2.getResponseDate(), httpCacheEntry2.getStatusLine(), httpCacheEntry2.getAllHeaders(), copy, hashMap);
    }

    public void flushCacheEntriesFor(HttpHost httpHost, HttpRequest httpRequest) throws IOException {
        this.storage.removeEntry(this.uriExtractor.getURI(httpHost, httpRequest));
    }

    public void flushInvalidatedCacheEntriesFor(HttpHost httpHost, HttpRequest httpRequest) throws IOException {
        this.cacheInvalidator.flushInvalidatedCacheEntries(httpHost, httpRequest);
    }

    public void flushInvalidatedCacheEntriesFor(HttpHost httpHost, HttpRequest httpRequest, HttpResponse httpResponse) {
        this.cacheInvalidator.flushInvalidatedCacheEntries(httpHost, httpRequest, httpResponse);
    }

    HttpResponse generateIncompleteResponseError(HttpResponse httpResponse, Resource resource) {
        int parseInt = Integer.parseInt(httpResponse.getFirstHeader("Content-Length").getValue());
        HttpResponse basicHttpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1, 502, HttpStatus.Bad_Gateway);
        basicHttpResponse.setHeader("Content-Type", "text/plain;charset=UTF-8");
        byte[] bytes = String.format("Received incomplete response with Content-Length %d but actual body length %d", new Object[]{Integer.valueOf(parseInt), Long.valueOf(resource.length())}).getBytes();
        basicHttpResponse.setHeader("Content-Length", Integer.toString(bytes.length));
        basicHttpResponse.setEntity(new ByteArrayEntity(bytes));
        return basicHttpResponse;
    }

    public HttpCacheEntry getCacheEntry(HttpHost httpHost, HttpRequest httpRequest) throws IOException {
        HttpCacheEntry entry = this.storage.getEntry(this.uriExtractor.getURI(httpHost, httpRequest));
        if (entry == null) {
            entry = null;
        } else if (entry.hasVariants()) {
            String str = (String) entry.getVariantMap().get(this.uriExtractor.getVariantKey(httpRequest, entry));
            return str != null ? this.storage.getEntry(str) : null;
        }
        return entry;
    }

    SizeLimitedResponseReader getResponseReader(HttpRequest httpRequest, HttpResponse httpResponse) {
        return new SizeLimitedResponseReader(this.resourceFactory, this.maxObjectSizeBytes, httpRequest, httpResponse);
    }

    public Map<String, Variant> getVariantCacheEntriesWithEtags(HttpHost httpHost, HttpRequest httpRequest) throws IOException {
        Map<String, Variant> hashMap = new HashMap();
        HttpCacheEntry entry = this.storage.getEntry(this.uriExtractor.getURI(httpHost, httpRequest));
        if (entry != null && entry.hasVariants()) {
            for (Entry entry2 : entry.getVariantMap().entrySet()) {
                addVariantWithEtag((String) entry2.getKey(), (String) entry2.getValue(), hashMap);
            }
        }
        return hashMap;
    }

    boolean isIncompleteResponse(HttpResponse httpResponse, Resource resource) {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode != 200 && statusCode != 206) {
            return false;
        }
        Header firstHeader = httpResponse.getFirstHeader("Content-Length");
        if (firstHeader == null) {
            return false;
        }
        try {
            return resource.length() < ((long) Integer.parseInt(firstHeader.getValue()));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void reuseVariantEntryFor(HttpHost httpHost, HttpRequest httpRequest, Variant variant) throws IOException {
        String uri = this.uriExtractor.getURI(httpHost, httpRequest);
        final HttpCacheEntry entry = variant.getEntry();
        final String variantKey = this.uriExtractor.getVariantKey(httpRequest, entry);
        final String cacheKey = variant.getCacheKey();
        final HttpRequest httpRequest2 = httpRequest;
        try {
            this.storage.updateEntry(uri, new HttpCacheUpdateCallback() {
                public HttpCacheEntry update(HttpCacheEntry httpCacheEntry) throws IOException {
                    return BasicHttpCache.this.doGetUpdatedParentEntry(httpRequest2.getRequestLine().getUri(), httpCacheEntry, entry, variantKey, cacheKey);
                }
            });
        } catch (Throwable e) {
            this.log.warn("Could not update key [" + uri + "]", e);
        }
    }

    void storeInCache(HttpHost httpHost, HttpRequest httpRequest, HttpCacheEntry httpCacheEntry) throws IOException {
        if (httpCacheEntry.hasVariants()) {
            storeVariantEntry(httpHost, httpRequest, httpCacheEntry);
        } else {
            storeNonVariantEntry(httpHost, httpRequest, httpCacheEntry);
        }
    }

    void storeNonVariantEntry(HttpHost httpHost, HttpRequest httpRequest, HttpCacheEntry httpCacheEntry) throws IOException {
        this.storage.putEntry(this.uriExtractor.getURI(httpHost, httpRequest), httpCacheEntry);
    }

    void storeVariantEntry(HttpHost httpHost, final HttpRequest httpRequest, final HttpCacheEntry httpCacheEntry) throws IOException {
        String uri = this.uriExtractor.getURI(httpHost, httpRequest);
        final String variantURI = this.uriExtractor.getVariantURI(httpHost, httpRequest, httpCacheEntry);
        this.storage.putEntry(variantURI, httpCacheEntry);
        try {
            this.storage.updateEntry(uri, new HttpCacheUpdateCallback() {
                public HttpCacheEntry update(HttpCacheEntry httpCacheEntry) throws IOException {
                    return BasicHttpCache.this.doGetUpdatedParentEntry(httpRequest.getRequestLine().getUri(), httpCacheEntry, httpCacheEntry, BasicHttpCache.this.uriExtractor.getVariantKey(httpRequest, httpCacheEntry), variantURI);
                }
            });
        } catch (Throwable e) {
            this.log.warn("Could not update key [" + uri + "]", e);
        }
    }

    public HttpCacheEntry updateCacheEntry(HttpHost httpHost, HttpRequest httpRequest, HttpCacheEntry httpCacheEntry, HttpResponse httpResponse, Date date, Date date2) throws IOException {
        HttpCacheEntry updateCacheEntry = this.cacheEntryUpdater.updateCacheEntry(httpRequest.getRequestLine().getUri(), httpCacheEntry, date, date2, httpResponse);
        storeInCache(httpHost, httpRequest, updateCacheEntry);
        return updateCacheEntry;
    }

    public HttpCacheEntry updateVariantCacheEntry(HttpHost httpHost, HttpRequest httpRequest, HttpCacheEntry httpCacheEntry, HttpResponse httpResponse, Date date, Date date2, String str) throws IOException {
        HttpCacheEntry updateCacheEntry = this.cacheEntryUpdater.updateCacheEntry(httpRequest.getRequestLine().getUri(), httpCacheEntry, date, date2, httpResponse);
        this.storage.putEntry(str, updateCacheEntry);
        return updateCacheEntry;
    }
}
