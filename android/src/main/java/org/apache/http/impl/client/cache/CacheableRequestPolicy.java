package org.apache.http.impl.client.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpRequest;
import org.apache.http.HttpVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.cache.HeaderConstants;

@Immutable
class CacheableRequestPolicy {
    private final Log log = LogFactory.getLog(getClass());

    CacheableRequestPolicy() {
    }

    public boolean isServableFromCache(HttpRequest httpRequest) {
        String method = httpRequest.getRequestLine().getMethod();
        if (HttpVersion.HTTP_1_1.compareToVersion(httpRequest.getRequestLine().getProtocolVersion()) != 0) {
            this.log.trace("non-HTTP/1.1 request was not serveable from cache");
            return false;
        } else if (!method.equals("GET")) {
            this.log.trace("non-GET request was not serveable from cache");
            return false;
        } else if (httpRequest.getHeaders("Pragma").length > 0) {
            this.log.trace("request with Pragma header was not serveable from cache");
            return false;
        } else {
            for (Header elements : httpRequest.getHeaders("Cache-Control")) {
                HeaderElement[] elements2 = elements.getElements();
                int length = elements2.length;
                int i = 0;
                while (i < length) {
                    HeaderElement headerElement = elements2[i];
                    if (HeaderConstants.CACHE_CONTROL_NO_STORE.equalsIgnoreCase(headerElement.getName())) {
                        this.log.trace("Request with no-store was not serveable from cache");
                        return false;
                    } else if ("no-cache".equalsIgnoreCase(headerElement.getName())) {
                        this.log.trace("Request with no-cache was not serveable from cache");
                        return false;
                    } else {
                        i++;
                    }
                }
            }
            this.log.trace("Request was serveable from cache");
            return true;
        }
    }
}
