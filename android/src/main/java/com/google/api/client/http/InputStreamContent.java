package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import java.io.InputStream;

public final class InputStreamContent extends AbstractInputStreamContent {
    private final InputStream inputStream;
    private long length = -1;
    private boolean retrySupported;

    public InputStreamContent(String str, InputStream inputStream) {
        super(str);
        this.inputStream = (InputStream) Preconditions.checkNotNull(inputStream);
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public long getLength() {
        return this.length;
    }

    public boolean retrySupported() {
        return this.retrySupported;
    }

    public InputStreamContent setCloseInputStream(boolean z) {
        return (InputStreamContent) super.setCloseInputStream(z);
    }

    @Deprecated
    public InputStreamContent setEncoding(String str) {
        return (InputStreamContent) super.setEncoding(str);
    }

    public InputStreamContent setLength(long j) {
        this.length = j;
        return this;
    }

    public InputStreamContent setRetrySupported(boolean z) {
        this.retrySupported = z;
        return this;
    }

    public InputStreamContent setType(String str) {
        return (InputStreamContent) super.setType(str);
    }
}
