package com.google.common.math;

import com.google.android.gms.location.LocationRequest;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import java.math.RoundingMode;
import org.apache.http.HttpStatus;

@GwtCompatible(emulated = true)
@Beta
public final class LongMath {
    static final int[] BIGGEST_BINOMIALS = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, 265, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66};
    @VisibleForTesting
    static final int[] BIGGEST_SIMPLE_BINOMIALS = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE, 287, 214, 169, 139, 119, LocationRequest.PRIORITY_NO_POWER, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61};
    static final long[] FACTORIALS = new long[]{1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
    @GwtIncompatible("TODO")
    @VisibleForTesting
    static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
    @GwtIncompatible("TODO")
    @VisibleForTesting
    static final long[] HALF_POWERS_OF_10 = new long[]{3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L};
    @VisibleForTesting
    static final byte[] MAX_LOG10_FOR_LEADING_ZEROS = new byte[]{(byte) 19, Ascii.DC2, Ascii.DC2, Ascii.DC2, Ascii.DC2, (byte) 17, (byte) 17, (byte) 17, Ascii.DLE, Ascii.DLE, Ascii.DLE, Ascii.SI, Ascii.SI, Ascii.SI, Ascii.SI, Ascii.SO, Ascii.SO, Ascii.SO, (byte) 13, (byte) 13, (byte) 13, Ascii.FF, Ascii.FF, Ascii.FF, Ascii.FF, Ascii.VT, Ascii.VT, Ascii.VT, (byte) 10, (byte) 10, (byte) 10, (byte) 9, (byte) 9, (byte) 9, (byte) 9, (byte) 8, (byte) 8, (byte) 8, (byte) 7, (byte) 7, (byte) 7, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 5, (byte) 5, (byte) 4, (byte) 4, (byte) 4, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0};
    @VisibleForTesting
    static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
    @GwtIncompatible("TODO")
    @VisibleForTesting
    static final long[] POWERS_OF_10 = new long[]{1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};

    static /* synthetic */ class C08151 {
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

    private LongMath() {
    }

    public static long binomial(int i, int i2) {
        int i3 = 0;
        MathPreconditions.checkNonNegative("n", i);
        MathPreconditions.checkNonNegative("k", i2);
        Preconditions.checkArgument(i2 <= i, "k (%s) > n (%s)", Integer.valueOf(i2), Integer.valueOf(i));
        if (i2 > (i >> 1)) {
            i2 = i - i2;
        }
        if (i2 >= BIGGEST_BINOMIALS.length || i > BIGGEST_BINOMIALS[i2]) {
            return Long.MAX_VALUE;
        }
        long j;
        if (i2 >= BIGGEST_SIMPLE_BINOMIALS.length || i > BIGGEST_SIMPLE_BINOMIALS[i2]) {
            i3 = 1;
            j = 1;
            while (i3 <= i2) {
                int gcd = IntMath.gcd(i, i3);
                j = (j / ((long) (i3 / gcd))) * ((long) (i / gcd));
                i3++;
                i--;
            }
            return j;
        }
        j = 1;
        while (i3 < i2) {
            j = (j * ((long) (i - i3))) / ((long) (i3 + 1));
            i3++;
        }
        return j;
    }

    @GwtIncompatible("TODO")
    public static long checkedAdd(long j, long j2) {
        int i = 0;
        long j3 = j + j2;
        int i2 = (j ^ j2) < 0 ? 1 : 0;
        if ((j ^ j3) >= 0) {
            i = 1;
        }
        MathPreconditions.checkNoOverflow(i | i2);
        return j3;
    }

    @GwtIncompatible("TODO")
    public static long checkedMultiply(long j, long j2) {
        boolean z = false;
        int numberOfLeadingZeros = ((Long.numberOfLeadingZeros(j) + Long.numberOfLeadingZeros(j ^ -1)) + Long.numberOfLeadingZeros(j2)) + Long.numberOfLeadingZeros(j2 ^ -1);
        if (numberOfLeadingZeros > 65) {
            return j * j2;
        }
        MathPreconditions.checkNoOverflow(numberOfLeadingZeros >= 64);
        MathPreconditions.checkNoOverflow((j2 != Long.MIN_VALUE ? 1 : 0) | (j >= 0 ? 1 : 0));
        long j3 = j * j2;
        if (j == 0 || j3 / j == j2) {
            z = true;
        }
        MathPreconditions.checkNoOverflow(z);
        return j3;
    }

    @GwtIncompatible("TODO")
    public static long checkedPow(long j, int i) {
        long j2 = 1;
        boolean z = true;
        boolean z2 = false;
        MathPreconditions.checkNonNegative("exponent", i);
        if (((j <= 2 ? 1 : 0) & (j >= -2 ? 1 : 0)) != 0) {
            switch ((int) j) {
                case -2:
                    if (i < 64) {
                        z2 = true;
                    }
                    MathPreconditions.checkNoOverflow(z2);
                    return (i & 1) == 0 ? 1 << i : -1 << i;
                case -1:
                    if ((i & 1) != 0) {
                        return -1;
                    }
                    break;
                case 0:
                    if (i != 0) {
                        return 0;
                    }
                    break;
                case 1:
                    break;
                case 2:
                    if (i >= 63) {
                        z = false;
                    }
                    MathPreconditions.checkNoOverflow(z);
                    return 1 << i;
            }
            return 1;
        }
        while (true) {
            switch (i) {
                case 0:
                    return j2;
                case 1:
                    return checkedMultiply(j2, j);
                default:
                    if ((i & 1) != 0) {
                        j2 = checkedMultiply(j2, j);
                    }
                    i >>= 1;
                    if (i > 0) {
                        MathPreconditions.checkNoOverflow(j <= FLOOR_SQRT_MAX_LONG);
                        j *= j;
                    }
            }
        }
    }

    @GwtIncompatible("TODO")
    public static long checkedSubtract(long j, long j2) {
        int i = 0;
        long j3 = j - j2;
        int i2 = (j ^ j2) >= 0 ? 1 : 0;
        if ((j ^ j3) >= 0) {
            i = 1;
        }
        MathPreconditions.checkNoOverflow(i | i2);
        return j3;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.common.annotations.GwtIncompatible("TODO")
    public static long divide(long r10, long r12, java.math.RoundingMode r14) {
        /*
        com.google.common.base.Preconditions.checkNotNull(r14);
        r2 = r10 / r12;
        r0 = r12 * r2;
        r0 = r10 - r0;
        r4 = 0;
        r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1));
        if (r4 != 0) goto L_0x0011;
    L_0x000f:
        r0 = r2;
    L_0x0010:
        return r0;
    L_0x0011:
        r4 = r10 ^ r12;
        r6 = 63;
        r4 = r4 >> r6;
        r4 = (int) r4;
        r5 = r4 | 1;
        r4 = com.google.common.math.LongMath.C08151.$SwitchMap$java$math$RoundingMode;
        r6 = r14.ordinal();
        r4 = r4[r6];
        switch(r4) {
            case 1: goto L_0x002a;
            case 2: goto L_0x0034;
            case 3: goto L_0x0044;
            case 4: goto L_0x003c;
            case 5: goto L_0x003e;
            case 6: goto L_0x004a;
            case 7: goto L_0x004a;
            case 8: goto L_0x004a;
            default: goto L_0x0024;
        };
    L_0x0024:
        r0 = new java.lang.AssertionError;
        r0.<init>();
        throw r0;
    L_0x002a:
        r6 = 0;
        r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r0 != 0) goto L_0x003a;
    L_0x0030:
        r0 = 1;
    L_0x0031:
        com.google.common.math.MathPreconditions.checkRoundingUnnecessary(r0);
    L_0x0034:
        r0 = 0;
    L_0x0035:
        if (r0 == 0) goto L_0x000f;
    L_0x0037:
        r0 = (long) r5;
        r0 = r0 + r2;
        goto L_0x0010;
    L_0x003a:
        r0 = 0;
        goto L_0x0031;
    L_0x003c:
        r0 = 1;
        goto L_0x0035;
    L_0x003e:
        if (r5 <= 0) goto L_0x0042;
    L_0x0040:
        r0 = 1;
        goto L_0x0035;
    L_0x0042:
        r0 = 0;
        goto L_0x0035;
    L_0x0044:
        if (r5 >= 0) goto L_0x0048;
    L_0x0046:
        r0 = 1;
        goto L_0x0035;
    L_0x0048:
        r0 = 0;
        goto L_0x0035;
    L_0x004a:
        r0 = java.lang.Math.abs(r0);
        r6 = java.lang.Math.abs(r12);
        r6 = r6 - r0;
        r0 = r0 - r6;
        r6 = 0;
        r4 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r4 != 0) goto L_0x0079;
    L_0x005a:
        r0 = java.math.RoundingMode.HALF_UP;
        if (r14 != r0) goto L_0x0072;
    L_0x005e:
        r0 = 1;
    L_0x005f:
        r1 = java.math.RoundingMode.HALF_EVEN;
        if (r14 != r1) goto L_0x0074;
    L_0x0063:
        r1 = 1;
        r4 = r1;
    L_0x0065:
        r6 = 1;
        r6 = r6 & r2;
        r8 = 0;
        r1 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r1 == 0) goto L_0x0077;
    L_0x006e:
        r1 = 1;
    L_0x006f:
        r1 = r1 & r4;
        r0 = r0 | r1;
        goto L_0x0035;
    L_0x0072:
        r0 = 0;
        goto L_0x005f;
    L_0x0074:
        r1 = 0;
        r4 = r1;
        goto L_0x0065;
    L_0x0077:
        r1 = 0;
        goto L_0x006f;
    L_0x0079:
        r6 = 0;
        r0 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1));
        if (r0 <= 0) goto L_0x0081;
    L_0x007f:
        r0 = 1;
        goto L_0x0035;
    L_0x0081:
        r0 = 0;
        goto L_0x0035;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.LongMath.divide(long, long, java.math.RoundingMode):long");
    }

    @GwtIncompatible("TODO")
    public static long factorial(int i) {
        MathPreconditions.checkNonNegative("n", i);
        return i < FACTORIALS.length ? FACTORIALS[i] : Long.MAX_VALUE;
    }

    @GwtIncompatible("TODO")
    static boolean fitsInInt(long j) {
        return ((long) ((int) j)) == j;
    }

    @GwtIncompatible("TODO")
    public static long gcd(long j, long j2) {
        MathPreconditions.checkNonNegative("a", j);
        MathPreconditions.checkNonNegative("b", j2);
        if (j == 0) {
            return j2;
        }
        if (j2 == 0) {
            return j;
        }
        int numberOfTrailingZeros = Long.numberOfTrailingZeros(j);
        long j3 = j >> numberOfTrailingZeros;
        int numberOfTrailingZeros2 = Long.numberOfTrailingZeros(j2);
        long j4 = j2 >> numberOfTrailingZeros2;
        while (j3 != j4) {
            j3 -= j4;
            long j5 = (j3 >> 63) & j3;
            j3 = (j3 - j5) - j5;
            j4 += j5;
            j3 >>= Long.numberOfTrailingZeros(j3);
        }
        return j3 << Math.min(numberOfTrailingZeros, numberOfTrailingZeros2);
    }

    public static boolean isPowerOfTwo(long j) {
        int i = 0;
        int i2 = j > 0 ? 1 : 0;
        if (((j - 1) & j) == 0) {
            i = 1;
        }
        return i & i2;
    }

    @GwtIncompatible("TODO")
    public static int log10(long j, RoundingMode roundingMode) {
        MathPreconditions.checkPositive("x", j);
        if (fitsInInt(j)) {
            return IntMath.log10((int) j, roundingMode);
        }
        int log10Floor = log10Floor(j);
        long j2 = POWERS_OF_10[log10Floor];
        switch (C08151.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(j == j2);
                return log10Floor;
            case 2:
            case 3:
                return log10Floor;
            case 4:
            case 5:
                return j != j2 ? log10Floor + 1 : log10Floor;
            case 6:
            case 7:
            case 8:
                return j > HALF_POWERS_OF_10[log10Floor] ? log10Floor + 1 : log10Floor;
            default:
                throw new AssertionError();
        }
    }

    @GwtIncompatible("TODO")
    static int log10Floor(long j) {
        byte b = MAX_LOG10_FOR_LEADING_ZEROS[Long.numberOfLeadingZeros(j)];
        return b - ((int) ((j - POWERS_OF_10[b]) >>> 63));
    }

    public static int log2(long j, RoundingMode roundingMode) {
        MathPreconditions.checkPositive("x", j);
        switch (C08151.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(j));
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return 64 - Long.numberOfLeadingZeros(j - 1);
            case 6:
            case 7:
            case 8:
                int numberOfLeadingZeros = Long.numberOfLeadingZeros(j);
                int i = 63 - numberOfLeadingZeros;
                return j > (MAX_POWER_OF_SQRT2_UNSIGNED >>> numberOfLeadingZeros) ? i + 1 : i;
            default:
                throw new AssertionError("impossible");
        }
        return 63 - Long.numberOfLeadingZeros(j);
    }

    @GwtIncompatible("TODO")
    public static int mod(long j, int i) {
        return (int) mod(j, (long) i);
    }

    @GwtIncompatible("TODO")
    public static long mod(long j, long j2) {
        if (j2 <= 0) {
            throw new ArithmeticException("Modulus " + j2 + " must be > 0");
        }
        long j3 = j % j2;
        return j3 >= 0 ? j3 : j3 + j2;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @com.google.common.annotations.GwtIncompatible("TODO")
    public static long pow(long r8, int r10) {
        /*
        r6 = 64;
        r0 = 0;
        r2 = 1;
        r4 = "exponent";
        com.google.common.math.MathPreconditions.checkNonNegative(r4, r10);
        r4 = -2;
        r4 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r4 > 0) goto L_0x001b;
    L_0x0011:
        r4 = 2;
        r4 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1));
        if (r4 > 0) goto L_0x001b;
    L_0x0017:
        r4 = (int) r8;
        switch(r4) {
            case -2: goto L_0x003a;
            case -1: goto L_0x002e;
            case 0: goto L_0x002a;
            case 1: goto L_0x002c;
            case 2: goto L_0x0035;
            default: goto L_0x001b;
        };
    L_0x001b:
        r6 = r2;
        r4 = r8;
    L_0x001d:
        switch(r10) {
            case 0: goto L_0x0047;
            case 1: goto L_0x0049;
            default: goto L_0x0020;
        };
    L_0x0020:
        r0 = r10 & 1;
        if (r0 != 0) goto L_0x004c;
    L_0x0024:
        r0 = r2;
    L_0x0025:
        r6 = r6 * r0;
        r4 = r4 * r4;
        r10 = r10 >> 1;
        goto L_0x001d;
    L_0x002a:
        if (r10 != 0) goto L_0x002d;
    L_0x002c:
        r0 = r2;
    L_0x002d:
        return r0;
    L_0x002e:
        r0 = r10 & 1;
        if (r0 == 0) goto L_0x002c;
    L_0x0032:
        r0 = -1;
        goto L_0x002d;
    L_0x0035:
        if (r10 >= r6) goto L_0x002d;
    L_0x0037:
        r0 = r2 << r10;
        goto L_0x002d;
    L_0x003a:
        if (r10 >= r6) goto L_0x002d;
    L_0x003c:
        r0 = r10 & 1;
        if (r0 != 0) goto L_0x0043;
    L_0x0040:
        r0 = r2 << r10;
        goto L_0x002d;
    L_0x0043:
        r0 = r2 << r10;
        r0 = -r0;
        goto L_0x002d;
    L_0x0047:
        r0 = r6;
        goto L_0x002d;
    L_0x0049:
        r0 = r6 * r4;
        goto L_0x002d;
    L_0x004c:
        r0 = r4;
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.LongMath.pow(long, int):long");
    }

    @GwtIncompatible("TODO")
    public static long sqrt(long j, RoundingMode roundingMode) {
        int i = 0;
        MathPreconditions.checkNonNegative("x", j);
        if (fitsInInt(j)) {
            return (long) IntMath.sqrt((int) j, roundingMode);
        }
        long sqrtFloor = sqrtFloor(j);
        switch (C08151.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                boolean z;
                if (sqrtFloor * sqrtFloor == j) {
                    z = true;
                }
                MathPreconditions.checkRoundingUnnecessary(z);
                return sqrtFloor;
            case 2:
            case 3:
                return sqrtFloor;
            case 4:
            case 5:
                return sqrtFloor * sqrtFloor != j ? sqrtFloor + 1 : sqrtFloor;
            case 6:
            case 7:
            case 8:
                long j2 = (sqrtFloor * sqrtFloor) + sqrtFloor;
                int i2 = j2 >= j ? 1 : 0;
                if (j2 < 0) {
                    i = 1;
                }
                return (i | i2) == 0 ? sqrtFloor + 1 : sqrtFloor;
            default:
                throw new AssertionError();
        }
    }

    @GwtIncompatible("TODO")
    private static long sqrtFloor(long j) {
        long sqrt = (long) Math.sqrt((double) j);
        long j2 = ((j / sqrt) + sqrt) >> 1;
        if (j2 == sqrt) {
            return sqrt;
        }
        while (true) {
            sqrt = ((j / j2) + j2) >> 1;
            if (sqrt >= j2) {
                return j2;
            }
            j2 = sqrt;
        }
    }
}
