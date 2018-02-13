package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Iterator;
import java.util.Map.Entry;

@GwtCompatible(emulated = true, serializable = true)
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
    final transient ImmutableMap<K, V> delegate;
    final transient ImmutableBiMap<V, K> inverse;

    RegularImmutableBiMap(ImmutableMap<K, V> immutableMap) {
        this.delegate = immutableMap;
        Builder builder = ImmutableMap.builder();
        Iterator it = immutableMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            builder.put(entry.getValue(), entry.getKey());
        }
        this.inverse = new RegularImmutableBiMap(builder.build(), this);
    }

    RegularImmutableBiMap(ImmutableMap<K, V> immutableMap, ImmutableBiMap<V, K> immutableBiMap) {
        this.delegate = immutableMap;
        this.inverse = immutableBiMap;
    }

    ImmutableMap<K, V> delegate() {
        return this.delegate;
    }

    public ImmutableBiMap<V, K> inverse() {
        return this.inverse;
    }

    boolean isPartialView() {
        return this.delegate.isPartialView() || this.inverse.delegate().isPartialView();
    }
}
