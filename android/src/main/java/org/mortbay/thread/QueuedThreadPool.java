package org.mortbay.thread;

import com.google.api.client.http.ExponentialBackOffPolicy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.log.Log;

public class QueuedThreadPool extends AbstractLifeCycle implements Serializable, ThreadPool {
    private boolean _daemon;
    private int _id;
    private List _idle;
    private Runnable[] _jobs;
    private final Object _joinLock;
    private long _lastShrink;
    private final Object _lock;
    private int _lowThreads;
    private int _maxIdleTimeMs;
    private int _maxQueued;
    private int _maxStopTimeMs;
    private int _maxThreads;
    private int _minThreads;
    private String _name;
    private int _nextJob;
    private int _nextJobSlot;
    private int _priority;
    private int _queued;
    private int _spawnOrShrinkAt;
    private Set _threads;
    private final Object _threadsLock;
    private boolean _warned;

    static class C13481 {
    }

    private class Lock {
        private final QueuedThreadPool this$0;

        private Lock(QueuedThreadPool queuedThreadPool) {
            this.this$0 = queuedThreadPool;
        }

        Lock(QueuedThreadPool queuedThreadPool, C13481 c13481) {
            this(queuedThreadPool);
        }
    }

    public class PoolThread extends Thread {
        Runnable _job = null;
        private final QueuedThreadPool this$0;

        PoolThread(QueuedThreadPool queuedThreadPool) {
            this.this$0 = queuedThreadPool;
            setDaemon(QueuedThreadPool.access$100(queuedThreadPool));
            setPriority(QueuedThreadPool.access$200(queuedThreadPool));
        }

