package org.mortbay.jetty;

import java.io.IOException;
import java.io.InputStream;
import org.mortbay.io.Buffer;
import org.mortbay.resource.Resource;

public interface HttpContent {
    Buffer getBuffer();

    long getContentLength();

    Buffer getContentType();

    InputStream getInputStream() throws IOException;

    Buffer getLastModified();

    Resource getResource();

    void release();
}
