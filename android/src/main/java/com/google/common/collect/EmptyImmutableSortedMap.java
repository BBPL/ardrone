package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

final class EmptyImmutableSortedMap<K, V> extends ImmutableSortedMap<K, V> {
    private final transient ImmutableSortedSet<K> keySet;

    EmptyImmutableSortedMap(Comparator<? super K> comparator) {
        this.keySet = ImmutableSortedSet.emptySet(comparator);
    }

    EmptyImmutableSortedMap(Comparator<? super K> comparator, ImmutableSortedMap<K, V> immutableSortedMap) {
        super(immutableSortedMap);
        this.keySet = ImmutableSortedSet.emptySet(comparator);
    }

    ImmutableSortedMap<K, V> createDescendingMap() {
        return new EmptyImmutableSortedMap(Ordering.from(comparator()).reverse(), this);
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        throw new AssertionError("should never be called");
    }

    public ImmutableSet<Entry<K, V>> entrySet() {
        return ImmutableSet.of();
    }

    public boolean equals(@Nullable Object obj) {
        return obj instanceof Map ? ((Map) obj).isEmpty() : false;
    }

    public V get(@Nullable Object obj) {
        return null;
    }

    public int hashCode() {
        return 0;
    }

    public ImmutableSortedMap<K, V> headMap(K k, boolean z) {
        Preconditions.checkNotNull(k);
        return this;
    }

    public boolean isEmpty() {
        return true;
    }

    boolean isPartialView() {
        return false;
    }

    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }

    public int size() {
        return 0;
    }

    public ImmutableSortedMap<K, V> tailMap(K k, boolean z) {
        Preconditions.checkNotNull(k);
        return this;
    }

    public String toString() {
        return "{}";
    }

    public ImmutableCollection<V> values() {
        return ImmutableList.of();
    }
}
