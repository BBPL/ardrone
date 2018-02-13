package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Table.Cell;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ImmutableTable<R, C, V> implements Table<R, C, V> {

    public static final class Builder<R, C, V> {
        private final List<Cell<R, C, V>> cells = Lists.newArrayList();
        private Comparator<? super C> columnComparator;
        private Comparator<? super R> rowComparator;

        public ImmutableTable<R, C, V> build() {
            switch (this.cells.size()) {
                case 0:
                    return ImmutableTable.of();
                case 1:
                    return new SingletonImmutableTable((Cell) Iterables.getOnlyElement(this.cells));
                default:
                    return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
            }
        }

        public Builder<R, C, V> orderColumnsBy(Comparator<? super C> comparator) {
            this.columnComparator = (Comparator) Preconditions.checkNotNull(comparator);
            return this;
        }

        public Builder<R, C, V> orderRowsBy(Comparator<? super R> comparator) {
            this.rowComparator = (Comparator) Preconditions.checkNotNull(comparator);
            return this;
        }

        public Builder<R, C, V> put(Cell<? extends R, ? extends C, ? extends V> cell) {
            if (cell instanceof ImmutableCell) {
                Preconditions.checkNotNull(cell.getRowKey());
                Preconditions.checkNotNull(cell.getColumnKey());
                Preconditions.checkNotNull(cell.getValue());
                this.cells.add(cell);
            } else {
                put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            }
            return this;
        }

        public Builder<R, C, V> put(R r, C c, V v) {
            this.cells.add(ImmutableTable.cellOf(r, c, v));
            return this;
        }

        public Builder<R, C, V> putAll(Table<? extends R, ? extends C, ? extends V> table) {
            for (Cell put : table.cellSet()) {
                put(put);
            }
            return this;
        }
    }

    ImmutableTable() {
    }

    public static final <R, C, V> Builder<R, C, V> builder() {
        return new Builder();
    }

    static <R, C, V> Cell<R, C, V> cellOf(R r, C c, V v) {
        return Tables.immutableCell(Preconditions.checkNotNull(r), Preconditions.checkNotNull(c), Preconditions.checkNotNull(v));
    }

    public static final <R, C, V> ImmutableTable<R, C, V> copyOf(Table<? extends R, ? extends C, ? extends V> table) {
        if (table instanceof ImmutableTable) {
            return (ImmutableTable) table;
        }
        Cell cell;
        switch (table.size()) {
            case 0:
                return of();
            case 1:
                cell = (Cell) Iterables.getOnlyElement(table.cellSet());
                return of(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            default:
                com.google.common.collect.ImmutableSet.Builder builder = ImmutableSet.builder();
                for (Cell cell2 : table.cellSet()) {
                    builder.add(cellOf(cell2.getRowKey(), cell2.getColumnKey(), cell2.getValue()));
                }
                return RegularImmutableTable.forCells(builder.build());
        }
    }

    public static final <R, C, V> ImmutableTable<R, C, V> of() {
        return EmptyImmutableTable.INSTANCE;
    }

    public static final <R, C, V> ImmutableTable<R, C, V> of(R r, C c, V v) {
        return new SingletonImmutableTable(r, c, v);
    }

    public abstract ImmutableSet<Cell<R, C, V>> cellSet();

    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public abstract ImmutableMap<R, V> column(C c);

    public abstract ImmutableSet<C> columnKeySet();

    public abstract ImmutableMap<C, Map<R, V>> columnMap();

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Table)) {
            return false;
        }
        return cellSet().equals(((Table) obj).cellSet());
    }

    public int hashCode() {
        return cellSet().hashCode();
    }

    public final V put(R r, C c, V v) {
        throw new UnsupportedOperationException();
    }

    public final void putAll(Table<? extends R, ? extends C, ? extends V> table) {
        throw new UnsupportedOperationException();
    }

    public final V remove(Object obj, Object obj2) {
        throw new UnsupportedOperationException();
    }

    public abstract ImmutableMap<C, V> row(R r);

    public abstract ImmutableSet<R> rowKeySet();

    public abstract ImmutableMap<R, Map<C, V>> rowMap();

    public String toString() {
        return rowMap().toString();
    }
}
