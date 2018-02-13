package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

@GwtCompatible
class StandardRowSortedTable<R, C, V> extends StandardTable<R, C, V> implements RowSortedTable<R, C, V> {
    private static final long serialVersionUID = 0;
    private transient SortedSet<R> rowKeySet;
    private transient RowSortedMap rowMap;

    private class RowKeySortedSet extends RowKeySet implements SortedSet<R> {
        private RowKeySortedSet() {
            super();
        }

        public Comparator<? super R> comparator() {
            return StandardRowSortedTable.this.sortedBackingMap().comparator();
        }

        public R first() {
            return StandardRowSortedTable.this.sortedBackingMap().firstKey();
        }

        public SortedSet<R> headSet(R r) {
            Preconditions.checkNotNull(r);
            return new StandardRowSortedTable(StandardRowSortedTable.this.sortedBackingMap().headMap(r), StandardRowSortedTable.this.factory).rowKeySet();
        }

        public R last() {
            return StandardRowSortedTable.this.sortedBackingMap().lastKey();
        }

        public SortedSet<R> subSet(R r, R r2) {
            Preconditions.checkNotNull(r);
            Preconditions.checkNotNull(r2);
            return new StandardRowSortedTable(StandardRowSortedTable.this.sortedBackingMap().subMap(r, r2), StandardRowSortedTable.this.factory).rowKeySet();
        }

        public SortedSet<R> tailSet(R r) {
            Preconditions.checkNotNull(r);
            return new StandardRowSortedTable(StandardRowSortedTable.this.sortedBackingMap().tailMap(r), StandardRowSortedTable.this.factory).rowKeySet();
        }
    }

    private class RowSortedMap extends RowMap implements SortedMap<R, Map<C, V>> {
        private RowSortedMap() {
            super();
        }

        public Comparator<? super R> comparator() {
            return StandardRowSortedTable.this.sortedBackingMap().comparator();
        }

        public R firstKey() {
            return StandardRowSortedTable.this.sortedBackingMap().firstKey();
        }

        public SortedMap<R, Map<C, V>> headMap(R r) {
            Preconditions.checkNotNull(r);
            return new StandardRowSortedTable(StandardRowSortedTable.this.sortedBackingMap().headMap(r), StandardRowSortedTable.this.factory).rowMap();
        }

        public R lastKey() {
            return StandardRowSortedTable.this.sortedBackingMap().lastKey();
        }

        public SortedMap<R, Map<C, V>> subMap(R r, R r2) {
            Preconditions.checkNotNull(r);
            Preconditions.checkNotNull(r2);
            return new StandardRowSortedTable(StandardRowSortedTable.this.sortedBackingMap().subMap(r, r2), StandardRowSortedTable.this.factory).rowMap();
        }

        public SortedMap<R, Map<C, V>> tailMap(R r) {
            Preconditions.checkNotNull(r);
            return new StandardRowSortedTable(StandardRowSortedTable.this.sortedBackingMap().tailMap(r), StandardRowSortedTable.this.factory).rowMap();
        }
    }

    StandardRowSortedTable(SortedMap<R, Map<C, V>> sortedMap, Supplier<? extends Map<C, V>> supplier) {
        super(sortedMap, supplier);
    }

    private SortedMap<R, Map<C, V>> sortedBackingMap() {
        return (SortedMap) this.backingMap;
    }

    public SortedSet<R> rowKeySet() {
        SortedSet<R> sortedSet = this.rowKeySet;
        if (sortedSet != null) {
            return sortedSet;
        }
        sortedSet = new RowKeySortedSet();
        this.rowKeySet = sortedSet;
        return sortedSet;
    }

    public SortedMap<R, Map<C, V>> rowMap() {
        SortedMap sortedMap = this.rowMap;
        if (sortedMap != null) {
            return sortedMap;
        }
        sortedMap = new RowSortedMap();
        this.rowMap = sortedMap;
        return sortedMap;
    }
}
