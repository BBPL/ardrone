package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.SortedLists.KeyAbsentBehavior;
import com.google.common.collect.SortedLists.KeyPresentBehavior;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class RegularImmutableSortedSet<E> extends ImmutableSortedSet<E> {
    private final transient ImmutableList<E> elements;

    RegularImmutableSortedSet(ImmutableList<E> immutableList, Comparator<? super E> comparator) {
        super(comparator);
        this.elements = immutableList;
        Preconditions.checkArgument(!immutableList.isEmpty());
    }

    private int unsafeBinarySearch(Object obj) throws ClassCastException {
        return Collections.binarySearch(this.elements, obj, unsafeComparator());
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            return unsafeBinarySearch(obj) >= 0;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public boolean containsAll(Collection<?> collection) {
        if (!SortedIterables.hasSameComparator(comparator(), collection) || collection.size() <= 1) {
            return super.containsAll(collection);
        }
        Iterator it = iterator();
        Iterator it2 = collection.iterator();
        Object next = it2.next();
        while (it.hasNext()) {
            try {
                int unsafeCompare = unsafeCompare(it.next(), next);
                if (unsafeCompare == 0) {
                    if (!it2.hasNext()) {
                        return true;
                    }
                    next = it2.next();
                } else if (unsafeCompare > 0) {
                    return false;
                }
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e2) {
                return false;
            }
        }
        return false;
    }

    ImmutableList<E> createAsList() {
        return new ImmutableSortedAsList(this, this.elements);
    }

    ImmutableSortedSet<E> createDescendingSet() {
        return new RegularImmutableSortedSet(this.elements.reverse(), Ordering.from(this.comparator).reverse());
    }

    public boolean equals(@Nullable Object obj) {
        if (obj != this) {
            if (!(obj instanceof Set)) {
                return false;
            }
            Set set = (Set) obj;
            if (size() != set.size()) {
                return false;
            }
            if (!SortedIterables.hasSameComparator(this.comparator, set)) {
                return containsAll(set);
            }
            Iterator it = set.iterator();
            try {
                Iterator it2 = iterator();
                while (it2.hasNext()) {
                    Object next = it2.next();
                    Object next2 = it.next();
                    if (next2 == null) {
                        return false;
                    }
                    if (unsafeCompare(next, next2) != 0) {
                        return false;
                    }
                }
            } catch (ClassCastException e) {
                return false;
            } catch (NoSuchElementException e2) {
                return false;
            }
        }
        return true;
    }

    public E first() {
        return this.elements.get(0);
    }

    ImmutableSortedSet<E> getSubSet(int i, int i2) {
        return (i == 0 && i2 == size()) ? this : i < i2 ? new RegularImmutableSortedSet(this.elements.subList(i, i2), this.comparator) : ImmutableSortedSet.emptySet(this.comparator);
    }

    int headIndex(E e, boolean z) {
        return SortedLists.binarySearch(this.elements, Preconditions.checkNotNull(e), comparator(), z ? KeyPresentBehavior.FIRST_AFTER : KeyPresentBehavior.FIRST_PRESENT, KeyAbsentBehavior.NEXT_HIGHER);
    }

    ImmutableSortedSet<E> headSetImpl(E e, boolean z) {
        return getSubSet(0, headIndex(e, z));
    }

    int indexOf(@Nullable Object obj) {
        if (obj == null) {
            return -1;
        }
        try {
            int binarySearch = SortedLists.binarySearch(this.elements, obj, unsafeComparator(), KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.INVERTED_INSERTION_INDEX);
            return binarySearch >= 0 ? binarySearch : -1;
        } catch (ClassCastException e) {
            return -1;
        }
    }

    public boolean isEmpty() {
        return false;
    }

    boolean isPartialView() {
        return this.elements.isPartialView();
    }

    public UnmodifiableIterator<E> iterator() {
        return this.elements.iterator();
    }

    public E last() {
        return this.elements.get(size() - 1);
    }

    public int size() {
        return this.elements.size();
    }

    ImmutableSortedSet<E> subSetImpl(E e, boolean z, E e2, boolean z2) {
        return tailSetImpl(e, z).headSetImpl(e2, z2);
    }

    int tailIndex(E e, boolean z) {
        return SortedLists.binarySearch(this.elements, Preconditions.checkNotNull(e), comparator(), z ? KeyPresentBehavior.FIRST_PRESENT : KeyPresentBehavior.FIRST_AFTER, KeyAbsentBehavior.NEXT_HIGHER);
    }

    ImmutableSortedSet<E> tailSetImpl(E e, boolean z) {
        return getSubSet(tailIndex(e, z), size());
    }

    public Object[] toArray() {
        return this.elements.toArray();
    }

    public <T> T[] toArray(T[] tArr) {
        return this.elements.toArray(tArr);
    }

    Comparator<Object> unsafeComparator() {
        return this.comparator;
    }
}
