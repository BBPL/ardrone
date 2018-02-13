package org.mortbay.jetty;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import org.mortbay.component.LifeCycle;
import org.mortbay.io.EndPoint;
import org.mortbay.io.Portable;
import org.mortbay.log.Log;
import org.mortbay.thread.ThreadPool;
import org.mortbay.util.ajax.Continuation;
import org.mortbay.util.ajax.WaitingContinuation;

public abstract class AbstractConnector extends AbstractBuffers implements Connector {
    private int _acceptQueueSize = 0;
    private int _acceptorPriorityOffset = 0;
    private transient Thread[] _acceptorThread;
    private int _acceptors = 1;
    private int _confidentialPort = 0;
    private String _confidentialScheme = "https";
    transient int _connections;
    transient long _connectionsDurationMax;
    transient long _connectionsDurationMin;
    transient long _connectionsDurationTotal;
    transient int _connectionsOpen;
    transient int _connectionsOpenMax;
    transient int _connectionsOpenMin;
    transient int _connectionsRequestsMax;
    transient int _connectionsRequestsMin;
    private boolean _forwarded;
    private String _forwardedForHeader = "X-Forwarded-For";
    private String _forwardedHostHeader = "X-Forwarded-Host";
    private String _forwardedServerHeader = "X-Forwarded-Server";
    private String _host;
    private String _hostHeader;
    private int _integralPort = 0;
    private String _integralScheme = "https";
    protected int _lowResourceMaxIdleTime = -1;
    protected int _maxIdleTime = 200000;
    private String _name;
    private int _port = 0;
    transient int _requests;
    private boolean _reuseAddress = true;
    private Server _server;
    protected int _soLingerTime = -1;
    Object _statsLock = new Object();
    transient long _statsStartedAt = -1;
    private ThreadPool _threadPool;
    private boolean _useDNS;

    private class Acceptor implements Runnable {
        int _acceptor = 0;
        private final AbstractConnector this$0;

