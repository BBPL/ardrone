package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Key;
import java.math.BigInteger;

public final class FeaturedChannel extends GenericJson {
    @Key
    private String channelId;
    @Key
    private ChannelSnippet channelSnippet;
    @JsonString
    @Key
    private Long endTimeMs;
    @Key
    private String featureId;
    @Key
    private String imageUrl;
    @JsonString
    @Key
    private Long startTimeMs;
    @JsonString
    @Key
    private BigInteger subscriberCount;

    public FeaturedChannel clone() {
        return (FeaturedChannel) super.clone();
    }

    public String getChannelId() {
        return this.channelId;
    }

    public ChannelSnippet getChannelSnippet() {
        return this.channelSnippet;
    }

    public Long getEndTimeMs() {
        return this.endTimeMs;
    }

    public String getFeatureId() {
        return this.featureId;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Long getStartTimeMs() {
        return this.startTimeMs;
    }

    public BigInteger getSubscriberCount() {
        return this.subscriberCount;
    }

    public FeaturedChannel set(String str, Object obj) {
        return (FeaturedChannel) super.set(str, obj);
    }

    public FeaturedChannel setChannelId(String str) {
        this.channelId = str;
        return this;
    }

    public FeaturedChannel setChannelSnippet(ChannelSnippet channelSnippet) {
        this.channelSnippet = channelSnippet;
        return this;
    }

    public FeaturedChannel setEndTimeMs(Long l) {
        this.endTimeMs = l;
        return this;
    }

    public FeaturedChannel setFeatureId(String str) {
        this.featureId = str;
        return this;
    }

    public FeaturedChannel setImageUrl(String str) {
        this.imageUrl = str;
        return this;
    }

    public FeaturedChannel setStartTimeMs(Long l) {
        this.startTimeMs = l;
        return this;
    }

    public FeaturedChannel setSubscriberCount(BigInteger bigInteger) {
        this.subscriberCount = bigInteger;
        return this;
    }
}
