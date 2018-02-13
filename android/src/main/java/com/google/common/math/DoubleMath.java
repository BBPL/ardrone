package com.google.common.math;

import com.google.api.client.http.ExponentialBackOffPolicy;
import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Booleans;
import java.math.BigInteger;
import java.math.RoundingMode;

@Beta
public final class DoubleMath {
    @VisibleForTesting
    static final double[] EVERY_SIXTEENTH_FACTORIAL = new double[]{1.0d, 2.0922789888E13d, 2.631308369336935E35d, 1.2413915592536073E61d, 1.2688693218588417E89d, 7.156945704626381E118d, 9.916779348709496E149d, 1.974506857221074E182d, 3.856204823625804E215d, 5.5502938327393044E249d, 4.7147236359920616E284d};
    private static final double LN_2 = Math.log(2.0d);
    @VisibleForTesting
    static final int MAX_FACTORIAL = 170;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9d;
    private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18d;
    private static final double MIN_INT_AS_DOUBLE = -2.147483648E9d;
    private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18d;

    static /* synthetic */ class C08131 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];

        static {
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    private DoubleMath() {
    }

    public static double factorial(int i) {
        MathPreconditions.checkNonNegative("n", i);
        if (i > MAX_FACTORIAL) {
            return Double.POSITIVE_INFINITY;
        }
        double d = 1.0d;
        for (int i2 = (i & -16) + 1; i2 <= i; i2++) {
            d *= (double) i2;
        }
        return EVERY_SIXTEENTH_FACTORIAL[i >> 4] * d;
    }

    @Beta
    public static int fuzzyCompare(double d, double d2, double d3) {
        return fuzzyEquals(d, d2, d3) ? 0 : d < d2 ? -1 : d > d2 ? 1 : Booleans.compare(Double.isNaN(d), Double.isNaN(d2));
    }

    @Beta
    public static boolean fuzzyEquals(double d, double d2, double d3) {
        MathPreconditions.checkNonNegative("tolerance", d3);
        return DoubleUtils.copySign(d - d2, 1.0d) <= d3 || d == d2 || !(d == d || d2 == d2);
    }

    public static boolean isMathematicalInteger(double d) {
        return DoubleUtils.isFinite(d) && (d == LN_2 || 52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(d)) <= DoubleUtils.getExponent(d));
    }

    public static boolean isPowerOfTwo(double d) {
        return d > LN_2 && DoubleUtils.isFinite(d) && LongMath.isPowerOfTwo(DoubleUtils.getSignificand(d));
    }

    public static double log2(double d) {
        return Math.log(d) / LN_2;
    }

    public static int log2(double d, RoundingMode roundingMode) {
        int i = 1;
        int i2 = 0;
        boolean z = d > LN_2 && DoubleUtils.isFinite(d);
        Preconditions.checkArgument(z, "x must be positive and finite");
        int exponent = DoubleUtils.getExponent(d);
        if (!DoubleUtils.isNormal(d)) {
            return log2(4.503599627370496E15d * d, roundingMode) - 52;
        }
        int i3;
        switch (C08131.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(d));
                break;
            case 2:
                break;
            case 3:
                if (isPowerOfTwo(d)) {
                    i = 0;
                    break;
                }
                break;
            case 4:
                i3 = exponent < 0 ? 1 : 0;
                if (!isPowerOfTwo(d)) {
                    i2 = 1;
                }
                i = i3 & i2;
                break;
            case 5:
                i3 = exponent >= 0 ? 1 : 0;
                if (isPowerOfTwo(d)) {
                    i = 0;
                }
                i &= i3;
                break;
            case 6:
            case 7:
            case 8:
                double scaleNormalize = DoubleUtils.scaleNormalize(d);
                if (scaleNormalize * scaleNormalize <= 2.0d) {
                    i = 0;
                    break;
                }
                break;
            default:
                throw new AssertionError();
        }
        i = 0;
        return i != 0 ? exponent + 1 : exponent;
    }

    static double roundIntermediate(double d, RoundingMode roundingMode) {
        if (DoubleUtils.isFinite(d)) {
            double rint;
            switch (C08131.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
                case 1:
                    MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(d));
                    return d;
                case 2:
                    return (d >= LN_2 || isMathematicalInteger(d)) ? d : d - 1.0d;
                case 3:
                    return (d <= LN_2 || isMathematicalInteger(d)) ? d : d + 1.0d;
                case 4:
                    return d;
                case 5:
                    return !isMathematicalInteger(d) ? d + DoubleUtils.copySign(1.0d, d) : d;
                case 6:
                    return Math.rint(d);
                case 7:
                    rint = Math.rint(d);
                    return Math.abs(d - rint) == ExponentialBackOffPolicy.DEFAULT_RANDOMIZATION_FACTOR ? d + DoubleUtils.copySign(ExponentialBackOffPolicy.DEFAULT_RANDOMIZATION_FACTOR, d) : rint;
                case 8:
                    rint = Math.rint(d);
                    return Math.abs(d - rint) != ExponentialBackOffPolicy.DEFAULT_RANDOMIZATION_FACTOR ? rint : d;
                default:
                    throw new AssertionError();
            }
        }
        throw new ArithmeticException("input is infinite or NaN");
    }

    public static BigInteger roundToBigInteger(double d, RoundingMode roundingMode) {
        int i = 0;
        double roundIntermediate = roundIntermediate(d, roundingMode);
        int i2 = MIN_LONG_AS_DOUBLE - roundIntermediate < 1.0d ? 1 : 0;
        if (roundIntermediate < MAX_LONG_AS_DOUBLE_PLUS_ONE) {
            i = 1;
        }
        if ((i & i2) != 0) {
            return BigInteger.valueOf((long) roundIntermediate);
        }
        BigInteger shiftLeft = BigInteger.valueOf(DoubleUtils.getSignificand(roundIntermediate)).shiftLeft(DoubleUtils.getExponent(roundIntermediate) - 52);
        return roundIntermediate < LN_2 ? shiftLeft.negate() : shiftLeft;
    }

    public static int roundToInt(double d, RoundingMode roundingMode) {
        int i = 0;
        double roundIntermediate = roundIntermediate(d, roundingMode);
        int i2 = roundIntermediate > -2.147483649E9d ? 1 : 0;
        if (roundIntermediate < 2.147483648E9d) {
            i = 1;
        }
        MathPreconditions.checkInRange(i & i2);
        return (int) roundIntermediate;
    }

    public static long roundToLong(double d, RoundingMode roundingMode) {
        int i = 0;
        double roundIntermediate = roundIntermediate(d, roundingMode);
        int i2 = MIN_LONG_AS_DOUBLE - roundIntermediate < 1.0d ? 1 : 0;
        if (roundIntermediate < MAX_LONG_AS_DOUBLE_PLUS_ONE) {
            i = 1;
        }
        MathPreconditions.checkInRange(i & i2);
        return (long) roundIntermediate;
    }
}
