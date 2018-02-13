package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class VideoProjectDetails extends GenericJson {
    @Key
    private List<String> tags;

    public VideoProjectDetails clone() {
        return (VideoProjectDetails) super.clone();
    }

    public List<String> getTags() {
        return this.tags;
    }

    public VideoProjectDetails set(String str, Object obj) {
        return (VideoProjectDetails) super.set(str, obj);
    }

    public VideoProjectDetails setTags(List<String> list) {
        this.tags = list;
        return this;
    }
}
