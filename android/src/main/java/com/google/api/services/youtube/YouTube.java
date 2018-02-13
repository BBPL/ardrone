package com.google.api.services.youtube;

import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityListResponse;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.GuideCategoryListResponse;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.LiveBroadcastList;
import com.google.api.services.youtube.model.LiveStream;
import com.google.api.services.youtube.model.LiveStreamList;
import com.google.api.services.youtube.model.PlayerListResponse;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoCategoryListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import java.io.IOException;

public class YouTube extends AbstractGoogleJsonClient {
    public static final String DEFAULT_BASE_URL = "https://www.googleapis.com/youtube/v3/";
    public static final String DEFAULT_ROOT_URL = "https://www.googleapis.com/";
    public static final String DEFAULT_SERVICE_PATH = "youtube/v3/";

    public class Activities {

        public class Insert extends YouTubeRequest<Activity> {
            private static final String REST_PATH = "activities";
            @Key
            private String part;

            protected Insert(String str, Activity activity) {
                super(YouTube.this, "POST", REST_PATH, activity, Activity.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public String getPart() {
                return this.part;
            }

            public Insert set(String str, Object obj) {
                return (Insert) super.set(str, obj);
            }

            public Insert setAlt(String str) {
                return (Insert) super.setAlt(str);
            }

            public Insert setFields(String str) {
                return (Insert) super.setFields(str);
            }

            public Insert setKey(String str) {
                return (Insert) super.setKey(str);
            }

            public Insert setOauthToken(String str) {
                return (Insert) super.setOauthToken(str);
            }

            public Insert setPart(String str) {
                this.part = str;
                return this;
            }

            public Insert setPrettyPrint(Boolean bool) {
                return (Insert) super.setPrettyPrint(bool);
            }

            public Insert setQuotaUser(String str) {
                return (Insert) super.setQuotaUser(str);
            }

            public Insert setUserIp(String str) {
                return (Insert) super.setUserIp(str);
            }
        }

        public class List extends YouTubeRequest<ActivityListResponse> {
            private static final String REST_PATH = "activities";
            @Key
            private String channelId;
            @Key
            private String home;
            @Key
            private Long maxResults;
            @Key
            private Boolean mine;
            @Key
            private String pageToken;
            @Key
            private String part;
            @Key
            private DateTime publishedAfter;
            @Key
            private DateTime publishedBefore;

            protected List(String str) {
                super(YouTube.this, "GET", REST_PATH, null, ActivityListResponse.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public String getChannelId() {
                return this.channelId;
            }

            public String getHome() {
                return this.home;
            }

            public Long getMaxResults() {
                return this.maxResults;
            }

            public Boolean getMine() {
                return this.mine;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public String getPart() {
                return this.part;
            }

            public DateTime getPublishedAfter() {
                return this.publishedAfter;
            }

            public DateTime getPublishedBefore() {
                return this.publishedBefore;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setChannelId(String str) {
                this.channelId = str;
                return this;
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setHome(String str) {
                this.home = str;
                return this;
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setMaxResults(Long l) {
                this.maxResults = l;
                return this;
            }

            public List setMine(Boolean bool) {
                this.mine = bool;
                return this;
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public List setPart(String str) {
                this.part = str;
                return this;
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setPublishedAfter(DateTime dateTime) {
                this.publishedAfter = dateTime;
                return this;
            }

            public List setPublishedBefore(DateTime dateTime) {
                this.publishedBefore = dateTime;
                return this;
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }
        }

        public Insert insert(String str, Activity activity) throws IOException {
            AbstractGoogleClientRequest insert = new Insert(str, activity);
            YouTube.this.initialize(insert);
            return insert;
        }

        public List list(String str) throws IOException {
            AbstractGoogleClientRequest list = new List(str);
            YouTube.this.initialize(list);
            return list;
        }
    }

    public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {
        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory, HttpRequestInitializer httpRequestInitializer) {
            super(httpTransport, jsonFactory, YouTube.DEFAULT_ROOT_URL, YouTube.DEFAULT_SERVICE_PATH, httpRequestInitializer, false);
        }

        public YouTube build() {
            return new YouTube(this);
        }

        public Builder setApplicationName(String str) {
            return (Builder) super.setApplicationName(str);
        }

        public Builder setGoogleClientRequestInitializer(GoogleClientRequestInitializer googleClientRequestInitializer) {
            return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
        }

        public Builder setHttpRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
        }

        public Builder setRootUrl(String str) {
            return (Builder) super.setRootUrl(str);
        }

        public Builder setServicePath(String str) {
            return (Builder) super.setServicePath(str);
        }

        public Builder setSuppressAllChecks(boolean z) {
            return (Builder) super.setSuppressAllChecks(z);
        }

        public Builder setSuppressPatternChecks(boolean z) {
            return (Builder) super.setSuppressPatternChecks(z);
        }

        public Builder setSuppressRequiredParameterChecks(boolean z) {
            return (Builder) super.setSuppressRequiredParameterChecks(z);
        }

        public Builder setYouTubeRequestInitializer(YouTubeRequestInitializer youTubeRequestInitializer) {
            return (Builder) super.setGoogleClientRequestInitializer((GoogleClientRequestInitializer) youTubeRequestInitializer);
        }
    }

    public class Channels {

        public class List extends YouTubeRequest<ChannelListResponse> {
            private static final String REST_PATH = "channels";
            @Key
            private String categoryId;
            @Key
            private String id;
            @Key
            private Boolean managedByMe;
            @Key
            private Long maxResults;
            @Key
            private Boolean mine;
            @Key
            private String mySubscribers;
            @Key
            private String onBehalfOfContentOwner;
            @Key
            private String pageToken;
            @Key
            private String part;

            protected List(String str) {
                super(YouTube.this, "GET", REST_PATH, null, ChannelListResponse.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public String getCategoryId() {
                return this.categoryId;
            }

            public String getId() {
                return this.id;
            }

            public Boolean getManagedByMe() {
                return this.managedByMe;
            }

            public Long getMaxResults() {
                return this.maxResults;
            }

            public Boolean getMine() {
                return this.mine;
            }

            public String getMySubscribers() {
                return this.mySubscribers;
            }

            public String getOnBehalfOfContentOwner() {
                return this.onBehalfOfContentOwner;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public String getPart() {
                return this.part;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setCategoryId(String str) {
                this.categoryId = str;
                return this;
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setId(String str) {
                this.id = str;
                return this;
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setManagedByMe(Boolean bool) {
                this.managedByMe = bool;
                return this;
            }

            public List setMaxResults(Long l) {
                this.maxResults = l;
                return this;
            }

            public List setMine(Boolean bool) {
                this.mine = bool;
                return this;
            }

            public List setMySubscribers(String str) {
                this.mySubscribers = str;
                return this;
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setOnBehalfOfContentOwner(String str) {
                this.onBehalfOfContentOwner = str;
                return this;
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public List setPart(String str) {
                this.part = str;
                return this;
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }
        }

        public List list(String str) throws IOException {
            AbstractGoogleClientRequest list = new List(str);
            YouTube.this.initialize(list);
            return list;
        }
    }

    public class GuideCategories {

        public class List extends YouTubeRequest<GuideCategoryListResponse> {
            private static final String REST_PATH = "guideCategories";
            @Key
            private String hl;
            @Key
            private String id;
            @Key
            private String part;
            @Key
            private String regionCode;

            protected List(String str) {
                super(YouTube.this, "GET", REST_PATH, null, GuideCategoryListResponse.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public String getHl() {
                return this.hl;
            }

            public String getId() {
                return this.id;
            }

            public String getPart() {
                return this.part;
            }

            public String getRegionCode() {
                return this.regionCode;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setHl(String str) {
                this.hl = str;
                return this;
            }

            public List setId(String str) {
                this.id = str;
                return this;
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPart(String str) {
                this.part = str;
                return this;
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setRegionCode(String str) {
                this.regionCode = str;
                return this;
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }
        }

        public List list(String str) throws IOException {
            AbstractGoogleClientRequest list = new List(str);
            YouTube.this.initialize(list);
            return list;
        }
    }

    public class LiveBroadcasts {

        public class Bind extends YouTubeRequest<LiveBroadcast> {
            private static final String REST_PATH = "liveBroadcasts/bind";
            @Key
            private String id;
            @Key
            private String part;
            @Key
            private String streamId;

            protected Bind(String str, String str2) {
                super(YouTube.this, "POST", REST_PATH, null, LiveBroadcast.class);
                this.id = (String) Preconditions.checkNotNull(str, "Required parameter id must be specified.");
                this.part = (String) Preconditions.checkNotNull(str2, "Required parameter part must be specified.");
            }

            public String getId() {
                return this.id;
            }

            public String getPart() {
                return this.part;
            }

            public String getStreamId() {
                return this.streamId;
            }

            public Bind set(String str, Object obj) {
                return (Bind) super.set(str, obj);
            }

            public Bind setAlt(String str) {
                return (Bind) super.setAlt(str);
            }

            public Bind setFields(String str) {
                return (Bind) super.setFields(str);
            }

            public Bind setId(String str) {
                this.id = str;
                return this;
            }

            public Bind setKey(String str) {
                return (Bind) super.setKey(str);
            }

            public Bind setOauthToken(String str) {
                return (Bind) super.setOauthToken(str);
            }

            public Bind setPart(String str) {
                this.part = str;
                return this;
            }

            public Bind setPrettyPrint(Boolean bool) {
                return (Bind) super.setPrettyPrint(bool);
            }

            public Bind setQuotaUser(String str) {
                return (Bind) super.setQuotaUser(str);
            }

            public Bind setStreamId(String str) {
                this.streamId = str;
                return this;
            }

            public Bind setUserIp(String str) {
                return (Bind) super.setUserIp(str);
            }
        }

        public class Delete extends YouTubeRequest<Void> {
            private static final String REST_PATH = "liveBroadcasts";
            @Key
            private String id;

            protected Delete(String str) {
                super(YouTube.this, "DELETE", REST_PATH, null, Void.class);
                this.id = (String) Preconditions.checkNotNull(str, "Required parameter id must be specified.");
            }

            public String getId() {
                return this.id;
            }

            public Delete set(String str, Object obj) {
                return (Delete) super.set(str, obj);
            }

            public Delete setAlt(String str) {
                return (Delete) super.setAlt(str);
            }

            public Delete setFields(String str) {
                return (Delete) super.setFields(str);
            }

            public Delete setId(String str) {
                this.id = str;
                return this;
            }

            public Delete setKey(String str) {
                return (Delete) super.setKey(str);
            }

            public Delete setOauthToken(String str) {
                return (Delete) super.setOauthToken(str);
            }

            public Delete setPrettyPrint(Boolean bool) {
                return (Delete) super.setPrettyPrint(bool);
            }

            public Delete setQuotaUser(String str) {
                return (Delete) super.setQuotaUser(str);
            }

            public Delete setUserIp(String str) {
                return (Delete) super.setUserIp(str);
            }
        }

        public class Insert extends YouTubeRequest<LiveBroadcast> {
            private static final String REST_PATH = "liveBroadcasts";
            @Key
            private String part;

            protected Insert(String str, LiveBroadcast liveBroadcast) {
                super(YouTube.this, "POST", REST_PATH, liveBroadcast, LiveBroadcast.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public String getPart() {
                return this.part;
            }

            public Insert set(String str, Object obj) {
                return (Insert) super.set(str, obj);
            }

            public Insert setAlt(String str) {
                return (Insert) super.setAlt(str);
            }

            public Insert setFields(String str) {
                return (Insert) super.setFields(str);
            }

            public Insert setKey(String str) {
                return (Insert) super.setKey(str);
            }

            public Insert setOauthToken(String str) {
                return (Insert) super.setOauthToken(str);
            }

            public Insert setPart(String str) {
                this.part = str;
                return this;
            }

            public Insert setPrettyPrint(Boolean bool) {
                return (Insert) super.setPrettyPrint(bool);
            }

            public Insert setQuotaUser(String str) {
                return (Insert) super.setQuotaUser(str);
            }

            public Insert setUserIp(String str) {
                return (Insert) super.setUserIp(str);
            }
        }

        public class List extends YouTubeRequest<LiveBroadcastList> {
            private static final String REST_PATH = "liveBroadcasts";
            @Key
            private String broadcastStatus;
            @Key
            private String id;
            @Key
            private Long maxResults;
            @Key
            private Boolean mine;
            @Key
            private String pageToken;
            @Key
            private String part;

            protected List(String str) {
                super(YouTube.this, "GET", REST_PATH, null, LiveBroadcastList.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public String getBroadcastStatus() {
                return this.broadcastStatus;
            }

            public String getId() {
                return this.id;
            }

            public Long getMaxResults() {
                return this.maxResults;
            }

            public Boolean getMine() {
                return this.mine;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public String getPart() {
                return this.part;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setBroadcastStatus(String str) {
                this.broadcastStatus = str;
                return this;
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setId(String str) {
                this.id = str;
                return this;
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setMaxResults(Long l) {
                this.maxResults = l;
                return this;
            }

            public List setMine(Boolean bool) {
                this.mine = bool;
                return this;
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public List setPart(String str) {
                this.part = str;
                return this;
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }
        }

        public class Transition extends YouTubeRequest<LiveBroadcast> {
            private static final String REST_PATH = "liveBroadcasts/transition";
            @Key
            private String broadcastStatus;
            @Key
            private String id;
            @Key
            private String part;

            protected Transition(String str, String str2, String str3) {
                super(YouTube.this, "POST", REST_PATH, null, LiveBroadcast.class);
                this.broadcastStatus = (String) Preconditions.checkNotNull(str, "Required parameter broadcastStatus must be specified.");
                this.id = (String) Preconditions.checkNotNull(str2, "Required parameter id must be specified.");
                this.part = (String) Preconditions.checkNotNull(str3, "Required parameter part must be specified.");
            }

            public String getBroadcastStatus() {
                return this.broadcastStatus;
            }

            public String getId() {
                return this.id;
            }

            public String getPart() {
                return this.part;
            }

            public Transition set(String str, Object obj) {
                return (Transition) super.set(str, obj);
            }

            public Transition setAlt(String str) {
                return (Transition) super.setAlt(str);
            }

            public Transition setBroadcastStatus(String str) {
                this.broadcastStatus = str;
                return this;
            }

            public Transition setFields(String str) {
                return (Transition) super.setFields(str);
            }

            public Transition setId(String str) {
                this.id = str;
                return this;
            }

            public Transition setKey(String str) {
                return (Transition) super.setKey(str);
            }

            public Transition setOauthToken(String str) {
                return (Transition) super.setOauthToken(str);
            }

            public Transition setPart(String str) {
                this.part = str;
                return this;
            }

            public Transition setPrettyPrint(Boolean bool) {
                return (Transition) super.setPrettyPrint(bool);
            }

            public Transition setQuotaUser(String str) {
                return (Transition) super.setQuotaUser(str);
            }

            public Transition setUserIp(String str) {
                return (Transition) super.setUserIp(str);
            }
        }

        public class Update extends YouTubeRequest<LiveBroadcast> {
            private static final String REST_PATH = "liveBroadcasts";
            @Key
            private String part;

            protected Update(String str, LiveBroadcast liveBroadcast) {
                super(YouTube.this, "PUT", REST_PATH, liveBroadcast, LiveBroadcast.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
                checkRequiredParameter(liveBroadcast, "content");
                checkRequiredParameter(liveBroadcast.getId(), "LiveBroadcast.getId()");
            }

            public String getPart() {
                return this.part;
            }

            public Update set(String str, Object obj) {
                return (Update) super.set(str, obj);
            }

            public Update setAlt(String str) {
                return (Update) super.setAlt(str);
            }

            public Update setFields(String str) {
                return (Update) super.setFields(str);
            }

            public Update setKey(String str) {
                return (Update) super.setKey(str);
            }

            public Update setOauthToken(String str) {
                return (Update) super.setOauthToken(str);
            }

            public Update setPart(String str) {
                this.part = str;
                return this;
            }

            public Update setPrettyPrint(Boolean bool) {
                return (Update) super.setPrettyPrint(bool);
            }

            public Update setQuotaUser(String str) {
                return (Update) super.setQuotaUser(str);
            }

            public Update setUserIp(String str) {
                return (Update) super.setUserIp(str);
            }
        }

        public Bind bind(String str, String str2) throws IOException {
            AbstractGoogleClientRequest bind = new Bind(str, str2);
            YouTube.this.initialize(bind);
            return bind;
        }

        public Delete delete(String str) throws IOException {
            AbstractGoogleClientRequest delete = new Delete(str);
            YouTube.this.initialize(delete);
            return delete;
        }

        public Insert insert(String str, LiveBroadcast liveBroadcast) throws IOException {
            AbstractGoogleClientRequest insert = new Insert(str, liveBroadcast);
            YouTube.this.initialize(insert);
            return insert;
        }

        public List list(String str) throws IOException {
            AbstractGoogleClientRequest list = new List(str);
            YouTube.this.initialize(list);
            return list;
        }

        public Transition transition(String str, String str2, String str3) throws IOException {
            AbstractGoogleClientRequest transition = new Transition(str, str2, str3);
            YouTube.this.initialize(transition);
            return transition;
        }

        public Update update(String str, LiveBroadcast liveBroadcast) throws IOException {
            AbstractGoogleClientRequest update = new Update(str, liveBroadcast);
            YouTube.this.initialize(update);
            return update;
        }
    }

    public class LiveStreams {

        public class Delete extends YouTubeRequest<Void> {
            private static final String REST_PATH = "liveStreams";
            @Key
            private String id;

            protected Delete(String str) {
                super(YouTube.this, "DELETE", REST_PATH, null, Void.class);
                this.id = (String) Preconditions.checkNotNull(str, "Required parameter id must be specified.");
            }

            public String getId() {
                return this.id;
            }

            public Delete set(String str, Object obj) {
                return (Delete) super.set(str, obj);
            }

            public Delete setAlt(String str) {
                return (Delete) super.setAlt(str);
            }

            public Delete setFields(String str) {
                return (Delete) super.setFields(str);
            }

            public Delete setId(String str) {
                this.id = str;
                return this;
            }

            public Delete setKey(String str) {
                return (Delete) super.setKey(str);
            }

            public Delete setOauthToken(String str) {
                return (Delete) super.setOauthToken(str);
            }

            public Delete setPrettyPrint(Boolean bool) {
                return (Delete) super.setPrettyPrint(bool);
            }

            public Delete setQuotaUser(String str) {
                return (Delete) super.setQuotaUser(str);
            }

            public Delete setUserIp(String str) {
                return (Delete) super.setUserIp(str);
            }
        }

        public class Insert extends YouTubeRequest<LiveStream> {
            private static final String REST_PATH = "liveStreams";
            @Key
            private String part;

            protected Insert(String str, LiveStream liveStream) {
                super(YouTube.this, "POST", REST_PATH, liveStream, LiveStream.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public String getPart() {
                return this.part;
            }

            public Insert set(String str, Object obj) {
                return (Insert) super.set(str, obj);
            }

            public Insert setAlt(String str) {
                return (Insert) super.setAlt(str);
            }

            public Insert setFields(String str) {
                return (Insert) super.setFields(str);
            }

            public Insert setKey(String str) {
                return (Insert) super.setKey(str);
            }

            public Insert setOauthToken(String str) {
                return (Insert) super.setOauthToken(str);
            }

            public Insert setPart(String str) {
                this.part = str;
                return this;
            }

            public Insert setPrettyPrint(Boolean bool) {
                return (Insert) super.setPrettyPrint(bool);
            }

            public Insert setQuotaUser(String str) {
                return (Insert) super.setQuotaUser(str);
            }

            public Insert setUserIp(String str) {
                return (Insert) super.setUserIp(str);
            }
        }

        public class List extends YouTubeRequest<LiveStreamList> {
            private static final String REST_PATH = "liveStreams";
            @Key
            private String id;
            @Key
            private Long maxResults;
            @Key
            private Boolean mine;
            @Key
            private String onBehalfOf;
            @Key
            private String pageToken;
            @Key
            private String part;

            protected List(String str) {
                super(YouTube.this, "GET", REST_PATH, null, LiveStreamList.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public String getId() {
                return this.id;
            }

            public Long getMaxResults() {
                return this.maxResults;
            }

            public Boolean getMine() {
                return this.mine;
            }

            public String getOnBehalfOf() {
                return this.onBehalfOf;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public String getPart() {
                return this.part;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setId(String str) {
                this.id = str;
                return this;
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setMaxResults(Long l) {
                this.maxResults = l;
                return this;
            }

            public List setMine(Boolean bool) {
                this.mine = bool;
                return this;
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setOnBehalfOf(String str) {
                this.onBehalfOf = str;
                return this;
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public List setPart(String str) {
                this.part = str;
                return this;
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }
        }

        public class Update extends YouTubeRequest<LiveStream> {
            private static final String REST_PATH = "liveStreams";
            @Key
            private String part;

            protected Update(String str, LiveStream liveStream) {
                super(YouTube.this, "PUT", REST_PATH, liveStream, LiveStream.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
                checkRequiredParameter(liveStream, "content");
                checkRequiredParameter(liveStream.getId(), "LiveStream.getId()");
            }

            public String getPart() {
                return this.part;
            }

            public Update set(String str, Object obj) {
                return (Update) super.set(str, obj);
            }

            public Update setAlt(String str) {
                return (Update) super.setAlt(str);
            }

            public Update setFields(String str) {
                return (Update) super.setFields(str);
            }

            public Update setKey(String str) {
                return (Update) super.setKey(str);
            }

            public Update setOauthToken(String str) {
                return (Update) super.setOauthToken(str);
            }

            public Update setPart(String str) {
                this.part = str;
                return this;
            }

            public Update setPrettyPrint(Boolean bool) {
                return (Update) super.setPrettyPrint(bool);
            }

            public Update setQuotaUser(String str) {
                return (Update) super.setQuotaUser(str);
            }

            public Update setUserIp(String str) {
                return (Update) super.setUserIp(str);
            }
        }

        public Delete delete(String str) throws IOException {
            AbstractGoogleClientRequest delete = new Delete(str);
            YouTube.this.initialize(delete);
            return delete;
        }

        public Insert insert(String str, LiveStream liveStream) throws IOException {
            AbstractGoogleClientRequest insert = new Insert(str, liveStream);
            YouTube.this.initialize(insert);
            return insert;
        }

        public List list(String str) throws IOException {
            AbstractGoogleClientRequest list = new List(str);
            YouTube.this.initialize(list);
            return list;
        }

        public Update update(String str, LiveStream liveStream) throws IOException {
            AbstractGoogleClientRequest update = new Update(str, liveStream);
            YouTube.this.initialize(update);
            return update;
        }
    }

    public class Players {

        public class List extends YouTubeRequest<PlayerListResponse> {
            private static final String REST_PATH = "players";
            @Key
            private String itag;
            @Key
            private String part;
            @Key
            private String videoId;

            protected List(String str) {
                super(YouTube.this, "GET", "players", null, PlayerListResponse.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public String getItag() {
                return this.itag;
            }

            public String getPart() {
                return this.part;
            }

            public String getVideoId() {
                return this.videoId;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setItag(String str) {
                this.itag = str;
                return this;
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPart(String str) {
                this.part = str;
                return this;
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }

            public List setVideoId(String str) {
                this.videoId = str;
                return this;
            }
        }

        public List list(String str) throws IOException {
            AbstractGoogleClientRequest list = new List(str);
            YouTube.this.initialize(list);
            return list;
        }
    }

    public class PlaylistItems {

        public class Delete extends YouTubeRequest<Void> {
            private static final String REST_PATH = "playlistItems";
            @Key
            private String id;

            protected Delete(String str) {
                super(YouTube.this, "DELETE", REST_PATH, null, Void.class);
                this.id = (String) Preconditions.checkNotNull(str, "Required parameter id must be specified.");
            }

            public String getId() {
                return this.id;
            }

            public Delete set(String str, Object obj) {
                return (Delete) super.set(str, obj);
            }

            public Delete setAlt(String str) {
                return (Delete) super.setAlt(str);
            }

            public Delete setFields(String str) {
                return (Delete) super.setFields(str);
            }

            public Delete setId(String str) {
                this.id = str;
                return this;
            }

            public Delete setKey(String str) {
                return (Delete) super.setKey(str);
            }

            public Delete setOauthToken(String str) {
                return (Delete) super.setOauthToken(str);
            }

            public Delete setPrettyPrint(Boolean bool) {
                return (Delete) super.setPrettyPrint(bool);
            }

            public Delete setQuotaUser(String str) {
                return (Delete) super.setQuotaUser(str);
            }

            public Delete setUserIp(String str) {
                return (Delete) super.setUserIp(str);
            }
        }

        public class Insert extends YouTubeRequest<PlaylistItem> {
            private static final String REST_PATH = "playlistItems";
            @Key
            private String part;

            protected Insert(String str, PlaylistItem playlistItem) {
                super(YouTube.this, "POST", REST_PATH, playlistItem, PlaylistItem.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public String getPart() {
                return this.part;
            }

            public Insert set(String str, Object obj) {
                return (Insert) super.set(str, obj);
            }

            public Insert setAlt(String str) {
                return (Insert) super.setAlt(str);
            }

            public Insert setFields(String str) {
                return (Insert) super.setFields(str);
            }

            public Insert setKey(String str) {
                return (Insert) super.setKey(str);
            }

            public Insert setOauthToken(String str) {
                return (Insert) super.setOauthToken(str);
            }

            public Insert setPart(String str) {
                this.part = str;
                return this;
            }

            public Insert setPrettyPrint(Boolean bool) {
                return (Insert) super.setPrettyPrint(bool);
            }

            public Insert setQuotaUser(String str) {
                return (Insert) super.setQuotaUser(str);
            }

            public Insert setUserIp(String str) {
                return (Insert) super.setUserIp(str);
            }
        }

        public class List extends YouTubeRequest<PlaylistItemListResponse> {
            private static final String REST_PATH = "playlistItems";
            @Key
            private String id;
            @Key
            private Long maxResults;
            @Key
            private String pageToken;
            @Key
            private String part;
            @Key
            private String playlistId;
            @Key
            private String videoId;

            protected List(String str) {
                super(YouTube.this, "GET", REST_PATH, null, PlaylistItemListResponse.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public String getId() {
                return this.id;
            }

            public Long getMaxResults() {
                return this.maxResults;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public String getPart() {
                return this.part;
            }

            public String getPlaylistId() {
                return this.playlistId;
            }

            public String getVideoId() {
                return this.videoId;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setId(String str) {
                this.id = str;
                return this;
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setMaxResults(Long l) {
                this.maxResults = l;
                return this;
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public List setPart(String str) {
                this.part = str;
                return this;
            }

            public List setPlaylistId(String str) {
                this.playlistId = str;
                return this;
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }

            public List setVideoId(String str) {
                this.videoId = str;
                return this;
            }
        }

        public class Update extends YouTubeRequest<PlaylistItem> {
            private static final String REST_PATH = "playlistItems";
            @Key
            private String part;

            protected Update(String str, PlaylistItem playlistItem) {
                super(YouTube.this, "PUT", REST_PATH, playlistItem, PlaylistItem.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public String getPart() {
                return this.part;
            }

            public Update set(String str, Object obj) {
                return (Update) super.set(str, obj);
            }

            public Update setAlt(String str) {
                return (Update) super.setAlt(str);
            }

            public Update setFields(String str) {
                return (Update) super.setFields(str);
            }

            public Update setKey(String str) {
                return (Update) super.setKey(str);
            }

            public Update setOauthToken(String str) {
                return (Update) super.setOauthToken(str);
            }

            public Update setPart(String str) {
                this.part = str;
                return this;
            }

            public Update setPrettyPrint(Boolean bool) {
                return (Update) super.setPrettyPrint(bool);
            }

            public Update setQuotaUser(String str) {
                return (Update) super.setQuotaUser(str);
            }

            public Update setUserIp(String str) {
                return (Update) super.setUserIp(str);
            }
        }

        public Delete delete(String str) throws IOException {
            AbstractGoogleClientRequest delete = new Delete(str);
            YouTube.this.initialize(delete);
            return delete;
        }

        public Insert insert(String str, PlaylistItem playlistItem) throws IOException {
            AbstractGoogleClientRequest insert = new Insert(str, playlistItem);
            YouTube.this.initialize(insert);
            return insert;
        }

        public List list(String str) throws IOException {
            AbstractGoogleClientRequest list = new List(str);
            YouTube.this.initialize(list);
            return list;
        }

        public Update update(String str, PlaylistItem playlistItem) throws IOException {
            AbstractGoogleClientRequest update = new Update(str, playlistItem);
            YouTube.this.initialize(update);
            return update;
        }
    }

    public class Playlists {

        public class Delete extends YouTubeRequest<Void> {
            private static final String REST_PATH = "playlists";
            @Key
            private String id;

            protected Delete(String str) {
                super(YouTube.this, "DELETE", REST_PATH, null, Void.class);
                this.id = (String) Preconditions.checkNotNull(str, "Required parameter id must be specified.");
            }

            public String getId() {
                return this.id;
            }

            public Delete set(String str, Object obj) {
                return (Delete) super.set(str, obj);
            }

            public Delete setAlt(String str) {
                return (Delete) super.setAlt(str);
            }

            public Delete setFields(String str) {
                return (Delete) super.setFields(str);
            }

            public Delete setId(String str) {
                this.id = str;
                return this;
            }

            public Delete setKey(String str) {
                return (Delete) super.setKey(str);
            }

            public Delete setOauthToken(String str) {
                return (Delete) super.setOauthToken(str);
            }

            public Delete setPrettyPrint(Boolean bool) {
                return (Delete) super.setPrettyPrint(bool);
            }

            public Delete setQuotaUser(String str) {
                return (Delete) super.setQuotaUser(str);
            }

            public Delete setUserIp(String str) {
                return (Delete) super.setUserIp(str);
            }
        }

        public class Insert extends YouTubeRequest<Playlist> {
            private static final String REST_PATH = "playlists";
            @Key
            private String part;

            protected Insert(String str, Playlist playlist) {
                super(YouTube.this, "POST", REST_PATH, playlist, Playlist.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public String getPart() {
                return this.part;
            }

            public Insert set(String str, Object obj) {
                return (Insert) super.set(str, obj);
            }

            public Insert setAlt(String str) {
                return (Insert) super.setAlt(str);
            }

            public Insert setFields(String str) {
                return (Insert) super.setFields(str);
            }

            public Insert setKey(String str) {
                return (Insert) super.setKey(str);
            }

            public Insert setOauthToken(String str) {
                return (Insert) super.setOauthToken(str);
            }

            public Insert setPart(String str) {
                this.part = str;
                return this;
            }

            public Insert setPrettyPrint(Boolean bool) {
                return (Insert) super.setPrettyPrint(bool);
            }

            public Insert setQuotaUser(String str) {
                return (Insert) super.setQuotaUser(str);
            }

            public Insert setUserIp(String str) {
                return (Insert) super.setUserIp(str);
            }
        }

        public class List extends YouTubeRequest<PlaylistListResponse> {
            private static final String REST_PATH = "playlists";
            @Key
            private String channelId;
            @Key
            private String id;
            @Key
            private Long maxResults;
            @Key
            private Boolean mine;
            @Key
            private String pageToken;
            @Key
            private String part;

            protected List(String str) {
                super(YouTube.this, "GET", REST_PATH, null, PlaylistListResponse.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public String getChannelId() {
                return this.channelId;
            }

            public String getId() {
                return this.id;
            }

            public Long getMaxResults() {
                return this.maxResults;
            }

            public Boolean getMine() {
                return this.mine;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public String getPart() {
                return this.part;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setChannelId(String str) {
                this.channelId = str;
                return this;
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setId(String str) {
                this.id = str;
                return this;
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setMaxResults(Long l) {
                this.maxResults = l;
                return this;
            }

            public List setMine(Boolean bool) {
                this.mine = bool;
                return this;
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public List setPart(String str) {
                this.part = str;
                return this;
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }
        }

        public class Update extends YouTubeRequest<Playlist> {
            private static final String REST_PATH = "playlists";
            @Key
            private String part;

            protected Update(String str, Playlist playlist) {
                super(YouTube.this, "PUT", REST_PATH, playlist, Playlist.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public String getPart() {
                return this.part;
            }

            public Update set(String str, Object obj) {
                return (Update) super.set(str, obj);
            }

            public Update setAlt(String str) {
                return (Update) super.setAlt(str);
            }

            public Update setFields(String str) {
                return (Update) super.setFields(str);
            }

            public Update setKey(String str) {
                return (Update) super.setKey(str);
            }

            public Update setOauthToken(String str) {
                return (Update) super.setOauthToken(str);
            }

            public Update setPart(String str) {
                this.part = str;
                return this;
            }

            public Update setPrettyPrint(Boolean bool) {
                return (Update) super.setPrettyPrint(bool);
            }

            public Update setQuotaUser(String str) {
                return (Update) super.setQuotaUser(str);
            }

            public Update setUserIp(String str) {
                return (Update) super.setUserIp(str);
            }
        }

        public Delete delete(String str) throws IOException {
            AbstractGoogleClientRequest delete = new Delete(str);
            YouTube.this.initialize(delete);
            return delete;
        }

        public Insert insert(String str, Playlist playlist) throws IOException {
            AbstractGoogleClientRequest insert = new Insert(str, playlist);
            YouTube.this.initialize(insert);
            return insert;
        }

        public List list(String str) throws IOException {
            AbstractGoogleClientRequest list = new List(str);
            YouTube.this.initialize(list);
            return list;
        }

        public Update update(String str, Playlist playlist) throws IOException {
            AbstractGoogleClientRequest update = new Update(str, playlist);
            YouTube.this.initialize(update);
            return update;
        }
    }

    public class Search {

        public class List extends YouTubeRequest<SearchListResponse> {
            private static final String REST_PATH = "search";
            @Key
            private String channelId;
            @Key
            private String channelType;
            @Key
            private Boolean forContentOwner;
            @Key
            private Long maxResults;
            @Key
            private String onBehalfOfContentOwner;
            @Key
            private String order;
            @Key
            private String pageToken;
            @Key
            private String part;
            @Key
            private DateTime publishedAfter;
            @Key
            private DateTime publishedBefore;
            @Key
            private String f264q;
            @Key
            private String regionCode;
            @Key
            private String relatedToVideoId;
            @Key
            private String safeSearch;
            @Key
            private String topicId;
            @Key
            private String type;
            @Key
            private String videoCaption;
            @Key
            private String videoCategoryId;
            @Key
            private String videoDefinition;
            @Key
            private String videoDimension;
            @Key
            private String videoDuration;
            @Key
            private String videoEmbeddable;
            @Key
            private String videoLicense;
            @Key
            private String videoSyndicated;
            @Key
            private String videoType;

            protected List(String str) {
                super(YouTube.this, "GET", REST_PATH, null, SearchListResponse.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public String getChannelId() {
                return this.channelId;
            }

            public String getChannelType() {
                return this.channelType;
            }

            public Boolean getForContentOwner() {
                return this.forContentOwner;
            }

            public Long getMaxResults() {
                return this.maxResults;
            }

            public String getOnBehalfOfContentOwner() {
                return this.onBehalfOfContentOwner;
            }

            public String getOrder() {
                return this.order;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public String getPart() {
                return this.part;
            }

            public DateTime getPublishedAfter() {
                return this.publishedAfter;
            }

            public DateTime getPublishedBefore() {
                return this.publishedBefore;
            }

            public String getQ() {
                return this.f264q;
            }

            public String getRegionCode() {
                return this.regionCode;
            }

            public String getRelatedToVideoId() {
                return this.relatedToVideoId;
            }

            public String getSafeSearch() {
                return this.safeSearch;
            }

            public String getTopicId() {
                return this.topicId;
            }

            public String getType() {
                return this.type;
            }

            public String getVideoCaption() {
                return this.videoCaption;
            }

            public String getVideoCategoryId() {
                return this.videoCategoryId;
            }

            public String getVideoDefinition() {
                return this.videoDefinition;
            }

            public String getVideoDimension() {
                return this.videoDimension;
            }

            public String getVideoDuration() {
                return this.videoDuration;
            }

            public String getVideoEmbeddable() {
                return this.videoEmbeddable;
            }

            public String getVideoLicense() {
                return this.videoLicense;
            }

            public String getVideoSyndicated() {
                return this.videoSyndicated;
            }

            public String getVideoType() {
                return this.videoType;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setChannelId(String str) {
                this.channelId = str;
                return this;
            }

            public List setChannelType(String str) {
                this.channelType = str;
                return this;
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setForContentOwner(Boolean bool) {
                this.forContentOwner = bool;
                return this;
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setMaxResults(Long l) {
                this.maxResults = l;
                return this;
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setOnBehalfOfContentOwner(String str) {
                this.onBehalfOfContentOwner = str;
                return this;
            }

            public List setOrder(String str) {
                this.order = str;
                return this;
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public List setPart(String str) {
                this.part = str;
                return this;
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setPublishedAfter(DateTime dateTime) {
                this.publishedAfter = dateTime;
                return this;
            }

            public List setPublishedBefore(DateTime dateTime) {
                this.publishedBefore = dateTime;
                return this;
            }

            public List setQ(String str) {
                this.f264q = str;
                return this;
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setRegionCode(String str) {
                this.regionCode = str;
                return this;
            }

            public List setRelatedToVideoId(String str) {
                this.relatedToVideoId = str;
                return this;
            }

            public List setSafeSearch(String str) {
                this.safeSearch = str;
                return this;
            }

            public List setTopicId(String str) {
                this.topicId = str;
                return this;
            }

            public List setType(String str) {
                this.type = str;
                return this;
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }

            public List setVideoCaption(String str) {
                this.videoCaption = str;
                return this;
            }

            public List setVideoCategoryId(String str) {
                this.videoCategoryId = str;
                return this;
            }

            public List setVideoDefinition(String str) {
                this.videoDefinition = str;
                return this;
            }

            public List setVideoDimension(String str) {
                this.videoDimension = str;
                return this;
            }

            public List setVideoDuration(String str) {
                this.videoDuration = str;
                return this;
            }

            public List setVideoEmbeddable(String str) {
                this.videoEmbeddable = str;
                return this;
            }

            public List setVideoLicense(String str) {
                this.videoLicense = str;
                return this;
            }

            public List setVideoSyndicated(String str) {
                this.videoSyndicated = str;
                return this;
            }

            public List setVideoType(String str) {
                this.videoType = str;
                return this;
            }
        }

        public List list(String str) throws IOException {
            AbstractGoogleClientRequest list = new List(str);
            YouTube.this.initialize(list);
            return list;
        }
    }

    public class Subscriptions {

        public class Delete extends YouTubeRequest<Void> {
            private static final String REST_PATH = "subscriptions";
            @Key
            private String id;

            protected Delete(String str) {
                super(YouTube.this, "DELETE", REST_PATH, null, Void.class);
                this.id = (String) Preconditions.checkNotNull(str, "Required parameter id must be specified.");
            }

            public String getId() {
                return this.id;
            }

            public Delete set(String str, Object obj) {
                return (Delete) super.set(str, obj);
            }

            public Delete setAlt(String str) {
                return (Delete) super.setAlt(str);
            }

            public Delete setFields(String str) {
                return (Delete) super.setFields(str);
            }

            public Delete setId(String str) {
                this.id = str;
                return this;
            }

            public Delete setKey(String str) {
                return (Delete) super.setKey(str);
            }

            public Delete setOauthToken(String str) {
                return (Delete) super.setOauthToken(str);
            }

            public Delete setPrettyPrint(Boolean bool) {
                return (Delete) super.setPrettyPrint(bool);
            }

            public Delete setQuotaUser(String str) {
                return (Delete) super.setQuotaUser(str);
            }

            public Delete setUserIp(String str) {
                return (Delete) super.setUserIp(str);
            }
        }

        public class Insert extends YouTubeRequest<Subscription> {
            private static final String REST_PATH = "subscriptions";
            @Key
            private String part;

            protected Insert(String str, Subscription subscription) {
                super(YouTube.this, "POST", REST_PATH, subscription, Subscription.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public String getPart() {
                return this.part;
            }

            public Insert set(String str, Object obj) {
                return (Insert) super.set(str, obj);
            }

            public Insert setAlt(String str) {
                return (Insert) super.setAlt(str);
            }

            public Insert setFields(String str) {
                return (Insert) super.setFields(str);
            }

            public Insert setKey(String str) {
                return (Insert) super.setKey(str);
            }

            public Insert setOauthToken(String str) {
                return (Insert) super.setOauthToken(str);
            }

            public Insert setPart(String str) {
                this.part = str;
                return this;
            }

            public Insert setPrettyPrint(Boolean bool) {
                return (Insert) super.setPrettyPrint(bool);
            }

            public Insert setQuotaUser(String str) {
                return (Insert) super.setQuotaUser(str);
            }

            public Insert setUserIp(String str) {
                return (Insert) super.setUserIp(str);
            }
        }

        public class List extends YouTubeRequest<SubscriptionListResponse> {
            private static final String REST_PATH = "subscriptions";
            @Key
            private String channelId;
            @Key
            private String forChannelId;
            @Key
            private String id;
            @Key
            private Long maxResults;
            @Key
            private Boolean mine;
            @Key
            private String order;
            @Key
            private String pageToken;
            @Key
            private String part;

            protected List(String str) {
                super(YouTube.this, "GET", REST_PATH, null, SubscriptionListResponse.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public String getChannelId() {
                return this.channelId;
            }

            public String getForChannelId() {
                return this.forChannelId;
            }

            public String getId() {
                return this.id;
            }

            public Long getMaxResults() {
                return this.maxResults;
            }

            public Boolean getMine() {
                return this.mine;
            }

            public String getOrder() {
                return this.order;
            }

            public String getPageToken() {
                return this.pageToken;
            }

            public String getPart() {
                return this.part;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setChannelId(String str) {
                this.channelId = str;
                return this;
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setForChannelId(String str) {
                this.forChannelId = str;
                return this;
            }

            public List setId(String str) {
                this.id = str;
                return this;
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setMaxResults(Long l) {
                this.maxResults = l;
                return this;
            }

            public List setMine(Boolean bool) {
                this.mine = bool;
                return this;
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setOrder(String str) {
                this.order = str;
                return this;
            }

            public List setPageToken(String str) {
                this.pageToken = str;
                return this;
            }

            public List setPart(String str) {
                this.part = str;
                return this;
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }
        }

        public Delete delete(String str) throws IOException {
            AbstractGoogleClientRequest delete = new Delete(str);
            YouTube.this.initialize(delete);
            return delete;
        }

        public Insert insert(String str, Subscription subscription) throws IOException {
            AbstractGoogleClientRequest insert = new Insert(str, subscription);
            YouTube.this.initialize(insert);
            return insert;
        }

        public List list(String str) throws IOException {
            AbstractGoogleClientRequest list = new List(str);
            YouTube.this.initialize(list);
            return list;
        }
    }

    public class VideoCategories {

        public class List extends YouTubeRequest<VideoCategoryListResponse> {
            private static final String REST_PATH = "videoCategories";
            @Key
            private String hl;
            @Key
            private String id;
            @Key
            private String part;
            @Key
            private String regionCode;

            protected List(String str) {
                super(YouTube.this, "GET", REST_PATH, null, VideoCategoryListResponse.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public String getHl() {
                return this.hl;
            }

            public String getId() {
                return this.id;
            }

            public String getPart() {
                return this.part;
            }

            public String getRegionCode() {
                return this.regionCode;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setHl(String str) {
                this.hl = str;
                return this;
            }

            public List setId(String str) {
                this.id = str;
                return this;
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setPart(String str) {
                this.part = str;
                return this;
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setRegionCode(String str) {
                this.regionCode = str;
                return this;
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }
        }

        public List list(String str) throws IOException {
            AbstractGoogleClientRequest list = new List(str);
            YouTube.this.initialize(list);
            return list;
        }
    }

    public class Videos {

        public class Delete extends YouTubeRequest<Void> {
            private static final String REST_PATH = "videos";
            @Key
            private String id;
            @Key
            private String onBehalfOfContentOwner;

            protected Delete(String str) {
                super(YouTube.this, "DELETE", REST_PATH, null, Void.class);
                this.id = (String) Preconditions.checkNotNull(str, "Required parameter id must be specified.");
            }

            public String getId() {
                return this.id;
            }

            public String getOnBehalfOfContentOwner() {
                return this.onBehalfOfContentOwner;
            }

            public Delete set(String str, Object obj) {
                return (Delete) super.set(str, obj);
            }

            public Delete setAlt(String str) {
                return (Delete) super.setAlt(str);
            }

            public Delete setFields(String str) {
                return (Delete) super.setFields(str);
            }

            public Delete setId(String str) {
                this.id = str;
                return this;
            }

            public Delete setKey(String str) {
                return (Delete) super.setKey(str);
            }

            public Delete setOauthToken(String str) {
                return (Delete) super.setOauthToken(str);
            }

            public Delete setOnBehalfOfContentOwner(String str) {
                this.onBehalfOfContentOwner = str;
                return this;
            }

            public Delete setPrettyPrint(Boolean bool) {
                return (Delete) super.setPrettyPrint(bool);
            }

            public Delete setQuotaUser(String str) {
                return (Delete) super.setQuotaUser(str);
            }

            public Delete setUserIp(String str) {
                return (Delete) super.setUserIp(str);
            }
        }

        public class Insert extends YouTubeRequest<Video> {
            private static final String REST_PATH = "videos";
            @Key
            private String part;

            protected Insert(String str, Video video) {
                super(YouTube.this, "POST", REST_PATH, video, Video.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            protected Insert(String str, Video video, AbstractInputStreamContent abstractInputStreamContent) {
                super(YouTube.this, "POST", "/upload/" + YouTube.this.getServicePath() + REST_PATH, video, Video.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
                initializeMediaUpload(abstractInputStreamContent);
            }

            public String getPart() {
                return this.part;
            }

            public Insert set(String str, Object obj) {
                return (Insert) super.set(str, obj);
            }

            public Insert setAlt(String str) {
                return (Insert) super.setAlt(str);
            }

            public Insert setFields(String str) {
                return (Insert) super.setFields(str);
            }

            public Insert setKey(String str) {
                return (Insert) super.setKey(str);
            }

            public Insert setOauthToken(String str) {
                return (Insert) super.setOauthToken(str);
            }

            public Insert setPart(String str) {
                this.part = str;
                return this;
            }

            public Insert setPrettyPrint(Boolean bool) {
                return (Insert) super.setPrettyPrint(bool);
            }

            public Insert setQuotaUser(String str) {
                return (Insert) super.setQuotaUser(str);
            }

            public Insert setUserIp(String str) {
                return (Insert) super.setUserIp(str);
            }
        }

        public class List extends YouTubeRequest<VideoListResponse> {
            private static final String REST_PATH = "videos";
            @Key
            private String id;
            @Key
            private String onBehalfOfContentOwner;
            @Key
            private String part;

            protected List(String str, String str2) {
                super(YouTube.this, "GET", REST_PATH, null, VideoListResponse.class);
                this.id = (String) Preconditions.checkNotNull(str, "Required parameter id must be specified.");
                this.part = (String) Preconditions.checkNotNull(str2, "Required parameter part must be specified.");
            }

            public HttpRequest buildHttpRequestUsingHead() throws IOException {
                return super.buildHttpRequestUsingHead();
            }

            public HttpResponse executeUsingHead() throws IOException {
                return super.executeUsingHead();
            }

            public String getId() {
                return this.id;
            }

            public String getOnBehalfOfContentOwner() {
                return this.onBehalfOfContentOwner;
            }

            public String getPart() {
                return this.part;
            }

            public List set(String str, Object obj) {
                return (List) super.set(str, obj);
            }

            public List setAlt(String str) {
                return (List) super.setAlt(str);
            }

            public List setFields(String str) {
                return (List) super.setFields(str);
            }

            public List setId(String str) {
                this.id = str;
                return this;
            }

            public List setKey(String str) {
                return (List) super.setKey(str);
            }

            public List setOauthToken(String str) {
                return (List) super.setOauthToken(str);
            }

            public List setOnBehalfOfContentOwner(String str) {
                this.onBehalfOfContentOwner = str;
                return this;
            }

            public List setPart(String str) {
                this.part = str;
                return this;
            }

            public List setPrettyPrint(Boolean bool) {
                return (List) super.setPrettyPrint(bool);
            }

            public List setQuotaUser(String str) {
                return (List) super.setQuotaUser(str);
            }

            public List setUserIp(String str) {
                return (List) super.setUserIp(str);
            }
        }

        public class Update extends YouTubeRequest<Video> {
            private static final String REST_PATH = "videos";
            @Key
            private String onBehalfOfContentOwner;
            @Key
            private String part;

            protected Update(String str, Video video) {
                super(YouTube.this, "PUT", REST_PATH, video, Video.class);
                this.part = (String) Preconditions.checkNotNull(str, "Required parameter part must be specified.");
            }

            public String getOnBehalfOfContentOwner() {
                return this.onBehalfOfContentOwner;
            }

            public String getPart() {
                return this.part;
            }

            public Update set(String str, Object obj) {
                return (Update) super.set(str, obj);
            }

            public Update setAlt(String str) {
                return (Update) super.setAlt(str);
            }

            public Update setFields(String str) {
                return (Update) super.setFields(str);
            }

            public Update setKey(String str) {
                return (Update) super.setKey(str);
            }

            public Update setOauthToken(String str) {
                return (Update) super.setOauthToken(str);
            }

            public Update setOnBehalfOfContentOwner(String str) {
                this.onBehalfOfContentOwner = str;
                return this;
            }

            public Update setPart(String str) {
                this.part = str;
                return this;
            }

            public Update setPrettyPrint(Boolean bool) {
                return (Update) super.setPrettyPrint(bool);
            }

            public Update setQuotaUser(String str) {
                return (Update) super.setQuotaUser(str);
            }

            public Update setUserIp(String str) {
                return (Update) super.setUserIp(str);
            }
        }

        public Delete delete(String str) throws IOException {
            AbstractGoogleClientRequest delete = new Delete(str);
            YouTube.this.initialize(delete);
            return delete;
        }

        public Insert insert(String str, Video video) throws IOException {
            AbstractGoogleClientRequest insert = new Insert(str, video);
            YouTube.this.initialize(insert);
            return insert;
        }

        public Insert insert(String str, Video video, AbstractInputStreamContent abstractInputStreamContent) throws IOException {
            AbstractGoogleClientRequest insert = new Insert(str, video, abstractInputStreamContent);
            YouTube.this.initialize(insert);
            return insert;
        }

        public List list(String str, String str2) throws IOException {
            AbstractGoogleClientRequest list = new List(str, str2);
            YouTube.this.initialize(list);
            return list;
        }

        public Update update(String str, Video video) throws IOException {
            AbstractGoogleClientRequest update = new Update(str, video);
            YouTube.this.initialize(update);
            return update;
        }
    }

    static {
        Preconditions.checkState(GoogleUtils.VERSION.equals(HttpRequest.VERSION), "You are currently running with version %s of google-api-client. You need version 1.14.1-beta of google-api-client to run version 1.14.1-beta of the YouTube Data API library.", GoogleUtils.VERSION);
    }

    public YouTube(HttpTransport httpTransport, JsonFactory jsonFactory, HttpRequestInitializer httpRequestInitializer) {
        this(new Builder(httpTransport, jsonFactory, httpRequestInitializer));
    }

    YouTube(Builder builder) {
        super(builder);
    }

    public Activities activities() {
        return new Activities();
    }

    public Channels channels() {
        return new Channels();
    }

    public GuideCategories guideCategories() {
        return new GuideCategories();
    }

    protected void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
        super.initialize(abstractGoogleClientRequest);
    }

    public LiveBroadcasts liveBroadcasts() {
        return new LiveBroadcasts();
    }

    public LiveStreams liveStreams() {
        return new LiveStreams();
    }

    public Players players() {
        return new Players();
    }

    public PlaylistItems playlistItems() {
        return new PlaylistItems();
    }

    public Playlists playlists() {
        return new Playlists();
    }

    public Search search() {
        return new Search();
    }

    public Subscriptions subscriptions() {
        return new Subscriptions();
    }

    public VideoCategories videoCategories() {
        return new VideoCategories();
    }

    public Videos videos() {
        return new Videos();
    }
}
