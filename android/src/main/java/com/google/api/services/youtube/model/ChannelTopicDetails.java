package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class ChannelTopicDetails extends GenericJson {
    @Key
    private List<String> topicIds;

    public ChannelTopicDetails clone() {
        return (ChannelTopicDetails) super.clone();
    }

    public List<String> getTopicIds() {
        return this.topicIds;
    }

    public ChannelTopicDetails set(String str, Object obj) {
        return (ChannelTopicDetails) super.set(str, obj);
    }

    public ChannelTopicDetails setTopicIds(List<String> list) {
        this.topicIds = list;
        return this;
    }
}
