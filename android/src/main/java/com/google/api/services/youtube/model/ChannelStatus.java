package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class ChannelStatus extends GenericJson {
    @Key
    private String privacyStatus;

    public ChannelStatus clone() {
        return (ChannelStatus) super.clone();
    }

    public String getPrivacyStatus() {
        return this.privacyStatus;
    }

    public ChannelStatus set(String str, Object obj) {
        return (ChannelStatus) super.set(str, obj);
    }

    public ChannelStatus setPrivacyStatus(String str) {
        this.privacyStatus = str;
        return this;
    }
}
