package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import javax.annotation.Nullable;

final class RegularImmutableSortedMultiset<E> extends ImmutableSortedMultiset<E> {
    private final transient int[] counts;
    private final transient long[] cumulativeCounts;
    private final transient RegularImmutableSortedSet<E> elementSet;
    private final transient int length;
    private final transient int offset;

    private final class EntrySet extends EntrySet {

        class C07301 extends ImmutableAsList<Entry<E>> {
            C07301() {
            }

            ImmutableCollection<Entry<E>> delegateCollection() {
                return EntrySet.this;
            }

            public Entry<E> get(int i) {
                return RegularImmutableSortedMultiset.this.getEntry(i);
            }
        }

        private EntrySet() {
            super();
        }

        ImmutableList<Entry<E>> createAsList() {
            return new C07301();
        }

        public UnmodifiableIterator<Entry<E>> iterator() {
            return asList().iterator();
        }

        public int size() {
            return RegularImmutableSortedMultiset.this.length;
        }
    }

    RegularImmutableSortedMultiset(RegularImmutableSortedSet<E> regularImmutableSortedSet, int[] iArr, long[] jArr, int i, int i2) {
        this.elementSet = regularImmutableSortedSet;
        this.counts = iArr;
        this.cumulativeCounts = jArr;
        this.offset = i;
        this.length = i2;
    }

    private Entry<E> getEntry(int i) {
        return Multisets.immutableEntry(this.elementSet.asList().get(i), this.counts[this.offset + i]);
    }

    public int count(@Nullable Object obj) {
        int indexOf = this.elementSet.indexOf(obj);
        return indexOf == -1 ? 0 : this.counts[indexOf + this.offset];
    }

    ImmutableSet<Entry<E>> createEntrySet() {
        return new EntrySet();
    }

    public ImmutableSortedSet<E> elementSet() {
        return this.elementSet;
    }

    public Entry<E> firstEntry() {
        return getEntry(0);
    }

    ImmutableSortedMultiset<E> getSubMultiset(int i, int i2) {
        ImmutableSortedMultiset<E> emptyMultiset;
        Preconditions.checkPositionIndexes(i, i2, this.length);
        if (i == i2) {
            emptyMultiset = ImmutableSortedMultiset.emptyMultiset(comparator());
        } else if (!(i == 0 && i2 == this.length)) {
            return new RegularImmutableSortedMultiset((RegularImmutableSortedSet) this.elementSet.getSubSet(i, i2), this.counts, this.cumulativeCounts, this.offset + i, i2 - i);
        }
        return emptyMultiset;
    }

    public ImmutableSortedMultiset<E> headMultiset(E e, BoundType boundType) {
        return getSubMultiset(0, this.elementSet.headIndex(e, Preconditions.checkNotNull(boundType) == BoundType.CLOSED));
    }

    boolean isPartialView() {
        return this.offset > 0 || this.length < this.counts.length;
    }

    public Entry<E> lastEntry() {
        return getEntry(this.length - 1);
    }

    public int size() {
        return Ints.saturatedCast(this.cumulativeCounts[this.offset + this.length] - this.cumulativeCounts[this.offset]);
    }

    public ImmutableSortedMultiset<E> tailMultiset(E e, BoundType boundType) {
        return getSubMultiset(this.elementSet.tailIndex(e, Preconditions.checkNotNull(boundType) == BoundType.CLOSED), this.length);
    }
}
