package com.parrot.freeflight.utils;

public class AsyncTaskResult<T> {
    public final Exception exception;
    public final T result;

    public AsyncTaskResult(T t, Exception exception) {
        this.result = t;
        this.exception = exception;
    }

    public boolean succeeded() {
        return this.exception == null;
    }
}
