package com.google.api.client.googleapis.subscriptions;

import java.util.Collection;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MemorySubscriptionStore implements SubscriptionStore {
    private final Lock lock = new ReentrantLock();
    private final SortedMap<String, StoredSubscription> storedSubscriptions = new TreeMap();

    public StoredSubscription getSubscription(String str) {
        this.lock.lock();
        try {
            StoredSubscription storedSubscription = (StoredSubscription) this.storedSubscriptions.get(str);
            return storedSubscription;
        } finally {
            this.lock.unlock();
        }
    }

    public Collection<StoredSubscription> listSubscriptions() {
        this.lock.lock();
        try {
            Collection<StoredSubscription> unmodifiableCollection = Collections.unmodifiableCollection(this.storedSubscriptions.values());
            return unmodifiableCollection;
        } finally {
            this.lock.unlock();
        }
    }

    public void removeSubscription(StoredSubscription storedSubscription) {
        this.lock.lock();
        try {
            this.storedSubscriptions.remove(storedSubscription.getId());
        } finally {
            this.lock.unlock();
        }
    }

    public void storeSubscription(StoredSubscription storedSubscription) {
        this.lock.lock();
        try {
            this.storedSubscriptions.put(storedSubscription.getId(), storedSubscription);
        } finally {
            this.lock.unlock();
        }
    }
}
