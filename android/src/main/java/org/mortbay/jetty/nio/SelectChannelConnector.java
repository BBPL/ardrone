package org.mortbay.jetty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.mortbay.io.Connection;
import org.mortbay.io.EndPoint;
import org.mortbay.io.nio.SelectChannelEndPoint;
import org.mortbay.io.nio.SelectorManager;
import org.mortbay.io.nio.SelectorManager.SelectSet;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.RetryRequest;
import org.mortbay.log.Log;
import org.mortbay.thread.Timeout.Task;
import org.mortbay.util.ajax.Continuation;

public class SelectChannelConnector extends AbstractNIOConnector {
    protected transient ServerSocketChannel _acceptChannel;
    private long _lowResourcesConnections;
    private long _lowResourcesMaxIdleTime;
    private SelectorManager _manager = new C13341(this);

    class C13341 extends SelectorManager {
        private final SelectChannelConnector this$0;

        C13341(SelectChannelConnector selectChannelConnector) {
            this.this$0 = selectChannelConnector;
        }

        protected SocketChannel acceptChannel(SelectionKey selectionKey) throws IOException {
            SocketChannel accept = ((ServerSocketChannel) selectionKey.channel()).accept();
            if (accept == null) {
                return null;
            }
            accept.configureBlocking(false);
            SelectChannelConnector.access$000(this.this$0, accept.socket());
            return accept;
        }

        public boolean dispatch(Runnable runnable) throws IOException {
            return this.this$0.getThreadPool().dispatch(runnable);
        }

        protected void endPointClosed(SelectChannelEndPoint selectChannelEndPoint) {
            SelectChannelConnector.access$100(this.this$0, (HttpConnection) selectChannelEndPoint.getConnection());
        }

        protected void endPointOpened(SelectChannelEndPoint selectChannelEndPoint) {
            SelectChannelConnector.access$200(this.this$0, (HttpConnection) selectChannelEndPoint.getConnection());
        }

        protected Connection newConnection(SocketChannel socketChannel, SelectChannelEndPoint selectChannelEndPoint) {
            return this.this$0.newConnection(socketChannel, selectChannelEndPoint);
        }

        protected SelectChannelEndPoint newEndPoint(SocketChannel socketChannel, SelectSet selectSet, SelectionKey selectionKey) throws IOException {
            return this.this$0.newEndPoint(socketChannel, selectSet, selectionKey);
        }
    }

    public static class ConnectorEndPoint extends SelectChannelEndPoint {
        public ConnectorEndPoint(SocketChannel socketChannel, SelectSet selectSet, SelectionKey selectionKey) {
            super(socketChannel, selectSet, selectionKey);
            scheduleIdle();
        }

        public void close() throws IOException {
            if (getConnection() instanceof HttpConnection) {
                RetryContinuation retryContinuation = (RetryContinuation) ((HttpConnection) getConnection()).getRequest().getContinuation();
                if (retryContinuation != null && retryContinuation.isPending()) {
                    retryContinuation.reset();
                }
            }
            super.close();
        }

        public void undispatch() {
            if (getConnection() instanceof HttpConnection) {
                RetryContinuation retryContinuation = (RetryContinuation) ((HttpConnection) getConnection()).getRequest().getContinuation();
                if (retryContinuation != null) {
                    Log.debug("continuation {}", retryContinuation);
                    if (retryContinuation.undispatch()) {
                        super.undispatch();
                        return;
                    }
                    return;
                }
                super.undispatch();
                return;
            }
            super.undispatch();
        }
    }

    public static class RetryContinuation extends Task implements Continuation, Runnable {
        SelectChannelEndPoint _endPoint = ((SelectChannelEndPoint) HttpConnection.getCurrentConnection().getEndPoint());
        boolean _new = true;
        Object _object;
        boolean _parked = false;
        boolean _pending = false;
        boolean _resumed = false;
        RetryRequest _retry;
        long _timeout;

        public void expire() {
            Object obj = null;
            synchronized (this) {
                if (this._parked && this._pending && !this._resumed) {
                    obj = 1;
                }
                this._parked = false;
            }
            if (obj != null) {
                this._endPoint.scheduleIdle();
                this._endPoint.getSelectSet().addChange(this);
                this._endPoint.getSelectSet().wakeup();
            }
        }

        public Object getObject() {
            return this._object;
        }

        public long getTimeout() {
            return this._timeout;
        }

        public boolean isNew() {
            return this._new;
        }

        public boolean isPending() {
            return this._pending;
        }

        public boolean isResumed() {
            return this._resumed;
        }

