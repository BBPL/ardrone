package org.mortbay.util.ajax;

import org.mortbay.jetty.HttpVersions;
import org.mortbay.log.Log;

public class WaitingContinuation implements Continuation {
    Object _mutex;
    boolean _new;
    Object _object;
    boolean _pending;
    boolean _resumed;

    public WaitingContinuation() {
        this._new = true;
        this._resumed = false;
        this._pending = false;
        this._mutex = this;
    }

    public WaitingContinuation(Object obj) {
        this._new = true;
        this._resumed = false;
        this._pending = false;
        if (obj == null) {
            obj = this;
        }
        this._mutex = obj;
    }

    public Object getMutex() {
        return this._mutex;
    }

    public Object getObject() {
        return this._object;
    }

    public boolean isNew() {
        return this._new;
    }

    public boolean isPending() {
        boolean z;
        synchronized (this._mutex) {
            z = this._pending;
        }
        return z;
    }

    public boolean isResumed() {
        boolean z;
        synchronized (this._mutex) {
            z = this._resumed;
        }
        return z;
    }

    public void reset() {
        synchronized (this._mutex) {
            this._resumed = false;
            this._pending = false;
            this._mutex.notify();
        }
    }

    public void resume() {
        synchronized (this._mutex) {
            this._resumed = true;
            this._mutex.notify();
        }
    }

    public void setMutex(Object obj) {
        if (!this._pending || obj == this._mutex) {
            if (obj == null) {
                obj = this;
            }
            this._mutex = obj;
            return;
        }
        throw new IllegalStateException();
    }

    public void setObject(Object obj) {
        this._object = obj;
    }

    public boolean suspend(long j) {
        boolean z;
        synchronized (this._mutex) {
            this._new = false;
            this._pending = true;
            try {
                if (!this._resumed && j >= 0) {
                    if (j == 0) {
                        this._mutex.wait();
                    } else if (j > 0) {
                        this._mutex.wait(j);
                    }
                }
                z = this._resumed;
                this._resumed = false;
                this._pending = false;
            } catch (Throwable e) {
                Log.ignore(e);
                z = this._resumed;
                this._resumed = false;
                this._pending = false;
            } catch (Throwable th) {
                boolean z2 = this._resumed;
                this._resumed = false;
                this._pending = false;
            }
        }
        return z;
    }

    public String toString() {
        String stringBuffer;
        synchronized (this) {
            stringBuffer = new StringBuffer().append("WaitingContinuation@").append(hashCode()).append(this._new ? ",new" : HttpVersions.HTTP_0_9).append(this._pending ? ",pending" : HttpVersions.HTTP_0_9).append(this._resumed ? ",resumed" : HttpVersions.HTTP_0_9).toString();
        }
        return stringBuffer;
    }
}
