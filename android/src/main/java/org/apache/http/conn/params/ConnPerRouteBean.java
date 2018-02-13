package org.apache.http.conn.params;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.routing.HttpRoute;

@Deprecated
@ThreadSafe
public final class ConnPerRouteBean implements ConnPerRoute {
    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 2;
    private volatile int defaultMax;
    private final ConcurrentHashMap<HttpRoute, Integer> maxPerHostMap;

    public ConnPerRouteBean() {
        this(2);
    }

    public ConnPerRouteBean(int i) {
        this.maxPerHostMap = new ConcurrentHashMap();
        setDefaultMaxPerRoute(i);
    }

    public int getDefaultMax() {
        return this.defaultMax;
    }

    public int getDefaultMaxPerRoute() {
        return this.defaultMax;
    }

    public int getMaxForRoute(HttpRoute httpRoute) {
        if (httpRoute == null) {
            throw new IllegalArgumentException("HTTP route may not be null.");
        }
        Integer num = (Integer) this.maxPerHostMap.get(httpRoute);
        return num != null ? num.intValue() : this.defaultMax;
    }

    public void setDefaultMaxPerRoute(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("The maximum must be greater than 0.");
        }
        this.defaultMax = i;
    }

    public void setMaxForRoute(HttpRoute httpRoute, int i) {
        if (httpRoute == null) {
            throw new IllegalArgumentException("HTTP route may not be null.");
        } else if (i < 1) {
            throw new IllegalArgumentException("The maximum must be greater than 0.");
        } else {
            this.maxPerHostMap.put(httpRoute, Integer.valueOf(i));
        }
    }

    public void setMaxForRoutes(Map<HttpRoute, Integer> map) {
        if (map != null) {
            this.maxPerHostMap.clear();
            this.maxPerHostMap.putAll(map);
        }
    }

    public String toString() {
        return this.maxPerHostMap.toString();
    }
}
