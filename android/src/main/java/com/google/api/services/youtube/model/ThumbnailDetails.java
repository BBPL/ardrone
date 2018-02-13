package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class ThumbnailDetails extends GenericJson {
    @Key
    private Thumbnail high;
    @Key
    private Thumbnail maxres;
    @Key
    private Thumbnail medium;
    @Key
    private Thumbnail standard;
    @Key("default")
    private Thumbnail youtubeDefault;

    public ThumbnailDetails clone() {
        return (ThumbnailDetails) super.clone();
    }

    public Thumbnail getDefault() {
        return this.youtubeDefault;
    }

    public Thumbnail getHigh() {
        return this.high;
    }

    public Thumbnail getMaxres() {
        return this.maxres;
    }

    public Thumbnail getMedium() {
        return this.medium;
    }

    public Thumbnail getStandard() {
        return this.standard;
    }

    public ThumbnailDetails set(String str, Object obj) {
        return (ThumbnailDetails) super.set(str, obj);
    }

    public ThumbnailDetails setDefault(Thumbnail thumbnail) {
        this.youtubeDefault = thumbnail;
        return this;
    }

    public ThumbnailDetails setHigh(Thumbnail thumbnail) {
        this.high = thumbnail;
        return this;
    }

    public ThumbnailDetails setMaxres(Thumbnail thumbnail) {
        this.maxres = thumbnail;
        return this;
    }

    public ThumbnailDetails setMedium(Thumbnail thumbnail) {
        this.medium = thumbnail;
        return this;
    }

    public ThumbnailDetails setStandard(Thumbnail thumbnail) {
        this.standard = thumbnail;
        return this;
    }
}
