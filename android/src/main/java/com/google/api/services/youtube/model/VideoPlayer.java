package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class VideoPlayer extends GenericJson {
    @Key
    private String embedHtml;

    public VideoPlayer clone() {
        return (VideoPlayer) super.clone();
    }

    public String getEmbedHtml() {
        return this.embedHtml;
    }

    public VideoPlayer set(String str, Object obj) {
        return (VideoPlayer) super.set(str, obj);
    }

    public VideoPlayer setEmbedHtml(String str) {
        this.embedHtml = str;
        return this;
    }
}
