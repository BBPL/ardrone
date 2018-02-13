package org.apache.http.impl.client.cache;

import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.util.HashSet;
import java.util.Set;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.cache.HttpCacheUpdateCallback;

@ThreadSafe
public class ManagedHttpCacheStorage implements HttpCacheStorage {
    private final CacheMap entries;
    private final ReferenceQueue<HttpCacheEntry> morque = new ReferenceQueue();
    private final Set<ResourceReference> resources = new HashSet();
    private volatile boolean shutdown;

    public ManagedHttpCacheStorage(CacheConfig cacheConfig) {
        this.entries = new CacheMap(cacheConfig.getMaxCacheEntries());
    }

    private void ensureValidState() throws IllegalStateException {
        if (this.shutdown) {
            throw new IllegalStateException("Cache has been shut down");
        }
    }

    private void keepResourceReference(HttpCacheEntry httpCacheEntry) {
        if (httpCacheEntry.getResource() != null) {
            this.resources.add(new ResourceReference(httpCacheEntry, this.morque));
        }
    }

    public void cleanResources() {
        if (!this.shutdown) {
            while (true) {
                ResourceReference resourceReference = (ResourceReference) this.morque.poll();
                if (resourceReference != null) {
                    synchronized (this) {
                        this.resources.remove(resourceReference);
                    }
                    resourceReference.getResource().dispose();
                } else {
                    return;
                }
            }
        }
    }

    public HttpCacheEntry getEntry(String str) throws IOException {
        if (str == null) {
            throw new IllegalArgumentException("URL may not be null");
        }
        HttpCacheEntry httpCacheEntry;
        ensureValidState();
        synchronized (this) {
            httpCacheEntry = (HttpCacheEntry) this.entries.get(str);
        }
        return httpCacheEntry;
    }

    public void putEntry(String str, HttpCacheEntry httpCacheEntry) throws IOException {
        if (str == null) {
            throw new IllegalArgumentException("URL may not be null");
        } else if (httpCacheEntry == null) {
            throw new IllegalArgumentException("Cache entry may not be null");
        } else {
            ensureValidState();
            synchronized (this) {
                this.entries.put(str, httpCacheEntry);
                keepResourceReference(httpCacheEntry);
            }
        }
    }

    public void removeEntry(String str) throws IOException {
        if (str == null) {
            throw new IllegalArgumentException("URL may not be null");
        }
        ensureValidState();
        synchronized (this) {
            this.entries.remove(str);
        }
    }

    public void shutdown() {
        if (!this.shutdown) {
            this.shutdown = true;
            synchronized (this) {
                this.entries.clear();
                for (ResourceReference resource : this.resources) {
                    resource.getResource().dispose();
                }
                this.resources.clear();
                do {
                } while (this.morque.poll() != null);
            }
        }
    }

    public void updateEntry(String str, HttpCacheUpdateCallback httpCacheUpdateCallback) throws IOException {
        if (str == null) {
            throw new IllegalArgumentException("URL may not be null");
        } else if (httpCacheUpdateCallback == null) {
            throw new IllegalArgumentException("Callback may not be null");
        } else {
            ensureValidState();
            synchronized (this) {
                HttpCacheEntry httpCacheEntry = (HttpCacheEntry) this.entries.get(str);
                HttpCacheEntry update = httpCacheUpdateCallback.update(httpCacheEntry);
                this.entries.put(str, update);
                if (httpCacheEntry != update) {
                    keepResourceReference(update);
                }
            }
        }
    }
}
