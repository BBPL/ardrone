package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class EmptyImmutableMap extends ImmutableMap<Object, Object> {
    static final EmptyImmutableMap INSTANCE = new EmptyImmutableMap();
    private static final long serialVersionUID = 0;

    private EmptyImmutableMap() {
    }

    public boolean containsKey(@Nullable Object obj) {
        return false;
    }

    public boolean containsValue(@Nullable Object obj) {
        return false;
    }

    ImmutableSet<Entry<Object, Object>> createEntrySet() {
        throw new AssertionError("should never be called");
    }

    public ImmutableSet<Entry<Object, Object>> entrySet() {
        return ImmutableSet.of();
    }

    public boolean equals(@Nullable Object obj) {
        return obj instanceof Map ? ((Map) obj).isEmpty() : false;
    }

    public Object get(@Nullable Object obj) {
        return null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    boolean isPartialView() {
        return false;
    }

    public ImmutableSet<Object> keySet() {
        return ImmutableSet.of();
    }

    Object readResolve() {
        return INSTANCE;
    }

    public int size() {
        return 0;
    }

    public String toString() {
        return "{}";
    }

    public ImmutableCollection<Object> values() {
        return ImmutableCollection.EMPTY_IMMUTABLE_COLLECTION;
    }
}
