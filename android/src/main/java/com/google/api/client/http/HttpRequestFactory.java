package com.google.api.client.http;

import java.io.IOException;

public final class HttpRequestFactory {
    private final HttpRequestInitializer initializer;
    private final HttpTransport transport;

    HttpRequestFactory(HttpTransport httpTransport, HttpRequestInitializer httpRequestInitializer) {
        this.transport = httpTransport;
        this.initializer = httpRequestInitializer;
    }

    public HttpRequest buildDeleteRequest(GenericUrl genericUrl) throws IOException {
        return buildRequest("DELETE", genericUrl, null);
    }

    public HttpRequest buildGetRequest(GenericUrl genericUrl) throws IOException {
        return buildRequest("GET", genericUrl, null);
    }

    public HttpRequest buildHeadRequest(GenericUrl genericUrl) throws IOException {
        return buildRequest("HEAD", genericUrl, null);
    }

    public HttpRequest buildPatchRequest(GenericUrl genericUrl, HttpContent httpContent) throws IOException {
        return buildRequest("PATCH", genericUrl, httpContent);
    }

    public HttpRequest buildPostRequest(GenericUrl genericUrl, HttpContent httpContent) throws IOException {
        return buildRequest("POST", genericUrl, httpContent);
    }

    public HttpRequest buildPutRequest(GenericUrl genericUrl, HttpContent httpContent) throws IOException {
        return buildRequest("PUT", genericUrl, httpContent);
    }

    public HttpRequest buildRequest(String str, GenericUrl genericUrl, HttpContent httpContent) throws IOException {
        HttpRequest buildRequest = this.transport.buildRequest();
        if (this.initializer != null) {
            this.initializer.initialize(buildRequest);
        }
        buildRequest.setRequestMethod(str);
        if (genericUrl != null) {
            buildRequest.setUrl(genericUrl);
        }
        if (httpContent != null) {
            buildRequest.setContent(httpContent);
        }
        return buildRequest;
    }

    public HttpRequestInitializer getInitializer() {
        return this.initializer;
    }

    public HttpTransport getTransport() {
        return this.transport;
    }
}
