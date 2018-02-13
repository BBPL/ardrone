package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class GuideCategoryListResponse extends GenericJson {
    @Key
    private String etag;
    @Key
    private List<GuideCategory> items;
    @Key
    private String kind;

    static {
        Data.nullOf(GuideCategory.class);
    }

    public GuideCategoryListResponse clone() {
        return (GuideCategoryListResponse) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public List<GuideCategory> getItems() {
        return this.items;
    }

    public String getKind() {
        return this.kind;
    }

    public GuideCategoryListResponse set(String str, Object obj) {
        return (GuideCategoryListResponse) super.set(str, obj);
    }

    public GuideCategoryListResponse setEtag(String str) {
        this.etag = str;
        return this;
    }

    public GuideCategoryListResponse setItems(List<GuideCategory> list) {
        this.items = list;
        return this;
    }

    public GuideCategoryListResponse setKind(String str) {
        this.kind = str;
        return this;
    }
}
