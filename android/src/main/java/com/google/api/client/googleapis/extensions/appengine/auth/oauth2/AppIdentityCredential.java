package com.google.api.client.googleapis.extensions.appengine.auth.oauth2;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.Lists;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AppIdentityCredential implements HttpRequestInitializer, HttpExecuteInterceptor {
    private final AppIdentityService appIdentityService;
    private final List<String> scopes;

    public static class Builder {
        AppIdentityService appIdentityService;
        final List<String> scopes;

        public Builder(Iterable<String> iterable) {
            this.scopes = Collections.unmodifiableList(Lists.newArrayList((Iterable) iterable));
        }

        public Builder(String... strArr) {
            this(Arrays.asList(strArr));
        }

        public AppIdentityCredential build() {
            return new AppIdentityCredential(this);
        }

        public final AppIdentityService getAppIdentityService() {
            return this.appIdentityService;
        }

        public final List<String> getScopes() {
            return this.scopes;
        }

        public Builder setAppIdentityService(AppIdentityService appIdentityService) {
            this.appIdentityService = appIdentityService;
            return this;
        }
    }

    protected AppIdentityCredential(Builder builder) {
        this.appIdentityService = builder.appIdentityService == null ? AppIdentityServiceFactory.getAppIdentityService() : builder.appIdentityService;
        this.scopes = builder.scopes;
    }

    @Deprecated
    protected AppIdentityCredential(AppIdentityService appIdentityService, List<String> list) {
        if (appIdentityService == null) {
            appIdentityService = AppIdentityServiceFactory.getAppIdentityService();
        }
        this.appIdentityService = appIdentityService;
        this.scopes = Lists.newArrayList((Iterable) list);
    }

    public AppIdentityCredential(Iterable<String> iterable) {
        this(new Builder((Iterable) iterable));
    }

    public AppIdentityCredential(String... strArr) {
        this(new Builder(strArr));
    }

    public final AppIdentityService getAppIdentityService() {
        return this.appIdentityService;
    }

    public final List<String> getScopes() {
        return this.scopes;
    }

    public void initialize(HttpRequest httpRequest) throws IOException {
        httpRequest.setInterceptor(this);
    }

    public void intercept(HttpRequest httpRequest) throws IOException {
        BearerToken.authorizationHeaderAccessMethod().intercept(httpRequest, this.appIdentityService.getAccessToken(this.scopes).getAccessToken());
    }
}
