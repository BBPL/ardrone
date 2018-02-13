package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@Beta
public abstract class RateLimiter {
    double maxPermits;
    private long nextFreeTicketMicros;
    private final long offsetNanos;
    double stableIntervalMicros;
    double storedPermits;
    private final SleepingTicker ticker;

    private static class Bursty extends RateLimiter {
        Bursty(SleepingTicker sleepingTicker) {
            super(sleepingTicker);
        }

        void doSetRate(double d, double d2) {
            double d3 = 0.0d;
            double d4 = this.maxPermits;
            this.maxPermits = d;
            if (d4 != 0.0d) {
                d3 = (this.storedPermits * this.maxPermits) / d4;
            }
            this.storedPermits = d3;
        }

        long storedPermitsToWaitTime(double d, double d2) {
            return 0;
        }
    }

    @VisibleForTesting
    static abstract class SleepingTicker extends Ticker {
        static final SleepingTicker SYSTEM_TICKER = new C08771();

        static final class C08771 extends SleepingTicker {
            C08771() {
            }

            public long read() {
                return Ticker.systemTicker().read();
            }

            public void sleepMicrosUninterruptibly(long j) {
                if (j > 0) {
                    Uninterruptibles.sleepUninterruptibly(j, TimeUnit.MICROSECONDS);
                }
            }
        }

        SleepingTicker() {
        }

        abstract void sleepMicrosUninterruptibly(long j);
    }

    private static class WarmingUp extends RateLimiter {
        private double halfPermits;
        private double slope;
        final long warmupPeriodMicros;

        WarmingUp(SleepingTicker sleepingTicker, long j, TimeUnit timeUnit) {
            super(sleepingTicker);
            this.warmupPeriodMicros = timeUnit.toMicros(j);
        }

        private double permitsToTime(double d) {
            return this.stableIntervalMicros + (this.slope * d);
        }

        void doSetRate(double d, double d2) {
            double d3 = this.maxPermits;
            this.maxPermits = ((double) this.warmupPeriodMicros) / d2;
            this.halfPermits = this.maxPermits / 2.0d;
            this.slope = ((3.0d * d2) - d2) / this.halfPermits;
            if (d3 == Double.POSITIVE_INFINITY) {
                this.storedPermits = 0.0d;
            } else {
                this.storedPermits = d3 == 0.0d ? this.maxPermits : (this.storedPermits * this.maxPermits) / d3;
            }
        }

        long storedPermitsToWaitTime(double d, double d2) {
            double d3 = d - this.halfPermits;
            long j = 0;
            if (d3 > 0.0d) {
                double min = Math.min(d3, d2);
                j = (long) (((permitsToTime(d3) + permitsToTime(d3 - min)) * min) / 2.0d);
                d2 -= min;
            }
            return (long) (((double) j) + (this.stableIntervalMicros * d2));
        }
    }

    private RateLimiter(SleepingTicker sleepingTicker) {
        this.nextFreeTicketMicros = 0;
        this.ticker = sleepingTicker;
        this.offsetNanos = sleepingTicker.read();
    }

    private static void checkPermits(int i) {
        Preconditions.checkArgument(i > 0, "Requested permits must be positive");
    }

    public static RateLimiter create(double d) {
        return create(SleepingTicker.SYSTEM_TICKER, d);
    }

    public static RateLimiter create(double d, long j, TimeUnit timeUnit) {
        return create(SleepingTicker.SYSTEM_TICKER, d, j, timeUnit);
    }

    @VisibleForTesting
    static RateLimiter create(SleepingTicker sleepingTicker, double d) {
        RateLimiter bursty = new Bursty(sleepingTicker);
        bursty.setRate(d);
        return bursty;
    }

    @VisibleForTesting
    static RateLimiter create(SleepingTicker sleepingTicker, double d, long j, TimeUnit timeUnit) {
        RateLimiter warmingUp = new WarmingUp(sleepingTicker, j, timeUnit);
        warmingUp.setRate(d);
        return warmingUp;
    }

    @VisibleForTesting
    static RateLimiter createBursty(SleepingTicker sleepingTicker, double d, int i) {
        RateLimiter bursty = new Bursty(sleepingTicker);
        bursty.setRate(d);
        bursty.maxPermits = (double) i;
        return bursty;
    }

    private long readSafeMicros() {
        return TimeUnit.NANOSECONDS.toMicros(this.ticker.read() - this.offsetNanos);
    }

    private long reserveNextTicket(double d, long j) {
        resync(j);
        long j2 = this.nextFreeTicketMicros;
        double min = Math.min(d, this.storedPermits);
        this.nextFreeTicketMicros = (storedPermitsToWaitTime(this.storedPermits, min) + ((long) (this.stableIntervalMicros * (d - min)))) + this.nextFreeTicketMicros;
        this.storedPermits -= min;
        return j2 - j;
    }

    private void resync(long j) {
        if (j > this.nextFreeTicketMicros) {
            this.storedPermits = Math.min(this.maxPermits, this.storedPermits + (((double) (j - this.nextFreeTicketMicros)) / this.stableIntervalMicros));
            this.nextFreeTicketMicros = j;
        }
    }

    public void acquire() {
        acquire(1);
    }

    public void acquire(int i) {
        long reserveNextTicket;
        checkPermits(i);
        synchronized (this) {
            reserveNextTicket = reserveNextTicket((double) i, readSafeMicros());
        }
        this.ticker.sleepMicrosUninterruptibly(reserveNextTicket);
    }

    abstract void doSetRate(double d, double d2);

    public final double getRate() {
        double toMicros;
        synchronized (this) {
            toMicros = ((double) TimeUnit.SECONDS.toMicros(1)) / this.stableIntervalMicros;
        }
        return toMicros;
    }

    public final void setRate(double d) {
        synchronized (this) {
            boolean z;
            double toMicros;
            if (d > 0.0d) {
                if (!Double.isNaN(d)) {
                    z = true;
                    Preconditions.checkArgument(z, "rate must be positive");
                    resync(readSafeMicros());
                    toMicros = ((double) TimeUnit.SECONDS.toMicros(1)) / d;
                    this.stableIntervalMicros = toMicros;
                    doSetRate(d, toMicros);
                }
            }
            z = false;
            Preconditions.checkArgument(z, "rate must be positive");
            resync(readSafeMicros());
            toMicros = ((double) TimeUnit.SECONDS.toMicros(1)) / d;
            this.stableIntervalMicros = toMicros;
            doSetRate(d, toMicros);
        }
    }

    abstract long storedPermitsToWaitTime(double d, double d2);

    public String toString() {
        return String.format("RateLimiter[stableRate=%3.1fqps]", new Object[]{Double.valueOf(1000000.0d / this.stableIntervalMicros)});
    }

    public boolean tryAcquire(int i, long j, TimeUnit timeUnit) {
        checkPermits(i);
        long toMicros = timeUnit.toMicros(j);
        synchronized (this) {
            long readSafeMicros = readSafeMicros();
            if (this.nextFreeTicketMicros > toMicros + readSafeMicros) {
                return false;
            }
            toMicros = reserveNextTicket((double) i, readSafeMicros);
            this.ticker.sleepMicrosUninterruptibly(toMicros);
            return true;
        }
    }

    public boolean tryAcquire(long j, TimeUnit timeUnit) {
        return tryAcquire(1, j, timeUnit);
    }
}
