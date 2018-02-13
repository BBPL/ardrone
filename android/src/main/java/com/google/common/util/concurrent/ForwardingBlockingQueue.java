package com.google.common.util.concurrent;

import com.google.common.collect.ForwardingQueue;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class ForwardingBlockingQueue<E> extends ForwardingQueue<E> implements BlockingQueue<E> {
    protected ForwardingBlockingQueue() {
    }

    protected abstract BlockingQueue<E> delegate();

    public int drainTo(Collection<? super E> collection) {
        return delegate().drainTo(collection);
    }

    public int drainTo(Collection<? super E> collection, int i) {
        return delegate().drainTo(collection, i);
    }

    public boolean offer(E e, long j, TimeUnit timeUnit) throws InterruptedException {
        return delegate().offer(e, j, timeUnit);
    }

    public E poll(long j, TimeUnit timeUnit) throws InterruptedException {
        return delegate().poll(j, timeUnit);
    }

    public void put(E e) throws InterruptedException {
        delegate().put(e);
    }

    public int remainingCapacity() {
        return delegate().remainingCapacity();
    }

    public E take() throws InterruptedException {
        return delegate().take();
    }
}
