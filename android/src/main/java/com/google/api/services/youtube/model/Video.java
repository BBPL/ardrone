package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class Video extends GenericJson {
    @Key
    private VideoAgeGating ageGatingDetails;
    @Key
    private VideoContentDetails contentDetails;
    @Key
    private String etag;
    @Key
    private VideoFileDetails fileDetails;
    @Key
    private String id;
    @Key
    private String kind;
    @Key
    private VideoMonetizationDetails monetizationDetails;
    @Key
    private VideoPlayer player;
    @Key
    private VideoProcessingDetails processingDetails;
    @Key
    private VideoProjectDetails projectDetails;
    @Key
    private VideoRecordingDetails recordingDetails;
    @Key
    private VideoSnippet snippet;
    @Key
    private VideoStatistics statistics;
    @Key
    private VideoStatus status;
    @Key
    private VideoSuggestions suggestions;
    @Key
    private VideoTopicDetails topicDetails;

    public Video clone() {
        return (Video) super.clone();
    }

    public VideoAgeGating getAgeGatingDetails() {
        return this.ageGatingDetails;
    }

    public VideoContentDetails getContentDetails() {
        return this.contentDetails;
    }

    public String getEtag() {
        return this.etag;
    }

    public VideoFileDetails getFileDetails() {
        return this.fileDetails;
    }

    public String getId() {
        return this.id;
    }

    public String getKind() {
        return this.kind;
    }

    public VideoMonetizationDetails getMonetizationDetails() {
        return this.monetizationDetails;
    }

    public VideoPlayer getPlayer() {
        return this.player;
    }

    public VideoProcessingDetails getProcessingDetails() {
        return this.processingDetails;
    }

    public VideoProjectDetails getProjectDetails() {
        return this.projectDetails;
    }

    public VideoRecordingDetails getRecordingDetails() {
        return this.recordingDetails;
    }

    public VideoSnippet getSnippet() {
        return this.snippet;
    }

    public VideoStatistics getStatistics() {
        return this.statistics;
    }

    public VideoStatus getStatus() {
        return this.status;
    }

    public VideoSuggestions getSuggestions() {
        return this.suggestions;
    }

    public VideoTopicDetails getTopicDetails() {
        return this.topicDetails;
    }

    public Video set(String str, Object obj) {
        return (Video) super.set(str, obj);
    }

    public Video setAgeGatingDetails(VideoAgeGating videoAgeGating) {
        this.ageGatingDetails = videoAgeGating;
        return this;
    }

    public Video setContentDetails(VideoContentDetails videoContentDetails) {
        this.contentDetails = videoContentDetails;
        return this;
    }

    public Video setEtag(String str) {
        this.etag = str;
        return this;
    }

    public Video setFileDetails(VideoFileDetails videoFileDetails) {
        this.fileDetails = videoFileDetails;
        return this;
    }

    public Video setId(String str) {
        this.id = str;
        return this;
    }

    public Video setKind(String str) {
        this.kind = str;
        return this;
    }

    public Video setMonetizationDetails(VideoMonetizationDetails videoMonetizationDetails) {
        this.monetizationDetails = videoMonetizationDetails;
        return this;
    }

    public Video setPlayer(VideoPlayer videoPlayer) {
        this.player = videoPlayer;
        return this;
    }

    public Video setProcessingDetails(VideoProcessingDetails videoProcessingDetails) {
        this.processingDetails = videoProcessingDetails;
        return this;
    }

    public Video setProjectDetails(VideoProjectDetails videoProjectDetails) {
        this.projectDetails = videoProjectDetails;
        return this;
    }

    public Video setRecordingDetails(VideoRecordingDetails videoRecordingDetails) {
        this.recordingDetails = videoRecordingDetails;
        return this;
    }

    public Video setSnippet(VideoSnippet videoSnippet) {
        this.snippet = videoSnippet;
        return this;
    }

    public Video setStatistics(VideoStatistics videoStatistics) {
        this.statistics = videoStatistics;
        return this;
    }

    public Video setStatus(VideoStatus videoStatus) {
        this.status = videoStatus;
        return this;
    }

    public Video setSuggestions(VideoSuggestions videoSuggestions) {
        this.suggestions = videoSuggestions;
        return this;
    }

    public Video setTopicDetails(VideoTopicDetails videoTopicDetails) {
        this.topicDetails = videoTopicDetails;
        return this;
    }
}
