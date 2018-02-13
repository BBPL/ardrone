package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
@Beta
public final class Range<C extends Comparable> implements Predicate<C>, Serializable {
    private static final long serialVersionUID = 0;
    final Cut<C> lowerBound;
    final Cut<C> upperBound;

    Range(Cut<C> cut, Cut<C> cut2) {
        if (cut.compareTo((Cut) cut2) > 0) {
            throw new IllegalArgumentException("Invalid range: " + toString(cut, cut2));
        }
        this.lowerBound = cut;
        this.upperBound = cut2;
    }

    private static <T> SortedSet<T> cast(Iterable<T> iterable) {
        return (SortedSet) iterable;
    }

    static int compareOrThrow(Comparable comparable, Comparable comparable2) {
        return comparable.compareTo(comparable2);
    }

    private static String toString(Cut<?> cut, Cut<?> cut2) {
        StringBuilder stringBuilder = new StringBuilder(16);
        cut.describeAsLowerBound(stringBuilder);
        stringBuilder.append('‥');
        cut2.describeAsUpperBound(stringBuilder);
        return stringBuilder.toString();
    }

    public boolean apply(C c) {
        return contains(c);
    }

    @GwtCompatible(serializable = false)
    public ContiguousSet<C> asSet(DiscreteDomain<C> discreteDomain) {
        return ContiguousSet.create(this, discreteDomain);
    }

    public Range<C> canonical(DiscreteDomain<C> discreteDomain) {
        Preconditions.checkNotNull(discreteDomain);
        Cut canonical = this.lowerBound.canonical(discreteDomain);
        Cut canonical2 = this.upperBound.canonical(discreteDomain);
        return (canonical == this.lowerBound && canonical2 == this.upperBound) ? this : Ranges.create(canonical, canonical2);
    }

    public boolean contains(C c) {
        Preconditions.checkNotNull(c);
        return this.lowerBound.isLessThan(c) && !this.upperBound.isLessThan(c);
    }

    public boolean containsAll(Iterable<? extends C> iterable) {
        if (!Iterables.isEmpty(iterable)) {
            if (iterable instanceof SortedSet) {
                SortedSet cast = cast(iterable);
                Comparator comparator = cast.comparator();
                if (Ordering.natural().equals(comparator) || comparator == null) {
                    return contains((Comparable) cast.first()) && contains((Comparable) cast.last());
                }
            }
            Iterator it = iterable.iterator();
            while (it.hasNext()) {
                if (!contains((Comparable) it.next())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean encloses(Range<C> range) {
        return this.lowerBound.compareTo(range.lowerBound) <= 0 && this.upperBound.compareTo(range.upperBound) >= 0;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Range)) {
            return false;
        }
        Range range = (Range) obj;
        return this.lowerBound.equals(range.lowerBound) && this.upperBound.equals(range.upperBound);
    }

    public boolean hasLowerBound() {
        return this.lowerBound != Cut.belowAll();
    }

    public boolean hasUpperBound() {
        return this.upperBound != Cut.aboveAll();
    }

    public int hashCode() {
        return (this.lowerBound.hashCode() * 31) + this.upperBound.hashCode();
    }

    public Range<C> intersection(Range<C> range) {
        return Ranges.create((Cut) Ordering.natural().max(this.lowerBound, range.lowerBound), (Cut) Ordering.natural().min(this.upperBound, range.upperBound));
    }

    public boolean isConnected(Range<C> range) {
        return this.lowerBound.compareTo(range.upperBound) <= 0 && range.lowerBound.compareTo(this.upperBound) <= 0;
    }

    public boolean isEmpty() {
        return this.lowerBound.equals(this.upperBound);
    }

    public BoundType lowerBoundType() {
        return this.lowerBound.typeAsLowerBound();
    }

    public C lowerEndpoint() {
        return this.lowerBound.endpoint();
    }

    public Range<C> span(Range<C> range) {
        return Ranges.create((Cut) Ordering.natural().min(this.lowerBound, range.lowerBound), (Cut) Ordering.natural().max(this.upperBound, range.upperBound));
    }

    public String toString() {
        return toString(this.lowerBound, this.upperBound);
    }

    public BoundType upperBoundType() {
        return this.upperBound.typeAsUpperBound();
    }

    public C upperEndpoint() {
        return this.upperBound.endpoint();
    }
}
