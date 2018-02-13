package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class Activity extends GenericJson {
    @Key
    private ActivityContentDetails contentDetails;
    @Key
    private String etag;
    @Key
    private String id;
    @Key
    private String kind;
    @Key
    private ActivitySnippet snippet;

    public Activity clone() {
        return (Activity) super.clone();
    }

    public ActivityContentDetails getContentDetails() {
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

    public ActivitySnippet getSnippet() {
        return this.snippet;
    }

    public Activity set(String str, Object obj) {
        return (Activity) super.set(str, obj);
    }

    public Activity setContentDetails(ActivityContentDetails activityContentDetails) {
        this.contentDetails = activityContentDetails;
        return this;
    }

    public Activity setEtag(String str) {
        this.etag = str;
        return this;
    }

    public Activity setId(String str) {
        this.id = str;
        return this;
    }

    public Activity setKind(String str) {
        this.kind = str;
        return this;
    }

    public Activity setSnippet(ActivitySnippet activitySnippet) {
        this.snippet = activitySnippet;
        return this;
    }
}