        void dispatch(Runnable runnable) {
            synchronized (this) {
                this._job = runnable;
                notify();
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r10 = this;
            r0 = 0;
            r1 = 0;
            r2 = r0;
            r0 = r1;
        L_0x0004:
            r3 = r10.this$0;	 Catch:{ InterruptedException -> 0x005d }
            r3 = r3.isRunning();	 Catch:{ InterruptedException -> 0x005d }
            if (r3 == 0) goto L_0x017d;
        L_0x000c:
            if (r2 == 0) goto L_0x0012;
        L_0x000e:
            r2.run();	 Catch:{ InterruptedException -> 0x005d }
            r0 = r1;
        L_0x0012:
            r2 = r10.this$0;	 Catch:{ InterruptedException -> 0x005d }
            r3 = org.mortbay.thread.QueuedThreadPool.access$300(r2);	 Catch:{ InterruptedException -> 0x005d }
            monitor-enter(r3);	 Catch:{ InterruptedException -> 0x005d }
            r2 = r10.this$0;	 Catch:{ all -> 0x005a }
            r2 = org.mortbay.thread.QueuedThreadPool.access$400(r2);	 Catch:{ all -> 0x005a }
            if (r2 <= 0) goto L_0x008f;
        L_0x0021:
            r2 = r10.this$0;	 Catch:{ all -> 0x005a }
            org.mortbay.thread.QueuedThreadPool.access$410(r2);	 Catch:{ all -> 0x005a }
            r2 = r10.this$0;	 Catch:{ all -> 0x005a }
            r2 = org.mortbay.thread.QueuedThreadPool.access$500(r2);	 Catch:{ all -> 0x005a }
            r4 = r10.this$0;	 Catch:{ all -> 0x005a }
            r4 = org.mortbay.thread.QueuedThreadPool.access$600(r4);	 Catch:{ all -> 0x005a }
            r2 = r2[r4];	 Catch:{ all -> 0x005a }
            r4 = r10.this$0;	 Catch:{ all -> 0x005a }
            r4 = org.mortbay.thread.QueuedThreadPool.access$500(r4);	 Catch:{ all -> 0x005a }
            r5 = r10.this$0;	 Catch:{ all -> 0x005a }
            r5 = org.mortbay.thread.QueuedThreadPool.access$608(r5);	 Catch:{ all -> 0x005a }
            r6 = 0;
            r4[r5] = r6;	 Catch:{ all -> 0x005a }
            r4 = r10.this$0;	 Catch:{ all -> 0x005a }
            r4 = org.mortbay.thread.QueuedThreadPool.access$600(r4);	 Catch:{ all -> 0x005a }
            r5 = r10.this$0;	 Catch:{ all -> 0x005a }
            r5 = org.mortbay.thread.QueuedThreadPool.access$500(r5);	 Catch:{ all -> 0x005a }
            r5 = r5.length;	 Catch:{ all -> 0x005a }
            if (r4 != r5) goto L_0x0058;
        L_0x0052:
            r4 = r10.this$0;	 Catch:{ all -> 0x005a }
            r5 = 0;
            org.mortbay.thread.QueuedThreadPool.access$602(r4, r5);	 Catch:{ all -> 0x005a }
        L_0x0058:
            monitor-exit(r3);	 Catch:{ all -> 0x005a }
            goto L_0x0004;
        L_0x005a:
            r0 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x005a }
            throw r0;	 Catch:{ InterruptedException -> 0x005d }
        L_0x005d:
            r0 = move-exception;
            org.mortbay.log.Log.ignore(r0);	 Catch:{ all -> 0x0133 }
            r0 = r10.this$0;
            r1 = org.mortbay.thread.QueuedThreadPool.access$300(r0);
            monitor-enter(r1);
            r0 = r10.this$0;	 Catch:{ all -> 0x016b }
            r0 = org.mortbay.thread.QueuedThreadPool.access$1000(r0);	 Catch:{ all -> 0x016b }
            r0.remove(r10);	 Catch:{ all -> 0x016b }
            monitor-exit(r1);	 Catch:{ all -> 0x016b }
            r0 = r10.this$0;
            r1 = org.mortbay.thread.QueuedThreadPool.access$1300(r0);
            monitor-enter(r1);
            r0 = r10.this$0;	 Catch:{ all -> 0x016e }
            r0 = org.mortbay.thread.QueuedThreadPool.access$700(r0);	 Catch:{ all -> 0x016e }
            r0.remove(r10);	 Catch:{ all -> 0x016e }
            monitor-exit(r1);	 Catch:{ all -> 0x016e }
            monitor-enter(r10);
            r1 = r10._job;	 Catch:{ all -> 0x0171 }
            monitor-exit(r10);	 Catch:{ all -> 0x0171 }
            if (r1 == 0) goto L_0x008e;
        L_0x0089:
            r0 = r10.this$0;
        L_0x008b:
            r0.dispatch(r1);
        L_0x008e:
            return;
        L_0x008f:
            r2 = r10.this$0;	 Catch:{ all -> 0x005a }
            r2 = org.mortbay.thread.QueuedThreadPool.access$700(r2);	 Catch:{ all -> 0x005a }
            r2 = r2.size();	 Catch:{ all -> 0x005a }
            r4 = r10.this$0;	 Catch:{ all -> 0x005a }
            r4 = org.mortbay.thread.QueuedThreadPool.access$800(r4);	 Catch:{ all -> 0x005a }
            if (r2 <= r4) goto L_0x010c;
        L_0x00a1:
            r4 = r10.this$0;	 Catch:{ all -> 0x005a }
            r4 = org.mortbay.thread.QueuedThreadPool.access$900(r4);	 Catch:{ all -> 0x005a }
            if (r2 > r4) goto L_0x00bb;
        L_0x00a9:
            r2 = r10.this$0;	 Catch:{ all -> 0x005a }
            r2 = org.mortbay.thread.QueuedThreadPool.access$1000(r2);	 Catch:{ all -> 0x005a }
            r2 = r2.size();	 Catch:{ all -> 0x005a }
            r4 = r10.this$0;	 Catch:{ all -> 0x005a }
            r4 = org.mortbay.thread.QueuedThreadPool.access$1100(r4);	 Catch:{ all -> 0x005a }
            if (r2 <= r4) goto L_0x010c;
        L_0x00bb:
            r4 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x005a }
            r2 = r10.this$0;	 Catch:{ all -> 0x005a }
            r6 = org.mortbay.thread.QueuedThreadPool.access$1200(r2);	 Catch:{ all -> 0x005a }
            r6 = r4 - r6;
            r2 = r10.this$0;	 Catch:{ all -> 0x005a }
            r2 = r2.getMaxIdleTimeMs();	 Catch:{ all -> 0x005a }
            r8 = (long) r2;	 Catch:{ all -> 0x005a }
            r2 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
            if (r2 <= 0) goto L_0x010c;
        L_0x00d2:
            r0 = r10.this$0;	 Catch:{ all -> 0x005a }
            org.mortbay.thread.QueuedThreadPool.access$1202(r0, r4);	 Catch:{ all -> 0x005a }
            r0 = r10.this$0;	 Catch:{ all -> 0x005a }
            r0 = org.mortbay.thread.QueuedThreadPool.access$1000(r0);	 Catch:{ all -> 0x005a }
            r0.remove(r10);	 Catch:{ all -> 0x005a }
            monitor-exit(r3);	 Catch:{ all -> 0x005a }
            r0 = r10.this$0;
            r1 = org.mortbay.thread.QueuedThreadPool.access$300(r0);
            monitor-enter(r1);
            r0 = r10.this$0;	 Catch:{ all -> 0x0174 }
            r0 = org.mortbay.thread.QueuedThreadPool.access$1000(r0);	 Catch:{ all -> 0x0174 }
            r0.remove(r10);	 Catch:{ all -> 0x0174 }
            monitor-exit(r1);	 Catch:{ all -> 0x0174 }
            r0 = r10.this$0;
            r1 = org.mortbay.thread.QueuedThreadPool.access$1300(r0);
            monitor-enter(r1);
            r0 = r10.this$0;	 Catch:{ all -> 0x0177 }
            r0 = org.mortbay.thread.QueuedThreadPool.access$700(r0);	 Catch:{ all -> 0x0177 }
            r0.remove(r10);	 Catch:{ all -> 0x0177 }
            monitor-exit(r1);	 Catch:{ all -> 0x0177 }
            monitor-enter(r10);
            r1 = r10._job;	 Catch:{ all -> 0x017a }
            monitor-exit(r10);	 Catch:{ all -> 0x017a }
            if (r1 == 0) goto L_0x008e;
        L_0x0109:
            r0 = r10.this$0;
            goto L_0x008b;
        L_0x010c:
            if (r0 != 0) goto L_0x0118;
        L_0x010e:
            r0 = r10.this$0;	 Catch:{ all -> 0x005a }
            r0 = org.mortbay.thread.QueuedThreadPool.access$1000(r0);	 Catch:{ all -> 0x005a }
            r0.add(r10);	 Catch:{ all -> 0x005a }
            r0 = 1;
        L_0x0118:
            monitor-exit(r3);	 Catch:{ all -> 0x005a }
            monitor-enter(r10);	 Catch:{ InterruptedException -> 0x005d }
            r2 = r10._job;	 Catch:{ all -> 0x0130 }
            if (r2 != 0) goto L_0x0128;
        L_0x011e:
            r2 = r10.this$0;	 Catch:{ all -> 0x0130 }
            r2 = r2.getMaxIdleTimeMs();	 Catch:{ all -> 0x0130 }
            r2 = (long) r2;	 Catch:{ all -> 0x0130 }
            r10.wait(r2);	 Catch:{ all -> 0x0130 }
        L_0x0128:
            r2 = r10._job;	 Catch:{ all -> 0x0130 }
            r3 = 0;
            r10._job = r3;	 Catch:{ all -> 0x0130 }
            monitor-exit(r10);	 Catch:{ all -> 0x0130 }
            goto L_0x0004;
        L_0x0130:
            r0 = move-exception;
            monitor-exit(r10);	 Catch:{ all -> 0x0130 }
            throw r0;	 Catch:{ InterruptedException -> 0x005d }
        L_0x0133:
            r0 = move-exception;
            r1 = r10.this$0;
            r1 = org.mortbay.thread.QueuedThreadPool.access$300(r1);
            monitor-enter(r1);
            r2 = r10.this$0;	 Catch:{ all -> 0x0162 }
            r2 = org.mortbay.thread.QueuedThreadPool.access$1000(r2);	 Catch:{ all -> 0x0162 }
            r2.remove(r10);	 Catch:{ all -> 0x0162 }
            monitor-exit(r1);	 Catch:{ all -> 0x0162 }
            r1 = r10.this$0;
            r1 = org.mortbay.thread.QueuedThreadPool.access$1300(r1);
            monitor-enter(r1);
            r2 = r10.this$0;	 Catch:{ all -> 0x0165 }
            r2 = org.mortbay.thread.QueuedThreadPool.access$700(r2);	 Catch:{ all -> 0x0165 }
            r2.remove(r10);	 Catch:{ all -> 0x0165 }
            monitor-exit(r1);	 Catch:{ all -> 0x0165 }
            monitor-enter(r10);
            r1 = r10._job;	 Catch:{ all -> 0x0168 }
            monitor-exit(r10);	 Catch:{ all -> 0x0168 }
            if (r1 == 0) goto L_0x0161;
        L_0x015c:
            r2 = r10.this$0;
            r2.dispatch(r1);
        L_0x0161:
            throw r0;
        L_0x0162:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0162 }
            throw r0;
        L_0x0165:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0165 }
            throw r0;
        L_0x0168:
            r0 = move-exception;
            monitor-exit(r10);	 Catch:{ all -> 0x0168 }
            throw r0;
        L_0x016b:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x016b }
            throw r0;
        L_0x016e:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x016e }
            throw r0;
        L_0x0171:
            r0 = move-exception;
            monitor-exit(r10);	 Catch:{ all -> 0x0171 }
            throw r0;
        L_0x0174:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0174 }
            throw r0;
        L_0x0177:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0177 }
            throw r0;
        L_0x017a:
            r0 = move-exception;
            monitor-exit(r10);	 Catch:{ all -> 0x017a }
            throw r0;
        L_0x017d:
            r0 = r10.this$0;
            r1 = org.mortbay.thread.QueuedThreadPool.access$300(r0);
            monitor-enter(r1);
            r0 = r10.this$0;	 Catch:{ all -> 0x01a9 }
            r0 = org.mortbay.thread.QueuedThreadPool.access$1000(r0);	 Catch:{ all -> 0x01a9 }
            r0.remove(r10);	 Catch:{ all -> 0x01a9 }
            monitor-exit(r1);	 Catch:{ all -> 0x01a9 }
            r0 = r10.this$0;
            r1 = org.mortbay.thread.QueuedThreadPool.access$1300(r0);
            monitor-enter(r1);
            r0 = r10.this$0;	 Catch:{ all -> 0x01ac }
            r0 = org.mortbay.thread.QueuedThreadPool.access$700(r0);	 Catch:{ all -> 0x01ac }
            r0.remove(r10);	 Catch:{ all -> 0x01ac }
            monitor-exit(r1);	 Catch:{ all -> 0x01ac }
            monitor-enter(r10);
            r1 = r10._job;	 Catch:{ all -> 0x01af }
            monitor-exit(r10);	 Catch:{ all -> 0x01af }
            if (r1 == 0) goto L_0x008e;
        L_0x01a5:
            r0 = r10.this$0;
            goto L_0x008b;
        L_0x01a9:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x01a9 }
            throw r0;
        L_0x01ac:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x01ac }
            throw r0;
        L_0x01af:
            r0 = move-exception;
            monitor-exit(r10);	 Catch:{ all -> 0x01af }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mortbay.thread.QueuedThreadPool.PoolThread.run():void");
        }
    }

    public QueuedThreadPool() {
        this._lock = new Lock(this, null);
        this._threadsLock = new Lock(this, null);
        this._joinLock = new Lock(this, null);
        this._maxIdleTimeMs = ExponentialBackOffPolicy.DEFAULT_MAX_INTERVAL_MILLIS;
        this._maxThreads = 250;
        this._minThreads = 2;
        this._warned = false;
        this._lowThreads = 0;
        this._priority = 5;
        this._spawnOrShrinkAt = 0;
        this._name = new StringBuffer().append("qtp-").append(hashCode()).toString();
    }

    public QueuedThreadPool(int i) {
        this();
        setMaxThreads(i);
    }

    static boolean access$100(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._daemon;
    }

    static List access$1000(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._idle;
    }

    static int access$1100(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._spawnOrShrinkAt;
    }

    static long access$1200(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._lastShrink;
    }

    static long access$1202(QueuedThreadPool queuedThreadPool, long j) {
        queuedThreadPool._lastShrink = j;
        return j;
    }

    static Object access$1300(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._threadsLock;
    }

    static int access$200(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._priority;
    }

    static Object access$300(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._lock;
    }

    static int access$400(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._queued;
    }

    static int access$410(QueuedThreadPool queuedThreadPool) {
        int i = queuedThreadPool._queued;
        queuedThreadPool._queued = i - 1;
        return i;
    }

    static Runnable[] access$500(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._jobs;
    }

    static int access$600(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._nextJob;
    }

    static int access$602(QueuedThreadPool queuedThreadPool, int i) {
        queuedThreadPool._nextJob = i;
        return i;
    }

    static int access$608(QueuedThreadPool queuedThreadPool) {
        int i = queuedThreadPool._nextJob;
        queuedThreadPool._nextJob = i + 1;
        return i;
    }

    static Set access$700(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._threads;
    }

    static int access$800(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._minThreads;
    }

    static int access$900(QueuedThreadPool queuedThreadPool) {
        return queuedThreadPool._maxThreads;
    }

    public boolean dispatch(Runnable runnable) {
        boolean z;
        boolean z2 = false;
        if (!isRunning() || runnable == null) {
            z = false;
        } else {
            PoolThread poolThread = null;
            synchronized (this._lock) {
                int size = this._idle.size();
                if (size > 0) {
                    poolThread = (PoolThread) this._idle.remove(size - 1);
                } else {
                    this._queued++;
                    if (this._queued > this._maxQueued) {
                        this._maxQueued = this._queued;
                    }
                    Runnable[] runnableArr = this._jobs;
                    int i = this._nextJobSlot;
                    this._nextJobSlot = i + 1;
                    runnableArr[i] = runnable;
                    if (this._nextJobSlot == this._jobs.length) {
                        this._nextJobSlot = 0;
                    }
                    if (this._nextJobSlot == this._nextJob) {
                        Object obj = new Runnable[(this._jobs.length + this._maxThreads)];
                        i = this._jobs.length - this._nextJob;
                        if (i > 0) {
                            System.arraycopy(this._jobs, this._nextJob, obj, 0, i);
                        }
                        if (this._nextJob != 0) {
                            System.arraycopy(this._jobs, 0, obj, i, this._nextJobSlot);
                        }
                        this._jobs = obj;
                        this._nextJob = 0;
                        this._nextJobSlot = this._queued;
                    }
                    if (this._queued > this._spawnOrShrinkAt) {
                        z2 = true;
                    }
                }
            }
            if (poolThread != null) {
                poolThread.dispatch(runnable);
                return true;
            } else if (z2) {
                newThread();
                return true;
            } else {
                z = true;
            }
        }
        return z;
    }

    protected void doStart() throws Exception {
        if (this._maxThreads < this._minThreads || this._minThreads <= 0) {
            throw new IllegalArgumentException("!0<minThreads<maxThreads");
        }
        this._threads = new HashSet();
        this._idle = new ArrayList();
        this._jobs = new Runnable[this._maxThreads];
        for (int i = 0; i < this._minThreads; i++) {
            newThread();
        }
    }

    protected void doStop() throws Exception {
        super.doStop();
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            synchronized (this._threadsLock) {
                for (Thread interrupt : this._threads) {
                    interrupt.interrupt();
                }
            }
            Thread.yield();
            if (this._threads.size() == 0 || (this._maxStopTimeMs > 0 && ((long) this._maxStopTimeMs) < System.currentTimeMillis() - currentTimeMillis)) {
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

    public String dump() {
        StringBuffer stringBuffer = new StringBuffer();
        synchronized (this._threadsLock) {
            for (Thread thread : this._threads) {
                stringBuffer.append(thread.getName()).append(" ").append(thread.toString()).append('\n');
            }
        }
        return stringBuffer.toString();
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

    public int getMaxQueued() {
        return this._maxQueued;
    }

    public int getMaxStopTimeMs() {
        return this._maxStopTimeMs;
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
        return this._queued;
    }

    public int getSpawnOrShrinkAt() {
        return this._spawnOrShrinkAt;
    }

    public int getThreads() {
        return this._threads.size();
    }

    public int getThreadsPriority() {
        return this._priority;
    }

    public boolean interruptThread(String str) {
        synchronized (this._threadsLock) {
            for (Thread thread : this._threads) {
                if (str.equals(thread.getName())) {
                    thread.interrupt();
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isDaemon() {
        return this._daemon;
    }

    public boolean isLowOnThreads() {
        return this._queued > this._lowThreads;
    }

    public void join() throws InterruptedException {
        synchronized (this._joinLock) {
            while (isRunning()) {
                this._joinLock.wait();
            }
        }
        while (isStopping()) {
            Thread.sleep(100);
        }
    }

    protected void newThread() {
        synchronized (this._threadsLock) {
            if (this._threads.size() < this._maxThreads) {
                PoolThread poolThread = new PoolThread(this);
                this._threads.add(poolThread);
                StringBuffer append = new StringBuffer().append(poolThread.hashCode()).append("@").append(this._name).append("-");
                int i = this._id;
                this._id = i + 1;
                poolThread.setName(append.append(i).toString());
                poolThread.start();
            } else if (!this._warned) {
                this._warned = true;
                Log.debug("Max threads for {}", this);
            }
        }
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

    public void setMaxStopTimeMs(int i) {
        this._maxStopTimeMs = i;
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
            synchronized (this._threadsLock) {
                while (isStarted() && this._threads.size() < this._minThreads) {
                    newThread();
                }
            }
            return;
        }
        throw new IllegalArgumentException("!0<=minThreads<maxThreads");
    }

    public void setName(String str) {
        this._name = str;
    }

    public void setSpawnOrShrinkAt(int i) {
        this._spawnOrShrinkAt = i;
    }

    public void setThreadsPriority(int i) {
        this._priority = i;
    }

    protected void stopJob(Thread thread, Object obj) {
        thread.interrupt();
    }

    public boolean stopThread(String str) {
        synchronized (this._threadsLock) {
            for (Thread thread : this._threads) {
                if (str.equals(thread.getName())) {
                    thread.stop();
                    return true;
                }
            }
            return false;
        }
    }
}
