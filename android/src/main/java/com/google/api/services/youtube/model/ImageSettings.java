package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class ImageSettings extends GenericJson {
    @Key
    private LocalizedProperty backgroundImageUrl;
    @Key
    private String bannerImageUrl;
    @Key
    private String bannerMobileImageUrl;
    @Key
    private LocalizedProperty largeBrandedBannerImageImapScript;
    @Key
    private LocalizedProperty largeBrandedBannerImageUrl;
    @Key
    private LocalizedProperty smallBrandedBannerImageImapScript;
    @Key
    private LocalizedProperty smallBrandedBannerImageUrl;
    @Key
    private String trackingImageUrl;
    @Key
    private String watchIconImageUrl;

    public ImageSettings clone() {
        return (ImageSettings) super.clone();
    }

    public LocalizedProperty getBackgroundImageUrl() {
        return this.backgroundImageUrl;
    }

    public String getBannerImageUrl() {
        return this.bannerImageUrl;
    }

    public String getBannerMobileImageUrl() {
        return this.bannerMobileImageUrl;
    }

    public LocalizedProperty getLargeBrandedBannerImageImapScript() {
        return this.largeBrandedBannerImageImapScript;
    }

    public LocalizedProperty getLargeBrandedBannerImageUrl() {
        return this.largeBrandedBannerImageUrl;
    }

    public LocalizedProperty getSmallBrandedBannerImageImapScript() {
        return this.smallBrandedBannerImageImapScript;
    }

    public LocalizedProperty getSmallBrandedBannerImageUrl() {
        return this.smallBrandedBannerImageUrl;
    }

    public String getTrackingImageUrl() {
        return this.trackingImageUrl;
    }

    public String getWatchIconImageUrl() {
        return this.watchIconImageUrl;
    }

    public ImageSettings set(String str, Object obj) {
        return (ImageSettings) super.set(str, obj);
    }

    public ImageSettings setBackgroundImageUrl(LocalizedProperty localizedProperty) {
        this.backgroundImageUrl = localizedProperty;
        return this;
    }

    public ImageSettings setBannerImageUrl(String str) {
        this.bannerImageUrl = str;
        return this;
    }

    public ImageSettings setBannerMobileImageUrl(String str) {
        this.bannerMobileImageUrl = str;
        return this;
    }

    public ImageSettings setLargeBrandedBannerImageImapScript(LocalizedProperty localizedProperty) {
        this.largeBrandedBannerImageImapScript = localizedProperty;
        return this;
    }

    public ImageSettings setLargeBrandedBannerImageUrl(LocalizedProperty localizedProperty) {
        this.largeBrandedBannerImageUrl = localizedProperty;
        return this;
    }

    public ImageSettings setSmallBrandedBannerImageImapScript(LocalizedProperty localizedProperty) {
        this.smallBrandedBannerImageImapScript = localizedProperty;
        return this;
    }

    public ImageSettings setSmallBrandedBannerImageUrl(LocalizedProperty localizedProperty) {
        this.smallBrandedBannerImageUrl = localizedProperty;
        return this;
    }

    public ImageSettings setTrackingImageUrl(String str) {
        this.trackingImageUrl = str;
        return this;
    }

    public ImageSettings setWatchIconImageUrl(String str) {
        this.watchIconImageUrl = str;
        return this;
    }
}
