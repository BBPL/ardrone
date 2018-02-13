package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import java.util.Collection;
import java.util.Comparator;
import javax.annotation.Nullable;

final class EmptyImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E> {
    private final ImmutableSortedSet<E> elementSet;

    EmptyImmutableSortedMultiset(Comparator<? super E> comparator) {
        this.elementSet = ImmutableSortedSet.emptySet(comparator);
    }

    public ImmutableList<E> asList() {
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

    ImmutableSet<Entry<E>> createEntrySet() {
        throw new AssertionError("should never be called");
    }

    public ImmutableSortedSet<E> elementSet() {
        return this.elementSet;
    }

    public ImmutableSet<Entry<E>> entrySet() {
        return ImmutableSet.of();
    }

    public boolean equals(@Nullable Object obj) {
        return obj instanceof Multiset ? ((Multiset) obj).isEmpty() : false;
    }

    public Entry<E> firstEntry() {
        return null;
    }

    public int hashCode() {
        return 0;
    }

    public ImmutableSortedMultiset<E> headMultiset(E e, BoundType boundType) {
        Preconditions.checkNotNull(e);
        Preconditions.checkNotNull(boundType);
        return this;
    }

    boolean isPartialView() {
        return false;
    }

    public UnmodifiableIterator<E> iterator() {
        return Iterators.emptyIterator();
    }

    public Entry<E> lastEntry() {
        return null;
    }

    public int size() {
        return 0;
    }

    public ImmutableSortedMultiset<E> tailMultiset(E e, BoundType boundType) {
        Preconditions.checkNotNull(e);
        Preconditions.checkNotNull(boundType);
        return this;
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
