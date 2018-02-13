package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Comparator;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class GeneralRange<T> implements Serializable {
    private final Comparator<? super T> comparator;
    private final boolean hasLowerBound;
    private final boolean hasUpperBound;
    private final BoundType lowerBoundType;
    @Nullable
    private final T lowerEndpoint;
    private transient GeneralRange<T> reverse;
    private final BoundType upperBoundType;
    @Nullable
    private final T upperEndpoint;

    private GeneralRange(Comparator<? super T> comparator, boolean z, @Nullable T t, BoundType boundType, boolean z2, @Nullable T t2, BoundType boundType2) {
        int i = 0;
        this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        this.hasLowerBound = z;
        this.hasUpperBound = z2;
        this.lowerEndpoint = t;
        this.lowerBoundType = (BoundType) Preconditions.checkNotNull(boundType);
        this.upperEndpoint = t2;
        this.upperBoundType = (BoundType) Preconditions.checkNotNull(boundType2);
        if (z) {
            comparator.compare(t, t);
        }
        if (z2) {
            comparator.compare(t2, t2);
        }
        if (z && z2) {
            int compare = comparator.compare(t, t2);
            Preconditions.checkArgument(compare <= 0, "lowerEndpoint (%s) > upperEndpoint (%s)", t, t2);
            if (compare == 0) {
                int i2 = boundType != BoundType.OPEN ? 1 : 0;
                if (boundType2 != BoundType.OPEN) {
                    i = 1;
                }
                Preconditions.checkArgument(i2 | i);
            }
        }
    }

    static <T> GeneralRange<T> all(Comparator<? super T> comparator) {
        return new GeneralRange(comparator, false, null, BoundType.OPEN, false, null, BoundType.OPEN);
    }

    static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, @Nullable T t, BoundType boundType) {
        return new GeneralRange(comparator, true, t, boundType, false, null, BoundType.OPEN);
    }

    static <T extends Comparable> GeneralRange<T> from(Range<T> range) {
        return new GeneralRange(Ordering.natural(), range.hasLowerBound(), range.hasLowerBound() ? range.lowerEndpoint() : null, range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN, range.hasUpperBound(), range.hasUpperBound() ? range.upperEndpoint() : null, range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN);
    }

    static <T> GeneralRange<T> range(Comparator<? super T> comparator, @Nullable T t, BoundType boundType, @Nullable T t2, BoundType boundType2) {
        return new GeneralRange(comparator, true, t, boundType, true, t2, boundType2);
    }

    static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, @Nullable T t, BoundType boundType) {
        return new GeneralRange(comparator, false, null, BoundType.OPEN, true, t, boundType);
    }

    Comparator<? super T> comparator() {
        return this.comparator;
    }

    boolean contains(@Nullable T t) {
        return (tooLow(t) || tooHigh(t)) ? false : true;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof GeneralRange)) {
            return false;
        }
        GeneralRange generalRange = (GeneralRange) obj;
        return this.comparator.equals(generalRange.comparator) && this.hasLowerBound == generalRange.hasLowerBound && this.hasUpperBound == generalRange.hasUpperBound && getLowerBoundType().equals(generalRange.getLowerBoundType()) && getUpperBoundType().equals(generalRange.getUpperBoundType()) && Objects.equal(getLowerEndpoint(), generalRange.getLowerEndpoint()) && Objects.equal(getUpperEndpoint(), generalRange.getUpperEndpoint());
    }

    BoundType getLowerBoundType() {
        return this.lowerBoundType;
    }

    T getLowerEndpoint() {
        return this.lowerEndpoint;
    }

    BoundType getUpperBoundType() {
        return this.upperBoundType;
    }

    T getUpperEndpoint() {
        return this.upperEndpoint;
    }

    boolean hasLowerBound() {
        return this.hasLowerBound;
    }

    boolean hasUpperBound() {
        return this.hasUpperBound;
    }

    public int hashCode() {
        return Objects.hashCode(this.comparator, getLowerEndpoint(), getLowerBoundType(), getUpperEndpoint(), getUpperBoundType());
    }

    GeneralRange<T> intersect(GeneralRange<T> generalRange) {
        int compare;
        Object obj;
        BoundType boundType;
        Preconditions.checkNotNull(generalRange);
        Preconditions.checkArgument(this.comparator.equals(generalRange.comparator));
        boolean z = this.hasLowerBound;
        Object lowerEndpoint = getLowerEndpoint();
        BoundType lowerBoundType = getLowerBoundType();
        if (!hasLowerBound()) {
            z = generalRange.hasLowerBound;
            lowerEndpoint = generalRange.getLowerEndpoint();
            lowerBoundType = generalRange.getLowerBoundType();
        } else if (generalRange.hasLowerBound()) {
            compare = this.comparator.compare(getLowerEndpoint(), generalRange.getLowerEndpoint());
            if (compare < 0 || (compare == 0 && generalRange.getLowerBoundType() == BoundType.OPEN)) {
                lowerEndpoint = generalRange.getLowerEndpoint();
                lowerBoundType = generalRange.getLowerBoundType();
            }
        }
        boolean z2 = this.hasUpperBound;
        Object upperEndpoint = getUpperEndpoint();
        BoundType upperBoundType = getUpperBoundType();
        if (!hasUpperBound()) {
            z2 = generalRange.hasUpperBound;
            upperEndpoint = generalRange.getUpperEndpoint();
            upperBoundType = generalRange.getUpperBoundType();
        } else if (generalRange.hasUpperBound()) {
            compare = this.comparator.compare(getUpperEndpoint(), generalRange.getUpperEndpoint());
            if (compare > 0 || (compare == 0 && generalRange.getUpperBoundType() == BoundType.OPEN)) {
                upperEndpoint = generalRange.getUpperEndpoint();
                upperBoundType = generalRange.getUpperBoundType();
            }
        }
        if (z && z2) {
            compare = this.comparator.compare(lowerEndpoint, upperEndpoint);
            if (compare > 0 || (compare == 0 && lowerBoundType == BoundType.OPEN && r7 == BoundType.OPEN)) {
                lowerBoundType = BoundType.OPEN;
                upperBoundType = BoundType.CLOSED;
                obj = upperEndpoint;
                boundType = lowerBoundType;
                return new GeneralRange(this.comparator, z, obj, boundType, z2, upperEndpoint, upperBoundType);
            }
        }
        obj = lowerEndpoint;
        boundType = lowerBoundType;
        return new GeneralRange(this.comparator, z, obj, boundType, z2, upperEndpoint, upperBoundType);
    }

    boolean isEmpty() {
        return (hasUpperBound() && tooLow(getUpperEndpoint())) || (hasLowerBound() && tooHigh(getLowerEndpoint()));
    }

    GeneralRange<T> reverse() {
        GeneralRange<T> generalRange = this.reverse;
        if (generalRange != null) {
            return generalRange;
        }
        generalRange = new GeneralRange(Ordering.from(this.comparator).reverse(), this.hasUpperBound, getUpperEndpoint(), getUpperBoundType(), this.hasLowerBound, getLowerEndpoint(), getLowerBoundType());
        generalRange.reverse = this;
        this.reverse = generalRange;
        return generalRange;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.comparator).append(":");
        switch (getLowerBoundType()) {
            case CLOSED:
                stringBuilder.append('[');
                break;
            case OPEN:
                stringBuilder.append('(');
                break;
        }
        if (hasLowerBound()) {
            stringBuilder.append(getLowerEndpoint());
        } else {
            stringBuilder.append("-∞");
        }
        stringBuilder.append(',');
        if (hasUpperBound()) {
            stringBuilder.append(getUpperEndpoint());
        } else {
            stringBuilder.append("∞");
        }
        switch (getUpperBoundType()) {
            case CLOSED:
                stringBuilder.append(']');
                break;
            case OPEN:
                stringBuilder.append(')');
                break;
        }
        return stringBuilder.toString();
    }

    boolean tooHigh(@Nullable T t) {
        int i = 0;
        if (!hasUpperBound()) {
            return false;
        }
        int compare = this.comparator.compare(t, getUpperEndpoint());
        int i2 = compare > 0 ? 1 : 0;
        compare = compare == 0 ? 1 : 0;
        if (getUpperBoundType() == BoundType.OPEN) {
            i = 1;
        }
        return i2 | (compare & i);
    }

    boolean tooLow(@Nullable T t) {
        int i = 0;
        if (!hasLowerBound()) {
            return false;
        }
        int compare = this.comparator.compare(t, getLowerEndpoint());
        int i2 = compare < 0 ? 1 : 0;
        compare = compare == 0 ? 1 : 0;
        if (getLowerBoundType() == BoundType.OPEN) {
            i = 1;
        }
        return i2 | (compare & i);
    }
}
