package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class Channel extends GenericJson {
    @Key
    private ChannelBrandingSettings brandingSettings;
    @Key
    private ChannelContentDetails contentDetails;
    @Key
    private String etag;
    @Key
    private String id;
    @Key
    private String kind;
    @Key
    private ChannelSnippet snippet;
    @Key
    private ChannelStatistics statistics;
    @Key
    private ChannelStatus status;
    @Key
    private ChannelTopicDetails topicDetails;

    public Channel clone() {
        return (Channel) super.clone();
    }

    public ChannelBrandingSettings getBrandingSettings() {
        return this.brandingSettings;
    }

    public ChannelContentDetails getContentDetails() {
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

    public ChannelSnippet getSnippet() {
        return this.snippet;
    }

    public ChannelStatistics getStatistics() {
        return this.statistics;
    }

    public ChannelStatus getStatus() {
        return this.status;
    }

    public ChannelTopicDetails getTopicDetails() {
        return this.topicDetails;
    }

    public Channel set(String str, Object obj) {
        return (Channel) super.set(str, obj);
    }

    public Channel setBrandingSettings(ChannelBrandingSettings channelBrandingSettings) {
        this.brandingSettings = channelBrandingSettings;
        return this;
    }

    public Channel setContentDetails(ChannelContentDetails channelContentDetails) {
        this.contentDetails = channelContentDetails;
        return this;
    }

    public Channel setEtag(String str) {
        this.etag = str;
        return this;
    }

    public Channel setId(String str) {
        this.id = str;
        return this;
    }

    public Channel setKind(String str) {
        this.kind = str;
        return this;
    }

    public Channel setSnippet(ChannelSnippet channelSnippet) {
        this.snippet = channelSnippet;
        return this;
    }

    public Channel setStatistics(ChannelStatistics channelStatistics) {
        this.statistics = channelStatistics;
        return this;
    }

    public Channel setStatus(ChannelStatus channelStatus) {
        this.status = channelStatus;
        return this;
    }

    public Channel setTopicDetails(ChannelTopicDetails channelTopicDetails) {
        this.topicDetails = channelTopicDetails;
        return this;
    }
}
