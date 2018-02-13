package com.google.api.client.googleapis.json;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class GoogleJsonErrorContainer extends GenericJson {
    @Key
    private GoogleJsonError error;

    public GoogleJsonErrorContainer clone() {
        return (GoogleJsonErrorContainer) super.clone();
    }

    public final GoogleJsonError getError() {
        return this.error;
    }

    public GoogleJsonErrorContainer set(String str, Object obj) {
        return (GoogleJsonErrorContainer) super.set(str, obj);
    }

    public final void setError(GoogleJsonError googleJsonError) {
        this.error = googleJsonError;
    }
}
