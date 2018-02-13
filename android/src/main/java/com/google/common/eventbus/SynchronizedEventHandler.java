package com.google.common.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class SynchronizedEventHandler extends EventHandler {
    public SynchronizedEventHandler(Object obj, Method method) {
        super(obj, method);
    }

    public void handleEvent(Object obj) throws InvocationTargetException {
        synchronized (this) {
            super.handleEvent(obj);
        }
    }
}
