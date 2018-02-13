package org.apache.http.impl.conn;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;

@ThreadSafe
public class BasicClientConnectionManager implements ClientConnectionManager {
    private static final AtomicLong COUNTER = new AtomicLong();
    public static final String MISUSE_MESSAGE = "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
    @GuardedBy("this")
    private ManagedClientConnectionImpl conn;
    private final ClientConnectionOperator connOperator;
    private final Log log;
    @GuardedBy("this")
    private HttpPoolEntry poolEntry;
    private final SchemeRegistry schemeRegistry;
    @GuardedBy("this")
    private volatile boolean shutdown;

    public BasicClientConnectionManager() {
        this(SchemeRegistryFactory.createDefault());
    }

    public BasicClientConnectionManager(SchemeRegistry schemeRegistry) {
        this.log = LogFactory.getLog(getClass());
        if (schemeRegistry == null) {
            throw new IllegalArgumentException("Scheme registry may not be null");
        }
        this.schemeRegistry = schemeRegistry;
        this.connOperator = createConnectionOperator(schemeRegistry);
    }

    private void assertNotShutdown() {
        if (this.shutdown) {
            throw new IllegalStateException("Connection manager has been shut down");
        }
    }

    private void shutdownConnection(HttpClientConnection httpClientConnection) {
        try {
            httpClientConnection.shutdown();
        } catch (Throwable e) {
            if (this.log.isDebugEnabled()) {
                this.log.debug("I/O exception shutting down connection", e);
            }
        }
    }

    public void closeExpiredConnections() {
        synchronized (this) {
            assertNotShutdown();
            long currentTimeMillis = System.currentTimeMillis();
            if (this.poolEntry != null && this.poolEntry.isExpired(currentTimeMillis)) {
                this.poolEntry.close();
                this.poolEntry.getTracker().reset();
            }
        }
    }

    public void closeIdleConnections(long j, TimeUnit timeUnit) {
        long j2 = 0;
        if (timeUnit == null) {
            throw new IllegalArgumentException("Time unit must not be null.");
        }
        synchronized (this) {
            assertNotShutdown();
            long toMillis = timeUnit.toMillis(j);
            if (toMillis >= 0) {
                j2 = toMillis;
            }
            toMillis = System.currentTimeMillis();
            if (this.poolEntry != null && this.poolEntry.getUpdated() <= toMillis - r0) {
                this.poolEntry.close();
                this.poolEntry.getTracker().reset();
            }
        }
    }

    protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schemeRegistry) {
        return new DefaultClientConnectionOperator(schemeRegistry);
    }

    protected void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    ManagedClientConnection getConnection(HttpRoute httpRoute, Object obj) {
        if (httpRoute == null) {
            throw new IllegalArgumentException("Route may not be null.");
        }
        ManagedClientConnection managedClientConnection;
        synchronized (this) {
            assertNotShutdown();
            if (this.log.isDebugEnabled()) {
                this.log.debug("Get connection for route " + httpRoute);
            }
            if (this.conn != null) {
                throw new IllegalStateException(MISUSE_MESSAGE);
            }
            if (!(this.poolEntry == null || this.poolEntry.getPlannedRoute().equals(httpRoute))) {
                this.poolEntry.close();
                this.poolEntry = null;
            }
            if (this.poolEntry == null) {
                HttpRoute httpRoute2 = httpRoute;
                this.poolEntry = new HttpPoolEntry(this.log, Long.toString(COUNTER.getAndIncrement()), httpRoute2, this.connOperator.createConnection(), 0, TimeUnit.MILLISECONDS);
            }
            if (this.poolEntry.isExpired(System.currentTimeMillis())) {
                this.poolEntry.close();
                this.poolEntry.getTracker().reset();
            }
            this.conn = new ManagedClientConnectionImpl(this, this.connOperator, this.poolEntry);
            managedClientConnection = this.conn;
        }
        return managedClientConnection;
    }

    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void releaseConnection(org.apache.http.conn.ManagedClientConnection r7, long r8, java.util.concurrent.TimeUnit r10) {
        /*
        r6 = this;
        r0 = r7 instanceof org.apache.http.impl.conn.ManagedClientConnectionImpl;
        if (r0 != 0) goto L_0x000c;
    L_0x0004:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "Connection class mismatch, connection not obtained from this manager";
        r0.<init>(r1);
        throw r0;
    L_0x000c:
        r0 = r7;
        r0 = (org.apache.http.impl.conn.ManagedClientConnectionImpl) r0;
        monitor-enter(r0);
        r1 = r6.log;	 Catch:{ all -> 0x0048 }
        r1 = r1.isDebugEnabled();	 Catch:{ all -> 0x0048 }
        if (r1 == 0) goto L_0x0030;
    L_0x0018:
        r1 = r6.log;	 Catch:{ all -> 0x0048 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0048 }
        r2.<init>();	 Catch:{ all -> 0x0048 }
        r3 = "Releasing connection ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0048 }
        r2 = r2.append(r7);	 Catch:{ all -> 0x0048 }
        r2 = r2.toString();	 Catch:{ all -> 0x0048 }
        r1.debug(r2);	 Catch:{ all -> 0x0048 }
    L_0x0030:
        r1 = r0.getPoolEntry();	 Catch:{ all -> 0x0048 }
        if (r1 != 0) goto L_0x0038;
    L_0x0036:
        monitor-exit(r0);	 Catch:{ all -> 0x0048 }
    L_0x0037:
        return;
    L_0x0038:
        r1 = r0.getManager();	 Catch:{ all -> 0x0048 }
        if (r1 == 0) goto L_0x004b;
    L_0x003e:
        if (r1 == r6) goto L_0x004b;
    L_0x0040:
        r1 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0048 }
        r2 = "Connection not obtained from this manager";
        r1.<init>(r2);	 Catch:{ all -> 0x0048 }
        throw r1;	 Catch:{ all -> 0x0048 }
    L_0x0048:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0048 }
        throw r1;
    L_0x004b:
        monitor-enter(r6);	 Catch:{ all -> 0x0048 }
        r1 = r6.shutdown;	 Catch:{ all -> 0x00de }
        if (r1 == 0) goto L_0x0056;
    L_0x0050:
        r6.shutdownConnection(r0);	 Catch:{ all -> 0x00de }
        monitor-exit(r6);	 Catch:{ all -> 0x00de }
        monitor-exit(r0);	 Catch:{ all -> 0x0048 }
        goto L_0x0037;
    L_0x0056:
        r1 = r0.isOpen();	 Catch:{ all -> 0x00cb }
        if (r1 == 0) goto L_0x0065;
    L_0x005c:
        r1 = r0.isMarkedReusable();	 Catch:{ all -> 0x00cb }
        if (r1 != 0) goto L_0x0065;
    L_0x0062:
        r6.shutdownConnection(r0);	 Catch:{ all -> 0x00cb }
    L_0x0065:
        r2 = r6.poolEntry;	 Catch:{ all -> 0x00cb }
        if (r10 == 0) goto L_0x00c5;
    L_0x0069:
        r1 = r10;
    L_0x006a:
        r2.updateExpiry(r8, r1);	 Catch:{ all -> 0x00cb }
        r1 = r6.log;	 Catch:{ all -> 0x00cb }
        r1 = r1.isDebugEnabled();	 Catch:{ all -> 0x00cb }
        if (r1 == 0) goto L_0x00b0;
    L_0x0075:
        r2 = 0;
        r1 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1));
        if (r1 <= 0) goto L_0x00c8;
    L_0x007b:
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00cb }
        r1.<init>();	 Catch:{ all -> 0x00cb }
        r2 = "for ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x00cb }
        r1 = r1.append(r8);	 Catch:{ all -> 0x00cb }
        r2 = " ";
        r1 = r1.append(r2);	 Catch:{ all -> 0x00cb }
        r1 = r1.append(r10);	 Catch:{ all -> 0x00cb }
        r1 = r1.toString();	 Catch:{ all -> 0x00cb }
    L_0x0098:
        r2 = r6.log;	 Catch:{ all -> 0x00cb }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00cb }
        r3.<init>();	 Catch:{ all -> 0x00cb }
        r4 = "Connection can be kept alive ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x00cb }
        r1 = r3.append(r1);	 Catch:{ all -> 0x00cb }
        r1 = r1.toString();	 Catch:{ all -> 0x00cb }
        r2.debug(r1);	 Catch:{ all -> 0x00cb }
    L_0x00b0:
        r0.detach();	 Catch:{ all -> 0x00de }
        r1 = 0;
        r6.conn = r1;	 Catch:{ all -> 0x00de }
        r1 = r6.poolEntry;	 Catch:{ all -> 0x00de }
        r1 = r1.isClosed();	 Catch:{ all -> 0x00de }
        if (r1 == 0) goto L_0x00c1;
    L_0x00be:
        r1 = 0;
        r6.poolEntry = r1;	 Catch:{ all -> 0x00de }
    L_0x00c1:
        monitor-exit(r6);	 Catch:{ all -> 0x00de }
        monitor-exit(r0);	 Catch:{ all -> 0x0048 }
        goto L_0x0037;
    L_0x00c5:
        r1 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ all -> 0x00cb }
        goto L_0x006a;
    L_0x00c8:
        r1 = "indefinitely";
        goto L_0x0098;
    L_0x00cb:
        r1 = move-exception;
        r0.detach();	 Catch:{ all -> 0x00de }
        r2 = 0;
        r6.conn = r2;	 Catch:{ all -> 0x00de }
        r2 = r6.poolEntry;	 Catch:{ all -> 0x00de }
        r2 = r2.isClosed();	 Catch:{ all -> 0x00de }
        if (r2 == 0) goto L_0x00dd;
    L_0x00da:
        r2 = 0;
        r6.poolEntry = r2;	 Catch:{ all -> 0x00de }
    L_0x00dd:
        throw r1;	 Catch:{ all -> 0x00de }
    L_0x00de:
        r1 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x00de }
        throw r1;	 Catch:{ all -> 0x0048 }
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.BasicClientConnectionManager.releaseConnection(org.apache.http.conn.ManagedClientConnection, long, java.util.concurrent.TimeUnit):void");
    }

    public final ClientConnectionRequest requestConnection(final HttpRoute httpRoute, final Object obj) {
        return new ClientConnectionRequest() {
            public void abortRequest() {
            }

            public ManagedClientConnection getConnection(long j, TimeUnit timeUnit) {
                return BasicClientConnectionManager.this.getConnection(httpRoute, obj);
            }
        };
    }

    public void shutdown() {
        synchronized (this) {
            this.shutdown = true;
            try {
                if (this.poolEntry != null) {
                    this.poolEntry.close();
                }
                this.poolEntry = null;
                this.conn = null;
            } catch (Throwable th) {
                this.poolEntry = null;
                this.conn = null;
            }
        }
    }
}
