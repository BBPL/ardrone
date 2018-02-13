package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import org.mortbay.jetty.HttpVersions;

@GwtCompatible
@Beta
public final class UnsignedInts {
    static final long INT_MASK = 4294967295L;

    enum LexicographicalComparator implements Comparator<int[]> {
        INSTANCE;

        public int compare(int[] iArr, int[] iArr2) {
            int min = Math.min(iArr.length, iArr2.length);
            for (int i = 0; i < min; i++) {
                if (iArr[i] != iArr2[i]) {
                    return UnsignedInts.compare(iArr[i], iArr2[i]);
                }
            }
            return iArr.length - iArr2.length;
        }
    }

    private UnsignedInts() {
    }

    public static int compare(int i, int i2) {
        return Ints.compare(flip(i), flip(i2));
    }

    public static int decode(String str) {
        ParseRequest fromString = ParseRequest.fromString(str);
        try {
            return parseUnsignedInt(fromString.rawValue, fromString.radix);
        } catch (Throwable e) {
            NumberFormatException numberFormatException = new NumberFormatException("Error parsing value: " + str);
            numberFormatException.initCause(e);
            throw numberFormatException;
        }
    }

    public static int divide(int i, int i2) {
        return (int) (toLong(i) / toLong(i2));
    }

    static int flip(int i) {
        return Integer.MIN_VALUE ^ i;
    }

    public static String join(String str, int... iArr) {
        Preconditions.checkNotNull(str);
        if (iArr.length == 0) {
            return HttpVersions.HTTP_0_9;
        }
        StringBuilder stringBuilder = new StringBuilder(iArr.length * 5);
        stringBuilder.append(toString(iArr[0]));
        for (int i = 1; i < iArr.length; i++) {
            stringBuilder.append(str).append(toString(iArr[i]));
        }
        return stringBuilder.toString();
    }

    public static Comparator<int[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static int max(int... iArr) {
        int i = 1;
        Preconditions.checkArgument(iArr.length > 0);
        int flip = flip(iArr[0]);
        while (i < iArr.length) {
            int flip2 = flip(iArr[i]);
            if (flip2 > flip) {
                flip = flip2;
            }
            i++;
        }
        return flip(flip);
    }

    public static int min(int... iArr) {
        int i = 1;
        Preconditions.checkArgument(iArr.length > 0);
        int flip = flip(iArr[0]);
        while (i < iArr.length) {
            int flip2 = flip(iArr[i]);
            if (flip2 < flip) {
                flip = flip2;
            }
            i++;
        }
        return flip(flip);
    }

    public static int parseUnsignedInt(String str) {
        return parseUnsignedInt(str, 10);
    }

    public static int parseUnsignedInt(String str, int i) {
        Preconditions.checkNotNull(str);
        long parseLong = Long.parseLong(str, i);
        if ((INT_MASK & parseLong) == parseLong) {
            return (int) parseLong;
        }
        throw new NumberFormatException("Input " + str + " in base " + i + " is not in the range of an unsigned integer");
    }

    public static int remainder(int i, int i2) {
        return (int) (toLong(i) % toLong(i2));
    }

    public static long toLong(int i) {
        return ((long) i) & INT_MASK;
    }

    public static String toString(int i) {
        return toString(i, 10);
    }

    public static String toString(int i, int i2) {
        return Long.toString(((long) i) & INT_MASK, i2);
    }
}
