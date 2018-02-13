package com.google.api.client.auth.oauth2;

import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.Arrays;

public class AuthorizationCodeFlow {
    private final String authorizationServerEncodedUrl;
    private final HttpExecuteInterceptor clientAuthentication;
    private final String clientId;
    private final Clock clock;
    private final CredentialCreatedListener credentialCreatedListener;
    private final CredentialStore credentialStore;
    private final JsonFactory jsonFactory;
    private final AccessMethod method;
    private final HttpRequestInitializer requestInitializer;
    private String scopes;
    private final String tokenServerEncodedUrl;
    private final HttpTransport transport;

    public static class Builder {
        String authorizationServerEncodedUrl;
        HttpExecuteInterceptor clientAuthentication;
        String clientId;
        Clock clock = Clock.SYSTEM;
        CredentialCreatedListener credentialCreatedListener;
        CredentialStore credentialStore;
        JsonFactory jsonFactory;
        AccessMethod method;
        HttpRequestInitializer requestInitializer;
        String scopes;
        GenericUrl tokenServerUrl;
        HttpTransport transport;

        public Builder(AccessMethod accessMethod, HttpTransport httpTransport, JsonFactory jsonFactory, GenericUrl genericUrl, HttpExecuteInterceptor httpExecuteInterceptor, String str, String str2) {
            setMethod(accessMethod);
            setTransport(httpTransport);
            setJsonFactory(jsonFactory);
            setTokenServerUrl(genericUrl);
            setClientAuthentication(httpExecuteInterceptor);
            setClientId(str);
            setAuthorizationServerEncodedUrl(str2);
        }

        public AuthorizationCodeFlow build() {
            return new AuthorizationCodeFlow(this);
        }

        public final String getAuthorizationServerEncodedUrl() {
            return this.authorizationServerEncodedUrl;
        }

        public final HttpExecuteInterceptor getClientAuthentication() {
            return this.clientAuthentication;
        }

        public final String getClientId() {
            return this.clientId;
        }

        public final Clock getClock() {
            return this.clock;
        }

        public final CredentialCreatedListener getCredentialCreatedListener() {
            return this.credentialCreatedListener;
        }

        public final CredentialStore getCredentialStore() {
            return this.credentialStore;
        }

        public final JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public final AccessMethod getMethod() {
            return this.method;
        }

        public final HttpRequestInitializer getRequestInitializer() {
            return this.requestInitializer;
        }

        public final String getScopes() {
            return this.scopes;
        }

        public final GenericUrl getTokenServerUrl() {
            return this.tokenServerUrl;
        }

        public final HttpTransport getTransport() {
            return this.transport;
        }

        public Builder setAuthorizationServerEncodedUrl(String str) {
            this.authorizationServerEncodedUrl = (String) Preconditions.checkNotNull(str);
            return this;
        }

        public Builder setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
            this.clientAuthentication = httpExecuteInterceptor;
            return this;
        }

        public Builder setClientId(String str) {
            this.clientId = (String) Preconditions.checkNotNull(str);
            return this;
        }

        public Builder setClock(Clock clock) {
            this.clock = (Clock) Preconditions.checkNotNull(clock);
            return this;
        }

        public Builder setCredentialCreatedListener(CredentialCreatedListener credentialCreatedListener) {
            this.credentialCreatedListener = credentialCreatedListener;
            return this;
        }

        public Builder setCredentialStore(CredentialStore credentialStore) {
            this.credentialStore = credentialStore;
            return this;
        }

        public Builder setJsonFactory(JsonFactory jsonFactory) {
            this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
            return this;
        }

        public Builder setMethod(AccessMethod accessMethod) {
            this.method = (AccessMethod) Preconditions.checkNotNull(accessMethod);
            return this;
        }

