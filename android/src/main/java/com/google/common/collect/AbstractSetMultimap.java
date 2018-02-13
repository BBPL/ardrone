package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractSetMultimap<K, V> extends AbstractMultimap<K, V> implements SetMultimap<K, V> {
    private static final long serialVersionUID = 7431625294878419160L;

    protected AbstractSetMultimap(Map<K, Collection<V>> map) {
        super(map);
    }

    public Map<K, Collection<V>> asMap() {
        return super.asMap();
    }

    abstract Set<V> createCollection();

    public Set<Entry<K, V>> entries() {
        return (Set) super.entries();
    }

    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    public Set<V> get(@Nullable K k) {
        return (Set) super.get(k);
    }

    public boolean put(K k, V v) {
        return super.put(k, v);
    }

    public Set<V> removeAll(@Nullable Object obj) {
        return (Set) super.removeAll(obj);
    }

    public Set<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
        return (Set) super.replaceValues(k, iterable);
    }
}
