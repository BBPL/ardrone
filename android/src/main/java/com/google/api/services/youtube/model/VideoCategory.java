package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class VideoCategory extends GenericJson {
    @Key
    private String etag;
    @Key
    private String id;
    @Key
    private String kind;
    @Key
    private VideoCategorySnippet snippet;

    public VideoCategory clone() {
        return (VideoCategory) super.clone();
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

    public VideoCategorySnippet getSnippet() {
        return this.snippet;
    }

    public VideoCategory set(String str, Object obj) {
        return (VideoCategory) super.set(str, obj);
    }

    public VideoCategory setEtag(String str) {
        this.etag = str;
        return this;
    }

    public VideoCategory setId(String str) {
        this.id = str;
        return this;
    }

    public VideoCategory setKind(String str) {
        this.kind = str;
        return this;
    }

    public VideoCategory setSnippet(VideoCategorySnippet videoCategorySnippet) {
        this.snippet = videoCategorySnippet;
        return this;
    }
}
