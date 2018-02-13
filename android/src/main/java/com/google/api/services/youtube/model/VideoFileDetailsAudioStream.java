package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Key;
import java.math.BigInteger;

public final class VideoFileDetailsAudioStream extends GenericJson {
    @JsonString
    @Key
    private BigInteger bitrateBps;
    @Key
    private Long channelCount;
    @Key
    private String codec;
    @Key
    private String vendor;

    public VideoFileDetailsAudioStream clone() {
        return (VideoFileDetailsAudioStream) super.clone();
    }

    public BigInteger getBitrateBps() {
        return this.bitrateBps;
    }

    public Long getChannelCount() {
        return this.channelCount;
    }

    public String getCodec() {
        return this.codec;
    }

    public String getVendor() {
        return this.vendor;
    }

    public VideoFileDetailsAudioStream set(String str, Object obj) {
        return (VideoFileDetailsAudioStream) super.set(str, obj);
    }

    public VideoFileDetailsAudioStream setBitrateBps(BigInteger bigInteger) {
        this.bitrateBps = bigInteger;
        return this;
    }

    public VideoFileDetailsAudioStream setChannelCount(Long l) {
        this.channelCount = l;
        return this;
    }

    public VideoFileDetailsAudioStream setCodec(String str) {
        this.codec = str;
        return this;
    }

    public VideoFileDetailsAudioStream setVendor(String str) {
        this.vendor = str;
        return this;
    }
}
