package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class MoreExecutors {

    private static class ListeningDecorator extends AbstractListeningExecutorService {
        final ExecutorService delegate;

        ListeningDecorator(ExecutorService executorService) {
            this.delegate = (ExecutorService) Preconditions.checkNotNull(executorService);
        }

        public boolean awaitTermination(long j, TimeUnit timeUnit) throws InterruptedException {
            return this.delegate.awaitTermination(j, timeUnit);
        }

        public void execute(Runnable runnable) {
            this.delegate.execute(runnable);
        }

        public boolean isShutdown() {
            return this.delegate.isShutdown();
        }

        public boolean isTerminated() {
            return this.delegate.isTerminated();
        }

        public void shutdown() {
            this.delegate.shutdown();
        }

        public List<Runnable> shutdownNow() {
            return this.delegate.shutdownNow();
        }
    }

    private static class SameThreadExecutorService extends AbstractListeningExecutorService {
        private final Lock lock;
        private int runningTasks;
        private boolean shutdown;
        private final Condition termination;

        private SameThreadExecutorService() {
            this.lock = new ReentrantLock();
            this.termination = this.lock.newCondition();
            this.runningTasks = 0;
            this.shutdown = false;
        }

        private void endTask() {
            this.lock.lock();
            try {
                this.runningTasks--;
                if (isTerminated()) {
                    this.termination.signalAll();
                }
                this.lock.unlock();
            } catch (Throwable th) {
                this.lock.unlock();
            }
        }

        private void startTask() {
            this.lock.lock();
            try {
                if (isShutdown()) {
                    throw new RejectedExecutionException("Executor already shutdown");
                }
                this.runningTasks++;
            } finally {
                this.lock.unlock();
            }
        }

        public boolean awaitTermination(long j, TimeUnit timeUnit) throws InterruptedException {
            long toNanos = timeUnit.toNanos(j);
            this.lock.lock();
            while (!isTerminated()) {
                if (toNanos <= 0) {
                    return false;
                }
                try {
                    toNanos = this.termination.awaitNanos(toNanos);
                } finally {
                    this.lock.unlock();
                }
            }
            this.lock.unlock();
            return true;
        }

        public void execute(Runnable runnable) {
            startTask();
            try {
                runnable.run();
            } finally {
                endTask();
            }
        }

        public boolean isShutdown() {
            this.lock.lock();
            try {
                boolean z = this.shutdown;
                return z;
            } finally {
                this.lock.unlock();
            }
        }

        public boolean isTerminated() {
            this.lock.lock();
            try {
                boolean z = this.shutdown && this.runningTasks == 0;
                this.lock.unlock();
                return z;
            } catch (Throwable th) {
                this.lock.unlock();
            }
        }

        public void shutdown() {
            this.lock.lock();
            try {
                this.shutdown = true;
            } finally {
                this.lock.unlock();
            }
        }

        public List<Runnable> shutdownNow() {
            shutdown();
            return Collections.emptyList();
        }
    }

    private static class ScheduledListeningDecorator extends ListeningDecorator implements ListeningScheduledExecutorService {
        final ScheduledExecutorService delegate;

        ScheduledListeningDecorator(ScheduledExecutorService scheduledExecutorService) {
            super(scheduledExecutorService);
            this.delegate = (ScheduledExecutorService) Preconditions.checkNotNull(scheduledExecutorService);
        }

        public ScheduledFuture<?> schedule(Runnable runnable, long j, TimeUnit timeUnit) {
            return this.delegate.schedule(runnable, j, timeUnit);
        }

        public <V> ScheduledFuture<V> schedule(Callable<V> callable, long j, TimeUnit timeUnit) {
            return this.delegate.schedule(callable, j, timeUnit);
        }

        public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
            return this.delegate.scheduleAtFixedRate(runnable, j, j2, timeUnit);
        }

        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
            return this.delegate.scheduleWithFixedDelay(runnable, j, j2, timeUnit);
        }
    }

    private MoreExecutors() {
    }

    @Beta
    public static void addDelayedShutdownHook(final ExecutorService executorService, final long j, final TimeUnit timeUnit) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    executorService.shutdown();
                    executorService.awaitTermination(j, timeUnit);
                } catch (InterruptedException e) {
                }
            }
        }, "DelayedShutdownHook-for-" + executorService));
    }

    @Beta
    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor threadPoolExecutor) {
        return getExitingExecutorService(threadPoolExecutor, 120, TimeUnit.SECONDS);
    }

    @Beta
    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor threadPoolExecutor, long j, TimeUnit timeUnit) {
        threadPoolExecutor.setThreadFactory(new ThreadFactoryBuilder().setDaemon(true).setThreadFactory(threadPoolExecutor.getThreadFactory()).build());
        ExecutorService unconfigurableExecutorService = Executors.unconfigurableExecutorService(threadPoolExecutor);
        addDelayedShutdownHook(unconfigurableExecutorService, j, timeUnit);
        return unconfigurableExecutorService;
    }

    @Beta
    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        return getExitingScheduledExecutorService(scheduledThreadPoolExecutor, 120, TimeUnit.SECONDS);
    }

    @Beta
    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, long j, TimeUnit timeUnit) {
        scheduledThreadPoolExecutor.setThreadFactory(new ThreadFactoryBuilder().setDaemon(true).setThreadFactory(scheduledThreadPoolExecutor.getThreadFactory()).build());
        Object unconfigurableScheduledExecutorService = Executors.unconfigurableScheduledExecutorService(scheduledThreadPoolExecutor);
        addDelayedShutdownHook(unconfigurableScheduledExecutorService, j, timeUnit);
        return unconfigurableScheduledExecutorService;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static <T> T invokeAnyImpl(com.google.common.util.concurrent.ListeningExecutorService r15, java.util.Collection<? extends java.util.concurrent.Callable<T>> r16, boolean r17, long r18) throws java.lang.InterruptedException, java.util.concurrent.ExecutionException, java.util.concurrent.TimeoutException {
        /*
        r1 = r16.size();
        if (r1 <= 0) goto L_0x006a;
    L_0x0006:
        r0 = 1;
    L_0x0007:
        com.google.common.base.Preconditions.checkArgument(r0);
        r11 = com.google.common.collect.Lists.newArrayListWithCapacity(r1);
        r12 = com.google.common.collect.Queues.newLinkedBlockingQueue();
        if (r17 == 0) goto L_0x006c;
    L_0x0014:
        r6 = java.lang.System.nanoTime();	 Catch:{ all -> 0x007a }
    L_0x0018:
        r13 = r16.iterator();	 Catch:{ all -> 0x007a }
        r0 = r13.next();	 Catch:{ all -> 0x007a }
        r0 = (java.util.concurrent.Callable) r0;	 Catch:{ all -> 0x007a }
        r0 = submitAndAddQueueListener(r15, r0, r12);	 Catch:{ all -> 0x007a }
        r11.add(r0);	 Catch:{ all -> 0x007a }
        r1 = r1 + -1;
        r10 = 1;
        r2 = 0;
        r4 = r18;
    L_0x002f:
        r0 = r12.poll();	 Catch:{ all -> 0x00a3 }
        r0 = (java.util.concurrent.Future) r0;	 Catch:{ all -> 0x00a3 }
        if (r0 != 0) goto L_0x00cd;
    L_0x0037:
        if (r1 <= 0) goto L_0x006f;
    L_0x0039:
        r3 = r1 + -1;
        r1 = r13.next();	 Catch:{ all -> 0x00a3 }
        r1 = (java.util.concurrent.Callable) r1;	 Catch:{ all -> 0x00a3 }
        r1 = submitAndAddQueueListener(r15, r1, r12);	 Catch:{ all -> 0x00a3 }
        r11.add(r1);	 Catch:{ all -> 0x00a3 }
        r1 = r10 + 1;
        r14 = r0;
        r0 = r1;
        r1 = r14;
    L_0x004d:
        if (r1 == 0) goto L_0x00c8;
    L_0x004f:
        r0 = r0 + -1;
        r1 = r1.get();	 Catch:{ ExecutionException -> 0x00ca, RuntimeException -> 0x00bc }
        r2 = r11.iterator();
    L_0x0059:
        r0 = r2.hasNext();
        if (r0 == 0) goto L_0x00cc;
    L_0x005f:
        r0 = r2.next();
        r0 = (java.util.concurrent.Future) r0;
        r3 = 1;
        r0.cancel(r3);
        goto L_0x0059;
    L_0x006a:
        r0 = 0;
        goto L_0x0007;
    L_0x006c:
        r6 = 0;
        goto L_0x0018;
    L_0x006f:
        if (r10 != 0) goto L_0x0091;
    L_0x0071:
        if (r2 != 0) goto L_0x0079;
    L_0x0073:
        r2 = new java.util.concurrent.ExecutionException;	 Catch:{ all -> 0x00a3 }
        r0 = 0;
        r2.<init>(r0);	 Catch:{ all -> 0x00a3 }
    L_0x0079:
        throw r2;	 Catch:{ all -> 0x007a }
    L_0x007a:
        r0 = move-exception;
        r1 = r0;
    L_0x007c:
        r2 = r11.iterator();
    L_0x0080:
        r0 = r2.hasNext();
        if (r0 == 0) goto L_0x00c7;
    L_0x0086:
        r0 = r2.next();
        r0 = (java.util.concurrent.Future) r0;
        r3 = 1;
        r0.cancel(r3);
        goto L_0x0080;
    L_0x0091:
        if (r17 == 0) goto L_0x00b2;
    L_0x0093:
        r0 = java.util.concurrent.TimeUnit.NANOSECONDS;	 Catch:{ all -> 0x00a3 }
        r0 = r12.poll(r4, r0);	 Catch:{ all -> 0x00a3 }
        r0 = (java.util.concurrent.Future) r0;	 Catch:{ all -> 0x00a3 }
        if (r0 != 0) goto L_0x00a6;
    L_0x009d:
        r0 = new java.util.concurrent.TimeoutException;	 Catch:{ all -> 0x00a3 }
        r0.<init>();	 Catch:{ all -> 0x00a3 }
        throw r0;	 Catch:{ all -> 0x00a3 }
    L_0x00a3:
        r0 = move-exception;
        r1 = r0;
        goto L_0x007c;
    L_0x00a6:
        r8 = java.lang.System.nanoTime();	 Catch:{ all -> 0x00a3 }
        r6 = r8 - r6;
        r4 = r4 - r6;
        r6 = r8;
        r3 = r1;
        r1 = r0;
        r0 = r10;
        goto L_0x004d;
    L_0x00b2:
        r0 = r12.take();	 Catch:{ all -> 0x00a3 }
        r0 = (java.util.concurrent.Future) r0;	 Catch:{ all -> 0x00a3 }
        r3 = r1;
        r1 = r0;
        r0 = r10;
        goto L_0x004d;
    L_0x00bc:
        r2 = move-exception;
        r1 = new java.util.concurrent.ExecutionException;	 Catch:{ all -> 0x00a3 }
        r1.<init>(r2);	 Catch:{ all -> 0x00a3 }
    L_0x00c2:
        r10 = r0;
        r2 = r1;
        r1 = r3;
        goto L_0x002f;
    L_0x00c7:
        throw r1;
    L_0x00c8:
        r1 = r2;
        goto L_0x00c2;
    L_0x00ca:
        r1 = move-exception;
        goto L_0x00c2;
    L_0x00cc:
        return r1;
    L_0x00cd:
        r3 = r1;
        r1 = r0;
        r0 = r10;
        goto L_0x004d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.MoreExecutors.invokeAnyImpl(com.google.common.util.concurrent.ListeningExecutorService, java.util.Collection, boolean, long):T");
    }

    public static ListeningExecutorService listeningDecorator(ExecutorService executorService) {
        return executorService instanceof ListeningExecutorService ? (ListeningExecutorService) executorService : executorService instanceof ScheduledExecutorService ? new ScheduledListeningDecorator((ScheduledExecutorService) executorService) : new ListeningDecorator(executorService);
    }

    public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService scheduledExecutorService) {
        return scheduledExecutorService instanceof ListeningScheduledExecutorService ? (ListeningScheduledExecutorService) scheduledExecutorService : new ScheduledListeningDecorator(scheduledExecutorService);
    }

    public static ListeningExecutorService sameThreadExecutor() {
        return new SameThreadExecutorService();
    }

    private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService listeningExecutorService, Callable<T> callable, final BlockingQueue<Future<T>> blockingQueue) {
        final ListenableFuture<T> submit = listeningExecutorService.submit((Callable) callable);
        submit.addListener(new Runnable() {
            public void run() {
                blockingQueue.add(submit);
            }
        }, sameThreadExecutor());
        return submit;
    }
}
