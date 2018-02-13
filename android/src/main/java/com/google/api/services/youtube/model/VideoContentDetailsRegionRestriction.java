package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class VideoContentDetailsRegionRestriction extends GenericJson {
    @Key
    private List<String> allowed;
    @Key
    private List<String> blocked;

    public VideoContentDetailsRegionRestriction clone() {
        return (VideoContentDetailsRegionRestriction) super.clone();
    }

    public List<String> getAllowed() {
        return this.allowed;
    }

    public List<String> getBlocked() {
        return this.blocked;
    }

    public VideoContentDetailsRegionRestriction set(String str, Object obj) {
        return (VideoContentDetailsRegionRestriction) super.set(str, obj);
    }

    public VideoContentDetailsRegionRestriction setAllowed(List<String> list) {
        this.allowed = list;
        return this;
    }

    public VideoContentDetailsRegionRestriction setBlocked(List<String> list) {
        this.blocked = list;
        return this;
    }
}
