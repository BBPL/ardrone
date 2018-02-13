package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
public abstract class AbstractPooledConnAdapter extends AbstractClientConnAdapter {
    protected volatile AbstractPoolEntry poolEntry;

    protected AbstractPooledConnAdapter(ClientConnectionManager clientConnectionManager, AbstractPoolEntry abstractPoolEntry) {
        super(clientConnectionManager, abstractPoolEntry.connection);
        this.poolEntry = abstractPoolEntry;
    }

    protected final void assertAttached() {
        if (this.poolEntry == null) {
            throw new ConnectionShutdownException();
        }
    }

    protected void assertValid(AbstractPoolEntry abstractPoolEntry) {
        if (isReleased() || abstractPoolEntry == null) {
            throw new ConnectionShutdownException();
        }
    }

    public void close() throws IOException {
        AbstractPoolEntry poolEntry = getPoolEntry();
        if (poolEntry != null) {
            poolEntry.shutdownEntry();
        }
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        if (wrappedConnection != null) {
            wrappedConnection.close();
        }
    }

    protected void detach() {
        synchronized (this) {
            this.poolEntry = null;
            super.detach();
        }
    }

    protected AbstractPoolEntry getPoolEntry() {
        return this.poolEntry;
    }

    public HttpRoute getRoute() {
        AbstractPoolEntry poolEntry = getPoolEntry();
        assertValid(poolEntry);
        return poolEntry.tracker == null ? null : poolEntry.tracker.toRoute();
    }

    public Object getState() {
        AbstractPoolEntry poolEntry = getPoolEntry();
        assertValid(poolEntry);
        return poolEntry.getState();
    }

    public void layerProtocol(HttpContext httpContext, HttpParams httpParams) throws IOException {
        AbstractPoolEntry poolEntry = getPoolEntry();
        assertValid(poolEntry);
        poolEntry.layerProtocol(httpContext, httpParams);
    }

    public void open(HttpRoute httpRoute, HttpContext httpContext, HttpParams httpParams) throws IOException {
        AbstractPoolEntry poolEntry = getPoolEntry();
        assertValid(poolEntry);
        poolEntry.open(httpRoute, httpContext, httpParams);
    }

    public void setState(Object obj) {
        AbstractPoolEntry poolEntry = getPoolEntry();
        assertValid(poolEntry);
        poolEntry.setState(obj);
    }

    public void shutdown() throws IOException {
        AbstractPoolEntry poolEntry = getPoolEntry();
        if (poolEntry != null) {
            poolEntry.shutdownEntry();
        }
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        if (wrappedConnection != null) {
            wrappedConnection.shutdown();
        }
    }

    public void tunnelProxy(HttpHost httpHost, boolean z, HttpParams httpParams) throws IOException {
        AbstractPoolEntry poolEntry = getPoolEntry();
        assertValid(poolEntry);
        poolEntry.tunnelProxy(httpHost, z, httpParams);
    }

    public void tunnelTarget(boolean z, HttpParams httpParams) throws IOException {
        AbstractPoolEntry poolEntry = getPoolEntry();
        assertValid(poolEntry);
        poolEntry.tunnelTarget(z, httpParams);
    }
}
