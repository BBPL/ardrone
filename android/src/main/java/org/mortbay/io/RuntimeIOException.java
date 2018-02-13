package org.mortbay.io;

public class RuntimeIOException extends RuntimeException {
    public RuntimeIOException(String str) {
        super(str);
    }

    public RuntimeIOException(String str, Throwable th) {
        super(str, th);
    }

    public RuntimeIOException(Throwable th) {
        super(th);
    }
}
