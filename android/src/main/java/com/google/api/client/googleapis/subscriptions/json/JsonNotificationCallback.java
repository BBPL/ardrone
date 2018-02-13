package com.google.api.client.googleapis.subscriptions.json;

import com.google.api.client.googleapis.subscriptions.TypedNotificationCallback;
import com.google.api.client.googleapis.subscriptions.UnparsedNotification;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.ObjectParser;
import java.io.IOException;

public abstract class JsonNotificationCallback<T> extends TypedNotificationCallback<T> {
    private transient JsonFactory jsonFactory;

    protected abstract JsonFactory createJsonFactory() throws IOException;

    public final JsonFactory getJsonFactory() throws IOException {
        if (this.jsonFactory == null) {
            this.jsonFactory = createJsonFactory();
        }
        return this.jsonFactory;
    }

    protected final ObjectParser getParser(UnparsedNotification unparsedNotification) throws IOException {
        return new JsonObjectParser(getJsonFactory());
    }

    public JsonNotificationCallback<T> setDataType(Class<T> cls) {
        return (JsonNotificationCallback) super.setDataType(cls);
    }
}
