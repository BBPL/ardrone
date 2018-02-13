package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

@Beta
public final class Monitor {
    @GuardedBy("lock")
    private final ArrayList<Guard> activeGuards;
    private final boolean fair;
    private final ReentrantLock lock;

    @Beta
    public static abstract class Guard {
        final Condition condition;
        final Monitor monitor;
        @GuardedBy("monitor.lock")
        int waiterCount = 0;

        protected Guard(Monitor monitor) {
            this.monitor = (Monitor) Preconditions.checkNotNull(monitor, "monitor");
            this.condition = monitor.lock.newCondition();
        }

        public final boolean equals(Object obj) {
            return this == obj;
        }

        public final int hashCode() {
            return super.hashCode();
        }

        public abstract boolean isSatisfied();
    }

    public Monitor() {
        this(false);
    }

    public Monitor(boolean z) {
        this.activeGuards = Lists.newArrayListWithCapacity(1);
        this.fair = z;
        this.lock = new ReentrantLock(z);
    }

    @GuardedBy("lock")
    private void decrementWaiters(Guard guard) {
        int i = guard.waiterCount - 1;
        guard.waiterCount = i;
        if (i == 0) {
            this.activeGuards.remove(guard);
        }
    }

    @GuardedBy("lock")
    private void incrementWaiters(Guard guard) {
        int i = guard.waiterCount;
        guard.waiterCount = i + 1;
        if (i == 0) {
            this.activeGuards.add(guard);
        }
    }

