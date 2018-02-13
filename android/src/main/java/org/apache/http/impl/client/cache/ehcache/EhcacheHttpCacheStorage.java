package org.apache.http.impl.client.cache.ehcache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheEntrySerializer;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.cache.HttpCacheUpdateCallback;
import org.apache.http.client.cache.HttpCacheUpdateException;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.DefaultHttpCacheEntrySerializer;

public class EhcacheHttpCacheStorage implements HttpCacheStorage {
    private final Ehcache cache;
    private final int maxUpdateRetries;
    private final HttpCacheEntrySerializer serializer;

    public EhcacheHttpCacheStorage(Ehcache ehcache) {
        this(ehcache, new CacheConfig(), new DefaultHttpCacheEntrySerializer());
    }

    public EhcacheHttpCacheStorage(Ehcache ehcache, CacheConfig cacheConfig) {
        this(ehcache, cacheConfig, new DefaultHttpCacheEntrySerializer());
    }

    public EhcacheHttpCacheStorage(Ehcache ehcache, CacheConfig cacheConfig, HttpCacheEntrySerializer httpCacheEntrySerializer) {
        this.cache = ehcache;
        this.maxUpdateRetries = cacheConfig.getMaxUpdateRetries();
        this.serializer = httpCacheEntrySerializer;
    }

    public HttpCacheEntry getEntry(String str) throws IOException {
        HttpCacheEntry httpCacheEntry;
        synchronized (this) {
            Element element = this.cache.get(str);
            if (element == null) {
                httpCacheEntry = null;
            } else {
                httpCacheEntry = this.serializer.readFrom(new ByteArrayInputStream((byte[]) element.getValue()));
            }
        }
        return httpCacheEntry;
    }

    public void putEntry(String str, HttpCacheEntry httpCacheEntry) throws IOException {
        synchronized (this) {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            this.serializer.writeTo(httpCacheEntry, byteArrayOutputStream);
            this.cache.put(new Element(str, byteArrayOutputStream.toByteArray()));
        }
    }

    public void removeEntry(String str) {
        synchronized (this) {
            this.cache.remove(str);
        }
    }

    public void updateEntry(String str, HttpCacheUpdateCallback httpCacheUpdateCallback) throws IOException, HttpCacheUpdateException {
        synchronized (this) {
            HttpCacheEntry update;
            int i = 0;
            while (true) {
                Element element = this.cache.get(str);
                HttpCacheEntry httpCacheEntry = null;
                if (element != null) {
                    httpCacheEntry = this.serializer.readFrom(new ByteArrayInputStream((byte[]) element.getValue()));
                }
                update = httpCacheUpdateCallback.update(httpCacheEntry);
                if (httpCacheEntry != null) {
                    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    this.serializer.writeTo(update, byteArrayOutputStream);
                    if (this.cache.replace(element, new Element(str, byteArrayOutputStream.toByteArray()))) {
                        break;
                    }
                    int i2 = i + 1;
                    if (i2 > this.maxUpdateRetries) {
                        throw new HttpCacheUpdateException("Failed to update");
                    }
                    i = i2;
                } else {
                    break;
                }
            }
            putEntry(str, update);
        }
    }
}
