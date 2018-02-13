package com.google.api.client.googleapis.subscriptions;

import com.google.api.client.util.Preconditions;

public abstract class Notification {
    private final String changeType;
    private final String clientToken;
    private final String eventType;
    private final long messageNumber;
    private final String subscriptionId;
    private final String topicId;
    private final String topicURI;

    protected Notification(Notification notification) {
        this(notification.getSubscriptionId(), notification.getTopicId(), notification.getTopicURI(), notification.getClientToken(), notification.getMessageNumber(), notification.getEventType(), notification.getChangeType());
    }

    protected Notification(String str, String str2, String str3, String str4, long j, String str5, String str6) {
        this.subscriptionId = (String) Preconditions.checkNotNull(str);
        this.topicId = (String) Preconditions.checkNotNull(str2);
        this.topicURI = (String) Preconditions.checkNotNull(str3);
        this.eventType = (String) Preconditions.checkNotNull(str5);
        this.clientToken = str4;
        Preconditions.checkArgument(j >= 1);
        this.messageNumber = j;
        this.changeType = str6;
    }

    public final String getChangeType() {
        return this.changeType;
    }

    public final String getClientToken() {
        return this.clientToken;
    }

    public final String getEventType() {
        return this.eventType;
    }

    public final long getMessageNumber() {
        return this.messageNumber;
    }

    public final String getSubscriptionId() {
        return this.subscriptionId;
    }

    public final String getTopicId() {
        return this.topicId;
    }

    public final String getTopicURI() {
        return this.topicURI;
    }
}
