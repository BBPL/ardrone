package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

@Beta
public abstract class AbstractScheduledService implements Service {
    private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
    private final AbstractService delegate = new C08461();

    class C08461 extends AbstractService {
        private volatile ScheduledExecutorService executorService;
        private final ReentrantLock lock = new ReentrantLock();
        private volatile Future<?> runningTask;
        private final Runnable task = new C08431();

        class C08431 implements Runnable {
            C08431() {
            }

            public void run() {
                C08461.this.lock.lock();
                try {
                    AbstractScheduledService.this.runOneIteration();
                    C08461.this.lock.unlock();
                } catch (Throwable th) {
                    C08461.this.lock.unlock();
                }
            }
        }

        class C08442 implements Runnable {
            C08442() {
            }

            public void run() {
                C08461.this.lock.lock();
                try {
                    AbstractScheduledService.this.startUp();
                    C08461.this.runningTask = AbstractScheduledService.this.scheduler().schedule(AbstractScheduledService.this.delegate, C08461.this.executorService, C08461.this.task);
                    C08461.this.notifyStarted();
                    C08461.this.lock.unlock();
                } catch (Throwable th) {
                    C08461.this.lock.unlock();
                }
            }
        }

        class C08453 implements Runnable {
            C08453() {
            }

            public void run() {
                try {
                    C08461.this.lock.lock();
                    if (C08461.this.state() != State.STOPPING) {
                        C08461.this.lock.unlock();
                        return;
                    }
                    AbstractScheduledService.this.shutDown();
                    C08461.this.lock.unlock();
                    C08461.this.notifyStopped();
                } catch (Throwable th) {
                    C08461.this.notifyFailed(th);
                    RuntimeException propagate = Throwables.propagate(th);
                }
            }
        }

        C08461() {
        }

        protected final void doStart() {
            this.executorService = AbstractScheduledService.this.executor();
            this.executorService.execute(new C08442());
        }

        protected final void doStop() {
            this.runningTask.cancel(false);
            this.executorService.execute(new C08453());
        }
    }

    public static abstract class Scheduler {
        private Scheduler() {
        }

        public static Scheduler newFixedDelaySchedule(long j, long j2, TimeUnit timeUnit) {
            final long j3 = j;
            final long j4 = j2;
            final TimeUnit timeUnit2 = timeUnit;
            return new Scheduler() {
                public Future<?> schedule(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable) {
                    return scheduledExecutorService.scheduleWithFixedDelay(runnable, j3, j4, timeUnit2);
                }
            };
        }

        public static Scheduler newFixedRateSchedule(long j, long j2, TimeUnit timeUnit) {
            final long j3 = j;
            final long j4 = j2;
            final TimeUnit timeUnit2 = timeUnit;
            return new Scheduler() {
                public Future<?> schedule(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable) {
                    return scheduledExecutorService.scheduleAtFixedRate(runnable, j3, j4, timeUnit2);
                }
            };
        }

        abstract Future<?> schedule(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable);
    }

    @Beta
    public static abstract class CustomScheduler extends Scheduler {

        private class ReschedulableCallable extends ForwardingFuture<Void> implements Callable<Void> {
            @GuardedBy("lock")
            private Future<Void> currentFuture;
            private final ScheduledExecutorService executor;
            private final ReentrantLock lock = new ReentrantLock();
            private final AbstractService service;
            private final Runnable wrappedRunnable;

            ReschedulableCallable(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable) {
                this.wrappedRunnable = runnable;
                this.executor = scheduledExecutorService;
                this.service = abstractService;
            }

            public Void call() throws Exception {
                this.wrappedRunnable.run();
                reschedule();
                return null;
            }

            public boolean cancel(boolean z) {
                this.lock.lock();
                try {
                    boolean cancel = this.currentFuture.cancel(z);
                    return cancel;
                } finally {
                    this.lock.unlock();
                }
            }

            protected Future<Void> delegate() {
                throw new UnsupportedOperationException("Only cancel is supported by this future");
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void reschedule() {
                /*
                r4 = this;
                r0 = r4.lock;
                r0.lock();
                r0 = r4.currentFuture;	 Catch:{ Throwable -> 0x002d }
                if (r0 == 0) goto L_0x0011;
            L_0x0009:
                r0 = r4.currentFuture;	 Catch:{ Throwable -> 0x002d }
                r0 = r0.isCancelled();	 Catch:{ Throwable -> 0x002d }
                if (r0 != 0) goto L_0x0027;
            L_0x0011:
                r0 = com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler.this;	 Catch:{ Throwable -> 0x002d }
                r0 = r0.getNextSchedule();	 Catch:{ Throwable -> 0x002d }
                r1 = r4.executor;	 Catch:{ Throwable -> 0x002d }
                r2 = r0.delay;	 Catch:{ Throwable -> 0x002d }
                r0 = r0.unit;	 Catch:{ Throwable -> 0x002d }
                r0 = r1.schedule(r4, r2, r0);	 Catch:{ Throwable -> 0x002d }
                r4.currentFuture = r0;	 Catch:{ Throwable -> 0x002d }
            L_0x0027:
                r0 = r4.lock;
                r0.unlock();
            L_0x002c:
                return;
            L_0x002d:
                r0 = move-exception;
                r1 = r4.service;	 Catch:{ all -> 0x0039 }
                r1.notifyFailed(r0);	 Catch:{ all -> 0x0039 }
                r0 = r4.lock;
                r0.unlock();
                goto L_0x002c;
            L_0x0039:
                r0 = move-exception;
                r1 = r4.lock;
                r1.unlock();
                throw r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractScheduledService.CustomScheduler.ReschedulableCallable.reschedule():void");
            }
        }

        @Beta
        protected static final class Schedule {
            private final long delay;
            private final TimeUnit unit;

            public Schedule(long j, TimeUnit timeUnit) {
                this.delay = j;
                this.unit = (TimeUnit) Preconditions.checkNotNull(timeUnit);
            }
        }

        public CustomScheduler() {
            super();
        }

        protected abstract Schedule getNextSchedule() throws Exception;

        final Future<?> schedule(AbstractService abstractService, ScheduledExecutorService scheduledExecutorService, Runnable runnable) {
            Future reschedulableCallable = new ReschedulableCallable(abstractService, scheduledExecutorService, runnable);
            reschedulableCallable.reschedule();
            return reschedulableCallable;
        }
    }

    public final void addListener(Listener listener, Executor executor) {
        this.delegate.addListener(listener, executor);
    }

    protected ScheduledExecutorService executor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    protected abstract void runOneIteration() throws Exception;

    protected abstract Scheduler scheduler();

    protected void shutDown() throws Exception {
    }

    public final ListenableFuture<State> start() {
        return this.delegate.start();
    }

    public final State startAndWait() {
        return this.delegate.startAndWait();
    }

    protected void startUp() throws Exception {
    }

    public final State state() {
        return this.delegate.state();
    }

    public final ListenableFuture<State> stop() {
        return this.delegate.stop();
    }

    public final State stopAndWait() {
        return this.delegate.stopAndWait();
    }

    public String toString() {
        return getClass().getSimpleName() + " [" + state() + "]";
    }
}
