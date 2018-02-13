package org.apache.http.impl.client.cache;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

@ThreadSafe
class CacheInvalidator {
    private final CacheKeyGenerator cacheKeyGenerator;
    private final Log log = LogFactory.getLog(getClass());
    private final HttpCacheStorage storage;

    public CacheInvalidator(CacheKeyGenerator cacheKeyGenerator, HttpCacheStorage httpCacheStorage) {
        this.cacheKeyGenerator = cacheKeyGenerator;
        this.storage = httpCacheStorage;
    }

    private void flushEntry(String str) {
        try {
            this.storage.removeEntry(str);
        } catch (Throwable e) {
            this.log.warn("unable to flush cache entry", e);
        }
    }

    private URL getAbsoluteURL(String str) {
        try {
            return new URL(str);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private URL getContentLocationURL(URL url, HttpResponse httpResponse) {
        Header firstHeader = httpResponse.getFirstHeader("Content-Location");
        if (firstHeader == null) {
            return null;
        }
        String value = firstHeader.getValue();
        URL absoluteURL = getAbsoluteURL(value);
        return absoluteURL == null ? getRelativeURL(url, value) : absoluteURL;
    }

    private HttpCacheEntry getEntry(String str) {
        try {
            return this.storage.getEntry(str);
        } catch (Throwable e) {
            this.log.warn("could not retrieve entry from storage", e);
            return null;
        }
    }

    private URL getRelativeURL(URL url, String str) {
        try {
            return new URL(url, str);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private boolean notGetOrHeadRequest(String str) {
        return ("GET".equals(str) || "HEAD".equals(str)) ? false : true;
    }

    private boolean responseAndEntryEtagsDiffer(HttpResponse httpResponse, HttpCacheEntry httpCacheEntry) {
        Header firstHeader = httpCacheEntry.getFirstHeader("ETag");
        Header firstHeader2 = httpResponse.getFirstHeader("ETag");
        return (firstHeader == null || firstHeader2 == null || firstHeader.getValue().equals(firstHeader2.getValue())) ? false : true;
    }

    private boolean responseDateOlderThanEntryDate(HttpResponse httpResponse, HttpCacheEntry httpCacheEntry) {
        boolean z = false;
        Header firstHeader = httpCacheEntry.getFirstHeader("Date");
        Header firstHeader2 = httpResponse.getFirstHeader("Date");
        if (!(firstHeader == null || firstHeader2 == null)) {
            try {
                z = DateUtils.parseDate(firstHeader2.getValue()).before(DateUtils.parseDate(firstHeader.getValue()));
            } catch (DateParseException e) {
            }
        }
        return z;
    }

    protected boolean flushAbsoluteUriFromSameHost(URL url, String str) {
        URL absoluteURL = getAbsoluteURL(str);
        if (absoluteURL == null) {
            return false;
        }
        flushUriIfSameHost(url, absoluteURL);
        return true;
    }

    public void flushInvalidatedCacheEntries(HttpHost httpHost, HttpRequest httpRequest) {
        if (requestShouldNotBeCached(httpRequest)) {
            this.log.debug("Request should not be cached");
            String uri = this.cacheKeyGenerator.getURI(httpHost, httpRequest);
            HttpCacheEntry entry = getEntry(uri);
            this.log.debug("parent entry: " + entry);
            if (entry != null) {
                for (String flushEntry : entry.getVariantMap().values()) {
                    flushEntry(flushEntry);
                }
                flushEntry(uri);
            }
            URL absoluteURL = getAbsoluteURL(uri);
            if (absoluteURL == null) {
                this.log.error("Couldn't transform request into valid URL");
                return;
            }
            Header firstHeader = httpRequest.getFirstHeader("Content-Location");
            if (firstHeader != null) {
                uri = firstHeader.getValue();
                if (!flushAbsoluteUriFromSameHost(absoluteURL, uri)) {
                    flushRelativeUriFromSameHost(absoluteURL, uri);
                }
            }
            firstHeader = httpRequest.getFirstHeader("Location");
            if (firstHeader != null) {
                flushAbsoluteUriFromSameHost(absoluteURL, firstHeader.getValue());
            }
        }
    }

    public void flushInvalidatedCacheEntries(HttpHost httpHost, HttpRequest httpRequest, HttpResponse httpResponse) {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode <= 299) {
            URL absoluteURL = getAbsoluteURL(this.cacheKeyGenerator.getURI(httpHost, httpRequest));
            if (absoluteURL != null) {
                URL contentLocationURL = getContentLocationURL(absoluteURL, httpResponse);
                if (contentLocationURL != null) {
                    HttpCacheEntry entry = getEntry(this.cacheKeyGenerator.canonicalizeUri(contentLocationURL.toString()));
                    if (entry != null && !responseDateOlderThanEntryDate(httpResponse, entry) && responseAndEntryEtagsDiffer(httpResponse, entry)) {
                        flushUriIfSameHost(absoluteURL, contentLocationURL);
                    }
                }
            }
        }
    }

    protected void flushRelativeUriFromSameHost(URL url, String str) {
        URL relativeURL = getRelativeURL(url, str);
        if (relativeURL != null) {
            flushUriIfSameHost(url, relativeURL);
        }
    }

    protected void flushUriIfSameHost(URL url, URL url2) {
        URL absoluteURL = getAbsoluteURL(this.cacheKeyGenerator.canonicalizeUri(url2.toString()));
        if (absoluteURL != null && absoluteURL.getAuthority().equalsIgnoreCase(url.getAuthority())) {
            flushEntry(absoluteURL.toString());
        }
    }

    protected boolean requestShouldNotBeCached(HttpRequest httpRequest) {
        return notGetOrHeadRequest(httpRequest.getRequestLine().getMethod());
    }
}
