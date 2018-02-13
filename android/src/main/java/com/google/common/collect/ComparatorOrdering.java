package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class ComparatorOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final Comparator<T> comparator;

    ComparatorOrdering(Comparator<T> comparator) {
        this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
    }

    public int binarySearch(List<? extends T> list, T t) {
        return Collections.binarySearch(list, t, this.comparator);
    }

    public int compare(T t, T t2) {
        return this.comparator.compare(t, t2);
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ComparatorOrdering)) {
            return false;
        }
        return this.comparator.equals(((ComparatorOrdering) obj).comparator);
    }

    public int hashCode() {
        return this.comparator.hashCode();
    }

    public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> iterable) {
        Object[] toArray = Iterables.toArray(iterable);
        for (Object checkNotNull : toArray) {
            Preconditions.checkNotNull(checkNotNull);
        }
        Arrays.sort(toArray, this.comparator);
        return ImmutableList.asImmutableList(toArray);
    }

    public <E extends T> List<E> sortedCopy(Iterable<E> iterable) {
        List<E> newArrayList = Lists.newArrayList((Iterable) iterable);
        Collections.sort(newArrayList, this.comparator);
        return newArrayList;
    }

    public String toString() {
        return this.comparator.toString();
    }
}
