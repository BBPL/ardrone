package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableListMultimap.Builder;
import com.google.common.collect.Maps.EntryTransformer;
import com.google.common.collect.Multiset.Entry;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Multimaps {

    static abstract class Keys<K, V> extends AbstractMultiset<K> {

        class KeysEntrySet extends EntrySet<K> {
            KeysEntrySet() {
            }

            public boolean contains(@Nullable Object obj) {
                if (obj instanceof Entry) {
                    Entry entry = (Entry) obj;
                    Collection collection = (Collection) Keys.this.multimap().asMap().get(entry.getElement());
                    if (collection != null && collection.size() == entry.getCount()) {
                        return true;
                    }
                }
                return false;
            }

            public boolean isEmpty() {
                return Keys.this.multimap().isEmpty();
            }

            public Iterator<Entry<K>> iterator() {
                return Keys.this.entryIterator();
            }

            Multiset<K> multiset() {
                return Keys.this;
            }

            public boolean remove(@Nullable Object obj) {
                if (obj instanceof Entry) {
                    Entry entry = (Entry) obj;
                    Collection collection = (Collection) Keys.this.multimap().asMap().get(entry.getElement());
                    if (collection != null && collection.size() == entry.getCount()) {
                        collection.clear();
                        return true;
                    }
                }
                return false;
            }

            public int size() {
                return Keys.this.distinctElements();
            }
        }

        Keys() {
        }

        public void clear() {
            multimap().clear();
        }

        public boolean contains(@Nullable Object obj) {
            return multimap().containsKey(obj);
        }

        public int count(@Nullable Object obj) {
            try {
                if (multimap().containsKey(obj)) {
                    Collection collection = (Collection) multimap().asMap().get(obj);
                    return collection == null ? 0 : collection.size();
                }
            } catch (ClassCastException e) {
                return 0;
            } catch (NullPointerException e2) {
            }
            return 0;
        }

        Set<Entry<K>> createEntrySet() {
            return new KeysEntrySet();
        }

        int distinctElements() {
            return multimap().asMap().size();
        }

        public Set<K> elementSet() {
            return multimap().keySet();
        }

        Iterator<Entry<K>> entryIterator() {
            return new TransformedIterator<Map.Entry<K, Collection<V>>, Entry<K>>(multimap().asMap().entrySet().iterator()) {
                Entry<K> transform(final Map.Entry<K, Collection<V>> entry) {
                    return new AbstractEntry<K>() {
                        public int getCount() {
                            return ((Collection) entry.getValue()).size();
                        }

                        public K getElement() {
                            return entry.getKey();
                        }
                    };
                }
            };
        }

        public Iterator<K> iterator() {
            return Maps.keyIterator(multimap().entries().iterator());
        }

        abstract Multimap<K, V> multimap();

        public int remove(@Nullable Object obj, int i) {
            int i2 = 0;
            Preconditions.checkArgument(i >= 0);
            if (i == 0) {
                return count(obj);
            }
            try {
                Collection collection = (Collection) multimap().asMap().get(obj);
                if (collection == null) {
                    return 0;
                }
                int size = collection.size();
                if (i >= size) {
                    collection.clear();
                } else {
                    Iterator it = collection.iterator();
                    while (i2 < i) {
                        it.next();
                        it.remove();
                        i2++;
                    }
                }
                return size;
            } catch (ClassCastException e) {
                return 0;
            } catch (NullPointerException e2) {
                return 0;
            }
        }
    }

    static abstract class Values<K, V> extends AbstractCollection<V> {
        Values() {
        }

        public void clear() {
            multimap().clear();
        }

        public boolean contains(@Nullable Object obj) {
            return multimap().containsValue(obj);
        }

        public Iterator<V> iterator() {
            return Maps.valueIterator(multimap().entries().iterator());
        }

        abstract Multimap<K, V> multimap();

        public int size() {
            return multimap().size();
        }
    }

    static abstract class Entries<K, V> extends AbstractCollection<Map.Entry<K, V>> {
        Entries() {
        }

        public void clear() {
            multimap().clear();
        }

        public boolean contains(@Nullable Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return multimap().containsEntry(entry.getKey(), entry.getValue());
        }

        abstract Multimap<K, V> multimap();

        public boolean remove(@Nullable Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return multimap().remove(entry.getKey(), entry.getValue());
        }

        public int size() {
            return multimap().size();
        }
    }

    static abstract class EntrySet<K, V> extends Entries<K, V> implements Set<Map.Entry<K, V>> {
        EntrySet() {
        }

        public boolean equals(@Nullable Object obj) {
            return Sets.equalsImpl(this, obj);
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    static abstract class AsMap<K, V> extends ImprovedAbstractMap<K, Collection<V>> {

        class EntrySet extends EntrySet<K, Collection<V>> {
            EntrySet() {
            }

            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return AsMap.this.entryIterator();
            }

            Map<K, Collection<V>> map() {
                return AsMap.this;
            }

            public boolean remove(Object obj) {
                if (!contains(obj)) {
                    return false;
                }
                AsMap.this.removeValuesForKey(((Map.Entry) obj).getKey());
                return true;
            }
        }

        AsMap() {
        }

        public void clear() {
            multimap().clear();
        }

        public boolean containsKey(Object obj) {
            return multimap().containsKey(obj);
        }

        protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return new EntrySet();
        }

        abstract Iterator<Map.Entry<K, Collection<V>>> entryIterator();

        public Collection<V> get(Object obj) {
            return containsKey(obj) ? multimap().get(obj) : null;
        }

        public boolean isEmpty() {
            return multimap().isEmpty();
        }

        public Set<K> keySet() {
            return multimap().keySet();
        }

        abstract Multimap<K, V> multimap();

        public Collection<V> remove(Object obj) {
            return containsKey(obj) ? multimap().removeAll(obj) : null;
        }

        void removeValuesForKey(Object obj) {
            multimap().removeAll(obj);
        }

        public abstract int size();
    }

    private static class CustomListMultimap<K, V> extends AbstractListMultimap<K, V> {
        @GwtIncompatible("java serialization not supported")
        private static final long serialVersionUID = 0;
        transient Supplier<? extends List<V>> factory;

        CustomListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> supplier) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(supplier);
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.factory = (Supplier) objectInputStream.readObject();
            setMap((Map) objectInputStream.readObject());
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeObject(this.factory);
            objectOutputStream.writeObject(backingMap());
        }

        protected List<V> createCollection() {
            return (List) this.factory.get();
        }
    }

    private static class CustomMultimap<K, V> extends AbstractMultimap<K, V> {
        @GwtIncompatible("java serialization not supported")
        private static final long serialVersionUID = 0;
        transient Supplier<? extends Collection<V>> factory;

        CustomMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> supplier) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(supplier);
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.factory = (Supplier) objectInputStream.readObject();
            setMap((Map) objectInputStream.readObject());
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeObject(this.factory);
            objectOutputStream.writeObject(backingMap());
        }

        protected Collection<V> createCollection() {
            return (Collection) this.factory.get();
        }
    }

    private static class CustomSetMultimap<K, V> extends AbstractSetMultimap<K, V> {
        @GwtIncompatible("not needed in emulated source")
        private static final long serialVersionUID = 0;
        transient Supplier<? extends Set<V>> factory;

        CustomSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> supplier) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(supplier);
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.factory = (Supplier) objectInputStream.readObject();
            setMap((Map) objectInputStream.readObject());
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeObject(this.factory);
            objectOutputStream.writeObject(backingMap());
        }

        protected Set<V> createCollection() {
            return (Set) this.factory.get();
        }
    }

    private static class CustomSortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V> {
        @GwtIncompatible("not needed in emulated source")
        private static final long serialVersionUID = 0;
        transient Supplier<? extends SortedSet<V>> factory;
        transient Comparator<? super V> valueComparator;

        CustomSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> supplier) {
            super(map);
            this.factory = (Supplier) Preconditions.checkNotNull(supplier);
            this.valueComparator = ((SortedSet) supplier.get()).comparator();
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.factory = (Supplier) objectInputStream.readObject();
            this.valueComparator = ((SortedSet) this.factory.get()).comparator();
            setMap((Map) objectInputStream.readObject());
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeObject(this.factory);
            objectOutputStream.writeObject(backingMap());
        }

        protected SortedSet<V> createCollection() {
            return (SortedSet) this.factory.get();
        }

        public Comparator<? super V> valueComparator() {
            return this.valueComparator;
        }
    }

    private static class FilteredMultimap<K, V> implements Multimap<K, V> {
        static final Predicate<Collection<?>> NOT_EMPTY = new C06891();
        Map<K, Collection<V>> asMap;
        Collection<Map.Entry<K, V>> entries;
        AbstractMultiset<K> keys;
        final Predicate<? super Map.Entry<K, V>> predicate;
        final Multimap<K, V> unfiltered;
        Collection<V> values;

        static final class C06891 implements Predicate<Collection<?>> {
            C06891() {
            }

            public boolean apply(Collection<?> collection) {
                return !collection.isEmpty();
            }
        }

        class C06902 implements EntryTransformer<K, Collection<V>, Collection<V>> {
            C06902() {
            }

            public Collection<V> transformEntry(K k, Collection<V> collection) {
                return FilteredMultimap.this.filterCollection(collection, new ValuePredicate(k));
            }
        }

        class AsMap extends ForwardingMap<K, Collection<V>> {
            com.google.common.collect.Multimaps$FilteredMultimap$AsMap.Values asMapValues;
            final Map<K, Collection<V>> delegate;
            com.google.common.collect.Multimaps$FilteredMultimap$AsMap.EntrySet entrySet;
            Set<K> keySet;

            class EntrySet extends EntrySet<K, Collection<V>> {
                Set<Map.Entry<K, Collection<V>>> delegateEntries;

                public EntrySet(Set<Map.Entry<K, Collection<V>>> set) {
                    this.delegateEntries = set;
                }

                public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                    return this.delegateEntries.iterator();
                }

                Map<K, Collection<V>> map() {
                    return AsMap.this;
                }

                public boolean remove(Object obj) {
                    if (obj instanceof Map.Entry) {
                        Map.Entry entry = (Map.Entry) obj;
                        Collection collection = (Collection) AsMap.this.delegate.get(entry.getKey());
                        if (collection != null && collection.equals(entry.getValue())) {
                            collection.clear();
                            return true;
                        }
                    }
                    return false;
                }

                public boolean removeAll(Collection<?> collection) {
                    return Sets.removeAllImpl((Set) this, (Collection) collection);
                }

                public boolean retainAll(final Collection<?> collection) {
                    return FilteredMultimap.this.removeEntriesIf(new Predicate<Map.Entry<K, Collection<V>>>() {
                        public boolean apply(Map.Entry<K, Collection<V>> entry) {
                            return !collection.contains(entry);
                        }
                    });
                }
            }

            class KeySet extends KeySet<K, Collection<V>> {
                KeySet() {
                }

                Map<K, Collection<V>> map() {
                    return AsMap.this;
                }

                public boolean remove(Object obj) {
                    Collection collection = (Collection) AsMap.this.delegate.get(obj);
                    if (collection == null) {
                        return false;
                    }
                    collection.clear();
                    return true;
                }

                public boolean removeAll(Collection<?> collection) {
                    return Sets.removeAllImpl((Set) this, collection.iterator());
                }

                public boolean retainAll(final Collection<?> collection) {
                    return FilteredMultimap.this.removeEntriesIf(new Predicate<Map.Entry<K, Collection<V>>>() {
                        public boolean apply(Map.Entry<K, Collection<V>> entry) {
                            return !collection.contains(entry.getKey());
                        }
                    });
                }
            }

            class Values extends Values<K, Collection<V>> {
                Values() {
                }

                Map<K, Collection<V>> map() {
                    return AsMap.this;
                }

                public boolean remove(Object obj) {
                    Iterator it = iterator();
                    while (it.hasNext()) {
                        Collection collection = (Collection) it.next();
                        if (collection.equals(obj)) {
                            collection.clear();
                            return true;
                        }
                    }
                    return false;
                }

                public boolean removeAll(final Collection<?> collection) {
                    return FilteredMultimap.this.removeEntriesIf(new Predicate<Map.Entry<K, Collection<V>>>() {
                        public boolean apply(Map.Entry<K, Collection<V>> entry) {
                            return collection.contains(entry.getValue());
                        }
                    });
                }

                public boolean retainAll(final Collection<?> collection) {
                    return FilteredMultimap.this.removeEntriesIf(new Predicate<Map.Entry<K, Collection<V>>>() {
                        public boolean apply(Map.Entry<K, Collection<V>> entry) {
                            return !collection.contains(entry.getValue());
                        }
                    });
                }
            }

            AsMap(Map<K, Collection<V>> map) {
                this.delegate = map;
            }

            public void clear() {
                FilteredMultimap.this.clear();
            }

            protected Map<K, Collection<V>> delegate() {
                return this.delegate;
            }

            public Set<Map.Entry<K, Collection<V>>> entrySet() {
                if (this.entrySet != null) {
                    return this.entrySet;
                }
                Set entrySet = new EntrySet(super.entrySet());
                this.entrySet = entrySet;
                return entrySet;
            }

            public Set<K> keySet() {
                if (this.keySet != null) {
                    return this.keySet;
                }
                Set<K> keySet = new KeySet();
                this.keySet = keySet;
                return keySet;
            }

            public Collection<V> remove(Object obj) {
                Collection<V> removeAll = FilteredMultimap.this.removeAll(obj);
                return removeAll.isEmpty() ? null : removeAll;
            }

            public Collection<Collection<V>> values() {
                if (this.asMapValues != null) {
                    return this.asMapValues;
                }
                Collection values = new Values();
                this.asMapValues = values;
                return values;
            }
        }

        class Keys extends Keys<K, V> {

            class EntrySet extends KeysEntrySet {
                EntrySet() {
                    super();
                }

                public boolean removeAll(Collection<?> collection) {
                    return Sets.removeAllImpl((Set) this, collection.iterator());
                }

                public boolean retainAll(final Collection<?> collection) {
                    return FilteredMultimap.this.removeEntriesIf(new Predicate<Map.Entry<K, Collection<V>>>() {
                        public boolean apply(Map.Entry<K, Collection<V>> entry) {
                            return !collection.contains(Multisets.immutableEntry(entry.getKey(), ((Collection) entry.getValue()).size()));
                        }
                    });
                }
            }

            Keys() {
            }

            Set<Entry<K>> createEntrySet() {
                return new EntrySet();
            }

            Multimap<K, V> multimap() {
                return FilteredMultimap.this;
            }

            public int remove(Object obj, int i) {
                int i2 = 0;
                Preconditions.checkArgument(i >= 0);
                Collection collection = (Collection) FilteredMultimap.this.unfiltered.asMap().get(obj);
                if (collection == null) {
                    return 0;
                }
                Iterator it = collection.iterator();
                int i3 = 0;
                while (it.hasNext()) {
                    if (FilteredMultimap.this.satisfiesPredicate(obj, it.next())) {
                        i3++;
                        if (i2 < i) {
                            it.remove();
                            i2++;
                        }
                    }
                }
                return i3;
            }
        }

        class ValuePredicate implements Predicate<V> {
            final K key;

            ValuePredicate(K k) {
                this.key = k;
            }

            public boolean apply(V v) {
                return FilteredMultimap.this.satisfiesPredicate(this.key, v);
            }
        }

        class Values extends Values<K, V> {
            Values() {
            }

            public boolean contains(@Nullable Object obj) {
                return Iterators.contains(iterator(), obj);
            }

            Multimap<K, V> multimap() {
                return FilteredMultimap.this;
            }

            public boolean remove(Object obj) {
                Iterator it = FilteredMultimap.this.unfiltered.entries().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if (Objects.equal(obj, entry.getValue()) && FilteredMultimap.this.predicate.apply(entry)) {
                        it.remove();
                        return true;
                    }
                }
                return false;
            }

            public boolean removeAll(Collection<?> collection) {
                Iterator it = FilteredMultimap.this.unfiltered.entries().iterator();
                boolean z = false;
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if (collection.contains(entry.getValue()) && FilteredMultimap.this.predicate.apply(entry)) {
                        it.remove();
                        z = true;
                    }
                }
                return z;
            }

            public boolean retainAll(Collection<?> collection) {
                Iterator it = FilteredMultimap.this.unfiltered.entries().iterator();
                boolean z = false;
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if (!collection.contains(entry.getValue()) && FilteredMultimap.this.predicate.apply(entry)) {
                        it.remove();
                        z = true;
                    }
                }
                return z;
            }
        }

        FilteredMultimap(Multimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> predicate) {
            this.unfiltered = multimap;
            this.predicate = predicate;
        }

        public Map<K, Collection<V>> asMap() {
            if (this.asMap != null) {
                return this.asMap;
            }
            Map<K, Collection<V>> createAsMap = createAsMap();
            this.asMap = createAsMap;
            return createAsMap;
        }

        public void clear() {
            entries().clear();
        }

        public boolean containsEntry(Object obj, Object obj2) {
            return this.unfiltered.containsEntry(obj, obj2) && satisfiesPredicate(obj, obj2);
        }

        public boolean containsKey(Object obj) {
            return asMap().containsKey(obj);
        }

        public boolean containsValue(Object obj) {
            return values().contains(obj);
        }

        Map<K, Collection<V>> createAsMap() {
            return new AsMap(Maps.filterValues(Maps.transformEntries(this.unfiltered.asMap(), new C06902()), NOT_EMPTY));
        }

        public Collection<Map.Entry<K, V>> entries() {
            if (this.entries != null) {
                return this.entries;
            }
            Collection<Map.Entry<K, V>> filter = Collections2.filter(this.unfiltered.entries(), this.predicate);
            this.entries = filter;
            return filter;
        }

        public boolean equals(@Nullable Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Multimap)) {
                return false;
            }
            return asMap().equals(((Multimap) obj).asMap());
        }

        Collection<V> filterCollection(Collection<V> collection, Predicate<V> predicate) {
            return collection instanceof Set ? Sets.filter((Set) collection, (Predicate) predicate) : Collections2.filter(collection, predicate);
        }

        public Collection<V> get(K k) {
            return filterCollection(this.unfiltered.get(k), new ValuePredicate(k));
        }

        public int hashCode() {
            return asMap().hashCode();
        }

        public boolean isEmpty() {
            return entries().isEmpty();
        }

        public Set<K> keySet() {
            return asMap().keySet();
        }

        public Multiset<K> keys() {
            if (this.keys != null) {
                return this.keys;
            }
            Multiset keys = new Keys();
            this.keys = keys;
            return keys;
        }

        public boolean put(K k, V v) {
            Preconditions.checkArgument(satisfiesPredicate(k, v));
            return this.unfiltered.put(k, v);
        }

        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            for (Map.Entry entry : multimap.entries()) {
                Preconditions.checkArgument(satisfiesPredicate(entry.getKey(), entry.getValue()));
            }
            return this.unfiltered.putAll(multimap);
        }

        public boolean putAll(K k, Iterable<? extends V> iterable) {
            for (Object satisfiesPredicate : iterable) {
                Preconditions.checkArgument(satisfiesPredicate(k, satisfiesPredicate));
            }
            return this.unfiltered.putAll(k, iterable);
        }

        public boolean remove(Object obj, Object obj2) {
            return containsEntry(obj, obj2) ? this.unfiltered.remove(obj, obj2) : false;
        }

        public Collection<V> removeAll(Object obj) {
            Object newArrayList = Lists.newArrayList();
            Collection collection = (Collection) this.unfiltered.asMap().get(obj);
            if (collection != null) {
                Iterator it = collection.iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    if (satisfiesPredicate(obj, next)) {
                        newArrayList.add(next);
                        it.remove();
                    }
                }
            }
            return this.unfiltered instanceof SetMultimap ? Collections.unmodifiableSet(Sets.newLinkedHashSet(newArrayList)) : Collections.unmodifiableList(newArrayList);
        }

        boolean removeEntriesIf(Predicate<Map.Entry<K, Collection<V>>> predicate) {
            Iterator it = this.unfiltered.asMap().entrySet().iterator();
            boolean z = false;
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Object key = entry.getKey();
                Collection collection = (Collection) entry.getValue();
                Predicate valuePredicate = new ValuePredicate(key);
                Collection filterCollection = filterCollection(collection, valuePredicate);
                if (predicate.apply(Maps.immutableEntry(key, filterCollection)) && !filterCollection.isEmpty()) {
                    z = true;
                    if (Iterables.all(collection, valuePredicate)) {
                        it.remove();
                    } else {
                        filterCollection.clear();
                    }
                }
            }
            return z;
        }

        public Collection<V> replaceValues(K k, Iterable<? extends V> iterable) {
            for (Object satisfiesPredicate : iterable) {
                Preconditions.checkArgument(satisfiesPredicate(k, satisfiesPredicate));
            }
            Collection<V> removeAll = removeAll(k);
            this.unfiltered.putAll(k, iterable);
            return removeAll;
        }

        boolean satisfiesPredicate(Object obj, Object obj2) {
            return this.predicate.apply(Maps.immutableEntry(obj, obj2));
        }

        public int size() {
            return entries().size();
        }

        public String toString() {
            return asMap().toString();
        }

        public Collection<V> values() {
            if (this.values != null) {
                return this.values;
            }
            Collection<V> values = new Values();
            this.values = values;
            return values;
        }
    }

    private static class MapMultimap<K, V> implements SetMultimap<K, V>, Serializable {
        private static final MapJoiner JOINER = Joiner.on("], ").withKeyValueSeparator("=[").useForNull("null");
        private static final long serialVersionUID = 7845222491160860175L;
        transient Map<K, Collection<V>> asMap;
        final Map<K, V> map;

        class AsMap extends ImprovedAbstractMap<K, Collection<V>> {
            AsMap() {
            }

            public boolean containsKey(Object obj) {
                return MapMultimap.this.map.containsKey(obj);
            }

            protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
                return new AsMapEntries();
            }

            public Collection<V> get(Object obj) {
                Collection<V> collection = MapMultimap.this.get(obj);
                return collection.isEmpty() ? null : collection;
            }

            public Collection<V> remove(Object obj) {
                Collection<V> removeAll = MapMultimap.this.removeAll(obj);
                return removeAll.isEmpty() ? null : removeAll;
            }
        }

        class AsMapEntries extends ImprovedAbstractSet<Map.Entry<K, Collection<V>>> {
            AsMapEntries() {
            }

            public boolean contains(Object obj) {
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry) obj;
                if (!(entry.getValue() instanceof Set)) {
                    return false;
                }
                Set set = (Set) entry.getValue();
                boolean z = set.size() == 1 && MapMultimap.this.containsEntry(entry.getKey(), set.iterator().next());
                return z;
            }

            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return new TransformedIterator<K, Map.Entry<K, Collection<V>>>(MapMultimap.this.map.keySet().iterator()) {
                    Map.Entry<K, Collection<V>> transform(final K k) {
                        return new AbstractMapEntry<K, Collection<V>>() {
                            public K getKey() {
                                return k;
                            }

                            public Collection<V> getValue() {
                                return MapMultimap.this.get(k);
                            }
                        };
                    }
                };
            }

            public boolean remove(Object obj) {
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry) obj;
                if (!(entry.getValue() instanceof Set)) {
                    return false;
                }
                Set set = (Set) entry.getValue();
                boolean z = set.size() == 1 && MapMultimap.this.map.entrySet().remove(Maps.immutableEntry(entry.getKey(), set.iterator().next()));
                return z;
            }

            public int size() {
                return MapMultimap.this.map.size();
            }
        }

        MapMultimap(Map<K, V> map) {
            this.map = (Map) Preconditions.checkNotNull(map);
        }

        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> map = this.asMap;
            if (map != null) {
                return map;
            }
            map = new AsMap();
            this.asMap = map;
            return map;
        }

        public void clear() {
            this.map.clear();
        }

        public boolean containsEntry(Object obj, Object obj2) {
            return this.map.entrySet().contains(Maps.immutableEntry(obj, obj2));
        }

        public boolean containsKey(Object obj) {
            return this.map.containsKey(obj);
        }

        public boolean containsValue(Object obj) {
            return this.map.containsValue(obj);
        }

        public Set<Map.Entry<K, V>> entries() {
            return this.map.entrySet();
        }

        public boolean equals(@Nullable Object obj) {
            if (obj != this) {
                if (!(obj instanceof Multimap)) {
                    return false;
                }
                Multimap multimap = (Multimap) obj;
                if (size() != multimap.size()) {
                    return false;
                }
                if (!asMap().equals(multimap.asMap())) {
                    return false;
                }
            }
            return true;
        }

        public Set<V> get(final K k) {
            return new ImprovedAbstractSet<V>() {

                class C06981 implements Iterator<V> {
                    int f273i;

                    C06981() {
                    }

                    public boolean hasNext() {
                        return this.f273i == 0 && MapMultimap.this.map.containsKey(k);
                    }

                    public V next() {
                        if (hasNext()) {
                            this.f273i++;
                            return MapMultimap.this.map.get(k);
                        }
                        throw new NoSuchElementException();
                    }

                    public void remove() {
                        boolean z = true;
                        if (this.f273i != 1) {
                            z = false;
                        }
                        Preconditions.checkState(z);
                        this.f273i = -1;
                        MapMultimap.this.map.remove(k);
                    }
                }

                public Iterator<V> iterator() {
                    return new C06981();
                }

                public int size() {
                    return MapMultimap.this.map.containsKey(k) ? 1 : 0;
                }
            };
        }

        public int hashCode() {
            return this.map.hashCode();
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public Set<K> keySet() {
            return this.map.keySet();
        }

        public Multiset<K> keys() {
            return Multisets.forSet(this.map.keySet());
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
            return this.map.entrySet().remove(Maps.immutableEntry(obj, obj2));
        }

        public Set<V> removeAll(Object obj) {
            Set hashSet = new HashSet(2);
            if (this.map.containsKey(obj)) {
                hashSet.add(this.map.remove(obj));
            }
            return hashSet;
        }

        public Set<V> replaceValues(K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return this.map.size();
        }

        public String toString() {
            if (this.map.isEmpty()) {
                return "{}";
            }
            StringBuilder append = Collections2.newStringBuilderForCollection(this.map.size()).append('{');
            JOINER.appendTo(append, this.map);
            return append.append("]}").toString();
        }

        public Collection<V> values() {
            return this.map.values();
        }
    }

    private static class TransformedEntriesMultimap<K, V1, V2> implements Multimap<K, V2> {
        private transient Map<K, Collection<V2>> asMap;
        private transient Collection<Map.Entry<K, V2>> entries;
        final Multimap<K, V1> fromMultimap;
        final EntryTransformer<? super K, ? super V1, V2> transformer;
        private transient Collection<V2> values;

        class C07042 implements EntryTransformer<K, Collection<V1>, Collection<V2>> {
            C07042() {
            }

            public Collection<V2> transformEntry(K k, Collection<V1> collection) {
                return TransformedEntriesMultimap.this.transform(k, collection);
            }
        }

        class C07053 implements Function<Map.Entry<K, V1>, V2> {
            C07053() {
            }

            public V2 apply(Map.Entry<K, V1> entry) {
                return TransformedEntriesMultimap.this.transformer.transformEntry(entry.getKey(), entry.getValue());
            }
        }

        private class TransformedEntries extends TransformedCollection<Map.Entry<K, V1>, Map.Entry<K, V2>> {

            class C07071 implements Function<Map.Entry<K, V1>, Map.Entry<K, V2>> {
                final /* synthetic */ TransformedEntriesMultimap val$this$0;
                final /* synthetic */ EntryTransformer val$transformer;

                C07071(TransformedEntriesMultimap transformedEntriesMultimap, EntryTransformer entryTransformer) {
                    this.val$this$0 = transformedEntriesMultimap;
                    this.val$transformer = entryTransformer;
                }

                public Map.Entry<K, V2> apply(final Map.Entry<K, V1> entry) {
                    return new AbstractMapEntry<K, V2>() {
                        public K getKey() {
                            return entry.getKey();
                        }

                        public V2 getValue() {
                            return C07071.this.val$transformer.transformEntry(entry.getKey(), entry.getValue());
                        }
                    };
                }
            }

            TransformedEntries(EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
                super(TransformedEntriesMultimap.this.fromMultimap.entries(), new C07071(TransformedEntriesMultimap.this, entryTransformer));
            }

            public boolean contains(Object obj) {
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry) obj;
                return TransformedEntriesMultimap.this.containsEntry(entry.getKey(), entry.getValue());
            }

            public boolean remove(Object obj) {
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry) obj;
                return TransformedEntriesMultimap.this.get(entry.getKey()).remove(entry.getValue());
            }
        }

        TransformedEntriesMultimap(Multimap<K, V1> multimap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
            this.fromMultimap = (Multimap) Preconditions.checkNotNull(multimap);
            this.transformer = (EntryTransformer) Preconditions.checkNotNull(entryTransformer);
        }

        public Map<K, Collection<V2>> asMap() {
            if (this.asMap != null) {
                return this.asMap;
            }
            Map<K, Collection<V2>> transformEntries = Maps.transformEntries(this.fromMultimap.asMap(), new C07042());
            this.asMap = transformEntries;
            return transformEntries;
        }

        public void clear() {
            this.fromMultimap.clear();
        }

        public boolean containsEntry(Object obj, Object obj2) {
            return get(obj).contains(obj2);
        }

        public boolean containsKey(Object obj) {
            return this.fromMultimap.containsKey(obj);
        }

        public boolean containsValue(Object obj) {
            return values().contains(obj);
        }

        public Collection<Map.Entry<K, V2>> entries() {
            if (this.entries != null) {
                return this.entries;
            }
            Collection<Map.Entry<K, V2>> transformedEntries = new TransformedEntries(this.transformer);
            this.entries = transformedEntries;
            return transformedEntries;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Multimap)) {
                return false;
            }
            return asMap().equals(((Multimap) obj).asMap());
        }

        public Collection<V2> get(K k) {
            return transform(k, this.fromMultimap.get(k));
        }

        public int hashCode() {
            return asMap().hashCode();
        }

        public boolean isEmpty() {
            return this.fromMultimap.isEmpty();
        }

        public Set<K> keySet() {
            return this.fromMultimap.keySet();
        }

        public Multiset<K> keys() {
            return this.fromMultimap.keys();
        }

        public boolean put(K k, V2 v2) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(Multimap<? extends K, ? extends V2> multimap) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(K k, Iterable<? extends V2> iterable) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object obj, Object obj2) {
            return get(obj).remove(obj2);
        }

        public Collection<V2> removeAll(Object obj) {
            return transform(obj, this.fromMultimap.removeAll(obj));
        }

        public Collection<V2> replaceValues(K k, Iterable<? extends V2> iterable) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return this.fromMultimap.size();
        }

        public String toString() {
            return asMap().toString();
        }

        Collection<V2> transform(final K k, Collection<V1> collection) {
            return Collections2.transform(collection, new Function<V1, V2>() {
                public V2 apply(V1 v1) {
                    return TransformedEntriesMultimap.this.transformer.transformEntry(k, v1);
                }
            });
        }

        public Collection<V2> values() {
            if (this.values != null) {
                return this.values;
            }
            Collection<V2> transform = Collections2.transform(this.fromMultimap.entries(), new C07053());
            this.values = transform;
            return transform;
        }
    }

    private static final class TransformedEntriesListMultimap<K, V1, V2> extends TransformedEntriesMultimap<K, V1, V2> implements ListMultimap<K, V2> {
        TransformedEntriesListMultimap(ListMultimap<K, V1> listMultimap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
            super(listMultimap, entryTransformer);
        }

        public List<V2> get(K k) {
            return transform((Object) k, this.fromMultimap.get(k));
        }

        public List<V2> removeAll(Object obj) {
            return transform(obj, this.fromMultimap.removeAll(obj));
        }

        public List<V2> replaceValues(K k, Iterable<? extends V2> iterable) {
            throw new UnsupportedOperationException();
        }

        List<V2> transform(final K k, Collection<V1> collection) {
            return Lists.transform((List) collection, new Function<V1, V2>() {
                public V2 apply(V1 v1) {
                    return TransformedEntriesListMultimap.this.transformer.transformEntry(k, v1);
                }
            });
        }
    }

    static class UnmodifiableAsMapEntries<K, V> extends ForwardingSet<Map.Entry<K, Collection<V>>> {
        private final Set<Map.Entry<K, Collection<V>>> delegate;

        UnmodifiableAsMapEntries(Set<Map.Entry<K, Collection<V>>> set) {
            this.delegate = set;
        }

        public boolean contains(Object obj) {
            return Maps.containsEntryImpl(delegate(), obj);
        }

        public boolean containsAll(Collection<?> collection) {
            return standardContainsAll(collection);
        }

        protected Set<Map.Entry<K, Collection<V>>> delegate() {
            return this.delegate;
        }

        public boolean equals(@Nullable Object obj) {
            return standardEquals(obj);
        }

        public Iterator<Map.Entry<K, Collection<V>>> iterator() {
            final Iterator it = this.delegate.iterator();
            return new ForwardingIterator<Map.Entry<K, Collection<V>>>() {
                protected Iterator<Map.Entry<K, Collection<V>>> delegate() {
                    return it;
                }

                public Map.Entry<K, Collection<V>> next() {
                    return Multimaps.unmodifiableAsMapEntry((Map.Entry) it.next());
                }
            };
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return standardToArray(tArr);
        }
    }

    private static class UnmodifiableAsMapValues<V> extends ForwardingCollection<Collection<V>> {
        final Collection<Collection<V>> delegate;

        UnmodifiableAsMapValues(Collection<Collection<V>> collection) {
            this.delegate = Collections.unmodifiableCollection(collection);
        }

        public boolean contains(Object obj) {
            return standardContains(obj);
        }

        public boolean containsAll(Collection<?> collection) {
            return standardContainsAll(collection);
        }

        protected Collection<Collection<V>> delegate() {
            return this.delegate;
        }

        public Iterator<Collection<V>> iterator() {
            final Iterator it = this.delegate.iterator();
            return new UnmodifiableIterator<Collection<V>>() {
                public boolean hasNext() {
                    return it.hasNext();
                }

                public Collection<V> next() {
                    return Multimaps.unmodifiableValueCollection((Collection) it.next());
                }
            };
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return standardToArray(tArr);
        }
    }

    private static class UnmodifiableMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final Multimap<K, V> delegate;
        transient Collection<Map.Entry<K, V>> entries;
        transient Set<K> keySet;
        transient Multiset<K> keys;
        transient Map<K, Collection<V>> map;
        transient Collection<V> values;

        UnmodifiableMultimap(Multimap<K, V> multimap) {
            this.delegate = (Multimap) Preconditions.checkNotNull(multimap);
        }

        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> map = this.map;
            if (map != null) {
                return map;
            }
            final Map unmodifiableMap = Collections.unmodifiableMap(this.delegate.asMap());
            map = new ForwardingMap<K, Collection<V>>() {
                Collection<Collection<V>> asMapValues;
                Set<Map.Entry<K, Collection<V>>> entrySet;

                public boolean containsValue(Object obj) {
                    return values().contains(obj);
                }

                protected Map<K, Collection<V>> delegate() {
                    return unmodifiableMap;
                }

                public Set<Map.Entry<K, Collection<V>>> entrySet() {
                    Set<Map.Entry<K, Collection<V>>> set = this.entrySet;
                    if (set != null) {
                        return set;
                    }
                    set = Multimaps.unmodifiableAsMapEntries(unmodifiableMap.entrySet());
                    this.entrySet = set;
                    return set;
                }

                public Collection<V> get(Object obj) {
                    Collection collection = (Collection) unmodifiableMap.get(obj);
                    return collection == null ? null : Multimaps.unmodifiableValueCollection(collection);
                }

                public Collection<Collection<V>> values() {
                    Collection<Collection<V>> collection = this.asMapValues;
                    if (collection != null) {
                        return collection;
                    }
                    collection = new UnmodifiableAsMapValues(unmodifiableMap.values());
                    this.asMapValues = collection;
                    return collection;
                }
            };
            this.map = map;
            return map;
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        protected Multimap<K, V> delegate() {
            return this.delegate;
        }

        public Collection<Map.Entry<K, V>> entries() {
            Collection<Map.Entry<K, V>> collection = this.entries;
            if (collection != null) {
                return collection;
            }
            collection = Multimaps.unmodifiableEntries(this.delegate.entries());
            this.entries = collection;
            return collection;
        }

        public Collection<V> get(K k) {
            return Multimaps.unmodifiableValueCollection(this.delegate.get(k));
        }

        public Set<K> keySet() {
            Set<K> set = this.keySet;
            if (set != null) {
                return set;
            }
            set = Collections.unmodifiableSet(this.delegate.keySet());
            this.keySet = set;
            return set;
        }

        public Multiset<K> keys() {
            Multiset<K> multiset = this.keys;
            if (multiset != null) {
                return multiset;
            }
            multiset = Multisets.unmodifiableMultiset(this.delegate.keys());
            this.keys = multiset;
            return multiset;
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

        public Collection<V> removeAll(Object obj) {
            throw new UnsupportedOperationException();
        }

        public Collection<V> replaceValues(K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }

        public Collection<V> values() {
            Collection<V> collection = this.values;
            if (collection != null) {
                return collection;
            }
            collection = Collections.unmodifiableCollection(this.delegate.values());
            this.values = collection;
            return collection;
        }
    }

    private static class UnmodifiableListMultimap<K, V> extends UnmodifiableMultimap<K, V> implements ListMultimap<K, V> {
        private static final long serialVersionUID = 0;

        UnmodifiableListMultimap(ListMultimap<K, V> listMultimap) {
            super(listMultimap);
        }

        public ListMultimap<K, V> delegate() {
            return (ListMultimap) super.delegate();
        }

        public List<V> get(K k) {
            return Collections.unmodifiableList(delegate().get(k));
        }

        public List<V> removeAll(Object obj) {
            throw new UnsupportedOperationException();
        }

        public List<V> replaceValues(K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }
    }

    private static class UnmodifiableSetMultimap<K, V> extends UnmodifiableMultimap<K, V> implements SetMultimap<K, V> {
        private static final long serialVersionUID = 0;

        UnmodifiableSetMultimap(SetMultimap<K, V> setMultimap) {
            super(setMultimap);
        }

        public SetMultimap<K, V> delegate() {
            return (SetMultimap) super.delegate();
        }

        public Set<Map.Entry<K, V>> entries() {
            return Maps.unmodifiableEntrySet(delegate().entries());
        }

        public Set<V> get(K k) {
            return Collections.unmodifiableSet(delegate().get(k));
        }

        public Set<V> removeAll(Object obj) {
            throw new UnsupportedOperationException();
        }

        public Set<V> replaceValues(K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }
    }

    private static class UnmodifiableSortedSetMultimap<K, V> extends UnmodifiableSetMultimap<K, V> implements SortedSetMultimap<K, V> {
        private static final long serialVersionUID = 0;

        UnmodifiableSortedSetMultimap(SortedSetMultimap<K, V> sortedSetMultimap) {
            super(sortedSetMultimap);
        }

        public SortedSetMultimap<K, V> delegate() {
            return (SortedSetMultimap) super.delegate();
        }

        public SortedSet<V> get(K k) {
            return Collections.unmodifiableSortedSet(delegate().get(k));
        }

        public SortedSet<V> removeAll(Object obj) {
            throw new UnsupportedOperationException();
        }

        public SortedSet<V> replaceValues(K k, Iterable<? extends V> iterable) {
            throw new UnsupportedOperationException();
        }

        public Comparator<? super V> valueComparator() {
            return delegate().valueComparator();
        }
    }

    private Multimaps() {
    }

    @GwtIncompatible("untested")
    public static <K, V> Multimap<K, V> filterEntries(Multimap<K, V> multimap, Predicate<? super Map.Entry<K, V>> predicate) {
        Preconditions.checkNotNull(predicate);
        return multimap instanceof FilteredMultimap ? filterFiltered((FilteredMultimap) multimap, predicate) : new FilteredMultimap((Multimap) Preconditions.checkNotNull(multimap), predicate);
    }

    private static <K, V> Multimap<K, V> filterFiltered(FilteredMultimap<K, V> filteredMultimap, Predicate<? super Map.Entry<K, V>> predicate) {
        return new FilteredMultimap(filteredMultimap.unfiltered, Predicates.and(filteredMultimap.predicate, predicate));
    }

    @GwtIncompatible("untested")
    public static <K, V> Multimap<K, V> filterKeys(Multimap<K, V> multimap, final Predicate<? super K> predicate) {
        Preconditions.checkNotNull(predicate);
        return filterEntries(multimap, new Predicate<Map.Entry<K, V>>() {
            public boolean apply(Map.Entry<K, V> entry) {
                return predicate.apply(entry.getKey());
            }
        });
    }

    @GwtIncompatible("untested")
    public static <K, V> Multimap<K, V> filterValues(Multimap<K, V> multimap, final Predicate<? super V> predicate) {
        Preconditions.checkNotNull(predicate);
        return filterEntries(multimap, new Predicate<Map.Entry<K, V>>() {
            public boolean apply(Map.Entry<K, V> entry) {
                return predicate.apply(entry.getValue());
            }
        });
    }

    public static <K, V> SetMultimap<K, V> forMap(Map<K, V> map) {
        return new MapMultimap(map);
    }

    public static <K, V> ImmutableListMultimap<K, V> index(Iterable<V> iterable, Function<? super V, K> function) {
        return index(iterable.iterator(), (Function) function);
    }

    public static <K, V> ImmutableListMultimap<K, V> index(Iterator<V> it, Function<? super V, K> function) {
        Preconditions.checkNotNull(function);
        Builder builder = ImmutableListMultimap.builder();
        while (it.hasNext()) {
            Object next = it.next();
            Preconditions.checkNotNull(next, it);
            builder.put(function.apply(next), next);
        }
        return builder.build();
    }

    public static <K, V, M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> multimap, M m) {
        Preconditions.checkNotNull(m);
        for (Map.Entry entry : multimap.entries()) {
            m.put(entry.getValue(), entry.getKey());
        }
        return m;
    }

    public static <K, V> ListMultimap<K, V> newListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> supplier) {
        return new CustomListMultimap(map, supplier);
    }

    public static <K, V> Multimap<K, V> newMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> supplier) {
        return new CustomMultimap(map, supplier);
    }

    public static <K, V> SetMultimap<K, V> newSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> supplier) {
        return new CustomSetMultimap(map, supplier);
    }

    public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> supplier) {
        return new CustomSortedSetMultimap(map, supplier);
    }

    public static <K, V> ListMultimap<K, V> synchronizedListMultimap(ListMultimap<K, V> listMultimap) {
        return Synchronized.listMultimap(listMultimap, null);
    }

    public static <K, V> Multimap<K, V> synchronizedMultimap(Multimap<K, V> multimap) {
        return Synchronized.multimap(multimap, null);
    }

    public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(SetMultimap<K, V> setMultimap) {
        return Synchronized.setMultimap(setMultimap, null);
    }

    public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(SortedSetMultimap<K, V> sortedSetMultimap) {
        return Synchronized.sortedSetMultimap(sortedSetMultimap, null);
    }

    public static <K, V1, V2> ListMultimap<K, V2> transformEntries(ListMultimap<K, V1> listMultimap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        return new TransformedEntriesListMultimap(listMultimap, entryTransformer);
    }

    public static <K, V1, V2> Multimap<K, V2> transformEntries(Multimap<K, V1> multimap, EntryTransformer<? super K, ? super V1, V2> entryTransformer) {
        return new TransformedEntriesMultimap(multimap, entryTransformer);
    }

    public static <K, V1, V2> ListMultimap<K, V2> transformValues(ListMultimap<K, V1> listMultimap, final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        return transformEntries((ListMultimap) listMultimap, new EntryTransformer<K, V1, V2>() {
            public V2 transformEntry(K k, V1 v1) {
                return function.apply(v1);
            }
        });
    }

    public static <K, V1, V2> Multimap<K, V2> transformValues(Multimap<K, V1> multimap, final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        return transformEntries((Multimap) multimap, new EntryTransformer<K, V1, V2>() {
            public V2 transformEntry(K k, V1 v1) {
                return function.apply(v1);
            }
        });
    }

    private static <K, V> Set<Map.Entry<K, Collection<V>>> unmodifiableAsMapEntries(Set<Map.Entry<K, Collection<V>>> set) {
        return new UnmodifiableAsMapEntries(Collections.unmodifiableSet(set));
    }

    private static <K, V> Map.Entry<K, Collection<V>> unmodifiableAsMapEntry(final Map.Entry<K, Collection<V>> entry) {
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, Collection<V>>() {
            public K getKey() {
                return entry.getKey();
            }

            public Collection<V> getValue() {
                return Multimaps.unmodifiableValueCollection((Collection) entry.getValue());
            }
        };
    }

    private static <K, V> Collection<Map.Entry<K, V>> unmodifiableEntries(Collection<Map.Entry<K, V>> collection) {
        return collection instanceof Set ? Maps.unmodifiableEntrySet((Set) collection) : new UnmodifiableEntries(Collections.unmodifiableCollection(collection));
    }

    @Deprecated
    public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ImmutableListMultimap<K, V> immutableListMultimap) {
        return (ListMultimap) Preconditions.checkNotNull(immutableListMultimap);
    }

    public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ListMultimap<K, V> listMultimap) {
        return ((listMultimap instanceof UnmodifiableListMultimap) || (listMultimap instanceof ImmutableListMultimap)) ? listMultimap : new UnmodifiableListMultimap(listMultimap);
    }

    @Deprecated
    public static <K, V> Multimap<K, V> unmodifiableMultimap(ImmutableMultimap<K, V> immutableMultimap) {
        return (Multimap) Preconditions.checkNotNull(immutableMultimap);
    }

    public static <K, V> Multimap<K, V> unmodifiableMultimap(Multimap<K, V> multimap) {
        return ((multimap instanceof UnmodifiableMultimap) || (multimap instanceof ImmutableMultimap)) ? multimap : new UnmodifiableMultimap(multimap);
    }

    @Deprecated
    public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(ImmutableSetMultimap<K, V> immutableSetMultimap) {
        return (SetMultimap) Preconditions.checkNotNull(immutableSetMultimap);
    }

    public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(SetMultimap<K, V> setMultimap) {
        return ((setMultimap instanceof UnmodifiableSetMultimap) || (setMultimap instanceof ImmutableSetMultimap)) ? setMultimap : new UnmodifiableSetMultimap(setMultimap);
    }

    public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(SortedSetMultimap<K, V> sortedSetMultimap) {
        return sortedSetMultimap instanceof UnmodifiableSortedSetMultimap ? sortedSetMultimap : new UnmodifiableSortedSetMultimap(sortedSetMultimap);
    }

    private static <V> Collection<V> unmodifiableValueCollection(Collection<V> collection) {
        return collection instanceof SortedSet ? Collections.unmodifiableSortedSet((SortedSet) collection) : collection instanceof Set ? Collections.unmodifiableSet((Set) collection) : collection instanceof List ? Collections.unmodifiableList((List) collection) : Collections.unmodifiableCollection(collection);
    }
}
