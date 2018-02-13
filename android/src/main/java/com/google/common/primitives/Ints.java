package com.google.common.primitives;

import com.google.common.annotations.Beta;
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
import javax.annotation.CheckForNull;
import org.mortbay.jetty.HttpVersions;

@GwtCompatible(emulated = true)
public final class Ints {
    public static final int BYTES = 4;
    public static final int MAX_POWER_OF_TWO = 1073741824;

    @GwtCompatible
    private static class IntArrayAsList extends AbstractList<Integer> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final int[] array;
        final int end;
        final int start;

        IntArrayAsList(int[] iArr) {
            this(iArr, 0, iArr.length);
        }

        IntArrayAsList(int[] iArr, int i, int i2) {
            this.array = iArr;
            this.start = i;
            this.end = i2;
        }

        public boolean contains(Object obj) {
            return (obj instanceof Integer) && Ints.indexOf(this.array, ((Integer) obj).intValue(), this.start, this.end) != -1;
        }

        public boolean equals(Object obj) {
            if (obj != this) {
                if (!(obj instanceof IntArrayAsList)) {
                    return super.equals(obj);
                }
                IntArrayAsList intArrayAsList = (IntArrayAsList) obj;
                int size = size();
                if (intArrayAsList.size() != size) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (this.array[this.start + i] != intArrayAsList.array[intArrayAsList.start + i]) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Integer get(int i) {
            Preconditions.checkElementIndex(i, size());
            return Integer.valueOf(this.array[this.start + i]);
        }

        public int hashCode() {
            int i = 1;
            for (int i2 = this.start; i2 < this.end; i2++) {
                i = (i * 31) + Ints.hashCode(this.array[i2]);
            }
            return i;
        }

        public int indexOf(Object obj) {
            if (obj instanceof Integer) {
                int access$000 = Ints.indexOf(this.array, ((Integer) obj).intValue(), this.start, this.end);
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
            if (obj instanceof Integer) {
                int access$100 = Ints.lastIndexOf(this.array, ((Integer) obj).intValue(), this.start, this.end);
                if (access$100 >= 0) {
                    return access$100 - this.start;
                }
            }
            return -1;
        }

        public Integer set(int i, Integer num) {
            Preconditions.checkElementIndex(i, size());
            int i2 = this.array[this.start + i];
            this.array[this.start + i] = ((Integer) Preconditions.checkNotNull(num)).intValue();
            return Integer.valueOf(i2);
        }

        public int size() {
            return this.end - this.start;
        }

        public List<Integer> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, size());
            return i == i2 ? Collections.emptyList() : new IntArrayAsList(this.array, this.start + i, this.start + i2);
        }

        int[] toIntArray() {
            int size = size();
            Object obj = new int[size];
            System.arraycopy(this.array, this.start, obj, 0, size);
            return obj;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(size() * 5);
            stringBuilder.append('[').append(this.array[this.start]);
            for (int i = this.start + 1; i < this.end; i++) {
                stringBuilder.append(", ").append(this.array[i]);
            }
            return stringBuilder.append(']').toString();
        }
    }

    private enum LexicographicalComparator implements Comparator<int[]> {
        INSTANCE;

        public int compare(int[] iArr, int[] iArr2) {
            int min = Math.min(iArr.length, iArr2.length);
            for (int i = 0; i < min; i++) {
                int compare = Ints.compare(iArr[i], iArr2[i]);
                if (compare != 0) {
                    return compare;
                }
            }
            return iArr.length - iArr2.length;
        }
    }

    private Ints() {
    }

    public static List<Integer> asList(int... iArr) {
        return iArr.length == 0 ? Collections.emptyList() : new IntArrayAsList(iArr);
    }

    public static int checkedCast(long j) {
        int i = (int) j;
        Preconditions.checkArgument(((long) i) == j, "Out of range: %s", Long.valueOf(j));
        return i;
    }

    public static int compare(int i, int i2) {
        return i < i2 ? -1 : i > i2 ? 1 : 0;
    }

    public static int[] concat(int[]... iArr) {
        int i = 0;
        for (int[] length : iArr) {
            i += length.length;
        }
        Object obj = new int[i];
        i = 0;
        for (Object obj2 : iArr) {
            System.arraycopy(obj2, 0, obj, i, obj2.length);
            i += obj2.length;
        }
        return obj;
    }

