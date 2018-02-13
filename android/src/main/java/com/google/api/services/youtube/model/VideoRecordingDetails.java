package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

public final class VideoRecordingDetails extends GenericJson {
    @Key
    private GeoPoint location;
    @Key
    private String locationDescription;
    @Key
    private DateTime recordingDate;

    public VideoRecordingDetails clone() {
        return (VideoRecordingDetails) super.clone();
    }

    public GeoPoint getLocation() {
        return this.location;
    }

    public String getLocationDescription() {
        return this.locationDescription;
    }

    public DateTime getRecordingDate() {
        return this.recordingDate;
    }

    public VideoRecordingDetails set(String str, Object obj) {
        return (VideoRecordingDetails) super.set(str, obj);
    }

    public VideoRecordingDetails setLocation(GeoPoint geoPoint) {
        this.location = geoPoint;
        return this;
    }

    public VideoRecordingDetails setLocationDescription(String str) {
        this.locationDescription = str;
        return this;
    }

    public VideoRecordingDetails setRecordingDate(DateTime dateTime) {
        this.recordingDate = dateTime;
        return this;
    }
}
