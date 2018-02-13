package org.apache.http.impl.client.cache;

import java.util.Date;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.mortbay.jetty.HttpStatus;
import org.mortbay.jetty.HttpVersions;

@Immutable
class CachedHttpResponseGenerator {
    private final CacheValidityPolicy validityStrategy;

    CachedHttpResponseGenerator() {
        this(new CacheValidityPolicy());
    }

    CachedHttpResponseGenerator(CacheValidityPolicy cacheValidityPolicy) {
        this.validityStrategy = cacheValidityPolicy;
    }

    private void addMissingContentLengthHeader(HttpResponse httpResponse, HttpEntity httpEntity) {
        if (!transferEncodingIsPresent(httpResponse) && httpResponse.getFirstHeader("Content-Length") == null) {
            httpResponse.setHeader(new BasicHeader("Content-Length", Long.toString(httpEntity.getContentLength())));
        }
    }

    private boolean transferEncodingIsPresent(HttpResponse httpResponse) {
        return httpResponse.getFirstHeader("Transfer-Encoding") != null;
    }

    HttpResponse generateNotModifiedResponse(HttpCacheEntry httpCacheEntry) {
        HttpResponse basicHttpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1, 304, HttpStatus.Not_Modified);
        Header firstHeader = httpCacheEntry.getFirstHeader("Date");
        if (firstHeader == null) {
            firstHeader = new BasicHeader("Date", DateUtils.formatDate(new Date()));
        }
        basicHttpResponse.addHeader(firstHeader);
        firstHeader = httpCacheEntry.getFirstHeader("ETag");
        if (firstHeader != null) {
            basicHttpResponse.addHeader(firstHeader);
        }
        firstHeader = httpCacheEntry.getFirstHeader("Content-Location");
        if (firstHeader != null) {
            basicHttpResponse.addHeader(firstHeader);
        }
        firstHeader = httpCacheEntry.getFirstHeader("Expires");
        if (firstHeader != null) {
            basicHttpResponse.addHeader(firstHeader);
        }
        firstHeader = httpCacheEntry.getFirstHeader("Cache-Control");
        if (firstHeader != null) {
            basicHttpResponse.addHeader(firstHeader);
        }
        firstHeader = httpCacheEntry.getFirstHeader("Vary");
        if (firstHeader != null) {
            basicHttpResponse.addHeader(firstHeader);
        }
        return basicHttpResponse;
    }

    HttpResponse generateResponse(HttpCacheEntry httpCacheEntry) {
        Date date = new Date();
        HttpResponse basicHttpResponse = new BasicHttpResponse(HttpVersion.HTTP_1_1, httpCacheEntry.getStatusCode(), httpCacheEntry.getReasonPhrase());
        HttpEntity cacheEntity = new CacheEntity(httpCacheEntry);
        basicHttpResponse.setHeaders(httpCacheEntry.getAllHeaders());
        addMissingContentLengthHeader(basicHttpResponse, cacheEntity);
        basicHttpResponse.setEntity(cacheEntity);
        long currentAgeSecs = this.validityStrategy.getCurrentAgeSecs(httpCacheEntry, date);
        if (currentAgeSecs > 0) {
            if (currentAgeSecs >= 2147483647L) {
                basicHttpResponse.setHeader("Age", "2147483648");
            } else {
                basicHttpResponse.setHeader("Age", HttpVersions.HTTP_0_9 + ((int) currentAgeSecs));
            }
        }
        return basicHttpResponse;
    }
}
