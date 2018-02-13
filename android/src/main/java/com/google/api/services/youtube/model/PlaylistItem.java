package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class PlaylistItem extends GenericJson {
    @Key
    private PlaylistItemContentDetails contentDetails;
    @Key
    private String etag;
    @Key
    private String id;
    @Key
    private String kind;
    @Key
    private PlaylistItemSnippet snippet;

    public PlaylistItem clone() {
        return (PlaylistItem) super.clone();
    }

    public PlaylistItemContentDetails getContentDetails() {
        return this.contentDetails;
    }

    public String getEtag() {
        return this.etag;
    }

    public String getId() {
        return this.id;
    }

    public String getKind() {
        return this.kind;
    }

    public PlaylistItemSnippet getSnippet() {
        return this.snippet;
    }

    public PlaylistItem set(String str, Object obj) {
        return (PlaylistItem) super.set(str, obj);
    }

    public PlaylistItem setContentDetails(PlaylistItemContentDetails playlistItemContentDetails) {
        this.contentDetails = playlistItemContentDetails;
        return this;
    }

    public PlaylistItem setEtag(String str) {
        this.etag = str;
        return this;
    }

    public PlaylistItem setId(String str) {
        this.id = str;
        return this;
    }

    public PlaylistItem setKind(String str) {
        this.kind = str;
        return this;
    }

    public PlaylistItem setSnippet(PlaylistItemSnippet playlistItemSnippet) {
        this.snippet = playlistItemSnippet;
        return this;
    }
}
