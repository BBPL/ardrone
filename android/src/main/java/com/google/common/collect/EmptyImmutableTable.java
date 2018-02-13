package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Table.Cell;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible
@Immutable
final class EmptyImmutableTable extends ImmutableTable<Object, Object, Object> {
    static final EmptyImmutableTable INSTANCE = new EmptyImmutableTable();
    private static final long serialVersionUID = 0;

    private EmptyImmutableTable() {
    }

    public ImmutableSet<Cell<Object, Object, Object>> cellSet() {
        return ImmutableSet.of();
    }

    public ImmutableMap<Object, Object> column(Object obj) {
        Preconditions.checkNotNull(obj);
        return ImmutableMap.of();
    }

    public ImmutableSet<Object> columnKeySet() {
        return ImmutableSet.of();
    }

    public ImmutableMap<Object, Map<Object, Object>> columnMap() {
        return ImmutableMap.of();
    }

    public boolean contains(@Nullable Object obj, @Nullable Object obj2) {
        return false;
    }

    public boolean containsColumn(@Nullable Object obj) {
        return false;
    }

    public boolean containsRow(@Nullable Object obj) {
        return false;
    }

    public boolean containsValue(@Nullable Object obj) {
        return false;
    }

    public boolean equals(@Nullable Object obj) {
        return obj == this ? true : obj instanceof Table ? ((Table) obj).isEmpty() : false;
    }

    public Object get(@Nullable Object obj, @Nullable Object obj2) {
        return null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    Object readResolve() {
        return INSTANCE;
    }

    public ImmutableMap<Object, Object> row(Object obj) {
        Preconditions.checkNotNull(obj);
        return ImmutableMap.of();
    }

    public ImmutableSet<Object> rowKeySet() {
        return ImmutableSet.of();
    }

    public ImmutableMap<Object, Map<Object, Object>> rowMap() {
        return ImmutableMap.of();
    }

    public int size() {
        return 0;
    }

    public String toString() {
        return "{}";
    }

    public ImmutableCollection<Object> values() {
        return ImmutableSet.of();
    }
}
