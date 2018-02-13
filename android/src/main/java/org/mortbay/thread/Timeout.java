package org.mortbay.thread;

public class Timeout {
    private long _duration;
    private Task _head;
    private Object _lock;
    private volatile long _now;

    public static class Task {
        long _delay;
        boolean _expired = false;
        Task _next = this;
        Task _prev = this;
        Timeout _timeout;
        long _timestamp = 0;

        static void access$000(Task task) {
            task.unlink();
        }

        static void access$100(Task task, Task task2) {
            task.link(task2);
        }

        private void link(Task task) {
            Task task2 = this._next;
            this._next._prev = task;
            this._next = task;
            this._next._next = task2;
            this._next._prev = this;
        }

        private void unlink() {
            this._next._prev = this._prev;
            this._prev._next = this._next;
            this._prev = this;
            this._next = this;
            this._expired = false;
        }

        public void cancel() {
            Timeout timeout = this._timeout;
            if (timeout != null) {
                synchronized (Timeout.access$300(timeout)) {
                    unlink();
                    this._timestamp = 0;
                }
            }
        }

        public void expire() {
        }

        public void expired() {
        }

        public long getAge() {
            Timeout timeout = this._timeout;
            if (timeout == null) {
                return 0;
            }
            long access$200 = Timeout.access$200(timeout);
            return (access$200 == 0 || this._timestamp == 0) ? 0 : access$200 - this._timestamp;
        }

        public long getTimestamp() {
            return this._timestamp;
        }

        public boolean isExpired() {
            return this._expired;
        }

        public boolean isScheduled() {
            return this._next != this;
        }

        public void reschedule() {
            Timeout timeout = this._timeout;
            if (timeout != null) {
                timeout.schedule(this, this._delay);
            }
        }

        public void schedule(Timeout timeout) {
            timeout.schedule(this);
        }

        public void schedule(Timeout timeout, long j) {
            timeout.schedule(this, j);
        }
    }

    public Timeout() {
        this._now = System.currentTimeMillis();
        this._head = new Task();
        this._lock = new Object();
        this._head._timeout = this;
    }

    public Timeout(Object obj) {
        this._now = System.currentTimeMillis();
        this._head = new Task();
        this._lock = obj;
        this._head._timeout = this;
    }

    static long access$200(Timeout timeout) {
        return timeout._now;
    }

    static Object access$300(Timeout timeout) {
        return timeout._lock;
    }

    public void cancelAll() {
        synchronized (this._lock) {
            Task task = this._head;
            Task task2 = this._head;
            Task task3 = this._head;
            task2._prev = task3;
            task._next = task3;
        }
    }

    public Task expired() {
        long j = this._now;
        synchronized (this._lock) {
            long j2 = this._duration;
            if (this._head._next != this._head) {
                Task task = this._head._next;
                if (task._timestamp > j - j2) {
                    return null;
                }
                Task.access$000(task);
                task._expired = true;
                return task;
            }
            return null;
        }
    }

    public long getDuration() {
        return this._duration;
    }

    public long getNow() {
        return this._now;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long getTimeToNext() {
        /*
        r8 = this;
        r2 = 0;
        r4 = r8._lock;
        monitor-enter(r4);
        r0 = r8._head;	 Catch:{ all -> 0x0024 }
        r0 = r0._next;	 Catch:{ all -> 0x0024 }
        r1 = r8._head;	 Catch:{ all -> 0x0024 }
        if (r0 != r1) goto L_0x0011;
    L_0x000d:
        monitor-exit(r4);	 Catch:{ all -> 0x0024 }
        r0 = -1;
    L_0x0010:
        return r0;
    L_0x0011:
        r0 = r8._duration;	 Catch:{ all -> 0x0024 }
        r5 = r8._head;	 Catch:{ all -> 0x0024 }
        r5 = r5._next;	 Catch:{ all -> 0x0024 }
        r6 = r5._timestamp;	 Catch:{ all -> 0x0024 }
        r0 = r0 + r6;
        r6 = r8._now;	 Catch:{ all -> 0x0024 }
        r0 = r0 - r6;
        r5 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r5 >= 0) goto L_0x0022;
    L_0x0021:
        r0 = r2;
    L_0x0022:
        monitor-exit(r4);	 Catch:{ all -> 0x0024 }
        goto L_0x0010;
    L_0x0024:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0024 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.thread.Timeout.getTimeToNext():long");
    }

    public boolean isEmpty() {
        boolean z;
        synchronized (this._lock) {
            z = this._head._next == this._head;
        }
        return z;
    }

    public void schedule(Task task) {
        schedule(task, 0);
    }

    public void schedule(Task task, long j) {
        synchronized (this._lock) {
            if (task._timestamp != 0) {
                Task.access$000(task);
                task._timestamp = 0;
            }
            task._timeout = this;
            task._expired = false;
            task._delay = j;
            task._timestamp = this._now + j;
            Task task2 = this._head._prev;
            while (task2 != this._head && task2._timestamp > task._timestamp) {
                task2 = task2._prev;
            }
            Task.access$100(task2, task);
        }
    }

    public void setDuration(long j) {
        this._duration = j;
    }

    public long setNow() {
        this._now = System.currentTimeMillis();
        return this._now;
    }

    public void setNow(long j) {
        this._now = j;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void tick() {
        /*
        r10 = this;
        r2 = r10._now;
        r4 = r10._duration;
    L_0x0004:
        r1 = r10._lock;	 Catch:{ Throwable -> 0x0027 }
        monitor-enter(r1);	 Catch:{ Throwable -> 0x0027 }
        r0 = r10._head;	 Catch:{ all -> 0x002e }
        r0 = r0._next;	 Catch:{ all -> 0x002e }
        r6 = r10._head;	 Catch:{ all -> 0x002e }
        if (r0 == r6) goto L_0x0017;
    L_0x000f:
        r6 = r0._timestamp;	 Catch:{ all -> 0x002e }
        r8 = r2 - r4;
        r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r6 <= 0) goto L_0x0019;
    L_0x0017:
        monitor-exit(r1);	 Catch:{ all -> 0x002e }
        return;
    L_0x0019:
        org.mortbay.thread.Timeout.Task.access$000(r0);	 Catch:{ all -> 0x002e }
        r6 = 1;
        r0._expired = r6;	 Catch:{ all -> 0x002e }
        r0.expire();	 Catch:{ all -> 0x002e }
        monitor-exit(r1);	 Catch:{ all -> 0x002e }
        r0.expired();	 Catch:{ Throwable -> 0x0027 }
        goto L_0x0004;
    L_0x0027:
        r0 = move-exception;
        r1 = "EXCEPTION ";
        org.mortbay.log.Log.warn(r1, r0);
        goto L_0x0004;
    L_0x002e:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x002e }
        throw r0;	 Catch:{ Throwable -> 0x0027 }
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.thread.Timeout.tick():void");
    }

    public void tick(long j) {
        this._now = j;
        tick();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(super.toString());
        for (Task task = this._head._next; task != this._head; task = task._next) {
            stringBuffer.append("-->");
            stringBuffer.append(task);
        }
        return stringBuffer.toString();
    }
}
