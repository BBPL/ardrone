package com.google.api.client.http;

import com.google.api.client.util.StreamingContent;
import java.io.IOException;
import java.io.OutputStream;

public interface HttpContent extends StreamingContent {
    @Deprecated
    String getEncoding();

    long getLength() throws IOException;

    String getType();

    boolean retrySupported();

    void writeTo(OutputStream outputStream) throws IOException;
}
