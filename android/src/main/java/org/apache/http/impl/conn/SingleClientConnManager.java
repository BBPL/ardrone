package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.HttpParams;

@Deprecated
@ThreadSafe
public class SingleClientConnManager implements ClientConnectionManager {
    public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
    protected final boolean alwaysShutDown;
    protected final ClientConnectionOperator connOperator;
    @GuardedBy("this")
    protected volatile long connectionExpiresTime;
    protected volatile boolean isShutDown;
    @GuardedBy("this")
    protected volatile long lastReleaseTime;
    private final Log log;
    @GuardedBy("this")
    protected volatile ConnAdapter managedConn;
    protected final SchemeRegistry schemeRegistry;
    @GuardedBy("this")
    protected volatile PoolEntry uniquePoolEntry;

    protected class ConnAdapter extends AbstractPooledConnAdapter {
        protected ConnAdapter(PoolEntry poolEntry, HttpRoute httpRoute) {
            super(SingleClientConnManager.this, poolEntry);
            markReusable();
            poolEntry.route = httpRoute;
        }
    }

    protected class PoolEntry extends AbstractPoolEntry {
        protected PoolEntry() {
            super(SingleClientConnManager.this.connOperator, null);
        }

        protected void close() throws IOException {
            shutdownEntry();
            if (this.connection.isOpen()) {
                this.connection.close();
            }
        }

        protected void shutdown() throws IOException {
            shutdownEntry();
            if (this.connection.isOpen()) {
                this.connection.shutdown();
            }
        }
    }

    public SingleClientConnManager() {
        this(SchemeRegistryFactory.createDefault());
    }

    public SingleClientConnManager(SchemeRegistry schemeRegistry) {
        this.log = LogFactory.getLog(getClass());
        if (schemeRegistry == null) {
            throw new IllegalArgumentException("Scheme registry must not be null.");
        }
        this.schemeRegistry = schemeRegistry;
        this.connOperator = createConnectionOperator(schemeRegistry);
        this.uniquePoolEntry = new PoolEntry();
        this.managedConn = null;
        this.lastReleaseTime = -1;
        this.alwaysShutDown = false;
        this.isShutDown = false;
    }

    public SingleClientConnManager(HttpParams httpParams, SchemeRegistry schemeRegistry) {
        this(schemeRegistry);
    }

    protected final void assertStillUp() throws IllegalStateException {
        if (this.isShutDown) {
            throw new IllegalStateException("Manager is shut down.");
        }
    }

    public void closeExpiredConnections() {
        if (System.currentTimeMillis() >= this.connectionExpiresTime) {
            closeIdleConnections(0, TimeUnit.MILLISECONDS);
        }
    }

