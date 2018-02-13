package com.google.api.client.http;

import com.google.api.client.util.StreamingContent;
import java.io.IOException;

public abstract class LowLevelHttpRequest {
    private String contentEncoding;
    private long contentLength = -1;
    private String contentType;
    private StreamingContent streamingContent;

    public abstract void addHeader(String str, String str2) throws IOException;

    public abstract LowLevelHttpResponse execute() throws IOException;

    public final String getContentEncoding() {
        return this.contentEncoding;
    }

    public final long getContentLength() {
        return this.contentLength;
    }

    public final String getContentType() {
        return this.contentType;
    }

    public final StreamingContent getStreamingContent() {
        return this.streamingContent;
    }

    @Deprecated
    public void setContent(HttpContent httpContent) throws IOException {
    }

    public final void setContentEncoding(String str) throws IOException {
        this.contentEncoding = str;
    }

    public final void setContentLength(long j) throws IOException {
        this.contentLength = j;
    }

    public final void setContentType(String str) throws IOException {
        this.contentType = str;
    }

    public final void setStreamingContent(StreamingContent streamingContent) throws IOException {
        this.streamingContent = streamingContent;
    }

    public void setTimeout(int i, int i2) throws IOException {
    }
}
