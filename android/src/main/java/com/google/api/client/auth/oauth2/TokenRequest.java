package com.google.api.client.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.Arrays;

public class TokenRequest extends GenericData {
    HttpExecuteInterceptor clientAuthentication;
    @Key("grant_type")
    private String grantType;
    private final JsonFactory jsonFactory;
    HttpRequestInitializer requestInitializer;
    @Key("scope")
    private String scopes;
    private GenericUrl tokenServerUrl;
    private final HttpTransport transport;

    class C04461 implements HttpRequestInitializer {
        C04461() {
        }

        public void initialize(HttpRequest httpRequest) throws IOException {
            if (TokenRequest.this.requestInitializer != null) {
                TokenRequest.this.requestInitializer.initialize(httpRequest);
            }
            final HttpExecuteInterceptor interceptor = httpRequest.getInterceptor();
            httpRequest.setInterceptor(new HttpExecuteInterceptor() {
                public void intercept(HttpRequest httpRequest) throws IOException {
                    if (interceptor != null) {
                        interceptor.intercept(httpRequest);
                    }
                    if (TokenRequest.this.clientAuthentication != null) {
                        TokenRequest.this.clientAuthentication.intercept(httpRequest);
                    }
                }
            });
        }
    }

    public TokenRequest(HttpTransport httpTransport, JsonFactory jsonFactory, GenericUrl genericUrl, String str) {
        this.transport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        setTokenServerUrl(genericUrl);
        setGrantType(str);
    }

    public TokenResponse execute() throws IOException {
        return (TokenResponse) executeUnparsed().parseAs(TokenResponse.class);
    }

    public final HttpResponse executeUnparsed() throws IOException {
        HttpRequest buildPostRequest = this.transport.createRequestFactory(new C04461()).buildPostRequest(this.tokenServerUrl, new UrlEncodedContent(this));
        buildPostRequest.setParser(new JsonObjectParser(this.jsonFactory));
        buildPostRequest.setThrowExceptionOnExecuteError(false);
        HttpResponse execute = buildPostRequest.execute();
        if (execute.isSuccessStatusCode()) {
            return execute;
        }
        throw TokenResponseException.from(this.jsonFactory, execute);
    }

    public final HttpExecuteInterceptor getClientAuthentication() {
        return this.clientAuthentication;
    }

    public final String getGrantType() {
        return this.grantType;
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
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

    public TokenRequest set(String str, Object obj) {
        return (TokenRequest) super.set(str, obj);
    }

    public TokenRequest setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
        this.clientAuthentication = httpExecuteInterceptor;
        return this;
    }

    public TokenRequest setGrantType(String str) {
        this.grantType = (String) Preconditions.checkNotNull(str);
        return this;
    }

    public TokenRequest setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
        this.requestInitializer = httpRequestInitializer;
        return this;
    }

    public TokenRequest setScopes(Iterable<String> iterable) {
        this.scopes = iterable == null ? null : Joiner.on(' ').join(iterable);
        return this;
    }

    public TokenRequest setScopes(String... strArr) {
        return setScopes(strArr == null ? null : Arrays.asList(strArr));
    }

    public TokenRequest setTokenServerUrl(GenericUrl genericUrl) {
        this.tokenServerUrl = genericUrl;
        Preconditions.checkArgument(genericUrl.getFragment() == null);
        return this;
    }
}
