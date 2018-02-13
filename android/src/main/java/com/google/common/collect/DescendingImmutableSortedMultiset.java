package com.google.common.collect;

import com.google.common.collect.Multiset.Entry;
import javax.annotation.Nullable;

final class DescendingImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E> {
    private final transient ImmutableSortedMultiset<E> forward;

    DescendingImmutableSortedMultiset(ImmutableSortedMultiset<E> immutableSortedMultiset) {
        this.forward = immutableSortedMultiset;
    }

    public int count(@Nullable Object obj) {
        return this.forward.count(obj);
    }

    ImmutableSet<Entry<E>> createEntrySet() {
        final ImmutableSet entrySet = this.forward.entrySet();
        return new EntrySet() {
            ImmutableList<Entry<E>> createAsList() {
                return entrySet.asList().reverse();
            }

            public UnmodifiableIterator<Entry<E>> iterator() {
                return asList().iterator();
            }

            public int size() {
                return entrySet.size();
            }
        };
    }

    public ImmutableSortedMultiset<E> descendingMultiset() {
        return this.forward;
    }

    public ImmutableSortedSet<E> elementSet() {
        return this.forward.elementSet().descendingSet();
    }

    public Entry<E> firstEntry() {
        return this.forward.lastEntry();
    }

    public ImmutableSortedMultiset<E> headMultiset(E e, BoundType boundType) {
        return this.forward.tailMultiset((Object) e, boundType).descendingMultiset();
    }

    boolean isPartialView() {
        return this.forward.isPartialView();
    }

    public Entry<E> lastEntry() {
        return this.forward.firstEntry();
    }

    public int size() {
        return this.forward.size();
    }

    public ImmutableSortedMultiset<E> tailMultiset(E e, BoundType boundType) {
        return this.forward.headMultiset((Object) e, boundType).descendingMultiset();
    }
}
