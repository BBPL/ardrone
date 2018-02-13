package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

public final class ConcurrentHashMultiset<E> extends AbstractMultiset<E> implements Serializable {
    private static final long serialVersionUID = 1;
    private final transient ConcurrentMap<E, AtomicInteger> countMap;
    private transient EntrySet entrySet;

    class C05662 extends AbstractIterator<Entry<E>> {
        private Iterator<Map.Entry<E, AtomicInteger>> mapEntries = ConcurrentHashMultiset.this.countMap.entrySet().iterator();

        C05662() {
        }

        protected Entry<E> computeNext() {
            while (this.mapEntries.hasNext()) {
                Map.Entry entry = (Map.Entry) this.mapEntries.next();
                int i = ((AtomicInteger) entry.getValue()).get();
                if (i != 0) {
                    return Multisets.immutableEntry(entry.getKey(), i);
                }
            }
            return (Entry) endOfData();
        }
    }

    private class EntrySet extends EntrySet {
        private EntrySet() {
            super();
        }

        private List<Entry<E>> snapshot() {
            Object newArrayListWithExpectedSize = Lists.newArrayListWithExpectedSize(size());
            Iterators.addAll(newArrayListWithExpectedSize, iterator());
            return newArrayListWithExpectedSize;
        }

        ConcurrentHashMultiset<E> multiset() {
            return ConcurrentHashMultiset.this;
        }

        public boolean remove(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            Object element = entry.getElement();
            int count = entry.getCount();
            return count != 0 ? multiset().setCount(element, count, 0) : false;
        }

        public Object[] toArray() {
            return snapshot().toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return snapshot().toArray(tArr);
        }
    }

    private static class FieldSettersHolder {
        static final FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");

        private FieldSettersHolder() {
        }
    }

