package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Table.Cell;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
class StandardTable<R, C, V> implements Table<R, C, V>, Serializable {
    private static final long serialVersionUID = 0;
    @GwtTransient
    final Map<R, Map<C, V>> backingMap;
    private transient CellSet cellSet;
    private transient Set<C> columnKeySet;
    private transient ColumnMap columnMap;
    @GwtTransient
    final Supplier<? extends Map<C, V>> factory;
    private transient RowKeySet rowKeySet;
    private transient RowMap rowMap;
    private transient Values values;

    private abstract class TableSet<T> extends AbstractSet<T> {
        private TableSet() {
        }

        public void clear() {
            StandardTable.this.backingMap.clear();
        }

        public boolean isEmpty() {
            return StandardTable.this.backingMap.isEmpty();
        }
    }

    class RowKeySet extends TableSet<R> {
        RowKeySet() {
            super();
        }

        public boolean contains(Object obj) {
            return StandardTable.this.containsRow(obj);
        }

        public Iterator<R> iterator() {
            return Maps.keyIterator(StandardTable.this.rowMap().entrySet().iterator());
        }

        public boolean remove(Object obj) {
            return (obj == null || StandardTable.this.backingMap.remove(obj) == null) ? false : true;
        }

        public int size() {
            return StandardTable.this.backingMap.size();
        }
    }

    class RowMap extends ImprovedAbstractMap<R, Map<C, V>> {

        class EntrySet extends TableSet<Entry<R, Map<C, V>>> {
            EntrySet() {
                super();
            }

            public boolean contains(Object obj) {
                if (!(obj instanceof Entry)) {
                    return false;
                }
                Entry entry = (Entry) obj;
                return entry.getKey() != null && (entry.getValue() instanceof Map) && Collections2.safeContains(StandardTable.this.backingMap.entrySet(), entry);
            }

            public Iterator<Entry<R, Map<C, V>>> iterator() {
                return new TransformedIterator<R, Entry<R, Map<C, V>>>(StandardTable.this.backingMap.keySet().iterator()) {
                    Entry<R, Map<C, V>> transform(R r) {
                        return new ImmutableEntry(r, StandardTable.this.row(r));
                    }
                };
            }

            public boolean remove(Object obj) {
                if (!(obj instanceof Entry)) {
                    return false;
                }
                Entry entry = (Entry) obj;
                return entry.getKey() != null && (entry.getValue() instanceof Map) && StandardTable.this.backingMap.entrySet().remove(entry);
            }

            public int size() {
                return StandardTable.this.backingMap.size();
            }
        }

        RowMap() {
        }

        public boolean containsKey(Object obj) {
            return StandardTable.this.containsRow(obj);
        }

        protected Set<Entry<R, Map<C, V>>> createEntrySet() {
            return new EntrySet();
        }

        public Map<C, V> get(Object obj) {
            return StandardTable.this.containsRow(obj) ? StandardTable.this.row(obj) : null;
        }

        public Set<R> keySet() {
            return StandardTable.this.rowKeySet();
        }

        public Map<C, V> remove(Object obj) {
            return obj == null ? null : (Map) StandardTable.this.backingMap.remove(obj);
        }
    }

    private class CellIterator implements Iterator<Cell<R, C, V>> {
        Iterator<Entry<C, V>> columnIterator;
        Entry<R, Map<C, V>> rowEntry;
        final Iterator<Entry<R, Map<C, V>>> rowIterator;

        private CellIterator() {
            this.rowIterator = StandardTable.this.backingMap.entrySet().iterator();
            this.columnIterator = Iterators.emptyModifiableIterator();
        }

        public boolean hasNext() {
            return this.rowIterator.hasNext() || this.columnIterator.hasNext();
        }

