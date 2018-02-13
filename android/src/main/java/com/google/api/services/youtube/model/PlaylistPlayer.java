package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class PlaylistPlayer extends GenericJson {
    @Key
    private String embedHtml;

    public PlaylistPlayer clone() {
        return (PlaylistPlayer) super.clone();
    }

    public String getEmbedHtml() {
        return this.embedHtml;
    }

    public PlaylistPlayer set(String str, Object obj) {
        return (PlaylistPlayer) super.set(str, obj);
    }

    public PlaylistPlayer setEmbedHtml(String str) {
        this.embedHtml = str;
        return this;
    }
}
