package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class LocalizedProperty extends GenericJson {
    @Key
    private List<LocalizedString> localized;
    @Key("default")
    private String youtubeDefault;

    public LocalizedProperty clone() {
        return (LocalizedProperty) super.clone();
    }

    public String getDefault() {
        return this.youtubeDefault;
    }

    public List<LocalizedString> getLocalized() {
        return this.localized;
    }

    public LocalizedProperty set(String str, Object obj) {
        return (LocalizedProperty) super.set(str, obj);
    }

    public LocalizedProperty setDefault(String str) {
        this.youtubeDefault = str;
        return this;
    }

    public LocalizedProperty setLocalized(List<LocalizedString> list) {
        this.localized = list;
        return this;
    }
}