        public Cell<R, C, V> next() {
            if (!this.columnIterator.hasNext()) {
                this.rowEntry = (Entry) this.rowIterator.next();
                this.columnIterator = ((Map) this.rowEntry.getValue()).entrySet().iterator();
            }
            Entry entry = (Entry) this.columnIterator.next();
            return Tables.immutableCell(this.rowEntry.getKey(), entry.getKey(), entry.getValue());
        }

        public void remove() {
            this.columnIterator.remove();
            if (((Map) this.rowEntry.getValue()).isEmpty()) {
                this.rowIterator.remove();
            }
        }
    }

    private class CellSet extends TableSet<Cell<R, C, V>> {
        private CellSet() {
            super();
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Cell)) {
                return false;
            }
            Cell cell = (Cell) obj;
            return StandardTable.this.containsMapping(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }

        public Iterator<Cell<R, C, V>> iterator() {
            return new CellIterator();
        }

        public boolean remove(Object obj) {
            if (!(obj instanceof Cell)) {
                return false;
            }
            Cell cell = (Cell) obj;
            return StandardTable.this.removeMapping(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }

        public int size() {
            return StandardTable.this.size();
        }
    }

    private class Column extends ImprovedAbstractMap<R, V> {
        final C columnKey;
        com.google.common.collect.StandardTable$Column.Values columnValues;
        com.google.common.collect.StandardTable$Column.KeySet keySet;

        class EntrySet extends ImprovedAbstractSet<Entry<R, V>> {
            EntrySet() {
            }

            public void clear() {
                Column.this.removePredicate(Predicates.alwaysTrue());
            }

            public boolean contains(Object obj) {
                if (!(obj instanceof Entry)) {
                    return false;
                }
                Entry entry = (Entry) obj;
                return StandardTable.this.containsMapping(entry.getKey(), Column.this.columnKey, entry.getValue());
            }

            public boolean isEmpty() {
                return !StandardTable.this.containsColumn(Column.this.columnKey);
            }

            public Iterator<Entry<R, V>> iterator() {
                return new EntrySetIterator();
            }

            public boolean remove(Object obj) {
                if (!(obj instanceof Entry)) {
                    return false;
                }
                Entry entry = (Entry) obj;
                return StandardTable.this.removeMapping(entry.getKey(), Column.this.columnKey, entry.getValue());
            }

            public boolean retainAll(Collection<?> collection) {
                return Column.this.removePredicate(Predicates.not(Predicates.in(collection)));
            }

            public int size() {
                int i = 0;
                for (Map containsKey : StandardTable.this.backingMap.values()) {
                    if (containsKey.containsKey(Column.this.columnKey)) {
                        i++;
                    }
                }
                return i;
            }
        }

        class EntrySetIterator extends AbstractIterator<Entry<R, V>> {
            final Iterator<Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();

            EntrySetIterator() {
            }

            protected Entry<R, V> computeNext() {
                while (this.iterator.hasNext()) {
                    final Entry entry = (Entry) this.iterator.next();
                    if (((Map) entry.getValue()).containsKey(Column.this.columnKey)) {
                        return new AbstractMapEntry<R, V>() {
                            public R getKey() {
                                return entry.getKey();
                            }

                            public V getValue() {
                                return ((Map) entry.getValue()).get(Column.this.columnKey);
                            }

                            public V setValue(V v) {
                                return ((Map) entry.getValue()).put(Column.this.columnKey, Preconditions.checkNotNull(v));
                            }
                        };
                    }
                }
                return (Entry) endOfData();
            }
        }

        class KeySet extends ImprovedAbstractSet<R> {
            KeySet() {
            }

            public void clear() {
                Column.this.entrySet().clear();
            }

            public boolean contains(Object obj) {
                return StandardTable.this.contains(obj, Column.this.columnKey);
            }

            public boolean isEmpty() {
                return !StandardTable.this.containsColumn(Column.this.columnKey);
            }

            public Iterator<R> iterator() {
                return Maps.keyIterator(Column.this.entrySet().iterator());
            }

