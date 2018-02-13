package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class PlayerAdsPlaylist extends GenericJson {
    @Key("vmap_xml")
    private String vmapXml;

    public PlayerAdsPlaylist clone() {
        return (PlayerAdsPlaylist) super.clone();
    }

    public String getVmapXml() {
        return this.vmapXml;
    }

    public PlayerAdsPlaylist set(String str, Object obj) {
        return (PlayerAdsPlaylist) super.set(str, obj);
    }

    public PlayerAdsPlaylist setVmapXml(String str) {
        this.vmapXml = str;
        return this;
    }
}
