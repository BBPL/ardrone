package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class LiveBroadcastContentDetails extends GenericJson {
    @Key
    private String boundStreamId;
    @Key
    private Boolean enableArchive;
    @Key
    private Boolean enableContentEncryption;
    @Key
    private Boolean enableDvr;
    @Key
    private Boolean enableEmbed;
    @Key
    private MonitorStream monitorStream;
    @Key
    private Boolean startWithSlateCuepoint;

    public static final class MonitorStream extends GenericJson {
        @Key
        private Long broadcastStreamDelayMs;
        @Key
        private String embedHtml;
        @Key
        private Boolean enableMonitorStream;

        public MonitorStream clone() {
            return (MonitorStream) super.clone();
        }

        public Long getBroadcastStreamDelayMs() {
            return this.broadcastStreamDelayMs;
        }

        public String getEmbedHtml() {
            return this.embedHtml;
        }

        public Boolean getEnableMonitorStream() {
            return this.enableMonitorStream;
        }

        public MonitorStream set(String str, Object obj) {
            return (MonitorStream) super.set(str, obj);
        }

        public MonitorStream setBroadcastStreamDelayMs(Long l) {
            this.broadcastStreamDelayMs = l;
            return this;
        }

        public MonitorStream setEmbedHtml(String str) {
            this.embedHtml = str;
            return this;
        }

        public MonitorStream setEnableMonitorStream(Boolean bool) {
            this.enableMonitorStream = bool;
            return this;
        }
    }

    public LiveBroadcastContentDetails clone() {
        return (LiveBroadcastContentDetails) super.clone();
    }

    public String getBoundStreamId() {
        return this.boundStreamId;
    }

    public Boolean getEnableArchive() {
        return this.enableArchive;
    }

    public Boolean getEnableContentEncryption() {
        return this.enableContentEncryption;
    }

    public Boolean getEnableDvr() {
        return this.enableDvr;
    }

    public Boolean getEnableEmbed() {
        return this.enableEmbed;
    }

    public MonitorStream getMonitorStream() {
        return this.monitorStream;
    }

    public Boolean getStartWithSlateCuepoint() {
        return this.startWithSlateCuepoint;
    }

    public LiveBroadcastContentDetails set(String str, Object obj) {
        return (LiveBroadcastContentDetails) super.set(str, obj);
    }

    public LiveBroadcastContentDetails setBoundStreamId(String str) {
        this.boundStreamId = str;
        return this;
    }

    public LiveBroadcastContentDetails setEnableArchive(Boolean bool) {
        this.enableArchive = bool;
        return this;
    }

    public LiveBroadcastContentDetails setEnableContentEncryption(Boolean bool) {
        this.enableContentEncryption = bool;
        return this;
    }

    public LiveBroadcastContentDetails setEnableDvr(Boolean bool) {
        this.enableDvr = bool;
        return this;
    }

    public LiveBroadcastContentDetails setEnableEmbed(Boolean bool) {
        this.enableEmbed = bool;
        return this;
    }

    public LiveBroadcastContentDetails setMonitorStream(MonitorStream monitorStream) {
        this.monitorStream = monitorStream;
        return this;
    }

    public LiveBroadcastContentDetails setStartWithSlateCuepoint(Boolean bool) {
        this.startWithSlateCuepoint = bool;
        return this;
    }
}
