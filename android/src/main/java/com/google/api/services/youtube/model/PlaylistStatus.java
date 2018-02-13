package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class PlaylistStatus extends GenericJson {
    @Key
    private String privacyStatus;

    public PlaylistStatus clone() {
        return (PlaylistStatus) super.clone();
    }

    public String getPrivacyStatus() {
        return this.privacyStatus;
    }

    public PlaylistStatus set(String str, Object obj) {
        return (PlaylistStatus) super.set(str, obj);
    }

    public PlaylistStatus setPrivacyStatus(String str) {
        this.privacyStatus = str;
        return this;
    }
}
