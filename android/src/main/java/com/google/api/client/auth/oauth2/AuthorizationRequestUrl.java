package com.google.api.client.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import java.util.Arrays;

public class AuthorizationRequestUrl extends GenericUrl {
    @Key("client_id")
    private String clientId;
    @Key("redirect_uri")
    private String redirectUri;
    @Key("response_type")
    private String responseTypes;
    @Key("scope")
    private String scopes;
    @Key
    private String state;

    public AuthorizationRequestUrl(String str, String str2, Iterable<String> iterable) {
        super(str);
        Preconditions.checkArgument(getFragment() == null);
        setClientId(str2);
        setResponseTypes((Iterable) iterable);
    }

    public AuthorizationRequestUrl clone() {
        return (AuthorizationRequestUrl) super.clone();
    }

    public final String getClientId() {
        return this.clientId;
    }

    public final String getRedirectUri() {
        return this.redirectUri;
    }

    public final String getResponseTypes() {
        return this.responseTypes;
    }

    public final String getScopes() {
        return this.scopes;
    }

    public final String getState() {
        return this.state;
    }

    public AuthorizationRequestUrl set(String str, Object obj) {
        return (AuthorizationRequestUrl) super.set(str, obj);
    }

    public AuthorizationRequestUrl setClientId(String str) {
        this.clientId = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public AuthorizationRequestUrl setRedirectUri(String str) {
        this.redirectUri = str;
        return this;
    }

    public AuthorizationRequestUrl setResponseTypes(Iterable<String> iterable) {
        this.responseTypes = Joiner.on(' ').join(iterable);
        return this;
    }

    public AuthorizationRequestUrl setResponseTypes(String... strArr) {
        return setResponseTypes(Arrays.asList(strArr));
    }

    public AuthorizationRequestUrl setScopes(Iterable<String> iterable) {
        this.scopes = iterable == null ? null : Joiner.on(' ').join(iterable);
        return this;
    }

    public AuthorizationRequestUrl setScopes(String... strArr) {
        return setScopes(strArr == null ? null : Arrays.asList(strArr));
    }

    public AuthorizationRequestUrl setState(String str) {
        this.state = str;
        return this;
    }
}
