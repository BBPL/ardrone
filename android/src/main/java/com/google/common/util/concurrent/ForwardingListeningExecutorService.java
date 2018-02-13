package com.google.common.util.concurrent;

import java.util.concurrent.Callable;

public abstract class ForwardingListeningExecutorService extends ForwardingExecutorService implements ListeningExecutorService {
    protected ForwardingListeningExecutorService() {
    }

    protected abstract ListeningExecutorService delegate();

    public ListenableFuture<?> submit(Runnable runnable) {
        return delegate().submit(runnable);
    }

    public <T> ListenableFuture<T> submit(Runnable runnable, T t) {
        return delegate().submit(runnable, t);
    }

    public <T> ListenableFuture<T> submit(Callable<T> callable) {
        return delegate().submit((Callable) callable);
    }
}
