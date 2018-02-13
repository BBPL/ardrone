package com.google.api.client.googleapis.subscriptions;

import java.io.IOException;
import java.util.Collection;

public interface SubscriptionStore {
    StoredSubscription getSubscription(String str) throws IOException;

    Collection<StoredSubscription> listSubscriptions() throws IOException;

    void removeSubscription(StoredSubscription storedSubscription) throws IOException;

    void storeSubscription(StoredSubscription storedSubscription) throws IOException;
}
