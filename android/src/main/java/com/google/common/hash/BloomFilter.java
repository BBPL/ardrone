package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.Serializable;

@Beta
public final class BloomFilter<T> implements Serializable {
    private static final double LN2 = Math.log(2.0d);
    private static final double LN2_SQUARED = (LN2 * LN2);
    private final BitArray bits;
    private final Funnel<T> funnel;
    private final int numHashFunctions;
    private final Strategy strategy;

    private static class SerialForm<T> implements Serializable {
        private static final long serialVersionUID = 1;
        final long[] data;
        final Funnel<T> funnel;
        final int numHashFunctions;
        final Strategy strategy;

        SerialForm(BloomFilter<T> bloomFilter) {
            this.data = bloomFilter.bits.data;
            this.numHashFunctions = bloomFilter.numHashFunctions;
            this.funnel = bloomFilter.funnel;
            this.strategy = bloomFilter.strategy;
        }

        Object readResolve() {
            return new BloomFilter(new BitArray(this.data), this.numHashFunctions, this.funnel, this.strategy);
        }
    }

    interface Strategy extends Serializable {
        <T> boolean mightContain(T t, Funnel<? super T> funnel, int i, BitArray bitArray);

        int ordinal();

        <T> boolean put(T t, Funnel<? super T> funnel, int i, BitArray bitArray);
    }

    private BloomFilter(BitArray bitArray, int i, Funnel<T> funnel, Strategy strategy) {
        Preconditions.checkArgument(i > 0, "numHashFunctions zero or negative");
        this.bits = (BitArray) Preconditions.checkNotNull(bitArray);
        this.numHashFunctions = i;
        this.funnel = (Funnel) Preconditions.checkNotNull(funnel);
        this.strategy = strategy;
        if (i > 255) {
            throw new AssertionError("Currently we don't allow BloomFilters that would use more than255 hash functions, please contact the guava team");
        }
    }

    public static <T> BloomFilter<T> create(Funnel<T> funnel, int i) {
        return create(funnel, i, 0.03d);
    }

    public static <T> BloomFilter<T> create(Funnel<T> funnel, int i, double d) {
        int i2 = 0;
        int i3 = 1;
        Preconditions.checkNotNull(funnel);
        Preconditions.checkArgument(i >= 0, "Expected insertions cannot be negative");
        int i4 = d > 0.0d ? 1 : 0;
        if (d < 1.0d) {
            i2 = 1;
        }
        Preconditions.checkArgument(i4 & i2, "False positive probability in (0.0, 1.0)");
        if (i != 0) {
            i3 = i;
        }
        i4 = optimalNumOfBits(i3, d);
        return new BloomFilter(new BitArray(i4), optimalNumOfHashFunctions(i3, i4), funnel, BloomFilterStrategies.MURMUR128_MITZ_32);
    }

    @VisibleForTesting
    static int optimalNumOfBits(int i, double d) {
        return (int) ((((double) (-i)) * Math.log(d)) / LN2_SQUARED);
    }

    @VisibleForTesting
    static int optimalNumOfHashFunctions(int i, int i2) {
        return Math.max(1, (int) Math.round(((double) (i2 / i)) * LN2));
    }

    private Object writeReplace() {
        return new SerialForm(this);
    }

    public BloomFilter<T> copy() {
        return new BloomFilter(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BloomFilter)) {
            return false;
        }
        BloomFilter bloomFilter = (BloomFilter) obj;
        return this.numHashFunctions == bloomFilter.numHashFunctions && this.bits.equals(bloomFilter.bits) && this.funnel == bloomFilter.funnel && this.strategy == bloomFilter.strategy;
    }

    public double expectedFalsePositiveProbability() {
        return Math.pow(((double) this.bits.bitCount()) / ((double) this.bits.size()), (double) this.numHashFunctions);
    }

    @VisibleForTesting
    int getHashCount() {
        return this.numHashFunctions;
    }

    public int hashCode() {
        return this.bits.hashCode();
    }

    public boolean mightContain(T t) {
        return this.strategy.mightContain(t, this.funnel, this.numHashFunctions, this.bits);
    }

    public boolean put(T t) {
        return this.strategy.put(t, this.funnel, this.numHashFunctions, this.bits);
    }
}
