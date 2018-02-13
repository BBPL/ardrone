package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

public final class SubscriptionSnippet extends GenericJson {
    @Key
    private String channelId;
    @Key
    private String description;
    @Key
    private DateTime publishedAt;
    @Key
    private ResourceId resourceId;
    @Key
    private ThumbnailDetails thumbnails;
    @Key
    private String title;

    public SubscriptionSnippet clone() {
        return (SubscriptionSnippet) super.clone();
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

    public ResourceId getResourceId() {
        return this.resourceId;
    }

    public ThumbnailDetails getThumbnails() {
        return this.thumbnails;
    }

    public String getTitle() {
        return this.title;
    }

    public SubscriptionSnippet set(String str, Object obj) {
        return (SubscriptionSnippet) super.set(str, obj);
    }

    public SubscriptionSnippet setChannelId(String str) {
        this.channelId = str;
        return this;
    }

    public SubscriptionSnippet setDescription(String str) {
        this.description = str;
        return this;
    }

    public SubscriptionSnippet setPublishedAt(DateTime dateTime) {
        this.publishedAt = dateTime;
        return this;
    }

    public SubscriptionSnippet setResourceId(ResourceId resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public SubscriptionSnippet setThumbnails(ThumbnailDetails thumbnailDetails) {
        this.thumbnails = thumbnailDetails;
        return this;
    }

    public SubscriptionSnippet setTitle(String str) {
        this.title = str;
        return this;
    }
}
