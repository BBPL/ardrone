package com.google.common.math;

import com.google.common.base.Preconditions;
import java.math.BigInteger;

final class DoubleUtils {
    static final int EXPONENT_BIAS = 1023;
    static final long EXPONENT_MASK = 9218868437227405312L;
    static final long IMPLICIT_BIT = 4503599627370496L;
    static final int MAX_EXPONENT = 1023;
    static final int MIN_EXPONENT = -1022;
    private static final long ONE_BITS = Double.doubleToRawLongBits(1.0d);
    static final int SIGNIFICAND_BITS = 52;
    static final long SIGNIFICAND_MASK = 4503599627370495L;
    static final long SIGN_MASK = Long.MIN_VALUE;

    private DoubleUtils() {
    }

    static double bigToDouble(BigInteger bigInteger) {
        Object obj = 1;
        BigInteger abs = bigInteger.abs();
        int bitLength = abs.bitLength() - 1;
        if (bitLength < 63) {
            return (double) bigInteger.longValue();
        }
        if (bitLength > 1023) {
            return ((double) bigInteger.signum()) * Double.POSITIVE_INFINITY;
        }
        int i = (bitLength - 52) - 1;
        long longValue = abs.shiftRight(i).longValue();
        long j = (longValue >> 1) & SIGNIFICAND_MASK;
        if ((longValue & 1) == 0 || ((1 & j) == 0 && abs.getLowestSetBit() >= i)) {
            obj = null;
        }
        if (obj != null) {
            j++;
        }
        return Double.longBitsToDouble((j + (((long) (bitLength + 1023)) << 52)) | (((long) bigInteger.signum()) & SIGN_MASK));
    }

    static double copySign(double d, double d2) {
        return Double.longBitsToDouble((Double.doubleToRawLongBits(d) & Long.MAX_VALUE) | (SIGN_MASK & Double.doubleToRawLongBits(d2)));
    }

    static double ensureNonNegative(double d) {
        Preconditions.checkArgument(!Double.isNaN(d));
        return d > 0.0d ? d : 0.0d;
    }

    static int getExponent(double d) {
        return ((int) ((EXPONENT_MASK & Double.doubleToRawLongBits(d)) >>> 52)) - 1023;
    }

    static long getSignificand(double d) {
        Preconditions.checkArgument(isFinite(d), "not a normal value");
        long doubleToRawLongBits = Double.doubleToRawLongBits(d) & SIGNIFICAND_MASK;
        return getExponent(d) == -1023 ? doubleToRawLongBits << 1 : IMPLICIT_BIT | doubleToRawLongBits;
    }

    static boolean isFinite(double d) {
        return getExponent(d) <= 1023;
    }

    static boolean isNormal(double d) {
        return getExponent(d) >= MIN_EXPONENT;
    }

    static double nextDown(double d) {
        return -nextUp(-d);
    }

    static double nextUp(double d) {
        if (!isFinite(d)) {
            return d;
        }
        if (d == 0.0d) {
            return Double.MIN_VALUE;
        }
        long doubleToRawLongBits = Double.doubleToRawLongBits(d);
        return Double.longBitsToDouble(doubleToRawLongBits + ((doubleToRawLongBits >> 63) | 1));
    }

    static double scaleNormalize(double d) {
        return Double.longBitsToDouble((Double.doubleToRawLongBits(d) & SIGNIFICAND_MASK) | ONE_BITS);
    }
}
