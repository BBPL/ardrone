package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Iterables {

    private static class ConsumingQueueIterator<T> extends AbstractIterator<T> {
        private final Queue<T> queue;

        private ConsumingQueueIterator(Queue<T> queue) {
            this.queue = queue;
        }

        public T computeNext() {
            try {
                return this.queue.remove();
            } catch (NoSuchElementException e) {
                return endOfData();
            }
        }
    }

    private static final class UnmodifiableIterable<T> extends FluentIterable<T> {
        private final Iterable<T> iterable;

        private UnmodifiableIterable(Iterable<T> iterable) {
            this.iterable = iterable;
        }

        public Iterator<T> iterator() {
            return Iterators.unmodifiableIterator(this.iterable.iterator());
        }

        public String toString() {
            return this.iterable.toString();
        }
    }

    private Iterables() {
    }

    public static <T> boolean addAll(Collection<T> collection, Iterable<? extends T> iterable) {
        return iterable instanceof Collection ? collection.addAll(Collections2.cast(iterable)) : Iterators.addAll(collection, iterable.iterator());
    }

    public static <T> boolean all(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.all(iterable.iterator(), predicate);
    }

    public static <T> boolean any(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.any(iterable.iterator(), predicate);
    }

    private static void checkNonnegativeIndex(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("position cannot be negative: " + i);
        }
    }

    public static <T> Iterable<T> concat(final Iterable<? extends Iterable<? extends T>> iterable) {
        Preconditions.checkNotNull(iterable);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.concat(Iterables.iterators(iterable));
            }
        };
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> iterable, Iterable<? extends T> iterable2) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(iterable2);
        return concat(Arrays.asList(new Iterable[]{iterable, iterable2}));
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> iterable, Iterable<? extends T> iterable2, Iterable<? extends T> iterable3) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(iterable2);
        Preconditions.checkNotNull(iterable3);
        return concat(Arrays.asList(new Iterable[]{iterable, iterable2, iterable3}));
    }

    public static <T> Iterable<T> concat(Iterable<? extends T> iterable, Iterable<? extends T> iterable2, Iterable<? extends T> iterable3, Iterable<? extends T> iterable4) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(iterable2);
        Preconditions.checkNotNull(iterable3);
        Preconditions.checkNotNull(iterable4);
        return concat(Arrays.asList(new Iterable[]{iterable, iterable2, iterable3, iterable4}));
    }

    public static <T> Iterable<T> concat(Iterable<? extends T>... iterableArr) {
        return concat(ImmutableList.copyOf((Object[]) iterableArr));
    }

    public static <T> Iterable<T> consumingIterable(final Iterable<T> iterable) {
        if (iterable instanceof Queue) {
            return new FluentIterable<T>() {
                public Iterator<T> iterator() {
                    return new ConsumingQueueIterator((Queue) iterable);
                }
            };
        }
        Preconditions.checkNotNull(iterable);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.consumingIterator(iterable.iterator());
            }
        };
    }

    public static boolean contains(Iterable<?> iterable, @Nullable Object obj) {
        boolean z = false;
        if (!(iterable instanceof Collection)) {
            return Iterators.contains(iterable.iterator(), obj);
        }
        try {
            return ((Collection) iterable).contains(obj);
        } catch (NullPointerException e) {
            return z;
        } catch (ClassCastException e2) {
            return z;
        }
    }

    public static <T> Iterable<T> cycle(final Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.cycle(iterable);
            }

            public String toString() {
                return iterable.toString() + " (cycled)";
            }
        };
    }

    public static <T> Iterable<T> cycle(T... tArr) {
        return cycle(Lists.newArrayList((Object[]) tArr));
    }

    public static boolean elementsEqual(Iterable<?> iterable, Iterable<?> iterable2) {
        return Iterators.elementsEqual(iterable.iterator(), iterable2.iterator());
    }

    public static <T> Iterable<T> filter(final Iterable<T> iterable, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(predicate);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.filter(iterable.iterator(), predicate);
            }
        };
    }

    @GwtIncompatible("Class.isInstance")
    public static <T> Iterable<T> filter(final Iterable<?> iterable, final Class<T> cls) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(cls);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.filter(iterable.iterator(), cls);
            }
        };
    }

    public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.find(iterable.iterator(), predicate);
    }

    public static <T> T find(Iterable<? extends T> iterable, Predicate<? super T> predicate, @Nullable T t) {
        return Iterators.find(iterable.iterator(), predicate, t);
    }

    public static int frequency(Iterable<?> iterable, @Nullable Object obj) {
        return iterable instanceof Multiset ? ((Multiset) iterable).count(obj) : iterable instanceof Set ? ((Set) iterable).contains(obj) ? 1 : 0 : Iterators.frequency(iterable.iterator(), obj);
    }

    public static <T> T get(Iterable<T> iterable, int i) {
        Preconditions.checkNotNull(iterable);
        if (iterable instanceof List) {
            return ((List) iterable).get(i);
        }
        if (iterable instanceof Collection) {
            Preconditions.checkElementIndex(i, ((Collection) iterable).size());
        } else {
            checkNonnegativeIndex(i);
        }
        return Iterators.get(iterable.iterator(), i);
    }

    public static <T> T get(Iterable<? extends T> iterable, int i, @Nullable T t) {
        Preconditions.checkNotNull(iterable);
        checkNonnegativeIndex(i);
        try {
            t = get(iterable, i);
        } catch (IndexOutOfBoundsException e) {
        }
        return t;
    }

    public static <T> T getFirst(Iterable<? extends T> iterable, @Nullable T t) {
        return Iterators.getNext(iterable.iterator(), t);
    }

    public static <T> T getLast(Iterable<T> iterable) {
        if (!(iterable instanceof List)) {
            return iterable instanceof SortedSet ? ((SortedSet) iterable).last() : Iterators.getLast(iterable.iterator());
        } else {
            List list = (List) iterable;
            if (!list.isEmpty()) {
                return getLastInNonemptyList(list);
            }
            throw new NoSuchElementException();
        }
    }

    public static <T> T getLast(Iterable<? extends T> iterable, @Nullable T t) {
        return ((iterable instanceof Collection) && Collections2.cast(iterable).isEmpty()) ? t : iterable instanceof List ? getLastInNonemptyList(Lists.cast(iterable)) : iterable instanceof SortedSet ? Sets.cast(iterable).last() : Iterators.getLast(iterable.iterator(), t);
    }

    private static <T> T getLastInNonemptyList(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static <T> T getOnlyElement(Iterable<T> iterable) {
        return Iterators.getOnlyElement(iterable.iterator());
    }

    public static <T> T getOnlyElement(Iterable<? extends T> iterable, @Nullable T t) {
        return Iterators.getOnlyElement(iterable.iterator(), t);
    }

    public static <T> int indexOf(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.indexOf(iterable.iterator(), predicate);
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        return iterable instanceof Collection ? ((Collection) iterable).isEmpty() : !iterable.iterator().hasNext();
    }

    private static <T> UnmodifiableIterator<Iterator<? extends T>> iterators(Iterable<? extends Iterable<? extends T>> iterable) {
        final Iterator it = iterable.iterator();
        return new UnmodifiableIterator<Iterator<? extends T>>() {
            public boolean hasNext() {
                return it.hasNext();
            }

            public Iterator<? extends T> next() {
                return ((Iterable) it.next()).iterator();
            }
        };
    }

    public static <T> Iterable<T> limit(final Iterable<T> iterable, final int i) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(i >= 0, "limit is negative");
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.limit(iterable.iterator(), i);
            }
        };
    }

    @Beta
    public static <T> Iterable<T> mergeSorted(final Iterable<? extends Iterable<? extends T>> iterable, final Comparator<? super T> comparator) {
        Preconditions.checkNotNull(iterable, "iterables");
        Preconditions.checkNotNull(comparator, "comparator");
        return new UnmodifiableIterable(new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.mergeSorted(Iterables.transform(iterable, Iterables.toIterator()), comparator);
            }
        });
    }

    public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> iterable, final int i) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(i > 0);
        return new FluentIterable<List<T>>() {
            public Iterator<List<T>> iterator() {
                return Iterators.paddedPartition(iterable.iterator(), i);
            }
        };
    }

    public static <T> Iterable<List<T>> partition(final Iterable<T> iterable, final int i) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(i > 0);
        return new FluentIterable<List<T>>() {
            public Iterator<List<T>> iterator() {
                return Iterators.partition(iterable.iterator(), i);
            }
        };
    }

    public static boolean removeAll(Iterable<?> iterable, Collection<?> collection) {
        return iterable instanceof Collection ? ((Collection) iterable).removeAll((Collection) Preconditions.checkNotNull(collection)) : Iterators.removeAll(iterable.iterator(), collection);
    }

    public static <T> boolean removeIf(Iterable<T> iterable, Predicate<? super T> predicate) {
        return ((iterable instanceof RandomAccess) && (iterable instanceof List)) ? removeIfFromRandomAccessList((List) iterable, (Predicate) Preconditions.checkNotNull(predicate)) : Iterators.removeIf(iterable.iterator(), predicate);
    }

    private static <T> boolean removeIfFromRandomAccessList(List<T> list, Predicate<? super T> predicate) {
        int i = 0;
        int i2 = 0;
        while (i2 < list.size()) {
            Object obj = list.get(i2);
            if (!predicate.apply(obj)) {
                if (i2 > i) {
                    try {
                        list.set(i, obj);
                    } catch (UnsupportedOperationException e) {
                        slowRemoveIfForRemainingElements(list, predicate, i, i2);
                    }
                }
                i++;
            }
            i2++;
        }
        list.subList(i, list.size()).clear();
        if (i2 == i) {
            return false;
        }
        return true;
    }

    public static boolean retainAll(Iterable<?> iterable, Collection<?> collection) {
        return iterable instanceof Collection ? ((Collection) iterable).retainAll((Collection) Preconditions.checkNotNull(collection)) : Iterators.retainAll(iterable.iterator(), collection);
    }

    public static int size(Iterable<?> iterable) {
        return iterable instanceof Collection ? ((Collection) iterable).size() : Iterators.size(iterable.iterator());
    }

    public static <T> Iterable<T> skip(final Iterable<T> iterable, final int i) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkArgument(i >= 0, "number to skip cannot be negative");
        if (!(iterable instanceof List)) {
            return new FluentIterable<T>() {
                public Iterator<T> iterator() {
                    final Iterator it = iterable.iterator();
                    Iterators.advance(it, i);
                    return new Iterator<T>() {
                        boolean atStart = true;

                        public boolean hasNext() {
                            return it.hasNext();
                        }

                        public T next() {
                            if (hasNext()) {
                                try {
                                    T next = it.next();
                                    return next;
                                } finally {
                                    this.atStart = false;
                                }
                            } else {
                                throw new NoSuchElementException();
                            }
                        }

                        public void remove() {
                            if (this.atStart) {
                                throw new IllegalStateException();
                            }
                            it.remove();
                        }
                    };
                }
            };
        }
        final List list = (List) iterable;
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return i >= list.size() ? Iterators.emptyIterator() : list.subList(i, list.size()).iterator();
            }
        };
    }

    private static <T> void slowRemoveIfForRemainingElements(List<T> list, Predicate<? super T> predicate, int i, int i2) {
        int size;
        for (size = list.size() - 1; size > i2; size--) {
            if (predicate.apply(list.get(size))) {
                list.remove(size);
            }
        }
        for (size = i2 - 1; size >= i; size--) {
            list.remove(size);
        }
    }

    static Object[] toArray(Iterable<?> iterable) {
        return toCollection(iterable).toArray();
    }

    @GwtIncompatible("Array.newInstance(Class, int)")
    public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> cls) {
        Collection toCollection = toCollection(iterable);
        return toCollection.toArray(ObjectArrays.newArray((Class) cls, toCollection.size()));
    }

    private static <E> Collection<E> toCollection(Iterable<E> iterable) {
        return iterable instanceof Collection ? (Collection) iterable : Lists.newArrayList(iterable.iterator());
    }

    private static <T> Function<Iterable<? extends T>, Iterator<? extends T>> toIterator() {
        return new Function<Iterable<? extends T>, Iterator<? extends T>>() {
            public Iterator<? extends T> apply(Iterable<? extends T> iterable) {
                return iterable.iterator();
            }
        };
    }

    public static String toString(Iterable<?> iterable) {
        return Iterators.toString(iterable.iterator());
    }

    public static <F, T> Iterable<T> transform(final Iterable<F> iterable, final Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(iterable);
        Preconditions.checkNotNull(function);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return Iterators.transform(iterable.iterator(), function);
            }
        };
    }

    public static <T> Optional<T> tryFind(Iterable<T> iterable, Predicate<? super T> predicate) {
        return Iterators.tryFind(iterable.iterator(), predicate);
    }

    @Deprecated
    public static <E> Iterable<E> unmodifiableIterable(ImmutableCollection<E> immutableCollection) {
        return (Iterable) Preconditions.checkNotNull(immutableCollection);
    }

    public static <T> Iterable<T> unmodifiableIterable(Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        return ((iterable instanceof UnmodifiableIterable) || (iterable instanceof ImmutableCollection)) ? iterable : new UnmodifiableIterable(iterable);
    }
}
