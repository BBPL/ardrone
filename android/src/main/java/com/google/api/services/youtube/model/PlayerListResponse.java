package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class PlayerListResponse extends GenericJson {
    @Key
    private String etag;
    @Key
    private String kind;
    @Key
    private List<Player> players;

    public PlayerListResponse clone() {
        return (PlayerListResponse) super.clone();
    }

    public String getEtag() {
        return this.etag;
    }

    public String getKind() {
        return this.kind;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public PlayerListResponse set(String str, Object obj) {
        return (PlayerListResponse) super.set(str, obj);
    }

    public PlayerListResponse setEtag(String str) {
        this.etag = str;
        return this;
    }

    public PlayerListResponse setKind(String str) {
        this.kind = str;
        return this;
    }

    public PlayerListResponse setPlayers(List<Player> list) {
        this.players = list;
        return this;
    }
}
