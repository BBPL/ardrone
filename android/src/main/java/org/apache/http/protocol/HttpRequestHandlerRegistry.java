package org.apache.http.protocol;

import java.util.Map;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public class HttpRequestHandlerRegistry implements HttpRequestHandlerResolver {
    private final UriPatternMatcher<HttpRequestHandler> matcher = new UriPatternMatcher();

    public Map<String, HttpRequestHandler> getHandlers() {
        return this.matcher.getObjects();
    }

    public HttpRequestHandler lookup(String str) {
        return (HttpRequestHandler) this.matcher.lookup(str);
    }

    public void register(String str, HttpRequestHandler httpRequestHandler) {
        if (str == null) {
            throw new IllegalArgumentException("URI request pattern may not be null");
        } else if (httpRequestHandler == null) {
            throw new IllegalArgumentException("Request handler may not be null");
        } else {
            this.matcher.register(str, httpRequestHandler);
        }
    }

    public void setHandlers(Map<String, HttpRequestHandler> map) {
        this.matcher.setObjects(map);
    }

    public void unregister(String str) {
        this.matcher.unregister(str);
    }
}
