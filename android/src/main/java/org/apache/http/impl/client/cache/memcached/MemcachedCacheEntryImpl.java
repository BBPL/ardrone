package org.apache.http.impl.client.cache.memcached;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import org.apache.http.client.cache.HttpCacheEntry;

public class MemcachedCacheEntryImpl implements MemcachedCacheEntry {
    private HttpCacheEntry httpCacheEntry;
    private String key;

    public MemcachedCacheEntryImpl(String str, HttpCacheEntry httpCacheEntry) {
        this.key = str;
        this.httpCacheEntry = httpCacheEntry;
    }

    public HttpCacheEntry getHttpCacheEntry() {
        HttpCacheEntry httpCacheEntry;
        synchronized (this) {
            httpCacheEntry = this.httpCacheEntry;
        }
        return httpCacheEntry;
    }

    public String getStorageKey() {
        String str;
        synchronized (this) {
            str = this.key;
        }
        return str;
    }

    public void set(byte[] bArr) {
        synchronized (this) {
            InputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                String str = (String) objectInputStream.readObject();
                HttpCacheEntry httpCacheEntry = (HttpCacheEntry) objectInputStream.readObject();
                objectInputStream.close();
                byteArrayInputStream.close();
                this.key = str;
                this.httpCacheEntry = httpCacheEntry;
            } catch (Throwable e) {
                throw new MemcachedSerializationException(e);
            } catch (Throwable e2) {
                throw new MemcachedSerializationException(e2);
            }
        }
    }

    public byte[] toByteArray() {
        byte[] toByteArray;
        synchronized (this) {
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(this.key);
                objectOutputStream.writeObject(this.httpCacheEntry);
                objectOutputStream.close();
                toByteArray = byteArrayOutputStream.toByteArray();
            } catch (Throwable e) {
                throw new MemcachedSerializationException(e);
            }
        }
        return toByteArray;
    }
}
