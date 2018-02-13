package org.apache.http.client.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public interface Resource extends Serializable {
    void dispose();

    InputStream getInputStream() throws IOException;

    long length();
}
