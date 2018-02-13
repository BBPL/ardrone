package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Multiset.Entry;
import java.util.Collection;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class EmptyImmutableMultiset extends ImmutableMultiset<Object> {
    static final EmptyImmutableMultiset INSTANCE = new EmptyImmutableMultiset();
    private static final long serialVersionUID = 0;

    EmptyImmutableMultiset() {
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

    public int count(@Nullable Object obj) {
        return 0;
    }

    ImmutableSet<Entry<Object>> createEntrySet() {
        throw new AssertionError("should never be called");
    }

    public ImmutableSet<Object> elementSet() {
        return ImmutableSet.of();
    }

    public ImmutableSet<Entry<Object>> entrySet() {
        return ImmutableSet.of();
    }

    public boolean equals(@Nullable Object obj) {
        return obj instanceof Multiset ? ((Multiset) obj).isEmpty() : false;
    }

    public int hashCode() {
        return 0;
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
}
