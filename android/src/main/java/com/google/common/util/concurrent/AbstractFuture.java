package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import javax.annotation.Nullable;

public abstract class AbstractFuture<V> implements ListenableFuture<V> {
    private final ExecutionList executionList = new ExecutionList();
    private final Sync<V> sync = new Sync();

    static final class Sync<V> extends AbstractQueuedSynchronizer {
        static final int CANCELLED = 4;
        static final int COMPLETED = 2;
        static final int COMPLETING = 1;
        static final int RUNNING = 0;
        private static final long serialVersionUID = 0;
        private Throwable exception;
        private V value;

        Sync() {
        }

        private boolean complete(@Nullable V v, @Nullable Throwable th, int i) {
            boolean compareAndSetState = compareAndSetState(0, 1);
            if (compareAndSetState) {
                this.value = v;
                this.exception = th;
                releaseShared(i);
            } else if (getState() == 1) {
                acquireShared(-1);
            }
            return compareAndSetState;
        }

        private V getValue() throws CancellationException, ExecutionException {
            int state = getState();
            switch (state) {
                case 2:
                    if (this.exception == null) {
                        return this.value;
                    }
                    throw new ExecutionException(this.exception);
                case 4:
                    throw new CancellationException("Task was cancelled.");
                default:
                    throw new IllegalStateException("Error, synchronizer in invalid state: " + state);
            }
        }

        boolean cancel() {
            return complete(null, null, 4);
        }

        V get() throws CancellationException, ExecutionException, InterruptedException {
            acquireSharedInterruptibly(-1);
            return getValue();
        }

        V get(long j) throws TimeoutException, CancellationException, ExecutionException, InterruptedException {
            if (tryAcquireSharedNanos(-1, j)) {
                return getValue();
            }
            throw new TimeoutException("Timeout waiting for task.");
        }

        boolean isCancelled() {
            return getState() == 4;
        }

        boolean isDone() {
            return (getState() & 6) != 0;
        }

        boolean set(@Nullable V v) {
            return complete(v, null, 2);
        }

        boolean setException(Throwable th) {
            return complete(null, th, 2);
        }

        protected int tryAcquireShared(int i) {
            return isDone() ? 1 : -1;
        }

        protected boolean tryReleaseShared(int i) {
            setState(i);
            return true;
        }
    }

    protected AbstractFuture() {
    }

    public void addListener(Runnable runnable, Executor executor) {
        this.executionList.add(runnable, executor);
    }

    public boolean cancel(boolean z) {
        if (!this.sync.cancel()) {
            return false;
        }
        this.executionList.execute();
        if (z) {
            interruptTask();
        }
        return true;
    }

    public V get() throws InterruptedException, ExecutionException {
        return this.sync.get();
    }

    public V get(long j, TimeUnit timeUnit) throws InterruptedException, TimeoutException, ExecutionException {
        return this.sync.get(timeUnit.toNanos(j));
    }

    protected void interruptTask() {
    }

    public boolean isCancelled() {
        return this.sync.isCancelled();
    }

    public boolean isDone() {
        return this.sync.isDone();
    }

    protected boolean set(@Nullable V v) {
        boolean z = this.sync.set(v);
        if (z) {
            this.executionList.execute();
        }
        return z;
    }

    protected boolean setException(Throwable th) {
        boolean exception = this.sync.setException((Throwable) Preconditions.checkNotNull(th));
        if (exception) {
            this.executionList.execute();
        }
        if (!(th instanceof Error)) {
            return exception;
        }
        throw ((Error) th);
    }
}
