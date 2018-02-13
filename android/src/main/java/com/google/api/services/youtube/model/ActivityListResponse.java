package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class ActivityListResponse extends GenericJson {
    @Key
    private String etag;
    @Key
    private List<Activity> items;
    @Key
    private String kind;
    @Key
    private String nextPageToken;
    @Key
    private PageInfo pageInfo;
    @Key
    private String prevPageToken;

    public ActivityListResponse clone() {
        return (ActivityListResponse) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public List<Activity> getItems() {
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

    public ActivityListResponse set(String str, Object obj) {
        return (ActivityListResponse) super.set(str, obj);
    }

    public ActivityListResponse setEtag(String str) {
        this.etag = str;
        return this;
    }

    public ActivityListResponse setItems(List<Activity> list) {
        this.items = list;
        return this;
    }

    public ActivityListResponse setKind(String str) {
        this.kind = str;
        return this;
    }

    public ActivityListResponse setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public ActivityListResponse setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    public ActivityListResponse setPrevPageToken(String str) {
        this.prevPageToken = str;
        return this;
    }
}
