package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import java.util.Arrays;

enum BloomFilterStrategies implements Strategy {
    MURMUR128_MITZ_32 {
        public <T> boolean mightContain(T t, Funnel<? super T> funnel, int i, BitArray bitArray) {
            long asLong = Hashing.murmur3_128().newHasher().putObject(t, funnel).hash().asLong();
            int i2 = (int) asLong;
            int i3 = (int) (asLong >>> 32);
            for (int i4 = 1; i4 <= i; i4++) {
                int i5 = (i4 * i3) + i2;
                if (i5 < 0) {
                    i5 ^= -1;
                }
                if (!bitArray.get(i5 % bitArray.size())) {
                    return false;
                }
            }
            return true;
        }

        public <T> boolean put(T t, Funnel<? super T> funnel, int i, BitArray bitArray) {
            long asLong = Hashing.murmur3_128().newHasher().putObject(t, funnel).hash().asLong();
            int i2 = (int) asLong;
            int i3 = (int) (asLong >>> 32);
            int i4 = 1;
            boolean z = false;
            while (i4 <= i) {
                int i5 = (i4 * i3) + i2;
                if (i5 < 0) {
                    i5 ^= -1;
                }
                i4++;
                z = bitArray.set(i5 % bitArray.size()) | z;
            }
            return z;
        }
    };

    static class BitArray {
        int bitCount;
        final long[] data;

        BitArray(int i) {
            this(new long[IntMath.divide(i, 64, RoundingMode.CEILING)]);
        }

        BitArray(long[] jArr) {
            int i = 0;
            Preconditions.checkArgument(jArr.length > 0, "data length is zero!");
            this.data = jArr;
            for (long bitCount : jArr) {
                i += Long.bitCount(bitCount);
            }
            this.bitCount = i;
        }

        int bitCount() {
            return this.bitCount;
        }

        BitArray copy() {
            return new BitArray((long[]) this.data.clone());
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof BitArray)) {
                return false;
            }
            return Arrays.equals(this.data, ((BitArray) obj).data);
        }

        boolean get(int i) {
            return (this.data[i >> 6] & (1 << i)) != 0;
        }

        public int hashCode() {
            return Arrays.hashCode(this.data);
        }

        boolean set(int i) {
            if (get(i)) {
                return false;
            }
            long[] jArr = this.data;
            int i2 = i >> 6;
            jArr[i2] = jArr[i2] | (1 << i);
            this.bitCount++;
            return true;
        }

        int size() {
            return this.data.length * 64;
        }
    }
}
