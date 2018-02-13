package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class EmptyImmutableList extends ImmutableList<Object> {
    static final EmptyImmutableList INSTANCE = new EmptyImmutableList();
    private static final long serialVersionUID = 0;

    private EmptyImmutableList() {
    }

    public boolean contains(@Nullable Object obj) {
        return false;
    }

    public boolean containsAll(Collection<?> collection) {
        return collection.isEmpty();
    }

    public boolean equals(@Nullable Object obj) {
        return obj instanceof List ? ((List) obj).isEmpty() : false;
    }

    public Object get(int i) {
        Preconditions.checkElementIndex(i, 0);
        throw new AssertionError("unreachable");
    }

    public int hashCode() {
        return 1;
    }

    public int indexOf(@Nullable Object obj) {
        return -1;
    }

    public boolean isEmpty() {
        return true;
    }

    boolean isPartialView() {
        return false;
    }

    public UnmodifiableIterator<Object> iterator() {
        return listIterator();
    }

    public int lastIndexOf(@Nullable Object obj) {
        return -1;
    }

    public UnmodifiableListIterator<Object> listIterator() {
        return Iterators.EMPTY_LIST_ITERATOR;
    }

    public UnmodifiableListIterator<Object> listIterator(int i) {
        Preconditions.checkPositionIndex(i, 0);
        return Iterators.EMPTY_LIST_ITERATOR;
    }

    Object readResolve() {
        return INSTANCE;
    }

    public ImmutableList<Object> reverse() {
        return this;
    }

    public int size() {
        return 0;
    }

    public ImmutableList<Object> subList(int i, int i2) {
        Preconditions.checkPositionIndexes(i, i2, 0);
        return this;
    }

    public Object[] toArray() {
        return ObjectArrays.EMPTY_ARRAY;
    }

    public <T> T[] toArray(T[] tArr) {
        if (tArr.length > 0) {
            tArr[0] = null;
        }
        return tArr;
    }

    public String toString() {
        return "[]";
    }
}
