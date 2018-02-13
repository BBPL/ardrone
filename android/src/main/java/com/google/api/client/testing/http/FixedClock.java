package com.google.api.client.testing.http;

import com.google.api.client.util.Clock;
import java.util.concurrent.atomic.AtomicLong;

public class FixedClock implements Clock {
    private AtomicLong currentTime;

    public FixedClock() {
        this(0);
    }

    public FixedClock(long j) {
        this.currentTime = new AtomicLong(j);
    }

    public long currentTimeMillis() {
        return this.currentTime.get();
    }

    public FixedClock setTime(long j) {
        this.currentTime.set(j);
        return this;
    }
}
