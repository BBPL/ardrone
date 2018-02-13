package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class PlaylistItemListResponse extends GenericJson {
    @Key
    private String etag;
    @Key
    private List<PlaylistItem> items;
    @Key
    private String kind;
    @Key
    private String nextPageToken;
    @Key
    private PageInfo pageInfo;
    @Key
    private String prevPageToken;

    static {
        Data.nullOf(PlaylistItem.class);
    }

    public PlaylistItemListResponse clone() {
        return (PlaylistItemListResponse) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public List<PlaylistItem> getItems() {
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

    public PlaylistItemListResponse set(String str, Object obj) {
        return (PlaylistItemListResponse) super.set(str, obj);
    }

    public PlaylistItemListResponse setEtag(String str) {
        this.etag = str;
        return this;
    }

    public PlaylistItemListResponse setItems(List<PlaylistItem> list) {
        this.items = list;
        return this;
    }

    public PlaylistItemListResponse setKind(String str) {
        this.kind = str;
        return this;
    }

    public PlaylistItemListResponse setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public PlaylistItemListResponse setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    public PlaylistItemListResponse setPrevPageToken(String str) {
        this.prevPageToken = str;
        return this;
    }
}
