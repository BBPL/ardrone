package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

public final class PlaylistItemSnippet extends GenericJson {
    @Key
    private String channelId;
    @Key
    private String channelTitle;
    @Key
    private String description;
    @Key
    private String playlistId;
    @Key
    private Long position;
    @Key
    private DateTime publishedAt;
    @Key
    private ResourceId resourceId;
    @Key
    private ThumbnailDetails thumbnails;
    @Key
    private String title;

    public PlaylistItemSnippet clone() {
        return (PlaylistItemSnippet) super.clone();
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

    public String getPlaylistId() {
        return this.playlistId;
    }

    public Long getPosition() {
        return this.position;
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

    public PlaylistItemSnippet set(String str, Object obj) {
        return (PlaylistItemSnippet) super.set(str, obj);
    }

    public PlaylistItemSnippet setChannelId(String str) {
        this.channelId = str;
        return this;
    }

    public PlaylistItemSnippet setChannelTitle(String str) {
        this.channelTitle = str;
        return this;
    }

    public PlaylistItemSnippet setDescription(String str) {
        this.description = str;
        return this;
    }

    public PlaylistItemSnippet setPlaylistId(String str) {
        this.playlistId = str;
        return this;
    }

    public PlaylistItemSnippet setPosition(Long l) {
        this.position = l;
        return this;
    }

    public PlaylistItemSnippet setPublishedAt(DateTime dateTime) {
        this.publishedAt = dateTime;
        return this;
    }

    public PlaylistItemSnippet setResourceId(ResourceId resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public PlaylistItemSnippet setThumbnails(ThumbnailDetails thumbnailDetails) {
        this.thumbnails = thumbnailDetails;
        return this;
    }

    public PlaylistItemSnippet setTitle(String str) {
        this.title = str;
        return this;
    }
}
