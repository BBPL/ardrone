package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingSortedMap<K, V> extends ForwardingMap<K, V> implements SortedMap<K, V> {
    protected ForwardingSortedMap() {
    }

    private int unsafeCompare(Object obj, Object obj2) {
        Comparator comparator = comparator();
        return comparator == null ? ((Comparable) obj).compareTo(obj2) : comparator.compare(obj, obj2);
    }

    public Comparator<? super K> comparator() {
        return delegate().comparator();
    }

    protected abstract SortedMap<K, V> delegate();

    public K firstKey() {
        return delegate().firstKey();
    }

    public SortedMap<K, V> headMap(K k) {
        return delegate().headMap(k);
    }

    public K lastKey() {
        return delegate().lastKey();
    }

    @Beta
    protected boolean standardContainsKey(@Nullable Object obj) {
        try {
            return unsafeCompare(tailMap(obj).firstKey(), obj) == 0;
        } catch (ClassCastException e) {
            return false;
        } catch (NoSuchElementException e2) {
            return false;
        } catch (NullPointerException e3) {
            return false;
        }
    }

    @Beta
    protected V standardRemove(@Nullable Object obj) {
        try {
            Iterator it = tailMap(obj).entrySet().iterator();
            if (it.hasNext()) {
                Entry entry = (Entry) it.next();
                if (unsafeCompare(entry.getKey(), obj) == 0) {
                    V value = entry.getValue();
                    it.remove();
                    return value;
                }
            }
            return null;
        } catch (ClassCastException e) {
            return null;
        } catch (NullPointerException e2) {
            return null;
        }
    }

    @Beta
    protected SortedMap<K, V> standardSubMap(K k, K k2) {
        return tailMap(k).headMap(k2);
    }

    public SortedMap<K, V> subMap(K k, K k2) {
        return delegate().subMap(k, k2);
    }

    public SortedMap<K, V> tailMap(K k) {
        return delegate().tailMap(k);
    }
}
