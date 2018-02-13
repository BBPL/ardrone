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
public final class Chars {
    public static final int BYTES = 2;

    @GwtCompatible
    private static class CharArrayAsList extends AbstractList<Character> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final char[] array;
        final int end;
        final int start;

        CharArrayAsList(char[] cArr) {
            this(cArr, 0, cArr.length);
        }

        CharArrayAsList(char[] cArr, int i, int i2) {
            this.array = cArr;
            this.start = i;
            this.end = i2;
        }

        public boolean contains(Object obj) {
            return (obj instanceof Character) && Chars.indexOf(this.array, ((Character) obj).charValue(), this.start, this.end) != -1;
        }

        public boolean equals(Object obj) {
            if (obj != this) {
                if (!(obj instanceof CharArrayAsList)) {
                    return super.equals(obj);
                }
                CharArrayAsList charArrayAsList = (CharArrayAsList) obj;
                int size = size();
                if (charArrayAsList.size() != size) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (this.array[this.start + i] != charArrayAsList.array[charArrayAsList.start + i]) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Character get(int i) {
            Preconditions.checkElementIndex(i, size());
            return Character.valueOf(this.array[this.start + i]);
        }

        public int hashCode() {
            int i = 1;
            for (int i2 = this.start; i2 < this.end; i2++) {
                i = (i * 31) + Chars.hashCode(this.array[i2]);
            }
            return i;
        }

        public int indexOf(Object obj) {
            if (obj instanceof Character) {
                int access$000 = Chars.indexOf(this.array, ((Character) obj).charValue(), this.start, this.end);
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
            if (obj instanceof Character) {
                int access$100 = Chars.lastIndexOf(this.array, ((Character) obj).charValue(), this.start, this.end);
                if (access$100 >= 0) {
                    return access$100 - this.start;
                }
            }
            return -1;
        }

        public Character set(int i, Character ch) {
            Preconditions.checkElementIndex(i, size());
            char c = this.array[this.start + i];
            this.array[this.start + i] = ((Character) Preconditions.checkNotNull(ch)).charValue();
            return Character.valueOf(c);
        }

        public int size() {
            return this.end - this.start;
        }

        public List<Character> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, size());
            return i == i2 ? Collections.emptyList() : new CharArrayAsList(this.array, this.start + i, this.start + i2);
        }

        char[] toCharArray() {
            int size = size();
            Object obj = new char[size];
            System.arraycopy(this.array, this.start, obj, 0, size);
            return obj;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(size() * 3);
            stringBuilder.append('[').append(this.array[this.start]);
            for (int i = this.start + 1; i < this.end; i++) {
                stringBuilder.append(", ").append(this.array[i]);
            }
            return stringBuilder.append(']').toString();
        }
    }

    private enum LexicographicalComparator implements Comparator<char[]> {
        INSTANCE;

        public int compare(char[] cArr, char[] cArr2) {
            int min = Math.min(cArr.length, cArr2.length);
            for (int i = 0; i < min; i++) {
                int compare = Chars.compare(cArr[i], cArr2[i]);
                if (compare != 0) {
                    return compare;
                }
            }
            return cArr.length - cArr2.length;
        }
    }

    private Chars() {
    }

    public static List<Character> asList(char... cArr) {
        return cArr.length == 0 ? Collections.emptyList() : new CharArrayAsList(cArr);
    }

    public static char checkedCast(long j) {
        char c = (char) ((int) j);
        Preconditions.checkArgument(((long) c) == j, "Out of range: %s", Long.valueOf(j));
        return c;
    }

    public static int compare(char c, char c2) {
        return c - c2;
    }

    public static char[] concat(char[]... cArr) {
        int i = 0;
        for (char[] length : cArr) {
            i += length.length;
        }
        Object obj = new char[i];
        i = 0;
        for (Object obj2 : cArr) {
            System.arraycopy(obj2, 0, obj, i, obj2.length);
            i += obj2.length;
        }
        return obj;
    }

    public static boolean contains(char[] cArr, char c) {
        for (char c2 : cArr) {
            if (c2 == c) {
                return true;
            }
        }
        return false;
    }

