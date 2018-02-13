package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class VideoAgeGating extends GenericJson {
    @Key
    private Boolean alcoholContent;
    @Key
    private Boolean restricted;
    @Key
    private String videoGameRating;

    public VideoAgeGating clone() {
        return (VideoAgeGating) super.clone();
    }

    public Boolean getAlcoholContent() {
        return this.alcoholContent;
    }

    public Boolean getRestricted() {
        return this.restricted;
    }

    public String getVideoGameRating() {
        return this.videoGameRating;
    }

    public VideoAgeGating set(String str, Object obj) {
        return (VideoAgeGating) super.set(str, obj);
    }

    public VideoAgeGating setAlcoholContent(Boolean bool) {
        this.alcoholContent = bool;
        return this;
    }

    public VideoAgeGating setRestricted(Boolean bool) {
        this.restricted = bool;
        return this;
    }

    public VideoAgeGating setVideoGameRating(String str) {
        this.videoGameRating = str;
        return this;
    }
}
