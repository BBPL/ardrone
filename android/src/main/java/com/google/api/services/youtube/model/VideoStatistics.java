package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Key;
import java.math.BigInteger;

public final class VideoStatistics extends GenericJson {
    @JsonString
    @Key
    private BigInteger commentCount;
    @JsonString
    @Key
    private BigInteger dislikeCount;
    @JsonString
    @Key
    private BigInteger favoriteCount;
    @JsonString
    @Key
    private BigInteger likeCount;
    @JsonString
    @Key
    private BigInteger viewCount;

    public VideoStatistics clone() {
        return (VideoStatistics) super.clone();
    }

    public BigInteger getCommentCount() {
        return this.commentCount;
    }

    public BigInteger getDislikeCount() {
        return this.dislikeCount;
    }

    public BigInteger getFavoriteCount() {
        return this.favoriteCount;
    }

    public BigInteger getLikeCount() {
        return this.likeCount;
    }

    public BigInteger getViewCount() {
        return this.viewCount;
    }

    public VideoStatistics set(String str, Object obj) {
        return (VideoStatistics) super.set(str, obj);
    }

    public VideoStatistics setCommentCount(BigInteger bigInteger) {
        this.commentCount = bigInteger;
        return this;
    }

    public VideoStatistics setDislikeCount(BigInteger bigInteger) {
        this.dislikeCount = bigInteger;
        return this;
    }

    public VideoStatistics setFavoriteCount(BigInteger bigInteger) {
        this.favoriteCount = bigInteger;
        return this;
    }

    public VideoStatistics setLikeCount(BigInteger bigInteger) {
        this.likeCount = bigInteger;
        return this;
    }

    public VideoStatistics setViewCount(BigInteger bigInteger) {
        this.viewCount = bigInteger;
        return this;
    }
}
