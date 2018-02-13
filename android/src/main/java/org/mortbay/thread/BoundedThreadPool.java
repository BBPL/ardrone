package org.mortbay.thread;

import com.google.api.client.http.ExponentialBackOffPolicy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.log.Log;

public class BoundedThreadPool extends AbstractLifeCycle implements Serializable, ThreadPool {
    private static int __id;
    private boolean _daemon;
    private int _id;
    private List _idle;
    private final Object _joinLock = new Object();
    private long _lastShrink;
    private final Object _lock = new Object();
    int _lowThreads = 0;
    private int _maxIdleTimeMs = ExponentialBackOffPolicy.DEFAULT_MAX_INTERVAL_MILLIS;
    private int _maxThreads = 255;
    private int _minThreads = 1;
    private String _name;
    int _priority = 5;
    private List _queue;
    private Set _threads;
    private boolean _warned = false;

    public class PoolThread extends Thread {
        Runnable _job = null;
        private final BoundedThreadPool this$0;

        PoolThread(BoundedThreadPool boundedThreadPool) {
            this.this$0 = boundedThreadPool;
            setDaemon(BoundedThreadPool.access$000(boundedThreadPool));
            setPriority(boundedThreadPool._priority);
        }

        PoolThread(BoundedThreadPool boundedThreadPool, Runnable runnable) {
            this.this$0 = boundedThreadPool;
            setDaemon(BoundedThreadPool.access$000(boundedThreadPool));
            setPriority(boundedThreadPool._priority);
            this._job = runnable;
        }

