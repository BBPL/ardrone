package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class GuideCategory extends GenericJson {
    @Key
    private String etag;
    @Key
    private String id;
    @Key
    private String kind;
    @Key
    private GuideCategorySnippet snippet;

    public GuideCategory clone() {
        return (GuideCategory) super.clone();
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

    public GuideCategorySnippet getSnippet() {
        return this.snippet;
    }

    public GuideCategory set(String str, Object obj) {
        return (GuideCategory) super.set(str, obj);
    }

    public GuideCategory setEtag(String str) {
        this.etag = str;
        return this;
    }

    public GuideCategory setId(String str) {
        this.id = str;
        return this;
    }

    public GuideCategory setKind(String str) {
        this.kind = str;
        return this;
    }

    public GuideCategory setSnippet(GuideCategorySnippet guideCategorySnippet) {
        this.snippet = guideCategorySnippet;
        return this;
    }
}
