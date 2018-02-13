package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.math.BigInteger;
import java.util.List;

public final class VideoFileDetails extends GenericJson {
    @Key
    private List<VideoFileDetailsAudioStream> audioStreams;
    @JsonString
    @Key
    private BigInteger bitrateBps;
    @Key
    private String container;
    @Key
    private String creationTime;
    @JsonString
    @Key
    private BigInteger durationMs;
    @Key
    private String fileName;
    @JsonString
    @Key
    private BigInteger fileSize;
    @Key
    private String fileType;
    @Key
    private GeoPoint recordingLocation;
    @Key
    private List<VideoFileDetailsVideoStream> videoStreams;

    static {
        Data.nullOf(VideoFileDetailsAudioStream.class);
    }

    public VideoFileDetails clone() {
        return (VideoFileDetails) super.clone();
    }

    public List<VideoFileDetailsAudioStream> getAudioStreams() {
        return this.audioStreams;
    }

    public BigInteger getBitrateBps() {
        return this.bitrateBps;
    }

    public String getContainer() {
        return this.container;
    }

    public String getCreationTime() {
        return this.creationTime;
    }

    public BigInteger getDurationMs() {
        return this.durationMs;
    }

    public String getFileName() {
        return this.fileName;
    }

    public BigInteger getFileSize() {
        return this.fileSize;
    }

    public String getFileType() {
        return this.fileType;
    }

    public GeoPoint getRecordingLocation() {
        return this.recordingLocation;
    }

    public List<VideoFileDetailsVideoStream> getVideoStreams() {
        return this.videoStreams;
    }

    public VideoFileDetails set(String str, Object obj) {
        return (VideoFileDetails) super.set(str, obj);
    }

    public VideoFileDetails setAudioStreams(List<VideoFileDetailsAudioStream> list) {
        this.audioStreams = list;
        return this;
    }

    public VideoFileDetails setBitrateBps(BigInteger bigInteger) {
        this.bitrateBps = bigInteger;
        return this;
    }

    public VideoFileDetails setContainer(String str) {
        this.container = str;
        return this;
    }

    public VideoFileDetails setCreationTime(String str) {
        this.creationTime = str;
        return this;
    }

    public VideoFileDetails setDurationMs(BigInteger bigInteger) {
        this.durationMs = bigInteger;
        return this;
    }

    public VideoFileDetails setFileName(String str) {
        this.fileName = str;
        return this;
    }

    public VideoFileDetails setFileSize(BigInteger bigInteger) {
        this.fileSize = bigInteger;
        return this;
    }

    public VideoFileDetails setFileType(String str) {
        this.fileType = str;
        return this;
    }

    public VideoFileDetails setRecordingLocation(GeoPoint geoPoint) {
        this.recordingLocation = geoPoint;
        return this;
    }

    public VideoFileDetails setVideoStreams(List<VideoFileDetailsVideoStream> list) {
        this.videoStreams = list;
        return this;
    }
}
