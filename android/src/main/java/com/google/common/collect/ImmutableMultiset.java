package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
public abstract class ImmutableMultiset<E> extends ImmutableCollection<E> implements Multiset<E> {
    private transient ImmutableSet<Entry<E>> entrySet;

    abstract class EntrySet extends ImmutableSet<Entry<E>> {
        private static final long serialVersionUID = 0;

        EntrySet() {
        }

        public boolean contains(Object obj) {
            if (obj instanceof Entry) {
                Entry entry = (Entry) obj;
                if (entry.getCount() > 0 && ImmutableMultiset.this.count(entry.getElement()) == entry.getCount()) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return ImmutableMultiset.this.hashCode();
        }

        boolean isPartialView() {
            return ImmutableMultiset.this.isPartialView();
        }

        public Object[] toArray() {
            return toArray(new Object[size()]);
        }

        public <T> T[] toArray(T[] tArr) {
            int size = size();
            if (tArr.length < size) {
                tArr = ObjectArrays.newArray((Object[]) tArr, size);
            } else if (tArr.length > size) {
                tArr[size] = null;
            }
            Iterator it = iterator();
            int i = 0;
            while (it.hasNext()) {
                tArr[i] = (Entry) it.next();
                i++;
            }
            return tArr;
        }

        Object writeReplace() {
            return new EntrySetSerializedForm(ImmutableMultiset.this);
        }
    }

    public static class Builder<E> extends com.google.common.collect.ImmutableCollection.Builder<E> {
        final Multiset<E> contents;

        public Builder() {
            this(LinkedHashMultiset.create());
        }

        Builder(Multiset<E> multiset) {
            this.contents = multiset;
        }

        public Builder<E> add(E e) {
            this.contents.add(Preconditions.checkNotNull(e));
            return this;
        }

        public Builder<E> add(E... eArr) {
            super.add((Object[]) eArr);
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> iterable) {
            if (iterable instanceof Multiset) {
                for (Entry entry : Multisets.cast(iterable).entrySet()) {
                    addCopies(entry.getElement(), entry.getCount());
                }
            } else {
                super.addAll((Iterable) iterable);
            }
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> it) {
            super.addAll((Iterator) it);
            return this;
        }

        public Builder<E> addCopies(E e, int i) {
            this.contents.add(Preconditions.checkNotNull(e), i);
            return this;
        }

        public ImmutableMultiset<E> build() {
            return ImmutableMultiset.copyOf(this.contents);
        }

        public Builder<E> setCount(E e, int i) {
            this.contents.setCount(Preconditions.checkNotNull(e), i);
            return this;
        }
    }

    static class EntrySetSerializedForm<E> implements Serializable {
        final ImmutableMultiset<E> multiset;

        EntrySetSerializedForm(ImmutableMultiset<E> immutableMultiset) {
            this.multiset = immutableMultiset;
        }

        Object readResolve() {
            return this.multiset.entrySet();
        }
    }

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final int[] counts;
        final Object[] elements;

        SerializedForm(Multiset<?> multiset) {
            int size = multiset.entrySet().size();
            this.elements = new Object[size];
            this.counts = new int[size];
            int i = 0;
            for (Entry entry : multiset.entrySet()) {
                this.elements[i] = entry.getElement();
                this.counts[i] = entry.getCount();
                i++;
            }
        }

        Object readResolve() {
            Iterable create = LinkedHashMultiset.create(this.elements.length);
            for (int i = 0; i < this.elements.length; i++) {
                create.add(this.elements[i], this.counts[i]);
            }
            return ImmutableMultiset.copyOf(create);
        }
    }

    ImmutableMultiset() {
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }

