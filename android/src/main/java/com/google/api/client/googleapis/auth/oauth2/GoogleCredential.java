package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebSignature.Header;
import com.google.api.client.json.webtoken.JsonWebToken.Payload;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.PemReader;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.SecurityUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.List;

public class GoogleCredential extends Credential {
    private String serviceAccountId;
    private PrivateKey serviceAccountPrivateKey;
    private String serviceAccountScopes;
    private String serviceAccountUser;

    public static class Builder extends com.google.api.client.auth.oauth2.Credential.Builder {
        String serviceAccountId;
        PrivateKey serviceAccountPrivateKey;
        String serviceAccountScopes;
        String serviceAccountUser;

        public Builder() {
            super(BearerToken.authorizationHeaderAccessMethod());
            setTokenServerEncodedUrl(GoogleOAuthConstants.TOKEN_SERVER_URL);
        }

        public Builder addRefreshListener(CredentialRefreshListener credentialRefreshListener) {
            return (Builder) super.addRefreshListener(credentialRefreshListener);
        }

        public GoogleCredential build() {
            return new GoogleCredential(this);
        }

        public final String getServiceAccountId() {
            return this.serviceAccountId;
        }

        public final PrivateKey getServiceAccountPrivateKey() {
            return this.serviceAccountPrivateKey;
        }

        public final String getServiceAccountScopes() {
            return this.serviceAccountScopes;
        }

        public final String getServiceAccountUser() {
            return this.serviceAccountUser;
        }

        public Builder setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
            return (Builder) super.setClientAuthentication(httpExecuteInterceptor);
        }

        public Builder setClientSecrets(GoogleClientSecrets googleClientSecrets) {
            Details details = googleClientSecrets.getDetails();
            setClientAuthentication(new ClientParametersAuthentication(details.getClientId(), details.getClientSecret()));
            return this;
        }

        public Builder setClientSecrets(String str, String str2) {
            setClientAuthentication(new ClientParametersAuthentication(str, str2));
            return this;
        }

        public Builder setClock(Clock clock) {
            return (Builder) super.setClock(clock);
        }

        public Builder setJsonFactory(JsonFactory jsonFactory) {
            return (Builder) super.setJsonFactory(jsonFactory);
        }

        public Builder setRefreshListeners(List<CredentialRefreshListener> list) {
            return (Builder) super.setRefreshListeners(list);
        }

