package com.google.api.services.picasa.model;

import com.google.api.client.util.Key;
import java.util.List;

public class MediaGroup {
    @Key("media:credit")
    private String credit;
    @Key("media:keywords")
    private String keywords;
    @Key("media:content")
    private MediaContent mediaContent;
    @Key("media:thumbnail")
    private List<MediaThumbnail> thumbnails;

    public String getCredit() {
        return this.credit;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public MediaContent getMediaContent() {
        return this.mediaContent;
    }

    public List<MediaThumbnail> getThumbnails() {
        return this.thumbnails;
    }

    public void setCredit(String str) {
        this.credit = str;
    }

    public void setKeywords(String str) {
        this.keywords = str;
    }

    public void setKeywords(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            stringBuilder.append((String) list.get(i));
            if (i < size - 1) {
                stringBuilder.append(",");
            }
        }
        this.keywords = stringBuilder.toString();
    }

    public void setKeywords(String[] strArr) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(strArr[i]);
            if (i < length - 1) {
                stringBuilder.append(",");
            }
        }
        this.keywords = stringBuilder.toString();
    }

    public void setMediaContent(MediaContent mediaContent) {
        this.mediaContent = mediaContent;
    }

    public void setThumbnails(List<MediaThumbnail> list) {
        this.thumbnails = list;
    }
}
