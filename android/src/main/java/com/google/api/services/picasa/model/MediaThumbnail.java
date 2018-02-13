package com.google.api.services.picasa.model;

import com.google.api.client.util.Key;

public class MediaThumbnail {
    @Key("@height")
    private int height;
    @Key("@url")
    private String url;
    @Key("@width")
    private int width;

    public int getHeight() {
        return this.height;
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

    public void setUrl(String str) {
        this.url = str;
    }

    public void setWidth(int i) {
        this.width = i;
    }
}
