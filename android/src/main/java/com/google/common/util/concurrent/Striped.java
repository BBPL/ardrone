package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Beta
public abstract class Striped<L> {
    private static final int ALL_SET = -1;
    private static final Supplier<ReadWriteLock> READ_WRITE_LOCK_SUPPLIER = new C08845();

    static final class C08801 implements Supplier<Lock> {
        C08801() {
        }

        public Lock get() {
            return new PaddedLock();
        }
    }

    static final class C08812 implements Supplier<Lock> {
        C08812() {
        }

        public Lock get() {
            return new ReentrantLock(false);
        }
    }

    static final class C08845 implements Supplier<ReadWriteLock> {
        C08845() {
        }

        public ReadWriteLock get() {
            return new ReentrantReadWriteLock();
        }
    }

    private static abstract class PowerOfTwoStriped<L> extends Striped<L> {
        final int mask;

        PowerOfTwoStriped(int i) {
            super();
            Preconditions.checkArgument(i > 0, "Stripes must be positive");
            this.mask = i > Ints.MAX_POWER_OF_TWO ? -1 : Striped.ceilToPowerOfTwo(i) - 1;
        }

        public final L get(Object obj) {
            return getAt(indexFor(obj));
        }

        final int indexFor(Object obj) {
            return Striped.smear(obj.hashCode()) & this.mask;
        }
    }

    private static class CompactStriped<L> extends PowerOfTwoStriped<L> {
        private final Object[] array;

        private CompactStriped(int i, Supplier<L> supplier) {
            int i2 = 0;
            super(i);
            Preconditions.checkArgument(i <= Ints.MAX_POWER_OF_TWO, "Stripes must be <= 2^30)");
            this.array = new Object[(this.mask + 1)];
            while (i2 < this.array.length) {
                this.array[i2] = supplier.get();
                i2++;
            }
        }

        public L getAt(int i) {
            return this.array[i];
        }

        public int size() {
            return this.array.length;
        }
    }

    private static class LazyStriped<L> extends PowerOfTwoStriped<L> {
        final ConcurrentMap<Integer, L> cache;
        final int size;

        LazyStriped(int i, Supplier<L> supplier) {
            super(i);
            this.size = this.mask == -1 ? Integer.MAX_VALUE : this.mask + 1;
            this.cache = new MapMaker().weakValues().makeComputingMap(Functions.forSupplier(supplier));
        }

        public L getAt(int i) {
            Preconditions.checkElementIndex(i, size());
            return this.cache.get(Integer.valueOf(i));
        }

        public int size() {
            return this.size;
        }
    }

    private static class PaddedLock extends ReentrantLock {
        long q1;
        long q2;
        long q3;

        PaddedLock() {
            super(false);
        }
    }

    private static class PaddedSemaphore extends Semaphore {
        long q1;
        long q2;
        long q3;

        PaddedSemaphore(int i) {
            super(i, false);
        }
    }

    private Striped() {
    }

    private static int ceilToPowerOfTwo(int i) {
        return 1 << IntMath.log2(i, RoundingMode.CEILING);
    }

    public static Striped<Lock> lazyWeakLock(int i) {
        return new LazyStriped(i, new C08812());
    }

    public static Striped<ReadWriteLock> lazyWeakReadWriteLock(int i) {
        return new LazyStriped(i, READ_WRITE_LOCK_SUPPLIER);
    }

    public static Striped<Semaphore> lazyWeakSemaphore(int i, final int i2) {
        return new LazyStriped(i, new Supplier<Semaphore>() {
            public Semaphore get() {
                return new Semaphore(i2, false);
            }
        });
    }

    public static Striped<Lock> lock(int i) {
        return new CompactStriped(i, new C08801());
    }

    public static Striped<ReadWriteLock> readWriteLock(int i) {
        return new CompactStriped(i, READ_WRITE_LOCK_SUPPLIER);
    }

    public static Striped<Semaphore> semaphore(int i, final int i2) {
        return new CompactStriped(i, new Supplier<Semaphore>() {
            public Semaphore get() {
                return new PaddedSemaphore(i2);
            }
        });
    }

    private static int smear(int i) {
        int i2 = ((i >>> 20) ^ (i >>> 12)) ^ i;
        return (i2 >>> 4) ^ ((i2 >>> 7) ^ i2);
    }

    public Iterable<L> bulkGet(Iterable<?> iterable) {
        int i = 0;
        Object[] toArray = Iterables.toArray(iterable, Object.class);
        int[] iArr = new int[toArray.length];
        for (int i2 = 0; i2 < toArray.length; i2++) {
            iArr[i2] = indexFor(toArray[i2]);
        }
        Arrays.sort(iArr);
        while (i < toArray.length) {
            toArray[i] = getAt(iArr[i]);
            i++;
        }
        return Collections.unmodifiableList(Arrays.asList(toArray));
    }

    public abstract L get(Object obj);

    public abstract L getAt(int i);

    abstract int indexFor(Object obj);

    public abstract int size();
}
