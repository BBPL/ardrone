package org.mortbay.io.nio;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.io.Connection;
import org.mortbay.io.EndPoint;
import org.mortbay.log.Log;
import org.mortbay.thread.Timeout;
import org.mortbay.thread.Timeout.Task;

public abstract class SelectorManager extends AbstractLifeCycle {
    private static final int __BUSY_KEY = Integer.getInteger("org.mortbay.io.nio.BUSY_KEY", -1).intValue();
    private static final int __BUSY_PAUSE = Integer.getInteger("org.mortbay.io.nio.BUSY_PAUSE", 50).intValue();
    private static final int __JVMBUG_THRESHHOLD = Integer.getInteger("org.mortbay.io.nio.JVMBUG_THRESHHOLD", 512).intValue();
    private static final int __MAX_SELECTS = Integer.getInteger("org.mortbay.io.nio.MAX_SELECTS", 15000).intValue();
    private static final int __MONITOR_PERIOD = Integer.getInteger("org.mortbay.io.nio.MONITOR_PERIOD", 1000).intValue();
    private boolean _delaySelectKeyUpdate = true;
    private long _lowResourcesConnections;
    private long _lowResourcesMaxIdleTime;
    private long _maxIdleTime;
    private transient SelectSet[] _selectSet;
    private int _selectSets = 1;
    private volatile int _set;

    private static class ChangeSelectableChannel {
        final Object _attachment;
        final SelectableChannel _channel;

        public ChangeSelectableChannel(SelectableChannel selectableChannel, Object obj) {
            this._channel = selectableChannel;
            this._attachment = obj;
        }
    }

    private interface ChangeTask {
        void run();
    }

    public class SelectSet {
        private SelectionKey _busyKey;
        private int _busyKeyCount;
        private transient int _change;
        private transient List[] _changes;
        private transient Timeout _idleTimeout = new Timeout(this);
        private transient int _jvmBug;
        private int _jvmFix0;
        private int _jvmFix1;
        private int _jvmFix2;
        private long _log;
        private long _monitorNext;
        private long _monitorStart;
        private transient int _nextSet;
        private int _paused;
        private boolean _pausing;
        private transient Timeout _retryTimeout;
        private volatile boolean _selecting;
        private transient Selector _selector;
        private int _selects;
        private transient int _setID;
        private final SelectorManager this$0;

        class C13211 implements Runnable {
            private final SelectSet this$1;
            private final SelectChannelEndPoint val$endpoint;

            C13211(SelectSet selectSet, SelectChannelEndPoint selectChannelEndPoint) {
                this.this$1 = selectSet;
                this.val$endpoint = selectChannelEndPoint;
            }

            public void run() {
                try {
                    this.val$endpoint.close();
                } catch (Throwable e) {
                    Log.ignore(e);
                }
            }
        }

        SelectSet(SelectorManager selectorManager, int i) throws Exception {
            this.this$0 = selectorManager;
            this._setID = i;
            this._idleTimeout.setDuration(selectorManager.getMaxIdleTime());
            this._retryTimeout = new Timeout(this);
            this._retryTimeout.setDuration(0);
            this._selector = Selector.open();
            this._changes = new ArrayList[]{new ArrayList(), new ArrayList()};
            this._change = 0;
            this._monitorStart = System.currentTimeMillis();
            this._monitorNext = this._monitorStart + ((long) SelectorManager.access$000());
            this._log = this._monitorStart + 60000;
        }

        public void addChange(Object obj) {
            synchronized (this._changes) {
                this._changes[this._change].add(obj);
            }
        }

        public void addChange(SelectableChannel selectableChannel, Object obj) {
            if (obj == null) {
                addChange(selectableChannel);
            } else if (obj instanceof EndPoint) {
                addChange(obj);
            } else {
                addChange(new ChangeSelectableChannel(selectableChannel, obj));
            }
        }

