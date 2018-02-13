package org.apache.http.impl.client;

import java.util.HashMap;
import org.apache.http.HttpHost;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthScheme;
import org.apache.http.client.AuthCache;

@NotThreadSafe
public class BasicAuthCache implements AuthCache {
    private final HashMap<HttpHost, AuthScheme> map = new HashMap();

    public void clear() {
        this.map.clear();
    }

    public AuthScheme get(HttpHost httpHost) {
        if (httpHost != null) {
            return (AuthScheme) this.map.get(getKey(httpHost));
        }
        throw new IllegalArgumentException("HTTP host may not be null");
    }

    protected HttpHost getKey(HttpHost httpHost) {
        if (httpHost.getPort() > 0) {
            return httpHost;
        }
        return new HttpHost(httpHost.getHostName(), httpHost.getSchemeName().equalsIgnoreCase("https") ? 443 : 80, httpHost.getSchemeName());
    }

    public void put(HttpHost httpHost, AuthScheme authScheme) {
        if (httpHost == null) {
            throw new IllegalArgumentException("HTTP host may not be null");
        }
        this.map.put(getKey(httpHost), authScheme);
    }

    public void remove(HttpHost httpHost) {
        if (httpHost == null) {
            throw new IllegalArgumentException("HTTP host may not be null");
        }
        this.map.remove(getKey(httpHost));
    }

    public String toString() {
        return this.map.toString();
    }
}
