package org.apache.http.impl.client.cache;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.protocol.HttpContext;

class AsynchronousValidationRequest implements Runnable {
    private final HttpCacheEntry cacheEntry;
    private final CachingHttpClient cachingClient;
    private final HttpContext context;
    private final String identifier;
    private final Log log = LogFactory.getLog(getClass());
    private final AsynchronousValidator parent;
    private final HttpRequest request;
    private final HttpHost target;

    AsynchronousValidationRequest(AsynchronousValidator asynchronousValidator, CachingHttpClient cachingHttpClient, HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext, HttpCacheEntry httpCacheEntry, String str) {
        this.parent = asynchronousValidator;
        this.cachingClient = cachingHttpClient;
        this.target = httpHost;
        this.request = httpRequest;
        this.context = httpContext;
        this.cacheEntry = httpCacheEntry;
        this.identifier = str;
    }

    String getIdentifier() {
        return this.identifier;
    }

    public void run() {
        try {
            this.cachingClient.revalidateCacheEntry(this.target, this.request, this.context, this.cacheEntry);
        } catch (IOException e) {
            this.log.debug("Asynchronous revalidation failed due to exception: " + e);
        } catch (ProtocolException e2) {
            this.log.error("ProtocolException thrown during asynchronous revalidation: " + e2);
        } finally {
            this.parent.markComplete(this.identifier);
        }
    }
}
