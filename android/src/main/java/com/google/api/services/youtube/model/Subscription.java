package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class Subscription extends GenericJson {
    @Key
    private SubscriptionContentDetails contentDetails;
    @Key
    private String etag;
    @Key
    private String id;
    @Key
    private String kind;
    @Key
    private SubscriptionSnippet snippet;

    public Subscription clone() {
        return (Subscription) super.clone();
    }

    public SubscriptionContentDetails getContentDetails() {
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

    public SubscriptionSnippet getSnippet() {
        return this.snippet;
    }

    public Subscription set(String str, Object obj) {
        return (Subscription) super.set(str, obj);
    }

    public Subscription setContentDetails(SubscriptionContentDetails subscriptionContentDetails) {
        this.contentDetails = subscriptionContentDetails;
        return this;
    }

    public Subscription setEtag(String str) {
        this.etag = str;
        return this;
    }

    public Subscription setId(String str) {
        this.id = str;
        return this;
    }

    public Subscription setKind(String str) {
        this.kind = str;
        return this;
    }

    public Subscription setSnippet(SubscriptionSnippet subscriptionSnippet) {
        this.snippet = subscriptionSnippet;
        return this;
    }
}