            public boolean remove(Object obj) {
                return StandardTable.this.remove(obj, Column.this.columnKey) != null;
            }

            public boolean retainAll(final Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                return Column.this.removePredicate(new Predicate<Entry<R, V>>() {
                    public boolean apply(Entry<R, V> entry) {
                        return !collection.contains(entry.getKey());
                    }
                });
            }

            public int size() {
                return Column.this.entrySet().size();
            }
        }

        class Values extends AbstractCollection<V> {
            Values() {
            }

            public void clear() {
                Column.this.entrySet().clear();
            }

            public boolean isEmpty() {
                return !StandardTable.this.containsColumn(Column.this.columnKey);
            }

            public Iterator<V> iterator() {
                return Maps.valueIterator(Column.this.entrySet().iterator());
            }

            public boolean remove(Object obj) {
                if (obj != null) {
                    Iterator it = StandardTable.this.backingMap.values().iterator();
                    while (it.hasNext()) {
                        Map map = (Map) it.next();
                        if (map.entrySet().remove(new ImmutableEntry(Column.this.columnKey, obj))) {
                            if (map.isEmpty()) {
                                it.remove();
                            }
                            return true;
                        }
                    }
                }
                return false;
            }

            public boolean removeAll(final Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                return Column.this.removePredicate(new Predicate<Entry<R, V>>() {
                    public boolean apply(Entry<R, V> entry) {
                        return collection.contains(entry.getValue());
                    }
                });
            }

            public boolean retainAll(final Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                return Column.this.removePredicate(new Predicate<Entry<R, V>>() {
                    public boolean apply(Entry<R, V> entry) {
                        return !collection.contains(entry.getValue());
                    }
                });
            }

            public int size() {
                return Column.this.entrySet().size();
            }
        }

        Column(C c) {
            this.columnKey = Preconditions.checkNotNull(c);
        }

        public boolean containsKey(Object obj) {
            return StandardTable.this.contains(obj, this.columnKey);
        }

        public Set<Entry<R, V>> createEntrySet() {
            return new EntrySet();
        }

        public V get(Object obj) {
            return StandardTable.this.get(obj, this.columnKey);
        }

        public Set<R> keySet() {
            Set set = this.keySet;
            if (set != null) {
                return set;
            }
            set = new KeySet();
            this.keySet = set;
            return set;
        }

        public V put(R r, V v) {
            return StandardTable.this.put(r, this.columnKey, v);
        }

        public V remove(Object obj) {
            return StandardTable.this.remove(obj, this.columnKey);
        }

        boolean removePredicate(Predicate<? super Entry<R, V>> predicate) {
            Iterator it = StandardTable.this.backingMap.entrySet().iterator();
            boolean z = false;
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                Map map = (Map) entry.getValue();
                Object obj = map.get(this.columnKey);
                if (obj != null && predicate.apply(new ImmutableEntry(entry.getKey(), obj))) {
                    map.remove(this.columnKey);
                    if (map.isEmpty()) {
                        it.remove();
                        z = true;
                    } else {
                        z = true;
                    }
                }
            }
            return z;
        }

