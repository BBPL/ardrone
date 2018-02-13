package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Key;
import java.math.BigInteger;
import java.util.List;

public final class PlaylistSnippet extends GenericJson {
    @Key
    private String channelId;
    @Key
    private String channelTitle;
    @Key
    private String description;
    @JsonString
    @Key
    private BigInteger publishedAtMs;
    @Key
    private List<String> tags;
    @Key
    private ThumbnailDetails thumbnails;
    @Key
    private String title;
    @JsonString
    @Key
    private BigInteger updatedAtMs;

    public PlaylistSnippet clone() {
        return (PlaylistSnippet) super.clone();
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

    public BigInteger getPublishedAtMs() {
        return this.publishedAtMs;
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

    public BigInteger getUpdatedAtMs() {
        return this.updatedAtMs;
    }

    public PlaylistSnippet set(String str, Object obj) {
        return (PlaylistSnippet) super.set(str, obj);
    }

    public PlaylistSnippet setChannelId(String str) {
        this.channelId = str;
        return this;
    }

    public PlaylistSnippet setChannelTitle(String str) {
        this.channelTitle = str;
        return this;
    }

    public PlaylistSnippet setDescription(String str) {
        this.description = str;
        return this;
    }

    public PlaylistSnippet setPublishedAtMs(BigInteger bigInteger) {
        this.publishedAtMs = bigInteger;
        return this;
    }

    public PlaylistSnippet setTags(List<String> list) {
        this.tags = list;
        return this;
    }

    public PlaylistSnippet setThumbnails(ThumbnailDetails thumbnailDetails) {
        this.thumbnails = thumbnailDetails;
        return this;
    }

    public PlaylistSnippet setTitle(String str) {
        this.title = str;
        return this;
    }

    public PlaylistSnippet setUpdatedAtMs(BigInteger bigInteger) {
        this.updatedAtMs = bigInteger;
        return this;
    }
}