    private static char[] copyOf(char[] cArr, int i) {
        Object obj = new char[i];
        System.arraycopy(cArr, 0, obj, 0, Math.min(cArr.length, i));
        return obj;
    }

    public static char[] ensureCapacity(char[] cArr, int i, int i2) {
        Preconditions.checkArgument(i >= 0, "Invalid minLength: %s", Integer.valueOf(i));
        Preconditions.checkArgument(i2 >= 0, "Invalid padding: %s", Integer.valueOf(i2));
        return cArr.length < i ? copyOf(cArr, i + i2) : cArr;
    }

    @GwtIncompatible("doesn't work")
    public static char fromByteArray(byte[] bArr) {
        Preconditions.checkArgument(bArr.length >= 2, "array too small: %s < %s", Integer.valueOf(bArr.length), Integer.valueOf(2));
        return fromBytes(bArr[0], bArr[1]);
    }

    @GwtIncompatible("doesn't work")
    public static char fromBytes(byte b, byte b2) {
        return (char) ((b << 8) | (b2 & 255));
    }

    public static int hashCode(char c) {
        return c;
    }

    public static int indexOf(char[] cArr, char c) {
        return indexOf(cArr, c, 0, cArr.length);
    }

    private static int indexOf(char[] cArr, char c, int i, int i2) {
        for (int i3 = i; i3 < i2; i3++) {
            if (cArr[i3] == c) {
                return i3;
            }
        }
        return -1;
    }

    public static int indexOf(char[] cArr, char[] cArr2) {
        Preconditions.checkNotNull(cArr, "array");
        Preconditions.checkNotNull(cArr2, "target");
        if (cArr2.length == 0) {
            return 0;
        }
        int i = 0;
        while (i < (cArr.length - cArr2.length) + 1) {
            int i2 = 0;
            while (i2 < cArr2.length) {
                if (cArr[i + i2] != cArr2[i2]) {
                    i++;
                } else {
                    i2++;
                }
            }
            return i;
        }
        return -1;
    }

    public static String join(String str, char... cArr) {
        Preconditions.checkNotNull(str);
        int length = cArr.length;
        if (length == 0) {
            return HttpVersions.HTTP_0_9;
        }
        StringBuilder stringBuilder = new StringBuilder((str.length() * (length - 1)) + length);
        stringBuilder.append(cArr[0]);
        for (int i = 1; i < length; i++) {
            stringBuilder.append(str).append(cArr[i]);
        }
        return stringBuilder.toString();
    }

    public static int lastIndexOf(char[] cArr, char c) {
        return lastIndexOf(cArr, c, 0, cArr.length);
    }

    private static int lastIndexOf(char[] cArr, char c, int i, int i2) {
        for (int i3 = i2 - 1; i3 >= i; i3--) {
            if (cArr[i3] == c) {
                return i3;
            }
        }
        return -1;
    }

    public static Comparator<char[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static char max(char... cArr) {
        int i = 1;
        Preconditions.checkArgument(cArr.length > 0);
        char c = cArr[0];
        while (i < cArr.length) {
            if (cArr[i] > c) {
                c = cArr[i];
            }
            i++;
        }
        return c;
    }

    public static char min(char... cArr) {
        int i = 1;
        Preconditions.checkArgument(cArr.length > 0);
        char c = cArr[0];
        while (i < cArr.length) {
            if (cArr[i] < c) {
                c = cArr[i];
            }
            i++;
        }
        return c;
    }

    public static char saturatedCast(long j) {
        return j > 65535 ? '￿' : j < 0 ? '\u0000' : (char) ((int) j);
    }

    public static char[] toArray(Collection<Character> collection) {
        if (collection instanceof CharArrayAsList) {
            return ((CharArrayAsList) collection).toCharArray();
        }
        Object[] toArray = collection.toArray();
        int length = toArray.length;
        char[] cArr = new char[length];
        for (int i = 0; i < length; i++) {
            cArr[i] = ((Character) Preconditions.checkNotNull(toArray[i])).charValue();
        }
        return cArr;
    }

    @GwtIncompatible("doesn't work")
    public static byte[] toByteArray(char c) {
        return new byte[]{(byte) (c >> 8), (byte) c};
    }
}
