package com.google.api.client.extensions.appengine.http;

import com.google.api.client.http.LowLevelHttpResponse;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

final class UrlFetchResponse extends LowLevelHttpResponse {
    private String contentEncoding;
    private long contentLength;
    private String contentType;
    private final HTTPResponse fetchResponse;
    private final ArrayList<String> headerNames = new ArrayList();
    private final ArrayList<String> headerValues = new ArrayList();

    UrlFetchResponse(HTTPResponse hTTPResponse) {
        this.fetchResponse = hTTPResponse;
        for (HTTPHeader hTTPHeader : hTTPResponse.getHeaders()) {
            String name = hTTPHeader.getName();
            String value = hTTPHeader.getValue();
            if (!(name == null || value == null)) {
                this.headerNames.add(name);
                this.headerValues.add(value);
                if ("content-type".equalsIgnoreCase(name)) {
                    this.contentType = value;
                } else if ("content-encoding".equalsIgnoreCase(name)) {
                    this.contentEncoding = value;
                } else if ("content-length".equalsIgnoreCase(name)) {
                    try {
                        this.contentLength = Long.parseLong(value);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
    }

    public InputStream getContent() {
        byte[] content = this.fetchResponse.getContent();
        return content == null ? null : new ByteArrayInputStream(content);
    }

    public String getContentEncoding() {
        return this.contentEncoding;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public String getContentType() {
        return this.contentType;
    }

    public int getHeaderCount() {
        return this.headerNames.size();
    }

    public String getHeaderName(int i) {
        return (String) this.headerNames.get(i);
    }

    public String getHeaderValue(int i) {
        return (String) this.headerValues.get(i);
    }

    public String getReasonPhrase() {
        return null;
    }

    public int getStatusCode() {
        return this.fetchResponse.getResponseCode();
    }

    public String getStatusLine() {
        return null;
    }
}
