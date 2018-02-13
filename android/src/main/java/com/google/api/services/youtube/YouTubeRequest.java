package com.google.api.services.youtube;

import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.util.Key;

public abstract class YouTubeRequest<T> extends AbstractGoogleJsonClientRequest<T> {
    @Key
    private String alt;
    @Key
    private String fields;
    @Key
    private String key;
    @Key("oauth_token")
    private String oauthToken;
    @Key
    private Boolean prettyPrint;
    @Key
    private String quotaUser;
    @Key
    private String userIp;

    public YouTubeRequest(YouTube youTube, String str, String str2, Object obj, Class<T> cls) {
        super(youTube, str, str2, obj, cls);
    }

    public final YouTube getAbstractGoogleClient() {
        return (YouTube) super.getAbstractGoogleClient();
    }

    public String getAlt() {
        return this.alt;
    }

    public String getFields() {
        return this.fields;
    }

    public String getKey() {
        return this.key;
    }

    public String getOauthToken() {
        return this.oauthToken;
    }

    public Boolean getPrettyPrint() {
        return this.prettyPrint;
    }

    public String getQuotaUser() {
        return this.quotaUser;
    }

    public String getUserIp() {
        return this.userIp;
    }

    public YouTubeRequest<T> set(String str, Object obj) {
        return (YouTubeRequest) super.set(str, obj);
    }

    public YouTubeRequest<T> setAlt(String str) {
        this.alt = str;
        return this;
    }

    public YouTubeRequest<T> setDisableGZipContent(boolean z) {
        return (YouTubeRequest) super.setDisableGZipContent(z);
    }

    public YouTubeRequest<T> setFields(String str) {
        this.fields = str;
        return this;
    }

    public YouTubeRequest<T> setKey(String str) {
        this.key = str;
        return this;
    }

    public YouTubeRequest<T> setOauthToken(String str) {
        this.oauthToken = str;
        return this;
    }

    public YouTubeRequest<T> setPrettyPrint(Boolean bool) {
        this.prettyPrint = bool;
        return this;
    }

    public YouTubeRequest<T> setQuotaUser(String str) {
        this.quotaUser = str;
        return this;
    }

    public YouTubeRequest<T> setRequestHeaders(HttpHeaders httpHeaders) {
        return (YouTubeRequest) super.setRequestHeaders(httpHeaders);
    }

    public YouTubeRequest<T> setUserIp(String str) {
        this.userIp = str;
        return this;
    }
}
