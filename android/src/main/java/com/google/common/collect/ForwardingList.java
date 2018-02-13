package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingList<E> extends ForwardingCollection<E> implements List<E> {
    protected ForwardingList() {
    }

    public void add(int i, E e) {
        delegate().add(i, e);
    }

    public boolean addAll(int i, Collection<? extends E> collection) {
        return delegate().addAll(i, collection);
    }

    protected abstract List<E> delegate();

    public boolean equals(@Nullable Object obj) {
        return obj == this || delegate().equals(obj);
    }

    public E get(int i) {
        return delegate().get(i);
    }

    public int hashCode() {
        return delegate().hashCode();
    }

    public int indexOf(Object obj) {
        return delegate().indexOf(obj);
    }

    public int lastIndexOf(Object obj) {
        return delegate().lastIndexOf(obj);
    }

    public ListIterator<E> listIterator() {
        return delegate().listIterator();
    }

    public ListIterator<E> listIterator(int i) {
        return delegate().listIterator(i);
    }

    public E remove(int i) {
        return delegate().remove(i);
    }

    public E set(int i, E e) {
        return delegate().set(i, e);
    }

    @Beta
    protected boolean standardAdd(E e) {
        add(size(), e);
        return true;
    }

    @Beta
    protected boolean standardAddAll(int i, Iterable<? extends E> iterable) {
        return Lists.addAllImpl(this, i, iterable);
    }

    @Beta
    protected boolean standardEquals(@Nullable Object obj) {
        return Lists.equalsImpl(this, obj);
    }

    @Beta
    protected int standardHashCode() {
        return Lists.hashCodeImpl(this);
    }

    @Beta
    protected int standardIndexOf(@Nullable Object obj) {
        return Lists.indexOfImpl(this, obj);
    }

    @Beta
    protected Iterator<E> standardIterator() {
        return listIterator();
    }

    @Beta
    protected int standardLastIndexOf(@Nullable Object obj) {
        return Lists.lastIndexOfImpl(this, obj);
    }

    @Beta
    protected ListIterator<E> standardListIterator() {
        return listIterator(0);
    }

    @Beta
    protected ListIterator<E> standardListIterator(int i) {
        return Lists.listIteratorImpl(this, i);
    }

    @Beta
    protected List<E> standardSubList(int i, int i2) {
        return Lists.subListImpl(this, i, i2);
    }

    public List<E> subList(int i, int i2) {
        return delegate().subList(i, i2);
    }
}
