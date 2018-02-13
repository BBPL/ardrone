package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
abstract class AbstractBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
    @GwtIncompatible("Not needed in emulated source.")
    private static final long serialVersionUID = 0;
    private transient Map<K, V> delegate;
    private transient Set<Entry<K, V>> entrySet;
    transient AbstractBiMap<V, K> inverse;
    private transient Set<K> keySet;
    private transient Set<V> valueSet;

    private class EntrySet extends ForwardingSet<Entry<K, V>> {
        final Set<Entry<K, V>> esDelegate;

        private EntrySet() {
            this.esDelegate = AbstractBiMap.this.delegate.entrySet();
        }

        public void clear() {
            AbstractBiMap.this.clear();
        }

        public boolean contains(Object obj) {
            return Maps.containsEntryImpl(delegate(), obj);
        }

        public boolean containsAll(Collection<?> collection) {
            return standardContainsAll(collection);
        }

        protected Set<Entry<K, V>> delegate() {
            return this.esDelegate;
        }

        public Iterator<Entry<K, V>> iterator() {
            final Iterator it = this.esDelegate.iterator();
            return new Iterator<Entry<K, V>>() {
                Entry<K, V> entry;

                public boolean hasNext() {
                    return it.hasNext();
                }

                public Entry<K, V> next() {
                    this.entry = (Entry) it.next();
                    final Entry entry = this.entry;
                    return new ForwardingMapEntry<K, V>() {
                        protected Entry<K, V> delegate() {
                            return entry;
                        }

                        public V setValue(V v) {
                            Preconditions.checkState(EntrySet.this.contains(this), "entry no longer in map");
                            if (Objects.equal(v, getValue())) {
                                return v;
                            }
                            Preconditions.checkArgument(!AbstractBiMap.this.containsValue(v), "value already present: %s", v);
                            V value = entry.setValue(v);
                            Preconditions.checkState(Objects.equal(v, AbstractBiMap.this.get(getKey())), "entry no longer in map");
                            AbstractBiMap.this.updateInverseMap(getKey(), true, value, v);
                            return value;
                        }
                    };
                }

                public void remove() {
                    Preconditions.checkState(this.entry != null);
                    Object value = this.entry.getValue();
                    it.remove();
                    AbstractBiMap.this.removeFromInverseMap(value);
                }
            };
        }

        public boolean remove(Object obj) {
            if (!this.esDelegate.contains(obj)) {
                return false;
            }
            Entry entry = (Entry) obj;
            AbstractBiMap.this.inverse.delegate.remove(entry.getValue());
            this.esDelegate.remove(entry);
            return true;
        }

        public boolean removeAll(Collection<?> collection) {
            return standardRemoveAll(collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return standardRetainAll(collection);
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return standardToArray(tArr);
        }
    }

    private static class Inverse<K, V> extends AbstractBiMap<K, V> {
        @GwtIncompatible("Not needed in emulated source.")
        private static final long serialVersionUID = 0;

        private Inverse(Map<K, V> map, AbstractBiMap<V, K> abstractBiMap) {
            super(map, abstractBiMap);
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            setInverse((AbstractBiMap) objectInputStream.readObject());
        }

        @GwtIncompatible("java.io.ObjectOuputStream")
        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeObject(inverse());
        }

        K checkKey(K k) {
            return this.inverse.checkValue(k);
        }

        V checkValue(V v) {
            return this.inverse.checkKey(v);
        }

        protected /* bridge */ /* synthetic */ Object delegate() {
            return super.delegate();
        }

        @GwtIncompatible("Not needed in the emulated source.")
        Object readResolve() {
            return inverse().inverse();
        }

        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }
    }

    private class KeySet extends ForwardingSet<K> {
        private KeySet() {
        }

        public void clear() {
            AbstractBiMap.this.clear();
        }

        protected Set<K> delegate() {
            return AbstractBiMap.this.delegate.keySet();
        }

        public Iterator<K> iterator() {
            return Maps.keyIterator(AbstractBiMap.this.entrySet().iterator());
        }

        public boolean remove(Object obj) {
            if (!contains(obj)) {
                return false;
            }
            AbstractBiMap.this.removeFromBothMaps(obj);
            return true;
        }

        public boolean removeAll(Collection<?> collection) {
            return standardRemoveAll(collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return standardRetainAll(collection);
        }
    }

    private class ValueSet extends ForwardingSet<V> {
        final Set<V> valuesDelegate;

        private ValueSet() {
            this.valuesDelegate = AbstractBiMap.this.inverse.keySet();
        }

        protected Set<V> delegate() {
            return this.valuesDelegate;
        }

        public Iterator<V> iterator() {
            return Maps.valueIterator(AbstractBiMap.this.entrySet().iterator());
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return standardToArray(tArr);
        }

        public String toString() {
            return standardToString();
        }
    }

    private AbstractBiMap(Map<K, V> map, AbstractBiMap<V, K> abstractBiMap) {
        this.delegate = map;
        this.inverse = abstractBiMap;
    }

    AbstractBiMap(Map<K, V> map, Map<V, K> map2) {
        setDelegates(map, map2);
    }

    private V putInBothMaps(@Nullable K k, @Nullable V v, boolean z) {
        checkKey(k);
        checkValue(v);
        boolean containsKey = containsKey(k);
        if (containsKey && Objects.equal(v, get(k))) {
            return v;
        }
        if (z) {
            inverse().remove(v);
        } else {
            Preconditions.checkArgument(!containsValue(v), "value already present: %s", v);
        }
        V put = this.delegate.put(k, v);
        updateInverseMap(k, containsKey, put, v);
        return put;
    }

    private V removeFromBothMaps(Object obj) {
        V remove = this.delegate.remove(obj);
        removeFromInverseMap(remove);
        return remove;
    }

    private void removeFromInverseMap(V v) {
        this.inverse.delegate.remove(v);
    }

    private void updateInverseMap(K k, boolean z, V v, V v2) {
        if (z) {
            removeFromInverseMap(v);
        }
        this.inverse.delegate.put(v2, k);
    }

    K checkKey(@Nullable K k) {
        return k;
    }

    V checkValue(@Nullable V v) {
        return v;
    }

    public void clear() {
        this.delegate.clear();
        this.inverse.delegate.clear();
    }

    public boolean containsValue(Object obj) {
        return this.inverse.containsKey(obj);
    }

    protected Map<K, V> delegate() {
        return this.delegate;
    }

    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = this.entrySet;
        if (set != null) {
            return set;
        }
        set = new EntrySet();
        this.entrySet = set;
        return set;
    }

    public V forcePut(K k, V v) {
        return putInBothMaps(k, v, true);
    }

    public BiMap<V, K> inverse() {
        return this.inverse;
    }

    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        set = new KeySet();
        this.keySet = set;
        return set;
    }

    public V put(K k, V v) {
        return putInBothMaps(k, v, false);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public V remove(Object obj) {
        return containsKey(obj) ? removeFromBothMaps(obj) : null;
    }

    void setDelegates(Map<K, V> map, Map<V, K> map2) {
        boolean z = true;
        Preconditions.checkState(this.delegate == null);
        Preconditions.checkState(this.inverse == null);
        Preconditions.checkArgument(map.isEmpty());
        Preconditions.checkArgument(map2.isEmpty());
        if (map == map2) {
            z = false;
        }
        Preconditions.checkArgument(z);
        this.delegate = map;
        this.inverse = new Inverse(map2, this);
    }

    void setInverse(AbstractBiMap<V, K> abstractBiMap) {
        this.inverse = abstractBiMap;
    }

    public Set<V> values() {
        Set<V> set = this.valueSet;
        if (set != null) {
            return set;
        }
        set = new ValueSet();
        this.valueSet = set;
        return set;
    }
}
