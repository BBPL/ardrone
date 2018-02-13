package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
class RegularImmutableAsList<E> extends ImmutableAsList<E> {
    private final ImmutableCollection<E> delegate;
    private final ImmutableList<? extends E> delegateList;

    RegularImmutableAsList(ImmutableCollection<E> immutableCollection, ImmutableList<? extends E> immutableList) {
        this.delegate = immutableCollection;
        this.delegateList = immutableList;
    }

    RegularImmutableAsList(ImmutableCollection<E> immutableCollection, Object[] objArr) {
        this((ImmutableCollection) immutableCollection, ImmutableList.asImmutableList(objArr));
    }

    ImmutableCollection<E> delegateCollection() {
        return this.delegate;
    }

    ImmutableList<? extends E> delegateList() {
        return this.delegateList;
    }

    public boolean equals(Object obj) {
        return this.delegateList.equals(obj);
    }

    public E get(int i) {
        return this.delegateList.get(i);
    }

    public int hashCode() {
        return this.delegateList.hashCode();
    }

    public int indexOf(Object obj) {
        return this.delegateList.indexOf(obj);
    }

    public int lastIndexOf(Object obj) {
        return this.delegateList.lastIndexOf(obj);
    }

    public UnmodifiableListIterator<E> listIterator(int i) {
        return this.delegateList.listIterator(i);
    }

    public Object[] toArray() {
        return this.delegateList.toArray();
    }

    public <T> T[] toArray(T[] tArr) {
        return this.delegateList.toArray(tArr);
    }
}
