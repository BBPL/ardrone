package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import java.io.IOException;

public class GoogleRefreshTokenRequest extends RefreshTokenRequest {
    public GoogleRefreshTokenRequest(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, String str3) {
        super(httpTransport, jsonFactory, new GenericUrl(GoogleOAuthConstants.TOKEN_SERVER_URL), str);
        setClientAuthentication(new ClientParametersAuthentication(str2, str3));
    }

    public GoogleTokenResponse execute() throws IOException {
        return (GoogleTokenResponse) executeUnparsed().parseAs(GoogleTokenResponse.class);
    }

    public GoogleRefreshTokenRequest set(String str, Object obj) {
        return (GoogleRefreshTokenRequest) super.set(str, obj);
    }

    public GoogleRefreshTokenRequest setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
        return (GoogleRefreshTokenRequest) super.setClientAuthentication(httpExecuteInterceptor);
    }

    public GoogleRefreshTokenRequest setGrantType(String str) {
        return (GoogleRefreshTokenRequest) super.setGrantType(str);
    }

    public GoogleRefreshTokenRequest setRefreshToken(String str) {
        return (GoogleRefreshTokenRequest) super.setRefreshToken(str);
    }

    public GoogleRefreshTokenRequest setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
        return (GoogleRefreshTokenRequest) super.setRequestInitializer(httpRequestInitializer);
    }

    public GoogleRefreshTokenRequest setScopes(Iterable<String> iterable) {
        return (GoogleRefreshTokenRequest) super.setScopes((Iterable) iterable);
    }

    public GoogleRefreshTokenRequest setScopes(String... strArr) {
        return (GoogleRefreshTokenRequest) super.setScopes(strArr);
    }

    public GoogleRefreshTokenRequest setTokenServerUrl(GenericUrl genericUrl) {
        return (GoogleRefreshTokenRequest) super.setTokenServerUrl(genericUrl);
    }
}
