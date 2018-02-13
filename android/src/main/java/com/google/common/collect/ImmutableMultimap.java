package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class ImmutableMultimap<K, V> implements Multimap<K, V>, Serializable {
    private static final long serialVersionUID = 0;
    private transient ImmutableCollection<Entry<K, V>> entries;
    private transient ImmutableMultiset<K> keys;
    final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
    final transient int size;
    private transient ImmutableCollection<V> values;

    public static class Builder<K, V> {
        Multimap<K, V> builderMultimap = new BuilderMultimap();
        Comparator<? super K> keyComparator;
        Comparator<? super V> valueComparator;

        class C05821 implements Function<Entry<K, Collection<V>>, K> {
            C05821() {
            }

            public K apply(Entry<K, Collection<V>> entry) {
                return entry.getKey();
            }
        }

        public ImmutableMultimap<K, V> build() {
            if (this.valueComparator != null) {
                for (Collection collection : this.builderMultimap.asMap().values()) {
                    Collections.sort((List) collection, this.valueComparator);
                }
            }
            if (this.keyComparator != null) {
                Multimap builderMultimap = new BuilderMultimap();
                List<Entry> newArrayList = Lists.newArrayList(this.builderMultimap.asMap().entrySet());
                Collections.sort(newArrayList, Ordering.from(this.keyComparator).onResultOf(new C05821()));
                for (Entry entry : newArrayList) {
                    builderMultimap.putAll(entry.getKey(), (Iterable) entry.getValue());
                }
                this.builderMultimap = builderMultimap;
            }
            return ImmutableMultimap.copyOf(this.builderMultimap);
        }

        @Beta
        public Builder<K, V> orderKeysBy(Comparator<? super K> comparator) {
            this.keyComparator = (Comparator) Preconditions.checkNotNull(comparator);
            return this;
        }

        @Beta
        public Builder<K, V> orderValuesBy(Comparator<? super V> comparator) {
            this.valueComparator = (Comparator) Preconditions.checkNotNull(comparator);
            return this;
        }

        public Builder<K, V> put(K k, V v) {
            this.builderMultimap.put(Preconditions.checkNotNull(k), Preconditions.checkNotNull(v));
            return this;
        }

        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            this.builderMultimap.put(Preconditions.checkNotNull(entry.getKey()), Preconditions.checkNotNull(entry.getValue()));
            return this;
        }

        public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
            for (Entry entry : multimap.asMap().entrySet()) {
                putAll(entry.getKey(), (Iterable) entry.getValue());
            }
            return this;
        }

        public Builder<K, V> putAll(K k, Iterable<? extends V> iterable) {
            Collection collection = this.builderMultimap.get(Preconditions.checkNotNull(k));
            for (Object checkNotNull : iterable) {
                collection.add(Preconditions.checkNotNull(checkNotNull));
            }
            return this;
        }

        public Builder<K, V> putAll(K k, V... vArr) {
            return putAll((Object) k, Arrays.asList(vArr));
        }
    }

    private static class BuilderMultimap<K, V> extends AbstractMultimap<K, V> {
        private static final long serialVersionUID = 0;

        BuilderMultimap() {
            super(new LinkedHashMap());
        }

        Collection<V> createCollection() {
            return Lists.newArrayList();
        }
    }

    private static class EntryCollection<K, V> extends ImmutableCollection<Entry<K, V>> {
        private static final long serialVersionUID = 0;
        final ImmutableMultimap<K, V> multimap;

        EntryCollection(ImmutableMultimap<K, V> immutableMultimap) {
            this.multimap = immutableMultimap;
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            return this.multimap.containsEntry(entry.getKey(), entry.getValue());
        }

        boolean isPartialView() {
            return this.multimap.isPartialView();
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            final Iterator it = this.multimap.map.entrySet().iterator();
            return new UnmodifiableIterator<Entry<K, V>>() {
                K key;
                Iterator<V> valueIterator;

                public boolean hasNext() {
                    return (this.key != null && this.valueIterator.hasNext()) || it.hasNext();
                }

                public Entry<K, V> next() {
                    if (this.key == null || !this.valueIterator.hasNext()) {
                        Entry entry = (Entry) it.next();
                        this.key = entry.getKey();
                        this.valueIterator = ((ImmutableCollection) entry.getValue()).iterator();
                    }
                    return Maps.immutableEntry(this.key, this.valueIterator.next());
                }
            };
        }

        public int size() {
            return this.multimap.size();
        }
    }

    @GwtIncompatible("java serialization is not supported")
    static class FieldSettersHolder {
        static final FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
        static final FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");

        FieldSettersHolder() {
        }
    }

    class Keys extends ImmutableMultiset<K> {

        private class KeysEntrySet extends EntrySet {
            private KeysEntrySet() {
                super();
            }

            ImmutableList<Multiset.Entry<K>> createAsList() {
                final ImmutableList asList = ImmutableMultimap.this.map.entrySet().asList();
                return new ImmutableAsList<Multiset.Entry<K>>() {
                    ImmutableCollection<Multiset.Entry<K>> delegateCollection() {
                        return KeysEntrySet.this;
                    }

                    public Multiset.Entry<K> get(int i) {
                        Entry entry = (Entry) asList.get(i);
                        return Multisets.immutableEntry(entry.getKey(), ((Collection) entry.getValue()).size());
                    }
                };
            }

            public UnmodifiableIterator<Multiset.Entry<K>> iterator() {
                return asList().iterator();
            }

            public int size() {
                return ImmutableMultimap.this.keySet().size();
            }
        }

        Keys() {
        }

        public boolean contains(@Nullable Object obj) {
            return ImmutableMultimap.this.containsKey(obj);
        }

        public int count(@Nullable Object obj) {
            Collection collection = (Collection) ImmutableMultimap.this.map.get(obj);
            return collection == null ? 0 : collection.size();
        }

        ImmutableSet<Multiset.Entry<K>> createEntrySet() {
            return new KeysEntrySet();
        }

        public Set<K> elementSet() {
            return ImmutableMultimap.this.keySet();
        }

        boolean isPartialView() {
            return true;
        }

        public int size() {
            return ImmutableMultimap.this.size();
        }
    }

    private static class Values<V> extends ImmutableCollection<V> {
        private static final long serialVersionUID = 0;
        final ImmutableMultimap<?, V> multimap;

        Values(ImmutableMultimap<?, V> immutableMultimap) {
            this.multimap = immutableMultimap;
        }

        boolean isPartialView() {
            return true;
        }

        public UnmodifiableIterator<V> iterator() {
            return Maps.valueIterator(this.multimap.entries().iterator());
        }

        public int size() {
            return this.multimap.size();
        }
    }

    ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> immutableMap, int i) {
        this.map = immutableMap;
        this.size = i;
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder();
    }

    public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
        if (multimap instanceof ImmutableMultimap) {
            ImmutableMultimap<K, V> immutableMultimap = (ImmutableMultimap) multimap;
            if (!immutableMultimap.isPartialView()) {
                return immutableMultimap;
            }
        }
        return ImmutableListMultimap.copyOf(multimap);
    }

    private ImmutableMultiset<K> createKeys() {
        return new Keys();
    }

    public static <K, V> ImmutableMultimap<K, V> of() {
        return ImmutableListMultimap.of();
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v) {
        return ImmutableListMultimap.of(k, v);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v, K k2, V v2) {
        return ImmutableListMultimap.of(k, v, k2, v2);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v, K k2, V v2, K k3, V v3) {
        return ImmutableListMultimap.of(k, v, k2, v2, k3, v3);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ImmutableListMultimap.of(k, v, k2, v2, k3, v3, k4, v4);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return ImmutableListMultimap.of(k, v, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    public ImmutableMap<K, Collection<V>> asMap() {
        return this.map;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsEntry(@Nullable Object obj, @Nullable Object obj2) {
        Collection collection = (Collection) this.map.get(obj);
        return collection != null && collection.contains(obj2);
    }

    public boolean containsKey(@Nullable Object obj) {
        return this.map.containsKey(obj);
    }

    public boolean containsValue(@Nullable Object obj) {
        Iterator it = this.map.values().iterator();
        while (it.hasNext()) {
            if (((ImmutableCollection) it.next()).contains(obj)) {
                return true;
            }
        }
        return false;
    }

    public ImmutableCollection<Entry<K, V>> entries() {
        ImmutableCollection<Entry<K, V>> immutableCollection = this.entries;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        immutableCollection = new EntryCollection(this);
        this.entries = immutableCollection;
        return immutableCollection;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Multimap)) {
            return false;
        }
        return this.map.equals(((Multimap) obj).asMap());
    }

    public abstract ImmutableCollection<V> get(K k);

    public int hashCode() {
        return this.map.hashCode();
    }

    @Beta
    public abstract ImmutableMultimap<V, K> inverse();

    public boolean isEmpty() {
        return this.size == 0;
    }

    boolean isPartialView() {
        return this.map.isPartialView();
    }

    public ImmutableSet<K> keySet() {
        return this.map.keySet();
    }

    public ImmutableMultiset<K> keys() {
        ImmutableMultiset<K> immutableMultiset = this.keys;
        if (immutableMultiset != null) {
            return immutableMultiset;
        }
        immutableMultiset = createKeys();
        this.keys = immutableMultiset;
        return immutableMultiset;
    }

    public boolean put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        throw new UnsupportedOperationException();
    }

    public boolean putAll(K k, Iterable<? extends V> iterable) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object obj, Object obj2) {
        throw new UnsupportedOperationException();
    }

    public ImmutableCollection<V> removeAll(Object obj) {
        throw new UnsupportedOperationException();
    }

    public ImmutableCollection<V> replaceValues(K k, Iterable<? extends V> iterable) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return this.size;
    }

    public String toString() {
        return this.map.toString();
    }

    public ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.values;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        immutableCollection = new Values(this);
        this.values = immutableCollection;
        return immutableCollection;
    }
}
