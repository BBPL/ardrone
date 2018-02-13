package org.apache.http.conn.scheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public final class SchemeRegistry {
    private final ConcurrentHashMap<String, Scheme> registeredSchemes = new ConcurrentHashMap();

    public final Scheme get(String str) {
        if (str != null) {
            return (Scheme) this.registeredSchemes.get(str);
        }
        throw new IllegalArgumentException("Name must not be null.");
    }

    public final Scheme getScheme(String str) {
        Scheme scheme = get(str);
        if (scheme != null) {
            return scheme;
        }
        throw new IllegalStateException("Scheme '" + str + "' not registered.");
    }

    public final Scheme getScheme(HttpHost httpHost) {
        if (httpHost != null) {
            return getScheme(httpHost.getSchemeName());
        }
        throw new IllegalArgumentException("Host must not be null.");
    }

    public final List<String> getSchemeNames() {
        return new ArrayList(this.registeredSchemes.keySet());
    }

    public final Scheme register(Scheme scheme) {
        if (scheme != null) {
            return (Scheme) this.registeredSchemes.put(scheme.getName(), scheme);
        }
        throw new IllegalArgumentException("Scheme must not be null.");
    }

    public void setItems(Map<String, Scheme> map) {
        if (map != null) {
            this.registeredSchemes.clear();
            this.registeredSchemes.putAll(map);
        }
    }

    public final Scheme unregister(String str) {
        if (str != null) {
            return (Scheme) this.registeredSchemes.remove(str);
        }
        throw new IllegalArgumentException("Name must not be null.");
    }
}
