package com.google.api.client.googleapis.testing.services;

import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.ObjectParser;

public class MockGoogleClient extends AbstractGoogleClient {

    public static class Builder extends com.google.api.client.googleapis.services.AbstractGoogleClient.Builder {
        public Builder(HttpTransport httpTransport, String str, String str2, ObjectParser objectParser, HttpRequestInitializer httpRequestInitializer) {
            super(httpTransport, str, str2, objectParser, httpRequestInitializer);
        }

        public MockGoogleClient build() {
            return new MockGoogleClient(this);
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

    protected MockGoogleClient(Builder builder) {
        super(builder);
    }

    @Deprecated
    public MockGoogleClient(HttpTransport httpTransport, HttpRequestInitializer httpRequestInitializer, String str, String str2, ObjectParser objectParser) {
        super(httpTransport, httpRequestInitializer, str, str2, objectParser);
    }

    @Deprecated
    public MockGoogleClient(HttpTransport httpTransport, HttpRequestInitializer httpRequestInitializer, String str, String str2, ObjectParser objectParser, GoogleClientRequestInitializer googleClientRequestInitializer, String str3, boolean z) {
        super(httpTransport, httpRequestInitializer, str, str2, objectParser, googleClientRequestInitializer, str3, z);
    }

    public MockGoogleClient(HttpTransport httpTransport, String str, String str2, ObjectParser objectParser, HttpRequestInitializer httpRequestInitializer) {
        this(new Builder(httpTransport, str, str2, objectParser, httpRequestInitializer));
    }
}
