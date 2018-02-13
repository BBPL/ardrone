package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class EventHandler {
    private final Method method;
    private final Object target;

    EventHandler(Object obj, Method method) {
        Preconditions.checkNotNull(obj, "EventHandler target cannot be null.");
        Preconditions.checkNotNull(method, "EventHandler method cannot be null.");
        this.target = obj;
        this.method = method;
        method.setAccessible(true);
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            EventHandler eventHandler = (EventHandler) obj;
            if (!this.method.equals(eventHandler.method)) {
                return false;
            }
            if (this.target != eventHandler.target) {
                return false;
            }
        }
        return true;
    }

    public void handleEvent(Object obj) throws InvocationTargetException {
        try {
            this.method.invoke(this.target, new Object[]{obj});
        } catch (Throwable e) {
            throw new Error("Method rejected target/argument: " + obj, e);
        } catch (Throwable e2) {
            throw new Error("Method became inaccessible: " + obj, e2);
        } catch (InvocationTargetException e3) {
            if (e3.getCause() instanceof Error) {
                throw ((Error) e3.getCause());
            }
            throw e3;
        }
    }

    public int hashCode() {
        return ((this.method.hashCode() + 31) * 31) + this.target.hashCode();
    }

    public String toString() {
        return "[wrapper " + this.method + "]";
    }
}
