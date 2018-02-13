package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Preconditions;
import java.util.Collections;
import org.mortbay.jetty.HttpVersions;

public class GoogleAuthorizationCodeFlow extends AuthorizationCodeFlow {
    private final String accessType;
    private final String approvalPrompt;

    public static class Builder extends com.google.api.client.auth.oauth2.AuthorizationCodeFlow.Builder {
        String accessType;
        String approvalPrompt;

        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory, GoogleClientSecrets googleClientSecrets, Iterable<String> iterable) {
            super(BearerToken.authorizationHeaderAccessMethod(), httpTransport, jsonFactory, new GenericUrl(GoogleOAuthConstants.TOKEN_SERVER_URL), new ClientParametersAuthentication(googleClientSecrets.getDetails().getClientId(), googleClientSecrets.getDetails().getClientSecret()), googleClientSecrets.getDetails().getClientId(), GoogleOAuthConstants.AUTHORIZATION_SERVER_URL);
            setScopes((Iterable) Preconditions.checkNotNull(iterable));
        }

        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, Iterable<String> iterable) {
            super(BearerToken.authorizationHeaderAccessMethod(), httpTransport, jsonFactory, new GenericUrl(GoogleOAuthConstants.TOKEN_SERVER_URL), new ClientParametersAuthentication(str, str2), str, GoogleOAuthConstants.AUTHORIZATION_SERVER_URL);
            setScopes((Iterable) Preconditions.checkNotNull(iterable));
        }

        public GoogleAuthorizationCodeFlow build() {
            return new GoogleAuthorizationCodeFlow(this);
        }

        public final String getAccessType() {
            return this.accessType;
        }

        public final String getApprovalPrompt() {
            return this.approvalPrompt;
        }

        public Builder setAccessType(String str) {
            this.accessType = str;
            return this;
        }

        public Builder setApprovalPrompt(String str) {
            this.approvalPrompt = str;
            return this;
        }

        public Builder setAuthorizationServerEncodedUrl(String str) {
            return (Builder) super.setAuthorizationServerEncodedUrl(str);
        }

        public Builder setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
            return (Builder) super.setClientAuthentication(httpExecuteInterceptor);
        }

        public Builder setClientId(String str) {
            return (Builder) super.setClientId(str);
        }

        public Builder setClock(Clock clock) {
            return (Builder) super.setClock(clock);
        }

        public Builder setCredentialStore(CredentialStore credentialStore) {
            return (Builder) super.setCredentialStore(credentialStore);
        }

        public Builder setJsonFactory(JsonFactory jsonFactory) {
            return (Builder) super.setJsonFactory(jsonFactory);
        }

        public Builder setMethod(AccessMethod accessMethod) {
            return (Builder) super.setMethod(accessMethod);
        }

        public Builder setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            return (Builder) super.setRequestInitializer(httpRequestInitializer);
        }

        public Builder setScopes(Iterable<String> iterable) {
            return (Builder) super.setScopes((Iterable) iterable);
        }

        public Builder setScopes(String... strArr) {
            return (Builder) super.setScopes(strArr);
        }

        public Builder setTokenServerUrl(GenericUrl genericUrl) {
            return (Builder) super.setTokenServerUrl(genericUrl);
        }

        public Builder setTransport(HttpTransport httpTransport) {
            return (Builder) super.setTransport(httpTransport);
        }
    }

    @Deprecated
    protected GoogleAuthorizationCodeFlow(AccessMethod accessMethod, HttpTransport httpTransport, JsonFactory jsonFactory, GenericUrl genericUrl, HttpExecuteInterceptor httpExecuteInterceptor, String str, String str2, CredentialStore credentialStore, HttpRequestInitializer httpRequestInitializer, String str3, String str4, String str5) {
        super(accessMethod, httpTransport, jsonFactory, genericUrl, httpExecuteInterceptor, str, str2, credentialStore, httpRequestInitializer, str3);
        this.accessType = str4;
        this.approvalPrompt = str5;
    }

    protected GoogleAuthorizationCodeFlow(Builder builder) {
        super(builder);
        this.accessType = builder.accessType;
        this.approvalPrompt = builder.approvalPrompt;
    }

    public GoogleAuthorizationCodeFlow(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, Iterable<String> iterable) {
        this(new Builder(httpTransport, jsonFactory, str, str2, iterable));
    }

    public final String getAccessType() {
        return this.accessType;
    }

    public final String getApprovalPrompt() {
        return this.approvalPrompt;
    }

    public GoogleAuthorizationCodeRequestUrl newAuthorizationUrl() {
        return new GoogleAuthorizationCodeRequestUrl(getAuthorizationServerEncodedUrl(), getClientId(), HttpVersions.HTTP_0_9, Collections.singleton(getScopes())).setAccessType(this.accessType).setApprovalPrompt(this.approvalPrompt);
    }

    public GoogleAuthorizationCodeTokenRequest newTokenRequest(String str) {
        return new GoogleAuthorizationCodeTokenRequest(getTransport(), getJsonFactory(), getTokenServerEncodedUrl(), HttpVersions.HTTP_0_9, HttpVersions.HTTP_0_9, str, HttpVersions.HTTP_0_9).setClientAuthentication(getClientAuthentication()).setRequestInitializer(getRequestInitializer()).setScopes(new String[]{getScopes()});
    }
}
