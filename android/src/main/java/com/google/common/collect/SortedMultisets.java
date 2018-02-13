package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Multiset.Entry;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;

@GwtCompatible
final class SortedMultisets {

    static abstract class ElementSet<E> extends ElementSet<E> implements SortedSet<E> {
        ElementSet() {
        }

        public Comparator<? super E> comparator() {
            return multiset().comparator();
        }

        public E first() {
            return SortedMultisets.getElementOrThrow(multiset().firstEntry());
        }

        public SortedSet<E> headSet(E e) {
            return multiset().headMultiset(e, BoundType.OPEN).elementSet();
        }

        public E last() {
            return SortedMultisets.getElementOrThrow(multiset().lastEntry());
        }

        abstract SortedMultiset<E> multiset();

        public SortedSet<E> subSet(E e, E e2) {
            return multiset().subMultiset(e, BoundType.CLOSED, e2, BoundType.OPEN).elementSet();
        }

        public SortedSet<E> tailSet(E e) {
            return multiset().tailMultiset(e, BoundType.CLOSED).elementSet();
        }
    }

    static abstract class DescendingMultiset<E> extends ForwardingMultiset<E> implements SortedMultiset<E> {
        private transient Comparator<? super E> comparator;
        private transient SortedSet<E> elementSet;
        private transient Set<Entry<E>> entrySet;

        class C07541 extends ElementSet<E> {
            C07541() {
            }

            SortedMultiset<E> multiset() {
                return DescendingMultiset.this;
            }
        }

        class C07552 extends EntrySet<E> {
            C07552() {
            }

            public Iterator<Entry<E>> iterator() {
                return DescendingMultiset.this.entryIterator();
            }

            Multiset<E> multiset() {
                return DescendingMultiset.this;
            }

            public int size() {
                return DescendingMultiset.this.forwardMultiset().entrySet().size();
            }
        }

        DescendingMultiset() {
        }

        public Comparator<? super E> comparator() {
            Comparator<? super E> comparator = this.comparator;
            if (comparator != null) {
                return comparator;
            }
            comparator = Ordering.from(forwardMultiset().comparator()).reverse();
            this.comparator = comparator;
            return comparator;
        }

        Set<Entry<E>> createEntrySet() {
            return new C07552();
        }

        protected Multiset<E> delegate() {
            return forwardMultiset();
        }

        public SortedMultiset<E> descendingMultiset() {
            return forwardMultiset();
        }

        public SortedSet<E> elementSet() {
            SortedSet<E> sortedSet = this.elementSet;
            if (sortedSet != null) {
                return sortedSet;
            }
            sortedSet = new C07541();
            this.elementSet = sortedSet;
            return sortedSet;
        }

        abstract Iterator<Entry<E>> entryIterator();

        public Set<Entry<E>> entrySet() {
            Set<Entry<E>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = createEntrySet();
            this.entrySet = set;
            return set;
        }

        public Entry<E> firstEntry() {
            return forwardMultiset().lastEntry();
        }

        abstract SortedMultiset<E> forwardMultiset();

        public SortedMultiset<E> headMultiset(E e, BoundType boundType) {
            return forwardMultiset().tailMultiset(e, boundType).descendingMultiset();
        }

        public Iterator<E> iterator() {
            return Multisets.iteratorImpl(this);
        }

        public Entry<E> lastEntry() {
            return forwardMultiset().firstEntry();
        }

        public Entry<E> pollFirstEntry() {
            return forwardMultiset().pollLastEntry();
        }

        public Entry<E> pollLastEntry() {
            return forwardMultiset().pollFirstEntry();
        }

        public SortedMultiset<E> subMultiset(E e, BoundType boundType, E e2, BoundType boundType2) {
            return forwardMultiset().subMultiset(e2, boundType2, e, boundType).descendingMultiset();
        }

        public SortedMultiset<E> tailMultiset(E e, BoundType boundType) {
            return forwardMultiset().headMultiset(e, boundType).descendingMultiset();
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return standardToArray(tArr);
        }

        public String toString() {
            return entrySet().toString();
        }
    }

    private SortedMultisets() {
    }

    private static <E> E getElementOrThrow(Entry<E> entry) {
        if (entry != null) {
            return entry.getElement();
        }
        throw new NoSuchElementException();
    }
}
