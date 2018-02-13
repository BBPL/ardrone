package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public final class LinkedHashMultimap<K, V> extends AbstractSetMultimap<K, V> {
    private static final int DEFAULT_KEY_CAPACITY = 16;
    private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
    private static final int MAX_VALUE_SET_TABLE_SIZE = 1073741824;
    @GwtIncompatible("java serialization not supported")
    private static final long serialVersionUID = 1;
    private transient ValueEntry<K, V> multimapHeaderEntry;
    @VisibleForTesting
    transient int valueSetCapacity = 2;

    class C06091 implements Iterator<Entry<K, V>> {
        ValueEntry<K, V> nextEntry = LinkedHashMultimap.this.multimapHeaderEntry.successorInMultimap;
        ValueEntry<K, V> toRemove;

        C06091() {
        }

        public boolean hasNext() {
            return this.nextEntry != LinkedHashMultimap.this.multimapHeaderEntry;
        }

        public Entry<K, V> next() {
            if (hasNext()) {
                Entry entry = this.nextEntry;
                this.toRemove = entry;
                this.nextEntry = this.nextEntry.successorInMultimap;
                return entry;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            Iterators.checkRemove(this.toRemove != null);
            LinkedHashMultimap.this.remove(this.toRemove.getKey(), this.toRemove.getValue());
            this.toRemove = null;
        }
    }

    private interface ValueSetLink<K, V> {
        ValueSetLink<K, V> getPredecessorInValueSet();

        ValueSetLink<K, V> getSuccessorInValueSet();

        void setPredecessorInValueSet(ValueSetLink<K, V> valueSetLink);

        void setSuccessorInValueSet(ValueSetLink<K, V> valueSetLink);
    }

    private static final class ValueEntry<K, V> extends AbstractMapEntry<K, V> implements ValueSetLink<K, V> {
        final K key;
        @Nullable
        ValueEntry<K, V> nextInValueSetHashRow;
        ValueEntry<K, V> predecessorInMultimap;
        ValueSetLink<K, V> predecessorInValueSet;
        ValueEntry<K, V> successorInMultimap;
        ValueSetLink<K, V> successorInValueSet;
        final V value;
        final int valueHash;

        ValueEntry(@Nullable K k, @Nullable V v, int i, @Nullable ValueEntry<K, V> valueEntry) {
            this.key = k;
            this.value = v;
            this.valueHash = i;
            this.nextInValueSetHashRow = valueEntry;
        }

        public K getKey() {
            return this.key;
        }

        public ValueEntry<K, V> getPredecessorInMultimap() {
            return this.predecessorInMultimap;
        }

        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return this.predecessorInValueSet;
        }

        public ValueEntry<K, V> getSuccessorInMultimap() {
            return this.successorInMultimap;
        }

        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.successorInValueSet;
        }

        public V getValue() {
            return this.value;
        }

        public void setPredecessorInMultimap(ValueEntry<K, V> valueEntry) {
            this.predecessorInMultimap = valueEntry;
        }

        public void setPredecessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.predecessorInValueSet = valueSetLink;
        }

        public void setSuccessorInMultimap(ValueEntry<K, V> valueEntry) {
            this.successorInMultimap = valueEntry;
        }

        public void setSuccessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.successorInValueSet = valueSetLink;
        }
    }

    @VisibleForTesting
    final class ValueSet extends ImprovedAbstractSet<V> implements ValueSetLink<K, V> {
        private ValueSetLink<K, V> firstEntry;
        private ValueEntry<K, V>[] hashTable;
        private final K key;
        private ValueSetLink<K, V> lastEntry;
        private int modCount = 0;
        private int size = 0;

        class C06101 implements Iterator<V> {
            int expectedModCount = ValueSet.this.modCount;
            ValueSetLink<K, V> nextEntry = ValueSet.this.firstEntry;
            ValueEntry<K, V> toRemove;

            C06101() {
            }

            private void checkForComodification() {
                if (ValueSet.this.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }

            public boolean hasNext() {
                checkForComodification();
                return this.nextEntry != ValueSet.this;
            }

            public V next() {
                if (hasNext()) {
                    ValueEntry valueEntry = (ValueEntry) this.nextEntry;
                    V value = valueEntry.getValue();
                    this.toRemove = valueEntry;
                    this.nextEntry = valueEntry.getSuccessorInValueSet();
                    return value;
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                int i = 0;
                checkForComodification();
                Iterators.checkRemove(this.toRemove != null);
                Object value = this.toRemove.getValue();
                if (value != null) {
                    i = value.hashCode();
                }
                int smear = Hashing.smear(i) & (ValueSet.this.hashTable.length - 1);
                ValueEntry valueEntry = ValueSet.this.hashTable[smear];
                ValueEntry valueEntry2 = null;
                while (valueEntry != null) {
                    if (valueEntry == this.toRemove) {
                        if (valueEntry2 == null) {
                            ValueSet.this.hashTable[smear] = valueEntry.nextInValueSetHashRow;
                        } else {
                            valueEntry2.nextInValueSetHashRow = valueEntry.nextInValueSetHashRow;
                        }
                        LinkedHashMultimap.deleteFromValueSet(this.toRemove);
                        LinkedHashMultimap.deleteFromMultimap(this.toRemove);
                        ValueSet.this.size = ValueSet.this.size - 1;
                        this.expectedModCount = ValueSet.access$104(ValueSet.this);
                        this.toRemove = null;
                    }
                    ValueEntry valueEntry3 = valueEntry;
                    valueEntry = valueEntry.nextInValueSetHashRow;
                    valueEntry2 = valueEntry3;
                }
                this.toRemove = null;
            }
        }

        ValueSet(K k, int i) {
            this.key = k;
            this.firstEntry = this;
            this.lastEntry = this;
            int highestOneBit = Integer.highestOneBit(Math.max(i, 2) - 1) << 1;
            if (highestOneBit < 0) {
                highestOneBit = 1073741824;
            }
            this.hashTable = new ValueEntry[highestOneBit];
        }

        static /* synthetic */ int access$104(ValueSet valueSet) {
            int i = valueSet.modCount + 1;
            valueSet.modCount = i;
            return i;
        }

        private void rehashIfNecessary() {
            if (this.size > threshold() && this.hashTable.length < 1073741824) {
                ValueEntry[] valueEntryArr = new ValueEntry[(this.hashTable.length * 2)];
                this.hashTable = valueEntryArr;
                int length = valueEntryArr.length;
                for (ValueSet valueSet = this.firstEntry; valueSet != this; valueSet = valueSet.getSuccessorInValueSet()) {
                    ValueEntry valueEntry = (ValueEntry) valueSet;
                    int smear = Hashing.smear(valueEntry.valueHash) & (length - 1);
                    valueEntry.nextInValueSetHashRow = valueEntryArr[smear];
                    valueEntryArr[smear] = valueEntry;
                }
            }
        }

        public boolean add(@Nullable V v) {
            int hashCode = v == null ? 0 : v.hashCode();
            int smear = Hashing.smear(hashCode) & (this.hashTable.length - 1);
            ValueEntry valueEntry = this.hashTable[smear];
            ValueEntry valueEntry2 = valueEntry;
            while (valueEntry2 != null) {
                if (hashCode == valueEntry2.valueHash && Objects.equal(v, valueEntry2.getValue())) {
                    return false;
                }
                valueEntry2 = valueEntry2.nextInValueSetHashRow;
            }
            ValueEntry valueEntry3 = new ValueEntry(this.key, v, hashCode, valueEntry);
            LinkedHashMultimap.succeedsInValueSet(this.lastEntry, valueEntry3);
            LinkedHashMultimap.succeedsInValueSet(valueEntry3, this);
            LinkedHashMultimap.succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), valueEntry3);
            LinkedHashMultimap.succeedsInMultimap(valueEntry3, LinkedHashMultimap.this.multimapHeaderEntry);
            this.hashTable[smear] = valueEntry3;
            this.size++;
            this.modCount++;
            rehashIfNecessary();
            return true;
        }

        public void clear() {
            Arrays.fill(this.hashTable, null);
            this.size = 0;
            for (ValueSet valueSet = this.firstEntry; valueSet != this; valueSet = valueSet.getSuccessorInValueSet()) {
                LinkedHashMultimap.deleteFromMultimap((ValueEntry) valueSet);
            }
            LinkedHashMultimap.succeedsInValueSet(this, this);
            this.modCount++;
        }

        public boolean contains(@Nullable Object obj) {
            int hashCode = obj == null ? 0 : obj.hashCode();
            ValueEntry valueEntry = this.hashTable[Hashing.smear(hashCode) & (this.hashTable.length - 1)];
            while (valueEntry != null) {
                if (hashCode == valueEntry.valueHash && Objects.equal(obj, valueEntry.getValue())) {
                    return true;
                }
                valueEntry = valueEntry.nextInValueSetHashRow;
            }
            return false;
        }

        public ValueSetLink<K, V> getPredecessorInValueSet() {
            return this.lastEntry;
        }

        public ValueSetLink<K, V> getSuccessorInValueSet() {
            return this.firstEntry;
        }

        public Iterator<V> iterator() {
            return new C06101();
        }

        public boolean remove(@Nullable Object obj) {
            int hashCode = obj == null ? 0 : obj.hashCode();
            int smear = Hashing.smear(hashCode) & (this.hashTable.length - 1);
            ValueEntry valueEntry = null;
            ValueEntry valueEntry2 = this.hashTable[smear];
            while (valueEntry2 != null) {
                if (hashCode == valueEntry2.valueHash && Objects.equal(obj, valueEntry2.getValue())) {
                    if (valueEntry == null) {
                        this.hashTable[smear] = valueEntry2.nextInValueSetHashRow;
                    } else {
                        valueEntry.nextInValueSetHashRow = valueEntry2.nextInValueSetHashRow;
                    }
                    LinkedHashMultimap.deleteFromValueSet(valueEntry2);
                    LinkedHashMultimap.deleteFromMultimap(valueEntry2);
                    this.size--;
                    this.modCount++;
                    return true;
                }
                ValueEntry valueEntry3 = valueEntry2;
                valueEntry2 = valueEntry2.nextInValueSetHashRow;
                valueEntry = valueEntry3;
            }
            return false;
        }

        public void setPredecessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.lastEntry = valueSetLink;
        }

        public void setSuccessorInValueSet(ValueSetLink<K, V> valueSetLink) {
            this.firstEntry = valueSetLink;
        }

        public int size() {
            return this.size;
        }

        @VisibleForTesting
        int threshold() {
            return this.hashTable.length;
        }
    }

    private LinkedHashMultimap(int i, int i2) {
        super(new LinkedHashMap(i));
        Preconditions.checkArgument(i2 >= 0, "expectedValuesPerKey must be >= 0 but was %s", Integer.valueOf(i2));
        this.valueSetCapacity = i2;
        this.multimapHeaderEntry = new ValueEntry(null, null, 0, null);
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    }

    public static <K, V> LinkedHashMultimap<K, V> create() {
        return new LinkedHashMultimap(16, 2);
    }

    public static <K, V> LinkedHashMultimap<K, V> create(int i, int i2) {
        return new LinkedHashMultimap(Maps.capacity(i), Maps.capacity(i2));
    }

    public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        LinkedHashMultimap<K, V> create = create(multimap.keySet().size(), 2);
        create.putAll(multimap);
        return create;
    }

    private static <K, V> void deleteFromMultimap(ValueEntry<K, V> valueEntry) {
        succeedsInMultimap(valueEntry.getPredecessorInMultimap(), valueEntry.getSuccessorInMultimap());
    }

    private static <K, V> void deleteFromValueSet(ValueSetLink<K, V> valueSetLink) {
        succeedsInValueSet(valueSetLink.getPredecessorInValueSet(), valueSetLink.getSuccessorInValueSet());
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int i;
        objectInputStream.defaultReadObject();
        this.multimapHeaderEntry = new ValueEntry(null, null, 0, null);
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
        this.valueSetCapacity = objectInputStream.readInt();
        int readInt = objectInputStream.readInt();
        Map linkedHashMap = new LinkedHashMap(Maps.capacity(readInt));
        for (i = 0; i < readInt; i++) {
            Object readObject = objectInputStream.readObject();
            linkedHashMap.put(readObject, createCollection(readObject));
        }
        readInt = objectInputStream.readInt();
        for (i = 0; i < readInt; i++) {
            Object readObject2 = objectInputStream.readObject();
            ((Collection) linkedHashMap.get(readObject2)).add(objectInputStream.readObject());
        }
        setMap(linkedHashMap);
    }

    private static <K, V> void succeedsInMultimap(ValueEntry<K, V> valueEntry, ValueEntry<K, V> valueEntry2) {
        valueEntry.setSuccessorInMultimap(valueEntry2);
        valueEntry2.setPredecessorInMultimap(valueEntry);
    }

    private static <K, V> void succeedsInValueSet(ValueSetLink<K, V> valueSetLink, ValueSetLink<K, V> valueSetLink2) {
        valueSetLink.setSuccessorInValueSet(valueSetLink2);
        valueSetLink2.setPredecessorInValueSet(valueSetLink);
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.valueSetCapacity);
        objectOutputStream.writeInt(keySet().size());
        for (Object writeObject : keySet()) {
            objectOutputStream.writeObject(writeObject);
        }
        objectOutputStream.writeInt(size());
        for (Entry entry : entries()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    public /* bridge */ /* synthetic */ Map asMap() {
        return super.asMap();
    }

    public void clear() {
        super.clear();
        succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
    }

    public /* bridge */ /* synthetic */ boolean containsEntry(Object obj, Object obj2) {
        return super.containsEntry(obj, obj2);
    }

    public /* bridge */ /* synthetic */ boolean containsKey(Object obj) {
        return super.containsKey(obj);
    }

    public /* bridge */ /* synthetic */ boolean containsValue(Object obj) {
        return super.containsValue(obj);
    }

    Collection<V> createCollection(K k) {
        return new ValueSet(k, this.valueSetCapacity);
    }

    Set<V> createCollection() {
        return new LinkedHashSet(this.valueSetCapacity);
    }

    Iterator<Entry<K, V>> createEntryIterator() {
        return new C06091();
    }

    public Set<Entry<K, V>> entries() {
        return super.entries();
    }

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public /* bridge */ /* synthetic */ Set get(Object obj) {
        return super.get(obj);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ Set keySet() {
        return super.keySet();
    }

    public /* bridge */ /* synthetic */ Multiset keys() {
        return super.keys();
    }

    public /* bridge */ /* synthetic */ boolean put(Object obj, Object obj2) {
        return super.put(obj, obj2);
    }

    public /* bridge */ /* synthetic */ boolean putAll(Multimap multimap) {
        return super.putAll(multimap);
    }

    public /* bridge */ /* synthetic */ boolean putAll(Object obj, Iterable iterable) {
        return super.putAll(obj, iterable);
    }

    public /* bridge */ /* synthetic */ boolean remove(Object obj, Object obj2) {
        return super.remove(obj, obj2);
    }

    public /* bridge */ /* synthetic */ Set removeAll(Object obj) {
        return super.removeAll(obj);
    }

    public Set<V> replaceValues(K k, Iterable<? extends V> iterable) {
        return super.replaceValues((Object) k, (Iterable) iterable);
    }

    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public Collection<V> values() {
        return super.values();
    }
}
