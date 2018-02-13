package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class LiveStreamList extends GenericJson {
    @Key
    private String etag;
    @Key
    private List<LiveStream> items;
    @Key
    private String kind;
    @Key
    private String nextPageToken;
    @Key
    private PageInfo pageInfo;
    @Key
    private String prevPageToken;

    static {
        Data.nullOf(LiveStream.class);
    }

    public LiveStreamList clone() {
        return (LiveStreamList) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public List<LiveStream> getItems() {
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

    public LiveStreamList set(String str, Object obj) {
        return (LiveStreamList) super.set(str, obj);
    }

    public LiveStreamList setEtag(String str) {
        this.etag = str;
        return this;
    }

    public LiveStreamList setItems(List<LiveStream> list) {
        this.items = list;
        return this;
    }

    public LiveStreamList setKind(String str) {
        this.kind = str;
        return this;
    }

    public LiveStreamList setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public LiveStreamList setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    public LiveStreamList setPrevPageToken(String str) {
        this.prevPageToken = str;
        return this;
    }
}
