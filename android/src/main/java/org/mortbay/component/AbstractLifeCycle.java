package org.mortbay.component;

import org.mortbay.component.LifeCycle.Listener;
import org.mortbay.log.Log;
import org.mortbay.util.LazyList;

public abstract class AbstractLifeCycle implements LifeCycle {
    static Class class$org$mortbay$component$LifeCycle$Listener;
    private final int FAILED = -1;
    private final int STARTED = 2;
    private final int STARTING = 1;
    private final int STOPPED = 0;
    private final int STOPPING = 3;
    protected Listener[] _listeners;
    private Object _lock = new Object();
    private volatile int _state = 0;

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    private void setFailed(Throwable th) {
        Log.warn(new StringBuffer().append("failed ").append(this).append(": ").append(th).toString());
        Log.debug(th);
        this._state = -1;
        if (this._listeners != null) {
            for (Listener lifeCycleFailure : this._listeners) {
                lifeCycleFailure.lifeCycleFailure(this, th);
            }
        }
    }

    private void setStarted() {
        this._state = 2;
        if (this._listeners != null) {
            for (Listener lifeCycleStarted : this._listeners) {
                lifeCycleStarted.lifeCycleStarted(this);
            }
        }
    }

    private void setStarting() {
        this._state = 1;
        if (this._listeners != null) {
            for (Listener lifeCycleStarting : this._listeners) {
                lifeCycleStarting.lifeCycleStarting(this);
            }
        }
    }

    private void setStopped() {
        int i = 0;
        this._state = 0;
        if (this._listeners != null) {
            while (i < this._listeners.length) {
                this._listeners[i].lifeCycleStopped(this);
                i++;
            }
        }
    }

    private void setStopping() {
        this._state = 3;
        if (this._listeners != null) {
            for (Listener lifeCycleStopping : this._listeners) {
                lifeCycleStopping.lifeCycleStopping(this);
            }
        }
    }

    public void addLifeCycleListener(Listener listener) {
        Class class$;
        Listener[] listenerArr = this._listeners;
        if (class$org$mortbay$component$LifeCycle$Listener == null) {
            class$ = class$("org.mortbay.component.LifeCycle$Listener");
            class$org$mortbay$component$LifeCycle$Listener = class$;
        } else {
            class$ = class$org$mortbay$component$LifeCycle$Listener;
        }
        this._listeners = (Listener[]) LazyList.addToArray(listenerArr, listener, class$);
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }

    public boolean isFailed() {
        return this._state == -1;
    }

    public boolean isRunning() {
        return this._state == 2 || this._state == 1;
    }

    public boolean isStarted() {
        return this._state == 2;
    }

    public boolean isStarting() {
        return this._state == 1;
    }

    public boolean isStopped() {
        return this._state == 0;
    }

    public boolean isStopping() {
        return this._state == 3;
    }

    public void removeLifeCycleListener(Listener listener) {
        this._listeners = (Listener[]) LazyList.removeFromArray(this._listeners, listener);
    }

    public final void start() throws Exception {
        synchronized (this._lock) {
            try {
                if (this._state == 2 || this._state == 1) {
                    return;
                }
                setStarting();
                doStart();
                Log.debug("started {}", this);
                setStarted();
            } catch (Throwable e) {
                setFailed(e);
                throw e;
            } catch (Throwable e2) {
                setFailed(e2);
                throw e2;
            }
        }
    }

    public final void stop() throws Exception {
        synchronized (this._lock) {
            try {
                if (this._state == 3 || this._state == 0) {
                    return;
                }
                setStopping();
                doStop();
                Log.debug("stopped {}", this);
                setStopped();
            } catch (Throwable e) {
                setFailed(e);
                throw e;
            } catch (Throwable e2) {
                setFailed(e2);
                throw e2;
            }
        }
    }
}
