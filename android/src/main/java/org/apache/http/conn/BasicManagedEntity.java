package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.util.EntityUtils;

@NotThreadSafe
public class BasicManagedEntity extends HttpEntityWrapper implements ConnectionReleaseTrigger, EofSensorWatcher {
    protected final boolean attemptReuse;
    protected ManagedClientConnection managedConn;

    public BasicManagedEntity(HttpEntity httpEntity, ManagedClientConnection managedClientConnection, boolean z) {
        super(httpEntity);
        if (managedClientConnection == null) {
            throw new IllegalArgumentException("Connection may not be null.");
        }
        this.managedConn = managedClientConnection;
        this.attemptReuse = z;
    }

    private void ensureConsumed() throws IOException {
        if (this.managedConn != null) {
            try {
                if (this.attemptReuse) {
                    EntityUtils.consume(this.wrappedEntity);
                    this.managedConn.markReusable();
                }
                releaseManagedConnection();
            } catch (Throwable th) {
                releaseManagedConnection();
            }
        }
    }

    public void abortConnection() throws IOException {
        if (this.managedConn != null) {
            try {
                this.managedConn.abortConnection();
            } finally {
                this.managedConn = null;
            }
        }
    }

    public void consumeContent() throws IOException {
        ensureConsumed();
    }

    public boolean eofDetected(InputStream inputStream) throws IOException {
        try {
            if (this.attemptReuse && this.managedConn != null) {
                inputStream.close();
                this.managedConn.markReusable();
            }
            releaseManagedConnection();
            return false;
        } catch (Throwable th) {
            releaseManagedConnection();
        }
    }

    public InputStream getContent() throws IOException {
        return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
    }

    public boolean isRepeatable() {
        return false;
    }

    public void releaseConnection() throws IOException {
        ensureConsumed();
    }

    protected void releaseManagedConnection() throws IOException {
        if (this.managedConn != null) {
            try {
                this.managedConn.releaseConnection();
            } finally {
                this.managedConn = null;
            }
        }
    }

    public boolean streamAbort(InputStream inputStream) throws IOException {
        if (this.managedConn != null) {
            this.managedConn.abortConnection();
        }
        return false;
    }

    public boolean streamClosed(InputStream inputStream) throws IOException {
        boolean isOpen;
        try {
            if (this.attemptReuse && this.managedConn != null) {
                isOpen = this.managedConn.isOpen();
                inputStream.close();
                this.managedConn.markReusable();
            }
        } catch (SocketException e) {
            if (isOpen) {
                throw e;
            }
        } catch (Throwable th) {
            releaseManagedConnection();
        }
        releaseManagedConnection();
        return false;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        super.writeTo(outputStream);
        ensureConsumed();
    }
}
