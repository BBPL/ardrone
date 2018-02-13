package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
public final class Multisets {
    private static final Ordering<Entry<?>> DECREASING_COUNT_ORDERING = new C07132();

    static abstract class AbstractEntry<E> implements Entry<E> {
        AbstractEntry() {
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            return getCount() == entry.getCount() && Objects.equal(getElement(), entry.getElement());
        }

        public int hashCode() {
            Object element = getElement();
            return (element == null ? 0 : element.hashCode()) ^ getCount();
        }

        public String toString() {
            String valueOf = String.valueOf(getElement());
            int count = getCount();
            return count == 1 ? valueOf : valueOf + " x " + count;
        }
    }

    static abstract class ElementSet<E> extends ImprovedAbstractSet<E> {
        ElementSet() {
        }

        public void clear() {
            multiset().clear();
        }

        public boolean contains(Object obj) {
            return multiset().contains(obj);
        }

        public boolean containsAll(Collection<?> collection) {
            return multiset().containsAll(collection);
        }

        public boolean isEmpty() {
            return multiset().isEmpty();
        }

        public Iterator<E> iterator() {
            return new TransformedIterator<Entry<E>, E>(multiset().entrySet().iterator()) {
                E transform(Entry<E> entry) {
                    return entry.getElement();
                }
            };
        }

        abstract Multiset<E> multiset();

        public boolean remove(Object obj) {
            int count = multiset().count(obj);
            if (count <= 0) {
                return false;
            }
            multiset().remove(obj, count);
            return true;
        }

        public int size() {
            return multiset().entrySet().size();
        }
    }

    static abstract class EntrySet<E> extends ImprovedAbstractSet<Entry<E>> {
        EntrySet() {
        }

        public void clear() {
            multiset().clear();
        }

        public boolean contains(@Nullable Object obj) {
            if (obj instanceof Entry) {
                Entry entry = (Entry) obj;
                if (entry.getCount() > 0 && multiset().count(entry.getElement()) == entry.getCount()) {
                    return true;
                }
            }
            return false;
        }

        abstract Multiset<E> multiset();

        public boolean remove(Object obj) {
            return contains(obj) && multiset().elementSet().remove(((Entry) obj).getElement());
        }
    }

    static final class C07132 extends Ordering<Entry<?>> {
        C07132() {
        }

        public int compare(Entry<?> entry, Entry<?> entry2) {
            return Ints.compare(entry2.getCount(), entry.getCount());
        }
    }

    static final class ImmutableEntry<E> extends AbstractEntry<E> implements Serializable {
        private static final long serialVersionUID = 0;
        final int count;
        @Nullable
        final E element;

        ImmutableEntry(@Nullable E e, int i) {
            this.element = e;
            this.count = i;
            Preconditions.checkArgument(i >= 0);
        }

        public int getCount() {
            return this.count;
        }

        @Nullable
        public E getElement() {
            return this.element;
        }
    }

    static final class MultisetIteratorImpl<E> implements Iterator<E> {
        private boolean canRemove;
        private Entry<E> currentEntry;
        private final Iterator<Entry<E>> entryIterator;
        private int laterCount;
        private final Multiset<E> multiset;
        private int totalCount;

        MultisetIteratorImpl(Multiset<E> multiset, Iterator<Entry<E>> it) {
            this.multiset = multiset;
            this.entryIterator = it;
        }

        public boolean hasNext() {
            return this.laterCount > 0 || this.entryIterator.hasNext();
        }

        public E next() {
            if (hasNext()) {
                if (this.laterCount == 0) {
                    this.currentEntry = (Entry) this.entryIterator.next();
                    int count = this.currentEntry.getCount();
                    this.laterCount = count;
                    this.totalCount = count;
                }
                this.laterCount--;
                this.canRemove = true;
                return this.currentEntry.getElement();
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            Iterators.checkRemove(this.canRemove);
            if (this.totalCount == 1) {
                this.entryIterator.remove();
            } else {
                this.multiset.remove(this.currentEntry.getElement());
            }
            this.totalCount--;
            this.canRemove = false;
        }
    }

    private static class SetMultiset<E> extends ForwardingCollection<E> implements Multiset<E>, Serializable {
        private static final long serialVersionUID = 0;
        final Set<E> delegate;
        transient Set<E> elementSet;
        transient Set<Entry<E>> entrySet;

