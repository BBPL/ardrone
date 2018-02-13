package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableBiMap<K, V> extends ImmutableMap<K, V> implements BiMap<K, V> {

    public static final class Builder<K, V> extends com.google.common.collect.ImmutableMap.Builder<K, V> {
        public ImmutableBiMap<K, V> build() {
            ImmutableMap build = super.build();
            return build.isEmpty() ? ImmutableBiMap.of() : new RegularImmutableBiMap(build);
        }

        public Builder<K, V> put(K k, V v) {
            super.put(k, v);
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            super.putAll(map);
            return this;
        }
    }

    private static class SerializedForm extends SerializedForm {
        private static final long serialVersionUID = 0;

        SerializedForm(ImmutableBiMap<?, ?> immutableBiMap) {
            super(immutableBiMap);
        }

        Object readResolve() {
            return createMap(new Builder());
        }
    }

    ImmutableBiMap() {
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableBiMap) {
            ImmutableBiMap<K, V> immutableBiMap = (ImmutableBiMap) map;
            if (!immutableBiMap.isPartialView()) {
                return immutableBiMap;
            }
        }
        return map.isEmpty() ? of() : new RegularImmutableBiMap(ImmutableMap.copyOf(map));
    }

    public static <K, V> ImmutableBiMap<K, V> of() {
        return EmptyImmutableBiMap.INSTANCE;
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k, V v) {
        return new RegularImmutableBiMap(ImmutableMap.of(k, v));
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k, V v, K k2, V v2) {
        return new RegularImmutableBiMap(ImmutableMap.of(k, v, k2, v2));
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3) {
        return new RegularImmutableBiMap(ImmutableMap.of(k, v, k2, v2, k3, v3));
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return new RegularImmutableBiMap(ImmutableMap.of(k, v, k2, v2, k3, v3, k4, v4));
    }

    public static <K, V> ImmutableBiMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return new RegularImmutableBiMap(ImmutableMap.of(k, v, k2, v2, k3, v3, k4, v4, k5, v5));
    }

    public boolean containsKey(@Nullable Object obj) {
        return delegate().containsKey(obj);
    }

    public boolean containsValue(@Nullable Object obj) {
        return inverse().containsKey(obj);
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return delegate().entrySet();
    }

    abstract ImmutableMap<K, V> delegate();

    public boolean equals(@Nullable Object obj) {
        return obj == this || delegate().equals(obj);
    }

    public V forcePut(K k, V v) {
        throw new UnsupportedOperationException();
    }

    public V get(@Nullable Object obj) {
        return delegate().get(obj);
    }

    public int hashCode() {
        return delegate().hashCode();
    }

    public abstract ImmutableBiMap<V, K> inverse();

    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    public ImmutableSet<K> keySet() {
        return delegate().keySet();
    }

    public int size() {
        return delegate().size();
    }

    public String toString() {
        return delegate().toString();
    }

    public ImmutableSet<V> values() {
        return inverse().keySet();
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