        public Builder setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            return (Builder) super.setRequestInitializer(httpRequestInitializer);
        }

        public Builder setServiceAccountId(String str) {
            this.serviceAccountId = str;
            return this;
        }

        public Builder setServiceAccountPrivateKey(PrivateKey privateKey) {
            this.serviceAccountPrivateKey = privateKey;
            return this;
        }

        public Builder setServiceAccountPrivateKeyFromP12File(File file) throws GeneralSecurityException, IOException {
            this.serviceAccountPrivateKey = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(), new FileInputStream(file), "notasecret", "privatekey", "notasecret");
            return this;
        }

        public Builder setServiceAccountPrivateKeyFromPemFile(File file) throws GeneralSecurityException, IOException {
            this.serviceAccountPrivateKey = SecurityUtils.getRsaKeyFactory().generatePrivate(new PKCS8EncodedKeySpec(PemReader.readFirstSectionAndClose(new FileReader(file), "PRIVATE KEY").getBase64DecodedBytes()));
            return this;
        }

        public Builder setServiceAccountScopes(Iterable<String> iterable) {
            this.serviceAccountScopes = iterable == null ? null : Joiner.on(' ').join(iterable);
            return this;
        }

        public Builder setServiceAccountScopes(String... strArr) {
            return setServiceAccountScopes(strArr == null ? null : Arrays.asList(strArr));
        }

        public Builder setServiceAccountUser(String str) {
            this.serviceAccountUser = str;
            return this;
        }

        public Builder setTokenServerEncodedUrl(String str) {
            return (Builder) super.setTokenServerEncodedUrl(str);
        }

        public Builder setTokenServerUrl(GenericUrl genericUrl) {
            return (Builder) super.setTokenServerUrl(genericUrl);
        }

        public Builder setTransport(HttpTransport httpTransport) {
            return (Builder) super.setTransport(httpTransport);
        }
    }

    public GoogleCredential() {
        this(new Builder());
    }

    @Deprecated
    protected GoogleCredential(AccessMethod accessMethod, HttpTransport httpTransport, JsonFactory jsonFactory, String str, HttpExecuteInterceptor httpExecuteInterceptor, HttpRequestInitializer httpRequestInitializer, List<CredentialRefreshListener> list, String str2, String str3, PrivateKey privateKey, String str4) {
        this(accessMethod, httpTransport, jsonFactory, str, httpExecuteInterceptor, httpRequestInitializer, list, str2, str3, privateKey, str4, Clock.SYSTEM);
    }

    @Deprecated
    protected GoogleCredential(AccessMethod accessMethod, HttpTransport httpTransport, JsonFactory jsonFactory, String str, HttpExecuteInterceptor httpExecuteInterceptor, HttpRequestInitializer httpRequestInitializer, List<CredentialRefreshListener> list, String str2, String str3, PrivateKey privateKey, String str4, Clock clock) {
        super(accessMethod, httpTransport, jsonFactory, str, httpExecuteInterceptor, httpRequestInitializer, list, clock);
        if (privateKey == null) {
            boolean z = str2 == null && str3 == null && str4 == null;
            Preconditions.checkArgument(z);
            return;
        }
        this.serviceAccountId = (String) Preconditions.checkNotNull(str2);
        this.serviceAccountScopes = (String) Preconditions.checkNotNull(str3);
        this.serviceAccountPrivateKey = privateKey;
        this.serviceAccountUser = str4;
    }

    protected GoogleCredential(Builder builder) {
        super((com.google.api.client.auth.oauth2.Credential.Builder) builder);
        if (builder.serviceAccountPrivateKey == null) {
            boolean z = builder.serviceAccountId == null && builder.serviceAccountScopes == null && builder.serviceAccountUser == null;
            Preconditions.checkArgument(z);
            return;
        }
        this.serviceAccountId = (String) Preconditions.checkNotNull(builder.serviceAccountId);
        this.serviceAccountScopes = (String) Preconditions.checkNotNull(builder.serviceAccountScopes);
        this.serviceAccountPrivateKey = builder.serviceAccountPrivateKey;
        this.serviceAccountUser = builder.serviceAccountUser;
    }

    protected TokenResponse executeRefreshToken() throws IOException {
        if (this.serviceAccountPrivateKey == null) {
            return super.executeRefreshToken();
        }
        Header header = new Header();
        header.setAlgorithm("RS256");
        header.setType("JWT");
        Payload payload = new Payload();
        long currentTimeMillis = getClock().currentTimeMillis();
        payload.setIssuer(this.serviceAccountId);
        payload.setAudience(getTokenServerEncodedUrl());
        payload.setIssuedAtTimeSeconds(Long.valueOf(currentTimeMillis / 1000));
        payload.setExpirationTimeSeconds(Long.valueOf((currentTimeMillis / 1000) + 3600));
        payload.setSubject(this.serviceAccountUser);
        payload.put("scope", (Object) this.serviceAccountScopes);
        try {
            String signUsingRsaSha256 = JsonWebSignature.signUsingRsaSha256(this.serviceAccountPrivateKey, getJsonFactory(), header, payload);
            TokenRequest tokenRequest = new TokenRequest(getTransport(), getJsonFactory(), new GenericUrl(getTokenServerEncodedUrl()), "urn:ietf:params:oauth:grant-type:jwt-bearer");
            tokenRequest.put("assertion", (Object) signUsingRsaSha256);
            return tokenRequest.execute();
        } catch (Throwable e) {
            IOException iOException = new IOException();
            iOException.initCause(e);
            throw iOException;
        }
    }

    public final String getServiceAccountId() {
        return this.serviceAccountId;
    }

    public final PrivateKey getServiceAccountPrivateKey() {
        return this.serviceAccountPrivateKey;
    }

    public final String getServiceAccountScopes() {
        return this.serviceAccountScopes;
    }

    public final String getServiceAccountUser() {
        return this.serviceAccountUser;
    }

    public GoogleCredential setAccessToken(String str) {
        return (GoogleCredential) super.setAccessToken(str);
    }

    public GoogleCredential setExpirationTimeMilliseconds(Long l) {
        return (GoogleCredential) super.setExpirationTimeMilliseconds(l);
    }

    public GoogleCredential setExpiresInSeconds(Long l) {
        return (GoogleCredential) super.setExpiresInSeconds(l);
    }

    public GoogleCredential setFromTokenResponse(TokenResponse tokenResponse) {
        return (GoogleCredential) super.setFromTokenResponse(tokenResponse);
    }

    public GoogleCredential setRefreshToken(String str) {
        if (str != null) {
            boolean z = (getJsonFactory() == null || getTransport() == null || getClientAuthentication() == null) ? false : true;
            Preconditions.checkArgument(z, "Please use the Builder and call setJsonFactory, setTransport and setClientSecrets");
        }
        return (GoogleCredential) super.setRefreshToken(str);
    }
}
