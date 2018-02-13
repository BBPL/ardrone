package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class VideoMonetizationDetails extends GenericJson {
    @Key
    private AccessPolicy access;

    public VideoMonetizationDetails clone() {
        return (VideoMonetizationDetails) super.clone();
    }

    public AccessPolicy getAccess() {
        return this.access;
    }

    public VideoMonetizationDetails set(String str, Object obj) {
        return (VideoMonetizationDetails) super.set(str, obj);
    }

    public VideoMonetizationDetails setAccess(AccessPolicy accessPolicy) {
        this.access = accessPolicy;
        return this;
    }
}
