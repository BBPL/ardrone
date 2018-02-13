package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Maps {
    static final MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");

    static abstract class EntrySet<K, V> extends ImprovedAbstractSet<Entry<K, V>> {
        EntrySet() {
        }

        public void clear() {
            map().clear();
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            Object key = entry.getKey();
            Object obj2 = map().get(key);
            return Objects.equal(obj2, entry.getValue()) ? obj2 != null || map().containsKey(key) : false;
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        abstract Map<K, V> map();

        public boolean remove(Object obj) {
            if (!contains(obj)) {
                return false;
            }
            return map().keySet().remove(((Entry) obj).getKey());
        }

        public boolean removeAll(Collection<?> collection) {
            boolean removeAll;
            try {
                removeAll = super.removeAll((Collection) Preconditions.checkNotNull(collection));
            } catch (UnsupportedOperationException e) {
                removeAll = true;
                for (Object remove : collection) {
                    removeAll |= remove(remove);
                }
            }
            return removeAll;
        }

        public boolean retainAll(Collection<?> collection) {
            try {
                return super.retainAll((Collection) Preconditions.checkNotNull(collection));
            } catch (UnsupportedOperationException e) {
                Collection newHashSetWithExpectedSize = Sets.newHashSetWithExpectedSize(collection.size());
                for (Object next : collection) {
                    if (contains(next)) {
                        newHashSetWithExpectedSize.add(((Entry) next).getKey());
                    }
                }
                return map().keySet().retainAll(newHashSetWithExpectedSize);
            }
        }

        public int size() {
            return map().size();
        }
    }

    static abstract class KeySet<K, V> extends ImprovedAbstractSet<K> {
        KeySet() {
        }

        public void clear() {
            map().clear();
        }

        public boolean contains(Object obj) {
            return map().containsKey(obj);
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public Iterator<K> iterator() {
            return Maps.keyIterator(map().entrySet().iterator());
        }

        abstract Map<K, V> map();

        public boolean remove(Object obj) {
            if (!contains(obj)) {
                return false;
            }
            map().remove(obj);
            return true;
        }

        public int size() {
            return map().size();
        }
    }

    @GwtCompatible
    static abstract class ImprovedAbstractMap<K, V> extends AbstractMap<K, V> {
        private Set<Entry<K, V>> entrySet;
        private Set<K> keySet;
        private Collection<V> values;

        class C06761 extends KeySet<K, V> {
            C06761() {
            }

            Map<K, V> map() {
                return ImprovedAbstractMap.this;
            }
        }

        class C06772 extends Values<K, V> {
            C06772() {
            }

            Map<K, V> map() {
                return ImprovedAbstractMap.this;
            }
        }

        ImprovedAbstractMap() {
        }

        protected abstract Set<Entry<K, V>> createEntrySet();

        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = createEntrySet();
            this.entrySet = set;
            return set;
        }

        public Set<K> keySet() {
            Set<K> set = this.keySet;
            if (set != null) {
                return set;
            }
            set = new C06761();
            this.keySet = set;
            return set;
        }

        public Collection<V> values() {
            Collection<V> collection = this.values;
            if (collection != null) {
                return collection;
            }
            collection = new C06772();
            this.values = collection;
            return collection;
        }
    }

    static abstract class Values<K, V> extends AbstractCollection<V> {
        Values() {
        }

        public void clear() {
            map().clear();
        }

        public boolean contains(@Nullable Object obj) {
            return map().containsValue(obj);
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public Iterator<V> iterator() {
            return Maps.valueIterator(map().entrySet().iterator());
        }

        abstract Map<K, V> map();

        public boolean remove(Object obj) {
            try {
                return super.remove(obj);
            } catch (UnsupportedOperationException e) {
                for (Entry entry : map().entrySet()) {
                    if (Objects.equal(obj, entry.getValue())) {
                        map().remove(entry.getKey());
                        return true;
                    }
                }
                return false;
            }
        }

        public boolean removeAll(Collection<?> collection) {
            try {
                return super.removeAll((Collection) Preconditions.checkNotNull(collection));
            } catch (UnsupportedOperationException e) {
                Collection newHashSet = Sets.newHashSet();
                for (Entry entry : map().entrySet()) {
                    if (collection.contains(entry.getValue())) {
                        newHashSet.add(entry.getKey());
                    }
                }
                return map().keySet().removeAll(newHashSet);
            }
        }

        public boolean retainAll(Collection<?> collection) {
            try {
                return super.retainAll((Collection) Preconditions.checkNotNull(collection));
            } catch (UnsupportedOperationException e) {
                Collection newHashSet = Sets.newHashSet();
                for (Entry entry : map().entrySet()) {
                    if (collection.contains(entry.getValue())) {
                        newHashSet.add(entry.getKey());
                    }
                }
                return map().keySet().retainAll(newHashSet);
            }
        }

        public int size() {
            return map().size();
        }
    }

    public interface EntryTransformer<K, V1, V2> {
        V2 transformEntry(@Nullable K k, @Nullable V1 v1);
    }

    private static abstract class AbstractFilteredMap<K, V> extends AbstractMap<K, V> {
        final Predicate<? super Entry<K, V>> predicate;
        final Map<K, V> unfiltered;
        Collection<V> values;

        class Values extends AbstractCollection<V> {
            Values() {
            }

            public void clear() {
                AbstractFilteredMap.this.entrySet().clear();
            }

            public boolean isEmpty() {
                return AbstractFilteredMap.this.entrySet().isEmpty();
            }

            public Iterator<V> iterator() {
                final Iterator it = AbstractFilteredMap.this.entrySet().iterator();
                return new UnmodifiableIterator<V>() {
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    public V next() {
                        return ((Entry) it.next()).getValue();
                    }
                };
            }

            public boolean remove(Object obj) {
                Iterator it = AbstractFilteredMap.this.unfiltered.entrySet().iterator();
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    if (Objects.equal(obj, entry.getValue()) && AbstractFilteredMap.this.predicate.apply(entry)) {
                        it.remove();
                        return true;
                    }
                }
                return false;
            }

            public boolean removeAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                Iterator it = AbstractFilteredMap.this.unfiltered.entrySet().iterator();
                boolean z = false;
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    if (collection.contains(entry.getValue()) && AbstractFilteredMap.this.predicate.apply(entry)) {
                        it.remove();
                        z = true;
                    }
                }
                return z;
            }

            public boolean retainAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                Iterator it = AbstractFilteredMap.this.unfiltered.entrySet().iterator();
                boolean z = false;
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    if (!collection.contains(entry.getValue()) && AbstractFilteredMap.this.predicate.apply(entry)) {
                        it.remove();
                        z = true;
                    }
                }
                return z;
            }

            public int size() {
                return AbstractFilteredMap.this.entrySet().size();
            }

            public Object[] toArray() {
                return Lists.newArrayList(iterator()).toArray();
            }

            public <T> T[] toArray(T[] tArr) {
                return Lists.newArrayList(iterator()).toArray(tArr);
            }
        }

        AbstractFilteredMap(Map<K, V> map, Predicate<? super Entry<K, V>> predicate) {
            this.unfiltered = map;
            this.predicate = predicate;
        }

        boolean apply(Object obj, V v) {
            return this.predicate.apply(Maps.immutableEntry(obj, v));
        }

        public boolean containsKey(Object obj) {
            return this.unfiltered.containsKey(obj) && apply(obj, this.unfiltered.get(obj));
        }

        public V get(Object obj) {
            V v = this.unfiltered.get(obj);
            return (v == null || !apply(obj, v)) ? null : v;
        }

        public boolean isEmpty() {
            return entrySet().isEmpty();
        }

        public V put(K k, V v) {
            Preconditions.checkArgument(apply(k, v));
            return this.unfiltered.put(k, v);
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            for (Entry entry : map.entrySet()) {
                Preconditions.checkArgument(apply(entry.getKey(), entry.getValue()));
            }
            this.unfiltered.putAll(map);
        }

        public V remove(Object obj) {
            return containsKey(obj) ? this.unfiltered.remove(obj) : null;
        }

        public Collection<V> values() {
            Collection<V> collection = this.values;
            if (collection != null) {
                return collection;
            }
            collection = new Values();
            this.values = collection;
            return collection;
        }
    }

    private static class AsMapView<K, V> extends ImprovedAbstractMap<K, V> {
        final Function<? super K, V> function;
        private final Set<K> set;

        class C06701 extends ForwardingSet<K> {
            C06701() {
            }

            public boolean add(K k) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(Collection<? extends K> collection) {
                throw new UnsupportedOperationException();
            }

            protected Set<K> delegate() {
                return AsMapView.this.set;
            }
        }

        class C06722 extends EntrySet<K, V> {
            C06722() {
            }

            public Iterator<Entry<K, V>> iterator() {
                final Iterator it = AsMapView.this.set.iterator();
                return new Iterator<Entry<K, V>>() {
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    public Entry<K, V> next() {
                        Object next = it.next();
                        return Maps.immutableEntry(next, AsMapView.this.function.apply(next));
                    }

                    public void remove() {
                        it.remove();
                    }
                };
            }

            Map<K, V> map() {
                return AsMapView.this;
            }
        }

        AsMapView(Set<K> set, Function<? super K, V> function) {
            this.set = (Set) Preconditions.checkNotNull(set);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        Set<K> backingSet() {
            return this.set;
        }

        public void clear() {
            this.set.clear();
        }

        public boolean containsKey(@Nullable Object obj) {
            return this.set.contains(obj);
        }

        protected Set<Entry<K, V>> createEntrySet() {
            return new C06722();
        }

        public V get(@Nullable Object obj) {
            return this.set.contains(obj) ? this.function.apply(obj) : null;
        }

        public Set<K> keySet() {
            return new C06701();
        }

        public V remove(@Nullable Object obj) {
            return this.set.remove(obj) ? this.function.apply(obj) : null;
        }

        public int size() {
            return this.set.size();
        }

        public Collection<V> values() {
            return Collections2.transform(this.set, this.function);
        }
    }

    static class FilteredEntryMap<K, V> extends AbstractFilteredMap<K, V> {
        Set<Entry<K, V>> entrySet;
        final Set<Entry<K, V>> filteredEntrySet;
        Set<K> keySet;

        private class EntrySet extends ForwardingSet<Entry<K, V>> {
            private EntrySet() {
            }

            protected Set<Entry<K, V>> delegate() {
                return FilteredEntryMap.this.filteredEntrySet;
            }

            public Iterator<Entry<K, V>> iterator() {
                final Iterator it = FilteredEntryMap.this.filteredEntrySet.iterator();
                return new UnmodifiableIterator<Entry<K, V>>() {
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    public Entry<K, V> next() {
                        final Entry entry = (Entry) it.next();
                        return new ForwardingMapEntry<K, V>() {
                            protected Entry<K, V> delegate() {
                                return entry;
                            }

                            public V setValue(V v) {
                                Preconditions.checkArgument(FilteredEntryMap.this.apply(entry.getKey(), v));
                                return super.setValue(v);
                            }
                        };
                    }
                };
            }
        }

        private class KeySet extends ImprovedAbstractSet<K> {
            private KeySet() {
            }

            public void clear() {
                FilteredEntryMap.this.filteredEntrySet.clear();
            }

            public boolean contains(Object obj) {
                return FilteredEntryMap.this.containsKey(obj);
            }

            public Iterator<K> iterator() {
                final Iterator it = FilteredEntryMap.this.filteredEntrySet.iterator();
                return new UnmodifiableIterator<K>() {
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    public K next() {
                        return ((Entry) it.next()).getKey();
                    }
                };
            }

            public boolean remove(Object obj) {
                if (!FilteredEntryMap.this.containsKey(obj)) {
                    return false;
                }
                FilteredEntryMap.this.unfiltered.remove(obj);
                return true;
            }

            public boolean retainAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                Iterator it = FilteredEntryMap.this.unfiltered.entrySet().iterator();
                boolean z = false;
                while (it.hasNext()) {
                    Entry entry = (Entry) it.next();
                    if (FilteredEntryMap.this.predicate.apply(entry) && !collection.contains(entry.getKey())) {
                        it.remove();
                        z = true;
                    }
                }
                return z;
            }

            public int size() {
                return FilteredEntryMap.this.filteredEntrySet.size();
            }

            public Object[] toArray() {
                return Lists.newArrayList(iterator()).toArray();
            }

            public <T> T[] toArray(T[] tArr) {
                return Lists.newArrayList(iterator()).toArray(tArr);
            }
        }

        FilteredEntryMap(Map<K, V> map, Predicate<? super Entry<K, V>> predicate) {
            super(map, predicate);
            this.filteredEntrySet = Sets.filter(map.entrySet(), this.predicate);
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

        public Set<K> keySet() {
            Set<K> set = this.keySet;
            if (set != null) {
                return set;
            }
            set = new KeySet();
            this.keySet = set;
            return set;
        }
    }

    private static class FilteredEntrySortedMap<K, V> extends FilteredEntryMap<K, V> implements SortedMap<K, V> {
        FilteredEntrySortedMap(SortedMap<K, V> sortedMap, Predicate<? super Entry<K, V>> predicate) {
            super(sortedMap, predicate);
        }

        public Comparator<? super K> comparator() {
            return sortedMap().comparator();
        }

        public K firstKey() {
            return keySet().iterator().next();
        }

        public SortedMap<K, V> headMap(K k) {
            return new FilteredEntrySortedMap(sortedMap().headMap(k), this.predicate);
        }

        public K lastKey() {
            SortedMap sortedMap = sortedMap();
            while (true) {
                K lastKey = sortedMap.lastKey();
                if (apply(lastKey, this.unfiltered.get(lastKey))) {
                    return lastKey;
                }
                sortedMap = sortedMap().headMap(lastKey);
            }
        }

        SortedMap<K, V> sortedMap() {
            return (SortedMap) this.unfiltered;
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            return new FilteredEntrySortedMap(sortedMap().subMap(k, k2), this.predicate);
        }

        public SortedMap<K, V> tailMap(K k) {
            return new FilteredEntrySortedMap(sortedMap().tailMap(k), this.predicate);
        }
    }

    private static class FilteredKeyMap<K, V> extends AbstractFilteredMap<K, V> {
        Set<Entry<K, V>> entrySet;
        Predicate<? super K> keyPredicate;
        Set<K> keySet;

        FilteredKeyMap(Map<K, V> map, Predicate<? super K> predicate, Predicate<Entry<K, V>> predicate2) {
            super(map, predicate2);
            this.keyPredicate = predicate;
        }

        public boolean containsKey(Object obj) {
            return this.unfiltered.containsKey(obj) && this.keyPredicate.apply(obj);
        }

        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = Sets.filter(this.unfiltered.entrySet(), this.predicate);
            this.entrySet = set;
            return set;
        }

        public Set<K> keySet() {
            Set<K> set = this.keySet;
            if (set != null) {
                return set;
            }
            set = Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
            this.keySet = set;
            return set;
        }
    }

    static class MapDifferenceImpl<K, V> implements MapDifference<K, V> {
        final boolean areEqual;
        final Map<K, ValueDifference<V>> differences;
        final Map<K, V> onBoth;
        final Map<K, V> onlyOnLeft;
        final Map<K, V> onlyOnRight;

        MapDifferenceImpl(boolean z, Map<K, V> map, Map<K, V> map2, Map<K, V> map3, Map<K, ValueDifference<V>> map4) {
            this.areEqual = z;
            this.onlyOnLeft = map;
            this.onlyOnRight = map2;
            this.onBoth = map3;
            this.differences = map4;
        }

        public boolean areEqual() {
            return this.areEqual;
        }

        public Map<K, ValueDifference<V>> entriesDiffering() {
            return this.differences;
        }

        public Map<K, V> entriesInCommon() {
            return this.onBoth;
        }

        public Map<K, V> entriesOnlyOnLeft() {
            return this.onlyOnLeft;
        }

        public Map<K, V> entriesOnlyOnRight() {
            return this.onlyOnRight;
        }

        public boolean equals(Object obj) {
            if (obj != this) {
                if (!(obj instanceof MapDifference)) {
                    return false;
                }
                MapDifference mapDifference = (MapDifference) obj;
                if (!entriesOnlyOnLeft().equals(mapDifference.entriesOnlyOnLeft()) || !entriesOnlyOnRight().equals(mapDifference.entriesOnlyOnRight()) || !entriesInCommon().equals(mapDifference.entriesInCommon())) {
                    return false;
                }
                if (!entriesDiffering().equals(mapDifference.entriesDiffering())) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            return Objects.hashCode(entriesOnlyOnLeft(), entriesOnlyOnRight(), entriesInCommon(), entriesDiffering());
        }

        public String toString() {
            if (this.areEqual) {
                return "equal";
            }
            StringBuilder stringBuilder = new StringBuilder("not equal");
            if (!this.onlyOnLeft.isEmpty()) {
                stringBuilder.append(": only on left=").append(this.onlyOnLeft);
            }
            if (!this.onlyOnRight.isEmpty()) {
                stringBuilder.append(": only on right=").append(this.onlyOnRight);
            }
            if (!this.differences.isEmpty()) {
                stringBuilder.append(": value differences=").append(this.differences);
            }
            return stringBuilder.toString();
        }
    }

    private static final class SortedAsMapView<K, V> extends AsMapView<K, V> implements SortedMap<K, V> {
        SortedAsMapView(SortedSet<K> sortedSet, Function<? super K, V> function) {
            super(sortedSet, function);
        }

        SortedSet<K> backingSet() {
            return (SortedSet) super.backingSet();
        }

        public Comparator<? super K> comparator() {
            return backingSet().comparator();
        }

        public K firstKey() {
            return backingSet().first();
        }

        public SortedMap<K, V> headMap(K k) {
            return Maps.asMap(backingSet().headSet(k), this.function);
        }

        public K lastKey() {
            return backingSet().last();
        }

        public SortedMap<K, V> subMap(K k, K k2) {
            return Maps.asMap(backingSet().subSet(k, k2), this.function);
        }

        public SortedMap<K, V> tailMap(K k) {
            return Maps.asMap(backingSet().tailSet(k), this.function);
        }
    }

    static class SortedMapDifferenceImpl<K, V> extends MapDifferenceImpl<K, V> implements SortedMapDifference<K, V> {
        SortedMapDifferenceImpl(boolean z, SortedMap<K, V> sortedMap, SortedMap<K, V> sortedMap2, SortedMap<K, V> sortedMap3, SortedMap<K, ValueDifference<V>> sortedMap4) {
            super(z, sortedMap, sortedMap2, sortedMap3, sortedMap4);
        }

        public SortedMap<K, ValueDifference<V>> entriesDiffering() {
            return (SortedMap) super.entriesDiffering();
        }

        public SortedMap<K, V> entriesInCommon() {
            return (SortedMap) super.entriesInCommon();
        }

        public SortedMap<K, V> entriesOnlyOnLeft() {
            return (SortedMap) super.entriesOnlyOnLeft();
        }

        public SortedMap<K, V> entriesOnlyOnRight() {
            return (SortedMap) super.entriesOnlyOnRight();
        }
    }

    static class TransformedEntriesMap<K, V1, V2> extends AbstractMap<K, V2> {
        Set<Entry<K, V2>> entrySet;
        final Map<K, V1> fromMap;
        final EntryTransformer<? super K, ? super V1, V2> transformer;
        Collection<V2> values;

        class C06801 extends EntrySet<K, V2> {
            C06801() {
            }

            public Iterator<Entry<K, V2>> iterator() {
                return new TransformedIterator<Entry<K, V1>, Entry<K, V2>>(TransformedEntriesMap.this.fromMap.entrySet().iterator()) {
                    Entry<K, V2> transform(final Entry<K, V1> entry) {
                        return new AbstractMapEntry<K, V2>() {
                            public K getKey() {
                                return entry.getKey();
                            }

                            public V2 getValue() {
                                return TransformedEntriesMap.this.transformer.transformEntry(entry.getKey(), entry.getValue());
                            }
                        };
                    }
                };
            }

            Map<K, V2> map() {
                return TransformedEntriesMap.this;
            }
        }

        class C06812 extends Values<K, V2> {
            C06812() {
            }

            Map<K, V2> map() {
                return TransformedEntriesMap.this;
            }
        }

        TransformedEntriesMap(Map<K, V1> map, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
            this.fromMap = (Map) Preconditions.checkNotNull(map);
            this.transformer = (EntryTransformer) Preconditions.checkNotNull(entryTransformer);
        }

        public void clear() {
            this.fromMap.clear();
        }

        public boolean containsKey(Object obj) {
            return this.fromMap.containsKey(obj);
        }

        public Set<Entry<K, V2>> entrySet() {
            Set<Entry<K, V2>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = new C06801();
            this.entrySet = set;
            return set;
        }

        public V2 get(Object obj) {
            Object obj2 = this.fromMap.get(obj);
            return (obj2 != null || this.fromMap.containsKey(obj)) ? this.transformer.transformEntry(obj, obj2) : null;
        }

        public Set<K> keySet() {
            return this.fromMap.keySet();
        }

        public V2 remove(Object obj) {
            return this.fromMap.containsKey(obj) ? this.transformer.transformEntry(obj, this.fromMap.remove(obj)) : null;
        }

        public int size() {
            return this.fromMap.size();
        }

        public Collection<V2> values() {
            Collection<V2> collection = this.values;
            if (collection != null) {
                return collection;
            }
            collection = new C06812();
            this.values = collection;
            return collection;
        }
    }

    static class TransformedEntriesSortedMap<K, V1, V2> extends TransformedEntriesMap<K, V1, V2> implements SortedMap<K, V2> {
        TransformedEntriesSortedMap(SortedMap<K, V1> sortedMap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
            super(sortedMap, entryTransformer);
        }

        public Comparator<? super K> comparator() {
            return fromMap().comparator();
        }

        public K firstKey() {
            return fromMap().firstKey();
        }

        protected SortedMap<K, V1> fromMap() {
            return (SortedMap) this.fromMap;
        }

        public SortedMap<K, V2> headMap(K k) {
            return Maps.transformEntries(fromMap().headMap(k), this.transformer);
        }

        public K lastKey() {
            return fromMap().lastKey();
        }

        public SortedMap<K, V2> subMap(K k, K k2) {
            return Maps.transformEntries(fromMap().subMap(k, k2), this.transformer);
        }

        public SortedMap<K, V2> tailMap(K k) {
            return Maps.transformEntries(fromMap().tailMap(k), this.transformer);
        }
    }

    private static class UnmodifiableBiMap<K, V> extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
        private static final long serialVersionUID = 0;
        final BiMap<? extends K, ? extends V> delegate;
        BiMap<V, K> inverse;
        final Map<K, V> unmodifiableMap;
        transient Set<V> values;

        UnmodifiableBiMap(BiMap<? extends K, ? extends V> biMap, @Nullable BiMap<V, K> biMap2) {
            this.unmodifiableMap = Collections.unmodifiableMap(biMap);
            this.delegate = biMap;
            this.inverse = biMap2;
        }

        protected Map<K, V> delegate() {
            return this.unmodifiableMap;
        }

        public V forcePut(K k, V v) {
            throw new UnsupportedOperationException();
        }

        public BiMap<V, K> inverse() {
            BiMap<V, K> biMap = this.inverse;
            if (biMap != null) {
                return biMap;
            }
            biMap = new UnmodifiableBiMap(this.delegate.inverse(), this);
            this.inverse = biMap;
            return biMap;
        }

        public Set<V> values() {
            Set<V> set = this.values;
            if (set != null) {
                return set;
            }
            set = Collections.unmodifiableSet(this.delegate.values());
            this.values = set;
            return set;
        }
    }

    static class UnmodifiableEntries<K, V> extends ForwardingCollection<Entry<K, V>> {
        private final Collection<Entry<K, V>> entries;

        UnmodifiableEntries(Collection<Entry<K, V>> collection) {
            this.entries = collection;
        }

        public boolean add(Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends Entry<K, V>> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        protected Collection<Entry<K, V>> delegate() {
            return this.entries;
        }

        public Iterator<Entry<K, V>> iterator() {
            final Iterator it = super.iterator();
            return new ForwardingIterator<Entry<K, V>>() {
                protected Iterator<Entry<K, V>> delegate() {
                    return it;
                }

                public Entry<K, V> next() {
                    return Maps.unmodifiableEntry((Entry) super.next());
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return standardToArray(tArr);
        }
    }

    static class UnmodifiableEntrySet<K, V> extends UnmodifiableEntries<K, V> implements Set<Entry<K, V>> {
        UnmodifiableEntrySet(Set<Entry<K, V>> set) {
            super(set);
        }

        public boolean equals(@Nullable Object obj) {
            return Sets.equalsImpl(this, obj);
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    static class ValueDifferenceImpl<V> implements ValueDifference<V> {
        private final V left;
        private final V right;

        private ValueDifferenceImpl(@Nullable V v, @Nullable V v2) {
            this.left = v;
            this.right = v2;
        }

        static <V> ValueDifference<V> create(@Nullable V v, @Nullable V v2) {
            return new ValueDifferenceImpl(v, v2);
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof ValueDifference)) {
                return false;
            }
            ValueDifference valueDifference = (ValueDifference) obj;
            return Objects.equal(this.left, valueDifference.leftValue()) && Objects.equal(this.right, valueDifference.rightValue());
        }

        public int hashCode() {
            return Objects.hashCode(this.left, this.right);
        }

        public V leftValue() {
            return this.left;
        }

        public V rightValue() {
            return this.right;
        }

        public String toString() {
            return "(" + this.left + ", " + this.right + ")";
        }
    }

    private Maps() {
    }

    private static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        return new EntryTransformer<K, V1, V2>() {
            public V2 transformEntry(K k, V1 v1) {
                return function.apply(v1);
            }
        };
    }

    @Beta
    static <K, V> Map<K, V> asMap(Set<K> set, Function<? super K, V> function) {
        return set instanceof SortedSet ? asMap((SortedSet) set, (Function) function) : new AsMapView(set, function);
    }

    @Beta
    static <K, V> SortedMap<K, V> asMap(SortedSet<K> sortedSet, Function<? super K, V> function) {
        return new SortedAsMapView(sortedSet, function);
    }

    static int capacity(int i) {
        if (i >= 3) {
            return i < Ints.MAX_POWER_OF_TWO ? (i / 3) + i : Integer.MAX_VALUE;
        } else {
            Preconditions.checkArgument(i >= 0);
            return i + 1;
        }
    }

    static <K, V> boolean containsEntryImpl(Collection<Entry<K, V>> collection, Object obj) {
        return !(obj instanceof Entry) ? false : collection.contains(unmodifiableEntry((Entry) obj));
    }

    static boolean containsKeyImpl(Map<?, ?> map, @Nullable Object obj) {
        for (Entry key : map.entrySet()) {
            if (Objects.equal(key.getKey(), obj)) {
                return true;
            }
        }
        return false;
    }

    static boolean containsValueImpl(Map<?, ?> map, @Nullable Object obj) {
        for (Entry value : map.entrySet()) {
            if (Objects.equal(value.getValue(), obj)) {
                return true;
            }
        }
        return false;
    }

    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> map, Map<? extends K, ? extends V> map2) {
        return map instanceof SortedMap ? difference((SortedMap) map, (Map) map2) : difference(map, map2, Equivalence.equals());
    }

    @Beta
    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> map, Map<? extends K, ? extends V> map2, Equivalence<? super V> equivalence) {
        boolean z = true;
        Preconditions.checkNotNull(equivalence);
        Map newHashMap = newHashMap();
        Map hashMap = new HashMap(map2);
        Map newHashMap2 = newHashMap();
        Map newHashMap3 = newHashMap();
        boolean z2 = true;
        for (Entry entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (map2.containsKey(key)) {
                Object remove = hashMap.remove(key);
                if (equivalence.equivalent(value, remove)) {
                    newHashMap2.put(key, value);
                } else {
                    newHashMap3.put(key, ValueDifferenceImpl.create(value, remove));
                    z2 = false;
                }
            } else {
                newHashMap.put(key, value);
                z2 = false;
            }
        }
        if (!(z2 && hashMap.isEmpty())) {
            z = false;
        }
        return mapDifference(z, newHashMap, hashMap, newHashMap2, newHashMap3);
    }

    public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> sortedMap, Map<? extends K, ? extends V> map) {
        boolean z = true;
        Preconditions.checkNotNull(sortedMap);
        Preconditions.checkNotNull(map);
        Comparator orNaturalOrder = orNaturalOrder(sortedMap.comparator());
        SortedMap newTreeMap = newTreeMap(orNaturalOrder);
        SortedMap newTreeMap2 = newTreeMap(orNaturalOrder);
        newTreeMap2.putAll(map);
        SortedMap newTreeMap3 = newTreeMap(orNaturalOrder);
        SortedMap newTreeMap4 = newTreeMap(orNaturalOrder);
        boolean z2 = true;
        for (Entry entry : sortedMap.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (map.containsKey(key)) {
                Object remove = newTreeMap2.remove(key);
                if (Objects.equal(value, remove)) {
                    newTreeMap3.put(key, value);
                } else {
                    newTreeMap4.put(key, ValueDifferenceImpl.create(value, remove));
                    z2 = false;
                }
            } else {
                newTreeMap.put(key, value);
                z2 = false;
            }
        }
        if (!(z2 && newTreeMap2.isEmpty())) {
            z = false;
        }
        return sortedMapDifference(z, newTreeMap, newTreeMap2, newTreeMap3, newTreeMap4);
    }

    static boolean equalsImpl(Map<?, ?> map, Object obj) {
        if (map == obj) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        return map.entrySet().equals(((Map) obj).entrySet());
    }

    public static <K, V> Map<K, V> filterEntries(Map<K, V> map, Predicate<? super Entry<K, V>> predicate) {
        if (map instanceof SortedMap) {
            return filterEntries((SortedMap) map, (Predicate) predicate);
        }
        Preconditions.checkNotNull(predicate);
        return map instanceof AbstractFilteredMap ? filterFiltered((AbstractFilteredMap) map, (Predicate) predicate) : new FilteredEntryMap((Map) Preconditions.checkNotNull(map), predicate);
    }

    public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> sortedMap, Predicate<? super Entry<K, V>> predicate) {
        Preconditions.checkNotNull(predicate);
        return sortedMap instanceof FilteredEntrySortedMap ? filterFiltered((FilteredEntrySortedMap) sortedMap, (Predicate) predicate) : new FilteredEntrySortedMap((SortedMap) Preconditions.checkNotNull(sortedMap), predicate);
    }

    private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> abstractFilteredMap, Predicate<? super Entry<K, V>> predicate) {
        return new FilteredEntryMap(abstractFilteredMap.unfiltered, Predicates.and(abstractFilteredMap.predicate, predicate));
    }

    private static <K, V> SortedMap<K, V> filterFiltered(FilteredEntrySortedMap<K, V> filteredEntrySortedMap, Predicate<? super Entry<K, V>> predicate) {
        return new FilteredEntrySortedMap(filteredEntrySortedMap.sortedMap(), Predicates.and(filteredEntrySortedMap.predicate, predicate));
    }

    public static <K, V> Map<K, V> filterKeys(Map<K, V> map, final Predicate<? super K> predicate) {
        if (map instanceof SortedMap) {
            return filterKeys((SortedMap) map, (Predicate) predicate);
        }
        Preconditions.checkNotNull(predicate);
        Predicate c06623 = new Predicate<Entry<K, V>>() {
            public boolean apply(Entry<K, V> entry) {
                return predicate.apply(entry.getKey());
            }
        };
        return map instanceof AbstractFilteredMap ? filterFiltered((AbstractFilteredMap) map, c06623) : new FilteredKeyMap((Map) Preconditions.checkNotNull(map), predicate, c06623);
    }

    public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> sortedMap, final Predicate<? super K> predicate) {
        Preconditions.checkNotNull(predicate);
        return filterEntries((SortedMap) sortedMap, new Predicate<Entry<K, V>>() {
            public boolean apply(Entry<K, V> entry) {
                return predicate.apply(entry.getKey());
            }
        });
    }

    public static <K, V> Map<K, V> filterValues(Map<K, V> map, final Predicate<? super V> predicate) {
        if (map instanceof SortedMap) {
            return filterValues((SortedMap) map, (Predicate) predicate);
        }
        Preconditions.checkNotNull(predicate);
        return filterEntries((Map) map, new Predicate<Entry<K, V>>() {
            public boolean apply(Entry<K, V> entry) {
                return predicate.apply(entry.getValue());
            }
        });
    }

    public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> sortedMap, final Predicate<? super V> predicate) {
        Preconditions.checkNotNull(predicate);
        return filterEntries((SortedMap) sortedMap, new Predicate<Entry<K, V>>() {
            public boolean apply(Entry<K, V> entry) {
                return predicate.apply(entry.getValue());
            }
        });
    }

    @GwtIncompatible("java.util.Properties")
    public static ImmutableMap<String, String> fromProperties(Properties properties) {
        Builder builder = ImmutableMap.builder();
        Enumeration propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String str = (String) propertyNames.nextElement();
            builder.put(str, properties.getProperty(str));
        }
        return builder.build();
    }

    static int hashCodeImpl(Map<?, ?> map) {
        return Sets.hashCodeImpl(map.entrySet());
    }

    @GwtCompatible(serializable = true)
    public static <K, V> Entry<K, V> immutableEntry(@Nullable K k, @Nullable V v) {
        return new ImmutableEntry(k, v);
    }

    static <K, V> Iterator<K> keyIterator(Iterator<Entry<K, V>> it) {
        return new TransformedIterator<Entry<K, V>, K>(it) {
            K transform(Entry<K, V> entry) {
                return entry.getKey();
            }
        };
    }

    @Nullable
    static <K> K keyOrNull(@Nullable Entry<K, ?> entry) {
        return entry == null ? null : entry.getKey();
    }

    private static <K, V> MapDifference<K, V> mapDifference(boolean z, Map<K, V> map, Map<K, V> map2, Map<K, V> map3, Map<K, ValueDifference<V>> map4) {
        return new MapDifferenceImpl(z, Collections.unmodifiableMap(map), Collections.unmodifiableMap(map2), Collections.unmodifiableMap(map3), Collections.unmodifiableMap(map4));
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return new MapMaker().makeMap();
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> cls) {
        return new EnumMap((Class) Preconditions.checkNotNull(cls));
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> map) {
        return new EnumMap(map);
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap();
    }

    public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        return new HashMap(map);
    }

    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int i) {
        return new HashMap(capacity(i));
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
        return new LinkedHashMap(map);
    }

    public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap();
    }

    public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable Comparator<C> comparator) {
        return new TreeMap(comparator);
    }

    public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> sortedMap) {
        return new TreeMap(sortedMap);
    }

    static <E> Comparator<? super E> orNaturalOrder(@Nullable Comparator<? super E> comparator) {
        return comparator != null ? comparator : Ordering.natural();
    }

    static <K, V> void putAllImpl(Map<K, V> map, Map<? extends K, ? extends V> map2) {
        for (Entry entry : map2.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    static <K, V> boolean removeEntryImpl(Collection<Entry<K, V>> collection, Object obj) {
        return !(obj instanceof Entry) ? false : collection.remove(unmodifiableEntry((Entry) obj));
    }

    static boolean safeContainsKey(Map<?, ?> map, Object obj) {
        try {
            return map.containsKey(obj);
        } catch (ClassCastException e) {
            return false;
        }
    }

    static <V> V safeGet(Map<?, V> map, Object obj) {
        try {
            return map.get(obj);
        } catch (ClassCastException e) {
            return null;
        }
    }

    private static <K, V> SortedMapDifference<K, V> sortedMapDifference(boolean z, SortedMap<K, V> sortedMap, SortedMap<K, V> sortedMap2, SortedMap<K, V> sortedMap3, SortedMap<K, ValueDifference<V>> sortedMap4) {
        return new SortedMapDifferenceImpl(z, Collections.unmodifiableSortedMap(sortedMap), Collections.unmodifiableSortedMap(sortedMap2), Collections.unmodifiableSortedMap(sortedMap3), Collections.unmodifiableSortedMap(sortedMap4));
    }

    public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> biMap) {
        return Synchronized.biMap(biMap, null);
    }

    static String toStringImpl(Map<?, ?> map) {
        StringBuilder append = Collections2.newStringBuilderForCollection(map.size()).append('{');
        STANDARD_JOINER.appendTo(append, (Map) map);
        return append.append('}').toString();
    }

    public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> map, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        return map instanceof SortedMap ? transformEntries((SortedMap) map, (EntryTransformer) entryTransformer) : new TransformedEntriesMap(map, entryTransformer);
    }

    @Beta
    public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> sortedMap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        return new TransformedEntriesSortedMap(sortedMap, entryTransformer);
    }

    public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> map, Function<? super V1, V2> function) {
        return transformEntries((Map) map, asEntryTransformer(function));
    }

    @Beta
    public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> sortedMap, Function<? super V1, V2> function) {
        return transformEntries((SortedMap) sortedMap, asEntryTransformer(function));
    }

    public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> iterable, Function<? super V, K> function) {
        return uniqueIndex(iterable.iterator(), (Function) function);
    }

    public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> it, Function<? super V, K> function) {
        Preconditions.checkNotNull(function);
        Builder builder = ImmutableMap.builder();
        while (it.hasNext()) {
            Object next = it.next();
            builder.put(function.apply(next), next);
        }
        return builder.build();
    }

    public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> biMap) {
        return new UnmodifiableBiMap(biMap, null);
    }

    static <K, V> Entry<K, V> unmodifiableEntry(final Entry<K, V> entry) {
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, V>() {
            public K getKey() {
                return entry.getKey();
            }

            public V getValue() {
                return entry.getValue();
            }
        };
    }

    static <K, V> Set<Entry<K, V>> unmodifiableEntrySet(Set<Entry<K, V>> set) {
        return new UnmodifiableEntrySet(Collections.unmodifiableSet(set));
    }

    @Nullable
    private static <K, V> Entry<K, V> unmodifiableOrNull(@Nullable Entry<K, V> entry) {
        return entry == null ? null : unmodifiableEntry(entry);
    }

    static <K, V> UnmodifiableIterator<V> valueIterator(final UnmodifiableIterator<Entry<K, V>> unmodifiableIterator) {
        return new UnmodifiableIterator<V>() {
            public boolean hasNext() {
                return unmodifiableIterator.hasNext();
            }

            public V next() {
                return ((Entry) unmodifiableIterator.next()).getValue();
            }
        };
    }

    static <K, V> Iterator<V> valueIterator(Iterator<Entry<K, V>> it) {
        return new TransformedIterator<Entry<K, V>, V>(it) {
            V transform(Entry<K, V> entry) {
                return entry.getValue();
            }
        };
    }
}
