package com.google.api.client.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

public class RefreshTokenRequest extends TokenRequest {
    @Key("refresh_token")
    private String refreshToken;

    public RefreshTokenRequest(HttpTransport httpTransport, JsonFactory jsonFactory, GenericUrl genericUrl, String str) {
        super(httpTransport, jsonFactory, genericUrl, "refresh_token");
        setRefreshToken(str);
    }

    public final String getRefreshToken() {
        return this.refreshToken;
    }

    public RefreshTokenRequest set(String str, Object obj) {
        return (RefreshTokenRequest) super.set(str, obj);
    }

    public RefreshTokenRequest setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
        return (RefreshTokenRequest) super.setClientAuthentication(httpExecuteInterceptor);
    }

    public RefreshTokenRequest setGrantType(String str) {
        return (RefreshTokenRequest) super.setGrantType(str);
    }

    public RefreshTokenRequest setRefreshToken(String str) {
        this.refreshToken = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public RefreshTokenRequest setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
        return (RefreshTokenRequest) super.setRequestInitializer(httpRequestInitializer);
    }

    public RefreshTokenRequest setScopes(Iterable<String> iterable) {
        return (RefreshTokenRequest) super.setScopes((Iterable) iterable);
    }

    public RefreshTokenRequest setScopes(String... strArr) {
        return (RefreshTokenRequest) super.setScopes(strArr);
    }

    public RefreshTokenRequest setTokenServerUrl(GenericUrl genericUrl) {
        return (RefreshTokenRequest) super.setTokenServerUrl(genericUrl);
    }
}