        public void reset() {
            synchronized (this) {
                this._resumed = false;
                this._pending = false;
                this._parked = false;
            }
            synchronized (this._endPoint.getSelectSet()) {
                cancel();
            }
        }

        public void resume() {
            boolean z = false;
            synchronized (this) {
                if (this._pending && !isExpired()) {
                    this._resumed = true;
                    z = this._parked;
                    this._parked = false;
                }
            }
            if (z) {
                SelectSet selectSet = this._endPoint.getSelectSet();
                synchronized (selectSet) {
                    cancel();
                }
                this._endPoint.scheduleIdle();
                selectSet.addChange(this);
                selectSet.wakeup();
            }
        }

        public void run() {
            this._endPoint.run();
        }

        public void setObject(Object obj) {
            this._object = obj;
        }

        public boolean suspend(long j) {
            boolean z;
            synchronized (this) {
                z = this._resumed;
                this._resumed = false;
                this._new = false;
                if (this._pending || z || j < 0) {
                    this._resumed = false;
                    this._pending = false;
                    this._parked = false;
                } else {
                    this._pending = true;
                    this._parked = false;
                    this._timeout = j;
                    if (this._retry == null) {
                        this._retry = new RetryRequest();
                    }
                    throw this._retry;
                }
            }
            synchronized (this._endPoint.getSelectSet()) {
                cancel();
            }
            return z;
        }

