package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class GoogleClientSecrets extends GenericJson {
    @Key
    private Details installed;
    @Key
    private Details web;

    public static final class Details extends GenericJson {
        @Key("auth_uri")
        private String authUri;
        @Key("client_id")
        private String clientId;
        @Key("client_secret")
        private String clientSecret;
        @Key("redirect_uris")
        private List<String> redirectUris;
        @Key("token_uri")
        private String tokenUri;

        public Details clone() {
            return (Details) super.clone();
        }

        public String getAuthUri() {
            return this.authUri;
        }

        public String getClientId() {
            return this.clientId;
        }

        public String getClientSecret() {
            return this.clientSecret;
        }

        public List<String> getRedirectUris() {
            return this.redirectUris;
        }

        public String getTokenUri() {
            return this.tokenUri;
        }

        public Details set(String str, Object obj) {
            return (Details) super.set(str, obj);
        }

        public Details setAuthUri(String str) {
            this.authUri = str;
            return this;
        }

        public Details setClientId(String str) {
            this.clientId = str;
            return this;
        }

        public Details setClientSecret(String str) {
            this.clientSecret = str;
            return this;
        }

        public Details setRedirectUris(List<String> list) {
            this.redirectUris = list;
            return this;
        }

        public Details setTokenUri(String str) {
            this.tokenUri = str;
            return this;
        }
    }

    public static GoogleClientSecrets load(JsonFactory jsonFactory, InputStream inputStream) throws IOException {
        return (GoogleClientSecrets) jsonFactory.fromInputStream(inputStream, GoogleClientSecrets.class);
    }

    public GoogleClientSecrets clone() {
        return (GoogleClientSecrets) super.clone();
    }

    public Details getDetails() {
        boolean z = false;
        if ((this.web == null) != (this.installed == null)) {
            z = true;
        }
        Preconditions.checkArgument(z);
        return this.web == null ? this.installed : this.web;
    }

    public Details getInstalled() {
        return this.installed;
    }

    public Details getWeb() {
        return this.web;
    }

    public GoogleClientSecrets set(String str, Object obj) {
        return (GoogleClientSecrets) super.set(str, obj);
    }

    public GoogleClientSecrets setInstalled(Details details) {
        this.installed = details;
        return this;
    }

    public GoogleClientSecrets setWeb(Details details) {
        this.web = details;
        return this;
    }
}
