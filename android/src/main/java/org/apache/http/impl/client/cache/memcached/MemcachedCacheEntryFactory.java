package org.apache.http.impl.client.cache.memcached;

import org.apache.http.client.cache.HttpCacheEntry;

public interface MemcachedCacheEntryFactory {
    MemcachedCacheEntry getMemcachedCacheEntry(String str, HttpCacheEntry httpCacheEntry);

    MemcachedCacheEntry getUnsetCacheEntry();
}
