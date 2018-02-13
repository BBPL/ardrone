package com.google.common.base.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Finalizer implements Runnable {
    private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
    private static final Field inheritableThreadLocals = getInheritableThreadLocalsField();
    private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
    private final WeakReference<Class<?>> finalizableReferenceClassReference;
    private final PhantomReference<Object> frqReference;
    private final ReferenceQueue<Object> queue = new ReferenceQueue();

    private static class ShutDown extends Exception {
        private ShutDown() {
        }
    }

    private Finalizer(Class<?> cls, Object obj) {
        this.finalizableReferenceClassReference = new WeakReference(cls);
        this.frqReference = new PhantomReference(obj, this.queue);
    }

    private void cleanUp(Reference<?> reference) throws ShutDown {
        Method finalizeReferentMethod = getFinalizeReferentMethod();
        Reference poll;
        do {
            poll.clear();
            if (poll == this.frqReference) {
                throw new ShutDown();
            }
            try {
                finalizeReferentMethod.invoke(poll, new Object[0]);
            } catch (Throwable th) {
                logger.log(Level.SEVERE, "Error cleaning up after reference.", th);
            }
            poll = this.queue.poll();
        } while (poll != null);
    }

    private Method getFinalizeReferentMethod() throws ShutDown {
        Class cls = (Class) this.finalizableReferenceClassReference.get();
        if (cls == null) {
            throw new ShutDown();
        }
        try {
            return cls.getMethod("finalizeReferent", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    public static Field getInheritableThreadLocalsField() {
        try {
            Field declaredField = Thread.class.getDeclaredField("inheritableThreadLocals");
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Throwable th) {
            logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
            return null;
        }
    }

    public static ReferenceQueue<Object> startFinalizer(Class<?> cls, Object obj) {
        if (cls.getName().equals(FINALIZABLE_REFERENCE)) {
            Object finalizer = new Finalizer(cls, obj);
            Thread thread = new Thread(finalizer);
            thread.setName(Finalizer.class.getName());
            thread.setDaemon(true);
            try {
                if (inheritableThreadLocals != null) {
                    inheritableThreadLocals.set(thread, null);
                }
            } catch (Throwable th) {
                logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", th);
            }
            thread.start();
            return finalizer.queue;
        }
        throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
    }

    public void run() {
        while (true) {
            try {
                cleanUp(this.queue.remove());
            } catch (InterruptedException e) {
            } catch (ShutDown e2) {
                return;
            }
        }
    }
}
