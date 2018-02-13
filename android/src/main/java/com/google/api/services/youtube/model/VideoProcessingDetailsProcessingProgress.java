package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Key;
import java.math.BigInteger;

public final class VideoProcessingDetailsProcessingProgress extends GenericJson {
    @JsonString
    @Key
    private BigInteger partsProcessed;
    @JsonString
    @Key
    private BigInteger partsTotal;
    @JsonString
    @Key
    private BigInteger timeLeftMs;

    public VideoProcessingDetailsProcessingProgress clone() {
        return (VideoProcessingDetailsProcessingProgress) super.clone();
    }

    public BigInteger getPartsProcessed() {
        return this.partsProcessed;
    }

    public BigInteger getPartsTotal() {
        return this.partsTotal;
    }

    public BigInteger getTimeLeftMs() {
        return this.timeLeftMs;
    }

    public VideoProcessingDetailsProcessingProgress set(String str, Object obj) {
        return (VideoProcessingDetailsProcessingProgress) super.set(str, obj);
    }

    public VideoProcessingDetailsProcessingProgress setPartsProcessed(BigInteger bigInteger) {
        this.partsProcessed = bigInteger;
        return this;
    }

    public VideoProcessingDetailsProcessingProgress setPartsTotal(BigInteger bigInteger) {
        this.partsTotal = bigInteger;
        return this;
    }

    public VideoProcessingDetailsProcessingProgress setTimeLeftMs(BigInteger bigInteger) {
        this.timeLeftMs = bigInteger;
        return this;
    }
}
