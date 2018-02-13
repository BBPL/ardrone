package com.google.api.client.http;

import com.google.api.client.util.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractInputStreamContent implements HttpContent {
    private boolean closeInputStream = true;
    @Deprecated
    private String encoding;
    private String type;

    public AbstractInputStreamContent(String str) {
        setType(str);
    }

    @Deprecated
    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        IOUtils.copy(inputStream, outputStream);
    }

    @Deprecated
    public static void copy(InputStream inputStream, OutputStream outputStream, boolean z) throws IOException {
        IOUtils.copy(inputStream, outputStream, z);
    }

    public final boolean getCloseInputStream() {
        return this.closeInputStream;
    }

    @Deprecated
    public String getEncoding() {
        return this.encoding;
    }

    public abstract InputStream getInputStream() throws IOException;

    public String getType() {
        return this.type;
    }

    public AbstractInputStreamContent setCloseInputStream(boolean z) {
        this.closeInputStream = z;
        return this;
    }

    @Deprecated
    public AbstractInputStreamContent setEncoding(String str) {
        this.encoding = str;
        return this;
    }

    public AbstractInputStreamContent setType(String str) {
        this.type = str;
        return this;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        IOUtils.copy(getInputStream(), outputStream, this.closeInputStream);
        outputStream.flush();
    }
}
