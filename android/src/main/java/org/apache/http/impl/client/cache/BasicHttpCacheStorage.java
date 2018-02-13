package org.apache.http.impl.client.cache;

import java.io.IOException;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.cache.HttpCacheUpdateCallback;

@ThreadSafe
public class BasicHttpCacheStorage implements HttpCacheStorage {
    private final CacheMap entries;

    public BasicHttpCacheStorage(CacheConfig cacheConfig) {
        this.entries = new CacheMap(cacheConfig.getMaxCacheEntries());
    }

    public HttpCacheEntry getEntry(String str) throws IOException {
        HttpCacheEntry httpCacheEntry;
        synchronized (this) {
            httpCacheEntry = (HttpCacheEntry) this.entries.get(str);
        }
        return httpCacheEntry;
    }

    public void putEntry(String str, HttpCacheEntry httpCacheEntry) throws IOException {
        synchronized (this) {
            this.entries.put(str, httpCacheEntry);
        }
    }

    public void removeEntry(String str) throws IOException {
        synchronized (this) {
            this.entries.remove(str);
        }
    }

    public void updateEntry(String str, HttpCacheUpdateCallback httpCacheUpdateCallback) throws IOException {
        synchronized (this) {
            this.entries.put(str, httpCacheUpdateCallback.update((HttpCacheEntry) this.entries.get(str)));
        }
    }
}
