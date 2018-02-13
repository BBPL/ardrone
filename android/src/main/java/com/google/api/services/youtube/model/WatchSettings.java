package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class WatchSettings extends GenericJson {
    @Key
    private String backgroundColor;
    @Key
    private String featuredPlaylistId;
    @Key
    private String textColor;

    public WatchSettings clone() {
        return (WatchSettings) super.clone();
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    public String getFeaturedPlaylistId() {
        return this.featuredPlaylistId;
    }

    public String getTextColor() {
        return this.textColor;
    }

    public WatchSettings set(String str, Object obj) {
        return (WatchSettings) super.set(str, obj);
    }

    public WatchSettings setBackgroundColor(String str) {
        this.backgroundColor = str;
        return this;
    }

    public WatchSettings setFeaturedPlaylistId(String str) {
        this.featuredPlaylistId = str;
        return this;
    }

    public WatchSettings setTextColor(String str) {
        this.textColor = str;
        return this;
    }
}
