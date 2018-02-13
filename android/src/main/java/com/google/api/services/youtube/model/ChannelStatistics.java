package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Key;
import java.math.BigInteger;

public final class ChannelStatistics extends GenericJson {
    @JsonString
    @Key
    private BigInteger commentCount;
    @JsonString
    @Key
    private BigInteger subscriberCount;
    @JsonString
    @Key
    private BigInteger videoCount;
    @JsonString
    @Key
    private BigInteger viewCount;

    public ChannelStatistics clone() {
        return (ChannelStatistics) super.clone();
    }

    public BigInteger getCommentCount() {
        return this.commentCount;
    }

    public BigInteger getSubscriberCount() {
        return this.subscriberCount;
    }

    public BigInteger getVideoCount() {
        return this.videoCount;
    }

    public BigInteger getViewCount() {
        return this.viewCount;
    }

    public ChannelStatistics set(String str, Object obj) {
        return (ChannelStatistics) super.set(str, obj);
    }

    public ChannelStatistics setCommentCount(BigInteger bigInteger) {
        this.commentCount = bigInteger;
        return this;
    }

    public ChannelStatistics setSubscriberCount(BigInteger bigInteger) {
        this.subscriberCount = bigInteger;
        return this;
    }

    public ChannelStatistics setVideoCount(BigInteger bigInteger) {
        this.videoCount = bigInteger;
        return this;
    }

    public ChannelStatistics setViewCount(BigInteger bigInteger) {
        this.viewCount = bigInteger;
        return this;
    }
}
