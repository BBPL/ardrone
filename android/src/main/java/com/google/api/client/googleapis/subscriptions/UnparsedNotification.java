package com.google.api.client.googleapis.subscriptions;

import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Strings;
import java.io.IOException;
import java.io.InputStream;

public final class UnparsedNotification extends Notification {
    private final InputStream content;
    private final String contentType;

    public UnparsedNotification(String str, String str2, String str3, String str4, long j, String str5, String str6, String str7, InputStream inputStream) {
        super(str, str2, str3, str4, j, str5, str6);
        this.contentType = str7;
        this.content = (InputStream) Preconditions.checkNotNull(inputStream);
    }

    public boolean deliverNotification(SubscriptionStore subscriptionStore) throws IOException {
        StoredSubscription subscription = subscriptionStore.getSubscription((String) Preconditions.checkNotNull(getSubscriptionId()));
        if (subscription == null) {
            return false;
        }
        String clientToken = subscription.getClientToken();
        boolean z = Strings.isNullOrEmpty(clientToken) || clientToken.equals(getClientToken());
        Preconditions.checkArgument(z, "Token mismatch for subscription with id=%s -- got=%s expected=%s", getSubscriptionId(), getClientToken(), clientToken);
        subscription.getNotificationCallback().handleNotification(subscription, this);
        return true;
    }

    public final InputStream getContent() {
        return this.content;
    }

    public final String getContentType() {
        return this.contentType;
    }
}
