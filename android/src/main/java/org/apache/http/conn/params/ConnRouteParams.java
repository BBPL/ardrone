package org.apache.http.conn.params;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;

@Immutable
public class ConnRouteParams implements ConnRoutePNames {
    public static final HttpHost NO_HOST = new HttpHost("127.0.0.255", 0, "no-host");
    public static final HttpRoute NO_ROUTE = new HttpRoute(NO_HOST);

    private ConnRouteParams() {
    }

    public static HttpHost getDefaultProxy(HttpParams httpParams) {
        if (httpParams == null) {
            throw new IllegalArgumentException("Parameters must not be null.");
        }
        HttpHost httpHost = (HttpHost) httpParams.getParameter(ConnRoutePNames.DEFAULT_PROXY);
        return (httpHost == null || !NO_HOST.equals(httpHost)) ? httpHost : null;
    }

    public static HttpRoute getForcedRoute(HttpParams httpParams) {
        if (httpParams == null) {
            throw new IllegalArgumentException("Parameters must not be null.");
        }
        HttpRoute httpRoute = (HttpRoute) httpParams.getParameter(ConnRoutePNames.FORCED_ROUTE);
        return (httpRoute == null || !NO_ROUTE.equals(httpRoute)) ? httpRoute : null;
    }

    public static InetAddress getLocalAddress(HttpParams httpParams) {
        if (httpParams != null) {
            return (InetAddress) httpParams.getParameter(ConnRoutePNames.LOCAL_ADDRESS);
        }
        throw new IllegalArgumentException("Parameters must not be null.");
    }

    public static void setDefaultProxy(HttpParams httpParams, HttpHost httpHost) {
        if (httpParams == null) {
            throw new IllegalArgumentException("Parameters must not be null.");
        }
        httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
    }

    public static void setForcedRoute(HttpParams httpParams, HttpRoute httpRoute) {
        if (httpParams == null) {
            throw new IllegalArgumentException("Parameters must not be null.");
        }
        httpParams.setParameter(ConnRoutePNames.FORCED_ROUTE, httpRoute);
    }

    public static void setLocalAddress(HttpParams httpParams, InetAddress inetAddress) {
        if (httpParams == null) {
            throw new IllegalArgumentException("Parameters must not be null.");
        }
        httpParams.setParameter(ConnRoutePNames.LOCAL_ADDRESS, inetAddress);
    }
}
