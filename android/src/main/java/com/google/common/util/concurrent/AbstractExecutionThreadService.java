package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

@Beta
public abstract class AbstractExecutionThreadService implements Service {
    private static final Logger logger = Logger.getLogger(AbstractExecutionThreadService.class.getName());
    private final Service delegate = new C08371();

    class C08371 extends AbstractService {

        class C08361 implements Runnable {
            C08361() {
            }

            public void run() {
                try {
                    AbstractExecutionThreadService.this.startUp();
                    C08371.this.notifyStarted();
                    if (C08371.this.isRunning()) {
                        AbstractExecutionThreadService.this.run();
                    }
                    AbstractExecutionThreadService.this.shutDown();
                    C08371.this.notifyStopped();
                } catch (Throwable th) {
                    C08371.this.notifyFailed(th);
                    RuntimeException propagate = Throwables.propagate(th);
                }
            }
        }

        C08371() {
        }

        protected final void doStart() {
            AbstractExecutionThreadService.this.executor().execute(new C08361());
        }

        protected void doStop() {
            AbstractExecutionThreadService.this.triggerShutdown();
        }
    }

    class C08382 implements Executor {
        C08382() {
        }

        public void execute(Runnable runnable) {
            new Thread(runnable, AbstractExecutionThreadService.this.getServiceName()).start();
        }
    }

    protected AbstractExecutionThreadService() {
    }

    public final void addListener(Listener listener, Executor executor) {
        this.delegate.addListener(listener, executor);
    }

    protected Executor executor() {
        return new C08382();
    }

    protected String getServiceName() {
        return getClass().getSimpleName();
    }

    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    protected abstract void run() throws Exception;

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
        return getServiceName() + " [" + state() + "]";
    }

    protected void triggerShutdown() {
    }
}
