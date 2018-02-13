package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
abstract class ImmutableMapKeySet<K, V> extends TransformedImmutableSet<Entry<K, V>, K> {

    @GwtIncompatible("serialization")
    private static class KeySetSerializedForm<K> implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableMap<K, ?> map;

        KeySetSerializedForm(ImmutableMap<K, ?> immutableMap) {
            this.map = immutableMap;
        }

        Object readResolve() {
            return this.map.keySet();
        }
    }

    ImmutableMapKeySet(ImmutableSet<Entry<K, V>> immutableSet) {
        super(immutableSet);
    }

    ImmutableMapKeySet(ImmutableSet<Entry<K, V>> immutableSet, int i) {
        super(immutableSet, i);
    }

    public boolean contains(@Nullable Object obj) {
        return map().containsKey(obj);
    }

    ImmutableList<K> createAsList() {
        final ImmutableList asList = map().entrySet().asList();
        return new ImmutableAsList<K>() {
            ImmutableCollection<K> delegateCollection() {
                return ImmutableMapKeySet.this;
            }

            public K get(int i) {
                return ((Entry) asList.get(i)).getKey();
            }
        };
    }

    boolean isPartialView() {
        return true;
    }

    abstract ImmutableMap<K, V> map();

    K transform(Entry<K, V> entry) {
        return entry.getKey();
    }

    @GwtIncompatible("serialization")
    Object writeReplace() {
        return new KeySetSerializedForm(map());
    }
}
