package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E> {
    private static final int CUTOFF = ((int) Math.floor(7.516192768E8d));
    private static final double DESIRED_LOAD_FACTOR = 0.7d;
    static final int MAX_TABLE_SIZE = 1073741824;

    static abstract class ArrayImmutableSet<E> extends ImmutableSet<E> {
        final transient Object[] elements;

        ArrayImmutableSet(Object[] objArr) {
            this.elements = objArr;
        }

        public boolean containsAll(Collection<?> collection) {
            if (collection != this) {
                if (!(collection instanceof ArrayImmutableSet)) {
                    return super.containsAll(collection);
                }
                if (collection.size() > size()) {
                    return false;
                }
                for (Object contains : ((ArrayImmutableSet) collection).elements) {
                    if (!contains(contains)) {
                        return false;
                    }
                }
            }
            return true;
        }

        ImmutableList<E> createAsList() {
            return new RegularImmutableAsList((ImmutableCollection) this, this.elements);
        }

        public boolean isEmpty() {
            return false;
        }

        boolean isPartialView() {
            return false;
        }

        public UnmodifiableIterator<E> iterator() {
            return asList().iterator();
        }

        public int size() {
            return this.elements.length;
        }

        public Object[] toArray() {
            return asList().toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return asList().toArray(tArr);
        }
    }

    public static class Builder<E> extends com.google.common.collect.ImmutableCollection.Builder<E> {
        Object[] contents;
        int size;

        public Builder() {
            this(4);
        }

        Builder(int i) {
            Preconditions.checkArgument(i >= 0, "capacity must be >= 0 but was %s", Integer.valueOf(i));
            this.contents = new Object[i];
            this.size = 0;
        }

        public Builder<E> add(E e) {
            expandFor(1);
            Object[] objArr = this.contents;
            int i = this.size;
            this.size = i + 1;
            objArr[i] = Preconditions.checkNotNull(e);
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

        public ImmutableSet<E> build() {
            ImmutableSet<E> access$000 = ImmutableSet.construct(this.size, this.contents);
            this.size = access$000.size();
            return access$000;
        }

        Builder<E> expandFor(int i) {
            int i2 = this.size + i;
            if (this.contents.length < i2) {
                this.contents = ObjectArrays.arraysCopyOf(this.contents, com.google.common.collect.ImmutableCollection.Builder.expandedCapacity(this.contents.length, i2));
            }
            return this;
        }
    }

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Object[] elements;

        SerializedForm(Object[] objArr) {
            this.elements = objArr;
        }

        Object readResolve() {
            return ImmutableSet.copyOf(this.elements);
        }
    }

    ImmutableSet() {
    }

    public static <E> Builder<E> builder() {
        return new Builder();
    }

    @VisibleForTesting
    static int chooseTableSize(int i) {
        if (i < CUTOFF) {
            int highestOneBit = Integer.highestOneBit(i - 1) << 1;
            while (((double) highestOneBit) * DESIRED_LOAD_FACTOR < ((double) i)) {
                highestOneBit <<= 1;
            }
            return highestOneBit;
        }
        Preconditions.checkArgument(i < 1073741824, "collection too large");
        return 1073741824;
    }

    private static <E> ImmutableSet<E> construct(int i, Object... objArr) {
        switch (i) {
            case 0:
                return of();
            case 1:
                return of(objArr[0]);
            default:
                int chooseTableSize = chooseTableSize(i);
                Object[] objArr2 = new Object[chooseTableSize];
                int i2 = chooseTableSize - 1;
                int i3 = 0;
                int i4 = 0;
                int i5 = 0;
                while (i4 < i) {
                    Object checkElementNotNull = ObjectArrays.checkElementNotNull(objArr[i4], i4);
                    int hashCode = checkElementNotNull.hashCode();
                    int smear = Hashing.smear(hashCode);
                    while (true) {
                        int i6 = smear & i2;
                        Object obj = objArr2[i6];
                        if (obj == null) {
                            int i7 = i5 + 1;
                            objArr[i5] = checkElementNotNull;
                            objArr2[i6] = checkElementNotNull;
                            smear = i3 + hashCode;
                            i3 = i7;
                        } else if (obj.equals(checkElementNotNull)) {
                            smear = i3;
                            i3 = i5;
                        } else {
                            smear++;
                        }
                        i4++;
                        i5 = i3;
                        i3 = smear;
                    }
                }
                Arrays.fill(objArr, i5, i, null);
                if (i5 == 1) {
                    return new SingletonImmutableSet(objArr[0], i3);
                }
                if (chooseTableSize != chooseTableSize(i5)) {
                    return construct(i5, objArr);
                }
                if (i5 < objArr.length) {
                    objArr = ObjectArrays.arraysCopyOf(objArr, i5);
                }
                return new RegularImmutableSet(objArr, i3, objArr2, i2);
        }
    }

    private static <E> ImmutableSet<E> copyFromCollection(Collection<? extends E> collection) {
        Object[] toArray = collection.toArray();
        switch (toArray.length) {
            case 0:
                return of();
            case 1:
                return of(toArray[0]);
            default:
                return construct(toArray.length, toArray);
        }
    }

    public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> iterable) {
        return iterable instanceof Collection ? copyOf(Collections2.cast(iterable)) : copyOf(iterable.iterator());
    }

    public static <E> ImmutableSet<E> copyOf(Collection<? extends E> collection) {
        if ((collection instanceof ImmutableSet) && !(collection instanceof ImmutableSortedSet)) {
            ImmutableSet<E> immutableSet = (ImmutableSet) collection;
            if (!immutableSet.isPartialView()) {
                return immutableSet;
            }
        }
        return copyFromCollection(collection);
    }

    public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return of();
        }
        Object next = it.next();
        return !it.hasNext() ? of(next) : new Builder().add(next).addAll((Iterator) it).build();
    }

    public static <E> ImmutableSet<E> copyOf(E[] eArr) {
        switch (eArr.length) {
            case 0:
                return of();
            case 1:
                return of(eArr[0]);
            default:
                return construct(eArr.length, (Object[]) eArr.clone());
        }
    }

    public static <E> ImmutableSet<E> of() {
        return EmptyImmutableSet.INSTANCE;
    }

    public static <E> ImmutableSet<E> of(E e) {
        return new SingletonImmutableSet(e);
    }

    public static <E> ImmutableSet<E> of(E e, E e2) {
        return construct(2, e, e2);
    }

    public static <E> ImmutableSet<E> of(E e, E e2, E e3) {
        return construct(3, e, e2, e3);
    }

    public static <E> ImmutableSet<E> of(E e, E e2, E e3, E e4) {
        return construct(4, e, e2, e3, e4);
    }

    public static <E> ImmutableSet<E> of(E e, E e2, E e3, E e4, E e5) {
        return construct(5, e, e2, e3, e4, e5);
    }

    public static <E> ImmutableSet<E> of(E e, E e2, E e3, E e4, E e5, E e6, E... eArr) {
        Object obj = new Object[(eArr.length + 6)];
        obj[0] = e;
        obj[1] = e2;
        obj[2] = e3;
        obj[3] = e4;
        obj[4] = e5;
        obj[5] = e6;
        System.arraycopy(eArr, 0, obj, 6, eArr.length);
        return construct(obj.length, obj);
    }

    public boolean equals(@Nullable Object obj) {
        return obj == this ? true : ((obj instanceof ImmutableSet) && isHashCodeFast() && ((ImmutableSet) obj).isHashCodeFast() && hashCode() != obj.hashCode()) ? false : Sets.equalsImpl(this, obj);
    }

    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }

    boolean isHashCodeFast() {
        return false;
    }

    public abstract UnmodifiableIterator<E> iterator();

    Object writeReplace() {
        return new SerializedForm(toArray());
    }
}
