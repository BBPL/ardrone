package org.mortbay.log;

import java.security.AccessController;
import java.security.PrivilegedAction;
import org.apache.sanselan.SanselanConstants;
import org.mortbay.util.Loader;

public class Log {
    public static final String EXCEPTION = "EXCEPTION ";
    public static final String IGNORED = "IGNORED";
    public static final String IGNORED_FMT = "IGNORED: {}";
    public static final String NOT_IMPLEMENTED = "NOT IMPLEMENTED ";
    public static boolean __ignored;
    private static Logger __log;
    public static String __logClass;
    private static final String[] __nestedEx = new String[]{"getTargetException", "getTargetError", "getException", "getRootCause"};
    private static final Class[] __noArgs = new Class[0];
    public static boolean __verbose;
    static Class class$org$mortbay$log$Log;
    static Class class$org$mortbay$log$StdErrLog;

    final class C13451 implements PrivilegedAction {
        C13451() {
        }

        public Object run() {
            boolean z = false;
            Log.__logClass = System.getProperty("org.mortbay.log.class", "org.mortbay.log.Slf4jLog");
            Log.__verbose = System.getProperty(SanselanConstants.PARAM_KEY_VERBOSE, null) != null;
            if (System.getProperty(Log.IGNORED, null) != null) {
                z = true;
            }
            Log.__ignored = z;
            return new Boolean(true);
        }
    }

    static {
        Class class$;
        AccessController.doPrivileged(new C13451());
        try {
            if (class$org$mortbay$log$Log == null) {
                class$ = class$("org.mortbay.log.Log");
                class$org$mortbay$log$Log = class$;
            } else {
                class$ = class$org$mortbay$log$Log;
            }
            Class loadClass = Loader.loadClass(class$, __logClass);
            __log = (Logger) loadClass.newInstance();
            class$ = loadClass;
        } catch (Throwable th) {
            Throwable th2 = th;
            if (class$org$mortbay$log$StdErrLog == null) {
                class$ = class$("org.mortbay.log.StdErrLog");
                class$org$mortbay$log$StdErrLog = class$;
            } else {
                class$ = class$org$mortbay$log$StdErrLog;
            }
            __log = new StdErrLog();
            __logClass = class$.getName();
            if (__verbose) {
                th2.printStackTrace();
            }
        }
        __log.info("Logging to {} via {}", __log, class$.getName());
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public static void debug(String str) {
        if (__log != null) {
            __log.debug(str, null, null);
        }
    }

    public static void debug(String str, Object obj) {
        if (__log != null) {
            __log.debug(str, obj, null);
        }
    }

    public static void debug(String str, Object obj, Object obj2) {
        if (__log != null) {
            __log.debug(str, obj, obj2);
        }
    }

    public static void debug(Throwable th) {
        if (__log != null && isDebugEnabled()) {
            __log.debug(EXCEPTION, th);
            unwind(th);
        }
    }

    public static Logger getLog() {
        return __log;
    }

    public static Logger getLogger(String str) {
        return __log == null ? __log : str == null ? __log : __log.getLogger(str);
    }

    public static void ignore(Throwable th) {
        if (__log != null) {
            if (__ignored) {
                __log.warn(IGNORED, th);
                unwind(th);
            } else if (__verbose) {
                __log.debug(IGNORED, th);
                unwind(th);
            }
        }
    }

    public static void info(String str) {
        if (__log != null) {
            __log.info(str, null, null);
        }
    }

    public static void info(String str, Object obj) {
        if (__log != null) {
            __log.info(str, obj, null);
        }
    }

    public static void info(String str, Object obj, Object obj2) {
        if (__log != null) {
            __log.info(str, obj, obj2);
        }
    }

    public static boolean isDebugEnabled() {
        return __log == null ? false : __log.isDebugEnabled();
    }

    public static void setLog(Logger logger) {
        __log = logger;
    }

    private static void unwind(Throwable th) {
        if (th != null) {
            for (String method : __nestedEx) {
                try {
                    Throwable th2 = (Throwable) th.getClass().getMethod(method, __noArgs).invoke(th, (Object[]) null);
                    if (!(th2 == null || th2 == th)) {
                        warn(new StringBuffer().append("Nested in ").append(th).append(":").toString(), th2);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public static void warn(String str) {
        if (__log != null) {
            __log.warn(str, null, null);
        }
    }

    public static void warn(String str, Object obj) {
        if (__log != null) {
            __log.warn(str, obj, null);
        }
    }

    public static void warn(String str, Object obj, Object obj2) {
        if (__log != null) {
            __log.warn(str, obj, obj2);
        }
    }

    public static void warn(String str, Throwable th) {
        if (__log != null) {
            __log.warn(str, th);
            unwind(th);
        }
    }

    public static void warn(Throwable th) {
        if (__log != null) {
            __log.warn(EXCEPTION, th);
            unwind(th);
        }
    }
}
