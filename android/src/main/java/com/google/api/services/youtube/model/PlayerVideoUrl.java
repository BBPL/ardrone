package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class PlayerVideoUrl extends GenericJson {
    @Key
    private Long itag;
    @Key
    private String url;

    public PlayerVideoUrl clone() {
        return (PlayerVideoUrl) super.clone();
    }

    public Long getItag() {
        return this.itag;
    }

    public String getUrl() {
        return this.url;
    }

    public PlayerVideoUrl set(String str, Object obj) {
        return (PlayerVideoUrl) super.set(str, obj);
    }

    public PlayerVideoUrl setItag(Long l) {
        this.itag = l;
        return this;
    }

    public PlayerVideoUrl setUrl(String str) {
        this.url = str;
        return this;
    }
}
