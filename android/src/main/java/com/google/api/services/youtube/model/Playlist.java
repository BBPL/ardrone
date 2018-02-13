package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class Playlist extends GenericJson {
    @Key
    private PlaylistContentDetails contentDetails;
    @Key
    private String etag;
    @Key
    private String id;
    @Key
    private String kind;
    @Key
    private PlaylistPlayer player;
    @Key
    private PlaylistSnippet snippet;
    @Key
    private PlaylistStatus status;

    public Playlist clone() {
        return (Playlist) super.clone();
    }

    public PlaylistContentDetails getContentDetails() {
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

    public PlaylistPlayer getPlayer() {
        return this.player;
    }

    public PlaylistSnippet getSnippet() {
        return this.snippet;
    }

    public PlaylistStatus getStatus() {
        return this.status;
    }

    public Playlist set(String str, Object obj) {
        return (Playlist) super.set(str, obj);
    }

    public Playlist setContentDetails(PlaylistContentDetails playlistContentDetails) {
        this.contentDetails = playlistContentDetails;
        return this;
    }

    public Playlist setEtag(String str) {
        this.etag = str;
        return this;
    }

    public Playlist setId(String str) {
        this.id = str;
        return this;
    }

    public Playlist setKind(String str) {
        this.kind = str;
        return this;
    }

    public Playlist setPlayer(PlaylistPlayer playlistPlayer) {
        this.player = playlistPlayer;
        return this;
    }

    public Playlist setSnippet(PlaylistSnippet playlistSnippet) {
        this.snippet = playlistSnippet;
        return this;
    }

    public Playlist setStatus(PlaylistStatus playlistStatus) {
        this.status = playlistStatus;
        return this;
    }
}
