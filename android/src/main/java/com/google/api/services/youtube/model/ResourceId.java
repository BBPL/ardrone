package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class ResourceId extends GenericJson {
    @Key
    private String channelId;
    @Key
    private String kind;
    @Key
    private String playlistId;
    @Key
    private String videoId;

    public ResourceId clone() {
        return (ResourceId) super.clone();
    }

    public String getChannelId() {
        return this.channelId;
    }

    public String getKind() {
        return this.kind;
    }

    public String getPlaylistId() {
        return this.playlistId;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public ResourceId set(String str, Object obj) {
        return (ResourceId) super.set(str, obj);
    }

    public ResourceId setChannelId(String str) {
        this.channelId = str;
        return this;
    }

    public ResourceId setKind(String str) {
        this.kind = str;
        return this;
    }

    public ResourceId setPlaylistId(String str) {
        this.playlistId = str;
        return this;
    }

    public ResourceId setVideoId(String str) {
        this.videoId = str;
        return this;
    }
}
