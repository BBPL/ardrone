package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;

@Beta
public abstract class AbstractService implements Service {
    private static final Logger logger = Logger.getLogger(AbstractService.class.getName());
    @GuardedBy("lock")
    private final List<ListenerExecutorPair> listeners = Lists.newArrayList();
    private final ReentrantLock lock = new ReentrantLock();
    @GuardedBy("queuedListeners")
    private final Queue<Runnable> queuedListeners = Queues.newConcurrentLinkedQueue();
    private final Transition shutdown = new Transition();
    @GuardedBy("lock")
    private volatile StateSnapshot snapshot = new StateSnapshot(State.NEW);
    private final Transition startup = new Transition();

    class C08491 implements Listener {
        C08491() {
        }

        public void failed(State state, Throwable th) {
            switch (state) {
                case STARTING:
                    AbstractService.this.startup.setException(th);
                    AbstractService.this.shutdown.setException(new Exception("Service failed to start.", th));
                    return;
                case RUNNING:
                    AbstractService.this.shutdown.setException(new Exception("Service failed while running", th));
                    return;
                case STOPPING:
                    AbstractService.this.shutdown.setException(th);
                    return;
                default:
                    throw new AssertionError("Unexpected from state: " + state);
            }
        }

        public void running() {
            AbstractService.this.startup.set(State.RUNNING);
        }

        public void starting() {
        }

        public void stopping(State state) {
            if (state == State.STARTING) {
                AbstractService.this.startup.set(State.STOPPING);
            }
        }

        public void terminated(State state) {
            if (state == State.NEW) {
                AbstractService.this.startup.set(State.TERMINATED);
            }
            AbstractService.this.shutdown.set(State.TERMINATED);
        }
    }

    private static class ListenerExecutorPair {
        final Executor executor;
        final Listener listener;

        ListenerExecutorPair(Listener listener, Executor executor) {
            this.listener = listener;
            this.executor = executor;
        }

        void execute(Runnable runnable) {
            try {
                this.executor.execute(runnable);
            } catch (Throwable e) {
                AbstractService.logger.log(Level.SEVERE, "Exception while executing listener " + this.listener + " with executor " + this.executor, e);
            }
        }
    }

    @Immutable
    private static final class StateSnapshot {
        @Nullable
        final Throwable failure;
        final boolean shutdownWhenStartupFinishes;
        final State state;

        StateSnapshot(State state) {
            this(state, false, null);
        }

        StateSnapshot(State state, boolean z, Throwable th) {
            boolean z2 = !z || state == State.STARTING;
            Preconditions.checkArgument(z2, "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", state);
            Preconditions.checkArgument(((th != null ? 1 : 0) ^ (state == State.FAILED ? 1 : 0)) == 0, "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", state, th);
            this.state = state;
            this.shutdownWhenStartupFinishes = z;
            this.failure = th;
        }

        State externalState() {
            return (this.shutdownWhenStartupFinishes && this.state == State.STARTING) ? State.STOPPING : this.state;
        }

        Throwable failureCause() {
            Preconditions.checkState(this.state == State.FAILED, "failureCause() is only valid if the service has failed, service is %s", this.state);
            return this.failure;
        }
    }

    private class Transition extends AbstractFuture<State> {
        private Transition() {
        }

        public State get(long j, TimeUnit timeUnit) throws InterruptedException, TimeoutException, ExecutionException {
            try {
                return (State) super.get(j, timeUnit);
            } catch (TimeoutException e) {
                throw new TimeoutException(AbstractService.this.toString());
            }
        }
    }

    protected AbstractService() {
        addListener(new C08491(), MoreExecutors.sameThreadExecutor());
    }

    private void executeListeners() {
        if (!this.lock.isHeldByCurrentThread()) {
            synchronized (this.queuedListeners) {
                while (true) {
                    Runnable runnable = (Runnable) this.queuedListeners.poll();
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }
        }
    }

    @GuardedBy("lock")
    private void failed(final State state, final Throwable th) {
        for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
            this.queuedListeners.add(new Runnable() {

                class C08581 implements Runnable {
                    C08581() {
                    }

                    public void run() {
                        listenerExecutorPair.listener.failed(state, th);
                    }
                }

                public void run() {
                    listenerExecutorPair.execute(new C08581());
                }
            });
        }
        this.listeners.clear();
    }

