package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class ChannelListResponse extends GenericJson {
    @Key
    private String etag;
    @Key
    private List<Channel> items;
    @Key
    private String kind;
    @Key
    private String nextPageToken;
    @Key
    private PageInfo pageInfo;
    @Key
    private String prevPageToken;

    static {
        Data.nullOf(Channel.class);
    }

    public ChannelListResponse clone() {
        return (ChannelListResponse) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public List<Channel> getItems() {
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

    public ChannelListResponse set(String str, Object obj) {
        return (ChannelListResponse) super.set(str, obj);
    }

    public ChannelListResponse setEtag(String str) {
        this.etag = str;
        return this;
    }

    public ChannelListResponse setItems(List<Channel> list) {
        this.items = list;
        return this;
    }

    public ChannelListResponse setKind(String str) {
        this.kind = str;
        return this;
    }

    public ChannelListResponse setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public ChannelListResponse setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    public ChannelListResponse setPrevPageToken(String str) {
        this.prevPageToken = str;
        return this;
    }
}
