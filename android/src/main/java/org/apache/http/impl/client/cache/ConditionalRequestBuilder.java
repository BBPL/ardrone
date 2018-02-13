package org.apache.http.impl.client.cache;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpRequest;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.cache.HeaderConstants;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.impl.client.RequestWrapper;

@Immutable
class ConditionalRequestBuilder {
    private static final Log log = LogFactory.getLog(ConditionalRequestBuilder.class);

    ConditionalRequestBuilder() {
    }

    public HttpRequest buildConditionalRequest(HttpRequest httpRequest, HttpCacheEntry httpCacheEntry) throws ProtocolException {
        HttpRequest requestWrapper = new RequestWrapper(httpRequest);
        requestWrapper.resetHeaders();
        Header firstHeader = httpCacheEntry.getFirstHeader("ETag");
        if (firstHeader != null) {
            requestWrapper.setHeader("If-None-Match", firstHeader.getValue());
        }
        firstHeader = httpCacheEntry.getFirstHeader("Last-Modified");
        if (firstHeader != null) {
            requestWrapper.setHeader("If-Modified-Since", firstHeader.getValue());
        }
        Object obj = null;
        for (Header elements : httpCacheEntry.getHeaders("Cache-Control")) {
            for (HeaderElement headerElement : elements.getElements()) {
                if (HeaderConstants.CACHE_CONTROL_MUST_REVALIDATE.equalsIgnoreCase(headerElement.getName()) || HeaderConstants.CACHE_CONTROL_PROXY_REVALIDATE.equalsIgnoreCase(headerElement.getName())) {
                    obj = 1;
                    break;
                }
            }
        }
        if (obj != null) {
            requestWrapper.addHeader("Cache-Control", "max-age=0");
        }
        return requestWrapper;
    }

    public HttpRequest buildConditionalRequestFromVariants(HttpRequest httpRequest, Map<String, Variant> map) {
        try {
            RequestWrapper requestWrapper = new RequestWrapper(httpRequest);
            requestWrapper.resetHeaders();
            StringBuilder stringBuilder = new StringBuilder();
            Object obj = 1;
            for (String str : map.keySet()) {
                if (obj == null) {
                    stringBuilder.append(",");
                }
                obj = null;
                stringBuilder.append(str);
            }
            requestWrapper.setHeader("If-None-Match", stringBuilder.toString());
            return requestWrapper;
        } catch (Throwable e) {
            log.warn("unable to build conditional request", e);
            return httpRequest;
        }
    }

    public HttpRequest buildUnconditionalRequest(HttpRequest httpRequest, HttpCacheEntry httpCacheEntry) {
        try {
            RequestWrapper requestWrapper = new RequestWrapper(httpRequest);
            requestWrapper.resetHeaders();
            requestWrapper.addHeader("Cache-Control", "no-cache");
            requestWrapper.addHeader("Pragma", "no-cache");
            requestWrapper.removeHeaders("If-Range");
            requestWrapper.removeHeaders("If-Match");
            requestWrapper.removeHeaders("If-None-Match");
            requestWrapper.removeHeaders("If-Unmodified-Since");
            requestWrapper.removeHeaders("If-Modified-Since");
            return requestWrapper;
        } catch (Throwable e) {
            log.warn("unable to build proper unconditional request", e);
            return httpRequest;
        }
    }
}
