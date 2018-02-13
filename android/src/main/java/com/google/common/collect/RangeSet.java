package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

abstract class RangeSet<C extends Comparable> {

    static class StandardComplement<C extends Comparable> extends RangeSet<C> {
        private transient Set<Range<C>> asRanges;
        final RangeSet<C> positive;

        class C07201 extends AbstractSet<Range<C>> {
            C07201() {
            }

            public Iterator<Range<C>> iterator() {
                final Iterator it = StandardComplement.this.positive.asRanges().iterator();
                return new AbstractIterator<Range<C>>() {
                    Cut<C> prevCut = Cut.belowAll();

                    protected Range<C> computeNext() {
                        while (it.hasNext()) {
                            Cut cut = this.prevCut;
                            Range range = (Range) it.next();
                            this.prevCut = range.upperBound;
                            if (cut.compareTo(range.lowerBound) < 0) {
                                return new Range(cut, range.lowerBound);
                            }
                        }
                        Cut aboveAll = Cut.aboveAll();
                        if (this.prevCut.compareTo(aboveAll) >= 0) {
                            return (Range) endOfData();
                        }
                        Range<C> range2 = new Range(this.prevCut, aboveAll);
                        this.prevCut = aboveAll;
                        return range2;
                    }
                };
            }

            public int size() {
                return Iterators.size(iterator());
            }
        }

        public StandardComplement(RangeSet<C> rangeSet) {
            this.positive = rangeSet;
        }

        public void add(Range<C> range) {
            this.positive.remove(range);
        }

        public final Set<Range<C>> asRanges() {
            Set<Range<C>> set = this.asRanges;
            if (set != null) {
                return set;
            }
            set = createAsRanges();
            this.asRanges = set;
            return set;
        }

        public RangeSet<C> complement() {
            return this.positive;
        }

        public boolean contains(C c) {
            return !this.positive.contains(c);
        }

        Set<Range<C>> createAsRanges() {
            return new C07201();
        }

        public void remove(Range<C> range) {
            this.positive.add(range);
        }
    }

    RangeSet() {
    }

    public void add(Range<C> range) {
        throw new UnsupportedOperationException();
    }

    public void addAll(RangeSet<C> rangeSet) {
        for (Range add : rangeSet.asRanges()) {
            add(add);
        }
    }

    public abstract Set<Range<C>> asRanges();

    public abstract RangeSet<C> complement();

    public boolean contains(C c) {
        return rangeContaining(c) != null;
    }

    public boolean encloses(Range<C> range) {
        for (Range encloses : asRanges()) {
            if (encloses.encloses(range)) {
                return true;
            }
        }
        return false;
    }

    public boolean enclosesAll(RangeSet<C> rangeSet) {
        for (Range encloses : rangeSet.asRanges()) {
            if (!encloses(encloses)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof RangeSet)) {
            return false;
        }
        return asRanges().equals(((RangeSet) obj).asRanges());
    }

    public final int hashCode() {
        return asRanges().hashCode();
    }

    public boolean isEmpty() {
        return asRanges().isEmpty();
    }

    public Range<C> rangeContaining(C c) {
        Preconditions.checkNotNull(c);
        for (Range<C> range : asRanges()) {
            if (range.contains(c)) {
                return range;
            }
        }
        return null;
    }

    public void remove(Range<C> range) {
        throw new UnsupportedOperationException();
    }

    public void removeAll(RangeSet<C> rangeSet) {
        for (Range remove : rangeSet.asRanges()) {
            remove(remove);
        }
    }

    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        for (Range append : asRanges()) {
            stringBuilder.append(append);
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
