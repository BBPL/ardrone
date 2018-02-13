package org.apache.http.impl.conn.tsccm;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.AbstractPoolEntry;

@Deprecated
public class BasicPoolEntry extends AbstractPoolEntry {
    private final long created;
    private long expiry;
    private long updated;
    private long validUntil;

    public BasicPoolEntry(ClientConnectionOperator clientConnectionOperator, HttpRoute httpRoute) {
        this(clientConnectionOperator, httpRoute, -1, TimeUnit.MILLISECONDS);
    }

    public BasicPoolEntry(ClientConnectionOperator clientConnectionOperator, HttpRoute httpRoute, long j, TimeUnit timeUnit) {
        super(clientConnectionOperator, httpRoute);
        if (httpRoute == null) {
            throw new IllegalArgumentException("HTTP route may not be null");
        }
        this.created = System.currentTimeMillis();
        if (j > 0) {
            this.validUntil = this.created + timeUnit.toMillis(j);
        } else {
            this.validUntil = Long.MAX_VALUE;
        }
        this.expiry = this.validUntil;
    }

    public BasicPoolEntry(ClientConnectionOperator clientConnectionOperator, HttpRoute httpRoute, ReferenceQueue<Object> referenceQueue) {
        super(clientConnectionOperator, httpRoute);
        if (httpRoute == null) {
            throw new IllegalArgumentException("HTTP route may not be null");
        }
        this.created = System.currentTimeMillis();
        this.validUntil = Long.MAX_VALUE;
        this.expiry = this.validUntil;
    }

    protected final OperatedClientConnection getConnection() {
        return this.connection;
    }

    public long getCreated() {
        return this.created;
    }

    public long getExpiry() {
        return this.expiry;
    }

    protected final HttpRoute getPlannedRoute() {
        return this.route;
    }

    public long getUpdated() {
        return this.updated;
    }

    public long getValidUntil() {
        return this.validUntil;
    }

    protected final BasicPoolEntryRef getWeakRef() {
        return null;
    }

    public boolean isExpired(long j) {
        return j >= this.expiry;
    }

    protected void shutdownEntry() {
        super.shutdownEntry();
    }

    public void updateExpiry(long j, TimeUnit timeUnit) {
        this.updated = System.currentTimeMillis();
        this.expiry = Math.min(this.validUntil, j > 0 ? this.updated + timeUnit.toMillis(j) : Long.MAX_VALUE);
    }
}
