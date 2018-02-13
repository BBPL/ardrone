package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class ActivityContentDetails extends GenericJson {
    @Key
    private Bulletin bulletin;
    @Key
    private ChannelItem channelItem;
    @Key
    private Comment comment;
    @Key
    private Favorite favorite;
    @Key
    private Like like;
    @Key
    private PlaylistItem playlistItem;
    @Key
    private Recommendation recommendation;
    @Key
    private Social social;
    @Key
    private Subscription subscription;
    @Key
    private Upload upload;

    public static final class Bulletin extends GenericJson {
        @Key
        private ResourceId resourceId;

        public Bulletin clone() {
            return (Bulletin) super.clone();
        }

        public ResourceId getResourceId() {
            return this.resourceId;
        }

        public Bulletin set(String str, Object obj) {
            return (Bulletin) super.set(str, obj);
        }

        public Bulletin setResourceId(ResourceId resourceId) {
            this.resourceId = resourceId;
            return this;
        }
    }

    public static final class ChannelItem extends GenericJson {
        @Key
        private ResourceId resourceId;

        public ChannelItem clone() {
            return (ChannelItem) super.clone();
        }

        public ResourceId getResourceId() {
            return this.resourceId;
        }

        public ChannelItem set(String str, Object obj) {
            return (ChannelItem) super.set(str, obj);
        }

        public ChannelItem setResourceId(ResourceId resourceId) {
            this.resourceId = resourceId;
            return this;
        }
    }

    public static final class Comment extends GenericJson {
        @Key
        private ResourceId resourceId;

        public Comment clone() {
            return (Comment) super.clone();
        }

        public ResourceId getResourceId() {
            return this.resourceId;
        }

        public Comment set(String str, Object obj) {
            return (Comment) super.set(str, obj);
        }

        public Comment setResourceId(ResourceId resourceId) {
            this.resourceId = resourceId;
            return this;
        }
    }

    public static final class Favorite extends GenericJson {
        @Key
        private ResourceId resourceId;

        public Favorite clone() {
            return (Favorite) super.clone();
        }

        public ResourceId getResourceId() {
            return this.resourceId;
        }

        public Favorite set(String str, Object obj) {
            return (Favorite) super.set(str, obj);
        }

        public Favorite setResourceId(ResourceId resourceId) {
            this.resourceId = resourceId;
            return this;
        }
    }

    public static final class Like extends GenericJson {
        @Key
        private ResourceId resourceId;

        public Like clone() {
            return (Like) super.clone();
        }

        public ResourceId getResourceId() {
            return this.resourceId;
        }

        public Like set(String str, Object obj) {
            return (Like) super.set(str, obj);
        }

        public Like setResourceId(ResourceId resourceId) {
            this.resourceId = resourceId;
            return this;
        }
    }

    public static final class PlaylistItem extends GenericJson {
        @Key
        private String playlistId;
        @Key
        private String playlistItemId;
        @Key
        private ResourceId resourceId;

        public PlaylistItem clone() {
            return (PlaylistItem) super.clone();
        }

        public String getPlaylistId() {
            return this.playlistId;
        }

        public String getPlaylistItemId() {
            return this.playlistItemId;
        }

        public ResourceId getResourceId() {
            return this.resourceId;
        }

        public PlaylistItem set(String str, Object obj) {
            return (PlaylistItem) super.set(str, obj);
        }

        public PlaylistItem setPlaylistId(String str) {
            this.playlistId = str;
            return this;
        }

        public PlaylistItem setPlaylistItemId(String str) {
            this.playlistItemId = str;
            return this;
        }

        public PlaylistItem setResourceId(ResourceId resourceId) {
            this.resourceId = resourceId;
            return this;
        }
    }

    public static final class Recommendation extends GenericJson {
        @Key
        private String reason;
        @Key
        private ResourceId resourceId;
        @Key
        private ResourceId seedResourceId;

        public Recommendation clone() {
            return (Recommendation) super.clone();
        }

        public String getReason() {
            return this.reason;
        }

        public ResourceId getResourceId() {
            return this.resourceId;
        }

        public ResourceId getSeedResourceId() {
            return this.seedResourceId;
        }

        public Recommendation set(String str, Object obj) {
            return (Recommendation) super.set(str, obj);
        }

        public Recommendation setReason(String str) {
            this.reason = str;
            return this;
        }

        public Recommendation setResourceId(ResourceId resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Recommendation setSeedResourceId(ResourceId resourceId) {
            this.seedResourceId = resourceId;
            return this;
        }
    }

    public static final class Social extends GenericJson {
        @Key
        private String author;
        @Key
        private String imageUrl;
        @Key
        private String referenceUrl;
        @Key
        private ResourceId resourceId;
        @Key
        private String type;

        public Social clone() {
            return (Social) super.clone();
        }

        public String getAuthor() {
            return this.author;
        }

        public String getImageUrl() {
            return this.imageUrl;
        }

        public String getReferenceUrl() {
            return this.referenceUrl;
        }

        public ResourceId getResourceId() {
            return this.resourceId;
        }

        public String getType() {
            return this.type;
        }

        public Social set(String str, Object obj) {
            return (Social) super.set(str, obj);
        }

        public Social setAuthor(String str) {
            this.author = str;
            return this;
        }

        public Social setImageUrl(String str) {
            this.imageUrl = str;
            return this;
        }

        public Social setReferenceUrl(String str) {
            this.referenceUrl = str;
            return this;
        }

        public Social setResourceId(ResourceId resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Social setType(String str) {
            this.type = str;
            return this;
        }
    }

    public static final class Subscription extends GenericJson {
        @Key
        private ResourceId resourceId;

        public Subscription clone() {
            return (Subscription) super.clone();
        }

        public ResourceId getResourceId() {
            return this.resourceId;
        }

        public Subscription set(String str, Object obj) {
            return (Subscription) super.set(str, obj);
        }

        public Subscription setResourceId(ResourceId resourceId) {
            this.resourceId = resourceId;
            return this;
        }
    }

    public static final class Upload extends GenericJson {
        @Key
        private String videoId;

        public Upload clone() {
            return (Upload) super.clone();
        }

        public String getVideoId() {
            return this.videoId;
        }

        public Upload set(String str, Object obj) {
            return (Upload) super.set(str, obj);
        }

        public Upload setVideoId(String str) {
            this.videoId = str;
            return this;
        }
    }

    public ActivityContentDetails clone() {
        return (ActivityContentDetails) super.clone();
    }

    public Bulletin getBulletin() {
        return this.bulletin;
    }

    public ChannelItem getChannelItem() {
        return this.channelItem;
    }

    public Comment getComment() {
        return this.comment;
    }

    public Favorite getFavorite() {
        return this.favorite;
    }

    public Like getLike() {
        return this.like;
    }

    public PlaylistItem getPlaylistItem() {
        return this.playlistItem;
    }

    public Recommendation getRecommendation() {
        return this.recommendation;
    }

    public Social getSocial() {
        return this.social;
    }

    public Subscription getSubscription() {
        return this.subscription;
    }

    public Upload getUpload() {
        return this.upload;
    }

    public ActivityContentDetails set(String str, Object obj) {
        return (ActivityContentDetails) super.set(str, obj);
    }

    public ActivityContentDetails setBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
        return this;
    }

    public ActivityContentDetails setChannelItem(ChannelItem channelItem) {
        this.channelItem = channelItem;
        return this;
    }

    public ActivityContentDetails setComment(Comment comment) {
        this.comment = comment;
        return this;
    }

    public ActivityContentDetails setFavorite(Favorite favorite) {
        this.favorite = favorite;
        return this;
    }

    public ActivityContentDetails setLike(Like like) {
        this.like = like;
        return this;
    }

    public ActivityContentDetails setPlaylistItem(PlaylistItem playlistItem) {
        this.playlistItem = playlistItem;
        return this;
    }

    public ActivityContentDetails setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
        return this;
    }

    public ActivityContentDetails setSocial(Social social) {
        this.social = social;
        return this;
    }

    public ActivityContentDetails setSubscription(Subscription subscription) {
        this.subscription = subscription;
        return this;
    }

    public ActivityContentDetails setUpload(Upload upload) {
        this.upload = upload;
        return this;
    }
}
