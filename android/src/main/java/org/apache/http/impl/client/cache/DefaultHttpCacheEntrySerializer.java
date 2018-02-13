package org.apache.http.impl.client.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheEntrySerializationException;
import org.apache.http.client.cache.HttpCacheEntrySerializer;

@Immutable
public class DefaultHttpCacheEntrySerializer implements HttpCacheEntrySerializer {
    public HttpCacheEntry readFrom(InputStream inputStream) throws IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            HttpCacheEntry httpCacheEntry = (HttpCacheEntry) objectInputStream.readObject();
            objectInputStream.close();
            return httpCacheEntry;
        } catch (Throwable e) {
            throw new HttpCacheEntrySerializationException("Class not found: " + e.getMessage(), e);
        } catch (Throwable th) {
            objectInputStream.close();
        }
    }

    public void writeTo(HttpCacheEntry httpCacheEntry, OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        try {
            objectOutputStream.writeObject(httpCacheEntry);
        } finally {
            objectOutputStream.close();
        }
    }
}
