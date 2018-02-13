package org.mortbay.jetty;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.io.Buffer;
import org.mortbay.io.ByteArrayBuffer;
import org.mortbay.io.View;
import org.mortbay.resource.Resource;

public class ResourceCache extends AbstractLifeCycle implements Serializable {
    protected transient Map _cache;
    protected transient int _cachedFiles;
    protected transient int _cachedSize;
    protected transient Content _leastRecentlyUsed;
    private int _maxCacheSize = 16777216;
    private int _maxCachedFileSize = AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START;
    private int _maxCachedFiles = 2048;
    private MimeTypes _mimeTypes;
    protected transient Content _mostRecentlyUsed;

    public class Content implements HttpContent {
        Buffer _buffer;
        Buffer _contentType;
        String _key;
        long _lastModified;
        Buffer _lastModifiedBytes;
        Content _next = this;
        Content _prev = this;
        Resource _resource;
        private final ResourceCache this$0;

        Content(ResourceCache resourceCache, Resource resource) {
            this.this$0 = resourceCache;
            this._resource = resource;
            this._contentType = ResourceCache.access$000(resourceCache).getMimeByExtension(this._resource.toString());
            this._lastModified = resource.lastModified();
        }

        void cache(String str) {
            this._key = str;
            this._next = this.this$0._mostRecentlyUsed;
            this.this$0._mostRecentlyUsed = this;
            if (this._next != null) {
                this._next._prev = this;
            }
            this._prev = null;
            if (this.this$0._leastRecentlyUsed == null) {
                this.this$0._leastRecentlyUsed = this;
            }
            this.this$0._cache.put(this._key, this);
            ResourceCache resourceCache = this.this$0;
            resourceCache._cachedSize += this._buffer.length();
            resourceCache = this.this$0;
            resourceCache._cachedFiles++;
            if (this._lastModified != -1) {
                this._lastModifiedBytes = new ByteArrayBuffer(HttpFields.formatDate(this._lastModified, false));
            }
        }

        public Buffer getBuffer() {
            return this._buffer == null ? null : new View(this._buffer);
        }

        public long getContentLength() {
            return this._buffer == null ? this._resource != null ? this._resource.length() : -1 : (long) this._buffer.length();
        }

        public Buffer getContentType() {
            return this._contentType;
        }

        public InputStream getInputStream() throws IOException {
            return this._resource.getInputStream();
        }

        public String getKey() {
            return this._key;
        }

        public Buffer getLastModified() {
            return this._lastModifiedBytes;
        }

        public Resource getResource() {
            return this._resource;
        }

        public void invalidate() {
            synchronized (this) {
                this.this$0._cache.remove(this._key);
                this._key = null;
                this.this$0._cachedSize -= this._buffer.length();
                ResourceCache resourceCache = this.this$0;
                resourceCache._cachedFiles--;
                if (this.this$0._mostRecentlyUsed == this) {
                    this.this$0._mostRecentlyUsed = this._next;
                } else {
                    this._prev._next = this._next;
                }
                if (this.this$0._leastRecentlyUsed == this) {
                    this.this$0._leastRecentlyUsed = this._prev;
                } else {
                    this._next._prev = this._prev;
                }
                this._prev = null;
                this._next = null;
                if (this._resource != null) {
                    this._resource.release();
                }
                this._resource = null;
            }
        }

        public boolean isCached() {
            return this._key != null;
        }

        boolean isValid() {
            if (this._lastModified == this._resource.lastModified()) {
                if (this.this$0._mostRecentlyUsed != this) {
                    Content content = this._prev;
                    Content content2 = this._next;
                    this._next = this.this$0._mostRecentlyUsed;
                    this.this$0._mostRecentlyUsed = this;
                    if (this._next != null) {
                        this._next._prev = this;
                    }
                    this._prev = null;
                    if (content != null) {
                        content._next = content2;
                    }
                    if (content2 != null) {
                        content2._prev = content;
                    }
                    if (this.this$0._leastRecentlyUsed == this && content != null) {
                        this.this$0._leastRecentlyUsed = content;
                    }
                }
                return true;
            }
            invalidate();
            return false;
        }

        public void release() {
        }

        public void setBuffer(Buffer buffer) {
            this._buffer = buffer;
        }

        public void setContentType(Buffer buffer) {
            this._contentType = buffer;
        }
    }

    public ResourceCache(MimeTypes mimeTypes) {
        this._mimeTypes = mimeTypes;
    }

    static MimeTypes access$000(ResourceCache resourceCache) {
        return resourceCache._mimeTypes;
    }