        public Collection<V> values() {
            Collection collection = this.columnValues;
            if (collection != null) {
                return collection;
            }
            collection = new Values();
            this.columnValues = collection;
            return collection;
        }
    }

    private class ColumnKeyIterator extends AbstractIterator<C> {
        Iterator<Entry<C, V>> entryIterator;
        final Iterator<Map<C, V>> mapIterator;
        final Map<C, V> seen;

        private ColumnKeyIterator() {
            this.seen = (Map) StandardTable.this.factory.get();
            this.mapIterator = StandardTable.this.backingMap.values().iterator();
            this.entryIterator = Iterators.emptyIterator();
        }

        protected C computeNext() {
            while (true) {
                if (this.entryIterator.hasNext()) {
                    Entry entry = (Entry) this.entryIterator.next();
                    if (!this.seen.containsKey(entry.getKey())) {
                        this.seen.put(entry.getKey(), entry.getValue());
                        return entry.getKey();
                    }
                } else if (!this.mapIterator.hasNext()) {
                    return endOfData();
                } else {
                    this.entryIterator = ((Map) this.mapIterator.next()).entrySet().iterator();
                }
            }
        }
    }

    private class ColumnKeySet extends TableSet<C> {
        private ColumnKeySet() {
            super();
        }

        public boolean contains(Object obj) {
            if (obj != null) {
                for (Map containsKey : StandardTable.this.backingMap.values()) {
                    if (containsKey.containsKey(obj)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public Iterator<C> iterator() {
            return StandardTable.this.createColumnKeyIterator();
        }

        public boolean remove(Object obj) {
            if (obj == null) {
                return false;
            }
            Iterator it = StandardTable.this.backingMap.values().iterator();
            boolean z = false;
            while (it.hasNext()) {
                Map map = (Map) it.next();
                if (map.keySet().remove(obj)) {
                    z = true;
                    if (map.isEmpty()) {
                        it.remove();
                    }
                }
            }
            return z;
        }

        public boolean removeAll(Collection<?> collection) {
            Preconditions.checkNotNull(collection);
            Iterator it = StandardTable.this.backingMap.values().iterator();
            boolean z = false;
            while (it.hasNext()) {
                Map map = (Map) it.next();
                if (Iterators.removeAll(map.keySet().iterator(), collection)) {
                    z = true;
                    if (map.isEmpty()) {
                        it.remove();
                    }
                }
            }
            return z;
        }

        public boolean retainAll(Collection<?> collection) {
            Preconditions.checkNotNull(collection);
            Iterator it = StandardTable.this.backingMap.values().iterator();
            boolean z = false;
            while (it.hasNext()) {
                Map map = (Map) it.next();
                if (map.keySet().retainAll(collection)) {
                    z = true;
                    if (map.isEmpty()) {
                        it.remove();
                    }
                }
            }
            return z;
        }

        public int size() {
            return Iterators.size(iterator());
        }
    }

    private abstract class TableCollection<T> extends AbstractCollection<T> {
        private TableCollection() {
        }

        public void clear() {
            StandardTable.this.backingMap.clear();
        }

        public boolean isEmpty() {
            return StandardTable.this.backingMap.isEmpty();
        }
    }

    private class ColumnMap extends ImprovedAbstractMap<C, Map<R, V>> {
        com.google.common.collect.StandardTable$ColumnMap.ColumnMapValues columnMapValues;

        class ColumnMapEntrySet extends TableSet<Entry<C, Map<R, V>>> {
            ColumnMapEntrySet() {
                super();
            }

            public boolean contains(Object obj) {
                if (obj instanceof Entry) {
                    Entry entry = (Entry) obj;
                    if (StandardTable.this.containsColumn(entry.getKey())) {
                        return ColumnMap.this.get(entry.getKey()).equals(entry.getValue());
                    }
                }
                return false;
            }

            public Iterator<Entry<C, Map<R, V>>> iterator() {
                return new TransformedIterator<C, Entry<C, Map<R, V>>>(StandardTable.this.columnKeySet().iterator()) {
                    Entry<C, Map<R, V>> transform(C c) {
                        return new ImmutableEntry(c, StandardTable.this.column(c));
                    }
                };
            }

            public boolean remove(Object obj) {
                if (!contains(obj)) {
                    return false;
                }
                StandardTable.this.removeColumn(((Entry) obj).getKey());
                return true;
            }

            public boolean removeAll(Collection<?> collection) {
                boolean z = false;
                for (Object remove : collection) {
                    z |= remove(remove);
                }
                return z;
            }

            public boolean retainAll(Collection<?> collection) {
                boolean z = false;
                Iterator it = Lists.newArrayList(StandardTable.this.columnKeySet().iterator()).iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    if (!collection.contains(new ImmutableEntry(next, StandardTable.this.column(next)))) {
                        StandardTable.this.removeColumn(next);
                        z = true;
                    }
                }
                return z;
            }

            public int size() {
                return StandardTable.this.columnKeySet().size();
            }
        }

        private class ColumnMapValues extends TableCollection<Map<R, V>> {
            private ColumnMapValues() {
                super();
            }

            public Iterator<Map<R, V>> iterator() {
                return Maps.valueIterator(ColumnMap.this.entrySet().iterator());
            }

            public boolean remove(Object obj) {
                for (Entry entry : ColumnMap.this.entrySet()) {
                    if (((Map) entry.getValue()).equals(obj)) {
                        StandardTable.this.removeColumn(entry.getKey());
                        return true;
                    }
                }
                return false;
            }

            public boolean removeAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                boolean z = false;
                Iterator it = Lists.newArrayList(StandardTable.this.columnKeySet().iterator()).iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    if (collection.contains(StandardTable.this.column(next))) {
                        StandardTable.this.removeColumn(next);
                        z = true;
                    }
                }
                return z;
            }

            public boolean retainAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                boolean z = false;
                Iterator it = Lists.newArrayList(StandardTable.this.columnKeySet().iterator()).iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    if (!collection.contains(StandardTable.this.column(next))) {
                        StandardTable.this.removeColumn(next);
                        z = true;
                    }
                }
                return z;
            }

            public int size() {
                return StandardTable.this.columnKeySet().size();
            }
        }

        private ColumnMap() {
        }

        public boolean containsKey(Object obj) {
            return StandardTable.this.containsColumn(obj);
        }

        public Set<Entry<C, Map<R, V>>> createEntrySet() {
            return new ColumnMapEntrySet();
        }

        public Map<R, V> get(Object obj) {
            return StandardTable.this.containsColumn(obj) ? StandardTable.this.column(obj) : null;
        }

        public Set<C> keySet() {
            return StandardTable.this.columnKeySet();
        }

        public Map<R, V> remove(Object obj) {
            return StandardTable.this.containsColumn(obj) ? StandardTable.this.removeColumn(obj) : null;
        }

        public Collection<Map<R, V>> values() {
            Collection collection = this.columnMapValues;
            if (collection != null) {
                return collection;
            }
            collection = new ColumnMapValues();
            this.columnMapValues = collection;
            return collection;
        }
    }

    class Row extends AbstractMap<C, V> {
        Map<C, V> backingRowMap;
        Set<Entry<C, V>> entrySet;
        Set<C> keySet;
        final R rowKey;

        class C07631 extends KeySet<C, V> {
            C07631() {
            }

            Map<C, V> map() {
                return Row.this;
            }
        }

        private class RowEntrySet extends EntrySet<C, V> {
            private RowEntrySet() {
            }

            public Iterator<Entry<C, V>> iterator() {
                Map backingRowMap = Row.this.backingRowMap();
                if (backingRowMap == null) {
                    return Iterators.emptyModifiableIterator();
                }
                final Iterator it = backingRowMap.entrySet().iterator();
                return new Iterator<Entry<C, V>>() {
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    public Entry<C, V> next() {
                        final Entry entry = (Entry) it.next();
                        return new ForwardingMapEntry<C, V>() {
                            protected Entry<C, V> delegate() {
                                return entry;
                            }

                            public boolean equals(Object obj) {
                                return standardEquals(obj);
                            }

                            public V setValue(V v) {
                                return super.setValue(Preconditions.checkNotNull(v));
                            }
                        };
                    }

                    public void remove() {
                        it.remove();
                        Row.this.maintainEmptyInvariant();
                    }
                };
            }

            Map<C, V> map() {
                return Row.this;
            }

            public int size() {
                Map backingRowMap = Row.this.backingRowMap();
                return backingRowMap == null ? 0 : backingRowMap.size();
            }
        }

        Row(R r) {
            this.rowKey = Preconditions.checkNotNull(r);
        }

        Map<C, V> backingRowMap() {
            if (this.backingRowMap != null && (!this.backingRowMap.isEmpty() || !StandardTable.this.backingMap.containsKey(this.rowKey))) {
                return this.backingRowMap;
            }
            Map<C, V> computeBackingRowMap = computeBackingRowMap();
            this.backingRowMap = computeBackingRowMap;
            return computeBackingRowMap;
        }

        public void clear() {
            Map backingRowMap = backingRowMap();
            if (backingRowMap != null) {
                backingRowMap.clear();
            }
            maintainEmptyInvariant();
        }

        Map<C, V> computeBackingRowMap() {
            return (Map) StandardTable.this.backingMap.get(this.rowKey);
        }

        public boolean containsKey(Object obj) {
            Map backingRowMap = backingRowMap();
            return (obj == null || backingRowMap == null || !Maps.safeContainsKey(backingRowMap, obj)) ? false : true;
        }

        public Set<Entry<C, V>> entrySet() {
            Set<Entry<C, V>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            set = new RowEntrySet();
            this.entrySet = set;
            return set;
        }

        public V get(Object obj) {
            Map backingRowMap = backingRowMap();
            return (obj == null || backingRowMap == null) ? null : Maps.safeGet(backingRowMap, obj);
        }

        public Set<C> keySet() {
            Set<C> set = this.keySet;
            if (set != null) {
                return set;
            }
            set = new C07631();
            this.keySet = set;
            return set;
        }

        void maintainEmptyInvariant() {
            if (backingRowMap() != null && this.backingRowMap.isEmpty()) {
                StandardTable.this.backingMap.remove(this.rowKey);
                this.backingRowMap = null;
            }
        }

        public V put(C c, V v) {
            Preconditions.checkNotNull(c);
            Preconditions.checkNotNull(v);
            return (this.backingRowMap == null || this.backingRowMap.isEmpty()) ? StandardTable.this.put(this.rowKey, c, v) : this.backingRowMap.put(c, v);
        }

        public V remove(Object obj) {
            try {
                Map backingRowMap = backingRowMap();
                if (backingRowMap == null) {
                    return null;
                }
                V remove = backingRowMap.remove(obj);
                maintainEmptyInvariant();
                return remove;
            } catch (ClassCastException e) {
                return null;
            }
        }
    }

    private class Values extends TableCollection<V> {
        private Values() {
            super();
        }

        public Iterator<V> iterator() {
            return new TransformedIterator<Cell<R, C, V>, V>(StandardTable.this.cellSet().iterator()) {
                V transform(Cell<R, C, V> cell) {
                    return cell.getValue();
                }
            };
        }

        public int size() {
            return StandardTable.this.size();
        }
    }

    StandardTable(Map<R, Map<C, V>> map, Supplier<? extends Map<C, V>> supplier) {
        this.backingMap = map;
        this.factory = supplier;
    }

    private boolean containsMapping(Object obj, Object obj2, Object obj3) {
        return obj3 != null && obj3.equals(get(obj, obj2));
    }

    private Map<C, V> getOrCreate(R r) {
        Map<C, V> map = (Map) this.backingMap.get(r);
        if (map != null) {
            return map;
        }
        map = (Map) this.factory.get();
        this.backingMap.put(r, map);
        return map;
    }

    private Map<R, V> removeColumn(Object obj) {
        Map<R, V> linkedHashMap = new LinkedHashMap();
        Iterator it = this.backingMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            Object remove = ((Map) entry.getValue()).remove(obj);
            if (remove != null) {
                linkedHashMap.put(entry.getKey(), remove);
                if (((Map) entry.getValue()).isEmpty()) {
                    it.remove();
                }
            }
        }
        return linkedHashMap;
    }

    private boolean removeMapping(Object obj, Object obj2, Object obj3) {
        if (!containsMapping(obj, obj2, obj3)) {
            return false;
        }
        remove(obj, obj2);
        return true;
    }

    public Set<Cell<R, C, V>> cellSet() {
        Set set = this.cellSet;
        if (set != null) {
            return set;
        }
        set = new CellSet();
        this.cellSet = set;
        return set;
    }

    public void clear() {
        this.backingMap.clear();
    }

    public Map<R, V> column(C c) {
        return new Column(c);
    }

    public Set<C> columnKeySet() {
        Set<C> set = this.columnKeySet;
        if (set != null) {
            return set;
        }
        set = new ColumnKeySet();
        this.columnKeySet = set;
        return set;
    }

    public Map<C, Map<R, V>> columnMap() {
        Map map = this.columnMap;
        if (map != null) {
            return map;
        }
        map = new ColumnMap();
        this.columnMap = map;
        return map;
    }

    public boolean contains(@Nullable Object obj, @Nullable Object obj2) {
        if (!(obj == null || obj2 == null)) {
            Map map = (Map) Maps.safeGet(this.backingMap, obj);
            if (map != null && Maps.safeContainsKey(map, obj2)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsColumn(@Nullable Object obj) {
        if (obj != null) {
            for (Map safeContainsKey : this.backingMap.values()) {
                if (Maps.safeContainsKey(safeContainsKey, obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsRow(@Nullable Object obj) {
        return obj != null && Maps.safeContainsKey(this.backingMap, obj);
    }

    public boolean containsValue(@Nullable Object obj) {
        if (obj != null) {
            for (Map containsValue : this.backingMap.values()) {
                if (containsValue.containsValue(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    Iterator<C> createColumnKeyIterator() {
        return new ColumnKeyIterator();
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Table)) {
            return false;
        }
        return cellSet().equals(((Table) obj).cellSet());
    }

    public V get(@Nullable Object obj, @Nullable Object obj2) {
        if (!(obj == null || obj2 == null)) {
            Map map = (Map) Maps.safeGet(this.backingMap, obj);
            if (map != null) {
                return Maps.safeGet(map, obj2);
            }
        }
        return null;
    }

    public int hashCode() {
        return cellSet().hashCode();
    }

    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }

    public V put(R r, C c, V v) {
        Preconditions.checkNotNull(r);
        Preconditions.checkNotNull(c);
        Preconditions.checkNotNull(v);
        return getOrCreate(r).put(c, v);
    }

    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        for (Cell cell : table.cellSet()) {
            put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }

    public V remove(@Nullable Object obj, @Nullable Object obj2) {
        V v;
        if (obj != null) {
            if (obj2 == null) {
                v = null;
            } else {
                Map map = (Map) Maps.safeGet(this.backingMap, obj);
                if (map != null) {
                    V remove = map.remove(obj2);
                    if (map.isEmpty()) {
                        this.backingMap.remove(obj);
                        return remove;
                    }
                    v = remove;
                }
            }
            return v;
        }
        v = null;
        return v;
    }

    public Map<C, V> row(R r) {
        return new Row(r);
    }

    public Set<R> rowKeySet() {
        Set set = this.rowKeySet;
        if (set != null) {
            return set;
        }
        set = new RowKeySet();
        this.rowKeySet = set;
        return set;
    }

    public Map<R, Map<C, V>> rowMap() {
        Map map = this.rowMap;
        if (map != null) {
            return map;
        }
        map = new RowMap();
        this.rowMap = map;
        return map;
    }

    public int size() {
        int i = 0;
        for (Map size : this.backingMap.values()) {
            i = size.size() + i;
        }
        return i;
    }

    public String toString() {
        return rowMap().toString();
    }

    public Collection<V> values() {
        Collection collection = this.values;
        if (collection != null) {
            return collection;
        }
        collection = new Values();
        this.values = collection;
        return collection;
    }
}
