package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Lists {

    private static class AbstractListWrapper<E> extends AbstractList<E> {
        final List<E> backingList;

        AbstractListWrapper(List<E> list) {
            this.backingList = (List) Preconditions.checkNotNull(list);
        }

        public void add(int i, E e) {
            this.backingList.add(i, e);
        }

        public boolean addAll(int i, Collection<? extends E> collection) {
            return this.backingList.addAll(i, collection);
        }

        public boolean contains(Object obj) {
            return this.backingList.contains(obj);
        }

        public E get(int i) {
            return this.backingList.get(i);
        }

        public E remove(int i) {
            return this.backingList.remove(i);
        }

        public E set(int i, E e) {
            return this.backingList.set(i, e);
        }

        public int size() {
            return this.backingList.size();
        }
    }

    private static class RandomAccessListWrapper<E> extends AbstractListWrapper<E> implements RandomAccess {
        RandomAccessListWrapper(List<E> list) {
            super(list);
        }
    }

    private static final class CharSequenceAsList extends AbstractList<Character> {
        private final CharSequence sequence;

        CharSequenceAsList(CharSequence charSequence) {
            this.sequence = charSequence;
        }

        public boolean contains(@Nullable Object obj) {
            return indexOf(obj) >= 0;
        }

        public boolean equals(@Nullable Object obj) {
            if (obj instanceof List) {
                List list = (List) obj;
                int length = this.sequence.length();
                if (length == list.size()) {
                    Iterator it = list.iterator();
                    int i = 0;
                    while (i < length) {
                        Object next = it.next();
                        if ((next instanceof Character) && ((Character) next).charValue() == this.sequence.charAt(i)) {
                            i++;
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        public Character get(int i) {
            Preconditions.checkElementIndex(i, size());
            return Character.valueOf(this.sequence.charAt(i));
        }

        public int hashCode() {
            int i = 1;
            for (int i2 = 0; i2 < this.sequence.length(); i2++) {
                i = (i * 31) + this.sequence.charAt(i2);
            }
            return i;
        }

        public int indexOf(@Nullable Object obj) {
            if (obj instanceof Character) {
                char charValue = ((Character) obj).charValue();
                for (int i = 0; i < this.sequence.length(); i++) {
                    if (this.sequence.charAt(i) == charValue) {
                        return i;
                    }
                }
            }
            return -1;
        }

        public int lastIndexOf(@Nullable Object obj) {
            if (obj instanceof Character) {
                char charValue = ((Character) obj).charValue();
                for (int length = this.sequence.length() - 1; length >= 0; length--) {
                    if (this.sequence.charAt(length) == charValue) {
                        return length;
                    }
                }
            }
            return -1;
        }

        public int size() {
            return this.sequence.length();
        }

        public List<Character> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, size());
            return Lists.charactersOf(this.sequence.subSequence(i, i2));
        }
    }

    private static class OnePlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
        private static final long serialVersionUID = 0;
        final E first;
        final E[] rest;

        OnePlusArrayList(@Nullable E e, E[] eArr) {
            this.first = e;
            this.rest = (Object[]) Preconditions.checkNotNull(eArr);
        }

        public E get(int i) {
            Preconditions.checkElementIndex(i, size());
            return i == 0 ? this.first : this.rest[i - 1];
        }

        public int size() {
            return this.rest.length + 1;
        }
    }

    private static class Partition<T> extends AbstractList<List<T>> {
        final List<T> list;
        final int size;

        Partition(List<T> list, int i) {
            this.list = list;
            this.size = i;
        }

        public List<T> get(int i) {
            Preconditions.checkElementIndex(i, size());
            int i2 = this.size * i;
            return this.list.subList(i2, Math.min(this.size + i2, this.list.size()));
        }

        public boolean isEmpty() {
            return this.list.isEmpty();
        }

        public int size() {
            int size = this.list.size() / this.size;
            return this.size * size != this.list.size() ? size + 1 : size;
        }
    }

    private static class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {
        RandomAccessPartition(List<T> list, int i) {
            super(list, i);
        }
    }

    private static class ReverseList<T> extends AbstractList<T> {
        private final List<T> forwardList;

        ReverseList(List<T> list) {
            this.forwardList = (List) Preconditions.checkNotNull(list);
        }

        private int reverseIndex(int i) {
            int size = size();
            Preconditions.checkElementIndex(i, size);
            return (size - 1) - i;
        }

        private int reversePosition(int i) {
            int size = size();
            Preconditions.checkPositionIndex(i, size);
            return size - i;
        }

        public void add(int i, @Nullable T t) {
            this.forwardList.add(reversePosition(i), t);
        }

        public void clear() {
            this.forwardList.clear();
        }

        public boolean contains(@Nullable Object obj) {
            return this.forwardList.contains(obj);
        }

        public boolean containsAll(Collection<?> collection) {
            return this.forwardList.containsAll(collection);
        }

        public T get(int i) {
            return this.forwardList.get(reverseIndex(i));
        }

        List<T> getForwardList() {
            return this.forwardList;
        }

        public int indexOf(@Nullable Object obj) {
            int lastIndexOf = this.forwardList.lastIndexOf(obj);
            return lastIndexOf >= 0 ? reverseIndex(lastIndexOf) : -1;
        }

        public boolean isEmpty() {
            return this.forwardList.isEmpty();
        }

        public Iterator<T> iterator() {
            return listIterator();
        }

        public int lastIndexOf(@Nullable Object obj) {
            int indexOf = this.forwardList.indexOf(obj);
            return indexOf >= 0 ? reverseIndex(indexOf) : -1;
        }

        public ListIterator<T> listIterator(int i) {
            final ListIterator listIterator = this.forwardList.listIterator(reversePosition(i));
            return new ListIterator<T>() {
                boolean canRemove;
                boolean canSet;

                public void add(T t) {
                    listIterator.add(t);
                    listIterator.previous();
                    this.canRemove = false;
                    this.canSet = false;
                }

                public boolean hasNext() {
                    return listIterator.hasPrevious();
                }

                public boolean hasPrevious() {
                    return listIterator.hasNext();
                }

                public T next() {
                    if (hasNext()) {
                        this.canRemove = true;
                        this.canSet = true;
                        return listIterator.previous();
                    }
                    throw new NoSuchElementException();
                }

                public int nextIndex() {
                    return ReverseList.this.reversePosition(listIterator.nextIndex());
                }

                public T previous() {
                    if (hasPrevious()) {
                        this.canRemove = true;
                        this.canSet = true;
                        return listIterator.next();
                    }
                    throw new NoSuchElementException();
                }

                public int previousIndex() {
                    return nextIndex() - 1;
                }

                public void remove() {
                    Preconditions.checkState(this.canRemove);
                    listIterator.remove();
                    this.canSet = false;
                    this.canRemove = false;
                }

                public void set(T t) {
                    Preconditions.checkState(this.canSet);
                    listIterator.set(t);
                }
            };
        }

        public T remove(int i) {
            return this.forwardList.remove(reverseIndex(i));
        }

        protected void removeRange(int i, int i2) {
            subList(i, i2).clear();
        }

        public T set(int i, @Nullable T t) {
            return this.forwardList.set(reverseIndex(i), t);
        }

        public int size() {
            return this.forwardList.size();
        }

        public List<T> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, size());
            return Lists.reverse(this.forwardList.subList(reversePosition(i2), reversePosition(i)));
        }
    }

    private static class RandomAccessReverseList<T> extends ReverseList<T> implements RandomAccess {
        RandomAccessReverseList(List<T> list) {
            super(list);
        }
    }

    private static final class StringAsImmutableList extends ImmutableList<Character> {
        int hash = 0;
        private final String string;

        StringAsImmutableList(String str) {
            this.string = str;
        }

        public boolean equals(@Nullable Object obj) {
            if (obj instanceof List) {
                List list = (List) obj;
                int length = this.string.length();
                if (length == list.size()) {
                    Iterator it = list.iterator();
                    int i = 0;
                    while (i < length) {
                        Object next = it.next();
                        if ((next instanceof Character) && ((Character) next).charValue() == this.string.charAt(i)) {
                            i++;
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        public Character get(int i) {
            Preconditions.checkElementIndex(i, size());
            return Character.valueOf(this.string.charAt(i));
        }

        public int hashCode() {
            int i = this.hash;
            if (i == 0) {
                i = 1;
                for (int i2 = 0; i2 < this.string.length(); i2++) {
                    i = (i * 31) + this.string.charAt(i2);
                }
                this.hash = i;
            }
            return i;
        }

        public int indexOf(@Nullable Object obj) {
            return obj instanceof Character ? this.string.indexOf(((Character) obj).charValue()) : -1;
        }

        boolean isPartialView() {
            return false;
        }

        public int lastIndexOf(@Nullable Object obj) {
            return obj instanceof Character ? this.string.lastIndexOf(((Character) obj).charValue()) : -1;
        }

        public int size() {
            return this.string.length();
        }

        public ImmutableList<Character> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, size());
            return Lists.charactersOf(this.string.substring(i, i2));
        }
    }

    private static class TransformingRandomAccessList<F, T> extends AbstractList<T> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final List<F> fromList;
        final Function<? super F, ? extends T> function;

        TransformingRandomAccessList(List<F> list, Function<? super F, ? extends T> function) {
            this.fromList = (List) Preconditions.checkNotNull(list);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        public void clear() {
            this.fromList.clear();
        }

        public T get(int i) {
            return this.function.apply(this.fromList.get(i));
        }

        public boolean isEmpty() {
            return this.fromList.isEmpty();
        }

        public T remove(int i) {
            return this.function.apply(this.fromList.remove(i));
        }

        public int size() {
            return this.fromList.size();
        }
    }

    private static class TransformingSequentialList<F, T> extends AbstractSequentialList<T> implements Serializable {
        private static final long serialVersionUID = 0;
        final List<F> fromList;
        final Function<? super F, ? extends T> function;

        TransformingSequentialList(List<F> list, Function<? super F, ? extends T> function) {
            this.fromList = (List) Preconditions.checkNotNull(list);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        public void clear() {
            this.fromList.clear();
        }

        public ListIterator<T> listIterator(int i) {
            final ListIterator listIterator = this.fromList.listIterator(i);
            return new ListIterator<T>() {
                public void add(T t) {
                    throw new UnsupportedOperationException();
                }

                public boolean hasNext() {
                    return listIterator.hasNext();
                }

                public boolean hasPrevious() {
                    return listIterator.hasPrevious();
                }

                public T next() {
                    return TransformingSequentialList.this.function.apply(listIterator.next());
                }

                public int nextIndex() {
                    return listIterator.nextIndex();
                }

                public T previous() {
                    return TransformingSequentialList.this.function.apply(listIterator.previous());
                }

                public int previousIndex() {
                    return listIterator.previousIndex();
                }

                public void remove() {
                    listIterator.remove();
                }

                public void set(T t) {
                    throw new UnsupportedOperationException("not supported");
                }
            };
        }

        public int size() {
            return this.fromList.size();
        }
    }

    private static class TwoPlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
        private static final long serialVersionUID = 0;
        final E first;
        final E[] rest;
        final E second;

        TwoPlusArrayList(@Nullable E e, @Nullable E e2, E[] eArr) {
            this.first = e;
            this.second = e2;
            this.rest = (Object[]) Preconditions.checkNotNull(eArr);
        }

        public E get(int i) {
            switch (i) {
                case 0:
                    return this.first;
                case 1:
                    return this.second;
                default:
                    Preconditions.checkElementIndex(i, size());
                    return this.rest[i - 2];
            }
        }

        public int size() {
            return this.rest.length + 2;
        }
    }

    private Lists() {
    }

    static <E> boolean addAllImpl(List<E> list, int i, Iterable<? extends E> iterable) {
        boolean z = false;
        ListIterator listIterator = list.listIterator(i);
        for (Object add : iterable) {
            listIterator.add(add);
            z = true;
        }
        return z;
    }

    public static <E> List<E> asList(@Nullable E e, @Nullable E e2, E[] eArr) {
        return new TwoPlusArrayList(e, e2, eArr);
    }

    public static <E> List<E> asList(@Nullable E e, E[] eArr) {
        return new OnePlusArrayList(e, eArr);
    }

    static <T> List<T> cast(Iterable<T> iterable) {
        return (List) iterable;
    }

    @Beta
    public static ImmutableList<Character> charactersOf(String str) {
        return new StringAsImmutableList((String) Preconditions.checkNotNull(str));
    }

    @Beta
    public static List<Character> charactersOf(CharSequence charSequence) {
        return new CharSequenceAsList((CharSequence) Preconditions.checkNotNull(charSequence));
    }

    @VisibleForTesting
    static int computeArrayListCapacity(int i) {
        Preconditions.checkArgument(i >= 0);
        return Ints.saturatedCast((5 + ((long) i)) + ((long) (i / 10)));
    }

    static boolean equalsImpl(List<?> list, @Nullable Object obj) {
        if (obj != Preconditions.checkNotNull(list)) {
            if (!(obj instanceof List)) {
                return false;
            }
            List list2 = (List) obj;
            if (list.size() != list2.size()) {
                return false;
            }
            if (!Iterators.elementsEqual(list.iterator(), list2.iterator())) {
                return false;
            }
        }
        return true;
    }

    static int hashCodeImpl(List<?> list) {
        int i = 1;
        for (Object next : list) {
            i = (i * 31) + (next == null ? 0 : next.hashCode());
        }
        return i;
    }

    static int indexOfImpl(List<?> list, @Nullable Object obj) {
        ListIterator listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            if (Objects.equal(obj, listIterator.next())) {
                return listIterator.previousIndex();
            }
        }
        return -1;
    }

    static int lastIndexOfImpl(List<?> list, @Nullable Object obj) {
        ListIterator listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            if (Objects.equal(obj, listIterator.previous())) {
                return listIterator.nextIndex();
            }
        }
        return -1;
    }

    static <E> ListIterator<E> listIteratorImpl(List<E> list, int i) {
        return new AbstractListWrapper(list).listIterator(i);
    }

    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList();
    }

    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> iterable) {
        Preconditions.checkNotNull(iterable);
        return iterable instanceof Collection ? new ArrayList(Collections2.cast(iterable)) : newArrayList(iterable.iterator());
    }

    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> it) {
        Preconditions.checkNotNull(it);
        ArrayList<E> newArrayList = newArrayList();
        while (it.hasNext()) {
            newArrayList.add(it.next());
        }
        return newArrayList;
    }

    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayList(E... eArr) {
        Preconditions.checkNotNull(eArr);
        Object arrayList = new ArrayList(computeArrayListCapacity(eArr.length));
        Collections.addAll(arrayList, eArr);
        return arrayList;
    }

    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayListWithCapacity(int i) {
        Preconditions.checkArgument(i >= 0);
        return new ArrayList(i);
    }

    @GwtCompatible(serializable = true)
    public static <E> ArrayList<E> newArrayListWithExpectedSize(int i) {
        return new ArrayList(computeArrayListCapacity(i));
    }

    @GwtIncompatible("CopyOnWriteArrayList")
    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList();
    }

    @GwtIncompatible("CopyOnWriteArrayList")
    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> iterable) {
        return new CopyOnWriteArrayList(iterable instanceof Collection ? Collections2.cast(iterable) : newArrayList((Iterable) iterable));
    }

    @GwtCompatible(serializable = true)
    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList();
    }

    @GwtCompatible(serializable = true)
    public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> iterable) {
        LinkedList<E> newLinkedList = newLinkedList();
        for (Object add : iterable) {
            newLinkedList.add(add);
        }
        return newLinkedList;
    }

    public static <T> List<List<T>> partition(List<T> list, int i) {
        Preconditions.checkNotNull(list);
        Preconditions.checkArgument(i > 0);
        return list instanceof RandomAccess ? new RandomAccessPartition(list, i) : new Partition(list, i);
    }

    public static <T> List<T> reverse(List<T> list) {
        return list instanceof ReverseList ? ((ReverseList) list).getForwardList() : list instanceof RandomAccess ? new RandomAccessReverseList(list) : new ReverseList(list);
    }

    static <E> List<E> subListImpl(List<E> list, int i, int i2) {
        return (list instanceof RandomAccess ? new RandomAccessListWrapper<E>(list) {
            private static final long serialVersionUID = 0;

            public ListIterator<E> listIterator(int i) {
                return this.backingList.listIterator(i);
            }
        } : new AbstractListWrapper<E>(list) {
            private static final long serialVersionUID = 0;

            public ListIterator<E> listIterator(int i) {
                return this.backingList.listIterator(i);
            }
        }).subList(i, i2);
    }

    public static <F, T> List<T> transform(List<F> list, Function<? super F, ? extends T> function) {
        return list instanceof RandomAccess ? new TransformingRandomAccessList(list, function) : new TransformingSequentialList(list, function);
    }
}
