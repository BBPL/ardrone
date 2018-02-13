package com.google.api.services.picasa.model;

import com.google.api.client.util.Key;

public class MediaContent {
    @Key("@height")
    private int height;
    @Key("@medium")
    private String medium;
    @Key("@type")
    private String type;
    @Key("@url")
    private String url;
    @Key("@width")
    private int width;

    public int getHeight() {
        return this.height;
    }

    public String getMedium() {
        return this.medium;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public void setMedium(String str) {
        this.medium = str;
    }

    public void setType(String str) {
        this.type = str;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public void setWidth(int i) {
        this.width = i;
    }
}
