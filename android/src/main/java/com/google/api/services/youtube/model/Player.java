package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class Player extends GenericJson {
    @Key
    private PlayerAdsPlaylist adsPlaylist;
    @Key
    private String etag;
    @Key
    private ResourceId id;
    @Key
    private InvideoFeature invideoFeature;
    @Key
    private String kind;
    @Key
    private PlayerVideoUrls videoUrls;

    public Player clone() {
        return (Player) super.clone();
    }

    public PlayerAdsPlaylist getAdsPlaylist() {
        return this.adsPlaylist;
    }

    public String getEtag() {
        return this.etag;
    }

    public ResourceId getId() {
        return this.id;
    }

    public InvideoFeature getInvideoFeature() {
        return this.invideoFeature;
    }

    public String getKind() {
        return this.kind;
    }

    public PlayerVideoUrls getVideoUrls() {
        return this.videoUrls;
    }

    public Player set(String str, Object obj) {
        return (Player) super.set(str, obj);
    }

    public Player setAdsPlaylist(PlayerAdsPlaylist playerAdsPlaylist) {
        this.adsPlaylist = playerAdsPlaylist;
        return this;
    }

    public Player setEtag(String str) {
        this.etag = str;
        return this;
    }

    public Player setId(ResourceId resourceId) {
        this.id = resourceId;
        return this;
    }

    public Player setInvideoFeature(InvideoFeature invideoFeature) {
        this.invideoFeature = invideoFeature;
        return this;
    }

    public Player setKind(String str) {
        this.kind = str;
        return this;
    }

    public Player setVideoUrls(PlayerVideoUrls playerVideoUrls) {
        this.videoUrls = playerVideoUrls;
        return this;
    }
}
