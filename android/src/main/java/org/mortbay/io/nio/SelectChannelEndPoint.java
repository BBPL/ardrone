package org.mortbay.io.nio;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import org.mortbay.io.Buffer;
import org.mortbay.io.Connection;
import org.mortbay.io.nio.SelectorManager.SelectSet;
import org.mortbay.log.Log;
import org.mortbay.thread.Timeout.Task;

public class SelectChannelEndPoint extends ChannelEndPoint implements Runnable {
    protected Connection _connection;
    protected boolean _dispatched = false;
    protected int _interestOps;
    protected SelectionKey _key;
    protected SelectorManager _manager;
    protected boolean _readBlocked;
    protected SelectSet _selectSet;
    private Task _timeoutTask = new IdleTask(this);
    protected boolean _writable = true;
    protected boolean _writeBlocked;

    public class IdleTask extends Task {
        private final SelectChannelEndPoint this$0;

        public IdleTask(SelectChannelEndPoint selectChannelEndPoint) {
            this.this$0 = selectChannelEndPoint;
        }

        public void expired() {
            this.this$0.idleExpired();
        }

        public String toString() {
            return new StringBuffer().append("TimeoutTask:").append(this.this$0.toString()).toString();
        }
    }

    public SelectChannelEndPoint(SocketChannel socketChannel, SelectSet selectSet, SelectionKey selectionKey) {
        super(socketChannel);
        this._manager = selectSet.getManager();
        this._selectSet = selectSet;
        this._connection = this._manager.newConnection(socketChannel, this);
        this._manager.endPointOpened(this);
        this._key = selectionKey;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateKey() {
        /*
        r4 = this;
        r0 = 0;
        r1 = -1;
        monitor-enter(r4);
        r2 = r4.getChannel();	 Catch:{ all -> 0x0051 }
        r2 = r2.isOpen();	 Catch:{ all -> 0x0051 }
        if (r2 == 0) goto L_0x0058;
    L_0x000d:
        r2 = r4._key;	 Catch:{ all -> 0x0051 }
        if (r2 == 0) goto L_0x0054;
    L_0x0011:
        r2 = r4._key;	 Catch:{ all -> 0x0051 }
        r2 = r2.isValid();	 Catch:{ all -> 0x0051 }
        if (r2 == 0) goto L_0x0054;
    L_0x0019:
        r1 = r4._key;	 Catch:{ all -> 0x0051 }
        r2 = r1.interestOps();	 Catch:{ all -> 0x0051 }
    L_0x001f:
        r1 = r4._dispatched;	 Catch:{ all -> 0x0051 }
        if (r1 == 0) goto L_0x0027;
    L_0x0023:
        r1 = r4._readBlocked;	 Catch:{ all -> 0x0051 }
        if (r1 == 0) goto L_0x0056;
    L_0x0027:
        r1 = 1;
    L_0x0028:
        r3 = r4._writable;	 Catch:{ all -> 0x0051 }
        if (r3 == 0) goto L_0x0030;
    L_0x002c:
        r3 = r4._writeBlocked;	 Catch:{ all -> 0x0051 }
        if (r3 == 0) goto L_0x0031;
    L_0x0030:
        r0 = 4;
    L_0x0031:
        r0 = r0 | r1;
        r4._interestOps = r0;	 Catch:{ all -> 0x0051 }
        r0 = r2;
    L_0x0035:
        r1 = r4._interestOps;	 Catch:{ all -> 0x0051 }
        if (r1 != r0) goto L_0x0045;
    L_0x0039:
        r0 = r4.getChannel();	 Catch:{ all -> 0x0051 }
        r0 = r0.isOpen();	 Catch:{ all -> 0x0051 }
        if (r0 == 0) goto L_0x0045;
    L_0x0043:
        monitor-exit(r4);	 Catch:{ all -> 0x0051 }
    L_0x0044:
        return;
    L_0x0045:
        monitor-exit(r4);	 Catch:{ all -> 0x0051 }
        r0 = r4._selectSet;
        r0.addChange(r4);
        r0 = r4._selectSet;
        r0.wakeup();
        goto L_0x0044;
    L_0x0051:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0051 }
        throw r0;
    L_0x0054:
        r2 = r1;
        goto L_0x001f;
    L_0x0056:
        r1 = r0;
        goto L_0x0028;
    L_0x0058:
        r0 = r1;
        goto L_0x0035;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.io.nio.SelectChannelEndPoint.updateKey():void");
    }

    public boolean blockReadable(long j) throws IOException {
        synchronized (this) {
            long now = this._selectSet.getNow();
            try {
                this._readBlocked = true;
                while (isOpen() && this._readBlocked) {
                    updateKey();
                    wait(j);
                    if (this._readBlocked && j < this._selectSet.getNow() - now) {
                        this._readBlocked = false;
                        return false;
                    }
                }
                this._readBlocked = false;
                return true;
            } catch (Throwable e) {
                Log.warn(e);
            } catch (Throwable th) {
                this._readBlocked = false;
            }
        }
    }

    public boolean blockWritable(long j) throws IOException {
        synchronized (this) {
            long now = this._selectSet.getNow();
            try {
                this._writeBlocked = true;
                while (isOpen() && this._writeBlocked) {
                    updateKey();
                    wait(j);
                    if (this._writeBlocked && j < this._selectSet.getNow() - now) {
                        this._writeBlocked = false;
                        scheduleIdle();
                        return false;
                    }
                }
                this._writeBlocked = false;
                scheduleIdle();
                return true;
            } catch (Throwable e) {
                Log.warn(e);
            } catch (Throwable th) {
                this._writeBlocked = false;
                scheduleIdle();
            }
        }
    }

    public void cancelIdle() {
        this._selectSet.cancelIdle(this._timeoutTask);
    }

    public void close() throws IOException {
        try {
            super.close();
        } catch (Throwable e) {
            Log.ignore(e);
        } finally {
            updateKey();
        }
    }

    void dispatch() throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x001e in list [B:7:0x0016]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r2 = this;
        r1 = 1;
        r0 = r2._manager;	 Catch:{ all -> 0x001f }
        r0 = r0.isDelaySelectKeyUpdate();	 Catch:{ all -> 0x001f }
        r0 = r2.dispatch(r0);	 Catch:{ all -> 0x001f }
        if (r0 == 0) goto L_0x0014;	 Catch:{ all -> 0x001f }
    L_0x000d:
        r1 = 0;	 Catch:{ all -> 0x001f }
        r0 = r2._manager;	 Catch:{ all -> 0x001f }
        r1 = r0.dispatch(r2);	 Catch:{ all -> 0x001f }
    L_0x0014:
        if (r1 != 0) goto L_0x001e;
    L_0x0016:
        r0 = "dispatch failed!";
        org.mortbay.log.Log.warn(r0);
        r2.undispatch();
    L_0x001e:
        return;
    L_0x001f:
        r0 = move-exception;
        if (r1 != 0) goto L_0x002a;
    L_0x0022:
        r1 = "dispatch failed!";
        org.mortbay.log.Log.warn(r1);
        r2.undispatch();
    L_0x002a:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.io.nio.SelectChannelEndPoint.dispatch():void");
    }

    public boolean dispatch(boolean z) throws IOException {
        synchronized (this) {
            if (this._key == null || !this._key.isValid()) {
                this._readBlocked = false;
                this._writeBlocked = false;
                notifyAll();
                return false;
            } else if (this._readBlocked || this._writeBlocked) {
                if (this._readBlocked && this._key.isReadable()) {
                    this._readBlocked = false;
                }
                if (this._writeBlocked && this._key.isWritable()) {
                    this._writeBlocked = false;
                }
                notifyAll();
                this._key.interestOps(0);
                return false;
            } else {
                if (!z) {
                    this._key.interestOps(0);
                }
                if (this._dispatched) {
                    this._key.interestOps(0);
                    return false;
                }
                if ((this._key.readyOps() & 4) == 4 && (this._key.interestOps() & 4) == 4) {
                    this._interestOps = this._key.interestOps() & -5;
                    this._key.interestOps(this._interestOps);
                    this._writable = true;
                }
                this._dispatched = true;
                return true;
            }
        }
    }

    void doUpdateKey() {
        synchronized (this) {
            if (!getChannel().isOpen()) {
                if (this._key != null && this._key.isValid()) {
                    this._key.interestOps(0);
                    this._key.cancel();
                }
                cancelIdle();
                this._manager.endPointClosed(this);
                this._key = null;
            } else if (this._interestOps > 0) {
                if (this._key != null && this._key.isValid()) {
                    this._key.interestOps(this._interestOps);
                } else if (((SelectableChannel) getChannel()).isRegistered()) {
                    updateKey();
                } else {
                    try {
                        this._key = ((SelectableChannel) getChannel()).register(this._selectSet.getSelector(), this._interestOps, this);
                    } catch (Throwable e) {
                        Log.ignore(e);
                        if (this._key != null && this._key.isValid()) {
                            this._key.cancel();
                        }
                        cancelIdle();
                        this._manager.endPointClosed(this);
                        this._key = null;
                    }
                }
            } else if (this._key == null || !this._key.isValid()) {
                this._key = null;
            } else {
                this._key.interestOps(0);
            }
        }
    }

    public int flush(Buffer buffer) throws IOException {
        int flush = super.flush(buffer);
        this._writable = flush > 0;
        return flush;
    }

    public int flush(Buffer buffer, Buffer buffer2, Buffer buffer3) throws IOException {
        int flush = super.flush(buffer, buffer2, buffer3);
        this._writable = flush > 0;
        return flush;
    }

    public Connection getConnection() {
        return this._connection;
    }

    public SelectSet getSelectSet() {
        return this._selectSet;
    }

    public Task getTimeoutTask() {
        return this._timeoutTask;
    }

    protected void idleExpired() {
        try {
            close();
        } catch (Throwable e) {
            Log.ignore(e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r2 = this;
        r0 = r2._connection;	 Catch:{ ClosedChannelException -> 0x0009, EofException -> 0x0011, HttpException -> 0x0028, Throwable -> 0x003a }
        r0.handle();	 Catch:{ ClosedChannelException -> 0x0009, EofException -> 0x0011, HttpException -> 0x0028, Throwable -> 0x003a }
        r2.undispatch();
    L_0x0008:
        return;
    L_0x0009:
        r0 = move-exception;
        org.mortbay.log.Log.ignore(r0);	 Catch:{ all -> 0x0023 }
        r2.undispatch();
        goto L_0x0008;
    L_0x0011:
        r0 = move-exception;
        r1 = "EOF";
        org.mortbay.log.Log.debug(r1, r0);	 Catch:{ all -> 0x0023 }
        r2.close();	 Catch:{ IOException -> 0x001e }
    L_0x001a:
        r2.undispatch();
        goto L_0x0008;
    L_0x001e:
        r0 = move-exception;
        org.mortbay.log.Log.ignore(r0);	 Catch:{ all -> 0x0023 }
        goto L_0x001a;
    L_0x0023:
        r0 = move-exception;
        r2.undispatch();
        throw r0;
    L_0x0028:
        r0 = move-exception;
        r1 = "BAD";
        org.mortbay.log.Log.debug(r1, r0);	 Catch:{ all -> 0x0023 }
        r2.close();	 Catch:{ IOException -> 0x0035 }
    L_0x0031:
        r2.undispatch();
        goto L_0x0008;
    L_0x0035:
        r0 = move-exception;
        org.mortbay.log.Log.ignore(r0);	 Catch:{ all -> 0x0023 }
        goto L_0x0031;
    L_0x003a:
        r0 = move-exception;
        r1 = "handle failed";
        org.mortbay.log.Log.warn(r1, r0);	 Catch:{ all -> 0x0023 }
        r2.close();	 Catch:{ IOException -> 0x0047 }
    L_0x0043:
        r2.undispatch();
        goto L_0x0008;
    L_0x0047:
        r0 = move-exception;
        org.mortbay.log.Log.ignore(r0);	 Catch:{ all -> 0x0023 }
        goto L_0x0043;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.io.nio.SelectChannelEndPoint.run():void");
    }

    public void scheduleIdle() {
        this._selectSet.scheduleIdle(this._timeoutTask);
    }

    public void scheduleWrite() {
        this._writable = false;
        updateKey();
    }

    public void setWritable(boolean z) {
        this._writable = z;
    }

    public String toString() {
        return new StringBuffer().append("SCEP@").append(hashCode()).append("[d=").append(this._dispatched).append(",io=").append(this._interestOps).append(",w=").append(this._writable).append(",b=").append(this._readBlocked).append("|").append(this._writeBlocked).append("]").toString();
    }

    public void undispatch() {
        synchronized (this) {
            try {
                this._dispatched = false;
                updateKey();
            } catch (Throwable e) {
                Log.ignore(e);
                this._interestOps = -1;
                this._selectSet.addChange(this);
            }
        }
    }
}
