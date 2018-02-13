package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@NotThreadSafe
class ManagedClientConnectionImpl implements ManagedClientConnection {
    private volatile long duration;
    private final ClientConnectionManager manager;
    private final ClientConnectionOperator operator;
    private volatile HttpPoolEntry poolEntry;
    private volatile boolean reusable;

    ManagedClientConnectionImpl(ClientConnectionManager clientConnectionManager, ClientConnectionOperator clientConnectionOperator, HttpPoolEntry httpPoolEntry) {
        if (clientConnectionManager == null) {
            throw new IllegalArgumentException("Connection manager may not be null");
        } else if (clientConnectionOperator == null) {
            throw new IllegalArgumentException("Connection operator may not be null");
        } else if (httpPoolEntry == null) {
            throw new IllegalArgumentException("HTTP pool entry may not be null");
        } else {
            this.manager = clientConnectionManager;
            this.operator = clientConnectionOperator;
            this.poolEntry = httpPoolEntry;
            this.reusable = false;
            this.duration = Long.MAX_VALUE;
        }
    }

    private OperatedClientConnection ensureConnection() {
        HttpPoolEntry httpPoolEntry = this.poolEntry;
        if (httpPoolEntry != null) {
            return (OperatedClientConnection) httpPoolEntry.getConnection();
        }
        throw new ConnectionShutdownException();
    }

    private HttpPoolEntry ensurePoolEntry() {
        HttpPoolEntry httpPoolEntry = this.poolEntry;
        if (httpPoolEntry != null) {
            return httpPoolEntry;
        }
        throw new ConnectionShutdownException();
    }

    private OperatedClientConnection getConnection() {
        HttpPoolEntry httpPoolEntry = this.poolEntry;
        return httpPoolEntry == null ? null : (OperatedClientConnection) httpPoolEntry.getConnection();
    }

    public void abortConnection() {
        synchronized (this) {
            if (this.poolEntry == null) {
                return;
            }
            this.reusable = false;
            try {
                ((OperatedClientConnection) this.poolEntry.getConnection()).shutdown();
            } catch (IOException e) {
            }
            this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
            this.poolEntry = null;
        }
    }

    public void close() throws IOException {
        HttpPoolEntry httpPoolEntry = this.poolEntry;
        if (httpPoolEntry != null) {
            OperatedClientConnection operatedClientConnection = (OperatedClientConnection) httpPoolEntry.getConnection();
            httpPoolEntry.getTracker().reset();
            operatedClientConnection.close();
        }
    }

    HttpPoolEntry detach() {
        HttpPoolEntry httpPoolEntry = this.poolEntry;
        this.poolEntry = null;
        return httpPoolEntry;
    }

    public void flush() throws IOException {
        ensureConnection().flush();
    }

    public Object getAttribute(String str) {
        OperatedClientConnection ensureConnection = ensureConnection();
        return ensureConnection instanceof HttpContext ? ((HttpContext) ensureConnection).getAttribute(str) : null;
    }

    public InetAddress getLocalAddress() {
        return ensureConnection().getLocalAddress();
    }

    public int getLocalPort() {
        return ensureConnection().getLocalPort();
    }

    public ClientConnectionManager getManager() {
        return this.manager;
    }

    public HttpConnectionMetrics getMetrics() {
        return ensureConnection().getMetrics();
    }

    HttpPoolEntry getPoolEntry() {
        return this.poolEntry;
    }

    public InetAddress getRemoteAddress() {
        return ensureConnection().getRemoteAddress();
    }

    public int getRemotePort() {
        return ensureConnection().getRemotePort();
    }

    public HttpRoute getRoute() {
        return ensurePoolEntry().getEffectiveRoute();
    }

    public SSLSession getSSLSession() {
        Socket socket = ensureConnection().getSocket();
        return socket instanceof SSLSocket ? ((SSLSocket) socket).getSession() : null;
    }

    public int getSocketTimeout() {
        return ensureConnection().getSocketTimeout();
    }

    public Object getState() {
        return ensurePoolEntry().getState();
    }

    public boolean isMarkedReusable() {
        return this.reusable;
    }

    public boolean isOpen() {
        OperatedClientConnection connection = getConnection();
        return connection != null ? connection.isOpen() : false;
    }

    public boolean isResponseAvailable(int i) throws IOException {
        return ensureConnection().isResponseAvailable(i);
    }

    public boolean isSecure() {
        return ensureConnection().isSecure();
    }

    public boolean isStale() {
        OperatedClientConnection connection = getConnection();
        return connection != null ? connection.isStale() : true;
    }

    public void layerProtocol(HttpContext httpContext, HttpParams httpParams) throws IOException {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        HttpHost targetHost;
        OperatedClientConnection operatedClientConnection;
        synchronized (this) {
            if (this.poolEntry == null) {
                throw new ConnectionShutdownException();
            }
            RouteTracker tracker = this.poolEntry.getTracker();
            if (!tracker.isConnected()) {
                throw new IllegalStateException("Connection not open");
            } else if (!tracker.isTunnelled()) {
                throw new IllegalStateException("Protocol layering without a tunnel not supported");
            } else if (tracker.isLayered()) {
                throw new IllegalStateException("Multiple protocol layering not supported");
            } else {
                targetHost = tracker.getTargetHost();
                operatedClientConnection = (OperatedClientConnection) this.poolEntry.getConnection();
            }
        }
        this.operator.updateSecureConnection(operatedClientConnection, targetHost, httpContext, httpParams);
        synchronized (this) {
            if (this.poolEntry == null) {
                throw new InterruptedIOException();
            }
            this.poolEntry.getTracker().layerProtocol(operatedClientConnection.isSecure());
        }
    }

