package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingObject;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import java.util.concurrent.Executor;

@Beta
public abstract class ForwardingService extends ForwardingObject implements Service {
    protected ForwardingService() {
    }

    public void addListener(Listener listener, Executor executor) {
        delegate().addListener(listener, executor);
    }

    protected abstract Service delegate();

    public boolean isRunning() {
        return delegate().isRunning();
    }

    protected State standardStartAndWait() {
        return (State) Futures.getUnchecked(start());
    }

    protected State standardStopAndWait() {
        return (State) Futures.getUnchecked(stop());
    }

    public ListenableFuture<State> start() {
        return delegate().start();
    }

    public State startAndWait() {
        return delegate().startAndWait();
    }

    public State state() {
        return delegate().state();
    }

    public ListenableFuture<State> stop() {
        return delegate().stop();
    }

    public State stopAndWait() {
        return delegate().stopAndWait();
    }
}
