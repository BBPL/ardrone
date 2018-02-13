package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.Iterator;

@GwtCompatible(emulated = true)
abstract class TransformedImmutableSet<D, E> extends ImmutableSet<E> {
    final int hashCode;
    final ImmutableCollection<D> source;

    TransformedImmutableSet(ImmutableCollection<D> immutableCollection) {
        this.source = immutableCollection;
        this.hashCode = Sets.hashCodeImpl(this);
    }

    TransformedImmutableSet(ImmutableCollection<D> immutableCollection, int i) {
        this.source = immutableCollection;
        this.hashCode = i;
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public boolean isEmpty() {
        return false;
    }

    @GwtIncompatible("unused")
    boolean isHashCodeFast() {
        return true;
    }

    public UnmodifiableIterator<E> iterator() {
        final Iterator it = this.source.iterator();
        return new UnmodifiableIterator<E>() {
            public boolean hasNext() {
                return it.hasNext();
            }

            public E next() {
                return TransformedImmutableSet.this.transform(it.next());
            }
        };
    }

    public int size() {
        return this.source.size();
    }

    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    public <T> T[] toArray(T[] tArr) {
        return ObjectArrays.toArrayImpl(this, tArr);
    }

    abstract E transform(D d);
}
