package org.apache.sanselan.common.byteSources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteSourceArray extends ByteSource {
    private final byte[] bytes;

    public ByteSourceArray(String str, byte[] bArr) {
        super(str);
        this.bytes = bArr;
    }

    public ByteSourceArray(byte[] bArr) {
        super(null);
        this.bytes = bArr;
    }

    public byte[] getAll() throws IOException {
        return this.bytes;
    }

    public byte[] getBlock(int i, int i2) throws IOException {
        if (i + i2 > this.bytes.length) {
            throw new IOException("Could not read block (block start: " + i + ", block length: " + i2 + ", data length: " + this.bytes.length + ").");
        }
        Object obj = new byte[i2];
        System.arraycopy(this.bytes, i, obj, 0, i2);
        return obj;
    }

    public String getDescription() {
        return new StringBuilder(String.valueOf(this.bytes.length)).append(" byte array").toString();
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.bytes);
    }

    public long getLength() {
        return (long) this.bytes.length;
    }
}
