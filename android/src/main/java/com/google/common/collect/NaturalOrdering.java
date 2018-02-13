package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@GwtCompatible(serializable = true)
final class NaturalOrdering extends Ordering<Comparable> implements Serializable {
    static final NaturalOrdering INSTANCE = new NaturalOrdering();
    private static final long serialVersionUID = 0;

    private NaturalOrdering() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    public int binarySearch(List<? extends Comparable> list, Comparable comparable) {
        return Collections.binarySearch(list, comparable);
    }

    public int compare(Comparable comparable, Comparable comparable2) {
        Preconditions.checkNotNull(comparable);
        Preconditions.checkNotNull(comparable2);
        return comparable == comparable2 ? 0 : comparable.compareTo(comparable2);
    }

    public <S extends Comparable> Ordering<S> reverse() {
        return ReverseNaturalOrdering.INSTANCE;
    }

    public <E extends Comparable> List<E> sortedCopy(Iterable<E> iterable) {
        List<E> newArrayList = Lists.newArrayList((Iterable) iterable);
        Collections.sort(newArrayList);
        return newArrayList;
    }

    public String toString() {
        return "Ordering.natural()";
    }
}
