package com.google.api.client.testing.http;

import com.google.api.client.http.HttpContent;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.io.OutputStream;

public class MockHttpContent implements HttpContent {
    private byte[] content = new byte[0];
    @Deprecated
    private String encoding;
    private long length = -1;
    private String type;

    public final byte[] getContent() {
        return this.content;
    }

    @Deprecated
    public String getEncoding() {
        return this.encoding;
    }

    public long getLength() throws IOException {
        return this.length;
    }

    public String getType() {
        return this.type;
    }

    public boolean retrySupported() {
        return true;
    }

    public MockHttpContent setContent(byte[] bArr) {
        this.content = (byte[]) Preconditions.checkNotNull(bArr);
        return this;
    }

    @Deprecated
    public MockHttpContent setEncoding(String str) {
        this.encoding = str;
        return this;
    }

    public MockHttpContent setLength(long j) {
        Preconditions.checkArgument(j >= -1);
        this.length = j;
        return this;
    }

    public MockHttpContent setType(String str) {
        this.type = str;
        return this;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.content);
        outputStream.flush();
    }
}
