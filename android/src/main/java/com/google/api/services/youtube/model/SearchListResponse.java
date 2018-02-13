package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class SearchListResponse extends GenericJson {
    @Key
    private String etag;
    @Key
    private List<SearchResult> items;
    @Key
    private String kind;
    @Key
    private String nextPageToken;
    @Key
    private PageInfo pageInfo;
    @Key
    private String prevPageToken;

    static {
        Data.nullOf(SearchResult.class);
    }

    public SearchListResponse clone() {
        return (SearchListResponse) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public List<SearchResult> getItems() {
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

    public SearchListResponse set(String str, Object obj) {
        return (SearchListResponse) super.set(str, obj);
    }

    public SearchListResponse setEtag(String str) {
        this.etag = str;
        return this;
    }

    public SearchListResponse setItems(List<SearchResult> list) {
        this.items = list;
        return this;
    }

    public SearchListResponse setKind(String str) {
        this.kind = str;
        return this;
    }

    public SearchListResponse setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public SearchListResponse setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    public SearchListResponse setPrevPageToken(String str) {
        this.prevPageToken = str;
        return this;
    }
}
