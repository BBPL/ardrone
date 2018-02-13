package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class PlayerVideoUrls extends GenericJson {
    @Key
    private PlayerRestrictionDetails restriction;
    @Key
    private List<PlayerVideoUrl> url;

    static {
        Data.nullOf(PlayerVideoUrl.class);
    }

    public PlayerVideoUrls clone() {
        return (PlayerVideoUrls) super.clone();
    }

    public PlayerRestrictionDetails getRestriction() {
        return this.restriction;
    }

    public List<PlayerVideoUrl> getUrl() {
        return this.url;
    }

    public PlayerVideoUrls set(String str, Object obj) {
        return (PlayerVideoUrls) super.set(str, obj);
    }

    public PlayerVideoUrls setRestriction(PlayerRestrictionDetails playerRestrictionDetails) {
        this.restriction = playerRestrictionDetails;
        return this;
    }

    public PlayerVideoUrls setUrl(List<PlayerVideoUrl> list) {
        this.url = list;
        return this;
    }
}
