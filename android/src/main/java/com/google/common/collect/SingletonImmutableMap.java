package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class SingletonImmutableMap<K, V> extends ImmutableMap<K, V> {
    final transient K singleKey;
    final transient V singleValue;

    SingletonImmutableMap(K k, V v) {
        this.singleKey = k;
        this.singleValue = v;
    }

    SingletonImmutableMap(Entry<K, V> entry) {
        this(entry.getKey(), entry.getValue());
    }

    public boolean containsKey(@Nullable Object obj) {
        return this.singleKey.equals(obj);
    }

    public boolean containsValue(@Nullable Object obj) {
        return this.singleValue.equals(obj);
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return ImmutableSet.of(Maps.immutableEntry(this.singleKey, this.singleValue));
    }

    ImmutableSet<K> createKeySet() {
        return ImmutableSet.of(this.singleKey);
    }

    ImmutableCollection<V> createValues() {
        return ImmutableList.of(this.singleValue);
    }

    public boolean equals(@Nullable Object obj) {
        if (obj != this) {
            if (obj instanceof Map) {
                Map map = (Map) obj;
                if (map.size() == 1) {
                    Entry entry = (Entry) map.entrySet().iterator().next();
                    if (!(this.singleKey.equals(entry.getKey()) && this.singleValue.equals(entry.getValue()))) {
                        return false;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public V get(@Nullable Object obj) {
        return this.singleKey.equals(obj) ? this.singleValue : null;
    }

    public int hashCode() {
        return this.singleKey.hashCode() ^ this.singleValue.hashCode();
    }

    public boolean isEmpty() {
        return false;
    }

    boolean isPartialView() {
        return false;
    }

    public int size() {
        return 1;
    }
}
