package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import org.mortbay.jetty.HttpVersions;

@GwtCompatible(emulated = true)
public final class Shorts {
    public static final int BYTES = 2;
    public static final short MAX_POWER_OF_TWO = (short) 16384;

    private enum LexicographicalComparator implements Comparator<short[]> {
        INSTANCE;

        public int compare(short[] sArr, short[] sArr2) {
            int min = Math.min(sArr.length, sArr2.length);
            for (int i = 0; i < min; i++) {
                int compare = Shorts.compare(sArr[i], sArr2[i]);
                if (compare != 0) {
                    return compare;
                }
            }
            return sArr.length - sArr2.length;
        }
    }

    @GwtCompatible
    private static class ShortArrayAsList extends AbstractList<Short> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final short[] array;
        final int end;
        final int start;

        ShortArrayAsList(short[] sArr) {
            this(sArr, 0, sArr.length);
        }

        ShortArrayAsList(short[] sArr, int i, int i2) {
            this.array = sArr;
            this.start = i;
            this.end = i2;
        }

        public boolean contains(Object obj) {
            return (obj instanceof Short) && Shorts.indexOf(this.array, ((Short) obj).shortValue(), this.start, this.end) != -1;
        }

        public boolean equals(Object obj) {
            if (obj != this) {
                if (!(obj instanceof ShortArrayAsList)) {
                    return super.equals(obj);
                }
                ShortArrayAsList shortArrayAsList = (ShortArrayAsList) obj;
                int size = size();
                if (shortArrayAsList.size() != size) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (this.array[this.start + i] != shortArrayAsList.array[shortArrayAsList.start + i]) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Short get(int i) {
            Preconditions.checkElementIndex(i, size());
            return Short.valueOf(this.array[this.start + i]);
        }

        public int hashCode() {
            int i = 1;
            for (int i2 = this.start; i2 < this.end; i2++) {
                i = (i * 31) + Shorts.hashCode(this.array[i2]);
            }
            return i;
        }

        public int indexOf(Object obj) {
            if (obj instanceof Short) {
                int access$000 = Shorts.indexOf(this.array, ((Short) obj).shortValue(), this.start, this.end);
                if (access$000 >= 0) {
                    return access$000 - this.start;
                }
            }
            return -1;
        }

        public boolean isEmpty() {
            return false;
        }

        public int lastIndexOf(Object obj) {
            if (obj instanceof Short) {
                int access$100 = Shorts.lastIndexOf(this.array, ((Short) obj).shortValue(), this.start, this.end);
                if (access$100 >= 0) {
                    return access$100 - this.start;
                }
            }
            return -1;
        }

        public Short set(int i, Short sh) {
            Preconditions.checkElementIndex(i, size());
            short s = this.array[this.start + i];
            this.array[this.start + i] = ((Short) Preconditions.checkNotNull(sh)).shortValue();
            return Short.valueOf(s);
        }

        public int size() {
            return this.end - this.start;
        }

        public List<Short> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, size());
            return i == i2 ? Collections.emptyList() : new ShortArrayAsList(this.array, this.start + i, this.start + i2);
        }