    public void markReusable() {
        this.reusable = true;
    }

    public void open(HttpRoute httpRoute, HttpContext httpContext, HttpParams httpParams) throws IOException {
        if (httpRoute == null) {
            throw new IllegalArgumentException("Route may not be null");
        } else if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            OperatedClientConnection operatedClientConnection;
            synchronized (this) {
                if (this.poolEntry == null) {
                    throw new ConnectionShutdownException();
                } else if (this.poolEntry.getTracker().isConnected()) {
                    throw new IllegalStateException("Connection already open");
                } else {
                    operatedClientConnection = (OperatedClientConnection) this.poolEntry.getConnection();
                }
            }
            HttpHost proxyHost = httpRoute.getProxyHost();
            this.operator.openConnection(operatedClientConnection, proxyHost != null ? proxyHost : httpRoute.getTargetHost(), httpRoute.getLocalAddress(), httpContext, httpParams);
            synchronized (this) {
                if (this.poolEntry == null) {
                    throw new InterruptedIOException();
                }
                RouteTracker tracker = this.poolEntry.getTracker();
                if (proxyHost == null) {
                    tracker.connectTarget(operatedClientConnection.isSecure());
                } else {
                    tracker.connectProxy(proxyHost, operatedClientConnection.isSecure());
                }
            }
        }
    }

    public void receiveResponseEntity(HttpResponse httpResponse) throws HttpException, IOException {
        ensureConnection().receiveResponseEntity(httpResponse);
    }

    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        return ensureConnection().receiveResponseHeader();
    }

    public void releaseConnection() {
        synchronized (this) {
            if (this.poolEntry == null) {
                return;
            }
            this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
            this.poolEntry = null;
        }
    }

    public Object removeAttribute(String str) {
        OperatedClientConnection ensureConnection = ensureConnection();
        return ensureConnection instanceof HttpContext ? ((HttpContext) ensureConnection).removeAttribute(str) : null;
    }

    public void sendRequestEntity(HttpEntityEnclosingRequest httpEntityEnclosingRequest) throws HttpException, IOException {
        ensureConnection().sendRequestEntity(httpEntityEnclosingRequest);
    }

    public void sendRequestHeader(HttpRequest httpRequest) throws HttpException, IOException {
        ensureConnection().sendRequestHeader(httpRequest);
    }

    public void setAttribute(String str, Object obj) {
        OperatedClientConnection ensureConnection = ensureConnection();
        if (ensureConnection instanceof HttpContext) {
            ((HttpContext) ensureConnection).setAttribute(str, obj);
        }
    }

    public void setIdleDuration(long j, TimeUnit timeUnit) {
        if (j > 0) {
            this.duration = timeUnit.toMillis(j);
        } else {
            this.duration = -1;
        }
    }

    public void setSocketTimeout(int i) {
        ensureConnection().setSocketTimeout(i);
    }

    public void setState(Object obj) {
        ensurePoolEntry().setState(obj);
    }

    public void shutdown() throws IOException {
        HttpPoolEntry httpPoolEntry = this.poolEntry;
        if (httpPoolEntry != null) {
            OperatedClientConnection operatedClientConnection = (OperatedClientConnection) httpPoolEntry.getConnection();
            httpPoolEntry.getTracker().reset();
            operatedClientConnection.shutdown();
        }
    }

    public void tunnelProxy(HttpHost httpHost, boolean z, HttpParams httpParams) throws IOException {
        if (httpHost == null) {
            throw new IllegalArgumentException("Next proxy amy not be null");
        } else if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            OperatedClientConnection operatedClientConnection;
            synchronized (this) {
                if (this.poolEntry == null) {
                    throw new ConnectionShutdownException();
                } else if (this.poolEntry.getTracker().isConnected()) {
                    operatedClientConnection = (OperatedClientConnection) this.poolEntry.getConnection();
                } else {
                    throw new IllegalStateException("Connection not open");
                }
            }
            operatedClientConnection.update(null, httpHost, z, httpParams);
            synchronized (this) {
                if (this.poolEntry == null) {
                    throw new InterruptedIOException();
                }
                this.poolEntry.getTracker().tunnelProxy(httpHost, z);
            }
        }
    }

    public void tunnelTarget(boolean z, HttpParams httpParams) throws IOException {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        HttpHost targetHost;
        OperatedClientConnection operatedClientConnection;
        synchronized (this) {
            if (this.poolEntry == null) {
                throw new ConnectionShutdownException();
            }
            RouteTracker tracker = this.poolEntry.getTracker();
            if (!tracker.isConnected()) {
                throw new IllegalStateException("Connection not open");
            } else if (tracker.isTunnelled()) {
                throw new IllegalStateException("Connection is already tunnelled");
            } else {
                targetHost = tracker.getTargetHost();
                operatedClientConnection = (OperatedClientConnection) this.poolEntry.getConnection();
            }
        }
        operatedClientConnection.update(null, targetHost, z, httpParams);
        synchronized (this) {
            if (this.poolEntry == null) {
                throw new InterruptedIOException();
            }
            this.poolEntry.getTracker().tunnelTarget(z);
        }
    }

    public void unmarkReusable() {
        this.reusable = false;
    }
}
