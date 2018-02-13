package org.apache.http.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BasicFuture<T> implements Future<T>, Cancellable {
    private final FutureCallback<T> callback;
    private volatile boolean cancelled;
    private volatile boolean completed;
    private volatile Exception ex;
    private volatile T result;

    public BasicFuture(FutureCallback<T> futureCallback) {
        this.callback = futureCallback;
    }

    private T getResult() throws ExecutionException {
        if (this.ex == null) {
            return this.result;
        }
        throw new ExecutionException(this.ex);
    }

    public boolean cancel() {
        return cancel(true);
    }

    public boolean cancel(boolean z) {
        boolean z2 = true;
        synchronized (this) {
            if (this.completed) {
                z2 = false;
            } else {
                this.completed = true;
                this.cancelled = true;
                if (this.callback != null) {
                    this.callback.cancelled();
                }
                notifyAll();
            }
        }
        return z2;
    }

    public boolean completed(T t) {
        boolean z = true;
        synchronized (this) {
            if (this.completed) {
                z = false;
            } else {
                this.completed = true;
                this.result = t;
                if (this.callback != null) {
                    this.callback.completed(t);
                }
                notifyAll();
            }
        }
        return z;
    }

    public boolean failed(Exception exception) {
        boolean z = true;
        synchronized (this) {
            if (this.completed) {
                z = false;
            } else {
                this.completed = true;
                this.ex = exception;
                if (this.callback != null) {
                    this.callback.failed(exception);
                }
                notifyAll();
            }
        }
        return z;
    }

    public T get() throws InterruptedException, ExecutionException {
        T result;
        synchronized (this) {
            while (!this.completed) {
                wait();
            }
            result = getResult();
        }
        return result;
    }

    public T get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        T result;
        synchronized (this) {
            long toMillis = timeUnit.toMillis(j);
            long currentTimeMillis = toMillis <= 0 ? 0 : System.currentTimeMillis();
            if (this.completed) {
                result = getResult();
            } else if (toMillis <= 0) {
                throw new TimeoutException();
            } else {
                long j2 = toMillis;
                do {
                    wait(j2);
                    if (this.completed) {
                        result = getResult();
                    } else {
                        j2 = toMillis - (System.currentTimeMillis() - currentTimeMillis);
                    }
                } while (j2 > 0);
                throw new TimeoutException();
            }
        }
        return result;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public boolean isDone() {
        return this.completed;
    }
}
