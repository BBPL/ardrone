package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class ChannelBrandingSettings extends GenericJson {
    @Key
    private ChannelSettings channel;
    @Key
    private List<PropertyValue> hints;
    @Key
    private ImageSettings image;
    @Key
    private WatchSettings watch;

    static {
        Data.nullOf(PropertyValue.class);
    }

    public ChannelBrandingSettings clone() {
        return (ChannelBrandingSettings) super.clone();
    }

    public ChannelSettings getChannel() {
        return this.channel;
    }

    public List<PropertyValue> getHints() {
        return this.hints;
    }

    public ImageSettings getImage() {
        return this.image;
    }

    public WatchSettings getWatch() {
        return this.watch;
    }

    public ChannelBrandingSettings set(String str, Object obj) {
        return (ChannelBrandingSettings) super.set(str, obj);
    }

    public ChannelBrandingSettings setChannel(ChannelSettings channelSettings) {
        this.channel = channelSettings;
        return this;
    }

    public ChannelBrandingSettings setHints(List<PropertyValue> list) {
        this.hints = list;
        return this;
    }

    public ChannelBrandingSettings setImage(ImageSettings imageSettings) {
        this.image = imageSettings;
        return this;
    }

    public ChannelBrandingSettings setWatch(WatchSettings watchSettings) {
        this.watch = watchSettings;
        return this;
    }
}