    @GuardedBy("lock")
    private void signalConditionsOfSatisfiedGuards(@Nullable Guard guard) {
        int i = 0;
        ArrayList arrayList = this.activeGuards;
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            try {
                Guard guard2 = (Guard) arrayList.get(i2);
                if (!(guard2 == guard && guard2.waiterCount == 1) && guard2.isSatisfied()) {
                    guard2.condition.signal();
                    return;
                }
                i2++;
            } catch (Throwable th) {
                Throwable th2 = th;
                while (i < size) {
                    ((Guard) arrayList.get(i)).condition.signalAll();
                    i++;
                }
                RuntimeException propagate = Throwables.propagate(th2);
            }
        }
    }

    @GuardedBy("lock")
    private void waitInterruptibly(Guard guard, boolean z) throws InterruptedException {
        if (!guard.isSatisfied()) {
            if (z) {
                signalConditionsOfSatisfiedGuards(null);
            }
            incrementWaiters(guard);
            try {
                Condition condition = guard.condition;
                do {
                    condition.await();
                } while (!guard.isSatisfied());
                decrementWaiters(guard);
            } catch (InterruptedException e) {
                signalConditionsOfSatisfiedGuards(guard);
                throw e;
            } catch (Throwable th) {
                decrementWaiters(guard);
            }
        }
    }

    @GuardedBy("lock")
    private boolean waitInterruptibly(Guard guard, long j, boolean z) throws InterruptedException {
        if (!guard.isSatisfied()) {
            if (z) {
                signalConditionsOfSatisfiedGuards(null);
            }
            incrementWaiters(guard);
            try {
                Condition condition = guard.condition;
                while (j > 0) {
                    j = condition.awaitNanos(j);
                    if (guard.isSatisfied()) {
                        decrementWaiters(guard);
                    }
                }
                decrementWaiters(guard);
                return false;
            } catch (InterruptedException e) {
                signalConditionsOfSatisfiedGuards(guard);
                throw e;
            } catch (Throwable th) {
                decrementWaiters(guard);
            }
        }
        return true;
    }

    @GuardedBy("lock")
    private void waitUninterruptibly(Guard guard, boolean z) {
        if (!guard.isSatisfied()) {
            if (z) {
                signalConditionsOfSatisfiedGuards(null);
            }
            incrementWaiters(guard);
            try {
                Condition condition = guard.condition;
                while (true) {
                    condition.awaitUninterruptibly();
                    if (guard.isSatisfied()) {
                        break;
                    }
                }
            } finally {
                decrementWaiters(guard);
            }
        }
    }

    @GuardedBy("lock")
    private boolean waitUninterruptibly(Guard guard, long j, boolean z) {
        Throwable th;
        if (guard.isSatisfied()) {
            return true;
        }
        long nanoTime = System.nanoTime();
        if (z) {
            signalConditionsOfSatisfiedGuards(null);
        }
        boolean z2;
        try {
            incrementWaiters(guard);
            try {
                Condition condition = guard.condition;
                long j2 = j;
                z2 = false;
                while (j2 > 0) {
                    try {
                        j2 = condition.awaitNanos(j2);
                        try {
                            if (guard.isSatisfied()) {
                                decrementWaiters(guard);
                                if (!z2) {
                                    return true;
                                }
                                Thread.currentThread().interrupt();
                                return true;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (InterruptedException e) {
                        signalConditionsOfSatisfiedGuards(guard);
                        j2 = j - (System.nanoTime() - nanoTime);
                        z2 = true;
                    } catch (Throwable th3) {
                        th = th3;
                        z2 = true;
                    }
                }
                try {
                    decrementWaiters(guard);
                    if (z2) {
                        Thread.currentThread().interrupt();
                    }
                    return false;
                } catch (Throwable th4) {
                    th = th4;
                    if (z2) {
                        Thread.currentThread().interrupt();
                    }
                    throw th;
                }
            } catch (Throwable th5) {
                th = th5;
                z2 = false;
                decrementWaiters(guard);
                throw th;
            }
        } catch (Throwable th6) {
            th = th6;
            z2 = false;
            if (z2) {
                Thread.currentThread().interrupt();
            }
            throw th;
        }
    }

    public void enter() {
        this.lock.lock();
    }

    public boolean enter(long j, TimeUnit timeUnit) {
        Throwable th;
        boolean z = true;
        ReentrantLock reentrantLock = this.lock;
        if (this.fair || !reentrantLock.tryLock()) {
            long nanoTime = System.nanoTime();
            long toNanos = timeUnit.toNanos(j);
            boolean z2 = false;
            long j2 = toNanos;
            while (true) {
                try {
                    z = reentrantLock.tryLock(j2, TimeUnit.NANOSECONDS);
                    break;
                } catch (InterruptedException e) {
                    j2 = toNanos - (System.nanoTime() - nanoTime);
                    z2 = z;
                } catch (Throwable th2) {
                    th = th2;
                }
            }
            if (z2) {
                Thread.currentThread().interrupt();
            }
        }
        return z;
        if (z) {
            Thread.currentThread().interrupt();
        }
        throw th;
    }

    public boolean enterIf(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            boolean isSatisfied = guard.isSatisfied();
            if (!isSatisfied) {
            }
            return isSatisfied;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean enterIf(Guard guard, long j, TimeUnit timeUnit) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock reentrantLock = this.lock;
        if (!enter(j, timeUnit)) {
            return false;
        }
        try {
            boolean isSatisfied = guard.isSatisfied();
            return !isSatisfied ? isSatisfied : isSatisfied;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        try {
            boolean isSatisfied = guard.isSatisfied();
            if (!isSatisfied) {
            }
            return isSatisfied;
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean enterIfInterruptibly(Guard guard, long j, TimeUnit timeUnit) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock reentrantLock = this.lock;
        if (!reentrantLock.tryLock(j, timeUnit)) {
            return false;
        }
        try {
            boolean isSatisfied = guard.isSatisfied();
            return !isSatisfied ? isSatisfied : isSatisfied;
        } finally {
            reentrantLock.unlock();
        }
    }

    public void enterInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    public boolean enterInterruptibly(long j, TimeUnit timeUnit) throws InterruptedException {
        return this.lock.tryLock(j, timeUnit);
    }

    public void enterWhen(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock reentrantLock = this.lock;
        boolean isHeldByCurrentThread = reentrantLock.isHeldByCurrentThread();
        reentrantLock.lockInterruptibly();
        try {
            waitInterruptibly(guard, isHeldByCurrentThread);
        } catch (Throwable th) {
            reentrantLock.unlock();
        }
    }

    public boolean enterWhen(Guard guard, long j, TimeUnit timeUnit) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        long nanoTime;
        ReentrantLock reentrantLock = this.lock;
        boolean isHeldByCurrentThread = reentrantLock.isHeldByCurrentThread();
        if (this.fair || !reentrantLock.tryLock()) {
            nanoTime = System.nanoTime();
            if (!reentrantLock.tryLock(j, timeUnit)) {
                return false;
            }
            nanoTime = timeUnit.toNanos(j) - (System.nanoTime() - nanoTime);
        } else {
            nanoTime = timeUnit.toNanos(j);
        }
        try {
            boolean waitInterruptibly = waitInterruptibly(guard, nanoTime, isHeldByCurrentThread);
            return !waitInterruptibly ? waitInterruptibly : waitInterruptibly;
        } finally {
            reentrantLock.unlock();
        }
    }

    public void enterWhenUninterruptibly(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock reentrantLock = this.lock;
        boolean isHeldByCurrentThread = reentrantLock.isHeldByCurrentThread();
        reentrantLock.lock();
        try {
            waitUninterruptibly(guard, isHeldByCurrentThread);
        } catch (Throwable th) {
            reentrantLock.unlock();
        }
    }

    public boolean enterWhenUninterruptibly(Guard guard, long j, TimeUnit timeUnit) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        long j2;
        ReentrantLock reentrantLock = this.lock;
        boolean isHeldByCurrentThread = reentrantLock.isHeldByCurrentThread();
        Object obj = null;
        if (this.fair || !reentrantLock.tryLock()) {
            long nanoTime;
            long toNanos;
            try {
                nanoTime = System.nanoTime();
                toNanos = timeUnit.toNanos(j);
                obj = null;
                j2 = toNanos;
                while (true) {
                    break;
                }
                if (reentrantLock.tryLock(j2, TimeUnit.NANOSECONDS)) {
                    j2 = toNanos - (System.nanoTime() - nanoTime);
                } else {
                    System.nanoTime();
                    if (obj == null) {
                        return false;
                    }
                    Thread.currentThread().interrupt();
                    return false;
                }
            } catch (InterruptedException e) {
                obj = 1;
                j2 = toNanos - (System.nanoTime() - nanoTime);
                int i = 1;
            } catch (Throwable th) {
                if (obj != null) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            j2 = timeUnit.toNanos(j);
            obj = null;
        }
        boolean waitUninterruptibly = waitUninterruptibly(guard, j2, isHeldByCurrentThread);
        if (!waitUninterruptibly) {
            reentrantLock.unlock();
        }
        if (obj == null) {
            return waitUninterruptibly;
        }
        Thread.currentThread().interrupt();
        return waitUninterruptibly;
    }

    public int getOccupiedDepth() {
        return this.lock.getHoldCount();
    }

    public int getQueueLength() {
        return this.lock.getQueueLength();
    }

    public int getWaitQueueLength(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        this.lock.lock();
        try {
            int i = guard.waiterCount;
            return i;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean hasQueuedThread(Thread thread) {
        return this.lock.hasQueuedThread(thread);
    }

    public boolean hasQueuedThreads() {
        return this.lock.hasQueuedThreads();
    }

    public boolean hasWaiters(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        this.lock.lock();
        try {
            boolean z = guard.waiterCount > 0;
            this.lock.unlock();
            return z;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public boolean isFair() {
        return this.lock.isFair();
    }

    public boolean isOccupied() {
        return this.lock.isLocked();
    }

    public boolean isOccupiedByCurrentThread() {
        return this.lock.isHeldByCurrentThread();
    }

    public void leave() {
        ReentrantLock reentrantLock = this.lock;
        if (reentrantLock.isHeldByCurrentThread()) {
            try {
                signalConditionsOfSatisfiedGuards(null);
            } finally {
                reentrantLock.unlock();
            }
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public boolean tryEnter() {
        return this.lock.tryLock();
    }

    public boolean tryEnterIf(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock reentrantLock = this.lock;
        if (!reentrantLock.tryLock()) {
            return false;
        }
        try {
            boolean isSatisfied = guard.isSatisfied();
            return !isSatisfied ? isSatisfied : isSatisfied;
        } finally {
            reentrantLock.unlock();
        }
    }

    public void waitFor(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        } else if (this.lock.isHeldByCurrentThread()) {
            waitInterruptibly(guard, true);
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public boolean waitFor(Guard guard, long j, TimeUnit timeUnit) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        } else if (this.lock.isHeldByCurrentThread()) {
            return waitInterruptibly(guard, timeUnit.toNanos(j), true);
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public void waitForUninterruptibly(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        } else if (this.lock.isHeldByCurrentThread()) {
            waitUninterruptibly(guard, true);
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public boolean waitForUninterruptibly(Guard guard, long j, TimeUnit timeUnit) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        } else if (this.lock.isHeldByCurrentThread()) {
            return waitUninterruptibly(guard, timeUnit.toNanos(j), true);
        } else {
            throw new IllegalMonitorStateException();
        }
    }
}
