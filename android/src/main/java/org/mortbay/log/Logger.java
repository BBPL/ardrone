package org.mortbay.log;

public interface Logger {
    void debug(String str, Object obj, Object obj2);

    void debug(String str, Throwable th);

    Logger getLogger(String str);

    void info(String str, Object obj, Object obj2);

    boolean isDebugEnabled();

    void setDebugEnabled(boolean z);

    void warn(String str, Object obj, Object obj2);

    void warn(String str, Throwable th);
}