    private Content load(String str, Resource resource) throws IOException {
        if (!(resource == null || !resource.exists() || resource.isDirectory())) {
            long length = resource.length();
            if (length > 0 && length < ((long) this._maxCachedFileSize) && length < ((long) this._maxCacheSize)) {
                Content content = new Content(this, resource);
                fill(content);
                synchronized (this._cache) {
                    Content content2 = (Content) this._cache.get(str);
                    if (content2 != null) {
                        content.release();
                        return content2;
                    }
                    int i = this._maxCacheSize;
                    int i2 = (int) length;
                    while (true) {
                        if (this._cachedSize <= i - i2 && (this._maxCachedFiles <= 0 || this._cachedFiles < this._maxCachedFiles)) {
                            break;
                        }
                        this._leastRecentlyUsed.invalidate();
                    }
                    content.cache(str);
                    return content;
                }
            }
        }
        return null;
    }

    public void doStart() throws Exception {
        synchronized (this) {
            this._cache = new HashMap();
            this._cachedSize = 0;
            this._cachedFiles = 0;
        }
    }

    public void doStop() throws InterruptedException {
        flushCache();
    }

    protected void fill(Content content) throws IOException {
        try {
            InputStream inputStream = content.getResource().getInputStream();
            int length = (int) content.getResource().length();
            Buffer byteArrayBuffer = new ByteArrayBuffer(length);
            byteArrayBuffer.readFrom(inputStream, length);
            inputStream.close();
            content.setBuffer(byteArrayBuffer);
        } finally {
            content.getResource().release();
        }
    }

    public void flushCache() {
        if (this._cache != null) {
            synchronized (this) {
                Iterator it = new ArrayList(this._cache.values()).iterator();
                while (it.hasNext()) {
                    ((Content) it.next()).invalidate();
                }
                this._cache.clear();
                this._cachedSize = 0;
                this._cachedFiles = 0;
                this._mostRecentlyUsed = null;
                this._leastRecentlyUsed = null;
            }
        }
    }

    public int getCachedFiles() {
        return this._cachedFiles;
    }

    public int getCachedSize() {
        return this._cachedSize;
    }

    public int getMaxCacheSize() {
        return this._maxCacheSize;
    }

    public int getMaxCachedFileSize() {
        return this._maxCachedFileSize;
    }

    public int getMaxCachedFiles() {
        return this._maxCachedFiles;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.mortbay.jetty.ResourceCache.Content lookup(java.lang.String r4, org.mortbay.resource.Resource r5) throws java.io.IOException {
        /*
        r3 = this;
        r1 = r3._cache;
        monitor-enter(r1);
        r0 = r3._cache;	 Catch:{ all -> 0x001b }
        r0 = r0.get(r4);	 Catch:{ all -> 0x001b }
        r0 = (org.mortbay.jetty.ResourceCache.Content) r0;	 Catch:{ all -> 0x001b }
        if (r0 == 0) goto L_0x0015;
    L_0x000d:
        r2 = r0.isValid();	 Catch:{ all -> 0x001b }
        if (r2 == 0) goto L_0x0015;
    L_0x0013:
        monitor-exit(r1);	 Catch:{ all -> 0x001b }
    L_0x0014:
        return r0;
    L_0x0015:
        monitor-exit(r1);	 Catch:{ all -> 0x001b }
        r0 = r3.load(r4, r5);
        goto L_0x0014;
    L_0x001b:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001b }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.ResourceCache.lookup(java.lang.String, org.mortbay.resource.Resource):org.mortbay.jetty.ResourceCache$Content");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.mortbay.jetty.ResourceCache.Content lookup(java.lang.String r4, org.mortbay.resource.ResourceFactory r5) throws java.io.IOException {
        /*
        r3 = this;
        r1 = r3._cache;
        monitor-enter(r1);
        r0 = r3._cache;	 Catch:{ all -> 0x001f }
        r0 = r0.get(r4);	 Catch:{ all -> 0x001f }
        r0 = (org.mortbay.jetty.ResourceCache.Content) r0;	 Catch:{ all -> 0x001f }
        if (r0 == 0) goto L_0x0015;
    L_0x000d:
        r2 = r0.isValid();	 Catch:{ all -> 0x001f }
        if (r2 == 0) goto L_0x0015;
    L_0x0013:
        monitor-exit(r1);	 Catch:{ all -> 0x001f }
    L_0x0014:
        return r0;
    L_0x0015:
        monitor-exit(r1);	 Catch:{ all -> 0x001f }
        r0 = r5.getResource(r4);
        r0 = r3.load(r4, r0);
        goto L_0x0014;
    L_0x001f:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001f }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.ResourceCache.lookup(java.lang.String, org.mortbay.resource.ResourceFactory):org.mortbay.jetty.ResourceCache$Content");
    }

    public void setMaxCacheSize(int i) {
        this._maxCacheSize = i;
        flushCache();
    }

    public void setMaxCachedFileSize(int i) {
        this._maxCachedFileSize = i;
        flushCache();
    }

    public void setMaxCachedFiles(int i) {
        this._maxCachedFiles = i;
    }
}
