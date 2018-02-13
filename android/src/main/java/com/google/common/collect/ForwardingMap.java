package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingMap<K, V> extends ForwardingObject implements Map<K, V> {

    @Beta
    protected abstract class StandardEntrySet extends EntrySet<K, V> {
        Map<K, V> map() {
            return ForwardingMap.this;
        }
    }

    @Beta
    protected class StandardKeySet extends KeySet<K, V> {
        Map<K, V> map() {
            return ForwardingMap.this;
        }
    }

    @Beta
    protected class StandardValues extends Values<K, V> {
        Map<K, V> map() {
            return ForwardingMap.this;
        }
    }

    protected ForwardingMap() {
    }

    public void clear() {
        delegate().clear();
    }

    public boolean containsKey(Object obj) {
        return delegate().containsKey(obj);
    }

    public boolean containsValue(Object obj) {
        return delegate().containsValue(obj);
    }

    protected abstract Map<K, V> delegate();

    public Set<Entry<K, V>> entrySet() {
        return delegate().entrySet();
    }

    public boolean equals(@Nullable Object obj) {
        return obj == this || delegate().equals(obj);
    }

    public V get(Object obj) {
        return delegate().get(obj);
    }

    public int hashCode() {
        return delegate().hashCode();
    }

    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    public Set<K> keySet() {
        return delegate().keySet();
    }

    public V put(K k, V v) {
        return delegate().put(k, v);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        delegate().putAll(map);
    }

    public V remove(Object obj) {
        return delegate().remove(obj);
    }

    public int size() {
        return delegate().size();
    }

    @Beta
    protected void standardClear() {
        Iterator it = entrySet().iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    @Beta
    protected boolean standardContainsKey(@Nullable Object obj) {
        return Maps.containsKeyImpl(this, obj);
    }

    @Beta
    protected boolean standardContainsValue(@Nullable Object obj) {
        return Maps.containsValueImpl(this, obj);
    }

    @Beta
    protected boolean standardEquals(@Nullable Object obj) {
        return Maps.equalsImpl(this, obj);
    }

    @Beta
    protected int standardHashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    @Beta
    protected boolean standardIsEmpty() {
        return !entrySet().iterator().hasNext();
    }

    @Beta
    protected void standardPutAll(Map<? extends K, ? extends V> map) {
        Maps.putAllImpl(this, map);
    }

    @Beta
    protected V standardRemove(@Nullable Object obj) {
        Iterator it = entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (Objects.equal(entry.getKey(), obj)) {
                V value = entry.getValue();
                it.remove();
                return value;
            }
        }
        return null;
    }

    @Beta
    protected String standardToString() {
        return Maps.toStringImpl(this);
    }

    public Collection<V> values() {
        return delegate().values();
    }
}
