package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collection;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class RegularContiguousSet<C extends Comparable> extends ContiguousSet<C> {
    private static final long serialVersionUID = 0;
    private final Range<C> range;

    @GwtIncompatible("NavigableSet")
    private final class DescendingContiguousSet extends ImmutableSortedSet<C> {
        private DescendingContiguousSet() {
            super(Ordering.natural().reverse());
        }

        ImmutableSortedSet<C> createDescendingSet() {
            return RegularContiguousSet.this;
        }

        public C first() {
            return RegularContiguousSet.this.last();
        }

        ImmutableSortedSet<C> headSetImpl(C c, boolean z) {
            return RegularContiguousSet.this.tailSetImpl((Comparable) c, z).descendingSet();
        }

        int indexOf(Object obj) {
            return contains(obj) ? (int) RegularContiguousSet.this.domain.distance(last(), (Comparable) obj) : -1;
        }

        boolean isPartialView() {
            return false;
        }

        public UnmodifiableIterator<C> iterator() {
            return new AbstractSequentialIterator<C>(first()) {
                final C last = DescendingContiguousSet.this.last();

                protected C computeNext(C c) {
                    return RegularContiguousSet.equalsOrThrow(c, this.last) ? null : RegularContiguousSet.this.domain.previous(c);
                }
            };
        }

        public C last() {
            return RegularContiguousSet.this.first();
        }

        public int size() {
            return RegularContiguousSet.this.size();
        }

        ImmutableSortedSet<C> subSetImpl(C c, boolean z, C c2, boolean z2) {
            return RegularContiguousSet.this.subSetImpl((Comparable) c2, z2, (Comparable) c, z).descendingSet();
        }

        ImmutableSortedSet<C> tailSetImpl(C c, boolean z) {
            return RegularContiguousSet.this.headSetImpl((Comparable) c, z).descendingSet();
        }
    }

    @GwtIncompatible("serialization")
    private static final class SerializedForm<C extends Comparable> implements Serializable {
        final DiscreteDomain<C> domain;
        final Range<C> range;

        private SerializedForm(Range<C> range, DiscreteDomain<C> discreteDomain) {
            this.range = range;
            this.domain = discreteDomain;
        }

        private Object readResolve() {
            return new RegularContiguousSet(this.range, this.domain);
        }
    }

    RegularContiguousSet(Range<C> range, DiscreteDomain<C> discreteDomain) {
        super(discreteDomain);
        this.range = range;
    }

    private static boolean equalsOrThrow(Comparable<?> comparable, @Nullable Comparable<?> comparable2) {
        return comparable2 != null && Range.compareOrThrow(comparable, comparable2) == 0;
    }

    private ContiguousSet<C> intersectionInCurrentDomain(Range<C> range) {
        return this.range.isConnected(range) ? this.range.intersection(range).asSet(this.domain) : new EmptyContiguousSet(this.domain);
    }

    public boolean contains(Object obj) {
        boolean z = false;
        if (obj != null) {
            try {
                z = this.range.contains((Comparable) obj);
            } catch (ClassCastException e) {
            }
        }
        return z;
    }

    public boolean containsAll(Collection<?> collection) {
        return Collections2.containsAllImpl(this, collection);
    }

    @GwtIncompatible("NavigableSet")
    ImmutableSortedSet<C> createDescendingSet() {
        return new DescendingContiguousSet();
    }

    public boolean equals(Object obj) {
        if (obj != this) {
            if (obj instanceof RegularContiguousSet) {
                RegularContiguousSet regularContiguousSet = (RegularContiguousSet) obj;
                if (this.domain.equals(regularContiguousSet.domain)) {
                    if (!(first().equals(regularContiguousSet.first()) && last().equals(regularContiguousSet.last()))) {
                        return false;
                    }
                }
            }
            return super.equals(obj);
        }
        return true;
    }

    public C first() {
        return this.range.lowerBound.leastValueAbove(this.domain);
    }

    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }

    ContiguousSet<C> headSetImpl(C c, boolean z) {
        return intersectionInCurrentDomain(Ranges.upTo(c, BoundType.forBoolean(z)));
    }

    @GwtIncompatible("not used by GWT emulation")
    int indexOf(Object obj) {
        return contains(obj) ? (int) this.domain.distance(first(), (Comparable) obj) : -1;
    }

    public ContiguousSet<C> intersection(ContiguousSet<C> contiguousSet) {
        Preconditions.checkNotNull(contiguousSet);
        Preconditions.checkArgument(this.domain.equals(contiguousSet.domain));
        if (contiguousSet.isEmpty()) {
            return contiguousSet;
        }
        Comparable comparable = (Comparable) Ordering.natural().max(first(), contiguousSet.first());
        Comparable comparable2 = (Comparable) Ordering.natural().min(last(), contiguousSet.last());
        return comparable.compareTo(comparable2) < 0 ? Ranges.closed(comparable, comparable2).asSet(this.domain) : new EmptyContiguousSet(this.domain);
    }

    public boolean isEmpty() {
        return false;
    }

    boolean isPartialView() {
        return false;
    }

    public UnmodifiableIterator<C> iterator() {
        return new AbstractSequentialIterator<C>(first()) {
            final C last = RegularContiguousSet.this.last();

            protected C computeNext(C c) {
                return RegularContiguousSet.equalsOrThrow(c, this.last) ? null : RegularContiguousSet.this.domain.next(c);
            }
        };
    }

    public C last() {
        return this.range.upperBound.greatestValueBelow(this.domain);
    }

    public Range<C> range() {
        return range(BoundType.CLOSED, BoundType.CLOSED);
    }

    public Range<C> range(BoundType boundType, BoundType boundType2) {
        return Ranges.create(this.range.lowerBound.withLowerBoundType(boundType, this.domain), this.range.upperBound.withUpperBoundType(boundType2, this.domain));
    }

    public int size() {
        long distance = this.domain.distance(first(), last());
        return distance >= 2147483647L ? Integer.MAX_VALUE : ((int) distance) + 1;
    }

    ContiguousSet<C> subSetImpl(C c, boolean z, C c2, boolean z2) {
        return (c.compareTo(c2) != 0 || z || z2) ? intersectionInCurrentDomain(Ranges.range(c, BoundType.forBoolean(z), c2, BoundType.forBoolean(z2))) : new EmptyContiguousSet(this.domain);
    }

    ContiguousSet<C> tailSetImpl(C c, boolean z) {
        return intersectionInCurrentDomain(Ranges.downTo(c, BoundType.forBoolean(z)));
    }

    public Object[] toArray() {
        return ObjectArrays.toArrayImpl(this);
    }

    public <T> T[] toArray(T[] tArr) {
        return ObjectArrays.toArrayImpl(this, tArr);
    }

    @GwtIncompatible("serialization")
    Object writeReplace() {
        return new SerializedForm(this.range, this.domain);
    }
}
