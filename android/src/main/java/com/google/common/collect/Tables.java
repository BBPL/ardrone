package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Table.Cell;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
public final class Tables {
    private static final Function<? extends Map<?, ?>, ? extends Map<?, ?>> UNMODIFIABLE_WRAPPER = new C07721();

    static abstract class AbstractCell<R, C, V> implements Cell<R, C, V> {
        AbstractCell() {
        }

        public boolean equals(Object obj) {
            if (obj != this) {
                if (!(obj instanceof Cell)) {
                    return false;
                }
                Cell cell = (Cell) obj;
                if (!Objects.equal(getRowKey(), cell.getRowKey()) || !Objects.equal(getColumnKey(), cell.getColumnKey())) {
                    return false;
                }
                if (!Objects.equal(getValue(), cell.getValue())) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            return Objects.hashCode(getRowKey(), getColumnKey(), getValue());
        }

        public String toString() {
            return "(" + getRowKey() + "," + getColumnKey() + ")=" + getValue();
        }
    }

    static final class C07721 implements Function<Map<Object, Object>, Map<Object, Object>> {
        C07721() {
        }

        public Map<Object, Object> apply(Map<Object, Object> map) {
            return Collections.unmodifiableMap(map);
        }
    }

    static final class ImmutableCell<R, C, V> extends AbstractCell<R, C, V> implements Serializable {
        private static final long serialVersionUID = 0;
        private final C columnKey;
        private final R rowKey;
        private final V value;

        ImmutableCell(@Nullable R r, @Nullable C c, @Nullable V v) {
            this.rowKey = r;
            this.columnKey = c;
            this.value = v;
        }

        public C getColumnKey() {
            return this.columnKey;
        }

        public R getRowKey() {
            return this.rowKey;
        }

        public V getValue() {
            return this.value;
        }
    }

    private static class TransformedTable<R, C, V1, V2> implements Table<R, C, V2> {
        CellSet cellSet;
        Map<C, Map<R, V2>> columnMap;
        final Table<R, C, V1> fromTable;
        final Function<? super V1, V2> function;
        Map<R, Map<C, V2>> rowMap;
        Collection<V2> values;

        class C07731 implements Function<Cell<R, C, V1>, Cell<R, C, V2>> {
            C07731() {
            }

            public Cell<R, C, V2> apply(Cell<R, C, V1> cell) {
                return Tables.immutableCell(cell.getRowKey(), cell.getColumnKey(), TransformedTable.this.function.apply(cell.getValue()));
            }
        }

        class C07742 implements Function<Map<C, V1>, Map<C, V2>> {
            C07742() {
            }

            public Map<C, V2> apply(Map<C, V1> map) {
                return Maps.transformValues((Map) map, TransformedTable.this.function);
            }
        }

        class C07753 implements Function<Map<R, V1>, Map<R, V2>> {
            C07753() {
            }

            public Map<R, V2> apply(Map<R, V1> map) {
                return Maps.transformValues((Map) map, TransformedTable.this.function);
            }
        }

        class CellSet extends TransformedCollection<Cell<R, C, V1>, Cell<R, C, V2>> implements Set<Cell<R, C, V2>> {
            CellSet() {
                super(TransformedTable.this.fromTable.cellSet(), TransformedTable.this.cellFunction());
            }

            public boolean contains(Object obj) {
                if (obj instanceof Cell) {
                    Cell cell = (Cell) obj;
                    if (Objects.equal(cell.getValue(), TransformedTable.this.get(cell.getRowKey(), cell.getColumnKey())) && (cell.getValue() != null || TransformedTable.this.fromTable.contains(cell.getRowKey(), cell.getColumnKey()))) {
                        return true;
                    }
                }
                return false;
            }

            public boolean equals(Object obj) {
                return Sets.equalsImpl(this, obj);
            }

            public int hashCode() {
                return Sets.hashCodeImpl(this);
            }

            public boolean remove(Object obj) {
                if (!contains(obj)) {
                    return false;
                }
                Cell cell = (Cell) obj;
                TransformedTable.this.fromTable.remove(cell.getRowKey(), cell.getColumnKey());
                return true;
            }
        }

