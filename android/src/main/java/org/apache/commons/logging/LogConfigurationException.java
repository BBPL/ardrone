package org.apache.commons.logging;

public class LogConfigurationException extends RuntimeException {
    protected Throwable cause;

    public LogConfigurationException() {
        this.cause = null;
    }

    public LogConfigurationException(String str) {
        super(str);
        this.cause = null;
    }

    public LogConfigurationException(String str, Throwable th) {
        super(new StringBuffer().append(str).append(" (Caused by ").append(th).append(")").toString());
        this.cause = null;
        this.cause = th;
    }

    public LogConfigurationException(Throwable th) {
        this(th == null ? null : th.toString(), th);
    }

    public Throwable getCause() {
        return this.cause;
    }
}
