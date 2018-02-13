package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

@Beta
public final class JdkFutureAdapters {

    private static class ListenableFutureAdapter<V> extends ForwardingFuture<V> implements ListenableFuture<V> {
        private static final Executor defaultAdapterExecutor = Executors.newCachedThreadPool(threadFactory);
        private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ListenableFutureAdapter-thread-%d").build();
        private final Executor adapterExecutor;
        private final Future<V> delegate;
        private final ExecutionList executionList;
        private final AtomicBoolean hasListeners;

        class C08731 implements Runnable {
            C08731() {
            }

            public void run() {
                try {
                    ListenableFutureAdapter.this.delegate.get();
                } catch (Error e) {
                    throw e;
                } catch (InterruptedException e2) {
                    Thread.currentThread().interrupt();
                    throw new AssertionError(e2);
                } catch (Throwable th) {
                }
                ListenableFutureAdapter.this.executionList.execute();
            }
        }

        ListenableFutureAdapter(Future<V> future) {
            this(future, defaultAdapterExecutor);
        }

        ListenableFutureAdapter(Future<V> future, Executor executor) {
            this.executionList = new ExecutionList();
            this.hasListeners = new AtomicBoolean(false);
            this.delegate = (Future) Preconditions.checkNotNull(future);
            this.adapterExecutor = (Executor) Preconditions.checkNotNull(executor);
        }

        public void addListener(Runnable runnable, Executor executor) {
            this.executionList.add(runnable, executor);
            if (!this.hasListeners.compareAndSet(false, true)) {
                return;
            }
            if (this.delegate.isDone()) {
                this.executionList.execute();
            } else {
                this.adapterExecutor.execute(new C08731());
            }
        }

        protected Future<V> delegate() {
            return this.delegate;
        }
    }

    private JdkFutureAdapters() {
    }

    public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future) {
        return future instanceof ListenableFuture ? (ListenableFuture) future : new ListenableFutureAdapter(future);
    }

    public static <V> ListenableFuture<V> listenInPoolThread(Future<V> future, Executor executor) {
        Preconditions.checkNotNull(executor);
        return future instanceof ListenableFuture ? (ListenableFuture) future : new ListenableFutureAdapter(future, executor);
    }
}
