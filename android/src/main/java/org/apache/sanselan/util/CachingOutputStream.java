package org.apache.sanselan.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CachingOutputStream extends OutputStream {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final OutputStream os;

    public CachingOutputStream(OutputStream outputStream) {
        this.os = outputStream;
    }

    public void close() throws IOException {
        this.os.close();
    }

    public void flush() throws IOException {
        this.os.flush();
    }

    public byte[] getCache() {
        return this.baos.toByteArray();
    }

    public void write(int i) throws IOException {
        this.os.write(i);
        this.baos.write(i);
    }
}
