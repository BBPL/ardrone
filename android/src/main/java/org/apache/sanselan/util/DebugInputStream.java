package org.apache.sanselan.util;

import java.io.IOException;
import java.io.InputStream;

public class DebugInputStream extends InputStream {
    private long bytes_read = 0;
    private final InputStream is;

    public DebugInputStream(InputStream inputStream) {
        this.is = inputStream;
    }

    public int available() throws IOException {
        return this.is.available();
    }

    public void close() throws IOException {
        this.is.close();
    }

    public long getBytesRead() {
        return this.bytes_read;
    }

    public int read() throws IOException {
        int read = this.is.read();
        this.bytes_read++;
        return read;
    }

    public long skip(long j) throws IOException {
        long skip = this.is.skip(j);
        this.bytes_read += j;
        return skip;
    }
}
