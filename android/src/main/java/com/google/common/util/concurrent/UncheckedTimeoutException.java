package com.google.common.util.concurrent;

public class UncheckedTimeoutException extends RuntimeException {
    private static final long serialVersionUID = 0;

    public UncheckedTimeoutException(String str) {
        super(str);
    }

    public UncheckedTimeoutException(String str, Throwable th) {
        super(str, th);
    }

    public UncheckedTimeoutException(Throwable th) {
        super(th);
    }
}
