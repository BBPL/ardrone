package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
public final class RemovalNotification<K, V> implements Entry<K, V> {
    private static final long serialVersionUID = 0;
    private final RemovalCause cause;
    @Nullable
    private final K key;
    @Nullable
    private final V value;

    RemovalNotification(@Nullable K k, @Nullable V v, RemovalCause removalCause) {
        this.key = k;
        this.value = v;
        this.cause = (RemovalCause) Preconditions.checkNotNull(removalCause);
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Entry)) {
            return false;
        }
        Entry entry = (Entry) obj;
        return Objects.equal(getKey(), entry.getKey()) && Objects.equal(getValue(), entry.getValue());
    }

    public RemovalCause getCause() {
        return this.cause;
    }

    @Nullable
    public K getKey() {
        return this.key;
    }

    @Nullable
    public V getValue() {
        return this.value;
    }

    public int hashCode() {
        int i = 0;
        Object key = getKey();
        Object value = getValue();
        int hashCode = key == null ? 0 : key.hashCode();
        if (value != null) {
            i = value.hashCode();
        }
        return i ^ hashCode;
    }

    public final V setValue(V v) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return getKey() + "=" + getValue();
    }

    public boolean wasEvicted() {
        return this.cause.wasEvicted();
    }
}
