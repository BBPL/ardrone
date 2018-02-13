package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Key;
import java.math.BigInteger;

public final class FeaturedVideo extends GenericJson {
    @Key
    private Long concurrentViewers;
    @JsonString
    @Key
    private Long endTimeMs;
    @Key
    private String featureId;
    @Key
    private Boolean isLive;
    @JsonString
    @Key
    private BigInteger lengthS;
    @JsonString
    @Key
    private Long startTimeMs;
    @Key
    private String videoId;
    @Key
    private VideoSnippet videoSnippet;
    @JsonString
    @Key
    private BigInteger viewCount;

    public FeaturedVideo clone() {
        return (FeaturedVideo) super.clone();
    }

    public Long getConcurrentViewers() {
        return this.concurrentViewers;
    }

    public Long getEndTimeMs() {
        return this.endTimeMs;
    }

    public String getFeatureId() {
        return this.featureId;
    }

    public Boolean getIsLive() {
        return this.isLive;
    }

    public BigInteger getLengthS() {
        return this.lengthS;
    }

    public Long getStartTimeMs() {
        return this.startTimeMs;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public VideoSnippet getVideoSnippet() {
        return this.videoSnippet;
    }

    public BigInteger getViewCount() {
        return this.viewCount;
    }

    public FeaturedVideo set(String str, Object obj) {
        return (FeaturedVideo) super.set(str, obj);
    }

    public FeaturedVideo setConcurrentViewers(Long l) {
        this.concurrentViewers = l;
        return this;
    }

    public FeaturedVideo setEndTimeMs(Long l) {
        this.endTimeMs = l;
        return this;
    }

    public FeaturedVideo setFeatureId(String str) {
        this.featureId = str;
        return this;
    }

    public FeaturedVideo setIsLive(Boolean bool) {
        this.isLive = bool;
        return this;
    }

    public FeaturedVideo setLengthS(BigInteger bigInteger) {
        this.lengthS = bigInteger;
        return this;
    }

    public FeaturedVideo setStartTimeMs(Long l) {
        this.startTimeMs = l;
        return this;
    }

    public FeaturedVideo setVideoId(String str) {
        this.videoId = str;
        return this;
    }

    public FeaturedVideo setVideoSnippet(VideoSnippet videoSnippet) {
        this.videoSnippet = videoSnippet;
        return this;
    }

    public FeaturedVideo setViewCount(BigInteger bigInteger) {
        this.viewCount = bigInteger;
        return this;
    }
}
