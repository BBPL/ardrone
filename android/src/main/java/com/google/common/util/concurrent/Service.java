package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.Executor;

@Beta
public interface Service {

    @Beta
    public interface Listener {
        void failed(State state, Throwable th);

        void running();

        void starting();

        void stopping(State state);

        void terminated(State state);
    }

    @Beta
    public enum State {
        NEW,
        STARTING,
        RUNNING,
        STOPPING,
        TERMINATED,
        FAILED
    }

    void addListener(Listener listener, Executor executor);

    boolean isRunning();

    ListenableFuture<State> start();

    State startAndWait();

    State state();

    ListenableFuture<State> stop();

    State stopAndWait();
}
