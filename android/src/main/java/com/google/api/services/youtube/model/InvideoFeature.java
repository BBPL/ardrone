package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class InvideoFeature extends GenericJson {
    @Key
    private FeaturedChannel featuredChannel;
    @Key
    private FeaturedVideo featuredVideo;

    public InvideoFeature clone() {
        return (InvideoFeature) super.clone();
    }

    public FeaturedChannel getFeaturedChannel() {
        return this.featuredChannel;
    }

    public FeaturedVideo getFeaturedVideo() {
        return this.featuredVideo;
    }

    public InvideoFeature set(String str, Object obj) {
        return (InvideoFeature) super.set(str, obj);
    }

    public InvideoFeature setFeaturedChannel(FeaturedChannel featuredChannel) {
        this.featuredChannel = featuredChannel;
        return this;
    }

    public InvideoFeature setFeaturedVideo(FeaturedVideo featuredVideo) {
        this.featuredVideo = featuredVideo;
        return this;
    }
}
