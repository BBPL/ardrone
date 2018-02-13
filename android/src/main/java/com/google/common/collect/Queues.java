package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

@Beta
public final class Queues {
    private Queues() {
    }

    public static <E> int drain(BlockingQueue<E> blockingQueue, Collection<? super E> collection, int i, long j, TimeUnit timeUnit) throws InterruptedException {
        Preconditions.checkNotNull(collection);
        long nanoTime = System.nanoTime();
        long toNanos = timeUnit.toNanos(j);
        int i2 = 0;
        while (i2 < i) {
            i2 += blockingQueue.drainTo(collection, i - i2);
            if (i2 < i) {
                Object poll = blockingQueue.poll((nanoTime + toNanos) - System.nanoTime(), TimeUnit.NANOSECONDS);
                if (poll == null) {
                    break;
                }
                collection.add(poll);
                i2++;
            }
        }
        return i2;
    }

    public static <E> int drainUninterruptibly(BlockingQueue<E> blockingQueue, Collection<? super E> collection, int i, long j, TimeUnit timeUnit) {
        Preconditions.checkNotNull(collection);
        long nanoTime = System.nanoTime();
        long toNanos = timeUnit.toNanos(j);
        int i2 = 0;
        Object obj = null;
        while (i2 < i) {
            try {
                i2 += blockingQueue.drainTo(collection, i - i2);
                if (i2 < i) {
                    Object poll;
                    while (true) {
                        try {
                            poll = blockingQueue.poll((nanoTime + toNanos) - System.nanoTime(), TimeUnit.NANOSECONDS);
                            break;
                        } catch (InterruptedException e) {
                            obj = 1;
                        }
                    }
                    if (poll == null) {
                        break;
                    }
                    collection.add(poll);
                    i2++;
                }
            } catch (Throwable th) {
                Throwable th2 = th;
                Object obj2 = obj;
                Throwable th3 = th2;
                if (obj2 != null) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (obj != null) {
            Thread.currentThread().interrupt();
        }
        return i2;
    }

    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int i) {
        return new ArrayBlockingQueue(i);
    }

    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
        return new ConcurrentLinkedQueue();
    }

    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return new ConcurrentLinkedQueue(Collections2.cast(iterable));
        }
        Object concurrentLinkedQueue = new ConcurrentLinkedQueue();
        Iterables.addAll(concurrentLinkedQueue, iterable);
        return concurrentLinkedQueue;
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
        return new LinkedBlockingQueue();
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int i) {
        return new LinkedBlockingQueue(i);
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return new LinkedBlockingQueue(Collections2.cast(iterable));
        }
        Object linkedBlockingQueue = new LinkedBlockingQueue();
        Iterables.addAll(linkedBlockingQueue, iterable);
        return linkedBlockingQueue;
    }

    public static <E> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
        return new PriorityBlockingQueue();
    }

    public static <E> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return new PriorityBlockingQueue(Collections2.cast(iterable));
        }
        Object priorityBlockingQueue = new PriorityBlockingQueue();
        Iterables.addAll(priorityBlockingQueue, iterable);
        return priorityBlockingQueue;
    }

    public static <E> PriorityQueue<E> newPriorityQueue() {
        return new PriorityQueue();
    }

    public static <E> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return new PriorityQueue(Collections2.cast(iterable));
        }
        Object priorityQueue = new PriorityQueue();
        Iterables.addAll(priorityQueue, iterable);
        return priorityQueue;
    }

    public static <E> SynchronousQueue<E> newSynchronousQueue() {
        return new SynchronousQueue();
    }
}
