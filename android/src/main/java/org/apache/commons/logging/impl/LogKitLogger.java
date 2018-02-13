package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

public class LogKitLogger implements Log, Serializable {
    protected transient Logger logger = null;
    protected String name = null;

    public LogKitLogger(String str) {
        this.name = str;
        this.logger = getLogger();
    }

    public void debug(Object obj) {
        if (obj != null) {
            getLogger().debug(String.valueOf(obj));
        }
    }

    public void debug(Object obj, Throwable th) {
        if (obj != null) {
            getLogger().debug(String.valueOf(obj), th);
        }
    }

    public void error(Object obj) {
        if (obj != null) {
            getLogger().error(String.valueOf(obj));
        }
    }

    public void error(Object obj, Throwable th) {
        if (obj != null) {
            getLogger().error(String.valueOf(obj), th);
        }
    }

    public void fatal(Object obj) {
        if (obj != null) {
            getLogger().fatalError(String.valueOf(obj));
        }
    }

    public void fatal(Object obj, Throwable th) {
        if (obj != null) {
            getLogger().fatalError(String.valueOf(obj), th);
        }
    }

    public Logger getLogger() {
        if (this.logger == null) {
            this.logger = Hierarchy.getDefaultHierarchy().getLoggerFor(this.name);
        }
        return this.logger;
    }

    public void info(Object obj) {
        if (obj != null) {
            getLogger().info(String.valueOf(obj));
        }
    }

    public void info(Object obj, Throwable th) {
        if (obj != null) {
            getLogger().info(String.valueOf(obj), th);
        }
    }

    public boolean isDebugEnabled() {
        return getLogger().isDebugEnabled();
    }

    public boolean isErrorEnabled() {
        return getLogger().isErrorEnabled();
    }

    public boolean isFatalEnabled() {
        return getLogger().isFatalErrorEnabled();
    }

    public boolean isInfoEnabled() {
        return getLogger().isInfoEnabled();
    }

    public boolean isTraceEnabled() {
        return getLogger().isDebugEnabled();
    }

    public boolean isWarnEnabled() {
        return getLogger().isWarnEnabled();
    }

    public void trace(Object obj) {
        debug(obj);
    }

    public void trace(Object obj, Throwable th) {
        debug(obj, th);
    }

    public void warn(Object obj) {
        if (obj != null) {
            getLogger().warn(String.valueOf(obj));
        }
    }

    public void warn(Object obj, Throwable th) {
        if (obj != null) {
            getLogger().warn(String.valueOf(obj), th);
        }
    }
}
