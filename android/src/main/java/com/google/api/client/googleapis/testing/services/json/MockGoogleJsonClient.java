package com.google.api.client.googleapis.testing.services.json;

import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;

public class MockGoogleJsonClient extends AbstractGoogleJsonClient {

    public static class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {
        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, HttpRequestInitializer httpRequestInitializer, boolean z) {
            super(httpTransport, jsonFactory, str, str2, httpRequestInitializer, z);
        }

        public MockGoogleJsonClient build() {
            return new MockGoogleJsonClient(this);
        }

        public Builder setApplicationName(String str) {
            return (Builder) super.setApplicationName(str);
        }

        public Builder setGoogleClientRequestInitializer(GoogleClientRequestInitializer googleClientRequestInitializer) {
            return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
        }

        public Builder setHttpRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
        }

        public Builder setRootUrl(String str) {
            return (Builder) super.setRootUrl(str);
        }

        public Builder setServicePath(String str) {
            return (Builder) super.setServicePath(str);
        }

        public Builder setSuppressPatternChecks(boolean z) {
            return (Builder) super.setSuppressPatternChecks(z);
        }
    }

    protected MockGoogleJsonClient(Builder builder) {
        super(builder);
    }

    @Deprecated
    public MockGoogleJsonClient(HttpTransport httpTransport, HttpRequestInitializer httpRequestInitializer, String str, String str2, JsonObjectParser jsonObjectParser, GoogleClientRequestInitializer googleClientRequestInitializer, String str3, boolean z) {
        super(httpTransport, httpRequestInitializer, str, str2, jsonObjectParser, googleClientRequestInitializer, str3, z);
    }

    public MockGoogleJsonClient(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, HttpRequestInitializer httpRequestInitializer, boolean z) {
        this(new Builder(httpTransport, jsonFactory, str, str2, httpRequestInitializer, z));
    }
}
