package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Executor;

@Beta
public abstract class AbstractIdleService implements Service {
    private final Service delegate = new C08411();

    class C08411 extends AbstractService {

        class C08391 implements Runnable {
            C08391() {
            }

            public void run() {
                try {
                    AbstractIdleService.this.startUp();
                    C08411.this.notifyStarted();
                } catch (Throwable th) {
                    C08411.this.notifyFailed(th);
                    RuntimeException propagate = Throwables.propagate(th);
                }
            }
        }

        class C08402 implements Runnable {
            C08402() {
            }

            public void run() {
                try {
                    AbstractIdleService.this.shutDown();
                    C08411.this.notifyStopped();
                } catch (Throwable th) {
                    C08411.this.notifyFailed(th);
                    RuntimeException propagate = Throwables.propagate(th);
                }
            }
        }

        C08411() {
        }

        protected final void doStart() {
            AbstractIdleService.this.executor(State.STARTING).execute(new C08391());
        }

        protected final void doStop() {
            AbstractIdleService.this.executor(State.STOPPING).execute(new C08402());
        }
    }

    private String getServiceName() {
        return getClass().getSimpleName();
    }

    public final void addListener(Listener listener, Executor executor) {
        this.delegate.addListener(listener, executor);
    }

    protected Executor executor(final State state) {
        return new Executor() {
            public void execute(Runnable runnable) {
                new Thread(runnable, AbstractIdleService.this.getServiceName() + " " + state).start();
            }
        };
    }

    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    protected abstract void shutDown() throws Exception;

    public final ListenableFuture<State> start() {
        return this.delegate.start();
    }

    public final State startAndWait() {
        return this.delegate.startAndWait();
    }

    protected abstract void startUp() throws Exception;

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
}
