package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class LiveStreamCdnIngestionInfo extends GenericJson {
    @Key
    private String backupIngestionAddress;
    @Key
    private String ingestionAddress;
    @Key
    private String streamName;

    public LiveStreamCdnIngestionInfo clone() {
        return (LiveStreamCdnIngestionInfo) super.clone();
    }

    public String getBackupIngestionAddress() {
        return this.backupIngestionAddress;
    }

    public String getIngestionAddress() {
        return this.ingestionAddress;
    }

    public String getStreamName() {
        return this.streamName;
    }

    public LiveStreamCdnIngestionInfo set(String str, Object obj) {
        return (LiveStreamCdnIngestionInfo) super.set(str, obj);
    }

    public LiveStreamCdnIngestionInfo setBackupIngestionAddress(String str) {
        this.backupIngestionAddress = str;
        return this;
    }

    public LiveStreamCdnIngestionInfo setIngestionAddress(String str) {
        this.ingestionAddress = str;
        return this;
    }

    public LiveStreamCdnIngestionInfo setStreamName(String str) {
        this.streamName = str;
        return this;
    }
}