        public Builder setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            this.requestInitializer = httpRequestInitializer;
            return this;
        }

        public Builder setScopes(Iterable<String> iterable) {
            this.scopes = iterable == null ? null : Joiner.on(' ').join(iterable);
            return this;
        }

        public Builder setScopes(String... strArr) {
            return setScopes(strArr == null ? null : Arrays.asList(strArr));
        }

        public Builder setTokenServerUrl(GenericUrl genericUrl) {
            this.tokenServerUrl = (GenericUrl) Preconditions.checkNotNull(genericUrl);
            return this;
        }

        public Builder setTransport(HttpTransport httpTransport) {
            this.transport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
            return this;
        }
    }

    public interface CredentialCreatedListener {
        void onCredentialCreated(Credential credential, TokenResponse tokenResponse) throws IOException;
    }

    protected AuthorizationCodeFlow(Builder builder) {
        this.method = (AccessMethod) Preconditions.checkNotNull(builder.method);
        this.transport = (HttpTransport) Preconditions.checkNotNull(builder.transport);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(builder.jsonFactory);
        this.tokenServerEncodedUrl = ((GenericUrl) Preconditions.checkNotNull(builder.tokenServerUrl)).build();
        this.clientAuthentication = builder.clientAuthentication;
        this.clientId = (String) Preconditions.checkNotNull(builder.clientId);
        this.authorizationServerEncodedUrl = (String) Preconditions.checkNotNull(builder.authorizationServerEncodedUrl);
        this.requestInitializer = builder.requestInitializer;
        this.credentialStore = builder.credentialStore;
        this.scopes = builder.scopes;
        this.clock = (Clock) Preconditions.checkNotNull(builder.clock);
        this.credentialCreatedListener = builder.credentialCreatedListener;
    }

    public AuthorizationCodeFlow(AccessMethod accessMethod, HttpTransport httpTransport, JsonFactory jsonFactory, GenericUrl genericUrl, HttpExecuteInterceptor httpExecuteInterceptor, String str, String str2) {
        this(new Builder(accessMethod, httpTransport, jsonFactory, genericUrl, httpExecuteInterceptor, str, str2));
    }

    @Deprecated
    protected AuthorizationCodeFlow(AccessMethod accessMethod, HttpTransport httpTransport, JsonFactory jsonFactory, GenericUrl genericUrl, HttpExecuteInterceptor httpExecuteInterceptor, String str, String str2, CredentialStore credentialStore, HttpRequestInitializer httpRequestInitializer, String str3) {
        this(accessMethod, httpTransport, jsonFactory, genericUrl, httpExecuteInterceptor, str, str2, credentialStore, httpRequestInitializer, str3, Clock.SYSTEM);
    }

    @Deprecated
    protected AuthorizationCodeFlow(AccessMethod accessMethod, HttpTransport httpTransport, JsonFactory jsonFactory, GenericUrl genericUrl, HttpExecuteInterceptor httpExecuteInterceptor, String str, String str2, CredentialStore credentialStore, HttpRequestInitializer httpRequestInitializer, String str3, Clock clock) {
        this.method = (AccessMethod) Preconditions.checkNotNull(accessMethod);
        this.transport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        this.tokenServerEncodedUrl = ((GenericUrl) Preconditions.checkNotNull(genericUrl)).build();
        this.clientAuthentication = httpExecuteInterceptor;
        this.clientId = (String) Preconditions.checkNotNull(str);
        this.authorizationServerEncodedUrl = (String) Preconditions.checkNotNull(str2);
        this.requestInitializer = httpRequestInitializer;
        this.credentialStore = credentialStore;
        this.scopes = str3;
        this.clock = (Clock) Preconditions.checkNotNull(clock);
        this.credentialCreatedListener = null;
    }

    private Credential newCredential(String str) {
        com.google.api.client.auth.oauth2.Credential.Builder clock = new com.google.api.client.auth.oauth2.Credential.Builder(this.method).setTransport(this.transport).setJsonFactory(this.jsonFactory).setTokenServerEncodedUrl(this.tokenServerEncodedUrl).setClientAuthentication(this.clientAuthentication).setRequestInitializer(this.requestInitializer).setClock(this.clock);
        if (this.credentialStore != null) {
            clock.addRefreshListener(new CredentialStoreRefreshListener(str, this.credentialStore));
        }
        return clock.build();
    }

    public Credential createAndStoreCredential(TokenResponse tokenResponse, String str) throws IOException {
        Credential fromTokenResponse = newCredential(str).setFromTokenResponse(tokenResponse);
        if (this.credentialStore != null) {
            this.credentialStore.store(str, fromTokenResponse);
        }
        if (this.credentialCreatedListener != null) {
            this.credentialCreatedListener.onCredentialCreated(fromTokenResponse, tokenResponse);
        }
        return fromTokenResponse;
    }

    public final String getAuthorizationServerEncodedUrl() {
        return this.authorizationServerEncodedUrl;
    }

    public final HttpExecuteInterceptor getClientAuthentication() {
        return this.clientAuthentication;
    }

    public final String getClientId() {
        return this.clientId;
    }

    public final Clock getClock() {
        return this.clock;
    }

    public final CredentialStore getCredentialStore() {
        return this.credentialStore;
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public final AccessMethod getMethod() {
        return this.method;
    }

    public final HttpRequestInitializer getRequestInitializer() {
        return this.requestInitializer;
    }

    public final String getScopes() {
        return this.scopes;
    }

    public final String getTokenServerEncodedUrl() {
        return this.tokenServerEncodedUrl;
    }

    public final HttpTransport getTransport() {
        return this.transport;
    }

    public Credential loadCredential(String str) throws IOException {
        if (this.credentialStore == null) {
            return null;
        }
        Credential newCredential = newCredential(str);
        return this.credentialStore.load(str, newCredential) ? newCredential : null;
    }

    public AuthorizationCodeRequestUrl newAuthorizationUrl() {
        return new AuthorizationCodeRequestUrl(this.authorizationServerEncodedUrl, this.clientId).setScopes(this.scopes);
    }

    public AuthorizationCodeTokenRequest newTokenRequest(String str) {
        return new AuthorizationCodeTokenRequest(this.transport, this.jsonFactory, new GenericUrl(this.tokenServerEncodedUrl), str).setClientAuthentication(this.clientAuthentication).setRequestInitializer(this.requestInitializer).setScopes(this.scopes);
    }
}
