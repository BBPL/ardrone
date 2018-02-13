package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FinalizableReferenceQueue {
    private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
    private static final Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
    private static final Method startFinalizer = getStartFinalizer(loadFinalizer(new SystemLoader(), new DecoupledLoader(), new DirectLoader()));
    final ReferenceQueue<Object> queue;
    final boolean threadStarted;

    interface FinalizerLoader {
        Class<?> loadFinalizer();
    }

    static class DecoupledLoader implements FinalizerLoader {
        private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader.Loading Finalizer in the current class loader instead. As a result, you will not be ableto garbage collect this class loader. To support reclaiming this class loader, eitherresolve the underlying issue, or move Google Collections to your system class path.";

        DecoupledLoader() {
        }

        URL getBaseUrl() throws IOException {
            String str = FinalizableReferenceQueue.FINALIZER_CLASS_NAME.replace('.', '/') + ".class";
            URL resource = getClass().getClassLoader().getResource(str);
            if (resource == null) {
                throw new FileNotFoundException(str);
            }
            String url = resource.toString();
            if (url.endsWith(str)) {
                return new URL(resource, url.substring(0, url.length() - str.length()));
            }
            throw new IOException("Unsupported path style: " + url);
        }

        public Class<?> loadFinalizer() {
            try {
                return newLoader(getBaseUrl()).loadClass(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
            } catch (Throwable e) {
                FinalizableReferenceQueue.logger.log(Level.WARNING, LOADING_ERROR, e);
                return null;
            }
        }

        URLClassLoader newLoader(URL url) {
            return new URLClassLoader(new URL[]{url}, null);
        }
    }

    static class DirectLoader implements FinalizerLoader {
        DirectLoader() {
        }

        public Class<?> loadFinalizer() {
            try {
                return Class.forName(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
            } catch (ClassNotFoundException e) {
                throw new AssertionError(e);
            }
        }
    }

    static class SystemLoader implements FinalizerLoader {
        @VisibleForTesting
        static boolean disabled;

        SystemLoader() {
        }

        public Class<?> loadFinalizer() {
            Class<?> cls = null;
            if (!disabled) {
                try {
                    ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                    if (systemClassLoader != null) {
                        try {
                            cls = systemClassLoader.loadClass(FinalizableReferenceQueue.FINALIZER_CLASS_NAME);
                        } catch (ClassNotFoundException e) {
                        }
                    }
                } catch (SecurityException e2) {
                    FinalizableReferenceQueue.logger.info("Not allowed to access system class loader.");
                }
            }
            return cls;
        }
    }

    public FinalizableReferenceQueue() {
        ReferenceQueue referenceQueue;
        boolean z = true;
        try {
            referenceQueue = (ReferenceQueue) startFinalizer.invoke(null, new Object[]{FinalizableReference.class, this});
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (Throwable th) {
            logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", th);
            referenceQueue = new ReferenceQueue();
            z = false;
        }
        this.queue = referenceQueue;
        this.threadStarted = z;
    }

    static Method getStartFinalizer(Class<?> cls) {
        try {
            return cls.getMethod("startFinalizer", new Class[]{Class.class, Object.class});
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    private static Class<?> loadFinalizer(FinalizerLoader... finalizerLoaderArr) {
        for (FinalizerLoader loadFinalizer : finalizerLoaderArr) {
            Class<?> loadFinalizer2 = loadFinalizer.loadFinalizer();
            if (loadFinalizer2 != null) {
                return loadFinalizer2;
            }
        }
        throw new AssertionError();
    }

    void cleanUp() {
        if (!this.threadStarted) {
            while (true) {
                Reference poll = this.queue.poll();
                if (poll != null) {
                    poll.clear();
                    try {
                        ((FinalizableReference) poll).finalizeReferent();
                    } catch (Throwable th) {
                        logger.log(Level.SEVERE, "Error cleaning up after reference.", th);
                    }
                } else {
                    return;
                }
            }
        }
    }
}
