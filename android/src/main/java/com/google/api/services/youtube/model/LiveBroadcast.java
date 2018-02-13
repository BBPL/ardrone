package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class LiveBroadcast extends GenericJson {
    @Key
    private LiveBroadcastContentDetails contentDetails;
    @Key
    private String etag;
    @Key
    private String id;
    @Key
    private String kind;
    @Key
    private LiveBroadcastSlateSettings slateSettings;
    @Key
    private LiveBroadcastSnippet snippet;
    @Key
    private LiveBroadcastStatus status;

    public LiveBroadcast clone() {
        return (LiveBroadcast) super.clone();
    }

    public LiveBroadcastContentDetails getContentDetails() {
        return this.contentDetails;
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

    public LiveBroadcastSlateSettings getSlateSettings() {
        return this.slateSettings;
    }

    public LiveBroadcastSnippet getSnippet() {
        return this.snippet;
    }

    public LiveBroadcastStatus getStatus() {
        return this.status;
    }

    public LiveBroadcast set(String str, Object obj) {
        return (LiveBroadcast) super.set(str, obj);
    }

    public LiveBroadcast setContentDetails(LiveBroadcastContentDetails liveBroadcastContentDetails) {
        this.contentDetails = liveBroadcastContentDetails;
        return this;
    }

    public LiveBroadcast setEtag(String str) {
        this.etag = str;
        return this;
    }

    public LiveBroadcast setId(String str) {
        this.id = str;
        return this;
    }

    public LiveBroadcast setKind(String str) {
        this.kind = str;
        return this;
    }

    public LiveBroadcast setSlateSettings(LiveBroadcastSlateSettings liveBroadcastSlateSettings) {
        this.slateSettings = liveBroadcastSlateSettings;
        return this;
    }

    public LiveBroadcast setSnippet(LiveBroadcastSnippet liveBroadcastSnippet) {
        this.snippet = liveBroadcastSnippet;
        return this;
    }

    public LiveBroadcast setStatus(LiveBroadcastStatus liveBroadcastStatus) {
        this.status = liveBroadcastStatus;
        return this;
    }
}
