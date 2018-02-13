package com.google.api.client.googleapis.services.json;

import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.sony.rdis.receiver.ServiceComuncationProtocol;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public abstract class AbstractGoogleJsonClient extends AbstractGoogleClient {

    public static abstract class Builder extends com.google.api.client.googleapis.services.AbstractGoogleClient.Builder {
        protected Builder(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, HttpRequestInitializer httpRequestInitializer, boolean z) {
            Collection asList;
            com.google.api.client.json.JsonObjectParser.Builder builder = new com.google.api.client.json.JsonObjectParser.Builder(jsonFactory);
            if (z) {
                asList = Arrays.asList(new String[]{ServiceComuncationProtocol.DATA, "error"});
            } else {
                asList = Collections.emptySet();
            }
            super(httpTransport, str, str2, builder.setWrapperKeys(asList).build(), httpRequestInitializer);
        }

        public abstract AbstractGoogleJsonClient build();

        public final JsonFactory getJsonFactory() {
            return getObjectParser().getJsonFactory();
        }

        public final JsonObjectParser getObjectParser() {
            return (JsonObjectParser) super.getObjectParser();
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

        public Builder setSuppressRequiredParameterChecks(boolean z) {
            return (Builder) super.setSuppressRequiredParameterChecks(z);
        }
    }

    protected AbstractGoogleJsonClient(Builder builder) {
        super(builder);
    }

    @Deprecated
    protected AbstractGoogleJsonClient(HttpTransport httpTransport, HttpRequestInitializer httpRequestInitializer, String str, String str2, JsonObjectParser jsonObjectParser, GoogleClientRequestInitializer googleClientRequestInitializer, String str3, boolean z) {
        super(httpTransport, httpRequestInitializer, str, str2, jsonObjectParser, googleClientRequestInitializer, str3, z);
    }

    @Deprecated
    protected AbstractGoogleJsonClient(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, HttpRequestInitializer httpRequestInitializer, boolean z) {
        super(httpTransport, httpRequestInitializer, str, str2, z ? new JsonCParser(jsonFactory) : new JsonObjectParser(jsonFactory));
    }

    public final JsonFactory getJsonFactory() {
        return getObjectParser().getJsonFactory();
    }

    public JsonObjectParser getObjectParser() {
        return (JsonObjectParser) super.getObjectParser();
    }
}