    static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Entry<? extends E>> collection) {
        com.google.common.collect.ImmutableMap.Builder builder = ImmutableMap.builder();
        long j = 0;
        for (Entry entry : collection) {
            int count = entry.getCount();
            if (count > 0) {
                builder.put(entry.getElement(), Integer.valueOf(count));
                j = ((long) count) + j;
            }
        }
        return j == 0 ? of() : new RegularImmutableMultiset(builder.build(), Ints.saturatedCast(j));
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> iterable) {
        if (iterable instanceof ImmutableMultiset) {
            ImmutableMultiset<E> immutableMultiset = (ImmutableMultiset) iterable;
            if (!immutableMultiset.isPartialView()) {
                return immutableMultiset;
            }
        }
        return copyOfInternal(iterable instanceof Multiset ? Multisets.cast(iterable) : LinkedHashMultiset.create((Iterable) iterable));
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> it) {
        Multiset create = LinkedHashMultiset.create();
        Iterators.addAll(create, it);
        return copyOfInternal(create);
    }

    public static <E> ImmutableMultiset<E> copyOf(E[] eArr) {
        return copyOf(Arrays.asList(eArr));
    }

    private static <E> ImmutableMultiset<E> copyOfInternal(Multiset<? extends E> multiset) {
        return copyFromEntries(multiset.entrySet());
    }

    private static <E> ImmutableMultiset<E> copyOfInternal(E... eArr) {
        return copyOf(Arrays.asList(eArr));
    }

    public static <E> ImmutableMultiset<E> of() {
        return EmptyImmutableMultiset.INSTANCE;
    }

    public static <E> ImmutableMultiset<E> of(E e) {
        return copyOfInternal(e);
    }

    public static <E> ImmutableMultiset<E> of(E e, E e2) {
        return copyOfInternal(e, e2);
    }

    public static <E> ImmutableMultiset<E> of(E e, E e2, E e3) {
        return copyOfInternal(e, e2, e3);
    }

    public static <E> ImmutableMultiset<E> of(E e, E e2, E e3, E e4) {
        return copyOfInternal(e, e2, e3, e4);
    }

    public static <E> ImmutableMultiset<E> of(E e, E e2, E e3, E e4, E e5) {
        return copyOfInternal(e, e2, e3, e4, e5);
    }

    public static <E> ImmutableMultiset<E> of(E e, E e2, E e3, E e4, E e5, E e6, E... eArr) {
        Iterable arrayList = new ArrayList(eArr.length + 6);
        Collections.addAll(arrayList, new Object[]{e, e2, e3, e4, e5, e6});
        Collections.addAll(arrayList, eArr);
        return copyOf(arrayList);
    }

    public final int add(E e, int i) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(@Nullable Object obj) {
        return count(obj) > 0;
    }

    public boolean containsAll(Collection<?> collection) {
        return elementSet().containsAll(collection);
    }

    abstract ImmutableSet<Entry<E>> createEntrySet();

    public ImmutableSet<Entry<E>> entrySet() {
        ImmutableSet<Entry<E>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = createEntrySet();
        this.entrySet = immutableSet;
        return immutableSet;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj != this) {
            if (!(obj instanceof Multiset)) {
                return false;
            }
            Multiset multiset = (Multiset) obj;
            if (size() != multiset.size()) {
                return false;
            }
            for (Entry entry : multiset.entrySet()) {
                if (count(entry.getElement()) != entry.getCount()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    public UnmodifiableIterator<E> iterator() {
        final Iterator it = entrySet().iterator();
        return new UnmodifiableIterator<E>() {
            E element;
            int remaining;

            public boolean hasNext() {
                return this.remaining > 0 || it.hasNext();
            }

            public E next() {
                if (this.remaining <= 0) {
                    Entry entry = (Entry) it.next();
                    this.element = entry.getElement();
                    this.remaining = entry.getCount();
                }
                this.remaining--;
                return this.element;
            }
        };
    }

    public final int remove(Object obj, int i) {
        throw new UnsupportedOperationException();
    }

    public final int setCount(E e, int i) {
        throw new UnsupportedOperationException();
    }

    public final boolean setCount(E e, int i, int i2) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return entrySet().toString();
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
