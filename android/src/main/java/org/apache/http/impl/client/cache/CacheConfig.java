package org.apache.http.impl.client.cache;

import com.parrot.ardronetool.ConfigArdroneMask;

public class CacheConfig {
    public static final int DEFAULT_ASYNCHRONOUS_WORKERS_CORE = 1;
    public static final int DEFAULT_ASYNCHRONOUS_WORKERS_MAX = 1;
    public static final int DEFAULT_ASYNCHRONOUS_WORKER_IDLE_LIFETIME_SECS = 60;
    public static final boolean DEFAULT_HEURISTIC_CACHING_ENABLED = false;
    public static final float DEFAULT_HEURISTIC_COEFFICIENT = 0.1f;
    public static final long DEFAULT_HEURISTIC_LIFETIME = 0;
    public static final int DEFAULT_MAX_CACHE_ENTRIES = 1000;
    public static final int DEFAULT_MAX_OBJECT_SIZE_BYTES = 8192;
    public static final int DEFAULT_MAX_UPDATE_RETRIES = 1;
    public static final int DEFAULT_REVALIDATION_QUEUE_SIZE = 100;
    private int asynchronousWorkerIdleLifetimeSecs = 60;
    private int asynchronousWorkersCore = 1;
    private int asynchronousWorkersMax = 1;
    private boolean heuristicCachingEnabled = false;
    private float heuristicCoefficient = DEFAULT_HEURISTIC_COEFFICIENT;
    private long heuristicDefaultLifetime = 0;
    private boolean isSharedCache = true;
    private int maxCacheEntries = 1000;
    private long maxObjectSize = ConfigArdroneMask.ARDRONE_COM_LOST_MASK;
    private int maxUpdateRetries = 1;
    private int revalidationQueueSize = 100;

    public int getAsynchronousWorkerIdleLifetimeSecs() {
        return this.asynchronousWorkerIdleLifetimeSecs;
    }

    public int getAsynchronousWorkersCore() {
        return this.asynchronousWorkersCore;
    }

    public int getAsynchronousWorkersMax() {
        return this.asynchronousWorkersMax;
    }

    public float getHeuristicCoefficient() {
        return this.heuristicCoefficient;
    }

    public long getHeuristicDefaultLifetime() {
        return this.heuristicDefaultLifetime;
    }

    public int getMaxCacheEntries() {
        return this.maxCacheEntries;
    }

    public long getMaxObjectSize() {
        return this.maxObjectSize;
    }

    @Deprecated
    public int getMaxObjectSizeBytes() {
        return this.maxObjectSize > 2147483647L ? Integer.MAX_VALUE : (int) this.maxObjectSize;
    }

    public int getMaxUpdateRetries() {
        return this.maxUpdateRetries;
    }

    public int getRevalidationQueueSize() {
        return this.revalidationQueueSize;
    }

    public boolean isHeuristicCachingEnabled() {
        return this.heuristicCachingEnabled;
    }

    public boolean isSharedCache() {
        return this.isSharedCache;
    }

    public void setAsynchronousWorkerIdleLifetimeSecs(int i) {
        this.asynchronousWorkerIdleLifetimeSecs = i;
    }

    public void setAsynchronousWorkersCore(int i) {
        this.asynchronousWorkersCore = i;
    }

    public void setAsynchronousWorkersMax(int i) {
        this.asynchronousWorkersMax = i;
    }

    public void setHeuristicCachingEnabled(boolean z) {
        this.heuristicCachingEnabled = z;
    }

    public void setHeuristicCoefficient(float f) {
        this.heuristicCoefficient = f;
    }

    public void setHeuristicDefaultLifetime(long j) {
        this.heuristicDefaultLifetime = j;
    }

    public void setMaxCacheEntries(int i) {
        this.maxCacheEntries = i;
    }

    public void setMaxObjectSize(long j) {
        this.maxObjectSize = j;
    }

    @Deprecated
    public void setMaxObjectSizeBytes(int i) {
        if (i > Integer.MAX_VALUE) {
            this.maxObjectSize = 2147483647L;
        } else {
            this.maxObjectSize = (long) i;
        }
    }

    public void setMaxUpdateRetries(int i) {
        this.maxUpdateRetries = i;
    }

    public void setRevalidationQueueSize(int i) {
        this.revalidationQueueSize = i;
    }

    public void setSharedCache(boolean z) {
        this.isSharedCache = z;
    }
}