        public void cancelIdle(Task task) {
            synchronized (this) {
                task.cancel();
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void doSelect() throws java.io.IOException {
            /*
            r16 = this;
            r0 = r16;
            r3 = r0._changes;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            monitor-enter(r3);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r0._changes;	 Catch:{ all -> 0x003f }
            r0 = r16;
            r4 = r0._change;	 Catch:{ all -> 0x003f }
            r5 = r2[r4];	 Catch:{ all -> 0x003f }
            r0 = r16;
            r2 = r0._change;	 Catch:{ all -> 0x003f }
            if (r2 != 0) goto L_0x003d;
        L_0x0015:
            r2 = 1;
        L_0x0016:
            r0 = r16;
            r0._change = r2;	 Catch:{ all -> 0x003f }
            r2 = 1;
            r0 = r16;
            r0._selecting = r2;	 Catch:{ all -> 0x003f }
            r0 = r16;
            r10 = r0._selector;	 Catch:{ all -> 0x003f }
            monitor-exit(r3);	 Catch:{ all -> 0x003f }
            r2 = 0;
            r4 = r2;
        L_0x0026:
            r2 = r5.size();	 Catch:{ all -> 0x0069 }
            if (r4 >= r2) goto L_0x0126;
        L_0x002c:
            r2 = r5.get(r4);	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r3 = r2 instanceof org.mortbay.io.EndPoint;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            if (r3 == 0) goto L_0x004c;
        L_0x0034:
            r2 = (org.mortbay.io.nio.SelectChannelEndPoint) r2;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r2.doUpdateKey();	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
        L_0x0039:
            r2 = r4 + 1;
            r4 = r2;
            goto L_0x0026;
        L_0x003d:
            r2 = 0;
            goto L_0x0016;
        L_0x003f:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x003f }
            throw r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x0042:
            r2 = move-exception;
            org.mortbay.log.Log.warn(r2);	 Catch:{ all -> 0x0348 }
            r2 = 0;
            r0 = r16;
            r0._selecting = r2;
        L_0x004b:
            return;
        L_0x004c:
            r3 = r2 instanceof java.lang.Runnable;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            if (r3 == 0) goto L_0x0078;
        L_0x0050:
            r0 = r16;
            r3 = r0.this$0;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r2 = (java.lang.Runnable) r2;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r3.dispatch(r2);	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            goto L_0x0039;
        L_0x005a:
            r2 = move-exception;
            r0 = r16;
            r3 = r0.this$0;	 Catch:{ all -> 0x0069 }
            r3 = r3.isRunning();	 Catch:{ all -> 0x0069 }
            if (r3 == 0) goto L_0x011c;
        L_0x0065:
            org.mortbay.log.Log.warn(r2);	 Catch:{ all -> 0x0069 }
            goto L_0x0039;
        L_0x0069:
            r2 = move-exception;
            r5.clear();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            throw r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x006e:
            r2 = move-exception;
            org.mortbay.log.Log.ignore(r2);	 Catch:{ all -> 0x0348 }
            r2 = 0;
            r0 = r16;
            r0._selecting = r2;
            goto L_0x004b;
        L_0x0078:
            r3 = r2 instanceof org.mortbay.io.nio.SelectorManager.ChangeSelectableChannel;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            if (r3 == 0) goto L_0x00c4;
        L_0x007c:
            r2 = (org.mortbay.io.nio.SelectorManager.ChangeSelectableChannel) r2;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r3 = r2._channel;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r6 = r2._attachment;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r2 = r3 instanceof java.nio.channels.SocketChannel;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            if (r2 == 0) goto L_0x00b7;
        L_0x0086:
            r0 = r3;
            r0 = (java.nio.channels.SocketChannel) r0;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r2 = r0;
            r2 = r2.isConnected();	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            if (r2 == 0) goto L_0x00b7;
        L_0x0090:
            r2 = 1;
            r2 = r3.register(r10, r2, r6);	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r0 = r16;
            r6 = r0.this$0;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r3 = (java.nio.channels.SocketChannel) r3;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r0 = r16;
            r3 = r6.newEndPoint(r3, r0, r2);	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r2.attach(r3);	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r3.dispatch();	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            goto L_0x0039;
        L_0x00a8:
            r2 = move-exception;
            r0 = r16;
            r3 = r0.this$0;	 Catch:{ all -> 0x0069 }
            r3 = r3.isRunning();	 Catch:{ all -> 0x0069 }
            if (r3 == 0) goto L_0x0121;
        L_0x00b3:
            org.mortbay.log.Log.warn(r2);	 Catch:{ all -> 0x0069 }
            goto L_0x0039;
        L_0x00b7:
            r2 = r3.isOpen();	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            if (r2 == 0) goto L_0x0039;
        L_0x00bd:
            r2 = 8;
            r3.register(r10, r2, r6);	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            goto L_0x0039;
        L_0x00c4:
            r3 = r2 instanceof java.nio.channels.SocketChannel;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            if (r3 == 0) goto L_0x00f6;
        L_0x00c8:
            r2 = (java.nio.channels.SocketChannel) r2;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r3 = r2.isConnected();	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            if (r3 == 0) goto L_0x00e8;
        L_0x00d0:
            r3 = 1;
            r6 = 0;
            r3 = r2.register(r10, r3, r6);	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r0 = r16;
            r6 = r0.this$0;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r0 = r16;
            r2 = r6.newEndPoint(r2, r0, r3);	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r3.attach(r2);	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r2.dispatch();	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            goto L_0x0039;
        L_0x00e8:
            r3 = r2.isOpen();	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            if (r3 == 0) goto L_0x0039;
        L_0x00ee:
            r3 = 8;
            r6 = 0;
            r2.register(r10, r3, r6);	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            goto L_0x0039;
        L_0x00f6:
            r3 = r2 instanceof java.nio.channels.ServerSocketChannel;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            if (r3 == 0) goto L_0x0107;
        L_0x00fa:
            r2 = (java.nio.channels.ServerSocketChannel) r2;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r3 = r16.getSelector();	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r6 = 16;
            r2.register(r3, r6);	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            goto L_0x0039;
        L_0x0107:
            r3 = r2 instanceof org.mortbay.io.nio.SelectorManager.ChangeTask;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            if (r3 == 0) goto L_0x0112;
        L_0x010b:
            r2 = (org.mortbay.io.nio.SelectorManager.ChangeTask) r2;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r2.run();	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            goto L_0x0039;
        L_0x0112:
            r3 = new java.lang.IllegalArgumentException;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r2 = r2.toString();	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            r3.<init>(r2);	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
            throw r3;	 Catch:{ Exception -> 0x005a, Error -> 0x00a8 }
        L_0x011c:
            org.mortbay.log.Log.debug(r2);	 Catch:{ all -> 0x0069 }
            goto L_0x0039;
        L_0x0121:
            org.mortbay.log.Log.debug(r2);	 Catch:{ all -> 0x0069 }
            goto L_0x0039;
        L_0x0126:
            r5.clear();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r6 = java.lang.System.currentTimeMillis();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            monitor-enter(r16);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r0._idleTimeout;	 Catch:{ all -> 0x0345 }
            r2.setNow(r6);	 Catch:{ all -> 0x0345 }
            r0 = r16;
            r2 = r0._retryTimeout;	 Catch:{ all -> 0x0345 }
            r2.setNow(r6);	 Catch:{ all -> 0x0345 }
            r0 = r16;
            r2 = r0.this$0;	 Catch:{ all -> 0x0345 }
            r2 = org.mortbay.io.nio.SelectorManager.access$100(r2);	 Catch:{ all -> 0x0345 }
            r4 = 0;
            r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r2 <= 0) goto L_0x0334;
        L_0x014a:
            r2 = r10.keys();	 Catch:{ all -> 0x0345 }
            r2 = r2.size();	 Catch:{ all -> 0x0345 }
            r2 = (long) r2;	 Catch:{ all -> 0x0345 }
            r0 = r16;
            r4 = r0.this$0;	 Catch:{ all -> 0x0345 }
            r4 = org.mortbay.io.nio.SelectorManager.access$100(r4);	 Catch:{ all -> 0x0345 }
            r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r2 <= 0) goto L_0x0334;
        L_0x015f:
            r0 = r16;
            r2 = r0._idleTimeout;	 Catch:{ all -> 0x0345 }
            r0 = r16;
            r3 = r0.this$0;	 Catch:{ all -> 0x0345 }
            r4 = org.mortbay.io.nio.SelectorManager.access$200(r3);	 Catch:{ all -> 0x0345 }
            r2.setDuration(r4);	 Catch:{ all -> 0x0345 }
        L_0x016e:
            r0 = r16;
            r2 = r0._idleTimeout;	 Catch:{ all -> 0x0345 }
            r4 = r2.getTimeToNext();	 Catch:{ all -> 0x0345 }
            r0 = r16;
            r2 = r0._retryTimeout;	 Catch:{ all -> 0x0345 }
            r2 = r2.getTimeToNext();	 Catch:{ all -> 0x0345 }
            monitor-exit(r16);	 Catch:{ all -> 0x0345 }
            r8 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r12 = 0;
            r11 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1));
            if (r11 < 0) goto L_0x0655;
        L_0x0187:
            r12 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r11 = (r12 > r4 ? 1 : (r12 == r4 ? 0 : -1));
            if (r11 <= 0) goto L_0x0655;
        L_0x018d:
            r8 = 0;
            r8 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
            if (r8 <= 0) goto L_0x0652;
        L_0x0193:
            r8 = 0;
            r8 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1));
            if (r8 < 0) goto L_0x0652;
        L_0x0199:
            r8 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));
            if (r8 <= 0) goto L_0x0652;
        L_0x019d:
            r8 = r2;
        L_0x019e:
            r2 = 2;
            r2 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1));
            if (r2 <= 0) goto L_0x04cc;
        L_0x01a4:
            r0 = r16;
            r2 = r0._pausing;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 == 0) goto L_0x01b2;
        L_0x01aa:
            r2 = org.mortbay.io.nio.SelectorManager.access$400();	 Catch:{ InterruptedException -> 0x034f }
            r2 = (long) r2;	 Catch:{ InterruptedException -> 0x034f }
            java.lang.Thread.sleep(r2);	 Catch:{ InterruptedException -> 0x034f }
        L_0x01b2:
            r3 = r10.select(r8);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r4 = java.lang.System.currentTimeMillis();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r0._idleTimeout;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2.setNow(r4);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r0._retryTimeout;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2.setNow(r4);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r0._selects;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2 + 1;
            r0 = r16;
            r0._selects = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r12 = r0._monitorNext;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1));
            if (r2 <= 0) goto L_0x0227;
        L_0x01da:
            r0 = r16;
            r2 = r0._selects;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r11 = org.mortbay.io.nio.SelectorManager.access$000();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2 * r11;
            r12 = (long) r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r14 = r0._monitorStart;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r14 = r4 - r14;
            r12 = r12 / r14;
            r2 = (int) r12;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r0._selects = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r0._selects;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r11 = org.mortbay.io.nio.SelectorManager.access$500();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 <= r11) goto L_0x064c;
        L_0x01fa:
            r2 = 1;
        L_0x01fb:
            r0 = r16;
            r0._pausing = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r0._pausing;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 == 0) goto L_0x020f;
        L_0x0205:
            r0 = r16;
            r2 = r0._paused;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2 + 1;
            r0 = r16;
            r0._paused = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x020f:
            r2 = 0;
            r0 = r16;
            r0._selects = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = 0;
            r0 = r16;
            r0._jvmBug = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r0._monitorStart = r4;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = org.mortbay.io.nio.SelectorManager.access$000();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r12 = (long) r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r12 = r12 + r4;
            r0 = r16;
            r0._monitorNext = r12;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x0227:
            r0 = r16;
            r12 = r0._log;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1));
            if (r2 <= 0) goto L_0x02dd;
        L_0x022f:
            r0 = r16;
            r2 = r0._paused;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 <= 0) goto L_0x025b;
        L_0x0235:
            r2 = new java.lang.StringBuffer;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2.<init>();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r2.append(r0);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r11 = " Busy selector - injecting delay ";
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r11 = r0._paused;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r11 = " times";
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2.toString();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            org.mortbay.log.Log.info(r2);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x025b:
            r0 = r16;
            r2 = r0._jvmFix2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 <= 0) goto L_0x0287;
        L_0x0261:
            r2 = new java.lang.StringBuffer;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2.<init>();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r2.append(r0);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r11 = " JVM BUG(s) - injecting delay";
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r11 = r0._jvmFix2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r11 = " times";
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2.toString();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            org.mortbay.log.Log.info(r2);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x0287:
            r0 = r16;
            r2 = r0._jvmFix1;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 <= 0) goto L_0x0355;
        L_0x028d:
            r2 = new java.lang.StringBuffer;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2.<init>();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r2.append(r0);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r11 = " JVM BUG(s) - recreating selector ";
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r11 = r0._jvmFix1;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r11 = " times, canceled keys ";
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r11 = r0._jvmFix0;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r11 = " times";
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2.toString();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            org.mortbay.log.Log.info(r2);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x02c1:
            r2 = 0;
            r0 = r16;
            r0._paused = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = 0;
            r0 = r16;
            r0._jvmFix2 = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = 0;
            r0 = r16;
            r0._jvmFix1 = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = 0;
            r0 = r16;
            r0._jvmFix0 = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r12 = 60000; // 0xea60 float:8.4078E-41 double:2.9644E-319;
            r12 = r12 + r4;
            r0 = r16;
            r0._log = r12;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x02dd:
            if (r3 != 0) goto L_0x0443;
        L_0x02df:
            r12 = 10;
            r2 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1));
            if (r2 <= 0) goto L_0x0443;
        L_0x02e5:
            r6 = r4 - r6;
            r12 = 2;
            r8 = r8 / r12;
            r2 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
            if (r2 >= 0) goto L_0x0443;
        L_0x02ee:
            r0 = r16;
            r2 = r0._jvmBug;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2 + 1;
            r0 = r16;
            r0._jvmBug = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r0._jvmBug;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r3 = org.mortbay.io.nio.SelectorManager.access$600();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 <= r3) goto L_0x038f;
        L_0x0302:
            r0 = r16;
            r2 = r0._jvmBug;	 Catch:{ InterruptedException -> 0x0389 }
            r3 = org.mortbay.io.nio.SelectorManager.access$600();	 Catch:{ InterruptedException -> 0x0389 }
            r3 = r3 + 1;
            if (r2 != r3) goto L_0x0318;
        L_0x030e:
            r0 = r16;
            r2 = r0._jvmFix2;	 Catch:{ InterruptedException -> 0x0389 }
            r2 = r2 + 1;
            r0 = r16;
            r0._jvmFix2 = r2;	 Catch:{ InterruptedException -> 0x0389 }
        L_0x0318:
            r2 = org.mortbay.io.nio.SelectorManager.access$400();	 Catch:{ InterruptedException -> 0x0389 }
            r2 = (long) r2;	 Catch:{ InterruptedException -> 0x0389 }
            java.lang.Thread.sleep(r2);	 Catch:{ InterruptedException -> 0x0389 }
            r6 = r4;
        L_0x0321:
            r0 = r16;
            r2 = r0._selector;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 == 0) goto L_0x032d;
        L_0x0327:
            r2 = r10.isOpen();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 != 0) goto L_0x04db;
        L_0x032d:
            r2 = 0;
            r0 = r16;
            r0._selecting = r2;
            goto L_0x004b;
        L_0x0334:
            r0 = r16;
            r2 = r0._idleTimeout;	 Catch:{ all -> 0x0345 }
            r0 = r16;
            r3 = r0.this$0;	 Catch:{ all -> 0x0345 }
            r4 = org.mortbay.io.nio.SelectorManager.access$300(r3);	 Catch:{ all -> 0x0345 }
            r2.setDuration(r4);	 Catch:{ all -> 0x0345 }
            goto L_0x016e;
        L_0x0345:
            r2 = move-exception;
            monitor-exit(r16);	 Catch:{ all -> 0x0345 }
            throw r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x0348:
            r2 = move-exception;
            r3 = 0;
            r0 = r16;
            r0._selecting = r3;
            throw r2;
        L_0x034f:
            r2 = move-exception;
            org.mortbay.log.Log.ignore(r2);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            goto L_0x01b2;
        L_0x0355:
            r2 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 == 0) goto L_0x02c1;
        L_0x035b:
            r0 = r16;
            r2 = r0._jvmFix0;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 <= 0) goto L_0x02c1;
        L_0x0361:
            r2 = new java.lang.StringBuffer;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2.<init>();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r2.append(r0);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r11 = " JVM BUG(s) - canceled keys ";
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r11 = r0._jvmFix0;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r11 = " times";
            r2 = r2.append(r11);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2.toString();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            org.mortbay.log.Log.info(r2);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            goto L_0x02c1;
        L_0x0389:
            r2 = move-exception;
            org.mortbay.log.Log.ignore(r2);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r6 = r4;
            goto L_0x0321;
        L_0x038f:
            r0 = r16;
            r2 = r0._jvmBug;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r3 = org.mortbay.io.nio.SelectorManager.access$600();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 != r3) goto L_0x03fd;
        L_0x0399:
            monitor-enter(r16);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r0._jvmFix1;	 Catch:{ all -> 0x03dc }
            r2 = r2 + 1;
            r0 = r16;
            r0._jvmFix1 = r2;	 Catch:{ all -> 0x03dc }
            r3 = java.nio.channels.Selector.open();	 Catch:{ all -> 0x03dc }
            r0 = r16;
            r2 = r0._selector;	 Catch:{ all -> 0x03dc }
            r2 = r2.keys();	 Catch:{ all -> 0x03dc }
            r4 = r2.iterator();	 Catch:{ all -> 0x03dc }
        L_0x03b4:
            r2 = r4.hasNext();	 Catch:{ all -> 0x03dc }
            if (r2 == 0) goto L_0x03e5;
        L_0x03ba:
            r2 = r4.next();	 Catch:{ all -> 0x03dc }
            r2 = (java.nio.channels.SelectionKey) r2;	 Catch:{ all -> 0x03dc }
            r5 = r2.isValid();	 Catch:{ all -> 0x03dc }
            if (r5 == 0) goto L_0x03b4;
        L_0x03c6:
            r5 = r2.interestOps();	 Catch:{ all -> 0x03dc }
            if (r5 == 0) goto L_0x03b4;
        L_0x03cc:
            r5 = r2.channel();	 Catch:{ all -> 0x03dc }
            r2 = r2.attachment();	 Catch:{ all -> 0x03dc }
            if (r2 != 0) goto L_0x03df;
        L_0x03d6:
            r0 = r16;
            r0.addChange(r5);	 Catch:{ all -> 0x03dc }
            goto L_0x03b4;
        L_0x03dc:
            r2 = move-exception;
            monitor-exit(r16);	 Catch:{ all -> 0x03dc }
            throw r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x03df:
            r0 = r16;
            r0.addChange(r5, r2);	 Catch:{ all -> 0x03dc }
            goto L_0x03b4;
        L_0x03e5:
            r0 = r16;
            r2 = r0._selector;	 Catch:{ all -> 0x03dc }
            r0 = r16;
            r0._selector = r3;	 Catch:{ all -> 0x03dc }
            r2.close();	 Catch:{ Exception -> 0x03f8 }
        L_0x03f0:
            monitor-exit(r16);	 Catch:{ all -> 0x03dc }
            r2 = 0;
            r0 = r16;
            r0._selecting = r2;
            goto L_0x004b;
        L_0x03f8:
            r2 = move-exception;
            org.mortbay.log.Log.warn(r2);	 Catch:{ all -> 0x03dc }
            goto L_0x03f0;
        L_0x03fd:
            r0 = r16;
            r2 = r0._jvmBug;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2 % 32;
            r3 = 31;
            if (r2 != r3) goto L_0x064f;
        L_0x0407:
            r2 = 0;
            r3 = r10.keys();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r4 = r3.iterator();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r3 = r2;
        L_0x0411:
            r2 = r4.hasNext();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 == 0) goto L_0x0430;
        L_0x0417:
            r2 = r4.next();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = (java.nio.channels.SelectionKey) r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r5 = r2.isValid();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r5 == 0) goto L_0x0411;
        L_0x0423:
            r5 = r2.interestOps();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r5 != 0) goto L_0x0411;
        L_0x0429:
            r2.cancel();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r3 + 1;
            r3 = r2;
            goto L_0x0411;
        L_0x0430:
            if (r3 <= 0) goto L_0x043c;
        L_0x0432:
            r0 = r16;
            r2 = r0._jvmFix0;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2 + 1;
            r0 = r16;
            r0._jvmFix0 = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x043c:
            r2 = 0;
            r0 = r16;
            r0._selecting = r2;
            goto L_0x004b;
        L_0x0443:
            r2 = org.mortbay.io.nio.SelectorManager.access$700();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 <= 0) goto L_0x064f;
        L_0x0449:
            r2 = 1;
            if (r3 != r2) goto L_0x064f;
        L_0x044c:
            r0 = r16;
            r2 = r0._selects;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r3 = org.mortbay.io.nio.SelectorManager.access$500();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 <= r3) goto L_0x064f;
        L_0x0456:
            r2 = r10.selectedKeys();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2.iterator();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2.next();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = (java.nio.channels.SelectionKey) r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r3 = r0._busyKey;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 != r3) goto L_0x04c6;
        L_0x046a:
            r0 = r16;
            r3 = r0._busyKeyCount;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r3 = r3 + 1;
            r0 = r16;
            r0._busyKeyCount = r3;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r6 = org.mortbay.io.nio.SelectorManager.access$700();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r3 <= r6) goto L_0x04bf;
        L_0x047a:
            r3 = r2.channel();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r3 = r3 instanceof java.nio.channels.ServerSocketChannel;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r3 != 0) goto L_0x04bf;
        L_0x0482:
            r3 = r2.attachment();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r3 = (org.mortbay.io.nio.SelectChannelEndPoint) r3;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r6 = new java.lang.StringBuffer;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r6.<init>();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r7 = "Busy Key ";
            r6 = r6.append(r7);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r7 = r2.channel();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r6 = r6.append(r7);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r7 = " ";
            r6 = r6.append(r7);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r6 = r6.append(r3);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r6 = r6.toString();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            org.mortbay.log.Log.warn(r6);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2.cancel();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r3 == 0) goto L_0x04bf;
        L_0x04b1:
            r0 = r16;
            r6 = r0.this$0;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r7 = new org.mortbay.io.nio.SelectorManager$SelectSet$1;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r7.<init>(r0, r3);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r6.dispatch(r7);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x04bf:
            r0 = r16;
            r0._busyKey = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r6 = r4;
            goto L_0x0321;
        L_0x04c6:
            r3 = 0;
            r0 = r16;
            r0._busyKeyCount = r3;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            goto L_0x04bf;
        L_0x04cc:
            r10.selectNow();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r0._selects;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = r2 + 1;
            r0 = r16;
            r0._selects = r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            goto L_0x0321;
        L_0x04db:
            r2 = r10.selectedKeys();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r5 = r2.iterator();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x04e3:
            r2 = r5.hasNext();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r2 == 0) goto L_0x0630;
        L_0x04e9:
            r2 = r5.next();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = (java.nio.channels.SelectionKey) r2;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r3 = r2.isValid();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            if (r3 != 0) goto L_0x0509;
        L_0x04f5:
            r2.cancel();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r3 = r2.attachment();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r3 = (org.mortbay.io.nio.SelectChannelEndPoint) r3;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            if (r3 == 0) goto L_0x04e3;
        L_0x0500:
            r3.doUpdateKey();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            goto L_0x04e3;
        L_0x0504:
            r2 = move-exception;
            org.mortbay.log.Log.ignore(r2);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            goto L_0x04e3;
        L_0x0509:
            r3 = r2.attachment();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r4 = r3 instanceof org.mortbay.io.nio.SelectChannelEndPoint;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            if (r4 == 0) goto L_0x053d;
        L_0x0511:
            r3 = (org.mortbay.io.nio.SelectChannelEndPoint) r3;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r3.dispatch();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            goto L_0x04e3;
        L_0x0517:
            r3 = move-exception;
            r0 = r16;
            r4 = r0.this$0;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r4 = r4.isRunning();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r4 == 0) goto L_0x062b;
        L_0x0522:
            org.mortbay.log.Log.warn(r3);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
        L_0x0525:
            if (r2 == 0) goto L_0x04e3;
        L_0x0527:
            r3 = r2.channel();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r3 = r3 instanceof java.nio.channels.ServerSocketChannel;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r3 != 0) goto L_0x04e3;
        L_0x052f:
            r3 = r2.isValid();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            if (r3 == 0) goto L_0x04e3;
        L_0x0535:
            r3 = 0;
            r2.interestOps(r3);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2.cancel();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            goto L_0x04e3;
        L_0x053d:
            r4 = r2.isAcceptable();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            if (r4 == 0) goto L_0x05ce;
        L_0x0543:
            r0 = r16;
            r3 = r0.this$0;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r3 = r3.acceptChannel(r2);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            if (r3 == 0) goto L_0x04e3;
        L_0x054d:
            r4 = 0;
            r3.configureBlocking(r4);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r4 = r0._nextSet;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r4 = r4 + 1;
            r0 = r16;
            r0._nextSet = r4;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r8 = r0.this$0;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r8 = org.mortbay.io.nio.SelectorManager.access$800(r8);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r8 = r8.length;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r4 = r4 % r8;
            r0 = r16;
            r0._nextSet = r4;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r4 = r0._nextSet;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r8 = r0._setID;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            if (r4 != r8) goto L_0x05aa;
        L_0x0573:
            r0 = r16;
            r4 = r0.this$0;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r4 = org.mortbay.io.nio.SelectorManager.access$800(r4);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r8 = r0._nextSet;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r4 = r4[r8];	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r4 = r4.getSelector();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r8 = 1;
            r4 = r3.register(r4, r8);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r8 = r0.this$0;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r9 = r0.this$0;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r9 = org.mortbay.io.nio.SelectorManager.access$800(r9);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r11 = r0._nextSet;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r9 = r9[r11];	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r3 = r8.newEndPoint(r3, r9, r4);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r4.attach(r3);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            if (r3 == 0) goto L_0x04e3;
        L_0x05a5:
            r3.dispatch();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            goto L_0x04e3;
        L_0x05aa:
            r0 = r16;
            r4 = r0.this$0;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r4 = org.mortbay.io.nio.SelectorManager.access$800(r4);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r8 = r0._nextSet;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r4 = r4[r8];	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r4.addChange(r3);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r3 = r0.this$0;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r3 = org.mortbay.io.nio.SelectorManager.access$800(r3);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r4 = r0._nextSet;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r3 = r3[r4];	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r3.wakeup();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            goto L_0x04e3;
        L_0x05ce:
            r4 = r2.isConnectable();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            if (r4 == 0) goto L_0x060d;
        L_0x05d4:
            r4 = r2.channel();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r4 = (java.nio.channels.SocketChannel) r4;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r3 = r4.finishConnect();	 Catch:{ Exception -> 0x05fb }
            if (r3 == 0) goto L_0x05f6;
        L_0x05e0:
            r3 = 1;
            r2.interestOps(r3);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r3 = r0.this$0;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r3 = r3.newEndPoint(r4, r0, r2);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r2.attach(r3);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r3.dispatch();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            goto L_0x04e3;
        L_0x05f6:
            r2.cancel();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            goto L_0x04e3;
        L_0x05fb:
            r8 = move-exception;
            r0 = r16;
            r9 = r0.this$0;	 Catch:{ all -> 0x0608 }
            r9.connectionFailed(r4, r8, r3);	 Catch:{ all -> 0x0608 }
            r2.cancel();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            goto L_0x04e3;
        L_0x0608:
            r3 = move-exception;
            r2.cancel();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            throw r3;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
        L_0x060d:
            r3 = r2.channel();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r3 = (java.nio.channels.SocketChannel) r3;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r4 = r0.this$0;	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r0 = r16;
            r3 = r4.newEndPoint(r3, r0, r2);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r2.attach(r3);	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            r4 = r2.isReadable();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            if (r4 == 0) goto L_0x04e3;
        L_0x0626:
            r3.dispatch();	 Catch:{ CancelledKeyException -> 0x0504, Exception -> 0x0517, ClosedSelectorException -> 0x0042 }
            goto L_0x04e3;
        L_0x062b:
            org.mortbay.log.Log.ignore(r3);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            goto L_0x0525;
        L_0x0630:
            r2 = r10.selectedKeys();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2.clear();	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r0._idleTimeout;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2.tick(r6);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r0 = r16;
            r2 = r0._retryTimeout;	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2.tick(r6);	 Catch:{ ClosedSelectorException -> 0x0042, CancelledKeyException -> 0x006e }
            r2 = 0;
            r0 = r16;
            r0._selecting = r2;
            goto L_0x004b;
        L_0x064c:
            r2 = 0;
            goto L_0x01fb;
        L_0x064f:
            r6 = r4;
            goto L_0x0321;
        L_0x0652:
            r8 = r4;
            goto L_0x019e;
        L_0x0655:
            r4 = r8;
            goto L_0x018d;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mortbay.io.nio.SelectorManager.SelectSet.doSelect():void");
        }

        public SelectorManager getManager() {
            return this.this$0;
        }

        public long getNow() {
            return this._idleTimeout.getNow();
        }

        Selector getSelector() {
            return this._selector;
        }

        public void scheduleIdle(Task task) {
            synchronized (this) {
                if (this._idleTimeout.getDuration() <= 0) {
                    return;
                }
                task.schedule(this._idleTimeout);
            }
        }

        public void scheduleTimeout(Task task, long j) {
            synchronized (this) {
                this._retryTimeout.schedule(task, j);
            }
        }

        void stop() throws Exception {
            boolean z = true;
            while (z) {
                wakeup();
                z = this._selecting;
            }
            Iterator it = new ArrayList(this._selector.keys()).iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = (SelectionKey) it.next();
                if (selectionKey != null) {
                    Object attachment = selectionKey.attachment();
                    if (attachment instanceof EndPoint) {
                        try {
                            ((EndPoint) attachment).close();
                        } catch (Throwable e) {
                            Log.ignore(e);
                        }
                    }
                }
            }
            synchronized (this) {
                z = this._selecting;
                while (z) {
                    wakeup();
                    z = this._selecting;
                }
                this._idleTimeout.cancelAll();
                this._retryTimeout.cancelAll();
                try {
                    if (this._selector != null) {
                        this._selector.close();
                    }
                } catch (Throwable e2) {
                    Log.ignore(e2);
                }
                this._selector = null;
            }
        }

        public void wakeup() {
            Selector selector = this._selector;
            if (selector != null) {
                selector.wakeup();
            }
        }
    }

    static int access$000() {
        return __MONITOR_PERIOD;
    }

    static long access$100(SelectorManager selectorManager) {
        return selectorManager._lowResourcesConnections;
    }

    static long access$200(SelectorManager selectorManager) {
        return selectorManager._lowResourcesMaxIdleTime;
    }

    static long access$300(SelectorManager selectorManager) {
        return selectorManager._maxIdleTime;
    }

    static int access$400() {
        return __BUSY_PAUSE;
    }

    static int access$500() {
        return __MAX_SELECTS;
    }

    static int access$600() {
        return __JVMBUG_THRESHHOLD;
    }

    static int access$700() {
        return __BUSY_KEY;
    }

    static SelectSet[] access$800(SelectorManager selectorManager) {
        return selectorManager._selectSet;
    }

    protected abstract SocketChannel acceptChannel(SelectionKey selectionKey) throws IOException;

    protected void connectionFailed(SocketChannel socketChannel, Throwable th, Object obj) {
        Log.warn(th);
    }

    public abstract boolean dispatch(Runnable runnable) throws IOException;

    public void doSelect(int i) throws IOException {
        SelectSet[] selectSetArr = this._selectSet;
        if (selectSetArr != null && selectSetArr.length > i && selectSetArr[i] != null) {
            selectSetArr[i].doSelect();
        }
    }

    protected void doStart() throws Exception {
        this._selectSet = new SelectSet[this._selectSets];
        for (int i = 0; i < this._selectSet.length; i++) {
            this._selectSet[i] = new SelectSet(this, i);
        }
        super.doStart();
    }

    protected void doStop() throws Exception {
        SelectSet[] selectSetArr = this._selectSet;
        this._selectSet = null;
        if (selectSetArr != null) {
            for (SelectSet selectSet : selectSetArr) {
                if (selectSet != null) {
                    selectSet.stop();
                }
            }
        }
        super.doStop();
    }

    protected abstract void endPointClosed(SelectChannelEndPoint selectChannelEndPoint);

    protected abstract void endPointOpened(SelectChannelEndPoint selectChannelEndPoint);

    public long getLowResourcesConnections() {
        return this._lowResourcesConnections * ((long) this._selectSets);
    }

    public long getLowResourcesMaxIdleTime() {
        return this._lowResourcesMaxIdleTime;
    }

    public long getMaxIdleTime() {
        return this._maxIdleTime;
    }

    public int getSelectSets() {
        return this._selectSets;
    }

    public boolean isDelaySelectKeyUpdate() {
        return this._delaySelectKeyUpdate;
    }

    protected abstract Connection newConnection(SocketChannel socketChannel, SelectChannelEndPoint selectChannelEndPoint);

    protected abstract SelectChannelEndPoint newEndPoint(SocketChannel socketChannel, SelectSet selectSet, SelectionKey selectionKey) throws IOException;

    public void register(ServerSocketChannel serverSocketChannel) throws IOException {
        int i = this._set;
        this._set = i + 1;
        SelectSet selectSet = this._selectSet[i % this._selectSets];
        selectSet.addChange(serverSocketChannel);
        selectSet.wakeup();
    }

    public void register(SocketChannel socketChannel, Object obj) throws IOException {
        int i = this._set;
        this._set = i + 1;
        int i2 = this._selectSets;
        SelectSet[] selectSetArr = this._selectSet;
        if (selectSetArr != null) {
            SelectSet selectSet = selectSetArr[i % i2];
            selectSet.addChange(socketChannel, obj);
            selectSet.wakeup();
        }
    }

    public void setDelaySelectKeyUpdate(boolean z) {
        this._delaySelectKeyUpdate = z;
    }

    public void setLowResourcesConnections(long j) {
        this._lowResourcesConnections = ((((long) this._selectSets) + j) - 1) / ((long) this._selectSets);
    }

    public void setLowResourcesMaxIdleTime(long j) {
        this._lowResourcesMaxIdleTime = j;
    }

    public void setMaxIdleTime(long j) {
        this._maxIdleTime = j;
    }

    public void setSelectSets(int i) {
        long j = this._lowResourcesConnections;
        long j2 = (long) this._selectSets;
        this._selectSets = i;
        this._lowResourcesConnections = (j * j2) / ((long) this._selectSets);
    }
}
