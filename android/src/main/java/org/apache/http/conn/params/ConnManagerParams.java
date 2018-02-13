package org.apache.http.conn.params;

import org.apache.http.annotation.Immutable;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;

@Immutable
@Deprecated
public final class ConnManagerParams implements ConnManagerPNames {
    private static final ConnPerRoute DEFAULT_CONN_PER_ROUTE = new C12951();
    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;

    static class C12951 implements ConnPerRoute {
        C12951() {
        }

        public int getMaxForRoute(HttpRoute httpRoute) {
            return 2;
        }
    }

    public static ConnPerRoute getMaxConnectionsPerRoute(HttpParams httpParams) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters must not be null.");
        }
        ConnPerRoute connPerRoute = (ConnPerRoute) httpParams.getParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE);
        return connPerRoute == null ? DEFAULT_CONN_PER_ROUTE : connPerRoute;
    }

    public static int getMaxTotalConnections(HttpParams httpParams) {
        if (httpParams != null) {
            return httpParams.getIntParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 20);
        }
        throw new IllegalArgumentException("HTTP parameters must not be null.");
    }

    public static long getTimeout(HttpParams httpParams) {
        if (httpParams != null) {
            return httpParams.getLongParameter("http.conn-manager.timeout", 0);
        }
        throw new IllegalArgumentException("HTTP parameters may not be null");
    }

    public static void setMaxConnectionsPerRoute(HttpParams httpParams, ConnPerRoute connPerRoute) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters must not be null.");
        }
        httpParams.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, connPerRoute);
    }

    public static void setMaxTotalConnections(HttpParams httpParams, int i) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters must not be null.");
        }
        httpParams.setIntParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, i);
    }

    public static void setTimeout(HttpParams httpParams, long j) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        httpParams.setLongParameter("http.conn-manager.timeout", j);
    }
}
