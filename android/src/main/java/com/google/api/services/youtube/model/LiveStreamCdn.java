package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class LiveStreamCdn extends GenericJson {
    @Key
    private String format;
    @Key
    private LiveStreamCdnIngestionInfo ingestionInfo;
    @Key
    private String ingestionType;

    public LiveStreamCdn clone() {
        return (LiveStreamCdn) super.clone();
    }

    public String getFormat() {
        return this.format;
    }

    public LiveStreamCdnIngestionInfo getIngestionInfo() {
        return this.ingestionInfo;
    }

    public String getIngestionType() {
        return this.ingestionType;
    }

    public LiveStreamCdn set(String str, Object obj) {
        return (LiveStreamCdn) super.set(str, obj);
    }

    public LiveStreamCdn setFormat(String str) {
        this.format = str;
        return this;
    }

    public LiveStreamCdn setIngestionInfo(LiveStreamCdnIngestionInfo liveStreamCdnIngestionInfo) {
        this.ingestionInfo = liveStreamCdnIngestionInfo;
        return this;
    }

    public LiveStreamCdn setIngestionType(String str) {
        this.ingestionType = str;
        return this;
    }
}
