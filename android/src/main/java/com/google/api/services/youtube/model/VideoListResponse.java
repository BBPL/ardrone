package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class VideoListResponse extends GenericJson {
    @Key
    private String etag;
    @Key
    private List<Video> items;
    @Key
    private String kind;

    public VideoListResponse clone() {
        return (VideoListResponse) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public List<Video> getItems() {
        return this.items;
    }

    public String getKind() {
        return this.kind;
    }

    public VideoListResponse set(String str, Object obj) {
        return (VideoListResponse) super.set(str, obj);
    }

    public VideoListResponse setEtag(String str) {
        this.etag = str;
        return this;
    }

    public VideoListResponse setItems(List<Video> list) {
        this.items = list;
        return this;
    }

    public VideoListResponse setKind(String str) {
        this.kind = str;
        return this;
    }
}
