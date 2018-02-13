package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class LiveStream extends GenericJson {
    @Key
    private LiveStreamCdn cdn;
    @Key
    private String etag;
    @Key
    private String id;
    @Key
    private String kind;
    @Key
    private LiveStreamSnippet snippet;
    @Key
    private LiveStreamStatus status;

    public LiveStream clone() {
        return (LiveStream) super.clone();
    }

    public LiveStreamCdn getCdn() {
        return this.cdn;
    }

    public String getEtag() {
        return this.etag;
    }

    public String getId() {
        return this.id;
    }

    public String getKind() {
        return this.kind;
    }

    public LiveStreamSnippet getSnippet() {
        return this.snippet;
    }

    public LiveStreamStatus getStatus() {
        return this.status;
    }

    public LiveStream set(String str, Object obj) {
        return (LiveStream) super.set(str, obj);
    }

    public LiveStream setCdn(LiveStreamCdn liveStreamCdn) {
        this.cdn = liveStreamCdn;
        return this;
    }

    public LiveStream setEtag(String str) {
        this.etag = str;
        return this;
    }

    public LiveStream setId(String str) {
        this.id = str;
        return this;
    }

    public LiveStream setKind(String str) {
        this.kind = str;
        return this;
    }

    public LiveStream setSnippet(LiveStreamSnippet liveStreamSnippet) {
        this.snippet = liveStreamSnippet;
        return this;
    }

    public LiveStream setStatus(LiveStreamStatus liveStreamStatus) {
        this.status = liveStreamStatus;
        return this;
    }
}
