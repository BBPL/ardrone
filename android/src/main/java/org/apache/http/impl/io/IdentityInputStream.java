package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;

@NotThreadSafe
public class IdentityInputStream extends InputStream {
    private boolean closed = false;
    private final SessionInputBuffer in;

    public IdentityInputStream(SessionInputBuffer sessionInputBuffer) {
        if (sessionInputBuffer == null) {
            throw new IllegalArgumentException("Session input buffer may not be null");
        }
        this.in = sessionInputBuffer;
    }

    public int available() throws IOException {
        return this.in instanceof BufferInfo ? ((BufferInfo) this.in).length() : 0;
    }

    public void close() throws IOException {
        this.closed = true;
    }

    public int read() throws IOException {
        return this.closed ? -1 : this.in.read();
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        return this.closed ? -1 : this.in.read(bArr, i, i2);
    }
}
