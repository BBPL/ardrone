package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class LiveBroadcastList extends GenericJson {
    @Key
    private String etag;
    @Key
    private List<LiveBroadcast> items;
    @Key
    private String kind;
    @Key
    private String nextPageToken;
    @Key
    private PageInfo pageInfo;
    @Key
    private String prevPageToken;

    public LiveBroadcastList clone() {
        return (LiveBroadcastList) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public List<LiveBroadcast> getItems() {
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

    public LiveBroadcastList set(String str, Object obj) {
        return (LiveBroadcastList) super.set(str, obj);
    }

    public LiveBroadcastList setEtag(String str) {
        this.etag = str;
        return this;
    }

    public LiveBroadcastList setItems(List<LiveBroadcast> list) {
        this.items = list;
        return this;
    }

    public LiveBroadcastList setKind(String str) {
        this.kind = str;
        return this;
    }

    public LiveBroadcastList setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public LiveBroadcastList setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    public LiveBroadcastList setPrevPageToken(String str) {
        this.prevPageToken = str;
        return this;
    }
}
