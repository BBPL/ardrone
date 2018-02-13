package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.parrot.ardronetool.ConfigArdroneMask;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
public final class Collections2 {
    static final Joiner STANDARD_JOINER = Joiner.on(", ").useForNull("null");

    static class FilteredCollection<E> implements Collection<E> {
        final Predicate<? super E> predicate;
        final Collection<E> unfiltered;

        FilteredCollection(Collection<E> collection, Predicate<? super E> predicate) {
            this.unfiltered = collection;
            this.predicate = predicate;
        }

        public boolean add(E e) {
            Preconditions.checkArgument(this.predicate.apply(e));
            return this.unfiltered.add(e);
        }

        public boolean addAll(Collection<? extends E> collection) {
            for (Object apply : collection) {
                Preconditions.checkArgument(this.predicate.apply(apply));
            }
            return this.unfiltered.addAll(collection);
        }

        public void clear() {
            Iterables.removeIf(this.unfiltered, this.predicate);
        }

        public boolean contains(Object obj) {
            try {
                return this.predicate.apply(obj) && this.unfiltered.contains(obj);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e2) {
                return false;
            }
        }

        public boolean containsAll(Collection<?> collection) {
            for (Object contains : collection) {
                if (!contains(contains)) {
                    return false;
                }
            }
            return true;
        }

        FilteredCollection<E> createCombined(Predicate<? super E> predicate) {
            return new FilteredCollection(this.unfiltered, Predicates.and(this.predicate, predicate));
        }

        public boolean isEmpty() {
            return !Iterators.any(this.unfiltered.iterator(), this.predicate);
        }

        public Iterator<E> iterator() {
            return Iterators.filter(this.unfiltered.iterator(), this.predicate);
        }

        public boolean remove(Object obj) {
            try {
                return this.predicate.apply(obj) && this.unfiltered.remove(obj);
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e2) {
                return false;
            }
        }

        public boolean removeAll(final Collection<?> collection) {
            Preconditions.checkNotNull(collection);
            return Iterables.removeIf(this.unfiltered, new Predicate<E>() {
                public boolean apply(E e) {
                    return FilteredCollection.this.predicate.apply(e) && collection.contains(e);
                }
            });
        }

        public boolean retainAll(final Collection<?> collection) {
            Preconditions.checkNotNull(collection);
            return Iterables.removeIf(this.unfiltered, new Predicate<E>() {
                public boolean apply(E e) {
                    return FilteredCollection.this.predicate.apply(e) && !collection.contains(e);
                }
            });
        }

        public int size() {
            return Iterators.size(iterator());
        }

        public Object[] toArray() {
            return Lists.newArrayList(iterator()).toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return Lists.newArrayList(iterator()).toArray(tArr);
        }

        public String toString() {
            return Iterators.toString(iterator());
        }
    }

    private static final class OrderedPermutationCollection<E> extends AbstractCollection<List<E>> {
        final Comparator<? super E> comparator;
        final ImmutableList<E> inputList;
        final int size;

        OrderedPermutationCollection(Iterable<E> iterable, Comparator<? super E> comparator) {
            this.inputList = Ordering.from((Comparator) comparator).immutableSortedCopy(iterable);
            this.comparator = comparator;
            this.size = calculateSize(this.inputList, comparator);
        }

        private static <E> int calculateSize(List<E> list, Comparator<? super E> comparator) {
            int i = 1;
            long j = 1;
            int i2 = 1;
            while (i < list.size()) {
                if (comparator.compare(list.get(i - 1), list.get(i)) < 0) {
                    j *= LongMath.binomial(i, i2);
                    i2 = 0;
                    if (!Collections2.isPositiveInt(j)) {
                        break;
                    }
                }
                i++;
                i2++;
            }
            j *= LongMath.binomial(i, i2);
            if (Collections2.isPositiveInt(j)) {
                return (int) j;
            }
            return Integer.MAX_VALUE;
        }

        public boolean contains(@Nullable Object obj) {
            if (!(obj instanceof List)) {
                return false;
            }
            return Collections2.isPermutation(this.inputList, (List) obj);
        }

        public boolean isEmpty() {
            return false;
        }

        public Iterator<List<E>> iterator() {
            return new OrderedPermutationIterator(this.inputList, this.comparator);
        }

        public int size() {
            return this.size;
        }

        public String toString() {
            return "orderedPermutationCollection(" + this.inputList + ")";
        }
    }

    private static final class OrderedPermutationIterator<E> extends AbstractIterator<List<E>> {
        final Comparator<? super E> comparator;
        List<E> nextPermutation;

        OrderedPermutationIterator(List<E> list, Comparator<? super E> comparator) {
            this.nextPermutation = Lists.newArrayList((Iterable) list);
            this.comparator = comparator;
        }

        void calculateNextPermutation() {
            int findNextJ = findNextJ();
            if (findNextJ == -1) {
                this.nextPermutation = null;
                return;
            }
            Collections.swap(this.nextPermutation, findNextJ, findNextL(findNextJ));
            Collections.reverse(this.nextPermutation.subList(findNextJ + 1, this.nextPermutation.size()));
        }

        protected List<E> computeNext() {
            if (this.nextPermutation == null) {
                return (List) endOfData();
            }
            List copyOf = ImmutableList.copyOf(this.nextPermutation);
            calculateNextPermutation();
            return copyOf;
        }

        int findNextJ() {
            for (int size = this.nextPermutation.size() - 2; size >= 0; size--) {
                if (this.comparator.compare(this.nextPermutation.get(size), this.nextPermutation.get(size + 1)) < 0) {
                    return size;
                }
            }
            return -1;
        }

