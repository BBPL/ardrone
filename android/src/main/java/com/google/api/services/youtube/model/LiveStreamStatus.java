package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class LiveStreamStatus extends GenericJson {
    @Key
    private String streamStatus;

    public LiveStreamStatus clone() {
        return (LiveStreamStatus) super.clone();
    }

    public String getStreamStatus() {
        return this.streamStatus;
    }

    public LiveStreamStatus set(String str, Object obj) {
        return (LiveStreamStatus) super.set(str, obj);
    }

    public LiveStreamStatus setStreamStatus(String str) {
        this.streamStatus = str;
        return this;
    }
}
