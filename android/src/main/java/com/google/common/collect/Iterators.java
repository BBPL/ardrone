package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Iterators {
    static final UnmodifiableListIterator<Object> EMPTY_LIST_ITERATOR = new C05991();
    private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new C06002();

    static final class C05991 extends UnmodifiableListIterator<Object> {
        C05991() {
        }

        public boolean hasNext() {
            return false;
        }

        public boolean hasPrevious() {
            return false;
        }

        public Object next() {
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return 0;
        }

        public Object previous() {
            throw new NoSuchElementException();
        }

        public int previousIndex() {
            return -1;
        }
    }

    static final class C06002 implements Iterator<Object> {
        C06002() {
        }

        public boolean hasNext() {
            return false;
        }

        public Object next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new IllegalStateException();
        }
    }

    private static class MergingIterator<T> extends AbstractIterator<T> {
        final Comparator<? super T> comparator;
        final Queue<PeekingIterator<T>> queue = new PriorityQueue(2, new C06081());

        class C06081 implements Comparator<PeekingIterator<T>> {
            C06081() {
            }

            public int compare(PeekingIterator<T> peekingIterator, PeekingIterator<T> peekingIterator2) {
                return MergingIterator.this.comparator.compare(peekingIterator.peek(), peekingIterator2.peek());
            }
        }

        public MergingIterator(Iterable<? extends Iterator<? extends T>> iterable, Comparator<? super T> comparator) {
            this.comparator = comparator;
            for (Iterator it : iterable) {
                if (it.hasNext()) {
                    this.queue.add(Iterators.peekingIterator(it));
                }
            }
        }

        protected T computeNext() {
            if (this.queue.isEmpty()) {
                return endOfData();
            }
            PeekingIterator peekingIterator = (PeekingIterator) this.queue.poll();
            T next = peekingIterator.next();
            if (!peekingIterator.hasNext()) {
                return next;
            }
            this.queue.add(peekingIterator);
            return next;
        }
    }

    private static class PeekingImpl<E> implements PeekingIterator<E> {
        private boolean hasPeeked;
        private final Iterator<? extends E> iterator;
        private E peekedElement;

        public PeekingImpl(Iterator<? extends E> it) {
            this.iterator = (Iterator) Preconditions.checkNotNull(it);
        }

        public boolean hasNext() {
            return this.hasPeeked || this.iterator.hasNext();
        }

        public E next() {
            if (!this.hasPeeked) {
                return this.iterator.next();
            }
            E e = this.peekedElement;
            this.hasPeeked = false;
            this.peekedElement = null;
            return e;
        }

        public E peek() {
            if (!this.hasPeeked) {
                this.peekedElement = this.iterator.next();
                this.hasPeeked = true;
            }
            return this.peekedElement;
        }

        public void remove() {
            Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
            this.iterator.remove();
        }
    }

    private Iterators() {
    }

    public static <T> boolean addAll(Collection<T> collection, Iterator<? extends T> it) {
        Preconditions.checkNotNull(collection);
        boolean z = false;
        while (it.hasNext()) {
            z |= collection.add(it.next());
        }
        return z;
    }

    public static int advance(Iterator<?> it, int i) {
        int i2 = 0;
        Preconditions.checkNotNull(it);
        Preconditions.checkArgument(i >= 0, "number to advance cannot be negative");
        while (i2 < i && it.hasNext()) {
            it.next();
            i2++;
        }
        return i2;
    }

    public static <T> boolean all(Iterator<T> it, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        while (it.hasNext()) {
            if (!predicate.apply(it.next())) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean any(Iterator<T> it, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        while (it.hasNext()) {
            if (predicate.apply(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static <T> Enumeration<T> asEnumeration(final Iterator<T> it) {
        Preconditions.checkNotNull(it);
        return new Enumeration<T>() {
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            public T nextElement() {
                return it.next();
            }
        };
    }

    static <T> ListIterator<T> cast(Iterator<T> it) {
        return (ListIterator) it;
    }

    private static void checkNonnegative(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("position (" + i + ") must not be negative");
        }
    }

    static void checkRemove(boolean z) {
        Preconditions.checkState(z, "no calls to next() since the last call to remove()");
    }

    static void clear(Iterator<?> it) {
        Preconditions.checkNotNull(it);
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    public static <T> Iterator<T> concat(final Iterator<? extends Iterator<? extends T>> it) {
        Preconditions.checkNotNull(it);
        return new Iterator<T>() {
            Iterator<? extends T> current = Iterators.emptyIterator();
            Iterator<? extends T> removeFrom;

            public boolean hasNext() {
                boolean hasNext;
                while (true) {
                    hasNext = ((Iterator) Preconditions.checkNotNull(this.current)).hasNext();
                    if (hasNext || !it.hasNext()) {
                        return hasNext;
                    }
                    this.current = (Iterator) it.next();
                }
                return hasNext;
            }

            public T next() {
                if (hasNext()) {
                    this.removeFrom = this.current;
                    return this.current.next();
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                Preconditions.checkState(this.removeFrom != null, "no calls to next() since last call to remove()");
                this.removeFrom.remove();
                this.removeFrom = null;
            }
        };
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> it, Iterator<? extends T> it2) {
        Preconditions.checkNotNull(it);
        Preconditions.checkNotNull(it2);
        return concat(Arrays.asList(new Iterator[]{it, it2}).iterator());
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> it, Iterator<? extends T> it2, Iterator<? extends T> it3) {
        Preconditions.checkNotNull(it);
        Preconditions.checkNotNull(it2);
        Preconditions.checkNotNull(it3);
        return concat(Arrays.asList(new Iterator[]{it, it2, it3}).iterator());
    }

    public static <T> Iterator<T> concat(Iterator<? extends T> it, Iterator<? extends T> it2, Iterator<? extends T> it3, Iterator<? extends T> it4) {
        Preconditions.checkNotNull(it);
        Preconditions.checkNotNull(it2);
        Preconditions.checkNotNull(it3);
        Preconditions.checkNotNull(it4);
        return concat(Arrays.asList(new Iterator[]{it, it2, it3, it4}).iterator());
    }

    public static <T> Iterator<T> concat(Iterator<? extends T>... itArr) {
        return concat(ImmutableList.copyOf((Object[]) itArr).iterator());
    }

    public static <T> Iterator<T> consumingIterator(final Iterator<T> it) {
        Preconditions.checkNotNull(it);
        return new UnmodifiableIterator<T>() {
            public boolean hasNext() {
                return it.hasNext();
            }

            public T next() {
                T next = it.next();
                it.remove();
                return next;
            }
        };
    }

    public static boolean contains(Iterator<?> it, @Nullable Object obj) {
        if (obj == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    return true;
                }
            }
        }
        while (it.hasNext()) {
            if (obj.equals(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable);
        return new Iterator<T>() {
            Iterator<T> iterator = Iterators.emptyIterator();
            Iterator<T> removeFrom;

            public boolean hasNext() {
                if (!this.iterator.hasNext()) {
                    this.iterator = iterable.iterator();
                }
                return this.iterator.hasNext();
            }

            public T next() {
                if (hasNext()) {
                    this.removeFrom = this.iterator;
                    return this.iterator.next();
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                Preconditions.checkState(this.removeFrom != null, "no calls to next() since last call to remove()");
                this.removeFrom.remove();
                this.removeFrom = null;
            }
        };
    }

    public static <T> Iterator<T> cycle(T... tArr) {
        return cycle(Lists.newArrayList((Object[]) tArr));
    }

    public static boolean elementsEqual(Iterator<?> it, Iterator<?> it2) {
        while (it.hasNext()) {
            if (!it2.hasNext()) {
                return false;
            }
            if (!Objects.equal(it.next(), it2.next())) {
                return false;
            }
        }
        return !it2.hasNext();
    }

    public static <T> UnmodifiableIterator<T> emptyIterator() {
        return emptyListIterator();
    }

    static <T> UnmodifiableListIterator<T> emptyListIterator() {
        return EMPTY_LIST_ITERATOR;
    }

    static <T> Iterator<T> emptyModifiableIterator() {
        return EMPTY_MODIFIABLE_ITERATOR;
    }

    public static <T> UnmodifiableIterator<T> filter(final Iterator<T> it, final Predicate<? super T> predicate) {
        Preconditions.checkNotNull(it);
        Preconditions.checkNotNull(predicate);
        return new AbstractIterator<T>() {
            protected T computeNext() {
                while (it.hasNext()) {
                    T next = it.next();
                    if (predicate.apply(next)) {
                        return next;
                    }
                }
                return endOfData();
            }
        };
    }

    @GwtIncompatible("Class.isInstance")
    public static <T> UnmodifiableIterator<T> filter(Iterator<?> it, Class<T> cls) {
        return filter((Iterator) it, Predicates.instanceOf(cls));
    }

    public static <T> T find(Iterator<T> it, Predicate<? super T> predicate) {
        return filter((Iterator) it, (Predicate) predicate).next();
    }

    public static <T> T find(Iterator<? extends T> it, Predicate<? super T> predicate, @Nullable T t) {
        UnmodifiableIterator filter = filter((Iterator) it, (Predicate) predicate);
        return filter.hasNext() ? filter.next() : t;
    }

    public static <T> UnmodifiableIterator<T> forArray(final T... tArr) {
        Preconditions.checkNotNull(tArr);
        return new AbstractIndexedListIterator<T>(tArr.length) {
            protected T get(int i) {
                return tArr[i];
            }
        };
    }

    static <T> UnmodifiableListIterator<T> forArray(final T[] tArr, final int i, int i2, int i3) {
        Preconditions.checkArgument(i2 >= 0);
        Preconditions.checkPositionIndexes(i, i + i2, tArr.length);
        return new AbstractIndexedListIterator<T>(i2, i3) {
            protected T get(int i) {
                return tArr[i + i];
            }
        };
    }

    public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> enumeration) {
        Preconditions.checkNotNull(enumeration);
        return new UnmodifiableIterator<T>() {
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            public T next() {
                return enumeration.nextElement();
            }
        };
    }

    public static int frequency(Iterator<?> it, @Nullable Object obj) {
        int i = 0;
        if (obj == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    i++;
                }
            }
        } else {
            while (it.hasNext()) {
                if (obj.equals(it.next())) {
                    i++;
                }
            }
        }
        return i;
    }

    public static <T> T get(Iterator<T> it, int i) {
        checkNonnegative(i);
        int i2 = 0;
        while (it.hasNext()) {
            T next = it.next();
            if (i2 == i) {
                return next;
            }
            i2++;
        }
        throw new IndexOutOfBoundsException("position (" + i + ") must be less than the number of elements that remained (" + i2 + ")");
    }

    public static <T> T get(Iterator<? extends T> it, int i, @Nullable T t) {
        checkNonnegative(i);
        try {
            t = get(it, i);
        } catch (IndexOutOfBoundsException e) {
        }
        return t;
    }

    public static <T> T getLast(Iterator<T> it) {
        T next;
        do {
            next = it.next();
        } while (it.hasNext());
        return next;
    }

    public static <T> T getLast(Iterator<? extends T> it, @Nullable T t) {
        return it.hasNext() ? getLast(it) : t;
    }

    public static <T> T getNext(Iterator<? extends T> it, @Nullable T t) {
        return it.hasNext() ? it.next() : t;
    }

    public static <T> T getOnlyElement(Iterator<T> it) {
        T next = it.next();
        if (!it.hasNext()) {
            return next;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("expected one element but was: <" + next);
        for (int i = 0; i < 4 && it.hasNext(); i++) {
            stringBuilder.append(", " + it.next());
        }
        if (it.hasNext()) {
            stringBuilder.append(", ...");
        }
        stringBuilder.append('>');
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static <T> T getOnlyElement(Iterator<? extends T> it, @Nullable T t) {
        return it.hasNext() ? getOnlyElement(it) : t;
    }

    public static <T> int indexOf(Iterator<T> it, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate, "predicate");
        int i = 0;
        while (it.hasNext()) {
            if (predicate.apply(it.next())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static <T> Iterator<T> limit(final Iterator<T> it, final int i) {
        Preconditions.checkNotNull(it);
        Preconditions.checkArgument(i >= 0, "limit is negative");
        return new Iterator<T>() {
            private int count;

            public boolean hasNext() {
                return this.count < i && it.hasNext();
            }

            public T next() {
                if (hasNext()) {
                    this.count++;
                    return it.next();
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                it.remove();
            }
        };
    }

    @Beta
    public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> iterable, Comparator<? super T> comparator) {
        Preconditions.checkNotNull(iterable, "iterators");
        Preconditions.checkNotNull(comparator, "comparator");
        return new MergingIterator(iterable, comparator);
    }

    public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> it, int i) {
        return partitionImpl(it, i, true);
    }

    public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> it, int i) {
        return partitionImpl(it, i, false);
    }

    private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> it, final int i, final boolean z) {
        Preconditions.checkNotNull(it);
        Preconditions.checkArgument(i > 0);
        return new UnmodifiableIterator<List<T>>() {
            public boolean hasNext() {
                return it.hasNext();
            }

            public List<T> next() {
                if (hasNext()) {
                    Object[] objArr = new Object[i];
                    int i = 0;
                    while (i < i && it.hasNext()) {
                        objArr[i] = it.next();
                        i++;
                    }
                    for (int i2 = i; i2 < i; i2++) {
                        objArr[i2] = null;
                    }
                    List<T> unmodifiableList = Collections.unmodifiableList(Arrays.asList(objArr));
                    return (z || i == i) ? unmodifiableList : unmodifiableList.subList(0, i);
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    @Deprecated
    public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> peekingIterator) {
        return (PeekingIterator) Preconditions.checkNotNull(peekingIterator);
    }

    public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> it) {
        return it instanceof PeekingImpl ? (PeekingImpl) it : new PeekingImpl(it);
    }

    public static boolean removeAll(Iterator<?> it, Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        boolean z = false;
        while (it.hasNext()) {
            if (collection.contains(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public static <T> boolean removeIf(Iterator<T> it, Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate);
        boolean z = false;
        while (it.hasNext()) {
            if (predicate.apply(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public static boolean retainAll(Iterator<?> it, Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        boolean z = false;
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public static <T> UnmodifiableIterator<T> singletonIterator(@Nullable final T t) {
        return new UnmodifiableIterator<T>() {
            boolean done;

            public boolean hasNext() {
                return !this.done;
            }

            public T next() {
                if (this.done) {
                    throw new NoSuchElementException();
                }
                this.done = true;
                return t;
            }
        };
    }

    public static int size(Iterator<?> it) {
        int i = 0;
        while (it.hasNext()) {
            it.next();
            i++;
        }
        return i;
    }

    @Deprecated
    @Beta
    public static int skip(Iterator<?> it, int i) {
        return advance(it, i);
    }

    @GwtIncompatible("Array.newInstance(Class, int)")
    public static <T> T[] toArray(Iterator<? extends T> it, Class<T> cls) {
        return Iterables.toArray(Lists.newArrayList((Iterator) it), cls);
    }

    public static String toString(Iterator<?> it) {
        return Joiner.on(", ").useForNull("null").appendTo(new StringBuilder().append('['), (Iterator) it).append(']').toString();
    }

    public static <F, T> Iterator<T> transform(Iterator<F> it, final Function<? super F, ? extends T> function) {
        Preconditions.checkNotNull(function);
        return new TransformedIterator<F, T>(it) {
            T transform(F f) {
                return function.apply(f);
            }
        };
    }

    public static <T> Optional<T> tryFind(Iterator<T> it, Predicate<? super T> predicate) {
        UnmodifiableIterator filter = filter((Iterator) it, (Predicate) predicate);
        return filter.hasNext() ? Optional.of(filter.next()) : Optional.absent();
    }

    @Deprecated
    public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> unmodifiableIterator) {
        return (UnmodifiableIterator) Preconditions.checkNotNull(unmodifiableIterator);
    }

    public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<T> it) {
        Preconditions.checkNotNull(it);
        return it instanceof UnmodifiableIterator ? (UnmodifiableIterator) it : new UnmodifiableIterator<T>() {
            public boolean hasNext() {
                return it.hasNext();
            }

            public T next() {
                return it.next();
            }
        };
    }

    static <T> UnmodifiableListIterator<T> unmodifiableListIterator(final ListIterator<T> listIterator) {
        Preconditions.checkNotNull(listIterator);
        return listIterator instanceof UnmodifiableListIterator ? (UnmodifiableListIterator) listIterator : new UnmodifiableListIterator<T>() {
            public boolean hasNext() {
                return listIterator.hasNext();
            }

            public boolean hasPrevious() {
                return listIterator.hasPrevious();
            }

            public T next() {
                return listIterator.next();
            }

            public int nextIndex() {
                return listIterator.nextIndex();
            }

            public T previous() {
                return listIterator.previous();
            }

            public int previousIndex() {
                return listIterator.previousIndex();
            }
        };
    }
}
