package com.google.api.client.googleapis.json;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.rpc2.JsonRpcRequest;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.List;

public final class GoogleJsonRpcHttpTransport {
    private static final String JSON_RPC_CONTENT_TYPE = "application/json-rpc";
    private final String accept;
    private final JsonFactory jsonFactory;
    private final String mimeType;
    private final String rpcServerUrl;
    private final HttpTransport transport;

    public static class Builder {
        static final GenericUrl DEFAULT_SERVER_URL = new GenericUrl("https://www.googleapis.com");
        private String accept = this.mimeType;
        private final HttpTransport httpTransport;
        private final JsonFactory jsonFactory;
        private String mimeType = GoogleJsonRpcHttpTransport.JSON_RPC_CONTENT_TYPE;
        private GenericUrl rpcServerUrl = DEFAULT_SERVER_URL;

        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory) {
            this.httpTransport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
            this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        }

        protected GoogleJsonRpcHttpTransport build() {
            return new GoogleJsonRpcHttpTransport(this.httpTransport, this.jsonFactory, this.rpcServerUrl.build(), this.mimeType, this.accept);
        }

        public final String getAccept() {
            return this.accept;
        }

        public final HttpTransport getHttpTransport() {
            return this.httpTransport;
        }

        public final JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public final String getMimeType() {
            return this.mimeType;
        }

        public final GenericUrl getRpcServerUrl() {
            return this.rpcServerUrl;
        }

        protected Builder setAccept(String str) {
            this.accept = (String) Preconditions.checkNotNull(str);
            return this;
        }

        protected Builder setMimeType(String str) {
            this.mimeType = (String) Preconditions.checkNotNull(str);
            return this;
        }

        protected Builder setRpcServerUrl(GenericUrl genericUrl) {
            this.rpcServerUrl = (GenericUrl) Preconditions.checkNotNull(genericUrl);
            return this;
        }
    }

    public GoogleJsonRpcHttpTransport(HttpTransport httpTransport, JsonFactory jsonFactory) {
        this((HttpTransport) Preconditions.checkNotNull(httpTransport), (JsonFactory) Preconditions.checkNotNull(jsonFactory), Builder.DEFAULT_SERVER_URL.build(), JSON_RPC_CONTENT_TYPE, JSON_RPC_CONTENT_TYPE);
    }

    protected GoogleJsonRpcHttpTransport(HttpTransport httpTransport, JsonFactory jsonFactory, String str, String str2, String str3) {
        this.transport = httpTransport;
        this.jsonFactory = jsonFactory;
        this.rpcServerUrl = str;
        this.mimeType = str2;
        this.accept = str3;
    }

    private HttpRequest internalExecute(Object obj) throws IOException {
        HttpContent jsonHttpContent = new JsonHttpContent(this.jsonFactory, obj);
        jsonHttpContent.setMediaType(new HttpMediaType(this.mimeType));
        HttpRequest buildPostRequest = this.transport.createRequestFactory().buildPostRequest(new GenericUrl(this.rpcServerUrl), jsonHttpContent);
        buildPostRequest.getHeaders().setAccept(this.accept);
        return buildPostRequest;
    }

    public HttpRequest buildPostRequest(JsonRpcRequest jsonRpcRequest) throws IOException {
        return internalExecute(jsonRpcRequest);
    }

    public HttpRequest buildPostRequest(List<JsonRpcRequest> list) throws IOException {
        return internalExecute(list);
    }

    public final String getAccept() {
        return this.accept;
    }

    public final HttpTransport getHttpTransport() {
        return this.transport;
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public final String getMimeType() {
        return this.mimeType;
    }

    public final String getRpcServerUrl() {
        return this.rpcServerUrl;
    }
}
