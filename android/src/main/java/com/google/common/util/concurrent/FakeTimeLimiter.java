package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Beta
public final class FakeTimeLimiter implements TimeLimiter {
    public <T> T callWithTimeout(Callable<T> callable, long j, TimeUnit timeUnit, boolean z) throws Exception {
        return callable.call();
    }

    public <T> T newProxy(T t, Class<T> cls, long j, TimeUnit timeUnit) {
        return t;
    }
}
