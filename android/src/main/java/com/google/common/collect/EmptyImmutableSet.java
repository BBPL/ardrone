package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class EmptyImmutableSet extends ImmutableSet<Object> {
    static final EmptyImmutableSet INSTANCE = new EmptyImmutableSet();
    private static final long serialVersionUID = 0;

    private EmptyImmutableSet() {
    }

    public ImmutableList<Object> asList() {
        return ImmutableList.of();
    }

    public boolean contains(@Nullable Object obj) {
        return false;
    }

    public boolean containsAll(Collection<?> collection) {
        return collection.isEmpty();
    }

    public boolean equals(@Nullable Object obj) {
        return obj instanceof Set ? ((Set) obj).isEmpty() : false;
    }

    public final int hashCode() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    boolean isHashCodeFast() {
        return true;
    }

    boolean isPartialView() {
        return false;
    }

    public UnmodifiableIterator<Object> iterator() {
        return Iterators.emptyIterator();
    }

    Object readResolve() {
        return INSTANCE;
    }

    public int size() {
        return 0;
    }

    public Object[] toArray() {
        return ObjectArrays.EMPTY_ARRAY;
    }

    public <T> T[] toArray(T[] tArr) {
        return asList().toArray(tArr);
    }

    public String toString() {
        return "[]";
    }
}
