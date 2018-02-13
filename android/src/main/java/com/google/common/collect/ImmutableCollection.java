package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class ImmutableCollection<E> implements Collection<E>, Serializable {
    static final ImmutableCollection<Object> EMPTY_IMMUTABLE_COLLECTION = new EmptyImmutableCollection();
    private transient ImmutableList<E> asList;

    private static class ArrayImmutableCollection<E> extends ImmutableCollection<E> {
        private final E[] elements;

        ArrayImmutableCollection(E[] eArr) {
            this.elements = eArr;
        }

        ImmutableList<E> createAsList() {
            return this.elements.length == 1 ? new SingletonImmutableList(this.elements[0]) : new RegularImmutableList(this.elements);
        }

        public boolean isEmpty() {
            return false;
        }

        boolean isPartialView() {
            return false;
        }

        public UnmodifiableIterator<E> iterator() {
            return Iterators.forArray(this.elements);
        }

        public int size() {
            return this.elements.length;
        }
    }

    public static abstract class Builder<E> {
        static final int DEFAULT_INITIAL_CAPACITY = 4;

        Builder() {
        }

        @VisibleForTesting
        static int expandedCapacity(int i, int i2) {
            if (i2 < 0) {
                throw new AssertionError("cannot store more than MAX_VALUE elements");
            }
            int i3 = ((i >> 1) + i) + 1;
            if (i3 < i2) {
                i3 = Integer.highestOneBit(i2 - 1) << 1;
            }
            return i3 < 0 ? Integer.MAX_VALUE : i3;
        }

        public abstract Builder<E> add(E e);

        public Builder<E> add(E... eArr) {
            for (Object add : eArr) {
                add(add);
            }
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> iterable) {
            for (Object add : iterable) {
                add(add);
            }
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> it) {
            while (it.hasNext()) {
                add(it.next());
            }
            return this;
        }

        public abstract ImmutableCollection<E> build();
    }

    private static class EmptyImmutableCollection extends ImmutableCollection<Object> {
        private static final Object[] EMPTY_ARRAY = new Object[0];

        private EmptyImmutableCollection() {
        }

        public boolean contains(@Nullable Object obj) {
            return false;
        }

        ImmutableList<Object> createAsList() {
            return ImmutableList.of();
        }

        public boolean isEmpty() {
            return true;
        }

        boolean isPartialView() {
            return false;
        }

        public UnmodifiableIterator<Object> iterator() {
            return Iterators.EMPTY_LIST_ITERATOR;
        }

        public int size() {
            return 0;
        }

        public Object[] toArray() {
            return EMPTY_ARRAY;
        }

        public <T> T[] toArray(T[] tArr) {
            if (tArr.length > 0) {
                tArr[0] = null;
            }
            return tArr;
        }
    }

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Object[] elements;

        SerializedForm(Object[] objArr) {
            this.elements = objArr;
        }

        Object readResolve() {
            return this.elements.length == 0 ? ImmutableCollection.EMPTY_IMMUTABLE_COLLECTION : new ArrayImmutableCollection(Platform.clone(this.elements));
        }
    }

    ImmutableCollection() {
    }

    public final boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    public final boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    public ImmutableList<E> asList() {
        ImmutableList<E> immutableList = this.asList;
        if (immutableList != null) {
            return immutableList;
        }
        immutableList = createAsList();
        this.asList = immutableList;
        return immutableList;
    }

    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(@Nullable Object obj) {
        return obj != null && Iterators.contains(iterator(), obj);
    }

    public boolean containsAll(Collection<?> collection) {
        return Collections2.containsAllImpl(this, collection);
    }

    ImmutableList<E> createAsList() {
        switch (size()) {
            case 0:
                return ImmutableList.of();
            case 1:
                return ImmutableList.of(iterator().next());
            default:
                return new RegularImmutableAsList(this, toArray());
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    abstract boolean isPartialView();

    public abstract UnmodifiableIterator<E> iterator();

    public final boolean remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    public final boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public final boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        return ObjectArrays.toArrayImpl(this);
    }

    public <T> T[] toArray(T[] tArr) {
        return ObjectArrays.toArrayImpl(this, tArr);
    }

    public String toString() {
        return Collections2.toStringImpl(this);
    }

    Object writeReplace() {
        return new SerializedForm(toArray());
    }
}
