package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class VideoSuggestionsTagSuggestion extends GenericJson {
    @Key
    private List<String> categoryRestricts;
    @Key
    private String tag;

    public VideoSuggestionsTagSuggestion clone() {
        return (VideoSuggestionsTagSuggestion) super.clone();
    }

    public List<String> getCategoryRestricts() {
        return this.categoryRestricts;
    }

    public String getTag() {
        return this.tag;
    }

    public VideoSuggestionsTagSuggestion set(String str, Object obj) {
        return (VideoSuggestionsTagSuggestion) super.set(str, obj);
    }

    public VideoSuggestionsTagSuggestion setCategoryRestricts(List<String> list) {
        this.categoryRestricts = list;
        return this;
    }

    public VideoSuggestionsTagSuggestion setTag(String str) {
        this.tag = str;
        return this;
    }
}
