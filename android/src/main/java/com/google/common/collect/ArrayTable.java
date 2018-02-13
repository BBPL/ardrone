package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Table.Cell;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public final class ArrayTable<R, C, V> implements Table<R, C, V>, Serializable {
    private static final long serialVersionUID = 0;
    private final V[][] array;
    private transient CellSet cellSet;
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final ImmutableList<C> columnList;
    private transient ColumnMap columnMap;
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableList<R> rowList;
    private transient RowMap rowMap;
    private transient Collection<V> values;

    private static abstract class ArrayMap<K, V> extends ImprovedAbstractMap<K, V> {
        private final ImmutableMap<K, Integer> keyIndex;

        class C05551 extends EntrySet<K, V> {
            C05551() {
            }

            public Iterator<Entry<K, V>> iterator() {
                return new AbstractIndexedListIterator<Entry<K, V>>(size()) {
                    protected Entry<K, V> get(final int i) {
                        return new AbstractMapEntry<K, V>() {
                            public K getKey() {
                                return ArrayMap.this.getKey(i);
                            }

                            public V getValue() {
                                return ArrayMap.this.getValue(i);
                            }

                            public V setValue(V v) {
                                return ArrayMap.this.setValue(i, v);
                            }
                        };
                    }
                };
            }

            Map<K, V> map() {
                return ArrayMap.this;
            }
        }

        private ArrayMap(ImmutableMap<K, Integer> immutableMap) {
            this.keyIndex = immutableMap;
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean containsKey(@Nullable Object obj) {
            return this.keyIndex.containsKey(obj);
        }

        protected Set<Entry<K, V>> createEntrySet() {
            return new C05551();
        }

        public V get(@Nullable Object obj) {
            Integer num = (Integer) this.keyIndex.get(obj);
            return num == null ? null : getValue(num.intValue());
        }

        K getKey(int i) {
            return this.keyIndex.keySet().asList().get(i);
        }

        abstract String getKeyRole();

        @Nullable
        abstract V getValue(int i);

        public boolean isEmpty() {
            return this.keyIndex.isEmpty();
        }

        public Set<K> keySet() {
            return this.keyIndex.keySet();
        }

        public V put(K k, V v) {
            Integer num = (Integer) this.keyIndex.get(k);
            if (num != null) {
                return setValue(num.intValue(), v);
            }
            throw new IllegalArgumentException(getKeyRole() + " " + k + " not in " + this.keyIndex.keySet());
        }

        public V remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Nullable
        abstract V setValue(int i, V v);

        public int size() {
            return this.keyIndex.size();
        }
    }

    private class CellSet extends AbstractSet<Cell<R, C, V>> {
        private CellSet() {
        }

        public boolean contains(Object obj) {
            if (obj instanceof Cell) {
                Cell cell = (Cell) obj;
                Integer num = (Integer) ArrayTable.this.rowKeyToIndex.get(cell.getRowKey());
                Integer num2 = (Integer) ArrayTable.this.columnKeyToIndex.get(cell.getColumnKey());
                if (!(num == null || num2 == null || !Objects.equal(ArrayTable.this.array[num.intValue()][num2.intValue()], cell.getValue()))) {
                    return true;
                }
            }
            return false;
        }

        public Iterator<Cell<R, C, V>> iterator() {
            return new AbstractIndexedListIterator<Cell<R, C, V>>(size()) {
                protected Cell<R, C, V> get(final int i) {
                    return new AbstractCell<R, C, V>() {
                        final int columnIndex = (i % ArrayTable.this.columnList.size());
                        final int rowIndex = (i / ArrayTable.this.columnList.size());

                        public C getColumnKey() {
                            return ArrayTable.this.columnList.get(this.columnIndex);
                        }

                        public R getRowKey() {
                            return ArrayTable.this.rowList.get(this.rowIndex);
                        }

                        public V getValue() {
                            return ArrayTable.this.array[this.rowIndex][this.columnIndex];
                        }
                    };
                }
            };
        }

        public int size() {
            return ArrayTable.this.size();
        }
    }

    private class Column extends ArrayMap<R, V> {
        final int columnIndex;

        Column(int i) {
            super(ArrayTable.this.rowKeyToIndex);
            this.columnIndex = i;
        }

        String getKeyRole() {
            return "Row";
        }

        V getValue(int i) {
            return ArrayTable.this.at(i, this.columnIndex);
        }

        V setValue(int i, V v) {
            return ArrayTable.this.set(i, this.columnIndex, v);
        }
    }

    private class ColumnMap extends ArrayMap<C, Map<R, V>> {
        private ColumnMap() {
            super(ArrayTable.this.columnKeyToIndex);
        }

        String getKeyRole() {
            return "Column";
        }

        Map<R, V> getValue(int i) {
            return new Column(i);
        }

        public Map<R, V> put(C c, Map<R, V> map) {
            throw new UnsupportedOperationException();
        }

        Map<R, V> setValue(int i, Map<R, V> map) {
            throw new UnsupportedOperationException();
        }
    }

    private class Row extends ArrayMap<C, V> {
        final int rowIndex;

        Row(int i) {
            super(ArrayTable.this.columnKeyToIndex);
            this.rowIndex = i;
        }

        String getKeyRole() {
            return "Column";
        }

        V getValue(int i) {
            return ArrayTable.this.at(this.rowIndex, i);
        }

        V setValue(int i, V v) {
            return ArrayTable.this.set(this.rowIndex, i, v);
        }
    }

    private class RowMap extends ArrayMap<R, Map<C, V>> {
        private RowMap() {
            super(ArrayTable.this.rowKeyToIndex);
        }

        String getKeyRole() {
            return "Row";
        }

        Map<C, V> getValue(int i) {
            return new Row(i);
        }

        public Map<C, V> put(R r, Map<C, V> map) {
            throw new UnsupportedOperationException();
        }

        Map<C, V> setValue(int i, Map<C, V> map) {
            throw new UnsupportedOperationException();
        }
    }

    private class Values extends AbstractCollection<V> {
        private Values() {
        }

        public Iterator<V> iterator() {
            return new TransformedIterator<Cell<R, C, V>, V>(ArrayTable.this.cellSet().iterator()) {
                V transform(Cell<R, C, V> cell) {
                    return cell.getValue();
                }
            };
        }

        public int size() {
            return ArrayTable.this.size();
        }
    }

    private ArrayTable(ArrayTable<R, C, V> arrayTable) {
        this.rowList = arrayTable.rowList;
        this.columnList = arrayTable.columnList;
        this.rowKeyToIndex = arrayTable.rowKeyToIndex;
        this.columnKeyToIndex = arrayTable.columnKeyToIndex;
        Object[][] objArr = (Object[][]) Array.newInstance(Object.class, new int[]{this.rowList.size(), this.columnList.size()});
        this.array = objArr;
        for (int i = 0; i < this.rowList.size(); i++) {
            System.arraycopy(arrayTable.array[i], 0, objArr[i], 0, arrayTable.array[i].length);
        }
    }

    private ArrayTable(Table<R, C, V> table) {
        this(table.rowKeySet(), table.columnKeySet());
        putAll(table);
    }

    private ArrayTable(Iterable<? extends R> iterable, Iterable<? extends C> iterable2) {
        this.rowList = ImmutableList.copyOf((Iterable) iterable);
        this.columnList = ImmutableList.copyOf((Iterable) iterable2);
        Preconditions.checkArgument(!this.rowList.isEmpty());
        Preconditions.checkArgument(!this.columnList.isEmpty());
        this.rowKeyToIndex = index(this.rowList);
        this.columnKeyToIndex = index(this.columnList);
        this.array = (Object[][]) Array.newInstance(Object.class, new int[]{this.rowList.size(), this.columnList.size()});
    }

    public static <R, C, V> ArrayTable<R, C, V> create(ArrayTable<R, C, V> arrayTable) {
        return new ArrayTable((ArrayTable) arrayTable);
    }

    public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, V> table) {
        return new ArrayTable((Table) table);
    }

    public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> iterable, Iterable<? extends C> iterable2) {
        return new ArrayTable(iterable, iterable2);
    }

    private static <E> ImmutableMap<E, Integer> index(List<E> list) {
        Builder builder = ImmutableMap.builder();
        for (int i = 0; i < list.size(); i++) {
            builder.put(list.get(i), Integer.valueOf(i));
        }
        return builder.build();
    }

    public V at(int i, int i2) {
        return this.array[i][i2];
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

    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }

    public Map<R, V> column(C c) {
        Preconditions.checkNotNull(c);
        Integer num = (Integer) this.columnKeyToIndex.get(c);
        return num == null ? ImmutableMap.of() : new Column(num.intValue());
    }

    public ImmutableList<C> columnKeyList() {
        return this.columnList;
    }

    public ImmutableSet<C> columnKeySet() {
        return this.columnKeyToIndex.keySet();
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
        return containsRow(obj) && containsColumn(obj2);
    }

    public boolean containsColumn(@Nullable Object obj) {
        return this.columnKeyToIndex.containsKey(obj);
    }

    public boolean containsRow(@Nullable Object obj) {
        return this.rowKeyToIndex.containsKey(obj);
    }

    public boolean containsValue(@Nullable Object obj) {
        for (Object[] objArr : this.array) {
            for (Object equal : r3[r2]) {
                if (Objects.equal(obj, equal)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Table)) {
            return false;
        }
        return cellSet().equals(((Table) obj).cellSet());
    }

    public V erase(@Nullable Object obj, @Nullable Object obj2) {
        Integer num = (Integer) this.rowKeyToIndex.get(obj);
        Integer num2 = (Integer) this.columnKeyToIndex.get(obj2);
        return (num == null || num2 == null) ? null : set(num.intValue(), num2.intValue(), null);
    }

    public void eraseAll() {
        for (Object[] fill : this.array) {
            Arrays.fill(fill, null);
        }
    }

    public V get(@Nullable Object obj, @Nullable Object obj2) {
        Integer num = (Integer) this.rowKeyToIndex.get(obj);
        Integer num2 = (Integer) this.columnKeyToIndex.get(obj2);
        return (num == null || num2 == null) ? null : this.array[num.intValue()][num2.intValue()];
    }

    public int hashCode() {
        return cellSet().hashCode();
    }

    public boolean isEmpty() {
        return false;
    }

    public V put(R r, C c, @Nullable V v) {
        Preconditions.checkNotNull(r);
        Preconditions.checkNotNull(c);
        Integer num = (Integer) this.rowKeyToIndex.get(r);
        Preconditions.checkArgument(num != null, "Row %s not in %s", r, this.rowList);
        Integer num2 = (Integer) this.columnKeyToIndex.get(c);
        Preconditions.checkArgument(num2 != null, "Column %s not in %s", c, this.columnList);
        return set(num.intValue(), num2.intValue(), v);
    }

    public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        for (Cell cell : table.cellSet()) {
            put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }

    @Deprecated
    public V remove(Object obj, Object obj2) {
        throw new UnsupportedOperationException();
    }

    public Map<C, V> row(R r) {
        Preconditions.checkNotNull(r);
        Integer num = (Integer) this.rowKeyToIndex.get(r);
        return num == null ? ImmutableMap.of() : new Row(num.intValue());
    }

    public ImmutableList<R> rowKeyList() {
        return this.rowList;
    }

    public ImmutableSet<R> rowKeySet() {
        return this.rowKeyToIndex.keySet();
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

    public V set(int i, int i2, @Nullable V v) {
        V v2 = this.array[i][i2];
        this.array[i][i2] = v;
        return v2;
    }

    public int size() {
        return this.rowList.size() * this.columnList.size();
    }

    public V[][] toArray(Class<V> cls) {
        Object[][] objArr = (Object[][]) Array.newInstance(cls, new int[]{this.rowList.size(), this.columnList.size()});
        for (int i = 0; i < this.rowList.size(); i++) {
            System.arraycopy(this.array[i], 0, objArr[i], 0, this.array[i].length);
        }
        return objArr;
    }

    public String toString() {
        return rowMap().toString();
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
