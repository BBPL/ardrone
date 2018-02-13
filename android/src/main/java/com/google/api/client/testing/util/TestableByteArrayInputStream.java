package com.google.api.client.testing.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class TestableByteArrayInputStream extends ByteArrayInputStream {
    private boolean closed;

    public TestableByteArrayInputStream(byte[] bArr) {
        super(bArr);
    }

    public TestableByteArrayInputStream(byte[] bArr, int i, int i2) {
        super(bArr);
    }

    public void close() throws IOException {
        this.closed = true;
    }

    public final byte[] getBuffer() {
        return this.buf;
    }

    public final boolean isClosed() {
        return this.closed;
    }
}