    public static boolean contains(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private static int[] copyOf(int[] iArr, int i) {
        Object obj = new int[i];
        System.arraycopy(iArr, 0, obj, 0, Math.min(iArr.length, i));
        return obj;
    }

    public static int[] ensureCapacity(int[] iArr, int i, int i2) {
        Preconditions.checkArgument(i >= 0, "Invalid minLength: %s", Integer.valueOf(i));
        Preconditions.checkArgument(i2 >= 0, "Invalid padding: %s", Integer.valueOf(i2));
        return iArr.length < i ? copyOf(iArr, i + i2) : iArr;
    }

    @GwtIncompatible("doesn't work")
    public static int fromByteArray(byte[] bArr) {
        Preconditions.checkArgument(bArr.length >= 4, "array too small: %s < %s", Integer.valueOf(bArr.length), Integer.valueOf(4));
        return fromBytes(bArr[0], bArr[1], bArr[2], bArr[3]);
    }

    @GwtIncompatible("doesn't work")
    public static int fromBytes(byte b, byte b2, byte b3, byte b4) {
        return (((b << 24) | ((b2 & 255) << 16)) | ((b3 & 255) << 8)) | (b4 & 255);
    }

    public static int hashCode(int i) {
        return i;
    }

    public static int indexOf(int[] iArr, int i) {
        return indexOf(iArr, i, 0, iArr.length);
    }

    private static int indexOf(int[] iArr, int i, int i2, int i3) {
        for (int i4 = i2; i4 < i3; i4++) {
            if (iArr[i4] == i) {
                return i4;
            }
        }
        return -1;
    }

    public static int indexOf(int[] iArr, int[] iArr2) {
        Preconditions.checkNotNull(iArr, "array");
        Preconditions.checkNotNull(iArr2, "target");
        if (iArr2.length == 0) {
            return 0;
        }
        int i = 0;
        while (i < (iArr.length - iArr2.length) + 1) {
            int i2 = 0;
            while (i2 < iArr2.length) {
                if (iArr[i + i2] != iArr2[i2]) {
                    i++;
                } else {
                    i2++;
                }
            }
            return i;
        }
        return -1;
    }

    public static String join(String str, int... iArr) {
        Preconditions.checkNotNull(str);
        if (iArr.length == 0) {
            return HttpVersions.HTTP_0_9;
        }
        StringBuilder stringBuilder = new StringBuilder(iArr.length * 5);
        stringBuilder.append(iArr[0]);
        for (int i = 1; i < iArr.length; i++) {
            stringBuilder.append(str).append(iArr[i]);
        }
        return stringBuilder.toString();
    }

    public static int lastIndexOf(int[] iArr, int i) {
        return lastIndexOf(iArr, i, 0, iArr.length);
    }

    private static int lastIndexOf(int[] iArr, int i, int i2, int i3) {
        for (int i4 = i3 - 1; i4 >= i2; i4--) {
            if (iArr[i4] == i) {
                return i4;
            }
        }
        return -1;
    }

    public static Comparator<int[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static int max(int... iArr) {
        int i = 1;
        Preconditions.checkArgument(iArr.length > 0);
        int i2 = iArr[0];
        while (i < iArr.length) {
            if (iArr[i] > i2) {
                i2 = iArr[i];
            }
            i++;
        }
        return i2;
    }

    public static int min(int... iArr) {
        int i = 1;
        Preconditions.checkArgument(iArr.length > 0);
        int i2 = iArr[0];
        while (i < iArr.length) {
            if (iArr[i] < i2) {
                i2 = iArr[i];
            }
            i++;
        }
        return i2;
    }

    public static int saturatedCast(long j) {
        return j > 2147483647L ? Integer.MAX_VALUE : j < -2147483648L ? Integer.MIN_VALUE : (int) j;
    }

    public static int[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof IntArrayAsList) {
            return ((IntArrayAsList) collection).toIntArray();
        }
        Object[] toArray = collection.toArray();
        int length = toArray.length;
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            iArr[i] = ((Number) Preconditions.checkNotNull(toArray[i])).intValue();
        }
        return iArr;
    }

    @GwtIncompatible("doesn't work")
    public static byte[] toByteArray(int i) {
        return new byte[]{(byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) i};
    }

    @CheckForNull
    @GwtIncompatible("TODO")
    @Beta
    public static Integer tryParse(String str) {
        return AndroidInteger.tryParse(str, 10);
    }
}
