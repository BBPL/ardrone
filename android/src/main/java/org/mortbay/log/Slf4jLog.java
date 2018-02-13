package org.mortbay.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jLog implements Logger {
    private Logger logger;

    public Slf4jLog() throws Exception {
        this("org.mortbay.log");
    }

    public Slf4jLog(String str) {
        this.logger = LoggerFactory.getLogger(str);
    }

    public void debug(String str, Object obj, Object obj2) {
        this.logger.debug(str, obj, obj2);
    }

    public void debug(String str, Throwable th) {
        this.logger.debug(str, th);
    }

    public Logger getLogger(String str) {
        return new Slf4jLog(str);
    }

    public void info(String str, Object obj, Object obj2) {
        this.logger.info(str, obj, obj2);
    }

    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    public void setDebugEnabled(boolean z) {
        warn("setDebugEnabled not implemented", null, null);
    }

    public String toString() {
        return this.logger.toString();
    }

    public void warn(String str, Object obj, Object obj2) {
        this.logger.warn(str, obj, obj2);
    }

    public void warn(String str, Throwable th) {
        if ((th instanceof RuntimeException) || (th instanceof Error)) {
            this.logger.error(str, th);
        } else {
            this.logger.warn(str, th);
        }
    }
}
