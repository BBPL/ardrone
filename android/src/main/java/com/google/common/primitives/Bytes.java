package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

@GwtCompatible
public final class Bytes {

    @GwtCompatible
    private static class ByteArrayAsList extends AbstractList<Byte> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final byte[] array;
        final int end;
        final int start;

        ByteArrayAsList(byte[] bArr) {
            this(bArr, 0, bArr.length);
        }

        ByteArrayAsList(byte[] bArr, int i, int i2) {
            this.array = bArr;
            this.start = i;
            this.end = i2;
        }

        public boolean contains(Object obj) {
            return (obj instanceof Byte) && Bytes.indexOf(this.array, ((Byte) obj).byteValue(), this.start, this.end) != -1;
        }

        public boolean equals(Object obj) {
            if (obj != this) {
                if (!(obj instanceof ByteArrayAsList)) {
                    return super.equals(obj);
                }
                ByteArrayAsList byteArrayAsList = (ByteArrayAsList) obj;
                int size = size();
                if (byteArrayAsList.size() != size) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (this.array[this.start + i] != byteArrayAsList.array[byteArrayAsList.start + i]) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Byte get(int i) {
            Preconditions.checkElementIndex(i, size());
            return Byte.valueOf(this.array[this.start + i]);
        }

        public int hashCode() {
            int i = 1;
            for (int i2 = this.start; i2 < this.end; i2++) {
                i = (i * 31) + Bytes.hashCode(this.array[i2]);
            }
            return i;
        }

        public int indexOf(Object obj) {
            if (obj instanceof Byte) {
                int access$000 = Bytes.indexOf(this.array, ((Byte) obj).byteValue(), this.start, this.end);
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
            if (obj instanceof Byte) {
                int access$100 = Bytes.lastIndexOf(this.array, ((Byte) obj).byteValue(), this.start, this.end);
                if (access$100 >= 0) {
                    return access$100 - this.start;
                }
            }
            return -1;
        }

        public Byte set(int i, Byte b) {
            Preconditions.checkElementIndex(i, size());
            byte b2 = this.array[this.start + i];
            this.array[this.start + i] = ((Byte) Preconditions.checkNotNull(b)).byteValue();
            return Byte.valueOf(b2);
        }

        public int size() {
            return this.end - this.start;
        }

        public List<Byte> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, size());
            return i == i2 ? Collections.emptyList() : new ByteArrayAsList(this.array, this.start + i, this.start + i2);
        }

        byte[] toByteArray() {
            int size = size();
            Object obj = new byte[size];
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

    private Bytes() {
    }

    public static List<Byte> asList(byte... bArr) {
        return bArr.length == 0 ? Collections.emptyList() : new ByteArrayAsList(bArr);
    }

    public static byte[] concat(byte[]... bArr) {
        int i = 0;
        for (byte[] length : bArr) {
            i += length.length;
        }
        Object obj = new byte[i];
        i = 0;
        for (Object obj2 : bArr) {
            System.arraycopy(obj2, 0, obj, i, obj2.length);
            i += obj2.length;
        }
        return obj;
    }

    public static boolean contains(byte[] bArr, byte b) {
        for (byte b2 : bArr) {
            if (b2 == b) {
                return true;
            }
        }
        return false;
    }

    private static byte[] copyOf(byte[] bArr, int i) {
        Object obj = new byte[i];
        System.arraycopy(bArr, 0, obj, 0, Math.min(bArr.length, i));
        return obj;
    }

    public static byte[] ensureCapacity(byte[] bArr, int i, int i2) {
        Preconditions.checkArgument(i >= 0, "Invalid minLength: %s", Integer.valueOf(i));
        Preconditions.checkArgument(i2 >= 0, "Invalid padding: %s", Integer.valueOf(i2));
        return bArr.length < i ? copyOf(bArr, i + i2) : bArr;
    }

    public static int hashCode(byte b) {
        return b;
    }

    public static int indexOf(byte[] bArr, byte b) {
        return indexOf(bArr, b, 0, bArr.length);
    }

    private static int indexOf(byte[] bArr, byte b, int i, int i2) {
        for (int i3 = i; i3 < i2; i3++) {
            if (bArr[i3] == b) {
                return i3;
            }
        }
        return -1;
    }

    public static int indexOf(byte[] bArr, byte[] bArr2) {
        Preconditions.checkNotNull(bArr, "array");
        Preconditions.checkNotNull(bArr2, "target");
        if (bArr2.length == 0) {
            return 0;
        }
        int i = 0;
        while (i < (bArr.length - bArr2.length) + 1) {
            int i2 = 0;
            while (i2 < bArr2.length) {
                if (bArr[i + i2] != bArr2[i2]) {
                    i++;
                } else {
                    i2++;
                }
            }
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(byte[] bArr, byte b) {
        return lastIndexOf(bArr, b, 0, bArr.length);
    }

    private static int lastIndexOf(byte[] bArr, byte b, int i, int i2) {
        for (int i3 = i2 - 1; i3 >= i; i3--) {
            if (bArr[i3] == b) {
                return i3;
            }
        }
        return -1;
    }

    public static byte[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof ByteArrayAsList) {
            return ((ByteArrayAsList) collection).toByteArray();
        }
        Object[] toArray = collection.toArray();
        int length = toArray.length;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = ((Number) Preconditions.checkNotNull(toArray[i])).byteValue();
        }
        return bArr;
    }
}
