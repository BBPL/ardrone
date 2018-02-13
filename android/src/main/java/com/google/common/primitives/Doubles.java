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
public final class Doubles {
    public static final int BYTES = 8;

    @GwtCompatible
    private static class DoubleArrayAsList extends AbstractList<Double> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final double[] array;
        final int end;
        final int start;

        DoubleArrayAsList(double[] dArr) {
            this(dArr, 0, dArr.length);
        }

        DoubleArrayAsList(double[] dArr, int i, int i2) {
            this.array = dArr;
            this.start = i;
            this.end = i2;
        }

        public boolean contains(Object obj) {
            return (obj instanceof Double) && Doubles.indexOf(this.array, ((Double) obj).doubleValue(), this.start, this.end) != -1;
        }

        public boolean equals(Object obj) {
            if (obj != this) {
                if (!(obj instanceof DoubleArrayAsList)) {
                    return super.equals(obj);
                }
                DoubleArrayAsList doubleArrayAsList = (DoubleArrayAsList) obj;
                int size = size();
                if (doubleArrayAsList.size() != size) {
                    return false;
                }
                for (int i = 0; i < size; i++) {
                    if (this.array[this.start + i] != doubleArrayAsList.array[doubleArrayAsList.start + i]) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Double get(int i) {
            Preconditions.checkElementIndex(i, size());
            return Double.valueOf(this.array[this.start + i]);
        }

        public int hashCode() {
            int i = 1;
            for (int i2 = this.start; i2 < this.end; i2++) {
                i = (i * 31) + Doubles.hashCode(this.array[i2]);
            }
            return i;
        }

        public int indexOf(Object obj) {
            if (obj instanceof Double) {
                int access$000 = Doubles.indexOf(this.array, ((Double) obj).doubleValue(), this.start, this.end);
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
            if (obj instanceof Double) {
                int access$100 = Doubles.lastIndexOf(this.array, ((Double) obj).doubleValue(), this.start, this.end);
                if (access$100 >= 0) {
                    return access$100 - this.start;
                }
            }
            return -1;
        }

        public Double set(int i, Double d) {
            Preconditions.checkElementIndex(i, size());
            double d2 = this.array[this.start + i];
            this.array[this.start + i] = ((Double) Preconditions.checkNotNull(d)).doubleValue();
            return Double.valueOf(d2);
        }

        public int size() {
            return this.end - this.start;
        }

        public List<Double> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, size());
            return i == i2 ? Collections.emptyList() : new DoubleArrayAsList(this.array, this.start + i, this.start + i2);
        }

        double[] toDoubleArray() {
            int size = size();
            Object obj = new double[size];
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

    private enum LexicographicalComparator implements Comparator<double[]> {
        INSTANCE;

        public int compare(double[] dArr, double[] dArr2) {
            int min = Math.min(dArr.length, dArr2.length);
            for (int i = 0; i < min; i++) {
                int compare = Doubles.compare(dArr[i], dArr2[i]);
                if (compare != 0) {
                    return compare;
                }
            }
            return dArr.length - dArr2.length;
        }
    }

    private Doubles() {
    }

    public static List<Double> asList(double... dArr) {
        return dArr.length == 0 ? Collections.emptyList() : new DoubleArrayAsList(dArr);
    }

    public static int compare(double d, double d2) {
        return Double.compare(d, d2);
    }

    public static double[] concat(double[]... dArr) {
        int i = 0;
        for (double[] length : dArr) {
            i += length.length;
        }
        Object obj = new double[i];
        i = 0;
        for (Object obj2 : dArr) {
            System.arraycopy(obj2, 0, obj, i, obj2.length);
            i += obj2.length;
        }
        return obj;
    }

    public static boolean contains(double[] dArr, double d) {
        for (double d2 : dArr) {
            if (d2 == d) {
                return true;
            }
        }
        return false;
    }

    private static double[] copyOf(double[] dArr, int i) {
        Object obj = new double[i];
        System.arraycopy(dArr, 0, obj, 0, Math.min(dArr.length, i));
        return obj;
    }

    public static double[] ensureCapacity(double[] dArr, int i, int i2) {
        Preconditions.checkArgument(i >= 0, "Invalid minLength: %s", Integer.valueOf(i));
        Preconditions.checkArgument(i2 >= 0, "Invalid padding: %s", Integer.valueOf(i2));
        return dArr.length < i ? copyOf(dArr, i + i2) : dArr;
    }

    public static int hashCode(double d) {
        return Double.valueOf(d).hashCode();
    }

    public static int indexOf(double[] dArr, double d) {
        return indexOf(dArr, d, 0, dArr.length);
    }

    private static int indexOf(double[] dArr, double d, int i, int i2) {
        for (int i3 = i; i3 < i2; i3++) {
            if (dArr[i3] == d) {
                return i3;
            }
        }
        return -1;
    }

    public static int indexOf(double[] dArr, double[] dArr2) {
        Preconditions.checkNotNull(dArr, "array");
        Preconditions.checkNotNull(dArr2, "target");
        if (dArr2.length == 0) {
            return 0;
        }
        int i = 0;
        while (i < (dArr.length - dArr2.length) + 1) {
            int i2 = 0;
            while (i2 < dArr2.length) {
                if (dArr[i + i2] != dArr2[i2]) {
                    i++;
                } else {
                    i2++;
                }
            }
            return i;
        }
        return -1;
    }

    public static boolean isFinite(double d) {
        int i = 0;
        int i2 = Double.NEGATIVE_INFINITY < d ? 1 : 0;
        if (d < Double.POSITIVE_INFINITY) {
            i = 1;
        }
        return i & i2;
    }

    public static String join(String str, double... dArr) {
        Preconditions.checkNotNull(str);
        if (dArr.length == 0) {
            return HttpVersions.HTTP_0_9;
        }
        StringBuilder stringBuilder = new StringBuilder(dArr.length * 12);
        stringBuilder.append(dArr[0]);
        for (int i = 1; i < dArr.length; i++) {
            stringBuilder.append(str).append(dArr[i]);
        }
        return stringBuilder.toString();
    }

    public static int lastIndexOf(double[] dArr, double d) {
        return lastIndexOf(dArr, d, 0, dArr.length);
    }

    private static int lastIndexOf(double[] dArr, double d, int i, int i2) {
        for (int i3 = i2 - 1; i3 >= i; i3--) {
            if (dArr[i3] == d) {
                return i3;
            }
        }
        return -1;
    }

    public static Comparator<double[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    public static double max(double... dArr) {
        int i = 1;
        Preconditions.checkArgument(dArr.length > 0);
        double d = dArr[0];
        while (i < dArr.length) {
            d = Math.max(d, dArr[i]);
            i++;
        }
        return d;
    }

    public static double min(double... dArr) {
        int i = 1;
        Preconditions.checkArgument(dArr.length > 0);
        double d = dArr[0];
        while (i < dArr.length) {
            d = Math.min(d, dArr[i]);
            i++;
        }
        return d;
    }

    public static double[] toArray(Collection<? extends Number> collection) {
        if (collection instanceof DoubleArrayAsList) {
            return ((DoubleArrayAsList) collection).toDoubleArray();
        }
        Object[] toArray = collection.toArray();
        int length = toArray.length;
        double[] dArr = new double[length];
        for (int i = 0; i < length; i++) {
            dArr[i] = ((Number) Preconditions.checkNotNull(toArray[i])).doubleValue();
        }
        return dArr;
    }
}
