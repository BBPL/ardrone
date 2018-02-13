package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Log4JLogger implements Log, Serializable {
    private static final String FQCN;
    static Class class$org$apache$commons$logging$impl$Log4JLogger;
    static Class class$org$apache$log4j$Level;
    static Class class$org$apache$log4j$Priority;
    private static Priority traceLevel;
    private transient Logger logger = null;
    private String name = null;

    static {
        Class class$;
        Class class$2;
        if (class$org$apache$commons$logging$impl$Log4JLogger == null) {
            class$ = class$("org.apache.commons.logging.impl.Log4JLogger");
            class$org$apache$commons$logging$impl$Log4JLogger = class$;
        } else {
            class$ = class$org$apache$commons$logging$impl$Log4JLogger;
        }
        FQCN = class$.getName();
        if (class$org$apache$log4j$Priority == null) {
            class$ = class$("org.apache.log4j.Priority");
            class$org$apache$log4j$Priority = class$;
        } else {
            class$ = class$org$apache$log4j$Priority;
        }
        if (class$org$apache$log4j$Level == null) {
            class$2 = class$("org.apache.log4j.Level");
            class$org$apache$log4j$Level = class$2;
        } else {
            class$2 = class$org$apache$log4j$Level;
        }
        if (class$.isAssignableFrom(class$2)) {
            try {
                if (class$org$apache$log4j$Level == null) {
                    class$ = class$("org.apache.log4j.Level");
                    class$org$apache$log4j$Level = class$;
                } else {
                    class$ = class$org$apache$log4j$Level;
                }
                traceLevel = (Priority) class$.getDeclaredField("TRACE").get(null);
                return;
            } catch (Exception e) {
                traceLevel = Priority.DEBUG;
                return;
            }
        }
        throw new InstantiationError("Log4J 1.2 not available");
    }

    public Log4JLogger(String str) {
        this.name = str;
        this.logger = getLogger();
    }

    public Log4JLogger(Logger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Warning - null logger in constructor; possible log4j misconfiguration.");
        }
        this.name = logger.getName();
        this.logger = logger;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public void debug(Object obj) {
        getLogger().log(FQCN, Priority.DEBUG, obj, null);
    }

    public void debug(Object obj, Throwable th) {
        getLogger().log(FQCN, Priority.DEBUG, obj, th);
    }

    public void error(Object obj) {
        getLogger().log(FQCN, Priority.ERROR, obj, null);
    }

    public void error(Object obj, Throwable th) {
        getLogger().log(FQCN, Priority.ERROR, obj, th);
    }

    public void fatal(Object obj) {
        getLogger().log(FQCN, Priority.FATAL, obj, null);
    }

    public void fatal(Object obj, Throwable th) {
        getLogger().log(FQCN, Priority.FATAL, obj, th);
    }

    public Logger getLogger() {
        if (this.logger == null) {
            this.logger = Logger.getLogger(this.name);
        }
        return this.logger;
    }

    public void info(Object obj) {
        getLogger().log(FQCN, Priority.INFO, obj, null);
    }

    public void info(Object obj, Throwable th) {
        getLogger().log(FQCN, Priority.INFO, obj, th);
    }

    public boolean isDebugEnabled() {
        return getLogger().isDebugEnabled();
    }

    public boolean isErrorEnabled() {
        return getLogger().isEnabledFor(Priority.ERROR);
    }

    public boolean isFatalEnabled() {
        return getLogger().isEnabledFor(Priority.FATAL);
    }

    public boolean isInfoEnabled() {
        return getLogger().isInfoEnabled();
    }

    public boolean isTraceEnabled() {
        return getLogger().isEnabledFor(traceLevel);
    }

    public boolean isWarnEnabled() {
        return getLogger().isEnabledFor(Priority.WARN);
    }

    public void trace(Object obj) {
        getLogger().log(FQCN, traceLevel, obj, null);
    }

    public void trace(Object obj, Throwable th) {
        getLogger().log(FQCN, traceLevel, obj, th);
    }

    public void warn(Object obj) {
        getLogger().log(FQCN, Priority.WARN, obj, null);
    }

    public void warn(Object obj, Throwable th) {
        getLogger().log(FQCN, Priority.WARN, obj, th);
    }
}
