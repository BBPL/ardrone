package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public final class Uninterruptibles {
    private Uninterruptibles() {
    }

    public static void awaitUninterruptibly(CountDownLatch countDownLatch) {
        Object obj = null;
        while (true) {
            try {
                countDownLatch.await();
                break;
            } catch (InterruptedException e) {
                obj = 1;
            } catch (Throwable th) {
                if (obj != null) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (obj != null) {
            Thread.currentThread().interrupt();
        }
    }

    public static boolean awaitUninterruptibly(CountDownLatch countDownLatch, long j, TimeUnit timeUnit) {
        Throwable th;
        Object obj = 1;
        Object obj2 = null;
        try {
            boolean await;
            long toNanos = timeUnit.toNanos(j);
            long nanoTime = System.nanoTime();
            long j2 = toNanos;
            while (true) {
                try {
                    await = countDownLatch.await(j2, TimeUnit.NANOSECONDS);
                    break;
                } catch (InterruptedException e) {
                    j2 = (nanoTime + toNanos) - System.nanoTime();
                    obj2 = obj;
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            if (obj2 != null) {
                Thread.currentThread().interrupt();
            }
            return await;
        } catch (Throwable th3) {
            Throwable th4 = th3;
            obj = null;
            th = th4;
            if (obj != null) {
                Thread.currentThread().interrupt();
            }
            throw th;
        }
    }

    public static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
        V v;
        Object obj = null;
        while (true) {
            try {
                v = future.get();
                break;
            } catch (InterruptedException e) {
                obj = 1;
            } catch (Throwable th) {
                if (obj != null) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (obj != null) {
            Thread.currentThread().interrupt();
        }
        return v;
    }

    public static <V> V getUninterruptibly(Future<V> future, long j, TimeUnit timeUnit) throws ExecutionException, TimeoutException {
        Throwable th;
        Object obj = 1;
        Object obj2 = null;
        try {
            V v;
            long toNanos = timeUnit.toNanos(j);
            long nanoTime = System.nanoTime();
            long j2 = toNanos;
            while (true) {
                try {
                    v = future.get(j2, TimeUnit.NANOSECONDS);
                    break;
                } catch (InterruptedException e) {
                    j2 = (nanoTime + toNanos) - System.nanoTime();
                    obj2 = obj;
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            if (obj2 != null) {
                Thread.currentThread().interrupt();
            }
            return v;
        } catch (Throwable th3) {
            Throwable th4 = th3;
            obj = null;
            th = th4;
            if (obj != null) {
                Thread.currentThread().interrupt();
            }
            throw th;
        }
    }

    public static void joinUninterruptibly(Thread thread) {
        Object obj = null;
        while (true) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                obj = 1;
            } catch (Throwable th) {
                if (obj != null) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (obj != null) {
            Thread.currentThread().interrupt();
        }
    }

    public static void joinUninterruptibly(Thread thread, long j, TimeUnit timeUnit) {
        Throwable th;
        Object obj = 1;
        Object obj2 = null;
        Preconditions.checkNotNull(thread);
        try {
            long toNanos = timeUnit.toNanos(j);
            long nanoTime = System.nanoTime();
            long j2 = toNanos;
            while (true) {
                try {
                    TimeUnit.NANOSECONDS.timedJoin(thread, j2);
                    break;
                } catch (InterruptedException e) {
                    j2 = (nanoTime + toNanos) - System.nanoTime();
                    int i = 1;
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            if (obj2 != null) {
                Thread.currentThread().interrupt();
            }
        } catch (Throwable th3) {
            Throwable th4 = th3;
            obj = null;
            th = th4;
            if (obj != null) {
                Thread.currentThread().interrupt();
            }
            throw th;
        }
    }

    public static <E> void putUninterruptibly(BlockingQueue<E> blockingQueue, E e) {
        Object obj = null;
        while (true) {
            try {
                blockingQueue.put(e);
                break;
            } catch (InterruptedException e2) {
                obj = 1;
            } catch (Throwable th) {
                if (obj != null) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (obj != null) {
            Thread.currentThread().interrupt();
        }
    }

    public static void sleepUninterruptibly(long j, TimeUnit timeUnit) {
        Throwable th;
        Object obj = 1;
        Object obj2 = null;
        try {
            long toNanos = timeUnit.toNanos(j);
            long nanoTime = System.nanoTime();
            long j2 = toNanos;
            while (true) {
                try {
                    TimeUnit.NANOSECONDS.sleep(j2);
                    break;
                } catch (InterruptedException e) {
                    j2 = (nanoTime + toNanos) - System.nanoTime();
                    int i = 1;
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            if (obj2 != null) {
                Thread.currentThread().interrupt();
            }
        } catch (Throwable th3) {
            Throwable th4 = th3;
            obj = null;
            th = th4;
            if (obj != null) {
                Thread.currentThread().interrupt();
            }
            throw th;
        }
    }

    public static <E> E takeUninterruptibly(BlockingQueue<E> blockingQueue) {
        E take;
        Object obj = null;
        while (true) {
            try {
                take = blockingQueue.take();
                break;
            } catch (InterruptedException e) {
                obj = 1;
            } catch (Throwable th) {
                if (obj != null) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (obj != null) {
            Thread.currentThread().interrupt();
        }
        return take;
    }
}
