package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Multiset.Entry;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
class RegularImmutableMultiset<E> extends ImmutableMultiset<E> {
    private final transient ImmutableMap<E, Integer> map;
    private final transient int size;

    private class EntrySet extends EntrySet {
        private EntrySet() {
            super();
        }

        ImmutableList<Entry<E>> createAsList() {
            final ImmutableList asList = RegularImmutableMultiset.this.map.entrySet().asList();
            return new ImmutableAsList<Entry<E>>() {
                ImmutableCollection<Entry<E>> delegateCollection() {
                    return EntrySet.this;
                }

                public Entry<E> get(int i) {
                    return RegularImmutableMultiset.entryFromMapEntry((Map.Entry) asList.get(i));
                }
            };
        }

        public UnmodifiableIterator<Entry<E>> iterator() {
            return asList().iterator();
        }

        public int size() {
            return RegularImmutableMultiset.this.map.size();
        }
    }

    RegularImmutableMultiset(ImmutableMap<E, Integer> immutableMap, int i) {
        this.map = immutableMap;
        this.size = i;
    }

    private static <E> Entry<E> entryFromMapEntry(Map.Entry<E, Integer> entry) {
        return Multisets.immutableEntry(entry.getKey(), ((Integer) entry.getValue()).intValue());
    }

    public boolean contains(@Nullable Object obj) {
        return this.map.containsKey(obj);
    }

    public int count(@Nullable Object obj) {
        Integer num = (Integer) this.map.get(obj);
        return num == null ? 0 : num.intValue();
    }

    ImmutableSet<Entry<E>> createEntrySet() {
        return new EntrySet();
    }

    public ImmutableSet<E> elementSet() {
        return this.map.keySet();
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    boolean isPartialView() {
        return this.map.isPartialView();
    }

    public int size() {
        return this.size;
    }
}
