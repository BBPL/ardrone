package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
class RegularImmutableList<E> extends ImmutableList<E> {
    private final transient Object[] array;
    private final transient int offset;
    private final transient int size;

    RegularImmutableList(Object[] objArr) {
        this(objArr, 0, objArr.length);
    }

    RegularImmutableList(Object[] objArr, int i, int i2) {
        this.offset = i;
        this.size = i2;
        this.array = objArr;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj != this) {
            if (!(obj instanceof List)) {
                return false;
            }
            List<Object> list = (List) obj;
            if (size() != list.size()) {
                return false;
            }
            int i = this.offset;
            if (obj instanceof RegularImmutableList) {
                RegularImmutableList regularImmutableList = (RegularImmutableList) obj;
                int i2 = regularImmutableList.offset;
                while (i2 < regularImmutableList.offset + regularImmutableList.size) {
                    if (!this.array[i].equals(regularImmutableList.array[i2])) {
                        return false;
                    }
                    i2++;
                    i++;
                }
            } else {
                for (Object equals : list) {
                    if (!this.array[i].equals(equals)) {
                        return false;
                    }
                    i++;
                }
            }
        }
        return true;
    }

    public E get(int i) {
        Preconditions.checkElementIndex(i, this.size);
        return this.array[this.offset + i];
    }

    public boolean isEmpty() {
        return false;
    }

    boolean isPartialView() {
        return (this.offset == 0 && this.size == this.array.length) ? false : true;
    }

    public UnmodifiableListIterator<E> listIterator(int i) {
        return Iterators.forArray(this.array, this.offset, this.size, i);
    }

    public int size() {
        return this.size;
    }

    ImmutableList<E> subListUnchecked(int i, int i2) {
        return new RegularImmutableList(this.array, this.offset + i, i2 - i);
    }

    public Object[] toArray() {
        Object obj = new Object[size()];
        System.arraycopy(this.array, this.offset, obj, 0, this.size);
        return obj;
    }

    public <T> T[] toArray(T[] tArr) {
        Object newArray;
        if (tArr.length < this.size) {
            newArray = ObjectArrays.newArray((Object[]) tArr, this.size);
        } else if (tArr.length > this.size) {
            tArr[this.size] = null;
        }
        System.arraycopy(this.array, this.offset, newArray, 0, this.size);
        return newArray;
    }

    public String toString() {
        StringBuilder append = Collections2.newStringBuilderForCollection(size()).append('[').append(this.array[this.offset]);
        for (int i = this.offset + 1; i < this.offset + this.size; i++) {
            append.append(", ").append(this.array[i]);
        }
        return append.append(']').toString();
    }
}
