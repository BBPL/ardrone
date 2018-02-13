package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class PlaylistContentDetails extends GenericJson {
    @Key
    private Long itemCount;

    public PlaylistContentDetails clone() {
        return (PlaylistContentDetails) super.clone();
    }

    public Long getItemCount() {
        return this.itemCount;
    }

    public PlaylistContentDetails set(String str, Object obj) {
        return (PlaylistContentDetails) super.set(str, obj);
    }

    public PlaylistContentDetails setItemCount(Long l) {
        this.itemCount = l;
        return this;
    }
}
