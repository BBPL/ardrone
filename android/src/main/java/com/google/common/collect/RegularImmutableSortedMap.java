package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Map.Entry;
import javax.annotation.Nullable;

final class RegularImmutableSortedMap<K, V> extends ImmutableSortedMap<K, V> {
    private final transient RegularImmutableSortedSet<K> keySet;
    private final transient ImmutableList<V> valueList;

    private class EntrySet extends ImmutableMapEntrySet<K, V> {

        class C07281 extends ImmutableAsList<Entry<K, V>> {
            private final ImmutableList<K> keyList = RegularImmutableSortedMap.this.keySet().asList();
            private final ImmutableList<V> valueList = RegularImmutableSortedMap.this.values().asList();

            C07281() {
            }

            ImmutableCollection<Entry<K, V>> delegateCollection() {
                return EntrySet.this;
            }

            public Entry<K, V> get(int i) {
                return Maps.immutableEntry(this.keyList.get(i), this.valueList.get(i));
            }
        }

        private EntrySet() {
        }

        ImmutableList<Entry<K, V>> createAsList() {
            return new C07281();
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return asList().iterator();
        }

        ImmutableMap<K, V> map() {
            return RegularImmutableSortedMap.this;
        }
    }

    RegularImmutableSortedMap(RegularImmutableSortedSet<K> regularImmutableSortedSet, ImmutableList<V> immutableList) {
        this.keySet = regularImmutableSortedSet;
        this.valueList = immutableList;
    }

    RegularImmutableSortedMap(RegularImmutableSortedSet<K> regularImmutableSortedSet, ImmutableList<V> immutableList, ImmutableSortedMap<K, V> immutableSortedMap) {
        super(immutableSortedMap);
        this.keySet = regularImmutableSortedSet;
        this.valueList = immutableList;
    }

    private ImmutableSortedMap<K, V> getSubMap(int i, int i2) {
        return (i == 0 && i2 == size()) ? this : i == i2 ? ImmutableSortedMap.emptyMap(comparator()) : ImmutableSortedMap.from(this.keySet.getSubSet(i, i2), this.valueList.subList(i, i2));
    }

    ImmutableSortedMap<K, V> createDescendingMap() {
        return new RegularImmutableSortedMap((RegularImmutableSortedSet) this.keySet.descendingSet(), this.valueList.reverse(), this);
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return new EntrySet();
    }

    public V get(@Nullable Object obj) {
        int indexOf = this.keySet.indexOf(obj);
        return indexOf == -1 ? null : this.valueList.get(indexOf);
    }

    public ImmutableSortedMap<K, V> headMap(K k, boolean z) {
        return getSubMap(0, this.keySet.headIndex(Preconditions.checkNotNull(k), z));
    }

    public ImmutableSortedSet<K> keySet() {
        return this.keySet;
    }

    public ImmutableSortedMap<K, V> tailMap(K k, boolean z) {
        return getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(k), z), size());
    }

    public ImmutableCollection<V> values() {
        return this.valueList;
    }
}