        Acceptor(AbstractConnector abstractConnector, int i) {
            this.this$0 = abstractConnector;
            this._acceptor = i;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r5 = this;
            r1 = java.lang.Thread.currentThread();
            r2 = r5.this$0;
            monitor-enter(r2);
            r0 = r5.this$0;	 Catch:{ all -> 0x009d }
            r0 = org.mortbay.jetty.AbstractConnector.access$000(r0);	 Catch:{ all -> 0x009d }
            if (r0 != 0) goto L_0x0011;
        L_0x000f:
            monitor-exit(r2);	 Catch:{ all -> 0x009d }
        L_0x0010:
            return;
        L_0x0011:
            r0 = r5.this$0;	 Catch:{ all -> 0x009d }
            r0 = org.mortbay.jetty.AbstractConnector.access$000(r0);	 Catch:{ all -> 0x009d }
            r3 = r5._acceptor;	 Catch:{ all -> 0x009d }
            r0[r3] = r1;	 Catch:{ all -> 0x009d }
            r0 = r5.this$0;	 Catch:{ all -> 0x009d }
            r0 = org.mortbay.jetty.AbstractConnector.access$000(r0);	 Catch:{ all -> 0x009d }
            r3 = r5._acceptor;	 Catch:{ all -> 0x009d }
            r0 = r0[r3];	 Catch:{ all -> 0x009d }
            r3 = r0.getName();	 Catch:{ all -> 0x009d }
            r0 = new java.lang.StringBuffer;	 Catch:{ all -> 0x009d }
            r0.<init>();	 Catch:{ all -> 0x009d }
            r0 = r0.append(r3);	 Catch:{ all -> 0x009d }
            r4 = " - Acceptor";
            r0 = r0.append(r4);	 Catch:{ all -> 0x009d }
            r4 = r5._acceptor;	 Catch:{ all -> 0x009d }
            r0 = r0.append(r4);	 Catch:{ all -> 0x009d }
            r4 = " ";
            r0 = r0.append(r4);	 Catch:{ all -> 0x009d }
            r4 = r5.this$0;	 Catch:{ all -> 0x009d }
            r0 = r0.append(r4);	 Catch:{ all -> 0x009d }
            r0 = r0.toString();	 Catch:{ all -> 0x009d }
            r1.setName(r0);	 Catch:{ all -> 0x009d }
            monitor-exit(r2);	 Catch:{ all -> 0x009d }
            r2 = r1.getPriority();
            r0 = r5.this$0;	 Catch:{ all -> 0x007e }
            r0 = org.mortbay.jetty.AbstractConnector.access$100(r0);	 Catch:{ all -> 0x007e }
            r0 = r2 - r0;
            r1.setPriority(r0);	 Catch:{ all -> 0x007e }
        L_0x0061:
            r0 = r5.this$0;	 Catch:{ all -> 0x007e }
            r0 = r0.isRunning();	 Catch:{ all -> 0x007e }
            if (r0 == 0) goto L_0x00af;
        L_0x0069:
            r0 = r5.this$0;	 Catch:{ all -> 0x007e }
            r0 = r0.getConnection();	 Catch:{ all -> 0x007e }
            if (r0 == 0) goto L_0x00af;
        L_0x0071:
            r0 = r5.this$0;	 Catch:{ EofException -> 0x0079, IOException -> 0x00a0, ThreadDeath -> 0x00a5, Throwable -> 0x00a7 }
            r4 = r5._acceptor;	 Catch:{ EofException -> 0x0079, IOException -> 0x00a0, ThreadDeath -> 0x00a5, Throwable -> 0x00a7 }
            r0.accept(r4);	 Catch:{ EofException -> 0x0079, IOException -> 0x00a0, ThreadDeath -> 0x00a5, Throwable -> 0x00a7 }
            goto L_0x0061;
        L_0x0079:
            r0 = move-exception;
            org.mortbay.log.Log.ignore(r0);	 Catch:{ all -> 0x007e }
            goto L_0x0061;
        L_0x007e:
            r0 = move-exception;
            r1.setPriority(r2);
            r1.setName(r3);
            r1 = r5.this$0;
            monitor-enter(r1);
            r2 = r5.this$0;	 Catch:{ all -> 0x00ac }
            r2 = org.mortbay.jetty.AbstractConnector.access$000(r2);	 Catch:{ all -> 0x00ac }
            if (r2 == 0) goto L_0x009b;
        L_0x0090:
            r2 = r5.this$0;	 Catch:{ all -> 0x00ac }
            r2 = org.mortbay.jetty.AbstractConnector.access$000(r2);	 Catch:{ all -> 0x00ac }
            r3 = r5._acceptor;	 Catch:{ all -> 0x00ac }
            r4 = 0;
            r2[r3] = r4;	 Catch:{ all -> 0x00ac }
        L_0x009b:
            monitor-exit(r1);	 Catch:{ all -> 0x00ac }
            throw r0;
        L_0x009d:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x009d }
            throw r0;
        L_0x00a0:
            r0 = move-exception;
            org.mortbay.log.Log.ignore(r0);	 Catch:{ all -> 0x007e }
            goto L_0x0061;
        L_0x00a5:
            r0 = move-exception;
            throw r0;	 Catch:{ all -> 0x007e }
        L_0x00a7:
            r0 = move-exception;
            org.mortbay.log.Log.warn(r0);	 Catch:{ all -> 0x007e }
            goto L_0x0061;
        L_0x00ac:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x00ac }
            throw r0;
        L_0x00af:
            r1.setPriority(r2);
            r1.setName(r3);
            r1 = r5.this$0;
            monitor-enter(r1);
            r0 = r5.this$0;	 Catch:{ all -> 0x00ce }
            r0 = org.mortbay.jetty.AbstractConnector.access$000(r0);	 Catch:{ all -> 0x00ce }
            if (r0 == 0) goto L_0x00cb;
        L_0x00c0:
            r0 = r5.this$0;	 Catch:{ all -> 0x00ce }
            r0 = org.mortbay.jetty.AbstractConnector.access$000(r0);	 Catch:{ all -> 0x00ce }
            r2 = r5._acceptor;	 Catch:{ all -> 0x00ce }
            r3 = 0;
            r0[r2] = r3;	 Catch:{ all -> 0x00ce }
        L_0x00cb:
            monitor-exit(r1);	 Catch:{ all -> 0x00ce }
            goto L_0x0010;
        L_0x00ce:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x00ce }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.AbstractConnector.Acceptor.run():void");
        }
    }

    static Thread[] access$000(AbstractConnector abstractConnector) {
        return abstractConnector._acceptorThread;
    }

    static int access$100(AbstractConnector abstractConnector) {
        return abstractConnector._acceptorPriorityOffset;
    }

    protected abstract void accept(int i) throws IOException, InterruptedException;

    protected void checkForwardedHeaders(EndPoint endPoint, Request request) throws IOException {
        InetAddress inetAddress = null;
        HttpFields requestFields = request.getConnection().getRequestFields();
        String leftMostValue = getLeftMostValue(requestFields.getStringField(getForwardedHostHeader()));
        String leftMostValue2 = getLeftMostValue(requestFields.getStringField(getForwardedServerHeader()));
        String leftMostValue3 = getLeftMostValue(requestFields.getStringField(getForwardedForHeader()));
        if (this._hostHeader != null) {
            requestFields.put(HttpHeaders.HOST_BUFFER, this._hostHeader);
            request.setServerName(inetAddress);
            request.setServerPort(-1);
            request.getServerName();
        } else if (leftMostValue != null) {
            requestFields.put(HttpHeaders.HOST_BUFFER, leftMostValue);
            request.setServerName(inetAddress);
            request.setServerPort(-1);
            request.getServerName();
        } else if (leftMostValue2 != null) {
            request.setServerName(leftMostValue2);
        }
        if (leftMostValue3 != null) {
            request.setRemoteAddr(leftMostValue3);
            if (this._useDNS) {
                try {
                    inetAddress = InetAddress.getByName(leftMostValue3);
                } catch (Throwable e) {
                    Log.ignore(e);
                }
            }
            if (inetAddress != null) {
                leftMostValue3 = inetAddress.getHostName();
            }
            request.setRemoteHost(leftMostValue3);
        }
    }

    protected void configure(Socket socket) throws IOException {
        try {
            socket.setTcpNoDelay(true);
            if (this._maxIdleTime >= 0) {
                socket.setSoTimeout(this._maxIdleTime);
            }
            if (this._soLingerTime >= 0) {
                socket.setSoLinger(true, this._soLingerTime / 1000);
            } else {
                socket.setSoLinger(false, 0);
            }
        } catch (Throwable e) {
            Log.ignore(e);
        }
    }

    protected void connectionClosed(HttpConnection httpConnection) {
        if (this._statsStartedAt >= 0) {
            long currentTimeMillis = System.currentTimeMillis() - httpConnection.getTimeStamp();
            int requests = httpConnection.getRequests();
            synchronized (this._statsLock) {
                this._requests += requests;
                this._connections++;
                this._connectionsOpen--;
                this._connectionsDurationTotal += currentTimeMillis;
                if (this._connectionsOpen < 0) {
                    this._connectionsOpen = 0;
                }
                if (this._connectionsOpen < this._connectionsOpenMin) {
                    this._connectionsOpenMin = this._connectionsOpen;
                }
                if (this._connectionsDurationMin == 0 || currentTimeMillis < this._connectionsDurationMin) {
                    this._connectionsDurationMin = currentTimeMillis;
                }
                if (currentTimeMillis > this._connectionsDurationMax) {
                    this._connectionsDurationMax = currentTimeMillis;
                }
                if (this._connectionsRequestsMin == 0 || requests < this._connectionsRequestsMin) {
                    this._connectionsRequestsMin = requests;
                }
                if (requests > this._connectionsRequestsMax) {
                    this._connectionsRequestsMax = requests;
                }
            }
        }
        httpConnection.destroy();
    }

    protected void connectionOpened(HttpConnection httpConnection) {
        if (this._statsStartedAt != -1) {
            synchronized (this._statsLock) {
                this._connectionsOpen++;
                if (this._connectionsOpen > this._connectionsOpenMax) {
                    this._connectionsOpenMax = this._connectionsOpen;
                }
            }
        }
    }

    public void customize(EndPoint endPoint, Request request) throws IOException {
        if (isForwarded()) {
            checkForwardedHeaders(endPoint, request);
        }
    }

    protected void doStart() throws Exception {
        if (this._server == null) {
            throw new IllegalStateException("No server");
        }
        open();
        super.doStart();
        if (this._threadPool == null) {
            this._threadPool = this._server.getThreadPool();
        }
        if (this._threadPool != this._server.getThreadPool() && (this._threadPool instanceof LifeCycle)) {
            ((LifeCycle) this._threadPool).start();
        }
        synchronized (this) {
            this._acceptorThread = new Thread[getAcceptors()];
            for (int i = 0; i < this._acceptorThread.length; i++) {
                if (!this._threadPool.dispatch(new Acceptor(this, i))) {
                    Log.warn("insufficient maxThreads configured for {}", (Object) this);
                    break;
                }
            }
        }
        Log.info("Started {}", this);
    }

    protected void doStop() throws Exception {
        Log.info("Stopped {}", this);
        try {
            close();
        } catch (Throwable e) {
            Log.warn(e);
        }
        if (this._threadPool == this._server.getThreadPool()) {
            this._threadPool = null;
        } else if (this._threadPool instanceof LifeCycle) {
            ((LifeCycle) this._threadPool).stop();
        }
        super.doStop();
        synchronized (this) {
            Thread[] threadArr = this._acceptorThread;
            this._acceptorThread = null;
        }
        if (threadArr != null) {
            for (Thread thread : threadArr) {
                if (thread != null) {
                    thread.interrupt();
                }
            }
        }
    }

    public int getAcceptQueueSize() {
        return this._acceptQueueSize;
    }

    public int getAcceptorPriorityOffset() {
        return this._acceptorPriorityOffset;
    }

    public int getAcceptors() {
        return this._acceptors;
    }

    public int getConfidentialPort() {
        return this._confidentialPort;
    }

    public String getConfidentialScheme() {
        return this._confidentialScheme;
    }

    public int getConnections() {
        return this._connections;
    }

    public long getConnectionsDurationAve() {
        return this._connections == 0 ? 0 : this._connectionsDurationTotal / ((long) this._connections);
    }

    public long getConnectionsDurationMax() {
        return this._connectionsDurationMax;
    }

    public long getConnectionsDurationMin() {
        return this._connectionsDurationMin;
    }

    public long getConnectionsDurationTotal() {
        return this._connectionsDurationTotal;
    }

    public int getConnectionsOpen() {
        return this._connectionsOpen;
    }

    public int getConnectionsOpenMax() {
        return this._connectionsOpenMax;
    }

    public int getConnectionsOpenMin() {
        return this._connectionsOpenMin;
    }

    public int getConnectionsRequestsAve() {
        return this._connections == 0 ? 0 : this._requests / this._connections;
    }

    public int getConnectionsRequestsMax() {
        return this._connectionsRequestsMax;
    }

    public int getConnectionsRequestsMin() {
        return this._connectionsRequestsMin;
    }

    public String getForwardedForHeader() {
        return this._forwardedForHeader;
    }

    public String getForwardedHostHeader() {
        return this._forwardedHostHeader;
    }

    public String getForwardedServerHeader() {
        return this._forwardedServerHeader;
    }

    public String getHost() {
        return this._host;
    }

    public String getHostHeader() {
        return this._hostHeader;
    }

    public int getIntegralPort() {
        return this._integralPort;
    }

    public String getIntegralScheme() {
        return this._integralScheme;
    }

    protected String getLeftMostValue(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(44);
        return indexOf != -1 ? str.substring(0, indexOf) : str;
    }

    public int getLowResourceMaxIdleTime() {
        return this._lowResourceMaxIdleTime;
    }

    public int getMaxIdleTime() {
        return this._maxIdleTime;
    }

    public String getName() {
        if (this._name == null) {
            this._name = new StringBuffer().append(getHost() == null ? Portable.ALL_INTERFACES : getHost()).append(":").append(getLocalPort() <= 0 ? getPort() : getLocalPort()).toString();
        }
        return this._name;
    }

    public int getPort() {
        return this._port;
    }

    public int getRequests() {
        return this._requests;
    }

    public boolean getResolveNames() {
        return this._useDNS;
    }

    public boolean getReuseAddress() {
        return this._reuseAddress;
    }

    public Server getServer() {
        return this._server;
    }

    public int getSoLingerTime() {
        return this._soLingerTime;
    }

    public boolean getStatsOn() {
        return this._statsStartedAt != -1;
    }

    public long getStatsOnMs() {
        return this._statsStartedAt != -1 ? System.currentTimeMillis() - this._statsStartedAt : 0;
    }

    public ThreadPool getThreadPool() {
        return this._threadPool;
    }

    public boolean isConfidential(Request request) {
        return false;
    }

    public boolean isForwarded() {
        return this._forwarded;
    }

    public boolean isIntegral(Request request) {
        return false;
    }

    public void join() throws InterruptedException {
        Thread[] threadArr = this._acceptorThread;
        if (threadArr != null) {
            for (int i = 0; i < threadArr.length; i++) {
                if (threadArr[i] != null) {
                    threadArr[i].join();
                }
            }
        }
    }

    public Continuation newContinuation() {
        return new WaitingContinuation();
    }

    public void persist(EndPoint endPoint) throws IOException {
    }

    public void setAcceptQueueSize(int i) {
        this._acceptQueueSize = i;
    }

    public void setAcceptorPriorityOffset(int i) {
        this._acceptorPriorityOffset = i;
    }

    public void setAcceptors(int i) {
        this._acceptors = i;
    }

    public void setConfidentialPort(int i) {
        this._confidentialPort = i;
    }

    public void setConfidentialScheme(String str) {
        this._confidentialScheme = str;
    }

    public void setForwarded(boolean z) {
        if (z) {
            Log.debug(new StringBuffer().append(this).append(" is forwarded").toString());
        }
        this._forwarded = z;
    }

    public void setForwardedForHeader(String str) {
        this._forwardedForHeader = str;
    }

    public void setForwardedHostHeader(String str) {
        this._forwardedHostHeader = str;
    }

    public void setForwardedServerHeader(String str) {
        this._forwardedServerHeader = str;
    }

    public void setHost(String str) {
        this._host = str;
    }

    public void setHostHeader(String str) {
        this._hostHeader = str;
    }

    public void setIntegralPort(int i) {
        this._integralPort = i;
    }

    public void setIntegralScheme(String str) {
        this._integralScheme = str;
    }

    public void setLowResourceMaxIdleTime(int i) {
        this._lowResourceMaxIdleTime = i;
    }

    public void setMaxIdleTime(int i) {
        this._maxIdleTime = i;
    }

    public void setName(String str) {
        this._name = str;
    }

    public void setPort(int i) {
        this._port = i;
    }

    public void setResolveNames(boolean z) {
        this._useDNS = z;
    }

    public void setReuseAddress(boolean z) {
        this._reuseAddress = z;
    }

    public void setServer(Server server) {
        this._server = server;
    }

    public void setSoLingerTime(int i) {
        this._soLingerTime = i;
    }

    public void setStatsOn(boolean z) {
        long j = -1;
        if (!z || this._statsStartedAt == -1) {
            Log.debug(new StringBuffer().append("Statistics on = ").append(z).append(" for ").append(this).toString());
            statsReset();
            if (z) {
                j = System.currentTimeMillis();
            }
            this._statsStartedAt = j;
        }
    }

    public void setThreadPool(ThreadPool threadPool) {
        this._threadPool = threadPool;
    }

    public void statsReset() {
        long j = -1;
        if (this._statsStartedAt != -1) {
            j = System.currentTimeMillis();
        }
        this._statsStartedAt = j;
        this._connections = 0;
        this._connectionsOpenMin = this._connectionsOpen;
        this._connectionsOpenMax = this._connectionsOpen;
        this._connectionsOpen = 0;
        this._connectionsDurationMin = 0;
        this._connectionsDurationMax = 0;
        this._connectionsDurationTotal = 0;
        this._requests = 0;
        this._connectionsRequestsMin = 0;
        this._connectionsRequestsMax = 0;
    }

    public void stopAccept(int i) throws Exception {
    }

    public String toString() {
        String name = getClass().getName();
        int lastIndexOf = name.lastIndexOf(46);
        if (lastIndexOf > 0) {
            name = name.substring(lastIndexOf + 1);
        }
        return new StringBuffer().append(name).append("@").append(getHost() == null ? Portable.ALL_INTERFACES : getHost()).append(":").append(getLocalPort() <= 0 ? getPort() : getLocalPort()).toString();
    }
}
