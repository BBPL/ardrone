package com.google.api.client.http;

import com.google.api.client.util.NanoClock;
import com.google.api.client.util.Preconditions;
import java.io.IOException;

public class ExponentialBackOffPolicy implements BackOffPolicy {
    public static final int DEFAULT_INITIAL_INTERVAL_MILLIS = 500;
    public static final int DEFAULT_MAX_ELAPSED_TIME_MILLIS = 900000;
    public static final int DEFAULT_MAX_INTERVAL_MILLIS = 60000;
    public static final double DEFAULT_MULTIPLIER = 1.5d;
    public static final double DEFAULT_RANDOMIZATION_FACTOR = 0.5d;
    private int currentIntervalMillis;
    private final int initialIntervalMillis;
    private final int maxElapsedTimeMillis;
    private final int maxIntervalMillis;
    private final double multiplier;
    private final NanoClock nanoClock;
    private final double randomizationFactor;
    long startTimeNanos;

    public static class Builder {
        int initialIntervalMillis = 500;
        int maxElapsedTimeMillis = ExponentialBackOffPolicy.DEFAULT_MAX_ELAPSED_TIME_MILLIS;
        int maxIntervalMillis = ExponentialBackOffPolicy.DEFAULT_MAX_INTERVAL_MILLIS;
        double multiplier = ExponentialBackOffPolicy.DEFAULT_MULTIPLIER;
        NanoClock nanoClock = NanoClock.SYSTEM;
        double randomizationFactor = ExponentialBackOffPolicy.DEFAULT_RANDOMIZATION_FACTOR;

        protected Builder() {
        }

        public ExponentialBackOffPolicy build() {
            return new ExponentialBackOffPolicy(this);
        }

        public final int getInitialIntervalMillis() {
            return this.initialIntervalMillis;
        }

        public final int getMaxElapsedTimeMillis() {
            return this.maxElapsedTimeMillis;
        }

        public final int getMaxIntervalMillis() {
            return this.maxIntervalMillis;
        }

        public final double getMultiplier() {
            return this.multiplier;
        }

        public final NanoClock getNanoClock() {
            return this.nanoClock;
        }

        public final double getRandomizationFactor() {
            return this.randomizationFactor;
        }

        public Builder setInitialIntervalMillis(int i) {
            this.initialIntervalMillis = i;
            return this;
        }

        public Builder setMaxElapsedTimeMillis(int i) {
            this.maxElapsedTimeMillis = i;
            return this;
        }

        public Builder setMaxIntervalMillis(int i) {
            this.maxIntervalMillis = i;
            return this;
        }

        public Builder setMultiplier(double d) {
            this.multiplier = d;
            return this;
        }

        public Builder setNanoClock(NanoClock nanoClock) {
            this.nanoClock = (NanoClock) Preconditions.checkNotNull(nanoClock);
            return this;
        }

        public Builder setRandomizationFactor(double d) {
            this.randomizationFactor = d;
            return this;
        }
    }

    public ExponentialBackOffPolicy() {
        this(new Builder());
    }

    protected ExponentialBackOffPolicy(Builder builder) {
        boolean z = true;
        this.initialIntervalMillis = builder.initialIntervalMillis;
        this.randomizationFactor = builder.randomizationFactor;
        this.multiplier = builder.multiplier;
        this.maxIntervalMillis = builder.maxIntervalMillis;
        this.maxElapsedTimeMillis = builder.maxElapsedTimeMillis;
        this.nanoClock = builder.nanoClock;
        Preconditions.checkArgument(this.initialIntervalMillis > 0);
        boolean z2 = 0.0d <= this.randomizationFactor && this.randomizationFactor < 1.0d;
        Preconditions.checkArgument(z2);
        Preconditions.checkArgument(this.multiplier >= 1.0d);
        Preconditions.checkArgument(this.maxIntervalMillis >= this.initialIntervalMillis);
        if (this.maxElapsedTimeMillis <= 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        reset();
    }

    public static Builder builder() {
        return new Builder();
    }

    static int getRandomValueFromInterval(double d, double d2, int i) {
        double d3 = ((double) i) * d;
        double d4 = ((double) i) - d3;
        return (int) (((((d3 + ((double) i)) - d4) + 1.0d) * d2) + d4);
    }

    private void incrementCurrentInterval() {
        if (((double) this.currentIntervalMillis) >= ((double) this.maxIntervalMillis) / this.multiplier) {
            this.currentIntervalMillis = this.maxIntervalMillis;
        } else {
            this.currentIntervalMillis = (int) (((double) this.currentIntervalMillis) * this.multiplier);
        }
    }

    public final int getCurrentIntervalMillis() {
        return this.currentIntervalMillis;
    }

    public final long getElapsedTimeMillis() {
        return (this.nanoClock.nanoTime() - this.startTimeNanos) / 1000000;
    }

    public final int getInitialIntervalMillis() {
        return this.initialIntervalMillis;
    }

    public final int getMaxElapsedTimeMillis() {
        return this.maxElapsedTimeMillis;
    }

    public final int getMaxIntervalMillis() {
        return this.maxIntervalMillis;
    }

    public final double getMultiplier() {
        return this.multiplier;
    }

    public long getNextBackOffMillis() throws IOException {
        if (getElapsedTimeMillis() > ((long) this.maxElapsedTimeMillis)) {
            return -1;
        }
        int randomValueFromInterval = getRandomValueFromInterval(this.randomizationFactor, Math.random(), this.currentIntervalMillis);
        incrementCurrentInterval();
        return (long) randomValueFromInterval;
    }

    public final double getRandomizationFactor() {
        return this.randomizationFactor;
    }

    public boolean isBackOffRequired(int i) {
        switch (i) {
            case 500:
            case 503:
                return true;
            default:
                return false;
        }
    }

    public final void reset() {
        this.currentIntervalMillis = this.initialIntervalMillis;
        this.startTimeNanos = this.nanoClock.nanoTime();
    }
}
