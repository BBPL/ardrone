package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

public final class SearchResultSnippet extends GenericJson {
    @Key
    private String channelId;
    @Key
    private String channelTitle;
    @Key
    private String description;
    @Key
    private DateTime publishedAt;
    @Key
    private ThumbnailDetails thumbnails;
    @Key
    private String title;

    public SearchResultSnippet clone() {
        return (SearchResultSnippet) super.clone();
    }

    public String getChannelId() {
        return this.channelId;
    }

    public String getChannelTitle() {
        return this.channelTitle;
    }

    public String getDescription() {
        return this.description;
    }

    public DateTime getPublishedAt() {
        return this.publishedAt;
    }

    public ThumbnailDetails getThumbnails() {
        return this.thumbnails;
    }

    public String getTitle() {
        return this.title;
    }

    public SearchResultSnippet set(String str, Object obj) {
        return (SearchResultSnippet) super.set(str, obj);
    }

    public SearchResultSnippet setChannelId(String str) {
        this.channelId = str;
        return this;
    }

    public SearchResultSnippet setChannelTitle(String str) {
        this.channelTitle = str;
        return this;
    }

    public SearchResultSnippet setDescription(String str) {
        this.description = str;
        return this;
    }

    public SearchResultSnippet setPublishedAt(DateTime dateTime) {
        this.publishedAt = dateTime;
        return this;
    }

    public SearchResultSnippet setThumbnails(ThumbnailDetails thumbnailDetails) {
        this.thumbnails = thumbnailDetails;
        return this;
    }

    public SearchResultSnippet setTitle(String str) {
        this.title = str;
        return this;
    }
}
