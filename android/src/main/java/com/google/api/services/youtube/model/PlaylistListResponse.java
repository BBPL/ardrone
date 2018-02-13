package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class PlaylistListResponse extends GenericJson {
    @Key
    private String etag;
    @Key
    private List<Playlist> items;
    @Key
    private String kind;
    @Key
    private String nextPageToken;
    @Key
    private PageInfo pageInfo;
    @Key
    private String prevPageToken;

    public PlaylistListResponse clone() {
        return (PlaylistListResponse) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public List<Playlist> getItems() {
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

    public PlaylistListResponse set(String str, Object obj) {
        return (PlaylistListResponse) super.set(str, obj);
    }

    public PlaylistListResponse setEtag(String str) {
        this.etag = str;
        return this;
    }

    public PlaylistListResponse setItems(List<Playlist> list) {
        this.items = list;
        return this;
    }

    public PlaylistListResponse setKind(String str) {
        this.kind = str;
        return this;
    }

    public PlaylistListResponse setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public PlaylistListResponse setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    public PlaylistListResponse setPrevPageToken(String str) {
        this.prevPageToken = str;
        return this;
    }
}
