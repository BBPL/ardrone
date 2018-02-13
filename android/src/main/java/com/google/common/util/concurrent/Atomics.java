package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import javax.annotation.Nullable;

@Beta
public final class Atomics {
    private Atomics() {
    }

    public static <V> AtomicReference<V> newReference() {
        return new AtomicReference();
    }

    public static <V> AtomicReference<V> newReference(@Nullable V v) {
        return new AtomicReference(v);
    }

    public static <E> AtomicReferenceArray<E> newReferenceArray(int i) {
        return new AtomicReferenceArray(i);
    }

    public static <E> AtomicReferenceArray<E> newReferenceArray(E[] eArr) {
        return new AtomicReferenceArray(eArr);
    }
}
