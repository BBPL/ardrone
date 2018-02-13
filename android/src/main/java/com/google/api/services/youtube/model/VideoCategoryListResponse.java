package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class VideoCategoryListResponse extends GenericJson {
    @Key
    private String etag;
    @Key
    private List<VideoCategory> items;
    @Key
    private String kind;

    public VideoCategoryListResponse clone() {
        return (VideoCategoryListResponse) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public List<VideoCategory> getItems() {
        return this.items;
    }

    public String getKind() {
        return this.kind;
    }

    public VideoCategoryListResponse set(String str, Object obj) {
        return (VideoCategoryListResponse) super.set(str, obj);
    }

    public VideoCategoryListResponse setEtag(String str) {
        this.etag = str;
        return this;
    }

    public VideoCategoryListResponse setItems(List<VideoCategory> list) {
        this.items = list;
        return this;
    }

    public VideoCategoryListResponse setKind(String str) {
        this.kind = str;
        return this;
    }
}
