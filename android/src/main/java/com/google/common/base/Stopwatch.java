package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.TimeUnit;

@GwtCompatible(emulated = true)
@Beta
public final class Stopwatch {
    private long elapsedNanos;
    private boolean isRunning;
    private long startTick;
    private final Ticker ticker;

    static /* synthetic */ class C05051 {
        static final /* synthetic */ int[] $SwitchMap$java$util$concurrent$TimeUnit = new int[TimeUnit.values().length];

        static {
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.NANOSECONDS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MICROSECONDS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.MILLISECONDS.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.SECONDS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public Stopwatch() {
        this(Ticker.systemTicker());
    }

    public Stopwatch(Ticker ticker) {
        this.ticker = (Ticker) Preconditions.checkNotNull(ticker);
    }

    private static String abbreviate(TimeUnit timeUnit) {
        switch (C05051.$SwitchMap$java$util$concurrent$TimeUnit[timeUnit.ordinal()]) {
            case 1:
                return "ns";
            case 2:
                return "μs";
            case 3:
                return "ms";
            case 4:
                return "s";
            default:
                throw new AssertionError();
        }
    }

    private static TimeUnit chooseUnit(long j) {
        return TimeUnit.SECONDS.convert(j, TimeUnit.NANOSECONDS) > 0 ? TimeUnit.SECONDS : TimeUnit.MILLISECONDS.convert(j, TimeUnit.NANOSECONDS) > 0 ? TimeUnit.MILLISECONDS : TimeUnit.MICROSECONDS.convert(j, TimeUnit.NANOSECONDS) > 0 ? TimeUnit.MICROSECONDS : TimeUnit.NANOSECONDS;
    }

    private long elapsedNanos() {
        return this.isRunning ? (this.ticker.read() - this.startTick) + this.elapsedNanos : this.elapsedNanos;
    }

    public long elapsedMillis() {
        return elapsedTime(TimeUnit.MILLISECONDS);
    }

    public long elapsedTime(TimeUnit timeUnit) {
        return timeUnit.convert(elapsedNanos(), TimeUnit.NANOSECONDS);
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public Stopwatch reset() {
        this.elapsedNanos = 0;
        this.isRunning = false;
        return this;
    }

    public Stopwatch start() {
        Preconditions.checkState(!this.isRunning);
        this.isRunning = true;
        this.startTick = this.ticker.read();
        return this;
    }

    public Stopwatch stop() {
        long read = this.ticker.read();
        Preconditions.checkState(this.isRunning);
        this.isRunning = false;
        this.elapsedNanos = (read - this.startTick) + this.elapsedNanos;
        return this;
    }

    @GwtIncompatible("String.format()")
    public String toString() {
        return toString(4);
    }

    @GwtIncompatible("String.format()")
    @Deprecated
    public String toString(int i) {
        long elapsedNanos = elapsedNanos();
        double convert = ((double) elapsedNanos) / ((double) TimeUnit.NANOSECONDS.convert(1, chooseUnit(elapsedNanos)));
        return String.format("%." + i + "g %s", new Object[]{Double.valueOf(convert), abbreviate(r2)});
    }
}