        int findNextL(int i) {
            Object obj = this.nextPermutation.get(i);
            for (int size = this.nextPermutation.size() - 1; size > i; size--) {
                if (this.comparator.compare(obj, this.nextPermutation.get(size)) < 0) {
                    return size;
                }
            }
            throw new AssertionError("this statement should be unreachable");
        }
    }

    private static final class PermutationCollection<E> extends AbstractCollection<List<E>> {
        final ImmutableList<E> inputList;

        PermutationCollection(ImmutableList<E> immutableList) {
            this.inputList = immutableList;
        }

        public boolean contains(@Nullable Object obj) {
            if (!(obj instanceof List)) {
                return false;
            }
            return Collections2.isPermutation(this.inputList, (List) obj);
        }

        public boolean isEmpty() {
            return false;
        }

        public Iterator<List<E>> iterator() {
            return new PermutationIterator(this.inputList);
        }

        public int size() {
            return IntMath.factorial(this.inputList.size());
        }

        public String toString() {
            return "permutations(" + this.inputList + ")";
        }
    }

    private static class PermutationIterator<E> extends AbstractIterator<List<E>> {
        final int[] f269c;
        int f270j;
        final List<E> list;
        final int[] f271o;

        PermutationIterator(List<E> list) {
            this.list = new ArrayList(list);
            int size = list.size();
            this.f269c = new int[size];
            this.f271o = new int[size];
            for (int i = 0; i < size; i++) {
                this.f269c[i] = 0;
                this.f271o[i] = 1;
            }
            this.f270j = Integer.MAX_VALUE;
        }

        void calculateNextPermutation() {
            this.f270j = this.list.size() - 1;
            int i = 0;
            if (this.f270j != -1) {
                while (true) {
                    int i2 = this.f269c[this.f270j] + this.f271o[this.f270j];
                    if (i2 < 0) {
                        switchDirection();
                    } else if (i2 != this.f270j + 1) {
                        Collections.swap(this.list, (this.f270j - this.f269c[this.f270j]) + i, i + (this.f270j - i2));
                        this.f269c[this.f270j] = i2;
                        return;
                    } else if (this.f270j != 0) {
                        i++;
                        switchDirection();
                    } else {
                        return;
                    }
                }
            }
        }

        protected List<E> computeNext() {
            if (this.f270j <= 0) {
                return (List) endOfData();
            }
            List copyOf = ImmutableList.copyOf(this.list);
            calculateNextPermutation();
            return copyOf;
        }

        void switchDirection() {
            this.f271o[this.f270j] = -this.f271o[this.f270j];
            this.f270j--;
        }
    }

    static class TransformedCollection<F, T> extends AbstractCollection<T> {
        final Collection<F> fromCollection;
        final Function<? super F, ? extends T> function;

        TransformedCollection(Collection<F> collection, Function<? super F, ? extends T> function) {
            this.fromCollection = (Collection) Preconditions.checkNotNull(collection);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        public void clear() {
            this.fromCollection.clear();
        }

        public boolean isEmpty() {
            return this.fromCollection.isEmpty();
        }

        public Iterator<T> iterator() {
            return Iterators.transform(this.fromCollection.iterator(), this.function);
        }

        public int size() {
            return this.fromCollection.size();
        }
    }

    private Collections2() {
    }

    static <T> Collection<T> cast(Iterable<T> iterable) {
        return (Collection) iterable;
    }

    static boolean containsAllImpl(Collection<?> collection, Collection<?> collection2) {
        Preconditions.checkNotNull(collection);
        for (Object contains : collection2) {
            if (!collection.contains(contains)) {
                return false;
            }
        }
        return true;
    }

    public static <E> Collection<E> filter(Collection<E> collection, Predicate<? super E> predicate) {
        return collection instanceof FilteredCollection ? ((FilteredCollection) collection).createCombined(predicate) : new FilteredCollection((Collection) Preconditions.checkNotNull(collection), (Predicate) Preconditions.checkNotNull(predicate));
    }

    private static boolean isPermutation(List<?> list, List<?> list2) {
        return list.size() != list2.size() ? false : HashMultiset.create((Iterable) list).equals(HashMultiset.create((Iterable) list2));
    }

    private static boolean isPositiveInt(long j) {
        return j >= 0 && j <= 2147483647L;
    }

    static StringBuilder newStringBuilderForCollection(int i) {
        Preconditions.checkArgument(i >= 0, "size must be non-negative");
        return new StringBuilder((int) Math.min(((long) i) * 8, ConfigArdroneMask.ARDRONE_COM_WATCHDOG_MASK));
    }

    @Beta
    public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> iterable) {
        return orderedPermutations(iterable, Ordering.natural());
    }

    @Beta
    public static <E> Collection<List<E>> orderedPermutations(Iterable<E> iterable, Comparator<? super E> comparator) {
        return new OrderedPermutationCollection(iterable, comparator);
    }

    @Beta
    public static <E> Collection<List<E>> permutations(Collection<E> collection) {
        return new PermutationCollection(ImmutableList.copyOf((Collection) collection));
    }

    static boolean safeContains(Collection<?> collection, Object obj) {
        try {
            return collection.contains(obj);
        } catch (ClassCastException e) {
            return false;
        }
    }

    static String toStringImpl(final Collection<?> collection) {
        StringBuilder append = newStringBuilderForCollection(collection.size()).append('[');
        STANDARD_JOINER.appendTo(append, Iterables.transform(collection, new Function<Object, Object>() {
            public Object apply(Object obj) {
                return obj == collection ? "(this Collection)" : obj;
            }
        }));
        return append.append(']').toString();
    }

    public static <F, T> Collection<T> transform(Collection<F> collection, Function<? super F, T> function) {
        return new TransformedCollection(collection, function);
    }
}
