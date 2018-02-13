package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class VideoTopicDetails extends GenericJson {
    @Key
    private List<String> topicIds;

    public VideoTopicDetails clone() {
        return (VideoTopicDetails) super.clone();
    }

    public List<String> getTopicIds() {
        return this.topicIds;
    }

    public VideoTopicDetails set(String str, Object obj) {
        return (VideoTopicDetails) super.set(str, obj);
    }

    public VideoTopicDetails setTopicIds(List<String> list) {
        this.topicIds = list;
        return this;
    }
}
