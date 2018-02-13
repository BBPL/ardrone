package org.apache.commons.logging;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.apache.commons.logging.impl.NoOpLog;
import org.mortbay.jetty.HttpVersions;

public class LogSource {
    protected static boolean jdk14IsAvailable;
    protected static boolean log4jIsAvailable;
    protected static Constructor logImplctor = null;
    protected static Hashtable logs = new Hashtable();

    static {
        String str = null;
        log4jIsAvailable = false;
        jdk14IsAvailable = false;
        try {
            if (Class.forName("org.apache.log4j.Logger") != null) {
                log4jIsAvailable = true;
            } else {
                log4jIsAvailable = false;
            }
        } catch (Throwable th) {
            log4jIsAvailable = false;
        }
        try {
            if (Class.forName("java.util.logging.Logger") == null || Class.forName("org.apache.commons.logging.impl.Jdk14Logger") == null) {
                jdk14IsAvailable = false;
                try {
                    str = System.getProperty("org.apache.commons.logging.log");
                    if (str == null) {
                        str = System.getProperty(LogFactoryImpl.LOG_PROPERTY);
                    }
                } catch (Throwable th2) {
                }
                if (str != null) {
                    try {
                        setLogImplementation(str);
                    } catch (Throwable th3) {
                        return;
                    }
                }
                try {
                    if (log4jIsAvailable) {
                        setLogImplementation("org.apache.commons.logging.impl.Log4JLogger");
                    } else if (jdk14IsAvailable) {
                        setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
                    } else {
                        setLogImplementation("org.apache.commons.logging.impl.Jdk14Logger");
                    }
                } catch (Throwable th4) {
                    return;
                }
            }
            jdk14IsAvailable = true;
            str = System.getProperty("org.apache.commons.logging.log");
            if (str == null) {
                str = System.getProperty(LogFactoryImpl.LOG_PROPERTY);
            }
            if (str != null) {
                setLogImplementation(str);
            } else if (log4jIsAvailable) {
                setLogImplementation("org.apache.commons.logging.impl.Log4JLogger");
            } else if (jdk14IsAvailable) {
                setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
            } else {
                setLogImplementation("org.apache.commons.logging.impl.Jdk14Logger");
            }
        } catch (Throwable th5) {
            jdk14IsAvailable = false;
        }
    }

    private LogSource() {
    }

    public static Log getInstance(Class cls) {
        return getInstance(cls.getName());
    }

    public static Log getInstance(String str) {
        Log log = (Log) logs.get(str);
        if (log != null) {
            return log;
        }
        log = makeNewLogInstance(str);
        logs.put(str, log);
        return log;
    }

    public static String[] getLogNames() {
        return (String[]) logs.keySet().toArray(new String[logs.size()]);
    }

    public static Log makeNewLogInstance(String str) {
        Log log;
        try {
            log = (Log) logImplctor.newInstance(new Object[]{str});
        } catch (Throwable th) {
            log = null;
        }
        return log == null ? new NoOpLog(str) : log;
    }

    public static void setLogImplementation(Class cls) throws LinkageError, ExceptionInInitializerError, NoSuchMethodException, SecurityException {
        logImplctor = cls.getConstructor(new Class[]{HttpVersions.HTTP_0_9.getClass()});
    }

    public static void setLogImplementation(String str) throws LinkageError, ExceptionInInitializerError, NoSuchMethodException, SecurityException, ClassNotFoundException {
        try {
            logImplctor = Class.forName(str).getConstructor(new Class[]{HttpVersions.HTTP_0_9.getClass()});
        } catch (Throwable th) {
            logImplctor = null;
        }
    }
}
