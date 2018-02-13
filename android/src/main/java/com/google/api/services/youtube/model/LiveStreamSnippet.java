package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

public final class LiveStreamSnippet extends GenericJson {
    @Key
    private String channelId;
    @Key
    private String description;
    @Key
    private DateTime publishedAt;
    @Key
    private String title;

    public LiveStreamSnippet clone() {
        return (LiveStreamSnippet) super.clone();
    }

    public String getChannelId() {
        return this.channelId;
    }

    public String getDescription() {
        return this.description;
    }

    public DateTime getPublishedAt() {
        return this.publishedAt;
    }

    public String getTitle() {
        return this.title;
    }

    public LiveStreamSnippet set(String str, Object obj) {
        return (LiveStreamSnippet) super.set(str, obj);
    }

    public LiveStreamSnippet setChannelId(String str) {
        this.channelId = str;
        return this;
    }

    public LiveStreamSnippet setDescription(String str) {
        this.description = str;
        return this;
    }

    public LiveStreamSnippet setPublishedAt(DateTime dateTime) {
        this.publishedAt = dateTime;
        return this;
    }

    public LiveStreamSnippet setTitle(String str) {
        this.title = str;
        return this;
    }
}