    @GuardedBy("lock")
    private void running() {
        for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
            this.queuedListeners.add(new Runnable() {

                class C08521 implements Runnable {
                    C08521() {
                    }

                    public void run() {
                        listenerExecutorPair.listener.running();
                    }
                }

                public void run() {
                    listenerExecutorPair.execute(new C08521());
                }
            });
        }
    }

    @GuardedBy("lock")
    private void starting() {
        for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
            this.queuedListeners.add(new Runnable() {

                class C08501 implements Runnable {
                    C08501() {
                    }

                    public void run() {
                        listenerExecutorPair.listener.starting();
                    }
                }

                public void run() {
                    listenerExecutorPair.execute(new C08501());
                }
            });
        }
    }

    @GuardedBy("lock")
    private void stopping(final State state) {
        for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
            this.queuedListeners.add(new Runnable() {

                class C08541 implements Runnable {
                    C08541() {
                    }

                    public void run() {
                        listenerExecutorPair.listener.stopping(state);
                    }
                }

                public void run() {
                    listenerExecutorPair.execute(new C08541());
                }
            });
        }
    }

    @GuardedBy("lock")
    private void terminated(final State state) {
        for (final ListenerExecutorPair listenerExecutorPair : this.listeners) {
            this.queuedListeners.add(new Runnable() {

                class C08561 implements Runnable {
                    C08561() {
                    }

                    public void run() {
                        listenerExecutorPair.listener.terminated(state);
                    }
                }

                public void run() {
                    listenerExecutorPair.execute(new C08561());
                }
            });
        }
        this.listeners.clear();
    }

    public final void addListener(Listener listener, Executor executor) {
        Preconditions.checkNotNull(listener, "listener");
        Preconditions.checkNotNull(executor, "executor");
        this.lock.lock();
        try {
            if (!(this.snapshot.state == State.TERMINATED || this.snapshot.state == State.FAILED)) {
                this.listeners.add(new ListenerExecutorPair(listener, executor));
            }
            this.lock.unlock();
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    protected abstract void doStart();

    protected abstract void doStop();

    public final boolean isRunning() {
        return state() == State.RUNNING;
    }

    protected final void notifyFailed(Throwable th) {
        Preconditions.checkNotNull(th);
        this.lock.lock();
        try {
            switch (this.snapshot.state) {
                case STARTING:
                case RUNNING:
                case STOPPING:
                    State state = this.snapshot.state;
                    this.snapshot = new StateSnapshot(State.FAILED, false, th);
                    failed(state, th);
                    break;
                case TERMINATED:
                case NEW:
                    throw new IllegalStateException("Failed while in state:" + this.snapshot.state, th);
                case FAILED:
                    break;
                default:
                    throw new AssertionError("Unexpected state: " + this.snapshot.state);
            }
            this.lock.unlock();
            executeListeners();
        } catch (Throwable th2) {
            this.lock.unlock();
            executeListeners();
        }
    }

    protected final void notifyStarted() {
        this.lock.lock();
        try {
            if (this.snapshot.state != State.STARTING) {
                Throwable illegalStateException = new IllegalStateException("Cannot notifyStarted() when the service is " + this.snapshot.state);
                notifyFailed(illegalStateException);
                throw illegalStateException;
            }
            if (this.snapshot.shutdownWhenStartupFinishes) {
                this.snapshot = new StateSnapshot(State.STOPPING);
                doStop();
            } else {
                this.snapshot = new StateSnapshot(State.RUNNING);
                running();
            }
            this.lock.unlock();
            executeListeners();
        } catch (Throwable th) {
            this.lock.unlock();
            executeListeners();
        }
    }

    protected final void notifyStopped() {
        this.lock.lock();
        try {
            if (this.snapshot.state == State.STOPPING || this.snapshot.state == State.RUNNING) {
                State state = this.snapshot.state;
                this.snapshot = new StateSnapshot(State.TERMINATED);
                terminated(state);
                return;
            }
            Throwable illegalStateException = new IllegalStateException("Cannot notifyStopped() when the service is " + this.snapshot.state);
            notifyFailed(illegalStateException);
            throw illegalStateException;
        } finally {
            this.lock.unlock();
            executeListeners();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.common.util.concurrent.ListenableFuture<com.google.common.util.concurrent.Service.State> start() {
        /*
        r2 = this;
        r0 = r2.lock;
        r0.lock();
        r0 = r2.snapshot;	 Catch:{ Throwable -> 0x0027 }
        r0 = r0.state;	 Catch:{ Throwable -> 0x0027 }
        r1 = com.google.common.util.concurrent.Service.State.NEW;	 Catch:{ Throwable -> 0x0027 }
        if (r0 != r1) goto L_0x001c;
    L_0x000d:
        r0 = new com.google.common.util.concurrent.AbstractService$StateSnapshot;	 Catch:{ Throwable -> 0x0027 }
        r1 = com.google.common.util.concurrent.Service.State.STARTING;	 Catch:{ Throwable -> 0x0027 }
        r0.<init>(r1);	 Catch:{ Throwable -> 0x0027 }
        r2.snapshot = r0;	 Catch:{ Throwable -> 0x0027 }
        r2.starting();	 Catch:{ Throwable -> 0x0027 }
        r2.doStart();	 Catch:{ Throwable -> 0x0027 }
    L_0x001c:
        r0 = r2.lock;
        r0.unlock();
        r2.executeListeners();
    L_0x0024:
        r0 = r2.startup;
        return r0;
    L_0x0027:
        r0 = move-exception;
        r2.notifyFailed(r0);	 Catch:{ all -> 0x0034 }
        r0 = r2.lock;
        r0.unlock();
        r2.executeListeners();
        goto L_0x0024;
    L_0x0034:
        r0 = move-exception;
        r1 = r2.lock;
        r1.unlock();
        r2.executeListeners();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractService.start():com.google.common.util.concurrent.ListenableFuture<com.google.common.util.concurrent.Service$State>");
    }

    public State startAndWait() {
        return (State) Futures.getUnchecked(start());
    }

    public final State state() {
        return this.snapshot.externalState();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.common.util.concurrent.ListenableFuture<com.google.common.util.concurrent.Service.State> stop() {
        /*
        r4 = this;
        r0 = r4.lock;
        r0.lock();
        r0 = com.google.common.util.concurrent.AbstractService.C08607.$SwitchMap$com$google$common$util$concurrent$Service$State;	 Catch:{ Throwable -> 0x0031 }
        r1 = r4.snapshot;	 Catch:{ Throwable -> 0x0031 }
        r1 = r1.state;	 Catch:{ Throwable -> 0x0031 }
        r1 = r1.ordinal();	 Catch:{ Throwable -> 0x0031 }
        r0 = r0[r1];	 Catch:{ Throwable -> 0x0031 }
        switch(r0) {
            case 1: goto L_0x0057;
            case 2: goto L_0x0072;
            case 3: goto L_0x004e;
            case 4: goto L_0x004e;
            case 5: goto L_0x004e;
            case 6: goto L_0x0040;
            default: goto L_0x0014;
        };	 Catch:{ Throwable -> 0x0031 }
    L_0x0014:
        r0 = new java.lang.AssertionError;	 Catch:{ Throwable -> 0x0031 }
        r1 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0031 }
        r1.<init>();	 Catch:{ Throwable -> 0x0031 }
        r2 = "Unexpected state: ";
        r1 = r1.append(r2);	 Catch:{ Throwable -> 0x0031 }
        r2 = r4.snapshot;	 Catch:{ Throwable -> 0x0031 }
        r2 = r2.state;	 Catch:{ Throwable -> 0x0031 }
        r1 = r1.append(r2);	 Catch:{ Throwable -> 0x0031 }
        r1 = r1.toString();	 Catch:{ Throwable -> 0x0031 }
        r0.<init>(r1);	 Catch:{ Throwable -> 0x0031 }
        throw r0;	 Catch:{ Throwable -> 0x0031 }
    L_0x0031:
        r0 = move-exception;
        r4.notifyFailed(r0);	 Catch:{ all -> 0x0068 }
        r0 = r4.lock;
        r0.unlock();
        r4.executeListeners();
    L_0x003d:
        r0 = r4.shutdown;
        return r0;
    L_0x0040:
        r0 = new com.google.common.util.concurrent.AbstractService$StateSnapshot;	 Catch:{ Throwable -> 0x0031 }
        r1 = com.google.common.util.concurrent.Service.State.TERMINATED;	 Catch:{ Throwable -> 0x0031 }
        r0.<init>(r1);	 Catch:{ Throwable -> 0x0031 }
        r4.snapshot = r0;	 Catch:{ Throwable -> 0x0031 }
        r0 = com.google.common.util.concurrent.Service.State.NEW;	 Catch:{ Throwable -> 0x0031 }
        r4.terminated(r0);	 Catch:{ Throwable -> 0x0031 }
    L_0x004e:
        r0 = r4.lock;
        r0.unlock();
        r4.executeListeners();
        goto L_0x003d;
    L_0x0057:
        r0 = new com.google.common.util.concurrent.AbstractService$StateSnapshot;	 Catch:{ Throwable -> 0x0031 }
        r1 = com.google.common.util.concurrent.Service.State.STARTING;	 Catch:{ Throwable -> 0x0031 }
        r2 = 1;
        r3 = 0;
        r0.<init>(r1, r2, r3);	 Catch:{ Throwable -> 0x0031 }
        r4.snapshot = r0;	 Catch:{ Throwable -> 0x0031 }
        r0 = com.google.common.util.concurrent.Service.State.STARTING;	 Catch:{ Throwable -> 0x0031 }
        r4.stopping(r0);	 Catch:{ Throwable -> 0x0031 }
        goto L_0x004e;
    L_0x0068:
        r0 = move-exception;
        r1 = r4.lock;
        r1.unlock();
        r4.executeListeners();
        throw r0;
    L_0x0072:
        r0 = new com.google.common.util.concurrent.AbstractService$StateSnapshot;	 Catch:{ Throwable -> 0x0031 }
        r1 = com.google.common.util.concurrent.Service.State.STOPPING;	 Catch:{ Throwable -> 0x0031 }
        r0.<init>(r1);	 Catch:{ Throwable -> 0x0031 }
        r4.snapshot = r0;	 Catch:{ Throwable -> 0x0031 }
        r0 = com.google.common.util.concurrent.Service.State.RUNNING;	 Catch:{ Throwable -> 0x0031 }
        r4.stopping(r0);	 Catch:{ Throwable -> 0x0031 }
        r4.doStop();	 Catch:{ Throwable -> 0x0031 }
        goto L_0x004e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractService.stop():com.google.common.util.concurrent.ListenableFuture<com.google.common.util.concurrent.Service$State>");
    }

    public State stopAndWait() {
        return (State) Futures.getUnchecked(stop());
    }

    public String toString() {
        return getClass().getSimpleName() + " [" + state() + "]";
    }
}
