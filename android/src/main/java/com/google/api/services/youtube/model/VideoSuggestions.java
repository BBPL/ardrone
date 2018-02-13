package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class VideoSuggestions extends GenericJson {
    @Key
    private List<String> editorSuggestions;
    @Key
    private List<String> processingErrors;
    @Key
    private List<String> processingHints;
    @Key
    private List<String> processingWarnings;
    @Key
    private List<VideoSuggestionsTagSuggestion> tagSuggestions;

    static {
        Data.nullOf(VideoSuggestionsTagSuggestion.class);
    }

    public VideoSuggestions clone() {
        return (VideoSuggestions) super.clone();
    }

    public List<String> getEditorSuggestions() {
        return this.editorSuggestions;
    }

    public List<String> getProcessingErrors() {
        return this.processingErrors;
    }

    public List<String> getProcessingHints() {
        return this.processingHints;
    }

    public List<String> getProcessingWarnings() {
        return this.processingWarnings;
    }

    public List<VideoSuggestionsTagSuggestion> getTagSuggestions() {
        return this.tagSuggestions;
    }

    public VideoSuggestions set(String str, Object obj) {
        return (VideoSuggestions) super.set(str, obj);
    }

    public VideoSuggestions setEditorSuggestions(List<String> list) {
        this.editorSuggestions = list;
        return this;
    }

    public VideoSuggestions setProcessingErrors(List<String> list) {
        this.processingErrors = list;
        return this;
    }

    public VideoSuggestions setProcessingHints(List<String> list) {
        this.processingHints = list;
        return this;
    }

    public VideoSuggestions setProcessingWarnings(List<String> list) {
        this.processingWarnings = list;
        return this;
    }

    public VideoSuggestions setTagSuggestions(List<VideoSuggestionsTagSuggestion> list) {
        this.tagSuggestions = list;
        return this;
    }
}
