package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class VideoStatus extends GenericJson {
    @Key
    private Boolean embeddable;
    @Key
    private String failureReason;
    @Key
    private String license;
    @Key
    private String privacyStatus;
    @Key
    private Boolean publicStatsViewable;
    @Key
    private String rejectionReason;
    @Key
    private String uploadStatus;

    public VideoStatus clone() {
        return (VideoStatus) super.clone();
    }

    public Boolean getEmbeddable() {
        return this.embeddable;
    }

    public String getFailureReason() {
        return this.failureReason;
    }

    public String getLicense() {
        return this.license;
    }

    public String getPrivacyStatus() {
        return this.privacyStatus;
    }

    public Boolean getPublicStatsViewable() {
        return this.publicStatsViewable;
    }

    public String getRejectionReason() {
        return this.rejectionReason;
    }

    public String getUploadStatus() {
        return this.uploadStatus;
    }

    public VideoStatus set(String str, Object obj) {
        return (VideoStatus) super.set(str, obj);
    }

    public VideoStatus setEmbeddable(Boolean bool) {
        this.embeddable = bool;
        return this;
    }

    public VideoStatus setFailureReason(String str) {
        this.failureReason = str;
        return this;
    }

    public VideoStatus setLicense(String str) {
        this.license = str;
        return this;
    }

    public VideoStatus setPrivacyStatus(String str) {
        this.privacyStatus = str;
        return this;
    }

    public VideoStatus setPublicStatsViewable(Boolean bool) {
        this.publicStatsViewable = bool;
        return this;
    }

    public VideoStatus setRejectionReason(String str) {
        this.rejectionReason = str;
        return this;
    }

    public VideoStatus setUploadStatus(String str) {
        this.uploadStatus = str;
        return this;
    }
}