        TransformedTable(Table<R, C, V1> table, Function<? super V1, V2> function) {
            this.fromTable = (Table) Preconditions.checkNotNull(table);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        Function<Cell<R, C, V1>, Cell<R, C, V2>> cellFunction() {
            return new C07731();
        }

        public Set<Cell<R, C, V2>> cellSet() {
            if (this.cellSet != null) {
                return this.cellSet;
            }
            Set cellSet = new CellSet();
            this.cellSet = cellSet;
            return cellSet;
        }

        public void clear() {
            this.fromTable.clear();
        }

        public Map<R, V2> column(C c) {
            return Maps.transformValues(this.fromTable.column(c), this.function);
        }

        public Set<C> columnKeySet() {
            return this.fromTable.columnKeySet();
        }

        public Map<C, Map<R, V2>> columnMap() {
            if (this.columnMap != null) {
                return this.columnMap;
            }
            Map<C, Map<R, V2>> createColumnMap = createColumnMap();
            this.columnMap = createColumnMap;
            return createColumnMap;
        }

        public boolean contains(Object obj, Object obj2) {
            return this.fromTable.contains(obj, obj2);
        }

        public boolean containsColumn(Object obj) {
            return this.fromTable.containsColumn(obj);
        }

        public boolean containsRow(Object obj) {
            return this.fromTable.containsRow(obj);
        }

        public boolean containsValue(Object obj) {
            return values().contains(obj);
        }

        Map<C, Map<R, V2>> createColumnMap() {
            return Maps.transformValues(this.fromTable.columnMap(), new C07753());
        }

        Map<R, Map<C, V2>> createRowMap() {
            return Maps.transformValues(this.fromTable.rowMap(), new C07742());
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

        public V2 get(Object obj, Object obj2) {
            return contains(obj, obj2) ? this.function.apply(this.fromTable.get(obj, obj2)) : null;
        }

        public int hashCode() {
            return cellSet().hashCode();
        }

        public boolean isEmpty() {
            return this.fromTable.isEmpty();
        }

        public V2 put(R r, C c, V2 v2) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Table<? extends R, ? extends C, ? extends V2> table) {
            throw new UnsupportedOperationException();
        }

        public V2 remove(Object obj, Object obj2) {
            return contains(obj, obj2) ? this.function.apply(this.fromTable.remove(obj, obj2)) : null;
        }

        public Map<C, V2> row(R r) {
            return Maps.transformValues(this.fromTable.row(r), this.function);
        }

        public Set<R> rowKeySet() {
            return this.fromTable.rowKeySet();
        }

        public Map<R, Map<C, V2>> rowMap() {
            if (this.rowMap != null) {
                return this.rowMap;
            }
            Map<R, Map<C, V2>> createRowMap = createRowMap();
            this.rowMap = createRowMap;
            return createRowMap;
        }

        public int size() {
            return this.fromTable.size();
        }

        public String toString() {
            return rowMap().toString();
        }

        public Collection<V2> values() {
            if (this.values != null) {
                return this.values;
            }
            Collection<V2> transform = Collections2.transform(this.fromTable.values(), this.function);
            this.values = transform;
            return transform;
        }
    }

    private static class TransposeTable<C, R, V> implements Table<C, R, V> {
        private static final Function<Cell<?, ?, ?>, Cell<?, ?, ?>> TRANSPOSE_CELL = new C07761();
        CellSet cellSet;
        final Table<R, C, V> original;

        static final class C07761 implements Function<Cell<?, ?, ?>, Cell<?, ?, ?>> {
            C07761() {
            }

            public Cell<?, ?, ?> apply(Cell<?, ?, ?> cell) {
                return Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
            }
        }

        class CellSet extends TransformedCollection<Cell<R, C, V>, Cell<C, R, V>> implements Set<Cell<C, R, V>> {
            CellSet() {
                super(TransposeTable.this.original.cellSet(), TransposeTable.TRANSPOSE_CELL);
            }

            public boolean contains(Object obj) {
                if (!(obj instanceof Cell)) {
                    return false;
                }
                Cell cell = (Cell) obj;
                return TransposeTable.this.original.cellSet().contains(Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
            }

            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                }
                if (!(obj instanceof Set)) {
                    return false;
                }
                Set set = (Set) obj;
                return set.size() == size() ? containsAll(set) : false;
            }

            public int hashCode() {
                return Sets.hashCodeImpl(this);
            }

            public boolean remove(Object obj) {
                if (!(obj instanceof Cell)) {
                    return false;
                }
                Cell cell = (Cell) obj;
                return TransposeTable.this.original.cellSet().remove(Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
            }
        }

        TransposeTable(Table<R, C, V> table) {
            this.original = (Table) Preconditions.checkNotNull(table);
        }

        public Set<Cell<C, R, V>> cellSet() {
            Set set = this.cellSet;
            if (set != null) {
                return set;
            }
            set = new CellSet();
            this.cellSet = set;
            return set;
        }

        public void clear() {
            this.original.clear();
        }

        public Map<C, V> column(R r) {
            return this.original.row(r);
        }

        public Set<R> columnKeySet() {
            return this.original.rowKeySet();
        }

        public Map<R, Map<C, V>> columnMap() {
            return this.original.rowMap();
        }

        public boolean contains(@Nullable Object obj, @Nullable Object obj2) {
            return this.original.contains(obj2, obj);
        }

        public boolean containsColumn(@Nullable Object obj) {
            return this.original.containsRow(obj);
        }

        public boolean containsRow(@Nullable Object obj) {
            return this.original.containsColumn(obj);
        }

        public boolean containsValue(@Nullable Object obj) {
            return this.original.containsValue(obj);
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
            return this.original.get(obj2, obj);
        }

