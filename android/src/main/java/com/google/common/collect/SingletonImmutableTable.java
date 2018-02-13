package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Table.Cell;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
final class SingletonImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
    private final C singleColumnKey;
    private final R singleRowKey;
    private final V singleValue;

    SingletonImmutableTable(Cell<R, C, V> cell) {
        this(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
    }

    SingletonImmutableTable(R r, C c, V v) {
        this.singleRowKey = Preconditions.checkNotNull(r);
        this.singleColumnKey = Preconditions.checkNotNull(c);
        this.singleValue = Preconditions.checkNotNull(v);
    }

    public ImmutableSet<Cell<R, C, V>> cellSet() {
        return ImmutableSet.of(Tables.immutableCell(this.singleRowKey, this.singleColumnKey, this.singleValue));
    }

    public ImmutableMap<R, V> column(C c) {
        Preconditions.checkNotNull(c);
        return containsColumn(c) ? ImmutableMap.of(this.singleRowKey, this.singleValue) : ImmutableMap.of();
    }

    public ImmutableSet<C> columnKeySet() {
        return ImmutableSet.of(this.singleColumnKey);
    }

    public ImmutableMap<C, Map<R, V>> columnMap() {
        return ImmutableMap.of(this.singleColumnKey, ImmutableMap.of(this.singleRowKey, this.singleValue));
    }

    public boolean contains(@Nullable Object obj, @Nullable Object obj2) {
        return containsRow(obj) && containsColumn(obj2);
    }

    public boolean containsColumn(@Nullable Object obj) {
        return Objects.equal(this.singleColumnKey, obj);
    }

    public boolean containsRow(@Nullable Object obj) {
        return Objects.equal(this.singleRowKey, obj);
    }

    public boolean containsValue(@Nullable Object obj) {
        return Objects.equal(this.singleValue, obj);
    }

    public boolean equals(@Nullable Object obj) {
        if (obj != this) {
            if (obj instanceof Table) {
                Table table = (Table) obj;
                if (table.size() == 1) {
                    Cell cell = (Cell) table.cellSet().iterator().next();
                    if (!(Objects.equal(this.singleRowKey, cell.getRowKey()) && Objects.equal(this.singleColumnKey, cell.getColumnKey()) && Objects.equal(this.singleValue, cell.getValue()))) {
                        return false;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public V get(@Nullable Object obj, @Nullable Object obj2) {
        return contains(obj, obj2) ? this.singleValue : null;
    }

    public int hashCode() {
        return Objects.hashCode(this.singleRowKey, this.singleColumnKey, this.singleValue);
    }

    public boolean isEmpty() {
        return false;
    }

    public ImmutableMap<C, V> row(R r) {
        Preconditions.checkNotNull(r);
        return containsRow(r) ? ImmutableMap.of(this.singleColumnKey, this.singleValue) : ImmutableMap.of();
    }

    public ImmutableSet<R> rowKeySet() {
        return ImmutableSet.of(this.singleRowKey);
    }

    public ImmutableMap<R, Map<C, V>> rowMap() {
        return ImmutableMap.of(this.singleRowKey, ImmutableMap.of(this.singleColumnKey, this.singleValue));
    }

    public int size() {
        return 1;
    }

    public String toString() {
        return '{' + this.singleRowKey + "={" + this.singleColumnKey + '=' + this.singleValue + "}}";
    }

    public ImmutableCollection<V> values() {
        return ImmutableSet.of(this.singleValue);
    }
}
