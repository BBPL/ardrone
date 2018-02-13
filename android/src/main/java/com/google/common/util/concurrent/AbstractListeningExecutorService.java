package com.google.common.util.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

abstract class AbstractListeningExecutorService implements ListeningExecutorService {
    AbstractListeningExecutorService() {
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
        if (collection == null) {
            throw new NullPointerException();
        }
        List<Future<T>> arrayList = new ArrayList(collection.size());
        try {
            for (Callable create : collection) {
                Runnable create2 = ListenableFutureTask.create(create);
                arrayList.add(create2);
                execute(create2);
            }
            for (Future future : arrayList) {
                if (!future.isDone()) {
                    try {
                        future.get();
                    } catch (CancellationException e) {
                    } catch (ExecutionException e2) {
                    }
                }
            }
            return arrayList;
        } catch (Throwable th) {
            Throwable th2 = th;
            for (Future future2 : arrayList) {
                future2.cancel(true);
            }
        }
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long j, TimeUnit timeUnit) throws InterruptedException {
        if (collection == null || timeUnit == null) {
            throw new NullPointerException();
        }
        long toNanos = timeUnit.toNanos(j);
        List<Future<T>> arrayList = new ArrayList(collection.size());
        try {
            for (Callable create : collection) {
                arrayList.add(ListenableFutureTask.create(create));
            }
            long nanoTime = System.nanoTime();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                execute((Runnable) it.next());
                long nanoTime2 = System.nanoTime();
                toNanos -= nanoTime2 - nanoTime;
                if (toNanos <= 0) {
                    for (Future cancel : arrayList) {
                        cancel.cancel(true);
                    }
                    return arrayList;
                }
                nanoTime = nanoTime2;
            }
            for (Future cancel2 : arrayList) {
                if (!cancel2.isDone()) {
                    if (toNanos <= 0) {
                        for (Future cancel22 : arrayList) {
                            cancel22.cancel(true);
                        }
                        return arrayList;
                    }
                    try {
                        cancel22.get(toNanos, TimeUnit.NANOSECONDS);
                    } catch (CancellationException e) {
                    } catch (ExecutionException e2) {
                    } catch (TimeoutException e3) {
                        for (Future cancel222 : arrayList) {
                            cancel222.cancel(true);
                        }
                    }
                    long nanoTime3 = System.nanoTime();
                    toNanos -= nanoTime3 - nanoTime;
                    nanoTime = nanoTime3;
                }
            }
            return arrayList;
        } catch (Throwable th) {
            Throwable th2 = th;
            for (Future cancel2222 : arrayList) {
                cancel2222.cancel(true);
            }
        }
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
        try {
            return MoreExecutors.invokeAnyImpl(this, collection, false, 0);
        } catch (TimeoutException e) {
            throw new AssertionError();
        }
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return MoreExecutors.invokeAnyImpl(this, collection, true, timeUnit.toNanos(j));
    }

    public ListenableFuture<?> submit(Runnable runnable) {
        Object create = ListenableFutureTask.create(runnable, null);
        execute(create);
        return create;
    }

    public <T> ListenableFuture<T> submit(Runnable runnable, T t) {
        Object create = ListenableFutureTask.create(runnable, t);
        execute(create);
        return create;
    }

    public <T> ListenableFuture<T> submit(Callable<T> callable) {
        Object create = ListenableFutureTask.create(callable);
        execute(create);
        return create;
    }
}
