package org.apache.http.impl.client.cache.memcached;

import org.apache.http.client.cache.HttpCacheEntry;

public class MemcachedCacheEntryFactoryImpl implements MemcachedCacheEntryFactory {
    public MemcachedCacheEntry getMemcachedCacheEntry(String str, HttpCacheEntry httpCacheEntry) {
        return new MemcachedCacheEntryImpl(str, httpCacheEntry);
    }

    public MemcachedCacheEntry getUnsetCacheEntry() {
        return new MemcachedCacheEntryImpl(null, null);
    }
}
