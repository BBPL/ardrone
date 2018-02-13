package com.google.common.math;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.math.RoundingMode;

@GwtCompatible(emulated = true)
@Beta
public final class IntMath {
    @VisibleForTesting
    static int[] BIGGEST_BINOMIALS = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED, 2345, 477, 193, 110, 75, 58, 49, 43, 39, 37, 35, 34, 34, 33};
    static final int[] FACTORIALS = new int[]{1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600};
    @VisibleForTesting
    static final int FLOOR_SQRT_MAX_INT = 46340;
    @VisibleForTesting
    static final int[] HALF_POWERS_OF_10 = new int[]{3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, Integer.MAX_VALUE};
    @VisibleForTesting
    static final byte[] MAX_LOG10_FOR_LEADING_ZEROS = new byte[]{(byte) 9, (byte) 9, (byte) 9, (byte) 8, (byte) 8, (byte) 8, (byte) 7, (byte) 7, (byte) 7, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 5, (byte) 5, (byte) 4, (byte) 4, (byte) 4, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
    @VisibleForTesting
    static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;
    @VisibleForTesting
    static final int[] POWERS_OF_10 = new int[]{1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

    static /* synthetic */ class C08141 {
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

    private IntMath() {
    }

    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int binomial(int i, int i2) {
        int i3 = 1;
        int i4 = 0;
        MathPreconditions.checkNonNegative("n", i);
        MathPreconditions.checkNonNegative("k", i2);
        Preconditions.checkArgument(i2 <= i, "k (%s) > n (%s)", Integer.valueOf(i2), Integer.valueOf(i));
        if (i2 > (i >> 1)) {
            i2 = i - i2;
        }
        if (i2 >= BIGGEST_BINOMIALS.length || i > BIGGEST_BINOMIALS[i2]) {
            i3 = Integer.MAX_VALUE;
        } else {
            switch (i2) {
                case 0:
                    break;
                case 1:
                    return i;
                default:
                    long j = 1;
                    while (i4 < i2) {
                        j = (j * ((long) (i - i4))) / ((long) (i4 + 1));
                        i4++;
                    }
                    return (int) j;
            }
        }
        return i3;
    }

    public static int checkedAdd(int i, int i2) {
        long j = ((long) i2) + ((long) i);
        MathPreconditions.checkNoOverflow(j == ((long) ((int) j)));
        return (int) j;
    }

    public static int checkedMultiply(int i, int i2) {
        long j = ((long) i2) * ((long) i);
        MathPreconditions.checkNoOverflow(j == ((long) ((int) j)));
        return (int) j;
    }

    public static int checkedPow(int i, int i2) {
        boolean z = false;
        MathPreconditions.checkNonNegative("exponent", i2);
        switch (i) {
            case -2:
                if (i2 < 32) {
                    z = true;
                }
                MathPreconditions.checkNoOverflow(z);
                return (i2 & 1) == 0 ? 1 << i2 : -1 << i2;
            case -1:
                if ((i2 & 1) != 0) {
                    return -1;
                }
                break;
            case 0:
                if (i2 != 0) {
                    return 0;
                }
                break;
            case 1:
                break;
            case 2:
                if (i2 < 31) {
                    z = true;
                }
                MathPreconditions.checkNoOverflow(z);
                return 1 << i2;
            default:
                int i3 = 1;
                while (true) {
                    switch (i2) {
                        case 0:
                            return i3;
                        case 1:
                            return checkedMultiply(i3, i);
                        default:
                            int checkedMultiply = (i2 & 1) != 0 ? checkedMultiply(i3, i) : i3;
                            i2 >>= 1;
                            if (i2 > 0) {
                                MathPreconditions.checkNoOverflow((i <= FLOOR_SQRT_MAX_INT ? 1 : 0) & (-46340 <= i ? 1 : 0));
                                i *= i;
                                i3 = checkedMultiply;
                            } else {
                                i3 = checkedMultiply;
                            }
                    }
                }
        }
        return 1;
    }

    public static int checkedSubtract(int i, int i2) {
        long j = ((long) i) - ((long) i2);
        MathPreconditions.checkNoOverflow(j == ((long) ((int) j)));
        return (int) j;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int divide(int r7, int r8, java.math.RoundingMode r9) {
        /*
        r0 = 1;
        r1 = 0;
        com.google.common.base.Preconditions.checkNotNull(r9);
        if (r8 != 0) goto L_0x000f;
    L_0x0007:
        r0 = new java.lang.ArithmeticException;
        r1 = "/ by zero";
        r0.<init>(r1);
        throw r0;
    L_0x000f:
        r2 = r7 / r8;
        r3 = r8 * r2;
        r3 = r7 - r3;
        if (r3 != 0) goto L_0x0019;
    L_0x0017:
        r0 = r2;
    L_0x0018:
        return r0;
    L_0x0019:
        r4 = r7 ^ r8;
        r4 = r4 >> 31;
        r5 = r4 | 1;
        r4 = com.google.common.math.IntMath.C08141.$SwitchMap$java$math$RoundingMode;
        r6 = r9.ordinal();
        r4 = r4[r6];
        switch(r4) {
            case 1: goto L_0x0030;
            case 2: goto L_0x0035;
            case 3: goto L_0x0041;
            case 4: goto L_0x0036;
            case 5: goto L_0x003d;
            case 6: goto L_0x0045;
            case 7: goto L_0x0045;
            case 8: goto L_0x0045;
            default: goto L_0x002a;
        };
    L_0x002a:
        r0 = new java.lang.AssertionError;
        r0.<init>();
        throw r0;
    L_0x0030:
        if (r3 != 0) goto L_0x003b;
    L_0x0032:
        com.google.common.math.MathPreconditions.checkRoundingUnnecessary(r0);
    L_0x0035:
        r0 = r1;
    L_0x0036:
        if (r0 == 0) goto L_0x0017;
    L_0x0038:
        r0 = r2 + r5;
        goto L_0x0018;
    L_0x003b:
        r0 = r1;
        goto L_0x0032;
    L_0x003d:
        if (r5 > 0) goto L_0x0036;
    L_0x003f:
        r0 = r1;
        goto L_0x0036;
    L_0x0041:
        if (r5 < 0) goto L_0x0036;
    L_0x0043:
        r0 = r1;
        goto L_0x0036;
    L_0x0045:
        r3 = java.lang.Math.abs(r3);
        r4 = java.lang.Math.abs(r8);
        r4 = r4 - r3;
        r3 = r3 - r4;
        if (r3 != 0) goto L_0x0069;
    L_0x0051:
        r3 = java.math.RoundingMode.HALF_UP;
        if (r9 == r3) goto L_0x0062;
    L_0x0055:
        r3 = java.math.RoundingMode.HALF_EVEN;
        if (r9 != r3) goto L_0x0065;
    L_0x0059:
        r4 = r0;
    L_0x005a:
        r3 = r2 & 1;
        if (r3 == 0) goto L_0x0067;
    L_0x005e:
        r3 = r0;
    L_0x005f:
        r3 = r3 & r4;
        if (r3 == 0) goto L_0x0063;
    L_0x0062:
        r1 = r0;
    L_0x0063:
        r0 = r1;
        goto L_0x0036;
    L_0x0065:
        r4 = r1;
        goto L_0x005a;
    L_0x0067:
        r3 = r1;
        goto L_0x005f;
    L_0x0069:
        if (r3 > 0) goto L_0x0036;
    L_0x006b:
        r0 = r1;
        goto L_0x0036;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.math.IntMath.divide(int, int, java.math.RoundingMode):int");
    }

    public static int factorial(int i) {
        MathPreconditions.checkNonNegative("n", i);
        return i < FACTORIALS.length ? FACTORIALS[i] : Integer.MAX_VALUE;
    }

    public static int gcd(int i, int i2) {
        MathPreconditions.checkNonNegative("a", i);
        MathPreconditions.checkNonNegative("b", i2);
        if (i == 0) {
            return i2;
        }
        if (i2 == 0) {
            return i;
        }
        int numberOfTrailingZeros = Integer.numberOfTrailingZeros(i);
        int i3 = i >> numberOfTrailingZeros;
        int numberOfTrailingZeros2 = Integer.numberOfTrailingZeros(i2);
        int i4 = i2 >> numberOfTrailingZeros2;
        while (i3 != i4) {
            i3 -= i4;
            int i5 = (i3 >> 31) & i3;
            i3 = (i3 - i5) - i5;
            i4 += i5;
            i3 >>= Integer.numberOfTrailingZeros(i3);
        }
        return i3 << Math.min(numberOfTrailingZeros, numberOfTrailingZeros2);
    }

    public static boolean isPowerOfTwo(int i) {
        int i2 = 1;
        int i3 = i > 0 ? 1 : 0;
        if (((i - 1) & i) != 0) {
            i2 = 0;
        }
        return i2 & i3;
    }

    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int log10(int i, RoundingMode roundingMode) {
        MathPreconditions.checkPositive("x", i);
        int log10Floor = log10Floor(i);
        int i2 = POWERS_OF_10[log10Floor];
        switch (C08141.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(i == i2);
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                if (i != i2) {
                    return log10Floor + 1;
                }
                break;
            case 6:
            case 7:
            case 8:
                if (i > HALF_POWERS_OF_10[log10Floor]) {
                    return log10Floor + 1;
                }
                break;
            default:
                throw new AssertionError();
        }
        return log10Floor;
    }

    private static int log10Floor(int i) {
        byte b = MAX_LOG10_FOR_LEADING_ZEROS[Integer.numberOfLeadingZeros(i)];
        return b - ((i - POWERS_OF_10[b]) >>> 31);
    }

    public static int log2(int i, RoundingMode roundingMode) {
        MathPreconditions.checkPositive("x", i);
        switch (C08141.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(i));
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                return 32 - Integer.numberOfLeadingZeros(i - 1);
            case 6:
            case 7:
            case 8:
                int numberOfLeadingZeros = Integer.numberOfLeadingZeros(i);
                int i2 = 31 - numberOfLeadingZeros;
                return i > (MAX_POWER_OF_SQRT2_UNSIGNED >>> numberOfLeadingZeros) ? i2 + 1 : i2;
            default:
                throw new AssertionError();
        }
        return 31 - Integer.numberOfLeadingZeros(i);
    }

    public static int mod(int i, int i2) {
        if (i2 <= 0) {
            throw new ArithmeticException("Modulus " + i2 + " must be > 0");
        }
        int i3 = i % i2;
        return i3 >= 0 ? i3 : i3 + i2;
    }

    @GwtIncompatible("failing tests")
    public static int pow(int i, int i2) {
        MathPreconditions.checkNonNegative("exponent", i2);
        switch (i) {
            case -2:
                return i2 < 32 ? (i2 & 1) == 0 ? 1 << i2 : -(1 << i2) : 0;
            case -1:
                if ((i2 & 1) != 0) {
                    return -1;
                }
                break;
            case 0:
                if (i2 != 0) {
                    return 0;
                }
                break;
            case 1:
                break;
            case 2:
                return i2 < 32 ? 1 << i2 : 0;
            default:
                int i3 = 1;
                int i4 = i;
                while (true) {
                    switch (i2) {
                        case 0:
                            return i3;
                        case 1:
                            return i4 * i3;
                        default:
                            i3 *= (i2 & 1) == 0 ? 1 : i4;
                            i4 *= i4;
                            i2 >>= 1;
                    }
                }
        }
        return 1;
    }

    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int sqrt(int i, RoundingMode roundingMode) {
        int i2 = 0;
        MathPreconditions.checkNonNegative("x", i);
        int sqrtFloor = sqrtFloor(i);
        switch (C08141.$SwitchMap$java$math$RoundingMode[roundingMode.ordinal()]) {
            case 1:
                boolean z;
                if (sqrtFloor * sqrtFloor == i) {
                    z = true;
                }
                MathPreconditions.checkRoundingUnnecessary(z);
                break;
            case 2:
            case 3:
                break;
            case 4:
            case 5:
                if (sqrtFloor * sqrtFloor != i) {
                    return sqrtFloor + 1;
                }
                break;
            case 6:
            case 7:
            case 8:
                int i3 = (sqrtFloor * sqrtFloor) + sqrtFloor;
                int i4 = i <= i3 ? 1 : 0;
                if (i3 < 0) {
                    i2 = 1;
                }
                if ((i2 | i4) == 0) {
                    return sqrtFloor + 1;
                }
                break;
            default:
                throw new AssertionError();
        }
        return sqrtFloor;
    }

    private static int sqrtFloor(int i) {
        return (int) Math.sqrt((double) i);
    }
}
