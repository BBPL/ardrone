package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class GuideCategorySnippet extends GenericJson {
    @Key
    private String channelId;
    @Key
    private String title;

    public GuideCategorySnippet clone() {
        return (GuideCategorySnippet) super.clone();
    }

    public String getChannelId() {
        return this.channelId;
    }

    public String getTitle() {
        return this.title;
    }

    public GuideCategorySnippet set(String str, Object obj) {
        return (GuideCategorySnippet) super.set(str, obj);
    }

    public GuideCategorySnippet setChannelId(String str) {
        this.channelId = str;
        return this;
    }

    public GuideCategorySnippet setTitle(String str) {
        this.title = str;
        return this;
    }
}
