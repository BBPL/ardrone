package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

public final class LiveBroadcastSnippet extends GenericJson {
    @Key
    private DateTime actualEndTime;
    @Key
    private DateTime actualStartTime;
    @Key
    private String channelId;
    @Key
    private String description;
    @Key
    private DateTime publishedAt;
    @Key
    private DateTime scheduledEndTime;
    @Key
    private DateTime scheduledStartTime;
    @Key
    private ThumbnailDetails thumbnails;
    @Key
    private String title;

    public LiveBroadcastSnippet clone() {
        return (LiveBroadcastSnippet) super.clone();
    }

    public DateTime getActualEndTime() {
        return this.actualEndTime;
    }

    public DateTime getActualStartTime() {
        return this.actualStartTime;
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

    public DateTime getScheduledEndTime() {
        return this.scheduledEndTime;
    }

    public DateTime getScheduledStartTime() {
        return this.scheduledStartTime;
    }

    public ThumbnailDetails getThumbnails() {
        return this.thumbnails;
    }

    public String getTitle() {
        return this.title;
    }

    public LiveBroadcastSnippet set(String str, Object obj) {
        return (LiveBroadcastSnippet) super.set(str, obj);
    }

    public LiveBroadcastSnippet setActualEndTime(DateTime dateTime) {
        this.actualEndTime = dateTime;
        return this;
    }

    public LiveBroadcastSnippet setActualStartTime(DateTime dateTime) {
        this.actualStartTime = dateTime;
        return this;
    }

    public LiveBroadcastSnippet setChannelId(String str) {
        this.channelId = str;
        return this;
    }

    public LiveBroadcastSnippet setDescription(String str) {
        this.description = str;
        return this;
    }

    public LiveBroadcastSnippet setPublishedAt(DateTime dateTime) {
        this.publishedAt = dateTime;
        return this;
    }

    public LiveBroadcastSnippet setScheduledEndTime(DateTime dateTime) {
        this.scheduledEndTime = dateTime;
        return this;
    }

    public LiveBroadcastSnippet setScheduledStartTime(DateTime dateTime) {
        this.scheduledStartTime = dateTime;
        return this;
    }

    public LiveBroadcastSnippet setThumbnails(ThumbnailDetails thumbnailDetails) {
        this.thumbnails = thumbnailDetails;
        return this;
    }

    public LiveBroadcastSnippet setTitle(String str) {
        this.title = str;
        return this;
    }
}
