package org.apache.http.impl.client.cache.memcached;

import org.apache.http.client.cache.HttpCacheEntry;

public interface MemcachedCacheEntry {
    HttpCacheEntry getHttpCacheEntry();

    String getStorageKey();

    void set(byte[] bArr);

    byte[] toByteArray();
}
