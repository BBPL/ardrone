package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@GwtCompatible
@Beta
public final class AtomicLongMap<K> {
    private transient Map<K, Long> asMap;
    private final ConcurrentHashMap<K, AtomicLong> map;

    class C08611 implements Function<AtomicLong, Long> {
        C08611() {
        }

        public Long apply(AtomicLong atomicLong) {
            return Long.valueOf(atomicLong.get());
        }
    }

    private AtomicLongMap(ConcurrentHashMap<K, AtomicLong> concurrentHashMap) {
        this.map = (ConcurrentHashMap) Preconditions.checkNotNull(concurrentHashMap);
    }

    public static <K> AtomicLongMap<K> create() {
        return new AtomicLongMap(new ConcurrentHashMap());
    }

    public static <K> AtomicLongMap<K> create(Map<? extends K, ? extends Long> map) {
        AtomicLongMap<K> create = create();
        create.putAll(map);
        return create;
    }

    private Map<K, Long> createAsMap() {
        return Collections.unmodifiableMap(Maps.transformValues(this.map, new C08611()));
    }

    public long addAndGet(K k, long j) {
        AtomicLong atomicLong;
        do {
            atomicLong = (AtomicLong) this.map.get(k);
            if (atomicLong == null) {
                atomicLong = (AtomicLong) this.map.putIfAbsent(k, new AtomicLong(j));
                if (atomicLong == null) {
                    return j;
                }
            }
            while (true) {
                long j2 = atomicLong.get();
                if (j2 != 0) {
                    long j3 = j2 + j;
                    if (atomicLong.compareAndSet(j2, j3)) {
                        return j3;
                    }
                }
            }
        } while (!this.map.replace(k, atomicLong, new AtomicLong(j)));
        return j;
    }

    public Map<K, Long> asMap() {
        Map<K, Long> map = this.asMap;
        if (map != null) {
            return map;
        }
        map = createAsMap();
        this.asMap = map;
        return map;
    }

    public void clear() {
        this.map.clear();
    }

    public boolean containsKey(Object obj) {
        return this.map.containsKey(obj);
    }

    public long decrementAndGet(K k) {
        return addAndGet(k, -1);
    }

    public long get(K k) {
        AtomicLong atomicLong = (AtomicLong) this.map.get(k);
        return atomicLong == null ? 0 : atomicLong.get();
    }

    public long getAndAdd(K k, long j) {
        AtomicLong atomicLong;
        do {
            atomicLong = (AtomicLong) this.map.get(k);
            if (atomicLong == null) {
                atomicLong = (AtomicLong) this.map.putIfAbsent(k, new AtomicLong(j));
                if (atomicLong == null) {
                    return 0;
                }
            }
            while (true) {
                long j2 = atomicLong.get();
                if (j2 != 0) {
                    if (atomicLong.compareAndSet(j2, j2 + j)) {
                        return j2;
                    }
                }
            }
        } while (!this.map.replace(k, atomicLong, new AtomicLong(j)));
        return 0;
    }

    public long getAndDecrement(K k) {
        return getAndAdd(k, -1);
    }

    public long getAndIncrement(K k) {
        return getAndAdd(k, 1);
    }

    public long incrementAndGet(K k) {
        return addAndGet(k, 1);
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public long put(K k, long j) {
        AtomicLong atomicLong;
        do {
            atomicLong = (AtomicLong) this.map.get(k);
            if (atomicLong == null) {
                atomicLong = (AtomicLong) this.map.putIfAbsent(k, new AtomicLong(j));
                if (atomicLong == null) {
                    return 0;
                }
            }
            while (true) {
                long j2 = atomicLong.get();
                if (j2 != 0) {
                    if (atomicLong.compareAndSet(j2, j)) {
                        return j2;
                    }
                }
            }
        } while (!this.map.replace(k, atomicLong, new AtomicLong(j)));
        return 0;
    }

    public void putAll(Map<? extends K, ? extends Long> map) {
        for (Entry entry : map.entrySet()) {
            put(entry.getKey(), ((Long) entry.getValue()).longValue());
        }
    }

    long putIfAbsent(K k, long j) {
        AtomicLong atomicLong;
        do {
            AtomicLong atomicLong2 = (AtomicLong) this.map.get(k);
            if (atomicLong2 == null) {
                atomicLong2 = (AtomicLong) this.map.putIfAbsent(k, new AtomicLong(j));
                if (atomicLong2 == null) {
                    return 0;
                }
            }
            atomicLong = atomicLong2;
            long j2 = atomicLong.get();
            if (j2 != 0) {
                return j2;
            }
        } while (!this.map.replace(k, atomicLong, new AtomicLong(j)));
        return 0;
    }

    public long remove(K k) {
        AtomicLong atomicLong = (AtomicLong) this.map.get(k);
        if (atomicLong == null) {
            return 0;
        }
        long j;
        do {
            j = atomicLong.get();
            if (j == 0) {
                break;
            }
        } while (!atomicLong.compareAndSet(j, 0));
        this.map.remove(k, atomicLong);
        return j;
    }

    boolean remove(K k, long j) {
        AtomicLong atomicLong = (AtomicLong) this.map.get(k);
        if (atomicLong != null) {
            long j2 = atomicLong.get();
            if (j2 == j && (j2 == 0 || atomicLong.compareAndSet(j2, 0))) {
                this.map.remove(k, atomicLong);
                return true;
            }
        }
        return false;
    }

    public void removeAllZeros() {
        for (Object next : this.map.keySet()) {
            AtomicLong atomicLong = (AtomicLong) this.map.get(next);
            if (atomicLong != null && atomicLong.get() == 0) {
                this.map.remove(next, atomicLong);
            }
        }
    }

    boolean replace(K k, long j, long j2) {
        if (j != 0) {
            AtomicLong atomicLong = (AtomicLong) this.map.get(k);
            if (atomicLong != null) {
                return atomicLong.compareAndSet(j, j2);
            }
        } else if (putIfAbsent(k, j2) == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return this.map.size();
    }

    public long sum() {
        long j = 0;
        for (AtomicLong atomicLong : this.map.values()) {
            j = atomicLong.get() + j;
        }
        return j;
    }

    public String toString() {
        return this.map.toString();
    }
}
