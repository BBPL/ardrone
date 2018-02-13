package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class LiveBroadcastStatus extends GenericJson {
    @Key
    private String lifeCycleStatus;
    @Key
    private String privacyStatus;

    public LiveBroadcastStatus clone() {
        return (LiveBroadcastStatus) super.clone();
    }

    public String getLifeCycleStatus() {
        return this.lifeCycleStatus;
    }

    public String getPrivacyStatus() {
        return this.privacyStatus;
    }

    public LiveBroadcastStatus set(String str, Object obj) {
        return (LiveBroadcastStatus) super.set(str, obj);
    }

    public LiveBroadcastStatus setLifeCycleStatus(String str) {
        this.lifeCycleStatus = str;
        return this;
    }

    public LiveBroadcastStatus setPrivacyStatus(String str) {
        this.privacyStatus = str;
        return this;
    }
}
