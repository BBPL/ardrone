package com.google.common.primitives;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Comparator;
import org.mortbay.jetty.HttpVersions;
import sun.misc.Unsafe;

public final class UnsignedBytes {
    public static final byte MAX_POWER_OF_TWO = Byte.MIN_VALUE;
    public static final byte MAX_VALUE = (byte) -1;
    private static final int UNSIGNED_MASK = 255;

    @VisibleForTesting
    static class LexicographicalComparatorHolder {
        static final Comparator<byte[]> BEST_COMPARATOR = getBestComparator();
        static final String UNSAFE_COMPARATOR_NAME = (LexicographicalComparatorHolder.class.getName() + "$UnsafeComparator");

        enum PureJavaComparator implements Comparator<byte[]> {
            INSTANCE;

            public int compare(byte[] bArr, byte[] bArr2) {
                int min = Math.min(bArr.length, bArr2.length);
                for (int i = 0; i < min; i++) {
                    int compare = UnsignedBytes.compare(bArr[i], bArr2[i]);
                    if (compare != 0) {
                        return compare;
                    }
                }
                return bArr.length - bArr2.length;
            }
        }

        @VisibleForTesting
        enum UnsafeComparator implements Comparator<byte[]> {
            INSTANCE;
            
            static final int BYTE_ARRAY_BASE_OFFSET = 0;
            static final boolean littleEndian = false;
            static final Unsafe theUnsafe = null;

            static final class C08181 implements PrivilegedAction<Object> {
                C08181() {
                }

                public Object run() {
                    try {
                        Field declaredField = Unsafe.class.getDeclaredField("theUnsafe");
                        declaredField.setAccessible(true);
                        return declaredField.get(null);
                    } catch (NoSuchFieldException e) {
                        throw new Error();
                    } catch (IllegalAccessException e2) {
                        throw new Error();
                    }
                }
            }

            static {
                littleEndian = ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN);
                theUnsafe = (Unsafe) AccessController.doPrivileged(new C08181());
                BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
                if (theUnsafe.arrayIndexScale(byte[].class) != 1) {
                    throw new AssertionError();
                }
            }

            public int compare(byte[] bArr, byte[] bArr2) {
                int min = Math.min(bArr.length, bArr2.length);
                int i = min / 8;
                int i2 = 0;
                while (i2 < i * 8) {
                    long j = theUnsafe.getLong(bArr, ((long) BYTE_ARRAY_BASE_OFFSET) + ((long) i2));
                    long j2 = theUnsafe.getLong(bArr2, ((long) BYTE_ARRAY_BASE_OFFSET) + ((long) i2));
                    long j3 = j ^ j2;
                    if (j3 == 0) {
                        i2 += 8;
                    } else if (!littleEndian) {
                        return UnsignedLongs.compare(j, j2);
                    } else {
                        long j4 = 0;
                        i = (int) j3;
                        if (i == 0) {
                            i = (int) (j3 >>> 32);
                            j4 = 32;
                        }
                        min = i << 16;
                        if (min == 0) {
                            j4 += 16;
                        } else {
                            i = min;
                        }
                        if ((i << 8) == 0) {
                            j4 += 8;
                        }
                        return (int) (((j >>> j4) & 255) - ((j2 >>> j4) & 255));
                    }
                }
                for (i *= 8; i < min; i++) {
                    i2 = UnsignedBytes.compare(bArr[i], bArr2[i]);
                    if (i2 != 0) {
                        return i2;
                    }
                }
                return bArr.length - bArr2.length;
            }
        }

        LexicographicalComparatorHolder() {
        }

        static Comparator<byte[]> getBestComparator() {
            try {
                return (Comparator) Class.forName(UNSAFE_COMPARATOR_NAME).getEnumConstants()[0];
            } catch (Throwable th) {
                return UnsignedBytes.lexicographicalComparatorJavaImpl();
            }
        }
    }

    private UnsignedBytes() {
    }

    public static byte checkedCast(long j) {
        Preconditions.checkArgument((j >> 8) == 0, "out of range: %s", Long.valueOf(j));
        return (byte) ((int) j);
    }

    public static int compare(byte b, byte b2) {
        return toInt(b) - toInt(b2);
    }

    public static String join(String str, byte... bArr) {
        Preconditions.checkNotNull(str);
        if (bArr.length == 0) {
            return HttpVersions.HTTP_0_9;
        }
        StringBuilder stringBuilder = new StringBuilder(bArr.length * (str.length() + 3));
        stringBuilder.append(toInt(bArr[0]));
        for (int i = 1; i < bArr.length; i++) {
            stringBuilder.append(str).append(toString(bArr[i]));
        }
        return stringBuilder.toString();
    }

    public static Comparator<byte[]> lexicographicalComparator() {
        return LexicographicalComparatorHolder.BEST_COMPARATOR;
    }

    @VisibleForTesting
    static Comparator<byte[]> lexicographicalComparatorJavaImpl() {
        return PureJavaComparator.INSTANCE;
    }

    public static byte max(byte... bArr) {
        int i = 1;
        Preconditions.checkArgument(bArr.length > 0);
        int toInt = toInt(bArr[0]);
        while (i < bArr.length) {
            int toInt2 = toInt(bArr[i]);
            if (toInt2 > toInt) {
                toInt = toInt2;
            }
            i++;
        }
        return (byte) toInt;
    }

    public static byte min(byte... bArr) {
        int i = 1;
        Preconditions.checkArgument(bArr.length > 0);
        int toInt = toInt(bArr[0]);
        while (i < bArr.length) {
            int toInt2 = toInt(bArr[i]);
            if (toInt2 < toInt) {
                toInt = toInt2;
            }
            i++;
        }
        return (byte) toInt;
    }

    @Beta
    public static byte parseUnsignedByte(String str) {
        return parseUnsignedByte(str, 10);
    }

    @Beta
    public static byte parseUnsignedByte(String str, int i) {
        int parseInt = Integer.parseInt((String) Preconditions.checkNotNull(str), i);
        if ((parseInt >> 8) == 0) {
            return (byte) parseInt;
        }
        throw new NumberFormatException("out of range: " + parseInt);
    }

    public static byte saturatedCast(long j) {
        return j > ((long) toInt((byte) -1)) ? (byte) -1 : j < 0 ? (byte) 0 : (byte) ((int) j);
    }

    public static int toInt(byte b) {
        return b & 255;
    }

    @Beta
    public static String toString(byte b) {
        return toString(b, 10);
    }

    @Beta
    public static String toString(byte b, int i) {
        boolean z = i >= 2 && i <= 36;
        Preconditions.checkArgument(z, "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", Integer.valueOf(i));
        return Integer.toString(toInt(b), i);
    }
}
