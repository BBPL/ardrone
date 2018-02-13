package org.apache.http.impl.conn;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.pool.PoolEntry;

class HttpPoolEntry extends PoolEntry<HttpRoute, OperatedClientConnection> {
    private final Log log;
    private final RouteTracker tracker;

    public HttpPoolEntry(Log log, String str, HttpRoute httpRoute, OperatedClientConnection operatedClientConnection, long j, TimeUnit timeUnit) {
        super(str, httpRoute, operatedClientConnection, j, timeUnit);
        this.log = log;
        this.tracker = new RouteTracker(httpRoute);
    }

    public void close() {
        try {
            ((OperatedClientConnection) getConnection()).close();
        } catch (Throwable e) {
            this.log.debug("I/O error closing connection", e);
        }
    }

    HttpRoute getEffectiveRoute() {
        return this.tracker.toRoute();
    }

    HttpRoute getPlannedRoute() {
        return (HttpRoute) getRoute();
    }

    RouteTracker getTracker() {
        return this.tracker;
    }

    public boolean isClosed() {
        return !((OperatedClientConnection) getConnection()).isOpen();
    }

    public boolean isExpired(long j) {
        boolean isExpired = super.isExpired(j);
        if (isExpired && this.log.isDebugEnabled()) {
            this.log.debug("Connection " + this + " expired @ " + new Date(getExpiry()));
        }
        return isExpired;
    }
}
