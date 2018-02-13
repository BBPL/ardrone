package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Table.Cell;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
    private static final Function<Cell<Object, Object, Object>, Object> GET_VALUE_FUNCTION = new C07311();
    private final ImmutableSet<Cell<R, C, V>> cellSet;
    @Nullable
    private volatile transient ImmutableList<V> valueList;

    static final class C07311 implements Function<Cell<Object, Object, Object>, Object> {
        C07311() {
        }

        public Object apply(Cell<Object, Object, Object> cell) {
            return cell.getValue();
        }
    }

    private static abstract class ImmutableArrayMap<K, V> extends ImmutableMap<K, V> {
        private final int size;

        class C07341 extends ImmutableMapEntrySet<K, V> {
            C07341() {
            }

            public UnmodifiableIterator<Entry<K, V>> iterator() {
                return new AbstractIndexedListIterator<Entry<K, V>>(size()) {
                    protected Entry<K, V> get(int i) {
                        return Maps.immutableEntry(ImmutableArrayMap.this.getKey(i), ImmutableArrayMap.this.getValue(i));
                    }
                };
            }

            ImmutableMap<K, V> map() {
                return ImmutableArrayMap.this;
            }
        }

        class C07362 extends ImmutableMapEntrySet<K, V> {

            class C07351 extends AbstractIterator<Entry<K, V>> {
                private int index = -1;
                private final int maxIndex = ImmutableArrayMap.this.keyToIndex().size();

                C07351() {
                }

                protected Entry<K, V> computeNext() {
                    this.index++;
                    while (this.index < this.maxIndex) {
                        Object value = ImmutableArrayMap.this.getValue(this.index);
                        if (value != null) {
                            return Maps.immutableEntry(ImmutableArrayMap.this.getKey(this.index), value);
                        }
                        this.index++;
                    }
                    return (Entry) endOfData();
                }
            }

            C07362() {
            }

            public UnmodifiableIterator<Entry<K, V>> iterator() {
                return new C07351();
            }

            ImmutableMap<K, V> map() {
                return ImmutableArrayMap.this;
            }
        }

        ImmutableArrayMap(int i) {
            this.size = i;
        }

        private boolean isFull() {
            return this.size == keyToIndex().size();
        }

        ImmutableSet<Entry<K, V>> createEntrySet() {
            return isFull() ? new C07341() : new C07362();
        }

        ImmutableSet<K> createKeySet() {
            return isFull() ? keyToIndex().keySet() : super.createKeySet();
        }

        public V get(@Nullable Object obj) {
            Integer num = (Integer) keyToIndex().get(obj);
            return num == null ? null : getValue(num.intValue());
        }

        K getKey(int i) {
            return keyToIndex().keySet().asList().get(i);
        }

        @Nullable
        abstract V getValue(int i);

        abstract ImmutableMap<K, Integer> keyToIndex();

        public int size() {
            return this.size;
        }
    }

    @VisibleForTesting
    @Immutable
    static final class DenseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
        private final int[] columnCounts = new int[this.columnKeyToIndex.size()];
        private final ImmutableMap<C, Integer> columnKeyToIndex;
        private final ImmutableMap<C, Map<R, V>> columnMap;
        private final int[] rowCounts = new int[this.rowKeyToIndex.size()];
        private final ImmutableMap<R, Integer> rowKeyToIndex;
        private final ImmutableMap<R, Map<C, V>> rowMap;
        private final V[][] values;

        private final class Column extends ImmutableArrayMap<R, V> {
            private final int columnIndex;

            Column(int i) {
                super(DenseImmutableTable.this.columnCounts[i]);
                this.columnIndex = i;
            }

            V getValue(int i) {
                return DenseImmutableTable.this.values[i][this.columnIndex];
            }

            boolean isPartialView() {
                return true;
            }

            ImmutableMap<R, Integer> keyToIndex() {
                return DenseImmutableTable.this.rowKeyToIndex;
            }
        }

        private final class ColumnMap extends ImmutableArrayMap<C, Map<R, V>> {
            private ColumnMap() {
                super(DenseImmutableTable.this.columnCounts.length);
            }

            Map<R, V> getValue(int i) {
                return new Column(i);
            }

            boolean isPartialView() {
                return false;
            }

            ImmutableMap<C, Integer> keyToIndex() {
                return DenseImmutableTable.this.columnKeyToIndex;
            }
        }

        private final class Row extends ImmutableArrayMap<C, V> {
            private final int rowIndex;

            Row(int i) {
                super(DenseImmutableTable.this.rowCounts[i]);
                this.rowIndex = i;
            }

            V getValue(int i) {
                return DenseImmutableTable.this.values[this.rowIndex][i];
            }

            boolean isPartialView() {
                return true;
            }

            ImmutableMap<C, Integer> keyToIndex() {
                return DenseImmutableTable.this.columnKeyToIndex;
            }
        }

        private final class RowMap extends ImmutableArrayMap<R, Map<C, V>> {
            private RowMap() {
                super(DenseImmutableTable.this.rowCounts.length);
            }

            Map<C, V> getValue(int i) {
                return new Row(i);
            }

            boolean isPartialView() {
                return false;
            }

            ImmutableMap<R, Integer> keyToIndex() {
                return DenseImmutableTable.this.rowKeyToIndex;
            }
        }

        DenseImmutableTable(ImmutableSet<Cell<R, C, V>> immutableSet, ImmutableSet<R> immutableSet2, ImmutableSet<C> immutableSet3) {
            super(immutableSet);
            this.values = (Object[][]) Array.newInstance(Object.class, new int[]{immutableSet2.size(), immutableSet3.size()});
            this.rowKeyToIndex = makeIndex(immutableSet2);
            this.columnKeyToIndex = makeIndex(immutableSet3);
            Iterator it = immutableSet.iterator();
            while (it.hasNext()) {
                Cell cell = (Cell) it.next();
                Object rowKey = cell.getRowKey();
                Object columnKey = cell.getColumnKey();
                int intValue = ((Integer) this.rowKeyToIndex.get(rowKey)).intValue();
                int intValue2 = ((Integer) this.columnKeyToIndex.get(columnKey)).intValue();
                Preconditions.checkArgument(this.values[intValue][intValue2] == null, "duplicate key: (%s, %s)", rowKey, columnKey);
                this.values[intValue][intValue2] = cell.getValue();
                int[] iArr = this.rowCounts;
                iArr[intValue] = iArr[intValue] + 1;
                iArr = this.columnCounts;
                iArr[intValue2] = iArr[intValue2] + 1;
            }
            this.rowMap = new RowMap();
            this.columnMap = new ColumnMap();
        }

        private static <E> ImmutableMap<E, Integer> makeIndex(ImmutableSet<E> immutableSet) {
            Builder builder = ImmutableMap.builder();
            int i = 0;
            Iterator it = immutableSet.iterator();
            while (it.hasNext()) {
                builder.put(it.next(), Integer.valueOf(i));
                i++;
            }
            return builder.build();
        }

        public /* bridge */ /* synthetic */ Set cellSet() {
            return super.cellSet();
        }

        public ImmutableMap<R, V> column(C c) {
            Integer num = (Integer) this.columnKeyToIndex.get(Preconditions.checkNotNull(c));
            return num == null ? ImmutableMap.of() : new Column(num.intValue());
        }

        public ImmutableSet<C> columnKeySet() {
            return this.columnKeyToIndex.keySet();
        }

        public ImmutableMap<C, Map<R, V>> columnMap() {
            return this.columnMap;
        }

        public boolean contains(@Nullable Object obj, @Nullable Object obj2) {
            return get(obj, obj2) != null;
        }

        public boolean containsColumn(@Nullable Object obj) {
            return this.columnKeyToIndex.containsKey(obj);
        }

        public boolean containsRow(@Nullable Object obj) {
            return this.rowKeyToIndex.containsKey(obj);
        }

        public V get(@Nullable Object obj, @Nullable Object obj2) {
            Integer num = (Integer) this.rowKeyToIndex.get(obj);
            Integer num2 = (Integer) this.columnKeyToIndex.get(obj2);
            return (num == null || num2 == null) ? null : this.values[num.intValue()][num2.intValue()];
        }

        public ImmutableMap<C, V> row(R r) {
            Preconditions.checkNotNull(r);
            Integer num = (Integer) this.rowKeyToIndex.get(r);
            return num == null ? ImmutableMap.of() : new Row(num.intValue());
        }

        public ImmutableSet<R> rowKeySet() {
            return this.rowKeyToIndex.keySet();
        }

        public ImmutableMap<R, Map<C, V>> rowMap() {
            return this.rowMap;
        }

        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }
    }

    @VisibleForTesting
    @Immutable
    static final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
        private final ImmutableMap<C, Map<R, V>> columnMap;
        private final ImmutableMap<R, Map<C, V>> rowMap;

        static final class C07371 implements Function<Builder<B, V>, Map<B, V>> {
            C07371() {
            }

            public Map<B, V> apply(Builder<B, V> builder) {
                return builder.build();
            }
        }

        SparseImmutableTable(ImmutableSet<Cell<R, C, V>> immutableSet, ImmutableSet<R> immutableSet2, ImmutableSet<C> immutableSet3) {
            super(immutableSet);
            Map makeIndexBuilder = makeIndexBuilder(immutableSet2);
            Map makeIndexBuilder2 = makeIndexBuilder(immutableSet3);
            Iterator it = immutableSet.iterator();
            while (it.hasNext()) {
                Cell cell = (Cell) it.next();
                Object rowKey = cell.getRowKey();
                Object columnKey = cell.getColumnKey();
                Object value = cell.getValue();
                ((Builder) makeIndexBuilder.get(rowKey)).put(columnKey, value);
                ((Builder) makeIndexBuilder2.get(columnKey)).put(rowKey, value);
            }
            this.rowMap = buildIndex(makeIndexBuilder);
            this.columnMap = buildIndex(makeIndexBuilder2);
        }

        private static final <A, B, V> ImmutableMap<A, Map<B, V>> buildIndex(Map<A, Builder<B, V>> map) {
            return ImmutableMap.copyOf(Maps.transformValues((Map) map, new C07371()));
        }

        private static final <A, B, V> Map<A, Builder<B, V>> makeIndexBuilder(ImmutableSet<A> immutableSet) {
            Map<A, Builder<B, V>> newLinkedHashMap = Maps.newLinkedHashMap();
            Iterator it = immutableSet.iterator();
            while (it.hasNext()) {
                newLinkedHashMap.put(it.next(), ImmutableMap.builder());
            }
            return newLinkedHashMap;
        }

        public /* bridge */ /* synthetic */ Set cellSet() {
            return super.cellSet();
        }

        public ImmutableMap<R, V> column(C c) {
            Preconditions.checkNotNull(c);
            return (ImmutableMap) Objects.firstNonNull((ImmutableMap) this.columnMap.get(c), ImmutableMap.of());
        }

        public ImmutableSet<C> columnKeySet() {
            return this.columnMap.keySet();
        }

        public ImmutableMap<C, Map<R, V>> columnMap() {
            return this.columnMap;
        }

        public boolean contains(@Nullable Object obj, @Nullable Object obj2) {
            Map map = (Map) this.rowMap.get(obj);
            return map != null && map.containsKey(obj2);
        }

        public boolean containsColumn(@Nullable Object obj) {
            return this.columnMap.containsKey(obj);
        }

        public boolean containsRow(@Nullable Object obj) {
            return this.rowMap.containsKey(obj);
        }

        public V get(@Nullable Object obj, @Nullable Object obj2) {
            Map map = (Map) this.rowMap.get(obj);
            return map == null ? null : map.get(obj2);
        }

        public ImmutableMap<C, V> row(R r) {
            Preconditions.checkNotNull(r);
            return (ImmutableMap) Objects.firstNonNull((ImmutableMap) this.rowMap.get(r), ImmutableMap.of());
        }

        public ImmutableSet<R> rowKeySet() {
            return this.rowMap.keySet();
        }

        public ImmutableMap<R, Map<C, V>> rowMap() {
            return this.rowMap;
        }

        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }
    }

    private RegularImmutableTable(ImmutableSet<Cell<R, C, V>> immutableSet) {
        this.cellSet = immutableSet;
    }

    static final <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Cell<R, C, V>> iterable) {
        return forCellsInternal(iterable, null, null);
    }

    static final <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Cell<R, C, V>> list, @Nullable final Comparator<? super R> comparator, @Nullable final Comparator<? super C> comparator2) {
        Preconditions.checkNotNull(list);
        if (!(comparator == null && comparator2 == null)) {
            Collections.sort(list, new Comparator<Cell<R, C, V>>() {
                public int compare(Cell<R, C, V> cell, Cell<R, C, V> cell2) {
                    int compare = comparator == null ? 0 : comparator.compare(cell.getRowKey(), cell2.getRowKey());
                    return compare != 0 ? compare : comparator2 != null ? comparator2.compare(cell.getColumnKey(), cell2.getColumnKey()) : 0;
                }
            });
        }
        return forCellsInternal(list, comparator, comparator2);
    }

    private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Cell<R, C, V>> iterable, @Nullable Comparator<? super R> comparator, @Nullable Comparator<? super C> comparator2) {
        ImmutableSet.Builder builder = ImmutableSet.builder();
        ImmutableSet.Builder builder2 = ImmutableSet.builder();
        ImmutableSet.Builder builder3 = ImmutableSet.builder();
        for (Object obj : iterable) {
            builder.add(obj);
            builder2.add(obj.getRowKey());
            builder3.add(obj.getColumnKey());
        }
        ImmutableSet build = builder.build();
        ImmutableSet build2 = builder2.build();
        if (comparator != null) {
            Collection newArrayList = Lists.newArrayList((Iterable) build2);
            Collections.sort(newArrayList, comparator);
            build2 = ImmutableSet.copyOf(newArrayList);
        }
        ImmutableSet build3 = builder3.build();
        if (comparator2 != null) {
            Collection newArrayList2 = Lists.newArrayList((Iterable) build3);
            Collections.sort(newArrayList2, comparator2);
            build3 = ImmutableSet.copyOf(newArrayList2);
        }
        return build.size() > (build2.size() * build3.size()) / 2 ? new DenseImmutableTable(build, build2, build3) : new SparseImmutableTable(build, build2, build3);
    }

    private Function<Cell<R, C, V>, V> getValueFunction() {
        return GET_VALUE_FUNCTION;
    }

    public final ImmutableSet<Cell<R, C, V>> cellSet() {
        return this.cellSet;
    }

    public final boolean containsValue(@Nullable Object obj) {
        return values().contains(obj);
    }

    public final boolean isEmpty() {
        return false;
    }

    public final int size() {
        return cellSet().size();
    }

    public final ImmutableCollection<V> values() {
        ImmutableCollection immutableCollection = this.valueList;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        immutableCollection = ImmutableList.copyOf(Iterables.transform(cellSet(), getValueFunction()));
        this.valueList = immutableCollection;
        return immutableCollection;
    }
}
