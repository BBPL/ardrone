package com.google.api.client.googleapis.subscriptions;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Objects;
import com.google.api.client.util.Preconditions;
import java.io.Serializable;
import java.util.UUID;

public final class StoredSubscription implements Serializable {
    private static final long serialVersionUID = 1;
    private String clientToken;
    private String expiration;
    private final String id;
    private final NotificationCallback notificationCallback;
    private String topicId;

    public StoredSubscription(NotificationCallback notificationCallback) {
        this(notificationCallback, randomId());
    }

    public StoredSubscription(NotificationCallback notificationCallback, GenericJson genericJson) {
        this(notificationCallback, (String) genericJson.get("id"));
        setClientToken((String) genericJson.get("clientToken"));
        setExpiration((String) genericJson.get("expiration"));
        setTopicId((String) genericJson.get("topicId"));
    }

    public StoredSubscription(NotificationCallback notificationCallback, String str) {
        this.notificationCallback = (NotificationCallback) Preconditions.checkNotNull(notificationCallback);
        this.id = (String) Preconditions.checkNotNull(str);
    }

    public static String randomId() {
        return UUID.randomUUID().toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StoredSubscription)) {
            return false;
        }
        return getId().equals(((StoredSubscription) obj).getId());
    }

    public String getClientToken() {
        String str;
        synchronized (this) {
            str = this.clientToken;
        }
        return str;
    }

    public String getExpiration() {
        String str;
        synchronized (this) {
            str = this.expiration;
        }
        return str;
    }

    public String getId() {
        String str;
        synchronized (this) {
            str = this.id;
        }
        return str;
    }

    public NotificationCallback getNotificationCallback() {
        NotificationCallback notificationCallback;
        synchronized (this) {
            notificationCallback = this.notificationCallback;
        }
        return notificationCallback;
    }

    public String getTopicId() {
        String str;
        synchronized (this) {
            str = this.topicId;
        }
        return str;
    }

    public int hashCode() {
        return getId().hashCode();
    }

    public StoredSubscription setClientToken(String str) {
        synchronized (this) {
            this.clientToken = str;
        }
        return this;
    }

    public StoredSubscription setExpiration(String str) {
        synchronized (this) {
            this.expiration = str;
        }
        return this;
    }

    public StoredSubscription setTopicId(String str) {
        synchronized (this) {
            this.topicId = str;
        }
        return this;
    }

    public String toString() {
        return Objects.toStringHelper(StoredSubscription.class).add("notificationCallback", getNotificationCallback()).add("clientToken", getClientToken()).add("expiration", getExpiration()).add("id", getId()).add("topicId", getTopicId()).toString();
    }
}
