package org.apache.http.impl.conn.tsccm;

import java.util.Date;
import java.util.concurrent.locks.Condition;

@Deprecated
public class WaitingThread {
    private boolean aborted;
    private final Condition cond;
    private final RouteSpecificPool pool;
    private Thread waiter;

    public WaitingThread(Condition condition, RouteSpecificPool routeSpecificPool) {
        if (condition == null) {
            throw new IllegalArgumentException("Condition must not be null.");
        }
        this.cond = condition;
        this.pool = routeSpecificPool;
    }

    public boolean await(Date date) throws InterruptedException {
        if (this.waiter != null) {
            throw new IllegalStateException("A thread is already waiting on this object.\ncaller: " + Thread.currentThread() + "\nwaiter: " + this.waiter);
        } else if (this.aborted) {
            throw new InterruptedException("Operation interrupted");
        } else {
            boolean awaitUntil;
            this.waiter = Thread.currentThread();
            if (date != null) {
                try {
                    awaitUntil = this.cond.awaitUntil(date);
                } catch (Throwable th) {
                    this.waiter = null;
                }
            } else {
                this.cond.await();
                awaitUntil = true;
            }
            if (this.aborted) {
                throw new InterruptedException("Operation interrupted");
            }
            this.waiter = null;
            return awaitUntil;
        }
    }

    public final Condition getCondition() {
        return this.cond;
    }

    public final RouteSpecificPool getPool() {
        return this.pool;
    }

    public final Thread getThread() {
        return this.waiter;
    }

    public void interrupt() {
        this.aborted = true;
        this.cond.signalAll();
    }

    public void wakeup() {
        if (this.waiter == null) {
            throw new IllegalStateException("Nobody waiting on this object.");
        }
        this.cond.signalAll();
    }
}
