package com.google.api.client.googleapis.testing.services.json;

import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest;
import com.google.api.client.http.HttpHeaders;

public class MockGoogleJsonClientRequest<T> extends AbstractGoogleJsonClientRequest<T> {
    public MockGoogleJsonClientRequest(AbstractGoogleJsonClient abstractGoogleJsonClient, String str, String str2, Object obj, Class<T> cls) {
        super(abstractGoogleJsonClient, str, str2, obj, cls);
    }

    public MockGoogleJsonClient getAbstractGoogleClient() {
        return (MockGoogleJsonClient) super.getAbstractGoogleClient();
    }

    public MockGoogleJsonClientRequest<T> setDisableGZipContent(boolean z) {
        return (MockGoogleJsonClientRequest) super.setDisableGZipContent(z);
    }

    public MockGoogleJsonClientRequest<T> setRequestHeaders(HttpHeaders httpHeaders) {
        return (MockGoogleJsonClientRequest) super.setRequestHeaders(httpHeaders);
    }
}
