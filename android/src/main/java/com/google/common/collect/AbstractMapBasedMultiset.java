package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
abstract class AbstractMapBasedMultiset<E> extends AbstractMultiset<E> implements Serializable {
    @GwtIncompatible("not needed in emulated source.")
    private static final long serialVersionUID = -2250766705698539974L;
    private transient Map<E, Count> backingMap;
    private transient long size = ((long) super.size());

    class MapBasedElementSet extends ElementSet<E> {
        MapBasedElementSet() {
        }

        Multiset<E> multiset() {
            return AbstractMapBasedMultiset.this;
        }
    }

    private class MapBasedMultisetIterator implements Iterator<E> {
        boolean canRemove;
        Entry<E, Count> currentEntry;
        final Iterator<Entry<E, Count>> entryIterator;
        int occurrencesLeft;

        MapBasedMultisetIterator() {
            this.entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
        }

        public boolean hasNext() {
            return this.occurrencesLeft > 0 || this.entryIterator.hasNext();
        }

        public E next() {
            if (this.occurrencesLeft == 0) {
                this.currentEntry = (Entry) this.entryIterator.next();
                this.occurrencesLeft = ((Count) this.currentEntry.getValue()).get();
            }
            this.occurrencesLeft--;
            this.canRemove = true;
            return this.currentEntry.getKey();
        }

        public void remove() {
            Preconditions.checkState(this.canRemove, "no calls to next() since the last call to remove()");
            if (((Count) this.currentEntry.getValue()).get() <= 0) {
                throw new ConcurrentModificationException();
            }
            if (((Count) this.currentEntry.getValue()).addAndGet(-1) == 0) {
                this.entryIterator.remove();
            }
            AbstractMapBasedMultiset.this.size = AbstractMapBasedMultiset.this.size - 1;
            this.canRemove = false;
        }
    }

    protected AbstractMapBasedMultiset(Map<E, Count> map) {
        this.backingMap = (Map) Preconditions.checkNotNull(map);
    }

    static /* synthetic */ long access$122(AbstractMapBasedMultiset abstractMapBasedMultiset, long j) {
        long j2 = abstractMapBasedMultiset.size - j;
        abstractMapBasedMultiset.size = j2;
        return j2;
    }

    private static int getAndSet(Count count, int i) {
        return count == null ? 0 : count.getAndSet(i);
    }

    @GwtIncompatible("java.io.ObjectStreamException")
    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("Stream data required");
    }

    public int add(@Nullable E e, int i) {
        int i2 = 0;
        if (i == 0) {
            return count(e);
        }
        Preconditions.checkArgument(i > 0, "occurrences cannot be negative: %s", Integer.valueOf(i));
        Count count = (Count) this.backingMap.get(e);
        if (count == null) {
            this.backingMap.put(e, new Count(i));
        } else {
            int i3 = count.get();
            Preconditions.checkArgument(((long) i) + ((long) i3) <= 2147483647L, "too many occurrences: %s", Long.valueOf(((long) i) + ((long) i3)));
            count.getAndAdd(i);
            i2 = i3;
        }
        this.size += (long) i;
        return i2;
    }

    Map<E, Count> backingMap() {
        return this.backingMap;
    }

    public void clear() {
        for (Count count : this.backingMap.values()) {
            count.set(0);
        }
        this.backingMap.clear();
        this.size = 0;
    }

    public int count(@Nullable Object obj) {
        try {
            Count count = (Count) this.backingMap.get(obj);
            return count == null ? 0 : count.get();
        } catch (NullPointerException e) {
            return 0;
        } catch (ClassCastException e2) {
            return 0;
        }
    }

    Set<E> createElementSet() {
        return new MapBasedElementSet();
    }

    int distinctElements() {
        return this.backingMap.size();
    }

    Iterator<Multiset.Entry<E>> entryIterator() {
        final Iterator it = this.backingMap.entrySet().iterator();
        return new Iterator<Multiset.Entry<E>>() {
            Entry<E, Count> toRemove;

            public boolean hasNext() {
                return it.hasNext();
            }

            public Multiset.Entry<E> next() {
                final Entry entry = (Entry) it.next();
                this.toRemove = entry;
                return new AbstractEntry<E>() {
                    public int getCount() {
                        int i = ((Count) entry.getValue()).get();
                        if (i == 0) {
                            Count count = (Count) AbstractMapBasedMultiset.this.backingMap.get(getElement());
                            if (count != null) {
                                return count.get();
                            }
                        }
                        return i;
                    }

                    public E getElement() {
                        return entry.getKey();
                    }
                };
            }

            public void remove() {
                Iterators.checkRemove(this.toRemove != null);
                AbstractMapBasedMultiset.access$122(AbstractMapBasedMultiset.this, (long) ((Count) this.toRemove.getValue()).getAndSet(0));
                it.remove();
                this.toRemove = null;
            }
        };
    }

    public Set<Multiset.Entry<E>> entrySet() {
        return super.entrySet();
    }

    public Iterator<E> iterator() {
        return new MapBasedMultisetIterator();
    }

    public int remove(@Nullable Object obj, int i) {
        if (i == 0) {
            return count(obj);
        }
        Preconditions.checkArgument(i > 0, "occurrences cannot be negative: %s", Integer.valueOf(i));
        Count count = (Count) this.backingMap.get(obj);
        if (count == null) {
            return 0;
        }
        int i2 = count.get();
        if (i2 <= i) {
            this.backingMap.remove(obj);
            i = i2;
        }
        count.addAndGet(-i);
        this.size -= (long) i;
        return i2;
    }

    void setBackingMap(Map<E, Count> map) {
        this.backingMap = map;
    }

    public int setCount(@Nullable E e, int i) {
        int andSet;
        Multisets.checkNonnegative(i, "count");
        if (i == 0) {
            andSet = getAndSet((Count) this.backingMap.remove(e), i);
        } else {
            Count count = (Count) this.backingMap.get(e);
            int andSet2 = getAndSet(count, i);
            if (count == null) {
                this.backingMap.put(e, new Count(i));
                andSet = andSet2;
            } else {
                andSet = andSet2;
            }
        }
        this.size += (long) (i - andSet);
        return andSet;
    }

    public int size() {
        return Ints.saturatedCast(this.size);
    }
}