        class C07161 extends EntrySet<E> {
            C07161() {
            }

            public Iterator<Entry<E>> iterator() {
                return new TransformedIterator<E, Entry<E>>(SetMultiset.this.delegate.iterator()) {
                    Entry<E> transform(E e) {
                        return Multisets.immutableEntry(e, 1);
                    }
                };
            }

            Multiset<E> multiset() {
                return SetMultiset.this;
            }

            public int size() {
                return SetMultiset.this.delegate.size();
            }
        }

        class ElementSet extends ForwardingSet<E> {
            ElementSet() {
            }

            public boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends E> collection) {
                throw new UnsupportedOperationException();
            }

            protected Set<E> delegate() {
                return SetMultiset.this.delegate;
            }
        }

        SetMultiset(Set<E> set) {
            this.delegate = (Set) Preconditions.checkNotNull(set);
        }

        public int add(E e, int i) {
            throw new UnsupportedOperationException();
        }

        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        public int count(Object obj) {
            return this.delegate.contains(obj) ? 1 : 0;
        }

        protected Set<E> delegate() {
            return this.delegate;
        }

        public Set<E> elementSet() {
            Set<E> set = this.elementSet;
            if (set != null) {
                return set;
            }
            set = new ElementSet();
            this.elementSet = set;
            return set;
        }

