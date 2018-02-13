package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Multiset.Entry;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

@GwtCompatible
@Beta
public interface SortedMultiset<E> extends Multiset<E>, SortedIterable<E> {
    Comparator<? super E> comparator();

    SortedMultiset<E> descendingMultiset();

    SortedSet<E> elementSet();

    Entry<E> firstEntry();

    SortedMultiset<E> headMultiset(E e, BoundType boundType);

    Iterator<E> iterator();

    Entry<E> lastEntry();

    Entry<E> pollFirstEntry();

    Entry<E> pollLastEntry();

    SortedMultiset<E> subMultiset(E e, BoundType boundType, E e2, BoundType boundType2);

    SortedMultiset<E> tailMultiset(E e, BoundType boundType);
}