        short[] toShortArray() {
            int size = size();
            Object obj = new short[size];
            System.arraycopy(this.array, this.start, obj, 0, size);
            return obj;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(size() * 6);
            stringBuilder.append('[').append(this.array[this.start]);
            for (int i = this.start + 1; i < this.end; i++) {
                stringBuilder.append(", ").append(this.array[i]);
            }
            return stringBuilder.append(']').toString();
        }
    }

    private Shorts() {
    }

    public static List<Short> asList(short... sArr) {
        return sArr.length == 0 ? Collections.emptyList() : new ShortArrayAsList(sArr);
    }

    public static short checkedCast(long j) {
        short s = (short) ((int) j);
        Preconditions.checkArgument(((long) s) == j, "Out of range: %s", Long.valueOf(j));
        return s;
    }

    public static int compare(short s, short s2) {
        return s - s2;
    }

    public static short[] concat(short[]... sArr) {
        int i = 0;
        for (short[] length : sArr) {
            i += length.length;
        }
        Object obj = new short[i];
        i = 0;
        for (Object obj2 : sArr) {
            System.arraycopy(obj2, 0, obj, i, obj2.length);
            i += obj2.length;
        }
        return obj;
    }

    public static boolean contains(short[] sArr, short s) {
        for (short s2 : sArr) {
            if (s2 == s) {
                return true;
            }
        }
        return false;
    }

    private static short[] copyOf(short[] sArr, int i) {
        Object obj = new short[i];
        System.arraycopy(sArr, 0, obj, 0, Math.min(sArr.length, i));
        return obj;
    }

    public static short[] ensureCapacity(short[] sArr, int i, int i2) {
        Preconditions.checkArgument(i >= 0, "Invalid minLength: %s", Integer.valueOf(i));
        Preconditions.checkArgument(i2 >= 0, "Invalid padding: %s", Integer.valueOf(i2));
        return sArr.length < i ? copyOf(sArr, i + i2) : sArr;
    }

    @GwtIncompatible("doesn't work")
    public static short fromByteArray(byte[] bArr) {
        Preconditions.checkArgument(bArr.length >= 2, "array too small: %s < %s", Integer.valueOf(bArr.length), Integer.valueOf(2));
        return fromBytes(bArr[0], bArr[1]);
    }

    @GwtIncompatible("doesn't work")
    public static short fromBytes(byte b, byte b2) {
        return (short) ((b << 8) | (b2 & 255));
    }

    public static int hashCode(short s) {
        return s;
    }

    public static int indexOf(short[] sArr, short s) {
        return indexOf(sArr, s, 0, sArr.length);
    }

    private static int indexOf(short[] sArr, short s, int i, int i2) {
        for (int i3 = i; i3 < i2; i3++) {
            if (sArr[i3] == s) {
                return i3;
            }
        }
        return -1;
    }

    public static int indexOf(short[] sArr, short[] sArr2) {
        Preconditions.checkNotNull(sArr, "array");
        Preconditions.checkNotNull(sArr2, "target");
        if (sArr2.length == 0) {
            return 0;
        }
        int i = 0;
        while (i < (sArr.length - sArr2.length) + 1) {
            int i2 = 0;
            while (i2 < sArr2.length) {
                if (sArr[i + i2] != sArr2[i2]) {
                    i++;
                } else {
                    i2++;
                }
            }
            return i;
        }
        return -1;
    }

    public static String join(String str, short... sArr) {
        Preconditions.checkNotNull(str);
        if (sArr.length == 0) {
            return HttpVersions.HTTP_0_9;
        }
        StringBuilder stringBuilder = new StringBuilder(sArr.length * 6);
        stringBuilder.append(sArr[0]);
        for (int i = 1; i < sArr.length; i++) {
            stringBuilder.append(str).append(sArr[i]);
        }
        return stringBuilder.toString();
    }

    public static int lastIndexOf(short[] sArr, short s) {
        return lastIndexOf(sArr, s, 0, sArr.length);
    }

    private static int lastIndexOf(short[] sArr, short s, int i, int i2) {
        for (int i3 = i2 - 1; i3 >= i; i3--) {
            if (sArr[i3] == s) {
                return i3;
            }
        }
        return -1;
    }

    public static Comparator<short[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static short max(short... sArr) {
        int i = 1;
        Preconditions.checkArgument(sArr.length > 0);
        short s = sArr[0];
        while (i < sArr.length) {
            if (sArr[i] > s) {
                s = sArr[i];
            }
            i++;
        }
        return s;
    }

    public static short min(short... sArr) {
        int i = 1;
        Preconditions.checkArgument(sArr.length > 0);
        short s = sArr[0];
        while (i < sArr.length) {
            if (sArr[i] < s) {
                s = sArr[i];
            }
            i++;
        }
        return s;
    }

    public static short saturatedCast(long j) {
        return j > 32767 ? Short.MAX_VALUE : j < -32768 ? Short.MIN_VALUE : (short) ((int) j);
    }

    public static short[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof ShortArrayAsList) {
            return ((ShortArrayAsList) collection).toShortArray();
        }
        Object[] toArray = collection.toArray();
        int length = toArray.length;
        short[] sArr = new short[length];
        for (int i = 0; i < length; i++) {
            sArr[i] = ((Number) Preconditions.checkNotNull(toArray[i])).shortValue();
        }
        return sArr;
    }

    @GwtIncompatible("doesn't work")
    public static byte[] toByteArray(short s) {
        return new byte[]{(byte) (s >> 8), (byte) s};
    }
}