        public Set<Entry<E>> entrySet() {
            Set<Entry<E>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = new C07161();
            this.entrySet = set;
            return set;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof Multiset)) {
                return false;
            }
            Multiset multiset = (Multiset) obj;
            return size() == multiset.size() && this.delegate.equals(multiset.elementSet());
        }

        public int hashCode() {
            Iterator it = iterator();
            int i = 0;
            while (it.hasNext()) {
                Object next = it.next();
                i += (next == null ? 0 : next.hashCode()) ^ 1;
            }
            return i;
        }

        public int remove(Object obj, int i) {
            if (i == 0) {
                return count(obj);
            }
            Preconditions.checkArgument(i > 0);
            return !this.delegate.remove(obj) ? 0 : 1;
        }

        public int setCount(E e, int i) {
            Multisets.checkNonnegative(i, "count");
            if (i == count(e)) {
                return i;
            }
            if (i == 0) {
                remove(e);
                return 1;
            }
            throw new UnsupportedOperationException();
        }

        public boolean setCount(E e, int i, int i2) {
            return Multisets.setCountImpl(this, e, i, i2);
        }
    }

    static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable {
        private static final long serialVersionUID = 0;
        final Multiset<? extends E> delegate;
        transient Set<E> elementSet;
        transient Set<Entry<E>> entrySet;

        UnmodifiableMultiset(Multiset<? extends E> multiset) {
            this.delegate = multiset;
        }

        public int add(E e, int i) {
            throw new UnsupportedOperationException();
        }

        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        Set<E> createElementSet() {
            return Collections.unmodifiableSet(this.delegate.elementSet());
        }

        protected Multiset<E> delegate() {
            return this.delegate;
        }

        public Set<E> elementSet() {
            Set<E> set = this.elementSet;
            if (set != null) {
                return set;
            }
            set = createElementSet();
            this.elementSet = set;
            return set;
        }

        public Set<Entry<E>> entrySet() {
            Set<Entry<E>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = Collections.unmodifiableSet(this.delegate.entrySet());
            this.entrySet = set;
            return set;
        }

        public Iterator<E> iterator() {
            return Iterators.unmodifiableIterator(this.delegate.iterator());
        }

        public int remove(Object obj, int i) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public int setCount(E e, int i) {
            throw new UnsupportedOperationException();
        }

        public boolean setCount(E e, int i, int i2) {
            throw new UnsupportedOperationException();
        }
    }

    private static final class UnmodifiableSortedMultiset<E> extends UnmodifiableMultiset<E> implements SortedMultiset<E> {
        private static final long serialVersionUID = 0;
        private transient UnmodifiableSortedMultiset<E> descendingMultiset;

        private UnmodifiableSortedMultiset(SortedMultiset<E> sortedMultiset) {
            super(sortedMultiset);
        }

        public Comparator<? super E> comparator() {
            return delegate().comparator();
        }

        SortedSet<E> createElementSet() {
            return Collections.unmodifiableSortedSet(delegate().elementSet());
        }

        protected SortedMultiset<E> delegate() {
            return (SortedMultiset) super.delegate();
        }

        public SortedMultiset<E> descendingMultiset() {
            SortedMultiset sortedMultiset = this.descendingMultiset;
            if (sortedMultiset != null) {
                return sortedMultiset;
            }
            sortedMultiset = new UnmodifiableSortedMultiset(delegate().descendingMultiset());
            sortedMultiset.descendingMultiset = this;
            this.descendingMultiset = sortedMultiset;
            return sortedMultiset;
        }

        public SortedSet<E> elementSet() {
            return (SortedSet) super.elementSet();
        }

        public Entry<E> firstEntry() {
            return delegate().firstEntry();
        }

        public SortedMultiset<E> headMultiset(E e, BoundType boundType) {
            return Multisets.unmodifiableSortedMultiset(delegate().headMultiset(e, boundType));
        }

        public Entry<E> lastEntry() {
            return delegate().lastEntry();
        }

        public Entry<E> pollFirstEntry() {
            throw new UnsupportedOperationException();
        }

        public Entry<E> pollLastEntry() {
            throw new UnsupportedOperationException();
        }

        public SortedMultiset<E> subMultiset(E e, BoundType boundType, E e2, BoundType boundType2) {
            return Multisets.unmodifiableSortedMultiset(delegate().subMultiset(e, boundType, e2, boundType2));
        }

        public SortedMultiset<E> tailMultiset(E e, BoundType boundType) {
            return Multisets.unmodifiableSortedMultiset(delegate().tailMultiset(e, boundType));
        }
    }

    private Multisets() {
    }

    static <E> boolean addAllImpl(Multiset<E> multiset, Collection<? extends E> collection) {
        if (collection.isEmpty()) {
            return false;
        }
        if (collection instanceof Multiset) {
            for (Entry entry : cast(collection).entrySet()) {
                multiset.add(entry.getElement(), entry.getCount());
            }
        } else {
            Iterators.addAll(multiset, collection.iterator());
        }
        return true;
    }

    static <T> Multiset<T> cast(Iterable<T> iterable) {
        return (Multiset) iterable;
    }

    static void checkNonnegative(int i, String str) {
        Preconditions.checkArgument(i >= 0, "%s cannot be negative: %s", str, Integer.valueOf(i));
    }

    public static boolean containsOccurrences(Multiset<?> multiset, Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(multiset2);
        for (Entry entry : multiset2.entrySet()) {
            if (multiset.count(entry.getElement()) < entry.getCount()) {
                return false;
            }
        }
        return true;
    }

    @Beta
    public static <E> ImmutableMultiset<E> copyHighestCountFirst(Multiset<E> multiset) {
        return ImmutableMultiset.copyFromEntries(DECREASING_COUNT_ORDERING.sortedCopy(multiset.entrySet()));
    }

    static boolean equalsImpl(Multiset<?> multiset, @Nullable Object obj) {
        if (obj != multiset) {
            if (!(obj instanceof Multiset)) {
                return false;
            }
            Multiset multiset2 = (Multiset) obj;
            if (multiset.size() != multiset2.size() || multiset.entrySet().size() != multiset2.entrySet().size()) {
                return false;
            }
            for (Entry entry : multiset2.entrySet()) {
                if (multiset.count(entry.getElement()) != entry.getCount()) {
                    return false;
                }
            }
        }
        return true;
    }

    static <E> Multiset<E> forSet(Set<E> set) {
        return new SetMultiset(set);
    }

    public static <E> Entry<E> immutableEntry(@Nullable E e, int i) {
        return new ImmutableEntry(e, i);
    }

    static int inferDistinctElements(Iterable<?> iterable) {
        return iterable instanceof Multiset ? ((Multiset) iterable).elementSet().size() : 11;
    }

    public static <E> Multiset<E> intersection(final Multiset<E> multiset, final Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(multiset2);
        return new AbstractMultiset<E>() {
            public int count(Object obj) {
                int count = multiset.count(obj);
                return count == 0 ? 0 : Math.min(count, multiset2.count(obj));
            }

            Set<E> createElementSet() {
                return Sets.intersection(multiset.elementSet(), multiset2.elementSet());
            }

            int distinctElements() {
                return elementSet().size();
            }

            Iterator<Entry<E>> entryIterator() {
                final Iterator it = multiset.entrySet().iterator();
                return new AbstractIterator<Entry<E>>() {
                    protected Entry<E> computeNext() {
                        while (it.hasNext()) {
                            Entry entry = (Entry) it.next();
                            Object element = entry.getElement();
                            int min = Math.min(entry.getCount(), multiset2.count(element));
                            if (min > 0) {
                                return Multisets.immutableEntry(element, min);
                            }
                        }
                        return (Entry) endOfData();
                    }
                };
            }
        };
    }

    static <E> Iterator<E> iteratorImpl(Multiset<E> multiset) {
        return new MultisetIteratorImpl(multiset, multiset.entrySet().iterator());
    }

    static boolean removeAllImpl(Multiset<?> multiset, Collection<?> collection) {
        Collection elementSet;
        if (collection instanceof Multiset) {
            elementSet = ((Multiset) collection).elementSet();
        }
        return multiset.elementSet().removeAll(elementSet);
    }

    public static boolean removeOccurrences(Multiset<?> multiset, Multiset<?> multiset2) {
        return removeOccurrencesImpl(multiset, multiset2);
    }

    private static <E> boolean removeOccurrencesImpl(Multiset<E> multiset, Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(multiset2);
        Iterator it = multiset.entrySet().iterator();
        boolean z = false;
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            int count = multiset2.count(entry.getElement());
            if (count >= entry.getCount()) {
                it.remove();
                z = true;
            } else if (count > 0) {
                multiset.remove(entry.getElement(), count);
                z = true;
            }
        }
        return z;
    }

    static boolean retainAllImpl(Multiset<?> multiset, Collection<?> collection) {
        Collection elementSet;
        Preconditions.checkNotNull(collection);
        if (collection instanceof Multiset) {
            elementSet = ((Multiset) collection).elementSet();
        }
        return multiset.elementSet().retainAll(elementSet);
    }

    public static boolean retainOccurrences(Multiset<?> multiset, Multiset<?> multiset2) {
        return retainOccurrencesImpl(multiset, multiset2);
    }

    private static <E> boolean retainOccurrencesImpl(Multiset<E> multiset, Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(multiset2);
        Iterator it = multiset.entrySet().iterator();
        boolean z = false;
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            int count = multiset2.count(entry.getElement());
            if (count == 0) {
                it.remove();
                z = true;
            } else if (count < entry.getCount()) {
                multiset.setCount(entry.getElement(), count);
                z = true;
            }
        }
        return z;
    }

    static <E> int setCountImpl(Multiset<E> multiset, E e, int i) {
        checkNonnegative(i, "count");
        int count = multiset.count(e);
        int i2 = i - count;
        if (i2 > 0) {
            multiset.add(e, i2);
        } else if (i2 < 0) {
            multiset.remove(e, -i2);
        }
        return count;
    }

    static <E> boolean setCountImpl(Multiset<E> multiset, E e, int i, int i2) {
        checkNonnegative(i, "oldCount");
        checkNonnegative(i2, "newCount");
        if (multiset.count(e) != i) {
            return false;
        }
        multiset.setCount(e, i2);
        return true;
    }

    static int sizeImpl(Multiset<?> multiset) {
        long j = 0;
        for (Entry count : multiset.entrySet()) {
            j = ((long) count.getCount()) + j;
        }
        return Ints.saturatedCast(j);
    }

    @Deprecated
    public static <E> Multiset<E> unmodifiableMultiset(ImmutableMultiset<E> immutableMultiset) {
        return (Multiset) Preconditions.checkNotNull(immutableMultiset);
    }

    public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
        return ((multiset instanceof UnmodifiableMultiset) || (multiset instanceof ImmutableMultiset)) ? multiset : new UnmodifiableMultiset((Multiset) Preconditions.checkNotNull(multiset));
    }

    @Beta
    public static <E> SortedMultiset<E> unmodifiableSortedMultiset(SortedMultiset<E> sortedMultiset) {
        return new UnmodifiableSortedMultiset((SortedMultiset) Preconditions.checkNotNull(sortedMultiset));
    }
}
