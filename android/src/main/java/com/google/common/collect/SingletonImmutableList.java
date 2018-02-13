package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class SingletonImmutableList<E> extends ImmutableList<E> {
    final transient E element;

    SingletonImmutableList(E e) {
        this.element = Preconditions.checkNotNull(e);
    }

    public boolean contains(@Nullable Object obj) {
        return this.element.equals(obj);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        List list = (List) obj;
        return list.size() == 1 && this.element.equals(list.get(0));
    }

    public E get(int i) {
        Preconditions.checkElementIndex(i, 1);
        return this.element;
    }

    public int hashCode() {
        return this.element.hashCode() + 31;
    }

    public int indexOf(@Nullable Object obj) {
        return this.element.equals(obj) ? 0 : -1;
    }

    public boolean isEmpty() {
        return false;
    }

    boolean isPartialView() {
        return false;
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    public int lastIndexOf(@Nullable Object obj) {
        return indexOf(obj);
    }

    public ImmutableList<E> reverse() {
        return this;
    }

    public int size() {
        return 1;
    }

    public ImmutableList<E> subList(int i, int i2) {
        Preconditions.checkPositionIndexes(i, i2, 1);
        return i == i2 ? ImmutableList.of() : this;
    }

    public Object[] toArray() {
        return new Object[]{this.element};
    }

    public <T> T[] toArray(T[] tArr) {
        if (tArr.length == 0) {
            tArr = ObjectArrays.newArray((Object[]) tArr, 1);
        } else if (tArr.length > 1) {
            tArr[1] = null;
        }
        tArr[0] = this.element;
        return tArr;
    }

    public String toString() {
        String obj = this.element.toString();
        return new StringBuilder(obj.length() + 2).append('[').append(obj).append(']').toString();
    }
}
