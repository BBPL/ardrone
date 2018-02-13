package com.google.common.cache;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Random;
import sun.misc.Unsafe;

abstract class Striped64 extends Number {
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final Unsafe UNSAFE;
    private static final long baseOffset;
    private static final long busyOffset;
    static final ThreadHashCode threadHashCode = new ThreadHashCode();
    volatile transient long base;
    volatile transient int busy;
    volatile transient Cell[] cells;

    static final class C05381 implements PrivilegedExceptionAction<Unsafe> {
        C05381() {
        }

        public Unsafe run() throws Exception {
            Field declaredField = Unsafe.class.getDeclaredField("theUnsafe");
            declaredField.setAccessible(true);
            return (Unsafe) declaredField.get(null);
        }
    }

    static final class Cell {
        private static final Unsafe UNSAFE;
        private static final long valueOffset;
        volatile long p0;
        volatile long p1;
        volatile long p2;
        volatile long p3;
        volatile long p4;
        volatile long p5;
        volatile long p6;
        volatile long q0;
        volatile long q1;
        volatile long q2;
        volatile long q3;
        volatile long q4;
        volatile long q5;
        volatile long q6;
        volatile long value;

        static {
            try {
                UNSAFE = Striped64.getUnsafe();
                valueOffset = UNSAFE.objectFieldOffset(Cell.class.getDeclaredField("value"));
            } catch (Throwable e) {
                throw new Error(e);
            }
        }

        Cell(long j) {
            this.value = j;
        }

        final boolean cas(long j, long j2) {
            return UNSAFE.compareAndSwapLong(this, valueOffset, j, j2);
        }
    }

    static final class HashCode {
        static final Random rng = new Random();
        int code;

        HashCode() {
            int nextInt = rng.nextInt();
            if (nextInt == 0) {
                nextInt = 1;
            }
            this.code = nextInt;
        }
    }

    static final class ThreadHashCode extends ThreadLocal<HashCode> {
        ThreadHashCode() {
        }

        public HashCode initialValue() {
            return new HashCode();
        }
    }

    static {
        try {
            UNSAFE = getUnsafe();
            baseOffset = UNSAFE.objectFieldOffset(Striped64.class.getDeclaredField("base"));
            busyOffset = UNSAFE.objectFieldOffset(Striped64.class.getDeclaredField("busy"));
        } catch (Throwable e) {
            throw new Error(e);
        }
    }

    Striped64() {
    }

    private static Unsafe getUnsafe() {
        Unsafe unsafe;
        try {
            unsafe = Unsafe.getUnsafe();
        } catch (SecurityException e) {
            try {
                unsafe = (Unsafe) AccessController.doPrivileged(new C05381());
            } catch (PrivilegedActionException e2) {
                throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
            }
        }
        return unsafe;
    }

    final boolean casBase(long j, long j2) {
        return UNSAFE.compareAndSwapLong(this, baseOffset, j, j2);
    }

    final boolean casBusy() {
        return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
    }

    abstract long fn(long j, long j2);

    final void internalReset(long j) {
        Cell[] cellArr = this.cells;
        this.base = j;
        if (cellArr != null) {
            for (Cell cell : cellArr) {
                if (cell != null) {
                    cell.value = j;
                }
            }
        }
    }

    final void retryUpdate(long j, HashCode hashCode, boolean z) {
        int i = hashCode.code;
        Object obj = null;
        while (true) {
            Object obj2;
            Cell[] cellArr = this.cells;
            if (cellArr != null) {
                int length = cellArr.length;
                if (length > 0) {
                    Cell cell = cellArr[(length - 1) & i];
                    Cell[] cellArr2;
                    if (cell != null) {
                        if (z) {
                            long j2 = cell.value;
                            if (cell.cas(j2, fn(j2, j))) {
                                break;
                            } else if (length >= NCPU || this.cells != cellArr) {
                                obj = null;
                            } else if (obj == null) {
                                obj = 1;
                            } else if (this.busy == 0 && casBusy()) {
                                try {
                                    if (this.cells == cellArr) {
                                        cellArr2 = new Cell[(length << 1)];
                                        for (int i2 = 0; i2 < length; i2++) {
                                            cellArr2[i2] = cellArr[i2];
                                        }
                                        this.cells = cellArr2;
                                    }
                                    this.busy = 0;
                                    obj = null;
                                } catch (Throwable th) {
                                    this.busy = 0;
                                }
                            }
                        } else {
                            z = true;
                        }
                    } else {
                        if (this.busy == 0) {
                            Cell cell2 = new Cell(j);
                            if (this.busy == 0 && casBusy()) {
                                obj2 = null;
                                try {
                                    cellArr2 = this.cells;
                                    if (cellArr2 != null) {
                                        int length2 = cellArr2.length;
                                        if (length2 > 0) {
                                            length2 = (length2 - 1) & i;
                                            if (cellArr2[length2] == null) {
                                                cellArr2[length2] = cell2;
                                                obj2 = 1;
                                            }
                                        }
                                    }
                                    this.busy = 0;
                                    if (obj2 != null) {
                                        break;
                                    }
                                } catch (Throwable th2) {
                                    this.busy = 0;
                                }
                            }
                        }
                        obj = null;
                    }
                    i ^= i << 13;
                    i ^= i >>> 17;
                    i ^= i << 5;
                }
            }
            if (this.busy == 0 && this.cells == cellArr && casBusy()) {
                obj2 = null;
                try {
                    if (this.cells == cellArr) {
                        Cell[] cellArr3 = new Cell[2];
                        cellArr3[i & 1] = new Cell(j);
                        this.cells = cellArr3;
                        obj2 = 1;
                    }
                    this.busy = 0;
                    if (obj2 != null) {
                        break;
                    }
                } catch (Throwable th3) {
                    this.busy = 0;
                }
            } else {
                long j3 = this.base;
                if (casBase(j3, fn(j3, j))) {
                    break;
                }
            }
        }
        hashCode.code = i;
    }
}
