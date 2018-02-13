package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import org.mortbay.jetty.HttpVersions;

@GwtCompatible
public final class Floats {
    public static final int BYTES = 4;

    @GwtCompatible
    private static class FloatArrayAsList extends AbstractList<Float> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final float[] array;
        final int end;
        final int start;

        FloatArrayAsList(float[] fArr) {
            this(fArr, 0, fArr.length);
        }

        FloatArrayAsList(float[] fArr, int i, int i2) {
            this.array = fArr;
            this.start = i;
            this.end = i2;
        }

        public boolean contains(Object obj) {
            return (obj instanceof Float) && Floats.indexOf(this.array, ((Float) obj).floatValue(), this.start, this.end) != -1;
        }

        public boolean equals(Object obj) {
            if (obj != this) {
                if (!(obj instanceof FloatArrayAsList)) {
                    return super.equals(obj);
                }
                FloatArrayAsList floatArrayAsList = (FloatArrayAsList) obj;
                int size = size();
                if (floatArrayAsList.size() != size) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (this.array[this.start + i] != floatArrayAsList.array[floatArrayAsList.start + i]) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Float get(int i) {
            Preconditions.checkElementIndex(i, size());
            return Float.valueOf(this.array[this.start + i]);
        }

        public int hashCode() {
            int i = 1;
            for (int i2 = this.start; i2 < this.end; i2++) {
                i = (i * 31) + Floats.hashCode(this.array[i2]);
            }
            return i;
        }

        public int indexOf(Object obj) {
            if (obj instanceof Float) {
                int access$000 = Floats.indexOf(this.array, ((Float) obj).floatValue(), this.start, this.end);
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
            if (obj instanceof Float) {
                int access$100 = Floats.lastIndexOf(this.array, ((Float) obj).floatValue(), this.start, this.end);
                if (access$100 >= 0) {
                    return access$100 - this.start;
                }
            }
            return -1;
        }

        public Float set(int i, Float f) {
            Preconditions.checkElementIndex(i, size());
            float f2 = this.array[this.start + i];
            this.array[this.start + i] = ((Float) Preconditions.checkNotNull(f)).floatValue();
            return Float.valueOf(f2);
        }

        public int size() {
            return this.end - this.start;
        }

        public List<Float> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, size());
            return i == i2 ? Collections.emptyList() : new FloatArrayAsList(this.array, this.start + i, this.start + i2);
        }

        float[] toFloatArray() {
            int size = size();
            Object obj = new float[size];
            System.arraycopy(this.array, this.start, obj, 0, size);
            return obj;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(size() * 12);
            stringBuilder.append('[').append(this.array[this.start]);
            for (int i = this.start + 1; i < this.end; i++) {
                stringBuilder.append(", ").append(this.array[i]);
            }
            return stringBuilder.append(']').toString();
        }
    }

    private enum LexicographicalComparator implements Comparator<float[]> {
        INSTANCE;

        public int compare(float[] fArr, float[] fArr2) {
            int min = Math.min(fArr.length, fArr2.length);
            for (int i = 0; i < min; i++) {
                int compare = Floats.compare(fArr[i], fArr2[i]);
                if (compare != 0) {
                    return compare;
                }
            }
            return fArr.length - fArr2.length;
        }
    }

    private Floats() {
    }

    public static List<Float> asList(float... fArr) {
        return fArr.length == 0 ? Collections.emptyList() : new FloatArrayAsList(fArr);
    }

    public static int compare(float f, float f2) {
        return Float.compare(f, f2);
    }

    public static float[] concat(float[]... fArr) {
        int i = 0;
        for (float[] length : fArr) {
            i += length.length;
        }
        Object obj = new float[i];
        i = 0;
        for (Object obj2 : fArr) {
            System.arraycopy(obj2, 0, obj, i, obj2.length);
            i += obj2.length;
        }
        return obj;
    }

    public static boolean contains(float[] fArr, float f) {
        for (float f2 : fArr) {
            if (f2 == f) {
                return true;
            }
        }
        return false;
    }

    private static float[] copyOf(float[] fArr, int i) {
        Object obj = new float[i];
        System.arraycopy(fArr, 0, obj, 0, Math.min(fArr.length, i));
        return obj;
    }

    public static float[] ensureCapacity(float[] fArr, int i, int i2) {
        Preconditions.checkArgument(i >= 0, "Invalid minLength: %s", Integer.valueOf(i));
        Preconditions.checkArgument(i2 >= 0, "Invalid padding: %s", Integer.valueOf(i2));
        return fArr.length < i ? copyOf(fArr, i + i2) : fArr;
    }

    public static int hashCode(float f) {
        return Float.valueOf(f).hashCode();
    }

    public static int indexOf(float[] fArr, float f) {
        return indexOf(fArr, f, 0, fArr.length);
    }

    private static int indexOf(float[] fArr, float f, int i, int i2) {
        for (int i3 = i; i3 < i2; i3++) {
            if (fArr[i3] == f) {
                return i3;
            }
        }
        return -1;
    }

    public static int indexOf(float[] fArr, float[] fArr2) {
        Preconditions.checkNotNull(fArr, "array");
        Preconditions.checkNotNull(fArr2, "target");
        if (fArr2.length == 0) {
            return 0;
        }
        int i = 0;
        while (i < (fArr.length - fArr2.length) + 1) {
            int i2 = 0;
            while (i2 < fArr2.length) {
                if (fArr[i + i2] != fArr2[i2]) {
                    i++;
                } else {
                    i2++;
                }
            }
            return i;
        }
        return -1;
    }

    public static boolean isFinite(float f) {
        int i = 0;
        int i2 = Float.NEGATIVE_INFINITY < f ? 1 : 0;
        if (f < Float.POSITIVE_INFINITY) {
            i = 1;
        }
        return i & i2;
    }

    public static String join(String str, float... fArr) {
        Preconditions.checkNotNull(str);
        if (fArr.length == 0) {
            return HttpVersions.HTTP_0_9;
        }
        StringBuilder stringBuilder = new StringBuilder(fArr.length * 12);
        stringBuilder.append(fArr[0]);
        for (int i = 1; i < fArr.length; i++) {
            stringBuilder.append(str).append(fArr[i]);
        }
        return stringBuilder.toString();
    }

    public static int lastIndexOf(float[] fArr, float f) {
        return lastIndexOf(fArr, f, 0, fArr.length);
    }

    private static int lastIndexOf(float[] fArr, float f, int i, int i2) {
        for (int i3 = i2 - 1; i3 >= i; i3--) {
            if (fArr[i3] == f) {
                return i3;
            }
        }
        return -1;
    }

    public static Comparator<float[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static float max(float... fArr) {
        int i = 1;
        Preconditions.checkArgument(fArr.length > 0);
        float f = fArr[0];
        while (i < fArr.length) {
            f = Math.max(f, fArr[i]);
            i++;
        }
        return f;
    }

    public static float min(float... fArr) {
        int i = 1;
        Preconditions.checkArgument(fArr.length > 0);
        float f = fArr[0];
        while (i < fArr.length) {
            f = Math.min(f, fArr[i]);
            i++;
        }
        return f;
    }

    public static float[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof FloatArrayAsList) {
            return ((FloatArrayAsList) collection).toFloatArray();
        }
        Object[] toArray = collection.toArray();
        int length = toArray.length;
        float[] fArr = new float[length];
        for (int i = 0; i < length; i++) {
            fArr[i] = ((Number) Preconditions.checkNotNull(toArray[i])).floatValue();
        }
        return fArr;
    }
}
