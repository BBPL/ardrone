package com.google.api.client.googleapis.testing.subscriptions;

import com.google.api.client.googleapis.subscriptions.NotificationCallback;
import com.google.api.client.googleapis.subscriptions.StoredSubscription;
import com.google.api.client.googleapis.subscriptions.UnparsedNotification;

public class MockNotificationCallback implements NotificationCallback {
    private static final long serialVersionUID = 0;
    private boolean wasCalled = false;

    public void handleNotification(StoredSubscription storedSubscription, UnparsedNotification unparsedNotification) {
        this.wasCalled = true;
    }

    public boolean wasCalled() {
        return this.wasCalled;
    }
}
