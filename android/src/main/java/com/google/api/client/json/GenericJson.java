package com.google.api.client.json;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Throwables;
import java.io.IOException;

public class GenericJson extends GenericData implements Cloneable {
    private JsonFactory jsonFactory;

    public GenericJson clone() {
        return (GenericJson) super.clone();
    }

    public final JsonFactory getFactory() {
        return this.jsonFactory;
    }

    public GenericJson set(String str, Object obj) {
        return (GenericJson) super.set(str, obj);
    }

    public final void setFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    public String toPrettyString() throws IOException {
        return this.jsonFactory != null ? this.jsonFactory.toPrettyString(this) : super.toString();
    }

    public String toString() {
        if (this.jsonFactory == null) {
            return super.toString();
        }
        try {
            return this.jsonFactory.toString(this);
        } catch (Throwable e) {
            throw Throwables.propagate(e);
        }
    }
}
