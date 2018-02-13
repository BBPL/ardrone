package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
    private transient ImmutableSet<Entry<K, V>> entrySet;
    private transient ImmutableSet<K> keySet;
    private transient ImmutableCollection<V> values;

    public static class Builder<K, V> {
        final ArrayList<Entry<K, V>> entries = Lists.newArrayList();

        private static <K, V> ImmutableMap<K, V> fromEntryList(List<Entry<K, V>> list) {
            switch (list.size()) {
                case 0:
                    return ImmutableMap.of();
                case 1:
                    return new SingletonImmutableMap((Entry) Iterables.getOnlyElement(list));
                default:
                    return new RegularImmutableMap((Entry[]) list.toArray(new Entry[list.size()]));
            }
        }

        public ImmutableMap<K, V> build() {
            return fromEntryList(this.entries);
        }

        public Builder<K, V> put(K k, V v) {
            this.entries.add(ImmutableMap.entryOf(k, v));
            return this;
        }

        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (entry instanceof ImmutableEntry) {
                Preconditions.checkNotNull(key);
                Preconditions.checkNotNull(value);
                this.entries.add(entry);
            } else {
                this.entries.add(ImmutableMap.entryOf(key, value));
            }
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            this.entries.ensureCapacity(this.entries.size() + map.size());
            for (Entry entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
            return this;
        }
    }

    static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private final Object[] keys;
        private final Object[] values;

        SerializedForm(ImmutableMap<?, ?> immutableMap) {
            this.keys = new Object[immutableMap.size()];
            this.values = new Object[immutableMap.size()];
            Iterator it = immutableMap.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                this.keys[i] = entry.getKey();
                this.values[i] = entry.getValue();
                i++;
            }
        }

        Object createMap(Builder<Object, Object> builder) {
            for (int i = 0; i < this.keys.length; i++) {
                builder.put(this.keys[i], this.values[i]);
            }
            return builder.build();
        }

        Object readResolve() {
            return createMap(new Builder());
        }
    }

    class C05782 extends ImmutableMapValues<K, V> {
        C05782() {
        }

        ImmutableMap<K, V> map() {
            return ImmutableMap.this;
        }
    }

    ImmutableMap() {
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        int i = 0;
        if ((map instanceof ImmutableMap) && !(map instanceof ImmutableSortedMap)) {
            ImmutableMap<K, V> immutableMap = (ImmutableMap) map;
            if (!immutableMap.isPartialView()) {
                return immutableMap;
            }
        }
        Entry[] entryArr = (Entry[]) map.entrySet().toArray(new Entry[0]);
        switch (entryArr.length) {
            case 0:
                return of();
            case 1:
                return new SingletonImmutableMap(entryOf(entryArr[0].getKey(), entryArr[0].getValue()));
        }
        while (i < entryArr.length) {
            entryArr[i] = entryOf(entryArr[i].getKey(), entryArr[i].getValue());
            i++;
        }
        return new RegularImmutableMap(entryArr);
    }

    static <K, V> Entry<K, V> entryOf(K k, V v) {
        Preconditions.checkNotNull(k, "null key in entry: null=%s", v);
        Preconditions.checkNotNull(v, "null value in entry: %s=null", k);
        return Maps.immutableEntry(k, v);
    }

    public static <K, V> ImmutableMap<K, V> of() {
        return EmptyImmutableMap.INSTANCE;
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v) {
        return new SingletonImmutableMap(Preconditions.checkNotNull(k), Preconditions.checkNotNull(v));
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2) {
        return new RegularImmutableMap(entryOf(k, v), entryOf(k2, v2));
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3) {
        return new RegularImmutableMap(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3));
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return new RegularImmutableMap(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4));
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return new RegularImmutableMap(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5));
    }

    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(@Nullable Object obj) {
        return get(obj) != null;
    }

    public boolean containsValue(@Nullable Object obj) {
        return obj != null && Maps.containsValueImpl(this, obj);
    }

    abstract ImmutableSet<Entry<K, V>> createEntrySet();

    ImmutableSet<K> createKeySet() {
        return new ImmutableMapKeySet<K, V>(entrySet()) {
            ImmutableMap<K, V> map() {
                return ImmutableMap.this;
            }
        };
    }

    ImmutableCollection<V> createValues() {
        return new C05782();
    }

    public ImmutableSet<Entry<K, V>> entrySet() {
        ImmutableSet<Entry<K, V>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = createEntrySet();
        this.entrySet = immutableSet;
        return immutableSet;
    }

    public boolean equals(@Nullable Object obj) {
        return Maps.equalsImpl(this, obj);
    }

    public abstract V get(@Nullable Object obj);

    public int hashCode() {
        return entrySet().hashCode();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    abstract boolean isPartialView();

    public ImmutableSet<K> keySet() {
        ImmutableSet<K> immutableSet = this.keySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        immutableSet = createKeySet();
        this.keySet = immutableSet;
        return immutableSet;
    }

    public final V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    public final V remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return Maps.toStringImpl(this);
    }

    public ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.values;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        immutableCollection = createValues();
        this.values = immutableCollection;
        return immutableCollection;
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
