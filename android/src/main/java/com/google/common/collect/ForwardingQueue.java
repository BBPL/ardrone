package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.NoSuchElementException;
import java.util.Queue;

@GwtCompatible
public abstract class ForwardingQueue<E> extends ForwardingCollection<E> implements Queue<E> {
    protected ForwardingQueue() {
    }

    protected abstract Queue<E> delegate();

    public E element() {
        return delegate().element();
    }

    public boolean offer(E e) {
        return delegate().offer(e);
    }

    public E peek() {
        return delegate().peek();
    }

    public E poll() {
        return delegate().poll();
    }

    public E remove() {
        return delegate().remove();
    }

    @Beta
    protected boolean standardOffer(E e) {
        try {
            return add(e);
        } catch (IllegalStateException e2) {
            return false;
        }
    }

    @Beta
    protected E standardPeek() {
        try {
            return element();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Beta
    protected E standardPoll() {
        try {
            return remove();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
