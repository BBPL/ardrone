package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess {

    public static final class Builder<E> extends com.google.common.collect.ImmutableCollection.Builder<E> {
        private Object[] contents;
        private int size;

        public Builder() {
            this(4);
        }

        Builder(int i) {
            this.contents = new Object[i];
            this.size = 0;
        }

        public Builder<E> add(E e) {
            Preconditions.checkNotNull(e);
            expandFor(1);
            Object[] objArr = this.contents;
            int i = this.size;
            this.size = i + 1;
            objArr[i] = e;
            return this;
        }

        public Builder<E> add(E... eArr) {
            for (int i = 0; i < eArr.length; i++) {
                ObjectArrays.checkElementNotNull(eArr[i], i);
            }
            expandFor(eArr.length);
            System.arraycopy(eArr, 0, this.contents, this.size, eArr.length);
            this.size += eArr.length;
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> iterable) {
            if (iterable instanceof Collection) {
                expandFor(((Collection) iterable).size());
            }
            super.addAll((Iterable) iterable);
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> it) {
            super.addAll((Iterator) it);
            return this;
        }

        public ImmutableList<E> build() {
            switch (this.size) {
                case 0:
                    return ImmutableList.of();
                case 1:
                    return ImmutableList.of(this.contents[0]);
                default:
                    return this.size == this.contents.length ? new RegularImmutableList(this.contents) : new RegularImmutableList(ObjectArrays.arraysCopyOf(this.contents, this.size));
            }
        }

        Builder<E> expandFor(int i) {
            int i2 = this.size + i;
            if (this.contents.length < i2) {
                this.contents = ObjectArrays.arraysCopyOf(this.contents, com.google.common.collect.ImmutableCollection.Builder.expandedCapacity(this.contents.length, i2));
            }
            return this;
        }
    }

    private static class ReverseImmutableList<E> extends ImmutableList<E> {
        private final transient ImmutableList<E> forwardList;
        private final transient int size;

        ReverseImmutableList(ImmutableList<E> immutableList) {
            this.forwardList = immutableList;
            this.size = immutableList.size();
        }

        private int reverseIndex(int i) {
            return (this.size - 1) - i;
        }

        private int reversePosition(int i) {
            return this.size - i;
        }

        public boolean contains(@Nullable Object obj) {
            return this.forwardList.contains(obj);
        }

        public boolean containsAll(Collection<?> collection) {
            return this.forwardList.containsAll(collection);
        }

        public E get(int i) {
            Preconditions.checkElementIndex(i, this.size);
            return this.forwardList.get(reverseIndex(i));
        }

        public int indexOf(@Nullable Object obj) {
            int lastIndexOf = this.forwardList.lastIndexOf(obj);
            return lastIndexOf >= 0 ? reverseIndex(lastIndexOf) : -1;
        }

        public boolean isEmpty() {
            return this.forwardList.isEmpty();
        }

        boolean isPartialView() {
            return this.forwardList.isPartialView();
        }

        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        public int lastIndexOf(@Nullable Object obj) {
            int indexOf = this.forwardList.indexOf(obj);
            return indexOf >= 0 ? reverseIndex(indexOf) : -1;
        }

        public UnmodifiableListIterator<E> listIterator(int i) {
            Preconditions.checkPositionIndex(i, this.size);
            final UnmodifiableListIterator listIterator = this.forwardList.listIterator(reversePosition(i));
            return new UnmodifiableListIterator<E>() {
                public boolean hasNext() {
                    return listIterator.hasPrevious();
                }

                public boolean hasPrevious() {
                    return listIterator.hasNext();
                }

                public E next() {
                    return listIterator.previous();
                }

                public int nextIndex() {
                    return ReverseImmutableList.this.reverseIndex(listIterator.previousIndex());
                }

                public E previous() {
                    return listIterator.next();
                }

                public int previousIndex() {
                    return ReverseImmutableList.this.reverseIndex(listIterator.nextIndex());
                }
            };
        }

        public /* bridge */ /* synthetic */ ListIterator listIterator() {
            return super.listIterator();
        }

        public ImmutableList<E> reverse() {
            return this.forwardList;
        }

        public int size() {
            return this.size;
        }

        public ImmutableList<E> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, this.size);
            return this.forwardList.subList(reversePosition(i2), reversePosition(i)).reverse();
        }
    }

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Object[] elements;

        SerializedForm(Object[] objArr) {
            this.elements = objArr;
        }

        Object readResolve() {
            return ImmutableList.copyOf(this.elements);
        }
    }

    class SubList extends ImmutableList<E> {
        final transient int length;
        final transient int offset;

        SubList(int i, int i2) {
            this.offset = i;
            this.length = i2;
        }

        public E get(int i) {
            Preconditions.checkElementIndex(i, this.length);
            return ImmutableList.this.get(this.offset + i);
        }

        boolean isPartialView() {
            return true;
        }

        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        public /* bridge */ /* synthetic */ ListIterator listIterator() {
            return super.listIterator();
        }

        public /* bridge */ /* synthetic */ ListIterator listIterator(int i) {
            return super.listIterator(i);
        }

        public int size() {
            return this.length;
        }

        public ImmutableList<E> subList(int i, int i2) {
            Preconditions.checkPositionIndexes(i, i2, this.length);
            return ImmutableList.this.subList(this.offset + i, this.offset + i2);
        }
    }

    ImmutableList() {
    }

    static <E> ImmutableList<E> asImmutableList(Object[] objArr) {
        switch (objArr.length) {
            case 0:
                return of();
            case 1:
                return new SingletonImmutableList(objArr[0]);
            default:
                return construct(objArr);
        }
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }

    private static <E> ImmutableList<E> construct(Object... objArr) {
        for (int i = 0; i < objArr.length; i++) {
            ObjectArrays.checkElementNotNull(objArr[i], i);
        }
        return new RegularImmutableList(objArr);
    }

    private static <E> ImmutableList<E> copyFromCollection(Collection<? extends E> collection) {
        return asImmutableList(collection.toArray());
    }

    public static <E> ImmutableList<E> copyOf(Iterable<? extends E> iterable) {
        Preconditions.checkNotNull(iterable);
        return iterable instanceof Collection ? copyOf(Collections2.cast(iterable)) : copyOf(iterable.iterator());
    }

    public static <E> ImmutableList<E> copyOf(Collection<? extends E> collection) {
        if (!(collection instanceof ImmutableCollection)) {
            return copyFromCollection(collection);
        }
        Object asList = ((ImmutableCollection) collection).asList();
        return asList.isPartialView() ? copyFromCollection(asList) : asList;
    }

    public static <E> ImmutableList<E> copyOf(Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return of();
        }
        Object next = it.next();
        return !it.hasNext() ? of(next) : new Builder().add(next).addAll((Iterator) it).build();
    }

    public static <E> ImmutableList<E> copyOf(E[] eArr) {
        switch (eArr.length) {
            case 0:
                return of();
            case 1:
                return new SingletonImmutableList(eArr[0]);
            default:
                return construct((Object[]) eArr.clone());
        }
    }

    public static <E> ImmutableList<E> of() {
        return EmptyImmutableList.INSTANCE;
    }

    public static <E> ImmutableList<E> of(E e) {
        return new SingletonImmutableList(e);
    }

    public static <E> ImmutableList<E> of(E e, E e2) {
        return construct(e, e2);
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3) {
        return construct(e, e2, e3);
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4) {
        return construct(e, e2, e3, e4);
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5) {
        return construct(e, e2, e3, e4, e5);
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6) {
        return construct(e, e2, e3, e4, e5, e6);
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6, E e7) {
        return construct(e, e2, e3, e4, e5, e6, e7);
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        return construct(e, e2, e3, e4, e5, e6, e7, e8);
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
        return construct(e, e2, e3, e4, e5, e6, e7, e8, e9);
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
        return construct(e, e2, e3, e4, e5, e6, e7, e8, e9, e10);
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
        return construct(e, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11);
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12, E... eArr) {
        Object obj = new Object[(eArr.length + 12)];
        obj[0] = e;
        obj[1] = e2;
        obj[2] = e3;
        obj[3] = e4;
        obj[4] = e5;
        obj[5] = e6;
        obj[6] = e7;
        obj[7] = e8;
        obj[8] = e9;
        obj[9] = e10;
        obj[10] = e11;
        obj[11] = e12;
        System.arraycopy(eArr, 0, obj, 12, eArr.length);
        return construct(obj);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    public final void add(int i, E e) {
        throw new UnsupportedOperationException();
    }

    public final boolean addAll(int i, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    public ImmutableList<E> asList() {
        return this;
    }

    public boolean contains(@Nullable Object obj) {
        return indexOf(obj) >= 0;
    }

    public boolean equals(Object obj) {
        return Lists.equalsImpl(this, obj);
    }

    public int hashCode() {
        return Lists.hashCodeImpl(this);
    }

    public int indexOf(@Nullable Object obj) {
        return obj == null ? -1 : Lists.indexOfImpl(this, obj);
    }

    public UnmodifiableIterator<E> iterator() {
        return listIterator();
    }

    public int lastIndexOf(@Nullable Object obj) {
        return obj == null ? -1 : Lists.lastIndexOfImpl(this, obj);
    }

    public UnmodifiableListIterator<E> listIterator() {
        return listIterator(0);
    }

    public UnmodifiableListIterator<E> listIterator(int i) {
        return new AbstractIndexedListIterator<E>(size(), i) {
            protected E get(int i) {
                return ImmutableList.this.get(i);
            }
        };
    }

    public final E remove(int i) {
        throw new UnsupportedOperationException();
    }

    public ImmutableList<E> reverse() {
        return new ReverseImmutableList(this);
    }

    public final E set(int i, E e) {
        throw new UnsupportedOperationException();
    }

    public ImmutableList<E> subList(int i, int i2) {
        Preconditions.checkPositionIndexes(i, i2, size());
        switch (i2 - i) {
            case 0:
                return of();
            case 1:
                return of(get(i));
            default:
                return subListUnchecked(i, i2);
        }
    }

    ImmutableList<E> subListUnchecked(int i, int i2) {
        return new SubList(i, i2 - i);
    }

    Object writeReplace() {
        return new SerializedForm(toArray());
    }
}
