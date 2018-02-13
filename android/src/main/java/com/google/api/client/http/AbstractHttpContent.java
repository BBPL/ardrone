package com.google.api.client.http;

import com.google.api.client.util.Charsets;
import com.google.api.client.util.IOUtils;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class AbstractHttpContent implements HttpContent {
    private long computedLength;
    private HttpMediaType mediaType;

    protected AbstractHttpContent(HttpMediaType httpMediaType) {
        this.computedLength = -1;
        this.mediaType = httpMediaType;
    }

    protected AbstractHttpContent(String str) {
        this(str == null ? null : new HttpMediaType(str));
    }

    public static long computeLength(HttpContent httpContent) throws IOException {
        return !httpContent.retrySupported() ? -1 : IOUtils.computeLength(httpContent);
    }

    protected long computeLength() throws IOException {
        return computeLength(this);
    }

    protected final Charset getCharset() {
        return (this.mediaType == null || this.mediaType.getCharsetParameter() == null) ? Charsets.UTF_8 : this.mediaType.getCharsetParameter();
    }

    @Deprecated
    public String getEncoding() {
        return null;
    }

    public long getLength() throws IOException {
        if (this.computedLength == -1) {
            this.computedLength = computeLength();
        }
        return this.computedLength;
    }

    public final HttpMediaType getMediaType() {
        return this.mediaType;
    }

    public String getType() {
        return this.mediaType == null ? null : this.mediaType.build();
    }

    public boolean retrySupported() {
        return true;
    }

    public AbstractHttpContent setMediaType(HttpMediaType httpMediaType) {
        this.mediaType = httpMediaType;
        return this;
    }
}
