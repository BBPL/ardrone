package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@GwtCompatible(emulated = true, serializable = true)
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    private static final double MAX_LOAD_FACTOR = 1.2d;
    private static final long serialVersionUID = 0;
    private final transient LinkedEntry<K, V>[] entries;
    private final transient int keySetHashCode;
    private final transient int mask;
    private final transient LinkedEntry<K, V>[] table;

    private class EntrySet extends ImmutableMapEntrySet<K, V> {
        private EntrySet() {
        }

        ImmutableList<Entry<K, V>> createAsList() {
            return new RegularImmutableAsList((ImmutableCollection) this, RegularImmutableMap.this.entries);
        }

        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return asList().iterator();
        }

        ImmutableMap<K, V> map() {
            return RegularImmutableMap.this;
        }
    }

    private interface LinkedEntry<K, V> extends Entry<K, V> {
        @Nullable
        LinkedEntry<K, V> next();
    }

    @Immutable
    private static final class NonTerminalEntry<K, V> extends ImmutableEntry<K, V> implements LinkedEntry<K, V> {
        final LinkedEntry<K, V> next;

        NonTerminalEntry(K k, V v, LinkedEntry<K, V> linkedEntry) {
            super(k, v);
            this.next = linkedEntry;
        }

        public LinkedEntry<K, V> next() {
            return this.next;
        }
    }

    @Immutable
    private static final class TerminalEntry<K, V> extends ImmutableEntry<K, V> implements LinkedEntry<K, V> {
        TerminalEntry(K k, V v) {
            super(k, v);
        }

        @Nullable
        public LinkedEntry<K, V> next() {
            return null;
        }
    }

    RegularImmutableMap(Entry<?, ?>... entryArr) {
        int length = entryArr.length;
        this.entries = createEntryArray(length);
        int chooseTableSize = chooseTableSize(length);
        this.table = createEntryArray(chooseTableSize);
        this.mask = chooseTableSize - 1;
        chooseTableSize = 0;
        int i = 0;
        while (i < length) {
            Entry entry = entryArr[i];
            Object key = entry.getKey();
            int hashCode = key.hashCode();
            int i2 = chooseTableSize + hashCode;
            hashCode = this.mask & Hashing.smear(hashCode);
            LinkedEntry linkedEntry = this.table[hashCode];
            LinkedEntry newLinkedEntry = newLinkedEntry(key, entry.getValue(), linkedEntry);
            this.table[hashCode] = newLinkedEntry;
            this.entries[i] = newLinkedEntry;
            for (newLinkedEntry = linkedEntry; newLinkedEntry != null; newLinkedEntry = newLinkedEntry.next()) {
                Preconditions.checkArgument(!key.equals(newLinkedEntry.getKey()), "duplicate key: %s", key);
            }
            i++;
            chooseTableSize = i2;
        }
        this.keySetHashCode = chooseTableSize;
    }

    private static int chooseTableSize(int i) {
        int highestOneBit = Integer.highestOneBit(i);
        if (((double) i) / ((double) highestOneBit) <= MAX_LOAD_FACTOR) {
            return highestOneBit;
        }
        int i2 = highestOneBit << 1;
        Preconditions.checkArgument(i2 > 0, "table too large: %s", Integer.valueOf(i));
        return i2;
    }

    private LinkedEntry<K, V>[] createEntryArray(int i) {
        return new LinkedEntry[i];
    }

    private static <K, V> LinkedEntry<K, V> newLinkedEntry(K k, V v, @Nullable LinkedEntry<K, V> linkedEntry) {
        return linkedEntry == null ? new TerminalEntry(k, v) : new NonTerminalEntry(k, v, linkedEntry);
    }

    public boolean containsValue(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        for (Entry value : this.entries) {
            if (value.getValue().equals(obj)) {
                return true;
            }
        }
        return false;
    }

    ImmutableSet<Entry<K, V>> createEntrySet() {
        return new EntrySet();
    }

    ImmutableSet<K> createKeySet() {
        return new ImmutableMapKeySet<K, V>(entrySet(), this.keySetHashCode) {
            ImmutableMap<K, V> map() {
                return RegularImmutableMap.this;
            }
        };
    }

    public V get(@Nullable Object obj) {
        if (obj != null) {
            for (LinkedEntry linkedEntry = this.table[Hashing.smear(obj.hashCode()) & this.mask]; linkedEntry != null; linkedEntry = linkedEntry.next()) {
                if (obj.equals(linkedEntry.getKey())) {
                    return linkedEntry.getValue();
                }
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return false;
    }

    boolean isPartialView() {
        return false;
    }

    public int size() {
        return this.entries.length;
    }

    public String toString() {
        StringBuilder append = Collections2.newStringBuilderForCollection(size()).append('{');
        Collections2.STANDARD_JOINER.appendTo(append, this.entries);
        return append.append('}').toString();
    }
}
