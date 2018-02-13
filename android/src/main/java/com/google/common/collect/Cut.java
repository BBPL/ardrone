package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Booleans;
import java.io.Serializable;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

@GwtCompatible
abstract class Cut<C extends Comparable> implements Comparable<Cut<C>>, Serializable {
    private static final long serialVersionUID = 0;
    final C endpoint;

    private static final class AboveAll extends Cut<Comparable<?>> {
        private static final AboveAll INSTANCE = new AboveAll();
        private static final long serialVersionUID = 0;

        private AboveAll() {
            super(null);
        }

        private Object readResolve() {
            return INSTANCE;
        }

        public int compareTo(Cut<Comparable<?>> cut) {
            return cut == this ? 0 : 1;
        }

        void describeAsLowerBound(StringBuilder stringBuilder) {
            throw new AssertionError();
        }

        void describeAsUpperBound(StringBuilder stringBuilder) {
            stringBuilder.append("+∞)");
        }

        Comparable<?> endpoint() {
            throw new IllegalStateException("range unbounded on this side");
        }

        Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> discreteDomain) {
            return discreteDomain.maxValue();
        }

        boolean isLessThan(Comparable<?> comparable) {
            return false;
        }

        Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> discreteDomain) {
            throw new AssertionError();
        }

        BoundType typeAsLowerBound() {
            throw new AssertionError("this statement should be unreachable");
        }

        BoundType typeAsUpperBound() {
            throw new IllegalStateException();
        }

        Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> discreteDomain) {
            throw new AssertionError("this statement should be unreachable");
        }

        Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> discreteDomain) {
            throw new IllegalStateException();
        }
    }

    private static final class AboveValue<C extends Comparable> extends Cut<C> {
        private static final long serialVersionUID = 0;

        AboveValue(C c) {
            super((Comparable) Preconditions.checkNotNull(c));
        }

        Cut<C> canonical(DiscreteDomain<C> discreteDomain) {
            Comparable leastValueAbove = leastValueAbove(discreteDomain);
            return leastValueAbove != null ? Cut.belowValue(leastValueAbove) : Cut.aboveAll();
        }

        public /* bridge */ /* synthetic */ int compareTo(Object obj) {
            return super.compareTo((Cut) obj);
        }

        void describeAsLowerBound(StringBuilder stringBuilder) {
            stringBuilder.append('(').append(this.endpoint);
        }

        void describeAsUpperBound(StringBuilder stringBuilder) {
            stringBuilder.append(this.endpoint).append(']');
        }

        C greatestValueBelow(DiscreteDomain<C> discreteDomain) {
            return this.endpoint;
        }

        public int hashCode() {
            return this.endpoint.hashCode() ^ -1;
        }

        boolean isLessThan(C c) {
            return Range.compareOrThrow(this.endpoint, c) < 0;
        }

        C leastValueAbove(DiscreteDomain<C> discreteDomain) {
            return discreteDomain.next(this.endpoint);
        }

        BoundType typeAsLowerBound() {
            return BoundType.OPEN;
        }

        BoundType typeAsUpperBound() {
            return BoundType.CLOSED;
        }

        Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain) {
            switch (boundType) {
                case CLOSED:
                    Comparable next = discreteDomain.next(this.endpoint);
                    return next == null ? Cut.belowAll() : Cut.belowValue(next);
                case OPEN:
                    return this;
                default:
                    throw new AssertionError();
            }
        }

        Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain) {
            switch (boundType) {
                case CLOSED:
                    return this;
                case OPEN:
                    Comparable next = discreteDomain.next(this.endpoint);
                    return next == null ? Cut.aboveAll() : Cut.belowValue(next);
                default:
                    throw new AssertionError();
            }
        }
    }

    private static final class BelowAll extends Cut<Comparable<?>> {
        private static final BelowAll INSTANCE = new BelowAll();
        private static final long serialVersionUID = 0;

        private BelowAll() {
            super(null);
        }

        private Object readResolve() {
            return INSTANCE;
        }

        Cut<Comparable<?>> canonical(DiscreteDomain<Comparable<?>> discreteDomain) {
            Cut<Comparable<?>> belowValue;
            try {
                belowValue = Cut.belowValue(discreteDomain.minValue());
            } catch (NoSuchElementException e) {
            }
            return belowValue;
        }

        public int compareTo(Cut<Comparable<?>> cut) {
            return cut == this ? 0 : -1;
        }

        void describeAsLowerBound(StringBuilder stringBuilder) {
            stringBuilder.append("(-∞");
        }

        void describeAsUpperBound(StringBuilder stringBuilder) {
            throw new AssertionError();
        }

        Comparable<?> endpoint() {
            throw new IllegalStateException("range unbounded on this side");
        }

        Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> discreteDomain) {
            throw new AssertionError();
        }

        boolean isLessThan(Comparable<?> comparable) {
            return true;
        }

        Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> discreteDomain) {
            return discreteDomain.minValue();
        }

        BoundType typeAsLowerBound() {
            throw new IllegalStateException();
        }

        BoundType typeAsUpperBound() {
            throw new AssertionError("this statement should be unreachable");
        }

        Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> discreteDomain) {
            throw new IllegalStateException();
        }

        Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> discreteDomain) {
            throw new AssertionError("this statement should be unreachable");
        }
    }

    private static final class BelowValue<C extends Comparable> extends Cut<C> {
        private static final long serialVersionUID = 0;

        BelowValue(C c) {
            super((Comparable) Preconditions.checkNotNull(c));
        }

        public /* bridge */ /* synthetic */ int compareTo(Object obj) {
            return super.compareTo((Cut) obj);
        }

        void describeAsLowerBound(StringBuilder stringBuilder) {
            stringBuilder.append('[').append(this.endpoint);
        }

        void describeAsUpperBound(StringBuilder stringBuilder) {
            stringBuilder.append(this.endpoint).append(')');
        }

        C greatestValueBelow(DiscreteDomain<C> discreteDomain) {
            return discreteDomain.previous(this.endpoint);
        }

        public int hashCode() {
            return this.endpoint.hashCode();
        }

        boolean isLessThan(C c) {
            return Range.compareOrThrow(this.endpoint, c) <= 0;
        }

        C leastValueAbove(DiscreteDomain<C> discreteDomain) {
            return this.endpoint;
        }

        BoundType typeAsLowerBound() {
            return BoundType.CLOSED;
        }

        BoundType typeAsUpperBound() {
            return BoundType.OPEN;
        }

        Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain) {
            switch (boundType) {
                case CLOSED:
                    return this;
                case OPEN:
                    Comparable previous = discreteDomain.previous(this.endpoint);
                    return previous == null ? Cut.belowAll() : new AboveValue(previous);
                default:
                    throw new AssertionError();
            }
        }

        Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain) {
            switch (boundType) {
                case CLOSED:
                    Comparable previous = discreteDomain.previous(this.endpoint);
                    return previous == null ? Cut.aboveAll() : new AboveValue(previous);
                case OPEN:
                    return this;
                default:
                    throw new AssertionError();
            }
        }
    }

    Cut(@Nullable C c) {
        this.endpoint = c;
    }

    static <C extends Comparable> Cut<C> aboveAll() {
        return AboveAll.INSTANCE;
    }

    static <C extends Comparable> Cut<C> aboveValue(C c) {
        return new AboveValue(c);
    }

    static <C extends Comparable> Cut<C> belowAll() {
        return BelowAll.INSTANCE;
    }

    static <C extends Comparable> Cut<C> belowValue(C c) {
        return new BelowValue(c);
    }

    Cut<C> canonical(DiscreteDomain<C> discreteDomain) {
        return this;
    }

    public int compareTo(Cut<C> cut) {
        if (cut == belowAll()) {
            return 1;
        }
        if (cut == aboveAll()) {
            return -1;
        }
        int compareOrThrow = Range.compareOrThrow(this.endpoint, cut.endpoint);
        return compareOrThrow == 0 ? Booleans.compare(this instanceof AboveValue, cut instanceof AboveValue) : compareOrThrow;
    }

    abstract void describeAsLowerBound(StringBuilder stringBuilder);

    abstract void describeAsUpperBound(StringBuilder stringBuilder);

    C endpoint() {
        return this.endpoint;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Cut)) {
            return false;
        }
        try {
            return compareTo((Cut) obj) == 0;
        } catch (ClassCastException e) {
            return false;
        }
    }

    abstract C greatestValueBelow(DiscreteDomain<C> discreteDomain);

    abstract boolean isLessThan(C c);

    abstract C leastValueAbove(DiscreteDomain<C> discreteDomain);

    abstract BoundType typeAsLowerBound();

    abstract BoundType typeAsUpperBound();

    abstract Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain);

    abstract Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain);
}
