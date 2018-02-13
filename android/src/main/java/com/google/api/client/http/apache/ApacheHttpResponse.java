package com.google.api.client.http.apache;

import com.google.api.client.http.LowLevelHttpResponse;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpRequestBase;

final class ApacheHttpResponse extends LowLevelHttpResponse {
    private final Header[] allHeaders;
    private final HttpRequestBase request;
    private final HttpResponse response;

    ApacheHttpResponse(HttpRequestBase httpRequestBase, HttpResponse httpResponse) {
        this.request = httpRequestBase;
        this.response = httpResponse;
        this.allHeaders = httpResponse.getAllHeaders();
    }

    public void disconnect() {
        this.request.abort();
    }

    public InputStream getContent() throws IOException {
        HttpEntity entity = this.response.getEntity();
        return entity == null ? null : entity.getContent();
    }

    public String getContentEncoding() {
        HttpEntity entity = this.response.getEntity();
        if (entity != null) {
            Header contentEncoding = entity.getContentEncoding();
            if (contentEncoding != null) {
                return contentEncoding.getValue();
            }
        }
        return null;
    }

    public long getContentLength() {
        HttpEntity entity = this.response.getEntity();
        return entity == null ? -1 : entity.getContentLength();
    }

    public String getContentType() {
        HttpEntity entity = this.response.getEntity();
        if (entity != null) {
            Header contentType = entity.getContentType();
            if (contentType != null) {
                return contentType.getValue();
            }
        }
        return null;
    }

    public int getHeaderCount() {
        return this.allHeaders.length;
    }

    public String getHeaderName(int i) {
        return this.allHeaders[i].getName();
    }

    public String getHeaderValue(int i) {
        return this.allHeaders[i].getValue();
    }

    public String getHeaderValue(String str) {
        return this.response.getLastHeader(str).getValue();
    }

    public String getReasonPhrase() {
        StatusLine statusLine = this.response.getStatusLine();
        return statusLine == null ? null : statusLine.getReasonPhrase();
    }

    public int getStatusCode() {
        StatusLine statusLine = this.response.getStatusLine();
        return statusLine == null ? 0 : statusLine.getStatusCode();
    }

    public String getStatusLine() {
        StatusLine statusLine = this.response.getStatusLine();
        return statusLine == null ? null : statusLine.toString();
    }
}