        public String toString() {
            String stringBuffer;
            synchronized (this) {
                stringBuffer = new StringBuffer().append("RetryContinuation@").append(hashCode()).append(this._new ? ",new" : HttpVersions.HTTP_0_9).append(this._pending ? ",pending" : HttpVersions.HTTP_0_9).append(this._resumed ? ",resumed" : HttpVersions.HTTP_0_9).append(isExpired() ? ",expired" : HttpVersions.HTTP_0_9).append(this._parked ? ",parked" : HttpVersions.HTTP_0_9).toString();
            }
            return stringBuffer;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean undispatch() {
            /*
            r6 = this;
            r0 = 1;
            r1 = 0;
            monitor-enter(r6);
            r2 = r6._pending;	 Catch:{ all -> 0x0038 }
            if (r2 != 0) goto L_0x0009;
        L_0x0007:
            monitor-exit(r6);	 Catch:{ all -> 0x0038 }
        L_0x0008:
            return r0;
        L_0x0009:
            r2 = r6.isExpired();	 Catch:{ all -> 0x0038 }
            if (r2 != 0) goto L_0x0013;
        L_0x000f:
            r2 = r6._resumed;	 Catch:{ all -> 0x0038 }
            if (r2 == 0) goto L_0x0034;
        L_0x0013:
            r2 = r0;
        L_0x0014:
            if (r2 != 0) goto L_0x0036;
        L_0x0016:
            r6._parked = r0;	 Catch:{ all -> 0x0038 }
            monitor-exit(r6);	 Catch:{ all -> 0x0038 }
            if (r2 == 0) goto L_0x003b;
        L_0x001b:
            r0 = r6._endPoint;
            r0.scheduleIdle();
            r0 = r6._endPoint;
            r0 = r0.getSelectSet();
            r0.addChange(r6);
        L_0x0029:
            r0 = r6._endPoint;
            r0 = r0.getSelectSet();
            r0.wakeup();
            r0 = r1;
            goto L_0x0008;
        L_0x0034:
            r2 = r1;
            goto L_0x0014;
        L_0x0036:
            r0 = r1;
            goto L_0x0016;
        L_0x0038:
            r0 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x0038 }
            throw r0;
        L_0x003b:
            r2 = r6._timeout;
            r4 = 0;
            r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r0 <= 0) goto L_0x0029;
        L_0x0043:
            r0 = r6._endPoint;
            r0 = r0.getSelectSet();
            r2 = r6._timeout;
            r0.scheduleTimeout(r6, r2);
            goto L_0x0029;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.nio.SelectChannelConnector.RetryContinuation.undispatch():boolean");
        }
    }

    static void access$000(SelectChannelConnector selectChannelConnector, Socket socket) throws IOException {
        selectChannelConnector.configure(socket);
    }

    static void access$100(SelectChannelConnector selectChannelConnector, HttpConnection httpConnection) {
        selectChannelConnector.connectionClosed(httpConnection);
    }

    static void access$200(SelectChannelConnector selectChannelConnector, HttpConnection httpConnection) {
        selectChannelConnector.connectionOpened(httpConnection);
    }

    public void accept(int i) throws IOException {
        this._manager.doSelect(i);
    }

    public void close() throws IOException {
        synchronized (this) {
            if (this._manager.isRunning()) {
                try {
                    this._manager.stop();
                } catch (Throwable e) {
                    Log.warn(e);
                }
            }
            if (this._acceptChannel != null) {
                this._acceptChannel.close();
            }
            this._acceptChannel = null;
        }
    }

    public void customize(EndPoint endPoint, Request request) throws IOException {
        ConnectorEndPoint connectorEndPoint = (ConnectorEndPoint) endPoint;
        connectorEndPoint.cancelIdle();
        request.setTimeStamp(connectorEndPoint.getSelectSet().getNow());
        super.customize(endPoint, request);
    }

    protected void doStart() throws Exception {
        this._manager.setSelectSets(getAcceptors());
        this._manager.setMaxIdleTime((long) getMaxIdleTime());
        this._manager.setLowResourcesConnections(getLowResourcesConnections());
        this._manager.setLowResourcesMaxIdleTime(getLowResourcesMaxIdleTime());
        this._manager.start();
        open();
        this._manager.register(this._acceptChannel);
        super.doStart();
    }

    protected void doStop() throws Exception {
        super.doStop();
    }

    public Object getConnection() {
        return this._acceptChannel;
    }

    public boolean getDelaySelectKeyUpdate() {
        return this._manager.isDelaySelectKeyUpdate();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getLocalPort() {
        /*
        r1 = this;
        monitor-enter(r1);
        r0 = r1._acceptChannel;	 Catch:{ all -> 0x001c }
        if (r0 == 0) goto L_0x000d;
    L_0x0005:
        r0 = r1._acceptChannel;	 Catch:{ all -> 0x001c }
        r0 = r0.isOpen();	 Catch:{ all -> 0x001c }
        if (r0 != 0) goto L_0x0010;
    L_0x000d:
        monitor-exit(r1);	 Catch:{ all -> 0x001c }
        r0 = -1;
    L_0x000f:
        return r0;
    L_0x0010:
        r0 = r1._acceptChannel;	 Catch:{ all -> 0x001c }
        r0 = r0.socket();	 Catch:{ all -> 0x001c }
        r0 = r0.getLocalPort();	 Catch:{ all -> 0x001c }
        monitor-exit(r1);	 Catch:{ all -> 0x001c }
        goto L_0x000f;
    L_0x001c:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001c }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.nio.SelectChannelConnector.getLocalPort():int");
    }

    public long getLowResourcesConnections() {
        return this._lowResourcesConnections;
    }

    public long getLowResourcesMaxIdleTime() {
        return this._lowResourcesMaxIdleTime;
    }

    protected Connection newConnection(SocketChannel socketChannel, SelectChannelEndPoint selectChannelEndPoint) {
        return new HttpConnection(this, selectChannelEndPoint, getServer());
    }

    public Continuation newContinuation() {
        return new RetryContinuation();
    }

    protected SelectChannelEndPoint newEndPoint(SocketChannel socketChannel, SelectSet selectSet, SelectionKey selectionKey) throws IOException {
        return new ConnectorEndPoint(socketChannel, selectSet, selectionKey);
    }

    public void open() throws IOException {
        synchronized (this) {
            if (this._acceptChannel == null) {
                this._acceptChannel = ServerSocketChannel.open();
                this._acceptChannel.socket().setReuseAddress(getReuseAddress());
                this._acceptChannel.socket().bind(getHost() == null ? new InetSocketAddress(getPort()) : new InetSocketAddress(getHost(), getPort()), getAcceptQueueSize());
                this._acceptChannel.configureBlocking(false);
            }
        }
    }

    public void persist(EndPoint endPoint) throws IOException {
        ((ConnectorEndPoint) endPoint).scheduleIdle();
        super.persist(endPoint);
    }

    public void setDelaySelectKeyUpdate(boolean z) {
        this._manager.setDelaySelectKeyUpdate(z);
    }

    public void setLowResourceMaxIdleTime(int i) {
        this._lowResourcesMaxIdleTime = (long) i;
        super.setLowResourceMaxIdleTime(i);
    }

    public void setLowResourcesConnections(long j) {
        this._lowResourcesConnections = j;
    }

    public void setLowResourcesMaxIdleTime(long j) {
        this._lowResourcesMaxIdleTime = j;
        super.setLowResourceMaxIdleTime((int) j);
    }

    public void setMaxIdleTime(int i) {
        this._manager.setMaxIdleTime((long) i);
        super.setMaxIdleTime(i);
    }
}
