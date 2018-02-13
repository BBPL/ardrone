package com.google.api.client.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class ArrayMap<K, V> extends AbstractMap<K, V> implements Cloneable {
    private Object[] data;
    int size;

    final class Entry implements java.util.Map.Entry<K, V> {
        private int index;

        Entry(int i) {
            this.index = i;
        }

        public boolean equals(Object obj) {
            if (this != obj) {
                if (!(obj instanceof java.util.Map.Entry)) {
                    return false;
                }
                java.util.Map.Entry entry = (java.util.Map.Entry) obj;
                if (!Objects.equal(getKey(), entry.getKey())) {
                    return false;
                }
                if (!Objects.equal(getValue(), entry.getValue())) {
                    return false;
                }
            }
            return true;
        }

        public K getKey() {
            return ArrayMap.this.getKey(this.index);
        }

        public V getValue() {
            return ArrayMap.this.getValue(this.index);
        }

        public int hashCode() {
            return getKey().hashCode() ^ getValue().hashCode();
        }

        public V setValue(V v) {
            return ArrayMap.this.set(this.index, v);
        }
    }

    final class EntryIterator implements Iterator<java.util.Map.Entry<K, V>> {
        private int nextIndex;
        private boolean removed;

        EntryIterator() {
        }

        public boolean hasNext() {
            return this.nextIndex < ArrayMap.this.size;
        }

        public java.util.Map.Entry<K, V> next() {
            int i = this.nextIndex;
            if (i == ArrayMap.this.size) {
                throw new NoSuchElementException();
            }
            this.nextIndex++;
            return new Entry(i);
        }

        public void remove() {
            int i = this.nextIndex - 1;
            if (this.removed || i < 0) {
                throw new IllegalArgumentException();
            }
            ArrayMap.this.remove(i);
            this.removed = true;
        }
    }

    final class EntrySet extends AbstractSet<java.util.Map.Entry<K, V>> {
        EntrySet() {
        }

        public Iterator<java.util.Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public int size() {
            return ArrayMap.this.size;
        }
    }

    public static <K, V> ArrayMap<K, V> create() {
        return new ArrayMap();
    }

    public static <K, V> ArrayMap<K, V> create(int i) {
        ArrayMap<K, V> create = create();
        create.ensureCapacity(i);
        return create;
    }

    private int getDataIndexOfKey(Object obj) {
        int i = this.size;
        Object[] objArr = this.data;
        for (int i2 = 0; i2 < (i << 1); i2 += 2) {
            Object obj2 = objArr[i2];
            if (obj == null) {
                if (obj2 == null) {
                    return i2;
                }
            } else if (obj.equals(obj2)) {
                return i2;
            }
        }
        return -2;
    }

    public static <K, V> ArrayMap<K, V> of(Object... objArr) {
        ArrayMap<K, V> create = create(1);
        int length = objArr.length;
        if (1 == length % 2) {
            throw new IllegalArgumentException("missing value for last key: " + objArr[length - 1]);
        }
        create.size = objArr.length / 2;
        Object obj = new Object[length];
        create.data = obj;
        System.arraycopy(objArr, 0, obj, 0, length);
        return create;
    }

    private V removeFromDataIndexOfKey(int i) {
        int i2 = this.size << 1;
        if (i < 0 || i >= i2) {
            return null;
        }
        V valueAtDataIndex = valueAtDataIndex(i + 1);
        Object obj = this.data;
        int i3 = (i2 - i) - 2;
        if (i3 != 0) {
            System.arraycopy(obj, i + 2, obj, i, i3);
        }
        this.size--;
        setData(i2 - 2, null, null);
        return valueAtDataIndex;
    }

    private void setData(int i, K k, V v) {
        Object[] objArr = this.data;
        objArr[i] = k;
        objArr[i + 1] = v;
    }

    private void setDataCapacity(int i) {
        if (i == 0) {
            this.data = null;
            return;
        }
        int i2 = this.size;
        Object obj = this.data;
        if (i2 == 0 || i != obj.length) {
            Object obj2 = new Object[i];
            this.data = obj2;
            if (i2 != 0) {
                System.arraycopy(obj, 0, obj2, 0, i2 << 1);
            }
        }
    }

    private V valueAtDataIndex(int i) {
        return i < 0 ? null : this.data[i];
    }

    public final void add(K k, V v) {
        set(this.size, k, v);
    }

    public void clear() {
        this.size = 0;
        this.data = null;
    }

    public ArrayMap<K, V> clone() {
        try {
            ArrayMap<K, V> arrayMap = (ArrayMap) super.clone();
            Object obj = this.data;
            if (obj == null) {
                return arrayMap;
            }
            int length = obj.length;
            Object obj2 = new Object[length];
            arrayMap.data = obj2;
            System.arraycopy(obj, 0, obj2, 0, length);
            return arrayMap;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public final boolean containsKey(Object obj) {
        return -2 != getDataIndexOfKey(obj);
    }

    public final boolean containsValue(Object obj) {
        int i = this.size;
        Object[] objArr = this.data;
        for (int i2 = 1; i2 < (i << 1); i2 += 2) {
            Object obj2 = objArr[i2];
            if (obj == null) {
                if (obj2 == null) {
                    return true;
                }
            } else if (obj.equals(obj2)) {
                return true;
            }
        }
        return false;
    }

    public final void ensureCapacity(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException();
        }
        Object[] objArr = this.data;
        int i2 = i << 1;
        int length = objArr == null ? 0 : objArr.length;
        if (i2 > length) {
            length = ((length / 2) * 3) + 1;
            if (length % 2 != 0) {
                length++;
            }
            if (length >= i2) {
                i2 = length;
            }
            setDataCapacity(i2);
        }
    }

    public final Set<java.util.Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    public final V get(Object obj) {
        return valueAtDataIndex(getDataIndexOfKey(obj) + 1);
    }

    public final int getIndexOfKey(K k) {
        return getDataIndexOfKey(k) >> 1;
    }

    public final K getKey(int i) {
        return (i < 0 || i >= this.size) ? null : this.data[i << 1];
    }

    public final V getValue(int i) {
        return (i < 0 || i >= this.size) ? null : valueAtDataIndex((i << 1) + 1);
    }

    public final V put(K k, V v) {
        int indexOfKey = getIndexOfKey(k);
        if (indexOfKey == -1) {
            indexOfKey = this.size;
        }
        return set(indexOfKey, k, v);
    }

    public final V remove(int i) {
        return removeFromDataIndexOfKey(i << 1);
    }

    public final V remove(Object obj) {
        return removeFromDataIndexOfKey(getDataIndexOfKey(obj));
    }

    public final V set(int i, V v) {
        int i2 = this.size;
        if (i < 0 || i >= i2) {
            throw new IndexOutOfBoundsException();
        }
        i2 = (i << 1) + 1;
        V valueAtDataIndex = valueAtDataIndex(i2);
        this.data[i2] = v;
        return valueAtDataIndex;
    }

    public final V set(int i, K k, V v) {
        if (i < 0) {
            throw new IndexOutOfBoundsException();
        }
        int i2 = i + 1;
        ensureCapacity(i2);
        int i3 = i << 1;
        V valueAtDataIndex = valueAtDataIndex(i3 + 1);
        setData(i3, k, v);
        if (i2 > this.size) {
            this.size = i2;
        }
        return valueAtDataIndex;
    }

    public final int size() {
        return this.size;
    }

    public final void trim() {
        setDataCapacity(this.size << 1);
    }
}
