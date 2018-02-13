package com.google.api.client.http;

import java.io.IOException;
import java.io.OutputStream;

public class EmptyContent implements HttpContent {
    @Deprecated
    public String getEncoding() {
        return null;
    }

    public long getLength() throws IOException {
        return 0;
    }

    public String getType() {
        return null;
    }

    public boolean retrySupported() {
        return true;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.flush();
    }
}
