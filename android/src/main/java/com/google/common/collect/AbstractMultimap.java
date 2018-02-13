package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractMultimap$WrappedCollection.WrappedIterator;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultimap<K, V> implements Multimap<K, V>, Serializable {
    private static final long serialVersionUID = 2447537837011683357L;
    private transient Map<K, Collection<V>> asMap;
    private transient Collection<Entry<K, V>> entries;
    private transient Set<K> keySet;
    private transient Map<K, Collection<V>> map;
    private transient Multiset<K> multiset;
    private transient int totalSize;
    private transient Collection<V> valuesCollection;

    class C05451 extends Keys<K, V> {
        C05451() {
        }

        Multimap<K, V> multimap() {
            return AbstractMultimap.this;
        }
    }

    class C05462 extends Values<K, V> {
        C05462() {
        }

        Multimap<K, V> multimap() {
            return AbstractMultimap.this;
        }
    }

    class C05473 extends EntrySet<K, V> {
        C05473() {
        }

        public Iterator<Entry<K, V>> iterator() {
            return AbstractMultimap.this.createEntryIterator();
        }

        Multimap<K, V> multimap() {
            return AbstractMultimap.this;
        }
    }

    class C05484 extends Entries<K, V> {
        C05484() {
        }

        public Iterator<Entry<K, V>> iterator() {
            return AbstractMultimap.this.createEntryIterator();
        }

        Multimap<K, V> multimap() {
            return AbstractMultimap.this;
        }
    }

    private class AsMap extends AbstractMap<K, Collection<V>> {
        transient Set<Entry<K, Collection<V>>> entrySet;
        final transient Map<K, Collection<V>> submap;

        class AsMapEntries extends EntrySet<K, Collection<V>> {
            AsMapEntries() {
            }

            public boolean contains(Object obj) {
                return Collections2.safeContains(AsMap.this.submap.entrySet(), obj);
            }

            public Iterator<Entry<K, Collection<V>>> iterator() {
                return new AsMapIterator();
            }

            Map<K, Collection<V>> map() {
                return AsMap.this;
            }

            public boolean remove(Object obj) {
                if (!contains(obj)) {
                    return false;
                }
                AbstractMultimap.this.removeValuesForKey(((Entry) obj).getKey());
                return true;
            }
        }

        class AsMapIterator implements Iterator<Entry<K, Collection<V>>> {
            Collection<V> collection;
            final Iterator<Entry<K, Collection<V>>> delegateIterator = AsMap.this.submap.entrySet().iterator();

            AsMapIterator() {
            }

            public boolean hasNext() {
                return this.delegateIterator.hasNext();
            }

            public Entry<K, Collection<V>> next() {
                Entry entry = (Entry) this.delegateIterator.next();
                Object key = entry.getKey();
                this.collection = (Collection) entry.getValue();
                return Maps.immutableEntry(key, AbstractMultimap.this.wrapCollection(key, this.collection));
            }

            public void remove() {
                this.delegateIterator.remove();
                AbstractMultimap.access$220(AbstractMultimap.this, this.collection.size());
                this.collection.clear();
            }
        }

        AsMap(Map<K, Collection<V>> map) {
            this.submap = map;
        }

        public void clear() {
            if (this.submap == AbstractMultimap.this.map) {
                AbstractMultimap.this.clear();
            } else {
                Iterators.clear(new AsMapIterator());
            }
        }

        public boolean containsKey(Object obj) {
            return Maps.safeContainsKey(this.submap, obj);
        }

        public Set<Entry<K, Collection<V>>> entrySet() {
            Set<Entry<K, Collection<V>>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = new AsMapEntries();
            this.entrySet = set;
            return set;
        }

        public boolean equals(@Nullable Object obj) {
            return this == obj || this.submap.equals(obj);
        }

        public Collection<V> get(Object obj) {
            Collection collection = (Collection) Maps.safeGet(this.submap, obj);
            return collection == null ? null : AbstractMultimap.this.wrapCollection(obj, collection);
        }

        public int hashCode() {
            return this.submap.hashCode();
        }

        public Set<K> keySet() {
            return AbstractMultimap.this.keySet();
        }

        public Collection<V> remove(Object obj) {
            Collection collection = (Collection) this.submap.remove(obj);
            if (collection == null) {
                return null;
            }
            Collection<V> createCollection = AbstractMultimap.this.createCollection();
            createCollection.addAll(collection);
            AbstractMultimap.access$220(AbstractMultimap.this, collection.size());
            collection.clear();
            return createCollection;
        }

        public int size() {
            return this.submap.size();
        }

        public String toString() {
            return this.submap.toString();
        }
    }

    private class EntryIterator implements Iterator<Entry<K, V>> {
        Collection<V> collection;
        K key;
        final Iterator<Entry<K, Collection<V>>> keyIterator;
        Iterator<V> valueIterator;

        EntryIterator() {
            this.keyIterator = AbstractMultimap.this.map.entrySet().iterator();
            if (this.keyIterator.hasNext()) {
                findValueIteratorAndKey();
            } else {
                this.valueIterator = Iterators.emptyModifiableIterator();
            }
        }

        void findValueIteratorAndKey() {
            Entry entry = (Entry) this.keyIterator.next();
            this.key = entry.getKey();
            this.collection = (Collection) entry.getValue();
            this.valueIterator = this.collection.iterator();
        }

        public boolean hasNext() {
            return this.keyIterator.hasNext() || this.valueIterator.hasNext();
        }

        public Entry<K, V> next() {
            if (!this.valueIterator.hasNext()) {
                findValueIteratorAndKey();
            }
            return Maps.immutableEntry(this.key, this.valueIterator.next());
        }

        public void remove() {
            this.valueIterator.remove();
            if (this.collection.isEmpty()) {
                this.keyIterator.remove();
            }
            AbstractMultimap.this.totalSize = AbstractMultimap.this.totalSize - 1;
        }
    }

    private class KeySet extends KeySet<K, Collection<V>> {
        final Map<K, Collection<V>> subMap;

        class C05491 implements Iterator<K> {
            Entry<K, Collection<V>> entry;
            final Iterator<Entry<K, Collection<V>>> entryIterator = KeySet.this.subMap.entrySet().iterator();

            C05491() {
            }

            public boolean hasNext() {
                return this.entryIterator.hasNext();
            }

            public K next() {
                this.entry = (Entry) this.entryIterator.next();
                return this.entry.getKey();
            }

            public void remove() {
                Iterators.checkRemove(this.entry != null);
                Collection collection = (Collection) this.entry.getValue();
                this.entryIterator.remove();
                AbstractMultimap.access$220(AbstractMultimap.this, collection.size());
                collection.clear();
            }
        }

        KeySet(Map<K, Collection<V>> map) {
            this.subMap = map;
        }

        public void clear() {
            Iterators.clear(iterator());
        }

        public boolean containsAll(Collection<?> collection) {
            return this.subMap.keySet().containsAll(collection);
        }

        public boolean equals(@Nullable Object obj) {
            return this == obj || this.subMap.keySet().equals(obj);
        }

        public int hashCode() {
            return this.subMap.keySet().hashCode();
        }

        public Iterator<K> iterator() {
            return new C05491();
        }

        Map<K, Collection<V>> map() {
            return this.subMap;
        }

        public boolean remove(Object obj) {
            int i;
            Collection collection = (Collection) this.subMap.remove(obj);
            if (collection != null) {
                int size = collection.size();
                collection.clear();
                AbstractMultimap.access$220(AbstractMultimap.this, size);
                i = size;
            } else {
                i = 0;
            }
            return i > 0;
        }
    }

    private class WrappedCollection extends AbstractCollection<V> {
        final WrappedCollection ancestor;
        final Collection<V> ancestorDelegate;
        Collection<V> delegate;
        final K key;

        class WrappedIterator implements Iterator<V> {
            final Iterator<V> delegateIterator;
            final Collection<V> originalDelegate = WrappedCollection.this.delegate;

            WrappedIterator() {
                this.delegateIterator = AbstractMultimap.this.iteratorOrListIterator(WrappedCollection.this.delegate);
            }

            WrappedIterator(Iterator<V> it) {
                this.delegateIterator = it;
            }

            Iterator<V> getDelegateIterator() {
                validateIterator();
                return this.delegateIterator;
            }

            public boolean hasNext() {
                validateIterator();
                return this.delegateIterator.hasNext();
            }

            public V next() {
                validateIterator();
                return this.delegateIterator.next();
            }

            public void remove() {
                this.delegateIterator.remove();
                AbstractMultimap.this.totalSize = AbstractMultimap.this.totalSize - 1;
                WrappedCollection.this.removeIfEmpty();
            }

            void validateIterator() {
                WrappedCollection.this.refreshIfEmpty();
                if (WrappedCollection.this.delegate != this.originalDelegate) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        WrappedCollection(@Nullable K k, Collection<V> collection, @Nullable WrappedCollection wrappedCollection) {
            this.key = k;
            this.delegate = collection;
            this.ancestor = wrappedCollection;
            this.ancestorDelegate = wrappedCollection == null ? null : wrappedCollection.getDelegate();
        }

        public boolean add(V v) {
            refreshIfEmpty();
            boolean isEmpty = this.delegate.isEmpty();
            boolean add = this.delegate.add(v);
            if (add) {
                AbstractMultimap.this.totalSize = AbstractMultimap.this.totalSize + 1;
                if (isEmpty) {
                    addToMap();
                }
            }
            return add;
        }

        public boolean addAll(Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int size = size();
            boolean addAll = this.delegate.addAll(collection);
            if (!addAll) {
                return addAll;
            }
            AbstractMultimap.access$212(AbstractMultimap.this, this.delegate.size() - size);
            if (size != 0) {
                return addAll;
            }
            addToMap();
            return addAll;
        }

        void addToMap() {
            if (this.ancestor != null) {
                this.ancestor.addToMap();
            } else {
                AbstractMultimap.this.map.put(this.key, this.delegate);
            }
        }

        public void clear() {
            int size = size();
            if (size != 0) {
                this.delegate.clear();
                AbstractMultimap.access$220(AbstractMultimap.this, size);
                removeIfEmpty();
            }
        }

        public boolean contains(Object obj) {
            refreshIfEmpty();
            return this.delegate.contains(obj);
        }

        public boolean containsAll(Collection<?> collection) {
            refreshIfEmpty();
            return this.delegate.containsAll(collection);
        }

        public boolean equals(@Nullable Object obj) {
            if (obj == this) {
                return true;
            }
            refreshIfEmpty();
            return this.delegate.equals(obj);
        }

        WrappedCollection getAncestor() {
            return this.ancestor;
        }

        Collection<V> getDelegate() {
            return this.delegate;
        }

        K getKey() {
            return this.key;
        }

        public int hashCode() {
            refreshIfEmpty();
            return this.delegate.hashCode();
        }

        public Iterator<V> iterator() {
            refreshIfEmpty();
            return new WrappedIterator();
        }

        void refreshIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.refreshIfEmpty();
                if (this.ancestor.getDelegate() != this.ancestorDelegate) {
                    throw new ConcurrentModificationException();
                }
            } else if (this.delegate.isEmpty()) {
                Collection collection = (Collection) AbstractMultimap.this.map.get(this.key);
                if (collection != null) {
                    this.delegate = collection;
                }
            }
        }

        public boolean remove(Object obj) {
            refreshIfEmpty();
            boolean remove = this.delegate.remove(obj);
            if (remove) {
                AbstractMultimap.this.totalSize = AbstractMultimap.this.totalSize - 1;
                removeIfEmpty();
            }
            return remove;
        }

        public boolean removeAll(Collection<?> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int size = size();
            boolean removeAll = this.delegate.removeAll(collection);
            if (!removeAll) {
                return removeAll;
            }
            AbstractMultimap.access$212(AbstractMultimap.this, this.delegate.size() - size);
            removeIfEmpty();
            return removeAll;
        }

        void removeIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.removeIfEmpty();
            } else if (this.delegate.isEmpty()) {
                AbstractMultimap.this.map.remove(this.key);
            }
        }

        public boolean retainAll(Collection<?> collection) {
            Preconditions.checkNotNull(collection);
            int size = size();
            boolean retainAll = this.delegate.retainAll(collection);
            if (retainAll) {
                AbstractMultimap.access$212(AbstractMultimap.this, this.delegate.size() - size);
                removeIfEmpty();
            }
            return retainAll;
        }

        public int size() {
            refreshIfEmpty();
            return this.delegate.size();
        }

        public String toString() {
            refreshIfEmpty();
            return this.delegate.toString();
        }
    }

    private class WrappedList extends WrappedCollection implements List<V> {

        private class WrappedListIterator extends WrappedIterator implements ListIterator<V> {
            WrappedListIterator() {
                super();
            }

            public WrappedListIterator(int i) {
                super(WrappedList.this.getListDelegate().listIterator(i));
            }

            private ListIterator<V> getDelegateListIterator() {
                return (ListIterator) getDelegateIterator();
            }

            public void add(V v) {
                boolean isEmpty = WrappedList.this.isEmpty();
                getDelegateListIterator().add(v);
                AbstractMultimap.this.totalSize = AbstractMultimap.this.totalSize + 1;
                if (isEmpty) {
                    WrappedList.this.addToMap();
                }
            }

            public boolean hasPrevious() {
                return getDelegateListIterator().hasPrevious();
            }

            public int nextIndex() {
                return getDelegateListIterator().nextIndex();
            }

            public V previous() {
                return getDelegateListIterator().previous();
            }

            public int previousIndex() {
                return getDelegateListIterator().previousIndex();
            }

            public void set(V v) {
                getDelegateListIterator().set(v);
            }
        }

        WrappedList(@Nullable K k, List<V> list, @Nullable WrappedCollection wrappedCollection) {
            super(k, list, wrappedCollection);
        }

        public void add(int i, V v) {
            refreshIfEmpty();
            boolean isEmpty = getDelegate().isEmpty();
            getListDelegate().add(i, v);
            AbstractMultimap.this.totalSize = AbstractMultimap.this.totalSize + 1;
            if (isEmpty) {
                addToMap();
            }
        }

        public boolean addAll(int i, Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int size = size();
            boolean addAll = getListDelegate().addAll(i, collection);
            if (!addAll) {
                return addAll;
            }
            AbstractMultimap.access$212(AbstractMultimap.this, getDelegate().size() - size);
            if (size != 0) {
                return addAll;
            }
            addToMap();
            return addAll;
        }

        public V get(int i) {
            refreshIfEmpty();
            return getListDelegate().get(i);
        }

        List<V> getListDelegate() {
            return (List) getDelegate();
        }

        public int indexOf(Object obj) {
            refreshIfEmpty();
            return getListDelegate().indexOf(obj);
        }

        public int lastIndexOf(Object obj) {
            refreshIfEmpty();
            return getListDelegate().lastIndexOf(obj);
        }

        public ListIterator<V> listIterator() {
            refreshIfEmpty();
            return new WrappedListIterator();
        }

        public ListIterator<V> listIterator(int i) {
            refreshIfEmpty();
            return new WrappedListIterator(i);
        }

        public V remove(int i) {
            refreshIfEmpty();
            V remove = getListDelegate().remove(i);
            AbstractMultimap.this.totalSize = AbstractMultimap.this.totalSize - 1;
            removeIfEmpty();
            return remove;
        }

        public V set(int i, V v) {
            refreshIfEmpty();
            return getListDelegate().set(i, v);
        }

        public List<V> subList(int i, int i2) {
            WrappedCollection ancestor;
            refreshIfEmpty();
            AbstractMultimap abstractMultimap = AbstractMultimap.this;
            Object key = getKey();
            List subList = getListDelegate().subList(i, i2);
            if (getAncestor() != null) {
                ancestor = getAncestor();
            }
            return abstractMultimap.wrapList(key, subList, ancestor);
        }
    }

    private class RandomAccessWrappedList extends WrappedList implements RandomAccess {
        RandomAccessWrappedList(@Nullable K k, List<V> list, @Nullable WrappedCollection wrappedCollection) {
            super(k, list, wrappedCollection);
        }
    }

    private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
        SortedSet<K> sortedKeySet;

        SortedAsMap(SortedMap<K, Collection<V>> sortedMap) {
            super(sortedMap);
        }

        public Comparator<? super K> comparator() {
            return sortedMap().comparator();
        }

        public K firstKey() {
            return sortedMap().firstKey();
        }

        public SortedMap<K, Collection<V>> headMap(K k) {
            return new SortedAsMap(sortedMap().headMap(k));
        }

        public SortedSet<K> keySet() {
            SortedSet<K> sortedSet = this.sortedKeySet;
            if (sortedSet != null) {
                return sortedSet;
            }
            sortedSet = new SortedKeySet(sortedMap());
            this.sortedKeySet = sortedSet;
            return sortedSet;
        }

        public K lastKey() {
            return sortedMap().lastKey();
        }

        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap) this.submap;
        }

        public SortedMap<K, Collection<V>> subMap(K k, K k2) {
            return new SortedAsMap(sortedMap().subMap(k, k2));
        }

        public SortedMap<K, Collection<V>> tailMap(K k) {
            return new SortedAsMap(sortedMap().tailMap(k));
        }
    }

    private class SortedKeySet extends KeySet implements SortedSet<K> {
        SortedKeySet(SortedMap<K, Collection<V>> sortedMap) {
            super(sortedMap);
        }

        public Comparator<? super K> comparator() {
            return sortedMap().comparator();
        }

        public K first() {
            return sortedMap().firstKey();
        }

        public SortedSet<K> headSet(K k) {
            return new SortedKeySet(sortedMap().headMap(k));
        }

        public K last() {
            return sortedMap().lastKey();
        }

        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap) this.subMap;
        }

        public SortedSet<K> subSet(K k, K k2) {
            return new SortedKeySet(sortedMap().subMap(k, k2));
        }

        public SortedSet<K> tailSet(K k) {
            return new SortedKeySet(sortedMap().tailMap(k));
        }
    }

    private class WrappedSet extends WrappedCollection implements Set<V> {
        WrappedSet(@Nullable K k, Set<V> set) {
            super(k, set, null);
        }

        public boolean removeAll(Collection<?> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int size = size();
            boolean removeAllImpl = Sets.removeAllImpl((Set) this.delegate, (Collection) collection);
            if (!removeAllImpl) {
                return removeAllImpl;
            }
            AbstractMultimap.access$212(AbstractMultimap.this, this.delegate.size() - size);
            removeIfEmpty();
            return removeAllImpl;
        }
    }

    private class WrappedSortedSet extends WrappedCollection implements SortedSet<V> {
        WrappedSortedSet(@Nullable K k, SortedSet<V> sortedSet, @Nullable WrappedCollection wrappedCollection) {
            super(k, sortedSet, wrappedCollection);
        }

        public Comparator<? super V> comparator() {
            return getSortedSetDelegate().comparator();
        }

        public V first() {
            refreshIfEmpty();
            return getSortedSetDelegate().first();
        }

        SortedSet<V> getSortedSetDelegate() {
            return (SortedSet) getDelegate();
        }

        public SortedSet<V> headSet(V v) {
            WrappedCollection ancestor;
            refreshIfEmpty();
            AbstractMultimap abstractMultimap = AbstractMultimap.this;
            Object key = getKey();
            SortedSet headSet = getSortedSetDelegate().headSet(v);
            if (getAncestor() != null) {
                ancestor = getAncestor();
            }
            return new WrappedSortedSet(key, headSet, ancestor);
        }

        public V last() {
            refreshIfEmpty();
            return getSortedSetDelegate().last();
        }

        public SortedSet<V> subSet(V v, V v2) {
            WrappedCollection ancestor;
            refreshIfEmpty();
            AbstractMultimap abstractMultimap = AbstractMultimap.this;
            Object key = getKey();
            SortedSet subSet = getSortedSetDelegate().subSet(v, v2);
            if (getAncestor() != null) {
                ancestor = getAncestor();
            }
            return new WrappedSortedSet(key, subSet, ancestor);
        }

        public SortedSet<V> tailSet(V v) {
            WrappedCollection ancestor;
            refreshIfEmpty();
            AbstractMultimap abstractMultimap = AbstractMultimap.this;
            Object key = getKey();
            SortedSet tailSet = getSortedSetDelegate().tailSet(v);
            if (getAncestor() != null) {
                ancestor = getAncestor();
            }
            return new WrappedSortedSet(key, tailSet, ancestor);
        }
    }

    protected AbstractMultimap(Map<K, Collection<V>> map) {
        Preconditions.checkArgument(map.isEmpty());
        this.map = map;
    }

    static /* synthetic */ int access$212(AbstractMultimap abstractMultimap, int i) {
        int i2 = abstractMultimap.totalSize + i;
        abstractMultimap.totalSize = i2;
        return i2;
    }

    static /* synthetic */ int access$220(AbstractMultimap abstractMultimap, int i) {
        int i2 = abstractMultimap.totalSize - i;
        abstractMultimap.totalSize = i2;
        return i2;
    }

    private Map<K, Collection<V>> createAsMap() {
        return this.map instanceof SortedMap ? new SortedAsMap((SortedMap) this.map) : new AsMap(this.map);
    }

    private Set<K> createKeySet() {
        return this.map instanceof SortedMap ? new SortedKeySet((SortedMap) this.map) : new KeySet(this.map);
    }

    private Collection<V> getOrCreateCollection(@Nullable K k) {
        Collection<V> collection = (Collection) this.map.get(k);
        if (collection != null) {
            return collection;
        }
        collection = createCollection(k);
        this.map.put(k, collection);
        return collection;
    }

    private Iterator<V> iteratorOrListIterator(Collection<V> collection) {
        return collection instanceof List ? ((List) collection).listIterator() : collection.iterator();
    }

    private int removeValuesForKey(Object obj) {
        int i = 0;
        try {
            Collection collection = (Collection) this.map.remove(obj);
            if (collection != null) {
                i = collection.size();
                collection.clear();
                this.totalSize -= i;
            }
            return i;
        } catch (NullPointerException e) {
            return 0;
        } catch (ClassCastException e2) {
            return 0;
        }
    }

    private Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
        return collection instanceof SortedSet ? Collections.unmodifiableSortedSet((SortedSet) collection) : collection instanceof Set ? Collections.unmodifiableSet((Set) collection) : collection instanceof List ? Collections.unmodifiableList((List) collection) : Collections.unmodifiableCollection(collection);
    }

    private Collection<V> wrapCollection(@Nullable K k, Collection<V> collection) {
        return collection instanceof SortedSet ? new WrappedSortedSet(k, (SortedSet) collection, null) : collection instanceof Set ? new WrappedSet(k, (Set) collection) : collection instanceof List ? wrapList(k, (List) collection, null) : new WrappedCollection(k, collection, null);
    }

    private List<V> wrapList(@Nullable K k, List<V> list, @Nullable WrappedCollection wrappedCollection) {
        return list instanceof RandomAccess ? new RandomAccessWrappedList(k, list, wrappedCollection) : new WrappedList(k, list, wrappedCollection);
    }

    public Map<K, Collection<V>> asMap() {
        Map<K, Collection<V>> map = this.asMap;
        if (map != null) {
            return map;
        }
        map = createAsMap();
        this.asMap = map;
        return map;
    }

    Map<K, Collection<V>> backingMap() {
        return this.map;
    }

    public void clear() {
        for (Collection clear : this.map.values()) {
            clear.clear();
        }
        this.map.clear();
        this.totalSize = 0;
    }

    public boolean containsEntry(@Nullable Object obj, @Nullable Object obj2) {
        Collection collection = (Collection) this.map.get(obj);
        return collection != null && collection.contains(obj2);
    }

    public boolean containsKey(@Nullable Object obj) {
        return this.map.containsKey(obj);
    }

    public boolean containsValue(@Nullable Object obj) {
        for (Collection contains : this.map.values()) {
            if (contains.contains(obj)) {
                return true;
            }
        }
        return false;
    }

    abstract Collection<V> createCollection();

    Collection<V> createCollection(@Nullable K k) {
        return createCollection();
    }

    Collection<Entry<K, V>> createEntries() {
        return this instanceof SetMultimap ? new C05473() : new C05484();
    }

    Iterator<Entry<K, V>> createEntryIterator() {
        return new EntryIterator();
    }

    public Collection<Entry<K, V>> entries() {
        Collection<Entry<K, V>> collection = this.entries;
        if (collection != null) {
            return collection;
        }
        collection = createEntries();
        this.entries = collection;
        return collection;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Multimap)) {
            return false;
        }
        return this.map.equals(((Multimap) obj).asMap());
    }

    public Collection<V> get(@Nullable K k) {
        Collection collection = (Collection) this.map.get(k);
        if (collection == null) {
            collection = createCollection(k);
        }
        return wrapCollection(k, collection);
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public boolean isEmpty() {
        return this.totalSize == 0;
    }

    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        set = createKeySet();
        this.keySet = set;
        return set;
    }

    public Multiset<K> keys() {
        Multiset<K> multiset = this.multiset;
        if (multiset != null) {
            return multiset;
        }
        multiset = new C05451();
        this.multiset = multiset;
        return multiset;
    }

    public boolean put(@Nullable K k, @Nullable V v) {
        Collection collection = (Collection) this.map.get(k);
        if (collection == null) {
            collection = createCollection(k);
            if (collection.add(v)) {
                this.totalSize++;
                this.map.put(k, collection);
                return true;
            }
            throw new AssertionError("New Collection violated the Collection spec");
        } else if (!collection.add(v)) {
            return false;
        } else {
            this.totalSize++;
            return true;
        }
    }

    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        boolean z = false;
        for (Entry entry : multimap.entries()) {
            z = put(entry.getKey(), entry.getValue()) | z;
        }
        return z;
    }

    public boolean putAll(@Nullable K k, Iterable<? extends V> iterable) {
        boolean z = false;
        if (iterable.iterator().hasNext()) {
            Collection orCreateCollection = getOrCreateCollection(k);
            int size = orCreateCollection.size();
            if (iterable instanceof Collection) {
                z = orCreateCollection.addAll(Collections2.cast(iterable));
            } else {
                for (Object add : iterable) {
                    z |= orCreateCollection.add(add);
                }
            }
            this.totalSize = (orCreateCollection.size() - size) + this.totalSize;
        }
        return z;
    }

    public boolean remove(@Nullable Object obj, @Nullable Object obj2) {
        Collection collection = (Collection) this.map.get(obj);
        if (collection == null) {
            return false;
        }
        boolean remove = collection.remove(obj2);
        if (remove) {
            this.totalSize--;
            if (collection.isEmpty()) {
                this.map.remove(obj);
                return remove;
            }
        }
        return remove;
    }

    public Collection<V> removeAll(@Nullable Object obj) {
        Collection collection = (Collection) this.map.remove(obj);
        Collection createCollection = createCollection();
        if (collection != null) {
            createCollection.addAll(collection);
            this.totalSize -= collection.size();
            collection.clear();
        }
        return unmodifiableCollectionSubclass(createCollection);
    }

    public Collection<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
        Iterator it = iterable.iterator();
        if (!it.hasNext()) {
            return removeAll(k);
        }
        Collection orCreateCollection = getOrCreateCollection(k);
        Collection createCollection = createCollection();
        createCollection.addAll(orCreateCollection);
        this.totalSize -= orCreateCollection.size();
        orCreateCollection.clear();
        while (it.hasNext()) {
            if (orCreateCollection.add(it.next())) {
                this.totalSize++;
            }
        }
        return unmodifiableCollectionSubclass(createCollection);
    }

    final void setMap(Map<K, Collection<V>> map) {
        this.map = map;
        this.totalSize = 0;
        for (Collection collection : map.values()) {
            Preconditions.checkArgument(!collection.isEmpty());
            this.totalSize = collection.size() + this.totalSize;
        }
    }

    public int size() {
        return this.totalSize;
    }

    public String toString() {
        return this.map.toString();
    }

    public Collection<V> values() {
        Collection<V> collection = this.valuesCollection;
        if (collection != null) {
            return collection;
        }
        collection = new C05462();
        this.valuesCollection = collection;
        return collection;
    }
}
