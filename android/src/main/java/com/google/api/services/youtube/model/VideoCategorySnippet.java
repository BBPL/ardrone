package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class VideoCategorySnippet extends GenericJson {
    @Key
    private String channelId;
    @Key
    private String title;

    public VideoCategorySnippet clone() {
        return (VideoCategorySnippet) super.clone();
    }

    public String getChannelId() {
        return this.channelId;
    }

    public String getTitle() {
        return this.title;
    }

    public VideoCategorySnippet set(String str, Object obj) {
        return (VideoCategorySnippet) super.set(str, obj);
    }

    public VideoCategorySnippet setChannelId(String str) {
        this.channelId = str;
        return this;
    }

    public VideoCategorySnippet setTitle(String str) {
        this.title = str;
        return this;
    }
}
