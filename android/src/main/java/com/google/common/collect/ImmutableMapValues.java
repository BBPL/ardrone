package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Map.Entry;

@GwtCompatible(emulated = true)
abstract class ImmutableMapValues<K, V> extends ImmutableCollection<V> {

    @GwtIncompatible("serialization")
    private static class SerializedForm<V> implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableMap<?, V> map;

        SerializedForm(ImmutableMap<?, V> immutableMap) {
            this.map = immutableMap;
        }

        Object readResolve() {
            return this.map.values();
        }
    }

    ImmutableMapValues() {
    }

    public boolean contains(Object obj) {
        return map().containsValue(obj);
    }

    ImmutableList<V> createAsList() {
        final ImmutableList asList = map().entrySet().asList();
        return new ImmutableAsList<V>() {
            ImmutableCollection<V> delegateCollection() {
                return ImmutableMapValues.this;
            }

            public V get(int i) {
                return ((Entry) asList.get(i)).getValue();
            }
        };
    }

    boolean isPartialView() {
        return true;
    }

    public UnmodifiableIterator<V> iterator() {
        return Maps.valueIterator(map().entrySet().iterator());
    }

    abstract ImmutableMap<K, V> map();

    public int size() {
        return map().size();
    }

    @GwtIncompatible("serialization")
    Object writeReplace() {
        return new SerializedForm(map());
    }
}
