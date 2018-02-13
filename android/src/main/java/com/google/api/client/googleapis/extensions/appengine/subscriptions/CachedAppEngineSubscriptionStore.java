package com.google.api.client.googleapis.extensions.appengine.subscriptions;

import com.google.api.client.googleapis.subscriptions.StoredSubscription;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import java.io.IOException;

public final class CachedAppEngineSubscriptionStore extends AppEngineSubscriptionStore {
    private static final int EXPIRATION_TIME = 3600;
    private MemcacheService memCache = MemcacheServiceFactory.getMemcacheService(CachedAppEngineSubscriptionStore.class.getCanonicalName());

    public StoredSubscription getSubscription(String str) throws IOException {
        if (this.memCache.contains(str)) {
            return (StoredSubscription) this.memCache.get(str);
        }
        StoredSubscription subscription = super.getSubscription(str);
        this.memCache.put(str, subscription, Expiration.byDeltaSeconds(EXPIRATION_TIME));
        return subscription;
    }

    public void removeSubscription(StoredSubscription storedSubscription) throws IOException {
        super.removeSubscription(storedSubscription);
        this.memCache.delete(storedSubscription.getId());
    }

    public void storeSubscription(StoredSubscription storedSubscription) throws IOException {
        super.storeSubscription(storedSubscription);
        this.memCache.put(storedSubscription.getId(), storedSubscription);
    }
}
