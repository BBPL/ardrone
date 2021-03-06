package android.support.v4.util;

public class SparseArrayCompat<E> {
    private static final Object DELETED = new Object();
    private boolean mGarbage;
    private int[] mKeys;
    private int mSize;
    private Object[] mValues;

    public SparseArrayCompat() {
        this(10);
    }

    public SparseArrayCompat(int i) {
        this.mGarbage = false;
        int idealIntArraySize = idealIntArraySize(i);
        this.mKeys = new int[idealIntArraySize];
        this.mValues = new Object[idealIntArraySize];
        this.mSize = 0;
    }

    private static int binarySearch(int[] iArr, int i, int i2, int i3) {
        int i4 = i - 1;
        int i5 = i + i2;
        while (i5 - i4 > 1) {
            int i6 = (i5 + i4) / 2;
            if (iArr[i6] < i3) {
                i4 = i6;
            } else {
                i5 = i6;
            }
        }
        return i5 == i + i2 ? (i + i2) ^ -1 : iArr[i5] != i3 ? i5 ^ -1 : i5;
    }

    private void gc() {
        int i = this.mSize;
        int[] iArr = this.mKeys;
        Object[] objArr = this.mValues;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            Object obj = objArr[i3];
            if (obj != DELETED) {
                if (i3 != i2) {
                    iArr[i2] = iArr[i3];
                    objArr[i2] = obj;
                }
                i2++;
            }
        }
        this.mGarbage = false;
        this.mSize = i2;
    }

    static int idealByteArraySize(int i) {
        for (int i2 = 4; i2 < 32; i2++) {
            if (i <= (1 << i2) - 12) {
                return (1 << i2) - 12;
            }
        }
        return i;
    }

    static int idealIntArraySize(int i) {
        return idealByteArraySize(i * 4) / 4;
    }

    public void append(int i, E e) {
        if (this.mSize == 0 || i > this.mKeys[this.mSize - 1]) {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                gc();
            }
            int i2 = this.mSize;
            if (i2 >= this.mKeys.length) {
                int idealIntArraySize = idealIntArraySize(i2 + 1);
                Object obj = new int[idealIntArraySize];
                Object obj2 = new Object[idealIntArraySize];
                System.arraycopy(this.mKeys, 0, obj, 0, this.mKeys.length);
                System.arraycopy(this.mValues, 0, obj2, 0, this.mValues.length);
                this.mKeys = obj;
                this.mValues = obj2;
            }
            this.mKeys[i2] = i;
            this.mValues[i2] = e;
            this.mSize = i2 + 1;
            return;
        }
        put(i, e);
    }

    public void clear() {
        int i = this.mSize;
        Object[] objArr = this.mValues;
        for (int i2 = 0; i2 < i; i2++) {
            objArr[i2] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public void delete(int i) {
        int binarySearch = binarySearch(this.mKeys, 0, this.mSize, i);
        if (binarySearch >= 0 && this.mValues[binarySearch] != DELETED) {
            this.mValues[binarySearch] = DELETED;
            this.mGarbage = true;
        }
    }

    public E get(int i) {
        return get(i, null);
    }

    public E get(int i, E e) {
        int binarySearch = binarySearch(this.mKeys, 0, this.mSize, i);
        return (binarySearch < 0 || this.mValues[binarySearch] == DELETED) ? e : this.mValues[binarySearch];
    }

    public int indexOfKey(int i) {
        if (this.mGarbage) {
            gc();
        }
        return binarySearch(this.mKeys, 0, this.mSize, i);
    }

    public int indexOfValue(E e) {
        if (this.mGarbage) {
            gc();
        }
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == e) {
                return i;
            }
        }
        return -1;
    }

    public int keyAt(int i) {
        if (this.mGarbage) {
            gc();
        }
        return this.mKeys[i];
    }

    public void put(int i, E e) {
        int binarySearch = binarySearch(this.mKeys, 0, this.mSize, i);
        if (binarySearch >= 0) {
            this.mValues[binarySearch] = e;
            return;
        }
        binarySearch ^= -1;
        if (binarySearch >= this.mSize || this.mValues[binarySearch] != DELETED) {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                gc();
                binarySearch = binarySearch(this.mKeys, 0, this.mSize, i) ^ -1;
            }
            if (this.mSize >= this.mKeys.length) {
                int idealIntArraySize = idealIntArraySize(this.mSize + 1);
                Object obj = new int[idealIntArraySize];
                Object obj2 = new Object[idealIntArraySize];
                System.arraycopy(this.mKeys, 0, obj, 0, this.mKeys.length);
                System.arraycopy(this.mValues, 0, obj2, 0, this.mValues.length);
                this.mKeys = obj;
                this.mValues = obj2;
            }
            if (this.mSize - binarySearch != 0) {
                System.arraycopy(this.mKeys, binarySearch, this.mKeys, binarySearch + 1, this.mSize - binarySearch);
                System.arraycopy(this.mValues, binarySearch, this.mValues, binarySearch + 1, this.mSize - binarySearch);
            }
            this.mKeys[binarySearch] = i;
            this.mValues[binarySearch] = e;
            this.mSize++;
            return;
        }
        this.mKeys[binarySearch] = i;
        this.mValues[binarySearch] = e;
    }

    public void remove(int i) {
        delete(i);
    }

    public void removeAt(int i) {
        if (this.mValues[i] != DELETED) {
            this.mValues[i] = DELETED;
            this.mGarbage = true;
        }
    }

    public void removeAtRange(int i, int i2) {
        int min = Math.min(this.mSize, i + i2);
        while (i < min) {
            removeAt(i);
            i++;
        }
    }

    public void setValueAt(int i, E e) {
        if (this.mGarbage) {
            gc();
        }
        this.mValues[i] = e;
    }

    public int size() {
        if (this.mGarbage) {
            gc();
        }
        return this.mSize;
    }

    public E valueAt(int i) {
        if (this.mGarbage) {
            gc();
        }
        return this.mValues[i];
    }
}
