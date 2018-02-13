package org.apache.http.impl.client.cache.memcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.MemcachedClientIF;
import net.spy.memcached.OperationTimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheEntrySerializer;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.cache.HttpCacheUpdateCallback;
import org.apache.http.client.cache.HttpCacheUpdateException;
import org.apache.http.impl.client.cache.CacheConfig;

public class MemcachedHttpCacheStorage implements HttpCacheStorage {
    private static final Log log = LogFactory.getLog(MemcachedHttpCacheStorage.class);
    private final MemcachedClientIF client;
    private final KeyHashingScheme keyHashingScheme;
    private final int maxUpdateRetries;
    private final MemcachedCacheEntryFactory memcachedCacheEntryFactory;

    public MemcachedHttpCacheStorage(InetSocketAddress inetSocketAddress) throws IOException {
        this(new MemcachedClient(new InetSocketAddress[]{inetSocketAddress}));
    }

    public MemcachedHttpCacheStorage(MemcachedClientIF memcachedClientIF) {
        this(memcachedClientIF, new CacheConfig(), new MemcachedCacheEntryFactoryImpl(), new SHA256KeyHashingScheme());
    }

    @Deprecated
    public MemcachedHttpCacheStorage(MemcachedClientIF memcachedClientIF, CacheConfig cacheConfig, HttpCacheEntrySerializer httpCacheEntrySerializer) {
        this(memcachedClientIF, cacheConfig, new MemcachedCacheEntryFactoryImpl(), new SHA256KeyHashingScheme());
    }

    public MemcachedHttpCacheStorage(MemcachedClientIF memcachedClientIF, CacheConfig cacheConfig, MemcachedCacheEntryFactory memcachedCacheEntryFactory, KeyHashingScheme keyHashingScheme) {
        this.client = memcachedClientIF;
        this.maxUpdateRetries = cacheConfig.getMaxUpdateRetries();
        this.memcachedCacheEntryFactory = memcachedCacheEntryFactory;
        this.keyHashingScheme = keyHashingScheme;
    }

    private byte[] convertToByteArray(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        log.warn("got a non-bytearray back from memcached: " + obj);
        return null;
    }

    private String getCacheKey(String str) {
        try {
            return this.keyHashingScheme.hash(str);
        } catch (MemcachedKeyHashingException e) {
            return null;
        }
    }

    private MemcachedCacheEntry reconstituteEntry(Object obj) throws IOException {
        byte[] convertToByteArray = convertToByteArray(obj);
        if (convertToByteArray == null) {
            return null;
        }
        MemcachedCacheEntry unsetCacheEntry = this.memcachedCacheEntryFactory.getUnsetCacheEntry();
        try {
            unsetCacheEntry.set(convertToByteArray);
            return unsetCacheEntry;
        } catch (MemcachedSerializationException e) {
            return null;
        }
    }

    private byte[] serializeEntry(String str, HttpCacheEntry httpCacheEntry) throws IOException {
        try {
            return this.memcachedCacheEntryFactory.getMemcachedCacheEntry(str, httpCacheEntry).toByteArray();
        } catch (Throwable e) {
            IOException iOException = new IOException();
            iOException.initCause(e);
            throw iOException;
        }
    }

    public HttpCacheEntry getEntry(String str) throws IOException {
        String cacheKey = getCacheKey(str);
        if (cacheKey != null) {
            try {
                MemcachedCacheEntry reconstituteEntry = reconstituteEntry(this.client.get(cacheKey));
                if (reconstituteEntry != null && str.equals(reconstituteEntry.getStorageKey())) {
                    return reconstituteEntry.getHttpCacheEntry();
                }
            } catch (OperationTimeoutException e) {
                throw new MemcachedOperationTimeoutException(e);
            }
        }
        return null;
    }

    public void putEntry(String str, HttpCacheEntry httpCacheEntry) throws IOException {
        Object serializeEntry = serializeEntry(str, httpCacheEntry);
        String cacheKey = getCacheKey(str);
        if (cacheKey != null) {
            try {
                this.client.set(cacheKey, 0, serializeEntry);
            } catch (OperationTimeoutException e) {
                throw new MemcachedOperationTimeoutException(e);
            }
        }
    }

    public void removeEntry(String str) throws IOException {
        String cacheKey = getCacheKey(str);
        if (cacheKey != null) {
            try {
                this.client.delete(cacheKey);
            } catch (OperationTimeoutException e) {
                throw new MemcachedOperationTimeoutException(e);
            }
        }
    }

    public void updateEntry(String str, HttpCacheUpdateCallback httpCacheUpdateCallback) throws HttpCacheUpdateException, IOException {
        int i = 0;
        String cacheKey = getCacheKey(str);
        if (cacheKey == null) {
            throw new HttpCacheUpdateException("couldn't generate cache key");
        }
        do {
            int i2 = i;
            try {
                CASValue sVar = this.client.gets(cacheKey);
                MemcachedCacheEntry reconstituteEntry = sVar == null ? null : reconstituteEntry(sVar.getValue());
                if (!(reconstituteEntry == null || str.equals(reconstituteEntry.getStorageKey()))) {
                    reconstituteEntry = null;
                }
                HttpCacheEntry httpCacheEntry = reconstituteEntry == null ? null : reconstituteEntry.getHttpCacheEntry();
                HttpCacheEntry update = httpCacheUpdateCallback.update(httpCacheEntry);
                if (httpCacheEntry == null) {
                    putEntry(str, update);
                    return;
                }
                if (this.client.cas(cacheKey, sVar.getCas(), serializeEntry(str, update)) != CASResponse.OK) {
                    i = i2 + 1;
                } else {
                    return;
                }
            } catch (OperationTimeoutException e) {
                throw new MemcachedOperationTimeoutException(e);
            }
        } while (i <= this.maxUpdateRetries);
        throw new HttpCacheUpdateException("Failed to update");
    }
}