        void dispatch(Runnable runnable) {
            synchronized (this) {
                if (this._job != null || runnable == null) {
                    throw new IllegalStateException();
                }
                this._job = runnable;
                notify();
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r10 = this;
            r2 = 0;
            monitor-enter(r10);	 Catch:{ all -> 0x0017 }
            r1 = r10._job;	 Catch:{ all -> 0x003d }
            r0 = 0;
            r10._job = r0;	 Catch:{ all -> 0x003d }
            monitor-exit(r10);	 Catch:{ all -> 0x003d }
        L_0x0008:
            r0 = r10.this$0;	 Catch:{ all -> 0x0017 }
            r0 = r0.isRunning();	 Catch:{ all -> 0x0017 }
            if (r0 == 0) goto L_0x0152;
        L_0x0010:
            if (r1 == 0) goto L_0x0040;
        L_0x0012:
            r1.run();	 Catch:{ all -> 0x0017 }
            r1 = r2;
            goto L_0x0008;
        L_0x0017:
            r0 = move-exception;
            r1 = r10.this$0;
            r1 = org.mortbay.thread.BoundedThreadPool.access$100(r1);
            monitor-enter(r1);
            r2 = r10.this$0;	 Catch:{ all -> 0x0143 }
            r2 = org.mortbay.thread.BoundedThreadPool.access$400(r2);	 Catch:{ all -> 0x0143 }
            r2.remove(r10);	 Catch:{ all -> 0x0143 }
            monitor-exit(r1);	 Catch:{ all -> 0x0143 }
            monitor-enter(r10);
            r1 = r10._job;	 Catch:{ all -> 0x0146 }
            monitor-exit(r10);	 Catch:{ all -> 0x0146 }
            if (r1 == 0) goto L_0x003c;
        L_0x002f:
            r2 = r10.this$0;
            r2 = r2.isRunning();
            if (r2 == 0) goto L_0x003c;
        L_0x0037:
            r2 = r10.this$0;
            r2.dispatch(r1);
        L_0x003c:
            throw r0;
        L_0x003d:
            r0 = move-exception;
            monitor-exit(r10);	 Catch:{ all -> 0x003d }
            throw r0;	 Catch:{ all -> 0x0017 }
        L_0x0040:
            r0 = r10.this$0;	 Catch:{ all -> 0x0017 }
            r3 = org.mortbay.thread.BoundedThreadPool.access$100(r0);	 Catch:{ all -> 0x0017 }
            monitor-enter(r3);	 Catch:{ all -> 0x0017 }
            r0 = r10.this$0;	 Catch:{ all -> 0x0063 }
            r0 = org.mortbay.thread.BoundedThreadPool.access$200(r0);	 Catch:{ all -> 0x0063 }
            r0 = r0.size();	 Catch:{ all -> 0x0063 }
            if (r0 <= 0) goto L_0x0066;
        L_0x0053:
            r0 = r10.this$0;	 Catch:{ all -> 0x0063 }
            r0 = org.mortbay.thread.BoundedThreadPool.access$200(r0);	 Catch:{ all -> 0x0063 }
            r1 = 0;
            r0 = r0.remove(r1);	 Catch:{ all -> 0x0063 }
            r0 = (java.lang.Runnable) r0;	 Catch:{ all -> 0x0063 }
            monitor-exit(r3);	 Catch:{ all -> 0x0063 }
            r1 = r0;
            goto L_0x0008;
        L_0x0063:
            r0 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0063 }
            throw r0;	 Catch:{ all -> 0x0017 }
        L_0x0066:
            r0 = r10.this$0;	 Catch:{ all -> 0x0063 }
            r4 = 0;
            org.mortbay.thread.BoundedThreadPool.access$302(r0, r4);	 Catch:{ all -> 0x0063 }
            r0 = r10.this$0;	 Catch:{ all -> 0x0063 }
            r0 = org.mortbay.thread.BoundedThreadPool.access$400(r0);	 Catch:{ all -> 0x0063 }
            r0 = r0.size();	 Catch:{ all -> 0x0063 }
            r4 = r10.this$0;	 Catch:{ all -> 0x0063 }
            r4 = org.mortbay.thread.BoundedThreadPool.access$500(r4);	 Catch:{ all -> 0x0063 }
            if (r0 > r4) goto L_0x009c;
        L_0x007e:
            r0 = r10.this$0;	 Catch:{ all -> 0x0063 }
            r0 = org.mortbay.thread.BoundedThreadPool.access$600(r0);	 Catch:{ all -> 0x0063 }
            r0 = r0.size();	 Catch:{ all -> 0x0063 }
            if (r0 <= 0) goto L_0x00de;
        L_0x008a:
            r0 = r10.this$0;	 Catch:{ all -> 0x0063 }
            r0 = org.mortbay.thread.BoundedThreadPool.access$400(r0);	 Catch:{ all -> 0x0063 }
            r0 = r0.size();	 Catch:{ all -> 0x0063 }
            r4 = r10.this$0;	 Catch:{ all -> 0x0063 }
            r4 = org.mortbay.thread.BoundedThreadPool.access$700(r4);	 Catch:{ all -> 0x0063 }
            if (r0 <= r4) goto L_0x00de;
        L_0x009c:
            r4 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0063 }
            r0 = r10.this$0;	 Catch:{ all -> 0x0063 }
            r6 = org.mortbay.thread.BoundedThreadPool.access$800(r0);	 Catch:{ all -> 0x0063 }
            r6 = r4 - r6;
            r0 = r10.this$0;	 Catch:{ all -> 0x0063 }
            r0 = r0.getMaxIdleTimeMs();	 Catch:{ all -> 0x0063 }
            r8 = (long) r0;	 Catch:{ all -> 0x0063 }
            r0 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
            if (r0 <= 0) goto L_0x00de;
        L_0x00b3:
            r0 = r10.this$0;	 Catch:{ all -> 0x0063 }
            org.mortbay.thread.BoundedThreadPool.access$802(r0, r4);	 Catch:{ all -> 0x0063 }
            monitor-exit(r3);	 Catch:{ all -> 0x0063 }
            r0 = r10.this$0;
            r1 = org.mortbay.thread.BoundedThreadPool.access$100(r0);
            monitor-enter(r1);
            r0 = r10.this$0;	 Catch:{ all -> 0x0149 }
            r0 = org.mortbay.thread.BoundedThreadPool.access$400(r0);	 Catch:{ all -> 0x0149 }
            r0.remove(r10);	 Catch:{ all -> 0x0149 }
            monitor-exit(r1);	 Catch:{ all -> 0x0149 }
            monitor-enter(r10);
            r1 = r10._job;	 Catch:{ all -> 0x014c }
            monitor-exit(r10);	 Catch:{ all -> 0x014c }
            if (r1 == 0) goto L_0x00dd;
        L_0x00d0:
            r0 = r10.this$0;
            r0 = r0.isRunning();
            if (r0 == 0) goto L_0x00dd;
        L_0x00d8:
            r0 = r10.this$0;
        L_0x00da:
            r0.dispatch(r1);
        L_0x00dd:
            return;
        L_0x00de:
            r0 = r10.this$0;	 Catch:{ all -> 0x0063 }
            r0 = org.mortbay.thread.BoundedThreadPool.access$600(r0);	 Catch:{ all -> 0x0063 }
            r0.add(r10);	 Catch:{ all -> 0x0063 }
            monitor-exit(r3);	 Catch:{ all -> 0x0063 }
            monitor-enter(r10);	 Catch:{ InterruptedException -> 0x0116 }
            r0 = r10._job;	 Catch:{ all -> 0x0113 }
            if (r0 != 0) goto L_0x00f7;
        L_0x00ed:
            r0 = r10.this$0;	 Catch:{ all -> 0x0113 }
            r0 = r0.getMaxIdleTimeMs();	 Catch:{ all -> 0x0113 }
            r4 = (long) r0;	 Catch:{ all -> 0x0113 }
            r10.wait(r4);	 Catch:{ all -> 0x0113 }
        L_0x00f7:
            r1 = r10._job;	 Catch:{ all -> 0x0113 }
            r0 = 0;
            r10._job = r0;	 Catch:{ all -> 0x0113 }
            monitor-exit(r10);	 Catch:{ all -> 0x0113 }
            r0 = r10.this$0;	 Catch:{ all -> 0x0017 }
            r3 = org.mortbay.thread.BoundedThreadPool.access$100(r0);	 Catch:{ all -> 0x0017 }
            monitor-enter(r3);	 Catch:{ all -> 0x0017 }
            r0 = r10.this$0;	 Catch:{ all -> 0x0110 }
            r0 = org.mortbay.thread.BoundedThreadPool.access$600(r0);	 Catch:{ all -> 0x0110 }
            r0.remove(r10);	 Catch:{ all -> 0x0110 }
            monitor-exit(r3);	 Catch:{ all -> 0x0110 }
            goto L_0x0008;
        L_0x0110:
            r0 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0110 }
            throw r0;	 Catch:{ all -> 0x0017 }
        L_0x0113:
            r0 = move-exception;
            monitor-exit(r10);	 Catch:{ all -> 0x0113 }
            throw r0;	 Catch:{ InterruptedException -> 0x0116 }
        L_0x0116:
            r0 = move-exception;
            org.mortbay.log.Log.ignore(r0);	 Catch:{ all -> 0x0130 }
            r0 = r10.this$0;	 Catch:{ all -> 0x0017 }
            r3 = org.mortbay.thread.BoundedThreadPool.access$100(r0);	 Catch:{ all -> 0x0017 }
            monitor-enter(r3);	 Catch:{ all -> 0x0017 }
            r0 = r10.this$0;	 Catch:{ all -> 0x012d }
            r0 = org.mortbay.thread.BoundedThreadPool.access$600(r0);	 Catch:{ all -> 0x012d }
            r0.remove(r10);	 Catch:{ all -> 0x012d }
            monitor-exit(r3);	 Catch:{ all -> 0x012d }
            goto L_0x0008;
        L_0x012d:
            r0 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x012d }
            throw r0;	 Catch:{ all -> 0x0017 }
        L_0x0130:
            r0 = move-exception;
            r1 = r10.this$0;	 Catch:{ all -> 0x0017 }
            r1 = org.mortbay.thread.BoundedThreadPool.access$100(r1);	 Catch:{ all -> 0x0017 }
            monitor-enter(r1);	 Catch:{ all -> 0x0017 }
            r2 = r10.this$0;	 Catch:{ all -> 0x014f }
            r2 = org.mortbay.thread.BoundedThreadPool.access$600(r2);	 Catch:{ all -> 0x014f }
            r2.remove(r10);	 Catch:{ all -> 0x014f }
            monitor-exit(r1);	 Catch:{ all -> 0x014f }
            throw r0;	 Catch:{ all -> 0x0017 }
        L_0x0143:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0143 }
            throw r0;
        L_0x0146:
            r0 = move-exception;
            monitor-exit(r10);	 Catch:{ all -> 0x0146 }
            throw r0;
        L_0x0149:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0149 }
            throw r0;
        L_0x014c:
            r0 = move-exception;
            monitor-exit(r10);	 Catch:{ all -> 0x014c }
            throw r0;
        L_0x014f:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x014f }
            throw r0;	 Catch:{ all -> 0x0017 }
        L_0x0152:
            r0 = r10.this$0;
            r1 = org.mortbay.thread.BoundedThreadPool.access$100(r0);
            monitor-enter(r1);
            r0 = r10.this$0;	 Catch:{ all -> 0x0175 }
            r0 = org.mortbay.thread.BoundedThreadPool.access$400(r0);	 Catch:{ all -> 0x0175 }
            r0.remove(r10);	 Catch:{ all -> 0x0175 }
            monitor-exit(r1);	 Catch:{ all -> 0x0175 }
            monitor-enter(r10);
            r1 = r10._job;	 Catch:{ all -> 0x0178 }
            monitor-exit(r10);	 Catch:{ all -> 0x0178 }
            if (r1 == 0) goto L_0x00dd;
        L_0x0169:
            r0 = r10.this$0;
            r0 = r0.isRunning();
            if (r0 == 0) goto L_0x00dd;
        L_0x0171:
            r0 = r10.this$0;
            goto L_0x00da;
        L_0x0175:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0175 }
            throw r0;
        L_0x0178:
            r0 = move-exception;
            monitor-exit(r10);	 Catch:{ all -> 0x0178 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mortbay.thread.BoundedThreadPool.PoolThread.run():void");
        }
    }

    public BoundedThreadPool() {
        StringBuffer append = new StringBuffer().append("btpool");
        int i = __id;
        __id = i + 1;
        this._name = append.append(i).toString();
    }

    static boolean access$000(BoundedThreadPool boundedThreadPool) {
        return boundedThreadPool._daemon;
    }

    static Object access$100(BoundedThreadPool boundedThreadPool) {
        return boundedThreadPool._lock;
    }

    static List access$200(BoundedThreadPool boundedThreadPool) {
        return boundedThreadPool._queue;
    }

    static boolean access$302(BoundedThreadPool boundedThreadPool, boolean z) {
        boundedThreadPool._warned = z;
        return z;
    }

    static Set access$400(BoundedThreadPool boundedThreadPool) {
        return boundedThreadPool._threads;
    }

    static int access$500(BoundedThreadPool boundedThreadPool) {
        return boundedThreadPool._maxThreads;
    }

    static List access$600(BoundedThreadPool boundedThreadPool) {
        return boundedThreadPool._idle;
    }

    static int access$700(BoundedThreadPool boundedThreadPool) {
        return boundedThreadPool._minThreads;
    }

    static long access$800(BoundedThreadPool boundedThreadPool) {
        return boundedThreadPool._lastShrink;
    }

    static long access$802(BoundedThreadPool boundedThreadPool, long j) {
        boundedThreadPool._lastShrink = j;
        return j;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dispatch(java.lang.Runnable r5) {
        /*
        r4 = this;
        r1 = 1;
        r2 = r4._lock;
        monitor-enter(r2);
        r0 = r4.isRunning();	 Catch:{ all -> 0x0027 }
        if (r0 == 0) goto L_0x000c;
    L_0x000a:
        if (r5 != 0) goto L_0x000f;
    L_0x000c:
        monitor-exit(r2);	 Catch:{ all -> 0x0027 }
        r0 = 0;
    L_0x000e:
        return r0;
    L_0x000f:
        r0 = r4._idle;	 Catch:{ all -> 0x0027 }
        r0 = r0.size();	 Catch:{ all -> 0x0027 }
        if (r0 <= 0) goto L_0x002a;
    L_0x0017:
        r3 = r4._idle;	 Catch:{ all -> 0x0027 }
        r0 = r0 + -1;
        r0 = r3.remove(r0);	 Catch:{ all -> 0x0027 }
        r0 = (org.mortbay.thread.BoundedThreadPool.PoolThread) r0;	 Catch:{ all -> 0x0027 }
        r0.dispatch(r5);	 Catch:{ all -> 0x0027 }
    L_0x0024:
        monitor-exit(r2);	 Catch:{ all -> 0x0027 }
        r0 = r1;
        goto L_0x000e;
    L_0x0027:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0027 }
        throw r0;
    L_0x002a:
        r0 = r4._threads;	 Catch:{ all -> 0x0027 }
        r0 = r0.size();	 Catch:{ all -> 0x0027 }
        r3 = r4._maxThreads;	 Catch:{ all -> 0x0027 }
        if (r0 >= r3) goto L_0x0038;
    L_0x0034:
        r4.newThread(r5);	 Catch:{ all -> 0x0027 }
        goto L_0x0024;
    L_0x0038:
        r0 = r4._warned;	 Catch:{ all -> 0x0027 }
        if (r0 != 0) goto L_0x0044;
    L_0x003c:
        r0 = 1;
        r4._warned = r0;	 Catch:{ all -> 0x0027 }
        r0 = "Out of threads for {}";
        org.mortbay.log.Log.debug(r0, r4);	 Catch:{ all -> 0x0027 }
    L_0x0044:
        r0 = r4._queue;	 Catch:{ all -> 0x0027 }
        r0.add(r5);	 Catch:{ all -> 0x0027 }
        goto L_0x0024;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.thread.BoundedThreadPool.dispatch(java.lang.Runnable):boolean");
    }

    protected void doStart() throws Exception {
        if (this._maxThreads < this._minThreads || this._minThreads <= 0) {
            throw new IllegalArgumentException("!0<minThreads<maxThreads");
        }
        this._threads = new HashSet();
        this._idle = new ArrayList();
        this._queue = new LinkedList();
        for (int i = 0; i < this._minThreads; i++) {
            newThread(null);
        }
    }

    protected void doStop() throws Exception {
        super.doStop();
        for (int i = 0; i < 100; i++) {
            synchronized (this._lock) {
                for (Thread interrupt : this._threads) {
                    interrupt.interrupt();
                }
            }
            Thread.yield();
            if (this._threads.size() == 0) {
                break;
            }
            try {
                Thread.sleep((long) (i * 100));
            } catch (InterruptedException e) {
            }
        }
        if (this._threads.size() > 0) {
            Log.warn(new StringBuffer().append(this._threads.size()).append(" threads could not be stopped").toString());
        }
        synchronized (this._joinLock) {
            this._joinLock.notifyAll();
        }
    }

    public int getIdleThreads() {
        return this._idle == null ? 0 : this._idle.size();
    }

    public int getLowThreads() {
        return this._lowThreads;
    }

    public int getMaxIdleTimeMs() {
        return this._maxIdleTimeMs;
    }

    public int getMaxThreads() {
        return this._maxThreads;
    }

    public int getMinThreads() {
        return this._minThreads;
    }

    public String getName() {
        return this._name;
    }

    public int getQueueSize() {
        int size;
        synchronized (this._lock) {
            size = this._queue.size();
        }
        return size;
    }

    public int getThreads() {
        return this._threads.size();
    }

    public int getThreadsPriority() {
        return this._priority;
    }

    public boolean isDaemon() {
        return this._daemon;
    }

    public boolean isLowOnThreads() {
        boolean z;
        synchronized (this._lock) {
            z = this._queue.size() > this._lowThreads;
        }
        return z;
    }

    public void join() throws InterruptedException {
        synchronized (this._joinLock) {
            while (isRunning()) {
                this._joinLock.wait();
            }
        }
        while (isStopping()) {
            Thread.sleep(10);
        }
    }

    protected PoolThread newThread(Runnable runnable) {
        PoolThread poolThread;
        synchronized (this._lock) {
            poolThread = new PoolThread(this, runnable);
            this._threads.add(poolThread);
            StringBuffer append = new StringBuffer().append(this._name).append("-");
            int i = this._id;
            this._id = i + 1;
            poolThread.setName(append.append(i).toString());
            poolThread.start();
        }
        return poolThread;
    }

    public void setDaemon(boolean z) {
        this._daemon = z;
    }

    public void setLowThreads(int i) {
        this._lowThreads = i;
    }

    public void setMaxIdleTimeMs(int i) {
        this._maxIdleTimeMs = i;
    }

    public void setMaxThreads(int i) {
        if (!isStarted() || i >= this._minThreads) {
            this._maxThreads = i;
            return;
        }
        throw new IllegalArgumentException("!minThreads<maxThreads");
    }

    public void setMinThreads(int i) {
        if (!isStarted() || (i > 0 && i <= this._maxThreads)) {
            this._minThreads = i;
            synchronized (this._lock) {
                while (isStarted() && this._threads.size() < this._minThreads) {
                    newThread(null);
                }
            }
            return;
        }
        throw new IllegalArgumentException("!0<=minThreads<maxThreads");
    }

    public void setName(String str) {
        this._name = str;
    }

    public void setThreadsPriority(int i) {
        this._priority = i;
    }

    protected void stopJob(Thread thread, Object obj) {
        thread.interrupt();
    }
}
