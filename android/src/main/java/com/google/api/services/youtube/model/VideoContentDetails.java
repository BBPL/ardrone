package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class VideoContentDetails extends GenericJson {
    @Key
    private String caption;
    @Key
    private AccessPolicy countryRestriction;
    @Key
    private String definition;
    @Key
    private String dimension;
    @Key
    private String duration;
    @Key
    private Boolean licensedContent;
    @Key
    private VideoContentDetailsRegionRestriction regionRestriction;

    public VideoContentDetails clone() {
        return (VideoContentDetails) super.clone();
    }

    public String getCaption() {
        return this.caption;
    }

    public AccessPolicy getCountryRestriction() {
        return this.countryRestriction;
    }

    public String getDefinition() {
        return this.definition;
    }

    public String getDimension() {
        return this.dimension;
    }

    public String getDuration() {
        return this.duration;
    }

    public Boolean getLicensedContent() {
        return this.licensedContent;
    }

    public VideoContentDetailsRegionRestriction getRegionRestriction() {
        return this.regionRestriction;
    }

    public VideoContentDetails set(String str, Object obj) {
        return (VideoContentDetails) super.set(str, obj);
    }

    public VideoContentDetails setCaption(String str) {
        this.caption = str;
        return this;
    }

    public VideoContentDetails setCountryRestriction(AccessPolicy accessPolicy) {
        this.countryRestriction = accessPolicy;
        return this;
    }

    public VideoContentDetails setDefinition(String str) {
        this.definition = str;
        return this;
    }

    public VideoContentDetails setDimension(String str) {
        this.dimension = str;
        return this;
    }

    public VideoContentDetails setDuration(String str) {
        this.duration = str;
        return this;
    }

    public VideoContentDetails setLicensedContent(Boolean bool) {
        this.licensedContent = bool;
        return this;
    }

    public VideoContentDetails setRegionRestriction(VideoContentDetailsRegionRestriction videoContentDetailsRegionRestriction) {
        this.regionRestriction = videoContentDetailsRegionRestriction;
        return this;
    }
}
