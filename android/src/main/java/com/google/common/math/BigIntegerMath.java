package com.google.common.math;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@GwtCompatible(emulated = true)
@Beta
public final class BigIntegerMath {
    private static final double LN_10 = Math.log(10.0d);
    private static final double LN_2 = Math.log(2.0d);
    @VisibleForTesting
    static final BigInteger SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
    @VisibleForTesting
    static final int SQRT2_PRECOMPUTE_THRESHOLD = 256;

    static /* synthetic */ class C08121 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];

        static {
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    private BigIntegerMath() {
    }

    public static BigInteger binomial(int i, int i2) {
        MathPreconditions.checkNonNegative("n", i);
        MathPreconditions.checkNonNegative("k", i2);
        Preconditions.checkArgument(i2 <= i, "k (%s) > n (%s)", Integer.valueOf(i2), Integer.valueOf(i));
        if (i2 > (i >> 1)) {
            i2 = i - i2;
        }
        if (i2 < LongMath.BIGGEST_BINOMIALS.length && i <= LongMath.BIGGEST_BINOMIALS[i2]) {
            return BigInteger.valueOf(LongMath.binomial(i, i2));
        }
        BigInteger bigInteger = BigInteger.ONE;
        long j = (long) i;
        long j2 = 1;
        int log2 = LongMath.log2((long) i, RoundingMode.CEILING);
        int i3 = log2;
        for (int i4 = 1; i4 < i2; i4++) {
            int i5 = i - i4;
            int i6 = i4 + 1;
            if (i3 + log2 >= 63) {
                bigInteger = bigInteger.multiply(BigInteger.valueOf(j)).divide(BigInteger.valueOf(j2));
                j = (long) i5;
                j2 = (long) i6;
                i3 = log2;
            } else {
                j *= (long) i5;
                j2 *= (long) i6;
                i3 += log2;
            }
        }
        return bigInteger.multiply(BigInteger.valueOf(j)).divide(BigInteger.valueOf(j2));
    }

    @GwtIncompatible("TODO")
    public static BigInteger divide(BigInteger bigInteger, BigInteger bigInteger2, RoundingMode roundingMode) {
        return new BigDecimal(bigInteger).divide(new BigDecimal(bigInteger2), 0, roundingMode).toBigIntegerExact();
    }

    public static BigInteger factorial(int i) {
        MathPreconditions.checkNonNegative("n", i);
        if (i < LongMath.FACTORIALS.length) {
            return BigInteger.valueOf(LongMath.FACTORIALS[i]);
        }
        List arrayList = new ArrayList(IntMath.divide(IntMath.log2(i, RoundingMode.CEILING) * i, 64, RoundingMode.CEILING));
        int length = LongMath.FACTORIALS.length;
        long j = LongMath.FACTORIALS[length - 1];
        int numberOfTrailingZeros = Long.numberOfTrailingZeros(j);
        j >>= numberOfTrailingZeros;
        int log2 = LongMath.log2(j, RoundingMode.FLOOR) + 1;
        int log22 = LongMath.log2((long) length, RoundingMode.FLOOR) + 1;
        long j2 = (long) length;
        int i2 = 1 << (log22 - 1);
        length = log22;
        long j3 = j2;
        while (j3 <= ((long) i)) {
            if ((((long) i2) & j3) != 0) {
                i2 <<= 1;
                length++;
            }
            int numberOfTrailingZeros2 = Long.numberOfTrailingZeros(j3);
            int i3 = numberOfTrailingZeros + numberOfTrailingZeros2;
            if ((length - numberOfTrailingZeros2) + log2 >= 64) {
                arrayList.add(BigInteger.valueOf(j));
                j = 1;
            }
            long j4 = (j3 >> numberOfTrailingZeros2) * j;
            numberOfTrailingZeros2 = LongMath.log2(j4, RoundingMode.FLOOR) + 1;
            j3 = 1 + j3;
            j = j4;
            numberOfTrailingZeros = i3;
            log2 = numberOfTrailingZeros2;
        }
        if (j > 1) {
            arrayList.add(BigInteger.valueOf(j));
        }
        return listProduct(arrayList).shiftLeft(numberOfTrailingZeros);
    }

    @GwtIncompatible("TODO")
    static boolean fitsInLong(BigInteger bigInteger) {
        return bigInteger.bitLength() <= 63;
    }

    public static boolean isPowerOfTwo(BigInteger bigInteger) {
        Preconditions.checkNotNull(bigInteger);
        return bigInteger.signum() > 0 && bigInteger.getLowestSetBit() == bigInteger.bitLength() - 1;
    }

    static BigInteger listProduct(List<BigInteger> list) {
        return listProduct(list, 0, list.size());
    }

    static BigInteger listProduct(List<BigInteger> list, int i, int i2) {
        switch (i2 - i) {
            case 0:
                return BigInteger.ONE;
            case 1:
                return (BigInteger) list.get(i);
            case 2:
                return ((BigInteger) list.get(i)).multiply((BigInteger) list.get(i + 1));
            case 3:
                return ((BigInteger) list.get(i)).multiply((BigInteger) list.get(i + 1)).multiply((BigInteger) list.get(i + 2));
            default:
                int i3 = (i2 + i) >>> 1;
                return listProduct(list, i, i3).multiply(listProduct(list, i3, i2));
        }
    }

    @GwtIncompatible("TODO")
    public static int log10(BigInteger bigInteger, RoundingMode roundingMode) {
        MathPreconditions.checkPositive("x", bigInteger);
        if (fitsInLong(bigInteger)) {
            return LongMath.log10(bigInteger.longValue(), roundingMode);
        }
        int i;
        BigInteger bigInteger2;
        int log2 = (int) ((((double) log2(bigInteger, RoundingMode.FLOOR)) * LN_2) / LN_10);
        BigInteger pow = BigInteger.TEN.pow(log2);
        int compareTo = pow.compareTo(bigInteger);
        BigInteger bigInteger3;
        if (compareTo > 0) {
            do {
                log2--;
                pow = pow.divide(BigInteger.TEN);
                compareTo = pow.compareTo(bigInteger);
            } while (compareTo > 0);
            bigInteger3 = pow;
            i = log2;
            bigInteger2 = bigInteger3;
        } else {
            BigInteger multiply = BigInteger.TEN.multiply(pow);
            int compareTo2 = multiply.compareTo(bigInteger);
            int i2 = compareTo;
            compareTo = log2;
            log2 = i2;
            while (compareTo2 <= 0) {
                compareTo++;
                pow = BigInteger.TEN.multiply(multiply);
                log2 = pow.compareTo(bigInteger);
                bigInteger3 = multiply;
                multiply = pow;
                pow = bigInteger3;
                int i3 = compareTo2;
                compareTo2 = log2;
                log2 = i3;
            }
            bigInteger3 = pow;
            i = compareTo;
            compareTo = log2;
            bigInteger2 = bigInteger3;
        }
        switch (C08121.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(compareTo == 0);
                return i;
            case 2:
            case 3:
                return i;
            case 4:
            case 5:
                return !bigInteger2.equals(bigInteger) ? i + 1 : i;
            case 6:
            case 7:
            case 8:
                return bigInteger.pow(2).compareTo(bigInteger2.pow(2).multiply(BigInteger.TEN)) > 0 ? i + 1 : i;
            default:
                throw new AssertionError();
        }
    }

    public static int log2(BigInteger bigInteger, RoundingMode roundingMode) {
        MathPreconditions.checkPositive("x", (BigInteger) Preconditions.checkNotNull(bigInteger));
        int bitLength = bigInteger.bitLength() - 1;
        switch (C08121.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(bigInteger));
                return bitLength;
            case 2:
            case 3:
                return bitLength;
            case 4:
            case 5:
                return !isPowerOfTwo(bigInteger) ? bitLength + 1 : bitLength;
            case 6:
            case 7:
            case 8:
                return bitLength < 256 ? bigInteger.compareTo(SQRT2_PRECOMPUTED_BITS.shiftRight(256 - bitLength)) > 0 ? bitLength + 1 : bitLength : bigInteger.pow(2).bitLength() + -1 >= (bitLength * 2) + 1 ? bitLength + 1 : bitLength;
            default:
                throw new AssertionError();
        }
    }

    @GwtIncompatible("TODO")
    public static BigInteger sqrt(BigInteger bigInteger, RoundingMode roundingMode) {
        MathPreconditions.checkNonNegative("x", bigInteger);
        if (fitsInLong(bigInteger)) {
            return BigInteger.valueOf(LongMath.sqrt(bigInteger.longValue(), roundingMode));
        }
        BigInteger sqrtFloor = sqrtFloor(bigInteger);
        switch (C08121.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(sqrtFloor.pow(2).equals(bigInteger));
                return sqrtFloor;
            case 2:
            case 3:
                return sqrtFloor;
            case 4:
            case 5:
                return !sqrtFloor.pow(2).equals(bigInteger) ? sqrtFloor.add(BigInteger.ONE) : sqrtFloor;
            case 6:
            case 7:
            case 8:
                return sqrtFloor.pow(2).add(sqrtFloor).compareTo(bigInteger) < 0 ? sqrtFloor.add(BigInteger.ONE) : sqrtFloor;
            default:
                throw new AssertionError();
        }
    }

    @GwtIncompatible("TODO")
    private static BigInteger sqrtApproxWithDoubles(BigInteger bigInteger) {
        return DoubleMath.roundToBigInteger(Math.sqrt(DoubleUtils.bigToDouble(bigInteger)), RoundingMode.HALF_EVEN);
    }

    @GwtIncompatible("TODO")
    private static BigInteger sqrtFloor(BigInteger bigInteger) {
        BigInteger sqrtApproxWithDoubles;
        int log2 = log2(bigInteger, RoundingMode.FLOOR);
        if (log2 < 1023) {
            sqrtApproxWithDoubles = sqrtApproxWithDoubles(bigInteger);
        } else {
            log2 = (log2 - 52) & -2;
            sqrtApproxWithDoubles = sqrtApproxWithDoubles(bigInteger.shiftRight(log2)).shiftLeft(log2 >> 1);
        }
        BigInteger shiftRight = sqrtApproxWithDoubles.add(bigInteger.divide(sqrtApproxWithDoubles)).shiftRight(1);
        if (!sqrtApproxWithDoubles.equals(shiftRight)) {
            do {
                sqrtApproxWithDoubles = shiftRight;
                shiftRight = sqrtApproxWithDoubles.add(bigInteger.divide(sqrtApproxWithDoubles)).shiftRight(1);
            } while (shiftRight.compareTo(sqrtApproxWithDoubles) < 0);
        }
        return sqrtApproxWithDoubles;
    }
}
