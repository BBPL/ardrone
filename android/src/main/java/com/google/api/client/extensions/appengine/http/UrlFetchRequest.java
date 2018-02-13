package com.google.api.client.extensions.appengine.http;

import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

final class UrlFetchRequest extends LowLevelHttpRequest {
    private final HTTPRequest request;

    UrlFetchRequest(FetchOptions fetchOptions, HTTPMethod hTTPMethod, String str) throws IOException {
        this.request = new HTTPRequest(new URL(str), hTTPMethod, fetchOptions);
    }

    public void addHeader(String str, String str2) {
        this.request.addHeader(new HTTPHeader(str, str2));
    }

    public LowLevelHttpResponse execute() throws IOException {
        if (getStreamingContent() != null) {
            String contentType = getContentType();
            if (contentType != null) {
                addHeader("Content-Type", contentType);
            }
            contentType = getContentEncoding();
            if (contentType != null) {
                addHeader("Content-Encoding", contentType);
            }
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            getStreamingContent().writeTo(byteArrayOutputStream);
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            if (toByteArray.length != 0) {
                this.request.setPayload(toByteArray);
            }
        }
        return new UrlFetchResponse(URLFetchServiceFactory.getURLFetchService().fetch(this.request));
    }

    public void setTimeout(int i, int i2) {
        FetchOptions fetchOptions = this.request.getFetchOptions();
        double d = (i == 0 || i2 == 0) ? Double.MAX_VALUE : ((double) (i + i2)) / 1000.0d;
        fetchOptions.setDeadline(Double.valueOf(d));
    }
}
