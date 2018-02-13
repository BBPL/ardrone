package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements SortedMap<K, V> {
    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new EmptyImmutableSortedMap(NATURAL_ORDER);
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
    private static final long serialVersionUID = 0;
    private transient ImmutableSortedMap<K, V> descendingMap;

    public static class Builder<K, V> extends com.google.common.collect.ImmutableMap.Builder<K, V> {
        private final Comparator<? super K> comparator;

        public Builder(Comparator<? super K> comparator) {
            this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        }

        public ImmutableSortedMap<K, V> build() {
            ImmutableSortedMap.sortEntries(this.entries, this.comparator);
            ImmutableSortedMap.validateEntries(this.entries, this.comparator);
            return ImmutableSortedMap.fromSortedEntries(this.comparator, this.entries);
        }

        public Builder<K, V> put(K k, V v) {
            this.entries.add(ImmutableMap.entryOf(k, v));
            return this;
        }

        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            for (Entry entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
            return this;
        }
    }

    private static class SerializedForm extends SerializedForm {
        private static final long serialVersionUID = 0;
        private final Comparator<Object> comparator;

        SerializedForm(ImmutableSortedMap<?, ?> immutableSortedMap) {
            super(immutableSortedMap);
            this.comparator = immutableSortedMap.comparator();
        }

        Object readResolve() {
            return createMap(new Builder(this.comparator));
        }
    }

    ImmutableSortedMap() {
    }

    ImmutableSortedMap(ImmutableSortedMap<K, V> immutableSortedMap) {
        this.descendingMap = immutableSortedMap;
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        return copyOfInternal(map, Ordering.natural());
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        return copyOfInternal(map, (Comparator) Preconditions.checkNotNull(comparator));
    }

    private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        int i;
        int i2 = 0;
        if (map instanceof SortedMap) {
            Comparator comparator2 = ((SortedMap) map).comparator();
            if (comparator2 == null) {
                i = comparator == NATURAL_ORDER ? 1 : 0;
            } else {
                boolean equals = comparator.equals(comparator2);
            }
        } else {
            i = 0;
        }
        if (i != 0 && (map instanceof ImmutableSortedMap)) {
            ImmutableSortedMap<K, V> immutableSortedMap = (ImmutableSortedMap) map;
            if (!immutableSortedMap.isPartialView()) {
                return immutableSortedMap;
            }
        }
        Entry[] entryArr = (Entry[]) map.entrySet().toArray(new Entry[0]);
        while (i2 < entryArr.length) {
            Entry entry = entryArr[i2];
            entryArr[i2] = ImmutableMap.entryOf(entry.getKey(), entry.getValue());
            i2++;
        }
        Collection asList = Arrays.asList(entryArr);
        if (i == 0) {
            sortEntries(asList, comparator);
            validateEntries(asList, comparator);
        }
        return fromSortedEntries(comparator, asList);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> sortedMap) {
        Comparator comparator = sortedMap.comparator();
        if (comparator == null) {
            comparator = NATURAL_ORDER;
        }
        return copyOfInternal(sortedMap, comparator);
    }

    static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
        return Ordering.natural().equals(comparator) ? of() : new EmptyImmutableSortedMap(comparator);
    }

    static <K, V> ImmutableSortedMap<K, V> from(ImmutableSortedSet<K> immutableSortedSet, ImmutableList<V> immutableList) {
        return immutableSortedSet.isEmpty() ? emptyMap(immutableSortedSet.comparator()) : new RegularImmutableSortedMap((RegularImmutableSortedSet) immutableSortedSet, immutableList);
    }

    static <K, V> ImmutableSortedMap<K, V> fromSortedEntries(Comparator<? super K> comparator, Collection<? extends Entry<? extends K, ? extends V>> collection) {
        if (collection.isEmpty()) {
            return emptyMap(comparator);
        }
        com.google.common.collect.ImmutableList.Builder builder = ImmutableList.builder();
        com.google.common.collect.ImmutableList.Builder builder2 = ImmutableList.builder();
        for (Entry entry : collection) {
            builder.add(entry.getKey());
            builder2.add(entry.getValue());
        }
        return new RegularImmutableSortedMap(new RegularImmutableSortedSet(builder.build(), comparator), builder2.build());
    }

    public static <K extends Comparable<K>, V> Builder<K, V> naturalOrder() {
        return new Builder(Ordering.natural());
    }

    public static <K, V> ImmutableSortedMap<K, V> of() {
        return NATURAL_EMPTY_MAP;
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k, V v) {
        return from(ImmutableSortedSet.of(k), ImmutableList.of(v));
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k, V v, K k2, V v2) {
        return new Builder(Ordering.natural()).put((Object) k, (Object) v).put((Object) k2, (Object) v2).build();
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3) {
        return new Builder(Ordering.natural()).put((Object) k, (Object) v).put((Object) k2, (Object) v2).put((Object) k3, (Object) v3).build();
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return new Builder(Ordering.natural()).put((Object) k, (Object) v).put((Object) k2, (Object) v2).put((Object) k3, (Object) v3).put((Object) k4, (Object) v4).build();
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return new Builder(Ordering.natural()).put((Object) k, (Object) v).put((Object) k2, (Object) v2).put((Object) k3, (Object) v3).put((Object) k4, (Object) v4).put((Object) k5, (Object) v5).build();
    }

    public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
        return new Builder(comparator);
    }

    public static <K extends Comparable<K>, V> Builder<K, V> reverseOrder() {
        return new Builder(Ordering.natural().reverse());
    }

    private static <K, V> void sortEntries(List<Entry<K, V>> list, final Comparator<? super K> comparator) {
        Collections.sort(list, new Comparator<Entry<K, V>>() {
            public int compare(Entry<K, V> entry, Entry<K, V> entry2) {
                return comparator.compare(entry.getKey(), entry2.getKey());
            }
        });
    }

    private static <K, V> void validateEntries(List<Entry<K, V>> list, Comparator<? super K> comparator) {
        for (int i = 1; i < list.size(); i++) {
            if (comparator.compare(((Entry) list.get(i - 1)).getKey(), ((Entry) list.get(i)).getKey()) == 0) {
                throw new IllegalArgumentException("Duplicate keys in mappings " + list.get(i - 1) + " and " + list.get(i));
            }
        }
    }

    public Entry<K, V> ceilingEntry(K k) {
        return tailMap(k, true).firstEntry();
    }

    public K ceilingKey(K k) {
        return Maps.keyOrNull(ceilingEntry(k));
    }

    public Comparator<? super K> comparator() {
        return keySet().comparator();
    }

    public boolean containsValue(@Nullable Object obj) {
        return values().contains(obj);
    }

    abstract ImmutableSortedMap<K, V> createDescendingMap();

    public ImmutableSortedSet<K> descendingKeySet() {
        return keySet().descendingSet();
    }

    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> immutableSortedMap = this.descendingMap;
        if (immutableSortedMap != null) {
            return immutableSortedMap;
        }
        immutableSortedMap = createDescendingMap();
        this.descendingMap = immutableSortedMap;
        return immutableSortedMap;
    }

    public ImmutableSet<Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    public Entry<K, V> firstEntry() {
        return isEmpty() ? null : (Entry) entrySet().asList().get(0);
    }

    public K firstKey() {
        return keySet().first();
    }

    public Entry<K, V> floorEntry(K k) {
        return headMap(k, true).lastEntry();
    }

    public K floorKey(K k) {
        return Maps.keyOrNull(floorEntry(k));
    }

    public ImmutableSortedMap<K, V> headMap(K k) {
        return headMap(k, false);
    }

    public abstract ImmutableSortedMap<K, V> headMap(K k, boolean z);

    public Entry<K, V> higherEntry(K k) {
        return tailMap(k, false).firstEntry();
    }

    public K higherKey(K k) {
        return Maps.keyOrNull(higherEntry(k));
    }

    boolean isPartialView() {
        return keySet().isPartialView() || values().isPartialView();
    }

    public abstract ImmutableSortedSet<K> keySet();

    public Entry<K, V> lastEntry() {
        return isEmpty() ? null : (Entry) entrySet().asList().get(size() - 1);
    }

    public K lastKey() {
        return keySet().last();
    }

    public Entry<K, V> lowerEntry(K k) {
        return headMap(k, false).lastEntry();
    }

    public K lowerKey(K k) {
        return Maps.keyOrNull(lowerEntry(k));
    }

    public ImmutableSortedSet<K> navigableKeySet() {
        return keySet();
    }

    public final Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    public final Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return values().size();
    }

    public ImmutableSortedMap<K, V> subMap(K k, K k2) {
        return subMap(k, true, k2, false);
    }

    public ImmutableSortedMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(k2);
        Preconditions.checkArgument(comparator().compare(k, k2) <= 0, "expected fromKey <= toKey but %s > %s", k, k2);
        return headMap(k2, z2).tailMap(k, z);
    }

    public ImmutableSortedMap<K, V> tailMap(K k) {
        return tailMap(k, true);
    }

    public abstract ImmutableSortedMap<K, V> tailMap(K k, boolean z);

    public abstract ImmutableCollection<V> values();

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