        public int hashCode() {
            return cellSet().hashCode();
        }

        public boolean isEmpty() {
            return this.original.isEmpty();
        }

        public V put(C c, R r, V v) {
            return this.original.put(r, c, v);
        }

        public void putAll(Table<? extends C, ? extends R, ? extends V> table) {
            this.original.putAll(Tables.transpose(table));
        }

        public V remove(@Nullable Object obj, @Nullable Object obj2) {
            return this.original.remove(obj2, obj);
        }

        public Map<R, V> row(C c) {
            return this.original.column(c);
        }

        public Set<C> rowKeySet() {
            return this.original.columnKeySet();
        }

        public Map<C, Map<R, V>> rowMap() {
            return this.original.columnMap();
        }

        public int size() {
            return this.original.size();
        }

        public String toString() {
            return rowMap().toString();
        }

        public Collection<V> values() {
            return this.original.values();
        }
    }

    private static class UnmodifiableTable<R, C, V> extends ForwardingTable<R, C, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final Table<? extends R, ? extends C, ? extends V> delegate;

        UnmodifiableTable(Table<? extends R, ? extends C, ? extends V> table) {
            this.delegate = (Table) Preconditions.checkNotNull(table);
        }

        public Set<Cell<R, C, V>> cellSet() {
            return Collections.unmodifiableSet(super.cellSet());
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public Map<R, V> column(@Nullable C c) {
            return Collections.unmodifiableMap(super.column(c));
        }

        public Set<C> columnKeySet() {
            return Collections.unmodifiableSet(super.columnKeySet());
        }

        public Map<C, Map<R, V>> columnMap() {
            return Collections.unmodifiableMap(Maps.transformValues(super.columnMap(), Tables.unmodifiableWrapper()));
        }

        protected Table<R, C, V> delegate() {
            return this.delegate;
        }

        public V put(@Nullable R r, @Nullable C c, @Nullable V v) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
            throw new UnsupportedOperationException();
        }

        public V remove(@Nullable Object obj, @Nullable Object obj2) {
            throw new UnsupportedOperationException();
        }

        public Map<C, V> row(@Nullable R r) {
            return Collections.unmodifiableMap(super.row(r));
        }

        public Set<R> rowKeySet() {
            return Collections.unmodifiableSet(super.rowKeySet());
        }

        public Map<R, Map<C, V>> rowMap() {
            return Collections.unmodifiableMap(Maps.transformValues(super.rowMap(), Tables.unmodifiableWrapper()));
        }

        public Collection<V> values() {
            return Collections.unmodifiableCollection(super.values());
        }
    }

    static final class UnmodifiableRowSortedMap<R, C, V> extends UnmodifiableTable<R, C, V> implements RowSortedTable<R, C, V> {
        private static final long serialVersionUID = 0;

        public UnmodifiableRowSortedMap(RowSortedTable<R, ? extends C, ? extends V> rowSortedTable) {
            super(rowSortedTable);
        }

        protected RowSortedTable<R, C, V> delegate() {
            return (RowSortedTable) super.delegate();
        }

        public SortedSet<R> rowKeySet() {
            return Collections.unmodifiableSortedSet(delegate().rowKeySet());
        }

        public SortedMap<R, Map<C, V>> rowMap() {
            return Collections.unmodifiableSortedMap(Maps.transformValues(delegate().rowMap(), Tables.unmodifiableWrapper()));
        }
    }

    private Tables() {
    }

    public static <R, C, V> Cell<R, C, V> immutableCell(@Nullable R r, @Nullable C c, @Nullable V v) {
        return new ImmutableCell(r, c, v);
    }

    @Beta
    public static <R, C, V> Table<R, C, V> newCustomTable(Map<R, Map<C, V>> map, Supplier<? extends Map<C, V>> supplier) {
        Preconditions.checkArgument(map.isEmpty());
        Preconditions.checkNotNull(supplier);
        return new StandardTable(map, supplier);
    }

    @Beta
    public static <R, C, V1, V2> Table<R, C, V2> transformValues(Table<R, C, V1> table, Function<? super V1, V2> function) {
        return new TransformedTable(table, function);
    }

    public static <R, C, V> Table<C, R, V> transpose(Table<R, C, V> table) {
        return table instanceof TransposeTable ? ((TransposeTable) table).original : new TransposeTable(table);
    }

    @Beta
    public static <R, C, V> RowSortedTable<R, C, V> unmodifiableRowSortedTable(RowSortedTable<R, ? extends C, ? extends V> rowSortedTable) {
        return new UnmodifiableRowSortedMap(rowSortedTable);
    }

    public static <R, C, V> Table<R, C, V> unmodifiableTable(Table<? extends R, ? extends C, ? extends V> table) {
        return new UnmodifiableTable(table);
    }

    private static <K, V> Function<Map<K, V>, Map<K, V>> unmodifiableWrapper() {
        return UNMODIFIABLE_WRAPPER;
    }
}
