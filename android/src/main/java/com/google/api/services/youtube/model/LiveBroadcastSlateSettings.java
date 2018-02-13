package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;
import java.util.Map;

public final class LiveBroadcastSlateSettings extends GenericJson {
    @Key
    private Boolean enableSlates;
    @Key
    private Map<String, Slates> slates;

    public static final class Slates extends GenericJson {
        @Key
        private String backgroundUrl;
        @Key
        private List<String> textLines;

        public Slates clone() {
            return (Slates) super.clone();
        }

        public String getBackgroundUrl() {
            return this.backgroundUrl;
        }

        public List<String> getTextLines() {
            return this.textLines;
        }

        public Slates set(String str, Object obj) {
            return (Slates) super.set(str, obj);
        }

        public Slates setBackgroundUrl(String str) {
            this.backgroundUrl = str;
            return this;
        }

        public Slates setTextLines(List<String> list) {
            this.textLines = list;
            return this;
        }
    }

    static {
        Data.nullOf(Slates.class);
    }

    public LiveBroadcastSlateSettings clone() {
        return (LiveBroadcastSlateSettings) super.clone();
    }

    public Boolean getEnableSlates() {
        return this.enableSlates;
    }

    public Map<String, Slates> getSlates() {
        return this.slates;
    }

    public LiveBroadcastSlateSettings set(String str, Object obj) {
        return (LiveBroadcastSlateSettings) super.set(str, obj);
    }

    public LiveBroadcastSlateSettings setEnableSlates(Boolean bool) {
        this.enableSlates = bool;
        return this;
    }

    public LiveBroadcastSlateSettings setSlates(Map<String, Slates> map) {
        this.slates = map;
        return this;
    }
}
