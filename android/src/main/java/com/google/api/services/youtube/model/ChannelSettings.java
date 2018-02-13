package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public final class ChannelSettings extends GenericJson {
    @Key
    private String defaultTab;
    @Key
    private String description;
    @Key
    private String featuredChannelsTitle;
    @Key
    private List<String> featuredChannelsUrls;
    @Key
    private String keywords;
    @Key
    private Boolean moderateComments;
    @Key
    private Boolean showBrowseView;
    @Key
    private Boolean showRelatedChannels;
    @Key
    private String title;
    @Key
    private String trackingAnalyticsAccountId;
    @Key
    private String unsubscribedTrailer;

    public ChannelSettings clone() {
        return (ChannelSettings) super.clone();
    }

    public String getDefaultTab() {
        return this.defaultTab;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFeaturedChannelsTitle() {
        return this.featuredChannelsTitle;
    }

    public List<String> getFeaturedChannelsUrls() {
        return this.featuredChannelsUrls;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public Boolean getModerateComments() {
        return this.moderateComments;
    }

    public Boolean getShowBrowseView() {
        return this.showBrowseView;
    }

    public Boolean getShowRelatedChannels() {
        return this.showRelatedChannels;
    }

    public String getTitle() {
        return this.title;
    }

    public String getTrackingAnalyticsAccountId() {
        return this.trackingAnalyticsAccountId;
    }

    public String getUnsubscribedTrailer() {
        return this.unsubscribedTrailer;
    }

    public ChannelSettings set(String str, Object obj) {
        return (ChannelSettings) super.set(str, obj);
    }

    public ChannelSettings setDefaultTab(String str) {
        this.defaultTab = str;
        return this;
    }

    public ChannelSettings setDescription(String str) {
        this.description = str;
        return this;
    }

    public ChannelSettings setFeaturedChannelsTitle(String str) {
        this.featuredChannelsTitle = str;
        return this;
    }

    public ChannelSettings setFeaturedChannelsUrls(List<String> list) {
        this.featuredChannelsUrls = list;
        return this;
    }

    public ChannelSettings setKeywords(String str) {
        this.keywords = str;
        return this;
    }

    public ChannelSettings setModerateComments(Boolean bool) {
        this.moderateComments = bool;
        return this;
    }

    public ChannelSettings setShowBrowseView(Boolean bool) {
        this.showBrowseView = bool;
        return this;
    }

    public ChannelSettings setShowRelatedChannels(Boolean bool) {
        this.showRelatedChannels = bool;
        return this;
    }

    public ChannelSettings setTitle(String str) {
        this.title = str;
        return this;
    }

    public ChannelSettings setTrackingAnalyticsAccountId(String str) {
        this.trackingAnalyticsAccountId = str;
        return this;
    }

    public ChannelSettings setUnsubscribedTrailer(String str) {
        this.unsubscribedTrailer = str;
        return this;
    }
}
