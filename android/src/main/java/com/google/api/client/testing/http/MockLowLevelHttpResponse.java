package com.google.api.client.testing.http;

import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.util.TestableByteArrayInputStream;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MockLowLevelHttpResponse extends LowLevelHttpResponse {
    private InputStream content;
    private String contentEncoding;
    private long contentLength = -1;
    private String contentType;
    private List<String> headerNames = new ArrayList();
    private List<String> headerValues = new ArrayList();
    private boolean isDisconnected;
    private String reasonPhrase;
    private int statusCode = 200;

    public MockLowLevelHttpResponse addHeader(String str, String str2) {
        this.headerNames.add(Preconditions.checkNotNull(str));
        this.headerValues.add(Preconditions.checkNotNull(str2));
        return this;
    }

    public void disconnect() throws IOException {
        this.isDisconnected = true;
        super.disconnect();
    }

    public InputStream getContent() throws IOException {
        return this.content;
    }

    public String getContentEncoding() {
        return this.contentEncoding;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public final String getContentType() {
        return this.contentType;
    }

    public int getHeaderCount() {
        return this.headerNames.size();
    }

    public String getHeaderName(int i) {
        return (String) this.headerNames.get(i);
    }

    public final List<String> getHeaderNames() {
        return this.headerNames;
    }

    public String getHeaderValue(int i) {
        return (String) this.headerValues.get(i);
    }

    public final List<String> getHeaderValues() {
        return this.headerValues;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusLine() {
        StringBuilder stringBuilder = new StringBuilder(this.statusCode);
        if (this.reasonPhrase != null) {
            stringBuilder.append(this.reasonPhrase);
        }
        return stringBuilder.toString();
    }

    public boolean isDisconnected() {
        return this.isDisconnected;
    }

    public MockLowLevelHttpResponse setContent(InputStream inputStream) {
        this.content = inputStream;
        return this;
    }

    public MockLowLevelHttpResponse setContent(String str) {
        if (str == null) {
            this.content = null;
            setContentLength(0);
        } else {
            byte[] bytesUtf8 = StringUtils.getBytesUtf8(str);
            this.content = new TestableByteArrayInputStream(bytesUtf8);
            setContentLength((long) bytesUtf8.length);
        }
        return this;
    }

    public MockLowLevelHttpResponse setContentEncoding(String str) {
        this.contentEncoding = str;
        return this;
    }

    public MockLowLevelHttpResponse setContentLength(long j) {
        this.contentLength = j;
        Preconditions.checkArgument(j >= -1);
        return this;
    }

    public MockLowLevelHttpResponse setContentType(String str) {
        this.contentType = str;
        return this;
    }

    public MockLowLevelHttpResponse setHeaderNames(List<String> list) {
        this.headerNames = (List) Preconditions.checkNotNull(list);
        return this;
    }

    public MockLowLevelHttpResponse setHeaderValues(List<String> list) {
        this.headerValues = (List) Preconditions.checkNotNull(list);
        return this;
    }

    public MockLowLevelHttpResponse setReasonPhrase(String str) {
        this.reasonPhrase = str;
        return this;
    }

    public MockLowLevelHttpResponse setStatusCode(int i) {
        this.statusCode = i;
        Preconditions.checkArgument(i >= 0);
        return this;
    }
}
