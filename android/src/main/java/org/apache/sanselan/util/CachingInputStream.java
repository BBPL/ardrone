package org.apache.sanselan.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachingInputStream extends InputStream {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final InputStream is;

    public CachingInputStream(InputStream inputStream) {
        this.is = inputStream;
    }

    public int available() throws IOException {
        return this.is.available();
    }

    public void close() throws IOException {
        this.is.close();
    }

    public byte[] getCache() {
        return this.baos.toByteArray();
    }

    public int read() throws IOException {
        int read = this.is.read();
        this.baos.write(read);
        return read;
    }
}
