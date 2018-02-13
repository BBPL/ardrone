package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;

public final class ListenableFutureTask<V> extends FutureTask<V> implements ListenableFuture<V> {
    private final ExecutionList executionList = new ExecutionList();

    private ListenableFutureTask(Runnable runnable, @Nullable V v) {
        super(runnable, v);
    }

    private ListenableFutureTask(Callable<V> callable) {
        super(callable);
    }

    public static <V> ListenableFutureTask<V> create(Runnable runnable, @Nullable V v) {
        return new ListenableFutureTask(runnable, v);
    }

    public static <V> ListenableFutureTask<V> create(Callable<V> callable) {
        return new ListenableFutureTask(callable);
    }

    public void addListener(Runnable runnable, Executor executor) {
        this.executionList.add(runnable, executor);
    }

    protected void done() {
        this.executionList.execute();
    }
}
