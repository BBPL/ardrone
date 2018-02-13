package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class GeoPoint extends GenericJson {
    @Key
    private Double elevation;
    @Key
    private Double latitude;
    @Key
    private Double longitude;

    public GeoPoint clone() {
        return (GeoPoint) super.clone();
    }

    public Double getElevation() {
        return this.elevation;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public GeoPoint set(String str, Object obj) {
        return (GeoPoint) super.set(str, obj);
    }

    public GeoPoint setElevation(Double d) {
        this.elevation = d;
        return this;
    }

    public GeoPoint setLatitude(Double d) {
        this.latitude = d;
        return this;
    }

    public GeoPoint setLongitude(Double d) {
        this.longitude = d;
        return this;
    }
}
