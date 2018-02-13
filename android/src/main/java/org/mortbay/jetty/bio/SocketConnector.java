package org.mortbay.jetty.bio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import org.mortbay.io.Buffer;
import org.mortbay.io.ByteArrayBuffer;
import org.mortbay.io.EndPoint;
import org.mortbay.io.bio.SocketEndPoint;
import org.mortbay.jetty.AbstractConnector;
import org.mortbay.jetty.EofException;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.HttpException;
import org.mortbay.jetty.Request;
import org.mortbay.log.Log;

public class SocketConnector extends AbstractConnector {
    protected Set _connections;
    protected ServerSocket _serverSocket;

    protected class Connection extends SocketEndPoint implements Runnable {
        HttpConnection _connection;
        boolean _dispatched = false;
        protected Socket _socket;
        int _sotimeout;
        private final SocketConnector this$0;

        public Connection(SocketConnector socketConnector, Socket socket) throws IOException {
            this.this$0 = socketConnector;
            super(socket);
            this._connection = socketConnector.newHttpConnection(this);
            this._sotimeout = socket.getSoTimeout();
            this._socket = socket;
        }

        public void dispatch() throws InterruptedException, IOException {
            if (this.this$0.getThreadPool() == null || !this.this$0.getThreadPool().dispatch(this)) {
                Log.warn("dispatch failed for {}", this._connection);
                close();
            }
        }

        public int fill(Buffer buffer) throws IOException {
            int fill = super.fill(buffer);
            if (fill < 0) {
                close();
            }
            return fill;
        }

        public void run() {
            try {
                SocketConnector.access$000(this.this$0, this._connection);
                synchronized (this.this$0._connections) {
                    this.this$0._connections.add(this);
                }
                while (this.this$0.isStarted() && !isClosed()) {
                    if (this._connection.isIdle() && this.this$0.getServer().getThreadPool().isLowOnThreads()) {
                        int lowResourceMaxIdleTime = this.this$0.getLowResourceMaxIdleTime();
                        if (lowResourceMaxIdleTime >= 0 && this._sotimeout != lowResourceMaxIdleTime) {
                            this._sotimeout = lowResourceMaxIdleTime;
                            this._socket.setSoTimeout(this._sotimeout);
                        }
                    }
                    this._connection.handle();
                }
                SocketConnector.access$100(this.this$0, this._connection);
                synchronized (this.this$0._connections) {
                    this.this$0._connections.remove(this);
                }
            } catch (EofException e) {
                Log.debug("EOF", e);
                try {
                    close();
                } catch (Throwable e2) {
                    Log.ignore(e2);
                }
                SocketConnector.access$100(this.this$0, this._connection);
                synchronized (this.this$0._connections) {
                    this.this$0._connections.remove(this);
                }
            } catch (HttpException e3) {
                Log.debug("BAD", e3);
                try {
                    close();
                } catch (Throwable e22) {
                    Log.ignore(e22);
                }
                SocketConnector.access$100(this.this$0, this._connection);
                synchronized (this.this$0._connections) {
                    this.this$0._connections.remove(this);
                }
            } catch (Throwable th) {
                SocketConnector.access$100(this.this$0, this._connection);
                synchronized (this.this$0._connections) {
                    this.this$0._connections.remove(this);
                }
            }
        }
    }

    static void access$000(SocketConnector socketConnector, HttpConnection httpConnection) {
        socketConnector.connectionOpened(httpConnection);
    }

    static void access$100(SocketConnector socketConnector, HttpConnection httpConnection) {
        socketConnector.connectionClosed(httpConnection);
    }

    public void accept(int i) throws IOException, InterruptedException {
        Socket accept = this._serverSocket.accept();
        configure(accept);
        new Connection(this, accept).dispatch();
    }

    public void close() throws IOException {
        if (this._serverSocket != null) {
            this._serverSocket.close();
        }
        this._serverSocket = null;
    }

    public void customize(EndPoint endPoint, Request request) throws IOException {
        Connection connection = (Connection) endPoint;
        if (connection._sotimeout != this._maxIdleTime) {
            connection._sotimeout = this._maxIdleTime;
            ((Socket) endPoint.getTransport()).setSoTimeout(this._maxIdleTime);
        }
        super.customize(endPoint, request);
    }

    protected void doStart() throws Exception {
        this._connections = new HashSet();
        super.doStart();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void doStop() throws java.lang.Exception {
        /*
        r3 = this;
        super.doStop();
        r1 = r3._connections;
        monitor-enter(r1);
        r0 = new java.util.HashSet;	 Catch:{ all -> 0x0026 }
        r2 = r3._connections;	 Catch:{ all -> 0x0026 }
        r0.<init>(r2);	 Catch:{ all -> 0x0026 }
        monitor-exit(r1);	 Catch:{ all -> 0x0022 }
        r1 = r0.iterator();
    L_0x0012:
        r0 = r1.hasNext();
        if (r0 == 0) goto L_0x0025;
    L_0x0018:
        r0 = r1.next();
        r0 = (org.mortbay.jetty.bio.SocketConnector.Connection) r0;
        r0.close();
        goto L_0x0012;
    L_0x0022:
        r0 = move-exception;
    L_0x0023:
        monitor-exit(r1);	 Catch:{ all -> 0x0026 }
        throw r0;
    L_0x0025:
        return;
    L_0x0026:
        r0 = move-exception;
        goto L_0x0023;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.bio.SocketConnector.doStop():void");
    }

    public Object getConnection() {
        return this._serverSocket;
    }

    public int getLocalPort() {
        return (this._serverSocket == null || this._serverSocket.isClosed()) ? -1 : this._serverSocket.getLocalPort();
    }

    protected Buffer newBuffer(int i) {
        return new ByteArrayBuffer(i);
    }

    protected HttpConnection newHttpConnection(EndPoint endPoint) {
        return new HttpConnection(this, endPoint, getServer());
    }

    protected ServerSocket newServerSocket(String str, int i, int i2) throws IOException {
        return str == null ? new ServerSocket(i, i2) : new ServerSocket(i, i2, InetAddress.getByName(str));
    }

    public void open() throws IOException {
        if (this._serverSocket == null || this._serverSocket.isClosed()) {
            this._serverSocket = newServerSocket(getHost(), getPort(), getAcceptQueueSize());
        }
        this._serverSocket.setReuseAddress(getReuseAddress());
    }
}
