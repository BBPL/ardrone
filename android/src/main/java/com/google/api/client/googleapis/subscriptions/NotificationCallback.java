package com.google.api.client.googleapis.subscriptions;

import java.io.IOException;
import java.io.Serializable;

public interface NotificationCallback extends Serializable {
    void handleNotification(StoredSubscription storedSubscription, UnparsedNotification unparsedNotification) throws IOException;
}
