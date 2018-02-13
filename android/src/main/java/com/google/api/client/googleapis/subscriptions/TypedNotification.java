package com.google.api.client.googleapis.subscriptions;

public final class TypedNotification<T> extends Notification {
    private final T content;

    public TypedNotification(Notification notification, T t) {
        super(notification);
        this.content = t;
    }

    public TypedNotification(String str, String str2, String str3, String str4, long j, String str5, String str6, T t) {
        super(str, str2, str3, str4, j, str5, str6);
        this.content = t;
    }

    public final T getContent() {
        return this.content;
    }
}
