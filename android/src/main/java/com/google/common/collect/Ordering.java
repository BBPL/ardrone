package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class Ordering<T> implements Comparator<T> {
    static final int LEFT_IS_GREATER = 1;
    static final int RIGHT_IS_GREATER = -1;

    @VisibleForTesting
    static class ArbitraryOrdering extends Ordering<Object> {
        private Map<Object, Integer> uids = Platform.tryWeakKeys(new MapMaker()).makeComputingMap(new C07181());

        class C07181 implements Function<Object, Integer> {
            final AtomicInteger counter = new AtomicInteger(0);

            C07181() {
            }

            public Integer apply(Object obj) {
                return Integer.valueOf(this.counter.getAndIncrement());
            }
        }

        ArbitraryOrdering() {
        }

        public int compare(Object obj, Object obj2) {
            if (obj == obj2) {
                return 0;
            }
            if (obj == null) {
                return -1;
            }
            if (obj2 == null) {
                return 1;
            }
            int identityHashCode = identityHashCode(obj);
            int identityHashCode2 = identityHashCode(obj2);
            if (identityHashCode != identityHashCode2) {
                return identityHashCode >= identityHashCode2 ? 1 : -1;
            } else {
                int compareTo = ((Integer) this.uids.get(obj)).compareTo((Integer) this.uids.get(obj2));
                if (compareTo != 0) {
                    return compareTo;
                }
                throw new AssertionError();
            }
        }

        int identityHashCode(Object obj) {
            return System.identityHashCode(obj);
        }

        public String toString() {
            return "Ordering.arbitrary()";
        }
    }

    private static class ArbitraryOrderingHolder {
        static final Ordering<Object> ARBITRARY_ORDERING = new ArbitraryOrdering();

        private ArbitraryOrderingHolder() {
        }
    }

    @VisibleForTesting
    static class IncomparableValueException extends ClassCastException {
        private static final long serialVersionUID = 0;
        final Object value;

        IncomparableValueException(Object obj) {
            super("Cannot compare value: " + obj);
            this.value = obj;
        }
    }

    protected Ordering() {
    }

    @GwtCompatible(serializable = true)
    public static Ordering<Object> allEqual() {
        return AllEqualOrdering.INSTANCE;
    }

    public static Ordering<Object> arbitrary() {
        return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
    }

    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> iterable) {
        return new CompoundOrdering(iterable);
    }

    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> explicit(T t, T... tArr) {
        return explicit(Lists.asList(t, tArr));
    }

    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> explicit(List<T> list) {
        return new ExplicitOrdering((List) list);
    }

    @GwtCompatible(serializable = true)
    @Deprecated
    public static <T> Ordering<T> from(Ordering<T> ordering) {
        return (Ordering) Preconditions.checkNotNull(ordering);
    }

    @GwtCompatible(serializable = true)
    public static <T> Ordering<T> from(Comparator<T> comparator) {
        return comparator instanceof Ordering ? (Ordering) comparator : new ComparatorOrdering(comparator);
    }

    @GwtCompatible(serializable = true)
    public static <C extends Comparable> Ordering<C> natural() {
        return NaturalOrdering.INSTANCE;
    }

    private <E extends T> int partition(E[] eArr, int i, int i2, int i3) {
        Object obj = eArr[i3];
        eArr[i3] = eArr[i2];
        eArr[i2] = obj;
        int i4 = i;
        while (i < i2) {
            if (compare(eArr[i], obj) < 0) {
                ObjectArrays.swap(eArr, i4, i);
                i4++;
            }
            i++;
        }
        ObjectArrays.swap(eArr, i2, i4);
        return i4;
    }

    private <E extends T> void quicksortLeastK(E[] eArr, int i, int i2, int i3) {
        if (i2 > i) {
            int partition = partition(eArr, i, i2, (i + i2) >>> 1);
            quicksortLeastK(eArr, i, partition - 1, i3);
            if (partition < i3) {
                quicksortLeastK(eArr, partition + 1, i2, i3);
            }
        }
    }

    @GwtCompatible(serializable = true)
    public static Ordering<Object> usingToString() {
        return UsingToStringOrdering.INSTANCE;
    }

    public int binarySearch(List<? extends T> list, @Nullable T t) {
        return Collections.binarySearch(list, t, this);
    }

    public abstract int compare(@Nullable T t, @Nullable T t2);

    @GwtCompatible(serializable = true)
    public <U extends T> Ordering<U> compound(Comparator<? super U> comparator) {
        return new CompoundOrdering(this, (Comparator) Preconditions.checkNotNull(comparator));
    }

    @Beta
    public <E extends T> List<E> greatestOf(Iterable<E> iterable, int i) {
        return reverse().leastOf(iterable, i);
    }

    public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> iterable) {
        Object[] toArray = Iterables.toArray(iterable);
        for (Object checkNotNull : toArray) {
            Preconditions.checkNotNull(checkNotNull);
        }
        Arrays.sort(toArray, this);
        return ImmutableList.asImmutableList(toArray);
    }

    public boolean isOrdered(Iterable<? extends T> iterable) {
        Iterator it = iterable.iterator();
        if (it.hasNext()) {
            Object next = it.next();
            while (it.hasNext()) {
                Object next2 = it.next();
                if (compare(next, next2) > 0) {
                    return false;
                }
                next = next2;
            }
        }
        return true;
    }

    public boolean isStrictlyOrdered(Iterable<? extends T> iterable) {
        Iterator it = iterable.iterator();
        if (it.hasNext()) {
            Object next = it.next();
            while (it.hasNext()) {
                Object next2 = it.next();
                if (compare(next, next2) >= 0) {
                    return false;
                }
                next = next2;
            }
        }
        return true;
    }

    @Beta
    public <E extends T> List<E> leastOf(Iterable<E> iterable, int i) {
        Preconditions.checkArgument(i >= 0, "%d is negative", Integer.valueOf(i));
        Object[] toArray = Iterables.toArray(iterable);
        if (toArray.length <= i) {
            Arrays.sort(toArray, this);
        } else {
            quicksortLeastK(toArray, 0, toArray.length - 1, i);
            Object[] objArr = new Object[i];
            System.arraycopy(toArray, 0, objArr, 0, i);
            toArray = objArr;
        }
        return Collections.unmodifiableList(Arrays.asList(toArray));
    }

    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<Iterable<S>> lexicographical() {
        return new LexicographicalOrdering(this);
    }

    public <E extends T> E max(Iterable<E> iterable) {
        return max(iterable.iterator());
    }

    public <E extends T> E max(@Nullable E e, @Nullable E e2) {
        return compare(e, e2) >= 0 ? e : e2;
    }

    public <E extends T> E max(@Nullable E e, @Nullable E e2, @Nullable E e3, E... eArr) {
        E max = max(max(e, e2), e3);
        for (Object max2 : eArr) {
            max = max(max, max2);
        }
        return max;
    }

    @Beta
    public <E extends T> E max(Iterator<E> it) {
        E next = it.next();
        while (it.hasNext()) {
            next = max(next, it.next());
        }
        return next;
    }

    public <E extends T> E min(Iterable<E> iterable) {
        return min(iterable.iterator());
    }

    public <E extends T> E min(@Nullable E e, @Nullable E e2) {
        return compare(e, e2) <= 0 ? e : e2;
    }

    public <E extends T> E min(@Nullable E e, @Nullable E e2, @Nullable E e3, E... eArr) {
        E min = min(min(e, e2), e3);
        for (Object min2 : eArr) {
            min = min(min, min2);
        }
        return min;
    }

    @Beta
    public <E extends T> E min(Iterator<E> it) {
        E next = it.next();
        while (it.hasNext()) {
            next = min(next, it.next());
        }
        return next;
    }

    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> nullsFirst() {
        return new NullsFirstOrdering(this);
    }

    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> nullsLast() {
        return new NullsLastOrdering(this);
    }

    @GwtCompatible(serializable = true)
    public <F> Ordering<F> onResultOf(Function<F, ? extends T> function) {
        return new ByFunctionOrdering(function, this);
    }

    @GwtCompatible(serializable = true)
    public <S extends T> Ordering<S> reverse() {
        return new ReverseOrdering(this);
    }

    public <E extends T> List<E> sortedCopy(Iterable<E> iterable) {
        Object[] toArray = Iterables.toArray(iterable);
        Arrays.sort(toArray, this);
        return Lists.newArrayList(Arrays.asList(toArray));
    }
}