    @VisibleForTesting
    ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> concurrentMap) {
        Preconditions.checkArgument(concurrentMap.isEmpty());
        this.countMap = concurrentMap;
    }

    public static <E> ConcurrentHashMultiset<E> create() {
        return new ConcurrentHashMultiset(new ConcurrentHashMap());
    }

    @Beta
    public static <E> ConcurrentHashMultiset<E> create(GenericMapMaker<? super E, ? super Number> genericMapMaker) {
        return new ConcurrentHashMultiset(genericMapMaker.makeMap());
    }

    public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> iterable) {
        Object create = create();
        Iterables.addAll(create, iterable);
        return create;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set((Object) this, (ConcurrentMap) objectInputStream.readObject());
    }

    private AtomicInteger safeGet(Object obj) {
        try {
            return (AtomicInteger) this.countMap.get(obj);
        } catch (NullPointerException e) {
            return null;
        } catch (ClassCastException e2) {
            return null;
        }
    }

    private List<E> snapshot() {
        List<E> newArrayListWithExpectedSize = Lists.newArrayListWithExpectedSize(size());
        for (Entry entry : entrySet()) {
            Object element = entry.getElement();
            for (int count = entry.getCount(); count > 0; count--) {
                newArrayListWithExpectedSize.add(element);
            }
        }
        return newArrayListWithExpectedSize;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.countMap);
    }

    public int add(E e, int i) {
        Preconditions.checkNotNull(e);
        if (i == 0) {
            return count(e);
        }
        Preconditions.checkArgument(i > 0, "Invalid occurrences: %s", Integer.valueOf(i));
        AtomicInteger safeGet;
        AtomicInteger atomicInteger;
        do {
            safeGet = safeGet(e);
            if (safeGet == null) {
                safeGet = (AtomicInteger) this.countMap.putIfAbsent(e, new AtomicInteger(i));
                if (safeGet == null) {
                    return 0;
                }
            }
            while (true) {
                int i2 = safeGet.get();
                if (i2 == 0) {
                    break;
                }
                try {
                    if (safeGet.compareAndSet(i2, IntMath.checkedAdd(i2, i))) {
                        return i2;
                    }
                } catch (ArithmeticException e2) {
                    throw new IllegalArgumentException("Overflow adding " + i + " occurrences to a count of " + i2);
                }
            }
            atomicInteger = new AtomicInteger(i);
            if (this.countMap.putIfAbsent(e, atomicInteger) == null) {
                return 0;
            }
        } while (!this.countMap.replace(e, safeGet, atomicInteger));
        return 0;
    }

    public /* bridge */ /* synthetic */ boolean add(Object obj) {
        return super.add(obj);
    }

    public /* bridge */ /* synthetic */ boolean addAll(Collection collection) {
        return super.addAll(collection);
    }

    public void clear() {
        this.countMap.clear();
    }

    public /* bridge */ /* synthetic */ boolean contains(Object obj) {
        return super.contains(obj);
    }

    public int count(@Nullable Object obj) {
        AtomicInteger safeGet = safeGet(obj);
        return safeGet == null ? 0 : safeGet.get();
    }

    Set<E> createElementSet() {
        final Set keySet = this.countMap.keySet();
        return new ForwardingSet<E>() {
            protected Set<E> delegate() {
                return keySet;
            }

            public boolean remove(Object obj) {
                boolean z = false;
                try {
                    z = keySet.remove(obj);
                } catch (NullPointerException e) {
                } catch (ClassCastException e2) {
                }
                return z;
            }

            public boolean removeAll(Collection<?> collection) {
                return standardRemoveAll(collection);
            }
        };
    }

    int distinctElements() {
        return this.countMap.size();
    }

    public /* bridge */ /* synthetic */ Set elementSet() {
        return super.elementSet();
    }

    Iterator<Entry<E>> entryIterator() {
        final Iterator c05662 = new C05662();
        return new ForwardingIterator<Entry<E>>() {
            private Entry<E> last;

            protected Iterator<Entry<E>> delegate() {
                return c05662;
            }

            public Entry<E> next() {
                this.last = (Entry) super.next();
                return this.last;
            }

            public void remove() {
                Preconditions.checkState(this.last != null);
                ConcurrentHashMultiset.this.setCount(this.last.getElement(), 0);
                this.last = null;
            }
        };
    }

    public Set<Entry<E>> entrySet() {
        Set set = this.entrySet;
        if (set != null) {
            return set;
        }
        set = new EntrySet();
        this.entrySet = set;
        return set;
    }

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public boolean isEmpty() {
        return this.countMap.isEmpty();
    }

    public /* bridge */ /* synthetic */ Iterator iterator() {
        return super.iterator();
    }

    public int remove(@Nullable Object obj, int i) {
        if (i == 0) {
            return count(obj);
        }
        Preconditions.checkArgument(i > 0, "Invalid occurrences: %s", Integer.valueOf(i));
        AtomicInteger safeGet = safeGet(obj);
        if (safeGet == null) {
            return 0;
        }
        int i2;
        int max;
        do {
            i2 = safeGet.get();
            if (i2 == 0) {
                return 0;
            }
            max = Math.max(0, i2 - i);
        } while (!safeGet.compareAndSet(i2, max));
        if (max == 0) {
            this.countMap.remove(obj, safeGet);
        }
        return i2;
    }

    public /* bridge */ /* synthetic */ boolean remove(Object obj) {
        return super.remove(obj);
    }

    public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
        return super.removeAll(collection);
    }

    public boolean removeExactly(@Nullable Object obj, int i) {
        if (i == 0) {
            return true;
        }
        Preconditions.checkArgument(i > 0, "Invalid occurrences: %s", Integer.valueOf(i));
        AtomicInteger safeGet = safeGet(obj);
        if (safeGet == null) {
            return false;
        }
        int i2;
        int i3;
        do {
            i2 = safeGet.get();
            if (i2 < i) {
                return false;
            }
            i3 = i2 - i;
        } while (!safeGet.compareAndSet(i2, i3));
        if (i3 != 0) {
            return true;
        }
        this.countMap.remove(obj, safeGet);
        return true;
    }

    public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
        return super.retainAll(collection);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int setCount(E r5, int r6) {
        /*
        r4 = this;
        r1 = 0;
        com.google.common.base.Preconditions.checkNotNull(r5);
        r0 = "count";
        com.google.common.collect.Multisets.checkNonnegative(r6, r0);
    L_0x0009:
        r0 = r4.safeGet(r5);
        if (r0 != 0) goto L_0x0023;
    L_0x000f:
        if (r6 != 0) goto L_0x0014;
    L_0x0011:
        r0 = r1;
    L_0x0012:
        r1 = r0;
    L_0x0013:
        return r1;
    L_0x0014:
        r0 = r4.countMap;
        r2 = new java.util.concurrent.atomic.AtomicInteger;
        r2.<init>(r6);
        r0 = r0.putIfAbsent(r5, r2);
        r0 = (java.util.concurrent.atomic.AtomicInteger) r0;
        if (r0 == 0) goto L_0x0013;
    L_0x0023:
        r2 = r0;
    L_0x0024:
        r0 = r2.get();
        if (r0 != 0) goto L_0x0042;
    L_0x002a:
        if (r6 == 0) goto L_0x0013;
    L_0x002c:
        r0 = new java.util.concurrent.atomic.AtomicInteger;
        r0.<init>(r6);
        r3 = r4.countMap;
        r3 = r3.putIfAbsent(r5, r0);
        if (r3 == 0) goto L_0x0013;
    L_0x0039:
        r3 = r4.countMap;
        r0 = r3.replace(r5, r2, r0);
        if (r0 == 0) goto L_0x0009;
    L_0x0041:
        goto L_0x0013;
    L_0x0042:
        r3 = r2.compareAndSet(r0, r6);
        if (r3 == 0) goto L_0x0024;
    L_0x0048:
        if (r6 != 0) goto L_0x0012;
    L_0x004a:
        r1 = r4.countMap;
        r1.remove(r5, r2);
        r1 = r0;
        goto L_0x0013;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ConcurrentHashMultiset.setCount(java.lang.Object, int):int");
    }

    public boolean setCount(E e, int i, int i2) {
        Preconditions.checkNotNull(e);
        Multisets.checkNonnegative(i, "oldCount");
        Multisets.checkNonnegative(i2, "newCount");
        AtomicInteger safeGet = safeGet(e);
        if (safeGet != null) {
            int i3 = safeGet.get();
            if (i3 != i) {
                return false;
            }
            if (i3 == 0) {
                if (i2 == 0) {
                    this.countMap.remove(e, safeGet);
                    return true;
                }
                AtomicInteger atomicInteger = new AtomicInteger(i2);
                return this.countMap.putIfAbsent(e, atomicInteger) == null || this.countMap.replace(e, safeGet, atomicInteger);
            } else if (!safeGet.compareAndSet(i3, i2)) {
                return false;
            } else {
                if (i2 == 0) {
                    this.countMap.remove(e, safeGet);
                    return true;
                }
            }
        } else if (i != 0) {
            return false;
        } else {
            if (!(i2 == 0 || this.countMap.putIfAbsent(e, new AtomicInteger(i2)) == null)) {
                return false;
            }
        }
        return true;
    }

    public int size() {
        long j = 0;
        for (AtomicInteger atomicInteger : this.countMap.values()) {
            j = ((long) atomicInteger.get()) + j;
        }
        return Ints.saturatedCast(j);
    }

    public Object[] toArray() {
        return snapshot().toArray();
    }

    public <T> T[] toArray(T[] tArr) {
        return snapshot().toArray(tArr);
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }
}
