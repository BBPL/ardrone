package org.apache.http.impl.conn;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolStats;

@ThreadSafe
public class PoolingClientConnectionManager implements ClientConnectionManager, ConnPoolControl<HttpRoute> {
    private final DnsResolver dnsResolver;
    private final Log log;
    private final ClientConnectionOperator operator;
    private final HttpConnPool pool;
    private final SchemeRegistry schemeRegistry;

    public PoolingClientConnectionManager() {
        this(SchemeRegistryFactory.createDefault());
    }

    public PoolingClientConnectionManager(SchemeRegistry schemeRegistry) {
        this(schemeRegistry, -1, TimeUnit.MILLISECONDS);
    }

    public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, long j, TimeUnit timeUnit) {
        this(schemeRegistry, j, timeUnit, new SystemDefaultDnsResolver());
    }

    public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, long j, TimeUnit timeUnit, DnsResolver dnsResolver) {
        this.log = LogFactory.getLog(getClass());
        if (schemeRegistry == null) {
            throw new IllegalArgumentException("Scheme registry may not be null");
        } else if (dnsResolver == null) {
            throw new IllegalArgumentException("DNS resolver may not be null");
        } else {
            this.schemeRegistry = schemeRegistry;
            this.dnsResolver = dnsResolver;
            this.operator = createConnectionOperator(schemeRegistry);
            this.pool = new HttpConnPool(this.log, 2, 20, j, timeUnit);
        }
    }

    public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, DnsResolver dnsResolver) {
        this(schemeRegistry, -1, TimeUnit.MILLISECONDS, dnsResolver);
    }

    private String format(HttpRoute httpRoute, Object obj) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[route: ").append(httpRoute).append("]");
        if (obj != null) {
            stringBuilder.append("[state: ").append(obj).append("]");
        }
        return stringBuilder.toString();
    }

    private String format(HttpPoolEntry httpPoolEntry) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[id: ").append(httpPoolEntry.getId()).append("]");
        stringBuilder.append("[route: ").append(httpPoolEntry.getRoute()).append("]");
        Object state = httpPoolEntry.getState();
        if (state != null) {
            stringBuilder.append("[state: ").append(state).append("]");
        }
        return stringBuilder.toString();
    }

    private String formatStats(HttpRoute httpRoute) {
        StringBuilder stringBuilder = new StringBuilder();
        PoolStats totalStats = this.pool.getTotalStats();
        PoolStats stats = this.pool.getStats(httpRoute);
        stringBuilder.append("[total kept alive: ").append(totalStats.getAvailable()).append("; ");
        stringBuilder.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
        stringBuilder.append(" of ").append(stats.getMax()).append("; ");
        stringBuilder.append("total allocated: ").append(totalStats.getLeased() + totalStats.getAvailable());
        stringBuilder.append(" of ").append(totalStats.getMax()).append("]");
        return stringBuilder.toString();
    }

    public void closeExpiredConnections() {
        this.log.debug("Closing expired connections");
        this.pool.closeExpired();
    }

    public void closeIdleConnections(long j, TimeUnit timeUnit) {
        if (this.log.isDebugEnabled()) {
            this.log.debug("Closing connections idle longer than " + j + " " + timeUnit);
        }
        this.pool.closeIdle(j, timeUnit);
    }

    protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schemeRegistry) {
        return new DefaultClientConnectionOperator(schemeRegistry, this.dnsResolver);
    }

    protected void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    public int getDefaultMaxPerRoute() {
        return this.pool.getDefaultMaxPerRoute();
    }

    public int getMaxPerRoute(HttpRoute httpRoute) {
        return this.pool.getMaxPerRoute(httpRoute);
    }

    public int getMaxTotal() {
        return this.pool.getMaxTotal();
    }

    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    public PoolStats getStats(HttpRoute httpRoute) {
        return this.pool.getStats(httpRoute);
    }

    public PoolStats getTotalStats() {
        return this.pool.getTotalStats();
    }

    ManagedClientConnection leaseConnection(Future<HttpPoolEntry> future, long j, TimeUnit timeUnit) throws InterruptedException, ConnectionPoolTimeoutException {
        try {
            HttpPoolEntry httpPoolEntry = (HttpPoolEntry) future.get(j, timeUnit);
            if (httpPoolEntry == null || future.isCancelled()) {
                throw new InterruptedException();
            } else if (httpPoolEntry.getConnection() == null) {
                throw new IllegalStateException("Pool entry with no connection");
            } else {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Connection leased: " + format(httpPoolEntry) + formatStats((HttpRoute) httpPoolEntry.getRoute()));
                }
                return new ManagedClientConnectionImpl(this, this.operator, httpPoolEntry);
            }
        } catch (ExecutionException e) {
            Throwable e2 = e;
            Throwable cause = e2.getCause();
            if (cause != null) {
                e2 = cause;
            }
            this.log.error("Unexpected exception leasing connection from pool", e2);
            throw new InterruptedException();
        } catch (TimeoutException e3) {
            throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
        }
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
        r1 = "Connection class mismatch, connection not obtained from this manager.";
        r0.<init>(r1);
        throw r0;
    L_0x000c:
        r7 = (org.apache.http.impl.conn.ManagedClientConnectionImpl) r7;
        r0 = r7.getManager();
        if (r0 == r6) goto L_0x001c;
    L_0x0014:
        r0 = new java.lang.IllegalStateException;
        r1 = "Connection not obtained from this manager.";
        r0.<init>(r1);
        throw r0;
    L_0x001c:
        monitor-enter(r7);
        r1 = r7.detach();	 Catch:{ all -> 0x00cf }
        if (r1 != 0) goto L_0x0025;
    L_0x0023:
        monitor-exit(r7);	 Catch:{ all -> 0x00cf }
    L_0x0024:
        return;
    L_0x0025:
        r0 = r7.isOpen();	 Catch:{ all -> 0x00e4 }
        if (r0 == 0) goto L_0x0034;
    L_0x002b:
        r0 = r7.isMarkedReusable();	 Catch:{ all -> 0x00e4 }
        if (r0 != 0) goto L_0x0034;
    L_0x0031:
        r7.shutdown();	 Catch:{ IOException -> 0x00d2 }
    L_0x0034:
        r0 = r7.isMarkedReusable();	 Catch:{ all -> 0x00e4 }
        if (r0 == 0) goto L_0x0091;
    L_0x003a:
        if (r10 == 0) goto L_0x00ef;
    L_0x003c:
        r0 = r10;
    L_0x003d:
        r1.updateExpiry(r8, r0);	 Catch:{ all -> 0x00e4 }
        r0 = r6.log;	 Catch:{ all -> 0x00e4 }
        r0 = r0.isDebugEnabled();	 Catch:{ all -> 0x00e4 }
        if (r0 == 0) goto L_0x0091;
    L_0x0048:
        r2 = 0;
        r0 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1));
        if (r0 <= 0) goto L_0x00f3;
    L_0x004e:
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00e4 }
        r0.<init>();	 Catch:{ all -> 0x00e4 }
        r2 = "for ";
        r0 = r0.append(r2);	 Catch:{ all -> 0x00e4 }
        r0 = r0.append(r8);	 Catch:{ all -> 0x00e4 }
        r2 = " ";
        r0 = r0.append(r2);	 Catch:{ all -> 0x00e4 }
        r0 = r0.append(r10);	 Catch:{ all -> 0x00e4 }
        r0 = r0.toString();	 Catch:{ all -> 0x00e4 }
    L_0x006b:
        r2 = r6.log;	 Catch:{ all -> 0x00e4 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00e4 }
        r3.<init>();	 Catch:{ all -> 0x00e4 }
        r4 = "Connection ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x00e4 }
        r4 = r6.format(r1);	 Catch:{ all -> 0x00e4 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x00e4 }
        r4 = " can be kept alive ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x00e4 }
        r0 = r3.append(r0);	 Catch:{ all -> 0x00e4 }
        r0 = r0.toString();	 Catch:{ all -> 0x00e4 }
        r2.debug(r0);	 Catch:{ all -> 0x00e4 }
    L_0x0091:
        r0 = r6.pool;	 Catch:{ all -> 0x00cf }
        r2 = r7.isMarkedReusable();	 Catch:{ all -> 0x00cf }
        r0.release(r1, r2);	 Catch:{ all -> 0x00cf }
        r0 = r6.log;	 Catch:{ all -> 0x00cf }
        r0 = r0.isDebugEnabled();	 Catch:{ all -> 0x00cf }
        if (r0 == 0) goto L_0x00cc;
    L_0x00a2:
        r2 = r6.log;	 Catch:{ all -> 0x00cf }
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00cf }
        r0.<init>();	 Catch:{ all -> 0x00cf }
        r3 = "Connection released: ";
        r0 = r0.append(r3);	 Catch:{ all -> 0x00cf }
        r3 = r6.format(r1);	 Catch:{ all -> 0x00cf }
        r3 = r0.append(r3);	 Catch:{ all -> 0x00cf }
        r0 = r1.getRoute();	 Catch:{ all -> 0x00cf }
        r0 = (org.apache.http.conn.routing.HttpRoute) r0;	 Catch:{ all -> 0x00cf }
        r0 = r6.formatStats(r0);	 Catch:{ all -> 0x00cf }
        r0 = r3.append(r0);	 Catch:{ all -> 0x00cf }
        r0 = r0.toString();	 Catch:{ all -> 0x00cf }
        r2.debug(r0);	 Catch:{ all -> 0x00cf }
    L_0x00cc:
        monitor-exit(r7);	 Catch:{ all -> 0x00cf }
        goto L_0x0024;
    L_0x00cf:
        r0 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x00cf }
        throw r0;
    L_0x00d2:
        r0 = move-exception;
        r2 = r6.log;	 Catch:{ all -> 0x00e4 }
        r2 = r2.isDebugEnabled();	 Catch:{ all -> 0x00e4 }
        if (r2 == 0) goto L_0x0034;
    L_0x00db:
        r2 = r6.log;	 Catch:{ all -> 0x00e4 }
        r3 = "I/O exception shutting down released connection";
        r2.debug(r3, r0);	 Catch:{ all -> 0x00e4 }
        goto L_0x0034;
    L_0x00e4:
        r0 = move-exception;
        r2 = r6.pool;	 Catch:{ all -> 0x00cf }
        r3 = r7.isMarkedReusable();	 Catch:{ all -> 0x00cf }
        r2.release(r1, r3);	 Catch:{ all -> 0x00cf }
        throw r0;	 Catch:{ all -> 0x00cf }
    L_0x00ef:
        r0 = java.util.concurrent.TimeUnit.MILLISECONDS;	 Catch:{ all -> 0x00e4 }
        goto L_0x003d;
    L_0x00f3:
        r0 = "indefinitely";
        goto L_0x006b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.PoolingClientConnectionManager.releaseConnection(org.apache.http.conn.ManagedClientConnection, long, java.util.concurrent.TimeUnit):void");
    }

    public ClientConnectionRequest requestConnection(HttpRoute httpRoute, Object obj) {
        if (httpRoute == null) {
            throw new IllegalArgumentException("HTTP route may not be null");
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("Connection request: " + format(httpRoute, obj) + formatStats(httpRoute));
        }
        final Future lease = this.pool.lease(httpRoute, obj);
        return new ClientConnectionRequest() {
            public void abortRequest() {
                lease.cancel(true);
            }

            public ManagedClientConnection getConnection(long j, TimeUnit timeUnit) throws InterruptedException, ConnectionPoolTimeoutException {
                return PoolingClientConnectionManager.this.leaseConnection(lease, j, timeUnit);
            }
        };
    }

    public void setDefaultMaxPerRoute(int i) {
        this.pool.setDefaultMaxPerRoute(i);
    }

    public void setMaxPerRoute(HttpRoute httpRoute, int i) {
        this.pool.setMaxPerRoute(httpRoute, i);
    }

    public void setMaxTotal(int i) {
        this.pool.setMaxTotal(i);
    }

    public void shutdown() {
        this.log.debug("Connection manager is shutting down");
        try {
            this.pool.shutdown();
        } catch (Throwable e) {
            this.log.debug("I/O exception shutting down connection manager", e);
        }
        this.log.debug("Connection manager shut down");
    }
}
