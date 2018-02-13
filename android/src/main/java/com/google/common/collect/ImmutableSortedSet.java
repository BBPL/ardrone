package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableSortedSet<E> extends ImmutableSortedSetFauxverideShim<E> implements SortedSet<E>, SortedIterable<E> {
    private static final ImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new EmptyImmutableSortedSet(NATURAL_ORDER);
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
    final transient Comparator<? super E> comparator;
    @GwtIncompatible("NavigableSet")
    transient ImmutableSortedSet<E> descendingSet;

    public static final class Builder<E> extends com.google.common.collect.ImmutableSet.Builder<E> {
        private final Comparator<? super E> comparator;

        public Builder(Comparator<? super E> comparator) {
            this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        }

        public Builder<E> add(E e) {
            super.add((Object) e);
            return this;
        }

        public Builder<E> add(E... eArr) {
            super.add((Object[]) eArr);
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> iterable) {
            super.addAll((Iterable) iterable);
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> it) {
            super.addAll((Iterator) it);
            return this;
        }

        public ImmutableSortedSet<E> build() {
            ImmutableSortedSet<E> construct = ImmutableSortedSet.construct(this.comparator, this.size, this.contents);
            this.size = construct.size();
            return construct;
        }
    }

    private static class SerializedForm<E> implements Serializable {
        private static final long serialVersionUID = 0;
        final Comparator<? super E> comparator;
        final Object[] elements;

        public SerializedForm(Comparator<? super E> comparator, Object[] objArr) {
            this.comparator = comparator;
            this.elements = objArr;
        }

        Object readResolve() {
            return new Builder(this.comparator).add(this.elements).build();
        }
    }

    ImmutableSortedSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    static <E> ImmutableSortedSet<E> construct(Comparator<? super E> comparator, int i, E... eArr) {
        int sortAndUnique = sortAndUnique(comparator, i, eArr);
        if (sortAndUnique == 0) {
            return emptySet(comparator);
        }
        Object[] arraysCopyOf;
        if (sortAndUnique < eArr.length) {
            arraysCopyOf = ObjectArrays.arraysCopyOf(eArr, sortAndUnique);
        }
        return new RegularImmutableSortedSet(ImmutableList.asImmutableList(arraysCopyOf), comparator);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> iterable) {
        return copyOf(Ordering.natural(), (Iterable) iterable);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> collection) {
        return copyOf(Ordering.natural(), (Collection) collection);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> iterable) {
        Preconditions.checkNotNull(comparator);
        if (SortedIterables.hasSameComparator(comparator, iterable) && (iterable instanceof ImmutableSortedSet)) {
            ImmutableSortedSet<E> immutableSortedSet = (ImmutableSortedSet) iterable;
            if (!immutableSortedSet.isPartialView()) {
                return immutableSortedSet;
            }
        }
        Object[] toArray = Iterables.toArray(iterable);
        return construct(comparator, toArray.length, toArray);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> collection) {
        return copyOf((Comparator) comparator, (Iterable) collection);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> it) {
        return copyOf((Comparator) comparator, Lists.newArrayList((Iterator) it));
    }

    public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> it) {
        return copyOf(Ordering.natural(), (Iterator) it);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] eArr) {
        return copyOf(Ordering.natural(), Arrays.asList(eArr));
    }

    public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
        Comparator comparator = SortedIterables.comparator(sortedSet);
        Object[] toArray = sortedSet.toArray();
        return toArray.length == 0 ? emptySet(comparator) : new RegularImmutableSortedSet(ImmutableList.asImmutableList(toArray), comparator);
    }

    private static <E> ImmutableSortedSet<E> emptySet() {
        return NATURAL_EMPTY_SET;
    }

    static <E> ImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
        return NATURAL_ORDER.equals(comparator) ? emptySet() : new EmptyImmutableSortedSet(comparator);
    }

    public static <E extends Comparable<E>> Builder<E> naturalOrder() {
        return new Builder(Ordering.natural());
    }

    public static <E> ImmutableSortedSet<E> of() {
        return emptySet();
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e) {
        return new RegularImmutableSortedSet(ImmutableList.of(e), Ordering.natural());
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e, E e2) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{e, e2}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e, E e2, E e3) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{e, e2, e3}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e, E e2, E e3, E e4) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{e, e2, e3, e4}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e, E e2, E e3, E e4, E e5) {
        return copyOf(Ordering.natural(), Arrays.asList(new Comparable[]{e, e2, e3, e4, e5}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e, E e2, E e3, E e4, E e5, E e6, E... eArr) {
        Collection arrayList = new ArrayList(eArr.length + 6);
        Collections.addAll(arrayList, new Comparable[]{e, e2, e3, e4, e5, e6});
        Collections.addAll(arrayList, eArr);
        return copyOf(Ordering.natural(), arrayList);
    }

    public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
        return new Builder(comparator);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    public static <E extends Comparable<E>> Builder<E> reverseOrder() {
        return new Builder(Ordering.natural().reverse());
    }

    static <E> int sortAndUnique(Comparator<? super E> comparator, int i, E... eArr) {
        int i2 = 0;
        if (i != 0) {
            int i3;
            for (i3 = 0; i3 < i; i3++) {
                ObjectArrays.checkElementNotNull(eArr[i3], i3);
            }
            Arrays.sort(eArr, 0, i, comparator);
            i3 = 1;
            i2 = 1;
            while (i3 < i) {
                int i4;
                Object obj = eArr[i3];
                if (comparator.compare(obj, eArr[i2 - 1]) != 0) {
                    i4 = i2 + 1;
                    eArr[i2] = obj;
                } else {
                    i4 = i2;
                }
                i3++;
                i2 = i4;
            }
            Arrays.fill(eArr, i2, i, null);
        }
        return i2;
    }

    static int unsafeCompare(Comparator<?> comparator, Object obj, Object obj2) {
        return comparator.compare(obj, obj2);
    }

    @GwtIncompatible("NavigableSet")
    public E ceiling(E e) {
        return Iterables.getFirst(tailSet(e, true), null);
    }

    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    @GwtIncompatible("NavigableSet")
    abstract ImmutableSortedSet<E> createDescendingSet();

    @GwtIncompatible("NavigableSet")
    public UnmodifiableIterator<E> descendingIterator() {
        return descendingSet().iterator();
    }

    @GwtIncompatible("NavigableSet")
    public ImmutableSortedSet<E> descendingSet() {
        ImmutableSortedSet<E> immutableSortedSet = this.descendingSet;
        if (immutableSortedSet != null) {
            return immutableSortedSet;
        }
        immutableSortedSet = createDescendingSet();
        this.descendingSet = immutableSortedSet;
        immutableSortedSet.descendingSet = this;
        return immutableSortedSet;
    }

    @GwtIncompatible("NavigableSet")
    public E floor(E e) {
        return Iterables.getFirst(headSet(e, true).descendingSet(), null);
    }

    public ImmutableSortedSet<E> headSet(E e) {
        return headSet(e, false);
    }

    @GwtIncompatible("NavigableSet")
    public ImmutableSortedSet<E> headSet(E e, boolean z) {
        return headSetImpl(Preconditions.checkNotNull(e), z);
    }

    abstract ImmutableSortedSet<E> headSetImpl(E e, boolean z);

    @GwtIncompatible("NavigableSet")
    public E higher(E e) {
        return Iterables.getFirst(tailSet(e, false), null);
    }

    abstract int indexOf(@Nullable Object obj);

    public abstract UnmodifiableIterator<E> iterator();

    @GwtIncompatible("NavigableSet")
    public E lower(E e) {
        return Iterables.getFirst(headSet(e, false).descendingSet(), null);
    }

    @GwtIncompatible("NavigableSet")
    public final E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @GwtIncompatible("NavigableSet")
    public final E pollLast() {
        throw new UnsupportedOperationException();
    }

    public ImmutableSortedSet<E> subSet(E e, E e2) {
        return subSet(e, true, e2, false);
    }

    @GwtIncompatible("NavigableSet")
    public ImmutableSortedSet<E> subSet(E e, boolean z, E e2, boolean z2) {
        Preconditions.checkNotNull(e);
        Preconditions.checkNotNull(e2);
        Preconditions.checkArgument(this.comparator.compare(e, e2) <= 0);
        return subSetImpl(e, z, e2, z2);
    }

    abstract ImmutableSortedSet<E> subSetImpl(E e, boolean z, E e2, boolean z2);

    public ImmutableSortedSet<E> tailSet(E e) {
        return tailSet(e, true);
    }

    @GwtIncompatible("NavigableSet")
    public ImmutableSortedSet<E> tailSet(E e, boolean z) {
        return tailSetImpl(Preconditions.checkNotNull(e), z);
    }

    abstract ImmutableSortedSet<E> tailSetImpl(E e, boolean z);

    int unsafeCompare(Object obj, Object obj2) {
        return unsafeCompare(this.comparator, obj, obj2);
    }

    Object writeReplace() {
        return new SerializedForm(this.comparator, toArray());
    }
}
