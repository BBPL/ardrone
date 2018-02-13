package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class SearchResult extends GenericJson {
    @Key
    private String etag;
    @Key
    private ResourceId id;
    @Key
    private String kind;
    @Key
    private SearchResultSnippet snippet;

    public SearchResult clone() {
        return (SearchResult) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public ResourceId getId() {
        return this.id;
    }

    public String getKind() {
        return this.kind;
    }

    public SearchResultSnippet getSnippet() {
        return this.snippet;
    }

    public SearchResult set(String str, Object obj) {
        return (SearchResult) super.set(str, obj);
    }

    public SearchResult setEtag(String str) {
        this.etag = str;
        return this;
    }

    public SearchResult setId(ResourceId resourceId) {
        this.id = resourceId;
        return this;
    }

    public SearchResult setKind(String str) {
        this.kind = str;
        return this;
    }

    public SearchResult setSnippet(SearchResultSnippet searchResultSnippet) {
        this.snippet = searchResultSnippet;
        return this;
    }
}
