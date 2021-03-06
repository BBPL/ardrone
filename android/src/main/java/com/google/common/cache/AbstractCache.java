package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

@GwtCompatible
@Beta
public abstract class AbstractCache<K, V> implements Cache<K, V> {

    @Beta
    public interface StatsCounter {
        void recordEviction();

        void recordHits(int i);

        void recordLoadException(long j);

        void recordLoadSuccess(long j);

        void recordMisses(int i);

        CacheStats snapshot();
    }

    @Beta
    public static final class SimpleStatsCounter implements StatsCounter {
        private final LongAdder evictionCount = new LongAdder();
        private final LongAdder hitCount = new LongAdder();
        private final LongAdder loadExceptionCount = new LongAdder();
        private final LongAdder loadSuccessCount = new LongAdder();
        private final LongAdder missCount = new LongAdder();
        private final LongAdder totalLoadTime = new LongAdder();

        public void incrementBy(StatsCounter statsCounter) {
            CacheStats snapshot = statsCounter.snapshot();
            this.hitCount.add(snapshot.hitCount());
            this.missCount.add(snapshot.missCount());
            this.loadSuccessCount.add(snapshot.loadSuccessCount());
            this.loadExceptionCount.add(snapshot.loadExceptionCount());
            this.totalLoadTime.add(snapshot.totalLoadTime());
            this.evictionCount.add(snapshot.evictionCount());
        }

        public void recordEviction() {
            this.evictionCount.increment();
        }

        public void recordHits(int i) {
            this.hitCount.add((long) i);
        }

        public void recordLoadException(long j) {
            this.loadExceptionCount.increment();
            this.totalLoadTime.add(j);
        }

        public void recordLoadSuccess(long j) {
            this.loadSuccessCount.increment();
            this.totalLoadTime.add(j);
        }

        public void recordMisses(int i) {
            this.missCount.add((long) i);
        }

        public CacheStats snapshot() {
            return new CacheStats(this.hitCount.sum(), this.missCount.sum(), this.loadSuccessCount.sum(), this.loadExceptionCount.sum(), this.totalLoadTime.sum(), this.evictionCount.sum());
        }
    }

    protected AbstractCache() {
    }

    public ConcurrentMap<K, V> asMap() {
        throw new UnsupportedOperationException();
    }

    public void cleanUp() {
    }

    public V get(K k, Callable<? extends V> callable) throws ExecutionException {
        throw new UnsupportedOperationException();
    }

    public ImmutableMap<K, V> getAllPresent(Iterable<?> iterable) {
        Map newLinkedHashMap = Maps.newLinkedHashMap();
        for (Object next : iterable) {
            if (!newLinkedHashMap.containsKey(next)) {
                newLinkedHashMap.put(next, getIfPresent(next));
            }
        }
        return ImmutableMap.copyOf(newLinkedHashMap);
    }

    public void invalidate(Object obj) {
        throw new UnsupportedOperationException();
    }

    public void invalidateAll() {
        throw new UnsupportedOperationException();
    }

    public void invalidateAll(Iterable<?> iterable) {
        for (Object invalidate : iterable) {
            invalidate(invalidate);
        }
    }

    public void put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public long size() {
        throw new UnsupportedOperationException();
    }

    public CacheStats stats() {
        throw new UnsupportedOperationException();
    }
}
