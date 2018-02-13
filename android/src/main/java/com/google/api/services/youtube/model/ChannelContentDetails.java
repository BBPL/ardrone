package com.google.api.services.youtube.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class ChannelContentDetails extends GenericJson {
    @Key
    private String googlePlusUserId;
    @Key
    private RelatedPlaylists relatedPlaylists;

    public static final class RelatedPlaylists extends GenericJson {
        @Key
        private String favorites;
        @Key
        private String likes;
        @Key
        private String uploads;
        @Key
        private String watchHistory;
        @Key
        private String watchLater;

        public RelatedPlaylists clone() {
            return (RelatedPlaylists) super.clone();
        }

        public String getFavorites() {
            return this.favorites;
        }

        public String getLikes() {
            return this.likes;
        }

        public String getUploads() {
            return this.uploads;
        }

        public String getWatchHistory() {
            return this.watchHistory;
        }

        public String getWatchLater() {
            return this.watchLater;
        }

        public RelatedPlaylists set(String str, Object obj) {
            return (RelatedPlaylists) super.set(str, obj);
        }

        public RelatedPlaylists setFavorites(String str) {
            this.favorites = str;
            return this;
        }

        public RelatedPlaylists setLikes(String str) {
            this.likes = str;
            return this;
        }

        public RelatedPlaylists setUploads(String str) {
            this.uploads = str;
            return this;
        }

        public RelatedPlaylists setWatchHistory(String str) {
            this.watchHistory = str;
            return this;
        }

        public RelatedPlaylists setWatchLater(String str) {
            this.watchLater = str;
            return this;
        }
    }

    public ChannelContentDetails clone() {
        return (ChannelContentDetails) super.clone();
    }

    public String getGooglePlusUserId() {
        return this.googlePlusUserId;
    }

    public RelatedPlaylists getRelatedPlaylists() {
        return this.relatedPlaylists;
    }

    public ChannelContentDetails set(String str, Object obj) {
        return (ChannelContentDetails) super.set(str, obj);
    }

    public ChannelContentDetails setGooglePlusUserId(String str) {
        this.googlePlusUserId = str;
        return this;
    }

    public ChannelContentDetails setRelatedPlaylists(RelatedPlaylists relatedPlaylists) {
        this.relatedPlaylists = relatedPlaylists;
        return this;
    }
}
