package com.google.api.client.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class ClientCredentialsTokenRequest extends TokenRequest {
    public ClientCredentialsTokenRequest(HttpTransport httpTransport, JsonFactory jsonFactory, GenericUrl genericUrl) {
        super(httpTransport, jsonFactory, genericUrl, "client_credentials");
    }

    public ClientCredentialsTokenRequest set(String str, Object obj) {
        return (ClientCredentialsTokenRequest) super.set(str, obj);
    }

    public ClientCredentialsTokenRequest setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
        return (ClientCredentialsTokenRequest) super.setClientAuthentication(httpExecuteInterceptor);
    }

    public ClientCredentialsTokenRequest setGrantType(String str) {
        return (ClientCredentialsTokenRequest) super.setGrantType(str);
    }

    public ClientCredentialsTokenRequest setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
        return (ClientCredentialsTokenRequest) super.setRequestInitializer(httpRequestInitializer);
    }

    public ClientCredentialsTokenRequest setScopes(Iterable<String> iterable) {
        return (ClientCredentialsTokenRequest) super.setScopes((Iterable) iterable);
    }

    public ClientCredentialsTokenRequest setScopes(String... strArr) {
        return (ClientCredentialsTokenRequest) super.setScopes(strArr);
    }

    public ClientCredentialsTokenRequest setTokenServerUrl(GenericUrl genericUrl) {
        return (ClientCredentialsTokenRequest) super.setTokenServerUrl(genericUrl);
    }
}