    public void closeIdleConnections(long j, TimeUnit timeUnit) {
        assertStillUp();
        if (timeUnit == null) {
            throw new IllegalArgumentException("Time unit must not be null.");
        }
        synchronized (this) {
            if (this.managedConn == null && this.uniquePoolEntry.connection.isOpen()) {
                if (this.lastReleaseTime <= System.currentTimeMillis() - timeUnit.toMillis(j)) {
                    try {
                        this.uniquePoolEntry.close();
                    } catch (Throwable e) {
                        this.log.debug("Problem closing idle connection.", e);
                    }
                }
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

    public ManagedClientConnection getConnection(HttpRoute httpRoute, Object obj) {
        Object obj2 = 1;
        Object obj3 = null;
        if (httpRoute == null) {
            throw new IllegalArgumentException("Route may not be null.");
        }
        ManagedClientConnection managedClientConnection;
        assertStillUp();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Get connection for route " + httpRoute);
        }
        synchronized (this) {
            if (this.managedConn != null) {
                throw new IllegalStateException(MISUSE_MESSAGE);
            }
            Object obj4;
            closeExpiredConnections();
            if (this.uniquePoolEntry.connection.isOpen()) {
                RouteTracker routeTracker = this.uniquePoolEntry.tracker;
                if (routeTracker == null || !routeTracker.toRoute().equals(httpRoute)) {
                    obj4 = null;
                    obj3 = 1;
                } else {
                    obj4 = null;
                }
            } else {
                int i = 1;
            }
            if (obj3 != null) {
                try {
                    this.uniquePoolEntry.shutdown();
                } catch (Throwable e) {
                    this.log.debug("Problem shutting down connection.", e);
                }
            } else {
                obj2 = obj4;
            }
            if (obj2 != null) {
                this.uniquePoolEntry = new PoolEntry();
            }
            this.managedConn = new ConnAdapter(this.uniquePoolEntry, httpRoute);
            managedClientConnection = this.managedConn;
        }
        return managedClientConnection;
    }

    public SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    public void releaseConnection(ManagedClientConnection managedClientConnection, long j, TimeUnit timeUnit) {
        assertStillUp();
        if (managedClientConnection instanceof ConnAdapter) {
            if (this.log.isDebugEnabled()) {
                this.log.debug("Releasing connection " + managedClientConnection);
            }
            ConnAdapter connAdapter = (ConnAdapter) managedClientConnection;
            synchronized (connAdapter) {
                if (connAdapter.poolEntry == null) {
                    return;
                }
                Object manager = connAdapter.getManager();
                if (manager == null || manager == this) {
                    try {
                        if (connAdapter.isOpen() && (this.alwaysShutDown || !connAdapter.isMarkedReusable())) {
                            if (this.log.isDebugEnabled()) {
                                this.log.debug("Released connection open but not reusable.");
                            }
                            connAdapter.shutdown();
                        }
                        connAdapter.detach();
                        synchronized (this) {
                            this.managedConn = null;
                            this.lastReleaseTime = System.currentTimeMillis();
                            if (j > 0) {
                                this.connectionExpiresTime = timeUnit.toMillis(j) + this.lastReleaseTime;
                            } else {
                                this.connectionExpiresTime = Long.MAX_VALUE;
                            }
                        }
                    } catch (Throwable e) {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Exception shutting down released connection.", e);
                        }
                        connAdapter.detach();
                        synchronized (this) {
                            this.managedConn = null;
                            this.lastReleaseTime = System.currentTimeMillis();
                            if (j > 0) {
                                this.connectionExpiresTime = timeUnit.toMillis(j) + this.lastReleaseTime;
                            } else {
                                this.connectionExpiresTime = Long.MAX_VALUE;
                            }
                        }
                    } catch (Throwable th) {
                        connAdapter.detach();
                        synchronized (this) {
                            this.managedConn = null;
                            this.lastReleaseTime = System.currentTimeMillis();
                            if (j > 0) {
                                this.connectionExpiresTime = timeUnit.toMillis(j) + this.lastReleaseTime;
                            } else {
                                this.connectionExpiresTime = Long.MAX_VALUE;
                            }
                        }
                    }
                    return;
                }
                throw new IllegalArgumentException("Connection not obtained from this manager.");
            }
        }
        throw new IllegalArgumentException("Connection class mismatch, connection not obtained from this manager.");
    }

    public final ClientConnectionRequest requestConnection(final HttpRoute httpRoute, final Object obj) {
        return new ClientConnectionRequest() {
            public void abortRequest() {
            }

            public ManagedClientConnection getConnection(long j, TimeUnit timeUnit) {
                return SingleClientConnManager.this.getConnection(httpRoute, obj);
            }
        };
    }

    protected void revokeConnection() {
        ConnAdapter connAdapter = this.managedConn;
        if (connAdapter != null) {
            connAdapter.detach();
            synchronized (this) {
                try {
                    this.uniquePoolEntry.shutdown();
                } catch (Throwable e) {
                    this.log.debug("Problem while shutting down connection.", e);
                }
            }
        }
    }

    public void shutdown() {
        this.isShutDown = true;
        synchronized (this) {
            try {
                if (this.uniquePoolEntry != null) {
                    this.uniquePoolEntry.shutdown();
                }
                this.uniquePoolEntry = null;
                this.managedConn = null;
            } catch (Throwable e) {
                this.log.debug("Problem while shutting down manager.", e);
                this.uniquePoolEntry = null;
                this.managedConn = null;
            } catch (Throwable th) {
                this.uniquePoolEntry = null;
                this.managedConn = null;
            }
        }
    }
}
