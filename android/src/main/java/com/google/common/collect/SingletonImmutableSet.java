package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class SingletonImmutableSet<E> extends ImmutableSet<E> {
    private transient int cachedHashCode;
    final transient E element;

    SingletonImmutableSet(E e) {
        this.element = Preconditions.checkNotNull(e);
    }

    SingletonImmutableSet(E e, int i) {
        this.element = e;
        this.cachedHashCode = i;
    }

    public boolean contains(Object obj) {
        return this.element.equals(obj);
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Set set = (Set) obj;
        return set.size() == 1 && this.element.equals(set.iterator().next());
    }

    public final int hashCode() {
        int i = this.cachedHashCode;
        if (i != 0) {
            return i;
        }
        i = this.element.hashCode();
        this.cachedHashCode = i;
        return i;
    }

    public boolean isEmpty() {
        return false;
    }

    boolean isHashCodeFast() {
        return this.cachedHashCode != 0;
    }

    boolean isPartialView() {
        return false;
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.singletonIterator(this.element);
    }

    public int size() {
        return 1;
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
