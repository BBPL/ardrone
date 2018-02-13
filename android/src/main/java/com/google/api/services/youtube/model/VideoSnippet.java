package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import java.util.List;

public final class VideoSnippet extends GenericJson {
    @Key
    private String categoryId;
    @Key
    private String channelId;
    @Key
    private String channelTitle;
    @Key
    private String description;
    @Key
    private DateTime publishedAt;
    @Key
    private List<String> tags;
    @Key
    private ThumbnailDetails thumbnails;
    @Key
    private String title;

    public VideoSnippet clone() {
        return (VideoSnippet) super.clone();
    }

    public String getCategoryId() {
        return this.categoryId;
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

    public List<String> getTags() {
        return this.tags;
    }

    public ThumbnailDetails getThumbnails() {
        return this.thumbnails;
    }

    public String getTitle() {
        return this.title;
    }

    public VideoSnippet set(String str, Object obj) {
        return (VideoSnippet) super.set(str, obj);
    }

    public VideoSnippet setCategoryId(String str) {
        this.categoryId = str;
        return this;
    }

    public VideoSnippet setChannelId(String str) {
        this.channelId = str;
        return this;
    }

    public VideoSnippet setChannelTitle(String str) {
        this.channelTitle = str;
        return this;
    }

    public VideoSnippet setDescription(String str) {
        this.description = str;
        return this;
    }

    public VideoSnippet setPublishedAt(DateTime dateTime) {
        this.publishedAt = dateTime;
        return this;
    }

    public VideoSnippet setTags(List<String> list) {
        this.tags = list;
        return this;
    }

    public VideoSnippet setThumbnails(ThumbnailDetails thumbnailDetails) {
        this.thumbnails = thumbnailDetails;
        return this;
    }

    public VideoSnippet setTitle(String str) {
        this.title = str;
        return this;
    }
}
