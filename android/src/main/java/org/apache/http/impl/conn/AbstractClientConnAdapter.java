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
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.protocol.HttpContext;

@NotThreadSafe
@Deprecated
public abstract class AbstractClientConnAdapter implements ManagedClientConnection, HttpContext {
    private final ClientConnectionManager connManager;
    private volatile long duration = Long.MAX_VALUE;
    private volatile boolean markedReusable = false;
    private volatile boolean released = false;
    private volatile OperatedClientConnection wrappedConnection;

    protected AbstractClientConnAdapter(ClientConnectionManager clientConnectionManager, OperatedClientConnection operatedClientConnection) {
        this.connManager = clientConnectionManager;
        this.wrappedConnection = operatedClientConnection;
    }

    public void abortConnection() {
        synchronized (this) {
            if (!this.released) {
                this.released = true;
                unmarkReusable();
                try {
                    shutdown();
                } catch (IOException e) {
                }
                this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
            }
        }
    }

    protected final void assertNotAborted() throws InterruptedIOException {
        if (isReleased()) {
            throw new InterruptedIOException("Connection has been shut down");
        }
    }

    protected final void assertValid(OperatedClientConnection operatedClientConnection) throws ConnectionShutdownException {
        if (isReleased() || operatedClientConnection == null) {
            throw new ConnectionShutdownException();
        }
    }

    protected void detach() {
        synchronized (this) {
            this.wrappedConnection = null;
            this.duration = Long.MAX_VALUE;
        }
    }

    public void flush() throws IOException {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        wrappedConnection.flush();
    }

    public Object getAttribute(String str) {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        return wrappedConnection instanceof HttpContext ? ((HttpContext) wrappedConnection).getAttribute(str) : null;
    }

    public InetAddress getLocalAddress() {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        return wrappedConnection.getLocalAddress();
    }

    public int getLocalPort() {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        return wrappedConnection.getLocalPort();
    }

    protected ClientConnectionManager getManager() {
        return this.connManager;
    }

    public HttpConnectionMetrics getMetrics() {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        return wrappedConnection.getMetrics();
    }

    public InetAddress getRemoteAddress() {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        return wrappedConnection.getRemoteAddress();
    }

    public int getRemotePort() {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        return wrappedConnection.getRemotePort();
    }

    public SSLSession getSSLSession() {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        if (isOpen()) {
            Socket socket = wrappedConnection.getSocket();
            if (socket instanceof SSLSocket) {
                return ((SSLSocket) socket).getSession();
            }
        }
        return null;
    }

    public int getSocketTimeout() {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        return wrappedConnection.getSocketTimeout();
    }

    protected OperatedClientConnection getWrappedConnection() {
        return this.wrappedConnection;
    }

    public boolean isMarkedReusable() {
        return this.markedReusable;
    }

    public boolean isOpen() {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        return wrappedConnection == null ? false : wrappedConnection.isOpen();
    }

    protected boolean isReleased() {
        return this.released;
    }

    public boolean isResponseAvailable(int i) throws IOException {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        return wrappedConnection.isResponseAvailable(i);
    }

    public boolean isSecure() {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        return wrappedConnection.isSecure();
    }

    public boolean isStale() {
        if (!isReleased()) {
            OperatedClientConnection wrappedConnection = getWrappedConnection();
            if (wrappedConnection != null) {
                return wrappedConnection.isStale();
            }
        }
        return true;
    }

    public void markReusable() {
        this.markedReusable = true;
    }

    public void receiveResponseEntity(HttpResponse httpResponse) throws HttpException, IOException {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        unmarkReusable();
        wrappedConnection.receiveResponseEntity(httpResponse);
    }

    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        unmarkReusable();
        return wrappedConnection.receiveResponseHeader();
    }

    public void releaseConnection() {
        synchronized (this) {
            if (!this.released) {
                this.released = true;
                this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
            }
        }
    }

    public Object removeAttribute(String str) {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        return wrappedConnection instanceof HttpContext ? ((HttpContext) wrappedConnection).removeAttribute(str) : null;
    }

    public void sendRequestEntity(HttpEntityEnclosingRequest httpEntityEnclosingRequest) throws HttpException, IOException {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        unmarkReusable();
        wrappedConnection.sendRequestEntity(httpEntityEnclosingRequest);
    }

    public void sendRequestHeader(HttpRequest httpRequest) throws HttpException, IOException {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        unmarkReusable();
        wrappedConnection.sendRequestHeader(httpRequest);
    }

    public void setAttribute(String str, Object obj) {
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        if (wrappedConnection instanceof HttpContext) {
            ((HttpContext) wrappedConnection).setAttribute(str, obj);
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
        OperatedClientConnection wrappedConnection = getWrappedConnection();
        assertValid(wrappedConnection);
        wrappedConnection.setSocketTimeout(i);
    }

    public void unmarkReusable() {
        this.markedReusable = false;
    }
}
