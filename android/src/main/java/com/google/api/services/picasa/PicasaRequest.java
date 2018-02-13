package com.google.api.services.picasa;

import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.util.Key;

public class PicasaRequest<T> extends AbstractGoogleClientRequest<T> {
    @Key
    private String fields;

    protected PicasaRequest(Picasa picasa, String str, String str2, HttpContent httpContent, Class<T> cls) {
        super(picasa, str, str2, httpContent, cls);
    }

    public final Picasa getAbstractGoogleClient() {
        return (Picasa) super.getAbstractGoogleClient();
    }

    public String getFields() {
        return this.fields;
    }

    public PicasaRequest<T> setDisableGZipContent(boolean z) {
        return (PicasaRequest) super.setDisableGZipContent(z);
    }

    public PicasaRequest<T> setFields(String str) {
        this.fields = str;
        return this;
    }

    public PicasaRequest<T> setRequestHeaders(HttpHeaders httpHeaders) {
        return (PicasaRequest) super.setRequestHeaders(httpHeaders);
    }
}
