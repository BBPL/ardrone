package com.google.api.client.googleapis.subscriptions;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class TypedNotificationCallback<T> implements NotificationCallback {
    private Class<T> dataClass;

    private Object parseContent(ObjectParser objectParser, UnparsedNotification unparsedNotification) throws IOException {
        Charset charset = null;
        if (unparsedNotification.getContentType() == null || Void.class.equals(this.dataClass)) {
            return null;
        }
        if (unparsedNotification.getContentType() != null) {
            charset = new HttpMediaType(unparsedNotification.getContentType()).getCharsetParameter();
        }
        return objectParser.parseAndClose(unparsedNotification.getContent(), charset, this.dataClass);
    }

    public final Class<T> getDataClass() {
        return this.dataClass;
    }

    protected abstract ObjectParser getParser(UnparsedNotification unparsedNotification) throws IOException;

    protected abstract void handleNotification(StoredSubscription storedSubscription, TypedNotification<T> typedNotification) throws IOException;

    public void handleNotification(StoredSubscription storedSubscription, UnparsedNotification unparsedNotification) throws IOException {
        handleNotification(storedSubscription, new TypedNotification(unparsedNotification, parseContent(getParser(unparsedNotification), unparsedNotification)));
    }

    public TypedNotificationCallback<T> setDataType(Class<T> cls) {
        this.dataClass = (Class) Preconditions.checkNotNull(cls);
        return this;
    }
}
