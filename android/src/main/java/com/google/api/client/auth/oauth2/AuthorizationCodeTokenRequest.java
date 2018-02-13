package com.google.api.client.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

public class AuthorizationCodeTokenRequest extends TokenRequest {
    @Key
    private String code;
    @Key("redirect_uri")
    private String redirectUri;

    public AuthorizationCodeTokenRequest(HttpTransport httpTransport, JsonFactory jsonFactory, GenericUrl genericUrl, String str) {
        super(httpTransport, jsonFactory, genericUrl, "authorization_code");
        setCode(str);
    }

    public final String getCode() {
        return this.code;
    }

    public final String getRedirectUri() {
        return this.redirectUri;
    }

    public AuthorizationCodeTokenRequest set(String str, Object obj) {
        return (AuthorizationCodeTokenRequest) super.set(str, obj);
    }

    public AuthorizationCodeTokenRequest setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
        return (AuthorizationCodeTokenRequest) super.setClientAuthentication(httpExecuteInterceptor);
    }

    public AuthorizationCodeTokenRequest setCode(String str) {
        this.code = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public AuthorizationCodeTokenRequest setGrantType(String str) {
        return (AuthorizationCodeTokenRequest) super.setGrantType(str);
    }

    public AuthorizationCodeTokenRequest setRedirectUri(String str) {
        this.redirectUri = str;
        return this;
    }

    public AuthorizationCodeTokenRequest setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
        return (AuthorizationCodeTokenRequest) super.setRequestInitializer(httpRequestInitializer);
    }

    public AuthorizationCodeTokenRequest setScopes(Iterable<String> iterable) {
        return (AuthorizationCodeTokenRequest) super.setScopes((Iterable) iterable);
    }

    public AuthorizationCodeTokenRequest setScopes(String... strArr) {
        return (AuthorizationCodeTokenRequest) super.setScopes(strArr);
    }

    public AuthorizationCodeTokenRequest setTokenServerUrl(GenericUrl genericUrl) {
        return (AuthorizationCodeTokenRequest) super.setTokenServerUrl(genericUrl);
    }
}
