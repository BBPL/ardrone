package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class SubscriptionListResponse extends GenericJson {
    @Key
    private String etag;
    @Key
    private List<Subscription> items;
    @Key
    private String kind;
    @Key
    private String nextPageToken;
    @Key
    private PageInfo pageInfo;
    @Key
    private String prevPageToken;

    public SubscriptionListResponse clone() {
        return (SubscriptionListResponse) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public List<Subscription> getItems() {
        return this.items;
    }

    public String getKind() {
        return this.kind;
    }

    public String getNextPageToken() {
        return this.nextPageToken;
    }

    public PageInfo getPageInfo() {
        return this.pageInfo;
    }

    public String getPrevPageToken() {
        return this.prevPageToken;
    }

    public SubscriptionListResponse set(String str, Object obj) {
        return (SubscriptionListResponse) super.set(str, obj);
    }

    public SubscriptionListResponse setEtag(String str) {
        this.etag = str;
        return this;
    }

    public SubscriptionListResponse setItems(List<Subscription> list) {
        this.items = list;
        return this;
    }

    public SubscriptionListResponse setKind(String str) {
        this.kind = str;
        return this;
    }

    public SubscriptionListResponse setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public SubscriptionListResponse setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    public SubscriptionListResponse setPrevPageToken(String str) {
        this.prevPageToken = str;
        return this;
    }
}
