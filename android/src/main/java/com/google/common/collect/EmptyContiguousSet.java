package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
final class EmptyContiguousSet<C extends Comparable> extends ContiguousSet<C> {

    @GwtIncompatible("serialization")
    private static final class SerializedForm<C extends Comparable> implements Serializable {
        private static final long serialVersionUID = 0;
        private final DiscreteDomain<C> domain;

        private SerializedForm(DiscreteDomain<C> discreteDomain) {
            this.domain = discreteDomain;
        }

        private Object readResolve() {
            return new EmptyContiguousSet(this.domain);
        }
    }

    EmptyContiguousSet(DiscreteDomain<C> discreteDomain) {
        super(discreteDomain);
    }

    public ImmutableList<C> asList() {
        return ImmutableList.of();
    }

    @GwtIncompatible("NavigableSet")
    ImmutableSortedSet<C> createDescendingSet() {
        return new EmptyImmutableSortedSet(Ordering.natural().reverse());
    }

    public boolean equals(@Nullable Object obj) {
        return obj instanceof Set ? ((Set) obj).isEmpty() : false;
    }

    public C first() {
        throw new NoSuchElementException();
    }

    public int hashCode() {
        return 0;
    }

    ContiguousSet<C> headSetImpl(C c, boolean z) {
        return this;
    }

    @GwtIncompatible("not used by GWT emulation")
    int indexOf(Object obj) {
        return -1;
    }

    public ContiguousSet<C> intersection(ContiguousSet<C> contiguousSet) {
        return this;
    }

    public boolean isEmpty() {
        return true;
    }

    boolean isPartialView() {
        return false;
    }

    public UnmodifiableIterator<C> iterator() {
        return Iterators.emptyIterator();
    }

    public C last() {
        throw new NoSuchElementException();
    }

    public Range<C> range() {
        throw new NoSuchElementException();
    }

    public Range<C> range(BoundType boundType, BoundType boundType2) {
        throw new NoSuchElementException();
    }

    public int size() {
        return 0;
    }

    ContiguousSet<C> subSetImpl(C c, boolean z, C c2, boolean z2) {
        return this;
    }

    ContiguousSet<C> tailSetImpl(C c, boolean z) {
        return this;
    }

    public String toString() {
        return "[]";
    }

    @GwtIncompatible("serialization")
    Object writeReplace() {
        return new SerializedForm(this.domain);
    }
}
