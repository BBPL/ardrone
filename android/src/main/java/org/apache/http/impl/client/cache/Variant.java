package org.apache.http.impl.client.cache;

import org.apache.http.client.cache.HttpCacheEntry;

class Variant {
    private final String cacheKey;
    private final HttpCacheEntry entry;
    private final String variantKey;

    public Variant(String str, String str2, HttpCacheEntry httpCacheEntry) {
        this.variantKey = str;
        this.cacheKey = str2;
        this.entry = httpCacheEntry;
    }

    public String getCacheKey() {
        return this.cacheKey;
    }

    public HttpCacheEntry getEntry() {
        return this.entry;
    }

    public String getVariantKey() {
        return this.variantKey;
    }
}
