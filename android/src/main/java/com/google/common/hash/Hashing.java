package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedInts;
import java.nio.ByteBuffer;
import java.util.Iterator;

@Beta
public final class Hashing {
    private static final HashFunction GOOD_FAST_HASH_FUNCTION_128 = murmur3_128(GOOD_FAST_HASH_SEED);
    private static final HashFunction GOOD_FAST_HASH_FUNCTION_32 = murmur3_32(GOOD_FAST_HASH_SEED);
    private static final int GOOD_FAST_HASH_SEED = ((int) System.currentTimeMillis());
    private static final HashFunction MD5 = new MessageDigestHashFunction("MD5");
    private static final Murmur3_128HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
    private static final Murmur3_32HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0);
    private static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1");
    private static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256");
    private static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512");

    @VisibleForTesting
    static final class ConcatenatedHashFunction extends AbstractCompositeHashFunction {
        private final int bits;

        ConcatenatedHashFunction(HashFunction... hashFunctionArr) {
            int i = 0;
            super(hashFunctionArr);
            for (HashFunction bits : hashFunctionArr) {
                i += bits.bits();
            }
            this.bits = i;
        }

        public int bits() {
            return this.bits;
        }

        HashCode makeHash(Hasher[] hasherArr) {
            byte[] bArr = new byte[(this.bits / 8)];
            ByteBuffer wrap = ByteBuffer.wrap(bArr);
            for (Hasher hash : hasherArr) {
                wrap.put(hash.hash().asBytes());
            }
            return HashCodes.fromBytesNoCopy(bArr);
        }
    }

    private static final class LinearCongruentialGenerator {
        private long state;

        public LinearCongruentialGenerator(long j) {
            this.state = j;
        }

        public double nextDouble() {
            this.state = (2862933555777941757L * this.state) + 1;
            return ((double) (((int) (this.state >>> 33)) + 1)) / 2.147483648E9d;
        }
    }

    private Hashing() {
    }

    static int checkPositiveAndMakeMultipleOf32(int i) {
        Preconditions.checkArgument(i > 0, "Number of bits must be positive");
        return (i + 31) & -32;
    }

    public static HashCode combineOrdered(Iterable<HashCode> iterable) {
        Iterator it = iterable.iterator();
        Preconditions.checkArgument(it.hasNext(), "Must be at least 1 hash code to combine.");
        byte[] bArr = new byte[(((HashCode) it.next()).bits() / 8)];
        for (HashCode asBytes : iterable) {
            byte[] asBytes2 = asBytes.asBytes();
            Preconditions.checkArgument(asBytes2.length == bArr.length, "All hashcodes must have the same bit length.");
            for (int i = 0; i < asBytes2.length; i++) {
                bArr[i] = (byte) ((bArr[i] * 37) ^ asBytes2[i]);
            }
        }
        return HashCodes.fromBytesNoCopy(bArr);
    }

    public static HashCode combineUnordered(Iterable<HashCode> iterable) {
        Iterator it = iterable.iterator();
        Preconditions.checkArgument(it.hasNext(), "Must be at least 1 hash code to combine.");
        byte[] bArr = new byte[(((HashCode) it.next()).bits() / 8)];
        for (HashCode asBytes : iterable) {
            byte[] asBytes2 = asBytes.asBytes();
            Preconditions.checkArgument(asBytes2.length == bArr.length, "All hashcodes must have the same bit length.");
            for (int i = 0; i < asBytes2.length; i++) {
                bArr[i] = (byte) (bArr[i] + asBytes2[i]);
            }
        }
        return HashCodes.fromBytesNoCopy(bArr);
    }

    public static int consistentHash(long j, int i) {
        int i2 = 0;
        Preconditions.checkArgument(i > 0, "buckets must be positive: %s", Integer.valueOf(i));
        LinearCongruentialGenerator linearCongruentialGenerator = new LinearCongruentialGenerator(j);
        while (true) {
            int nextDouble = (int) (((double) (i2 + 1)) / linearCongruentialGenerator.nextDouble());
            if (nextDouble < 0 || nextDouble >= i) {
                return i2;
            }
            i2 = nextDouble;
        }
        return i2;
    }

    public static int consistentHash(HashCode hashCode, int i) {
        return consistentHash(padToLong(hashCode), i);
    }

    public static HashFunction goodFastHash(int i) {
        int checkPositiveAndMakeMultipleOf32 = checkPositiveAndMakeMultipleOf32(i);
        if (checkPositiveAndMakeMultipleOf32 == 32) {
            return GOOD_FAST_HASH_FUNCTION_32;
        }
        if (checkPositiveAndMakeMultipleOf32 <= 128) {
            return GOOD_FAST_HASH_FUNCTION_128;
        }
        int i2 = (checkPositiveAndMakeMultipleOf32 + 127) / 128;
        HashFunction[] hashFunctionArr = new HashFunction[i2];
        hashFunctionArr[0] = GOOD_FAST_HASH_FUNCTION_128;
        checkPositiveAndMakeMultipleOf32 = GOOD_FAST_HASH_SEED;
        for (int i3 = 1; i3 < i2; i3++) {
            checkPositiveAndMakeMultipleOf32 += 1500450271;
            hashFunctionArr[i3] = murmur3_128(checkPositiveAndMakeMultipleOf32);
        }
        return new ConcatenatedHashFunction(hashFunctionArr);
    }

    public static HashFunction md5() {
        return MD5;
    }

    public static HashFunction murmur3_128() {
        return MURMUR3_128;
    }

    public static HashFunction murmur3_128(int i) {
        return new Murmur3_128HashFunction(i);
    }

    public static HashFunction murmur3_32() {
        return MURMUR3_32;
    }

    public static HashFunction murmur3_32(int i) {
        return new Murmur3_32HashFunction(i);
    }

    public static long padToLong(HashCode hashCode) {
        return hashCode.bits() < 64 ? UnsignedInts.toLong(hashCode.asInt()) : hashCode.asLong();
    }

    public static HashFunction sha1() {
        return SHA_1;
    }

    public static HashFunction sha256() {
        return SHA_256;
    }

    public static HashFunction sha512() {
        return SHA_512;
    }
}
