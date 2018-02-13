package org.apache.http.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import org.apache.http.HttpInetConnection;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.impl.io.SocketInputBuffer;
import org.apache.http.impl.io.SocketOutputBuffer;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

@NotThreadSafe
public class SocketHttpServerConnection extends AbstractHttpServerConnection implements HttpInetConnection {
    private volatile boolean open;
    private volatile Socket socket = null;

    private static void formatAddress(StringBuilder stringBuilder, SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            stringBuilder.append(inetSocketAddress.getAddress() != null ? inetSocketAddress.getAddress().getHostAddress() : inetSocketAddress.getAddress()).append(':').append(inetSocketAddress.getPort());
            return;
        }
        stringBuilder.append(socketAddress);
    }

    protected void assertNotOpen() {
        if (this.open) {
            throw new IllegalStateException("Connection is already open");
        }
    }

    protected void assertOpen() {
        if (!this.open) {
            throw new IllegalStateException("Connection is not open");
        }
    }

    protected void bind(Socket socket, HttpParams httpParams) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("Socket may not be null");
        } else if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            this.socket = socket;
            int socketBufferSize = HttpConnectionParams.getSocketBufferSize(httpParams);
            init(createSessionInputBuffer(socket, socketBufferSize, httpParams), createSessionOutputBuffer(socket, socketBufferSize, httpParams), httpParams);
            this.open = true;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws java.io.IOException {
        /*
        r2 = this;
        r1 = 0;
        r0 = r2.open;
        if (r0 != 0) goto L_0x0006;
    L_0x0005:
        return;
    L_0x0006:
        r2.open = r1;
        r2.open = r1;
        r1 = r2.socket;
        r2.doFlush();	 Catch:{ all -> 0x0019 }
        r1.shutdownOutput();	 Catch:{ IOException -> 0x001e, UnsupportedOperationException -> 0x0022 }
    L_0x0012:
        r1.shutdownInput();	 Catch:{ IOException -> 0x0020, UnsupportedOperationException -> 0x0022 }
    L_0x0015:
        r1.close();
        goto L_0x0005;
    L_0x0019:
        r0 = move-exception;
        r1.close();
        throw r0;
    L_0x001e:
        r0 = move-exception;
        goto L_0x0012;
    L_0x0020:
        r0 = move-exception;
        goto L_0x0015;
    L_0x0022:
        r0 = move-exception;
        goto L_0x0015;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.SocketHttpServerConnection.close():void");
    }

    protected SessionInputBuffer createSessionInputBuffer(Socket socket, int i, HttpParams httpParams) throws IOException {
        return new SocketInputBuffer(socket, i, httpParams);
    }

    protected SessionOutputBuffer createSessionOutputBuffer(Socket socket, int i, HttpParams httpParams) throws IOException {
        return new SocketOutputBuffer(socket, i, httpParams);
    }

    public InetAddress getLocalAddress() {
        return this.socket != null ? this.socket.getLocalAddress() : null;
    }

    public int getLocalPort() {
        return this.socket != null ? this.socket.getLocalPort() : -1;
    }

    public InetAddress getRemoteAddress() {
        return this.socket != null ? this.socket.getInetAddress() : null;
    }

    public int getRemotePort() {
        return this.socket != null ? this.socket.getPort() : -1;
    }

    protected Socket getSocket() {
        return this.socket;
    }

    public int getSocketTimeout() {
        int i = -1;
        if (this.socket != null) {
            try {
                i = this.socket.getSoTimeout();
            } catch (SocketException e) {
            }
        }
        return i;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setSocketTimeout(int i) {
        assertOpen();
        if (this.socket != null) {
            try {
                this.socket.setSoTimeout(i);
            } catch (SocketException e) {
            }
        }
    }

    public void shutdown() throws IOException {
        this.open = false;
        Socket socket = this.socket;
        if (socket != null) {
            socket.close();
        }
    }

    public String toString() {
        if (this.socket == null) {
            return super.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        SocketAddress remoteSocketAddress = this.socket.getRemoteSocketAddress();
        SocketAddress localSocketAddress = this.socket.getLocalSocketAddress();
        if (!(remoteSocketAddress == null || localSocketAddress == null)) {
            formatAddress(stringBuilder, localSocketAddress);
            stringBuilder.append("<->");
            formatAddress(stringBuilder, remoteSocketAddress);
        }
        return stringBuilder.toString();
    }
}
