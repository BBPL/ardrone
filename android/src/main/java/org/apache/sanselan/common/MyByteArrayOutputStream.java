package org.apache.sanselan.common;

import java.io.IOException;
import java.io.OutputStream;

public class MyByteArrayOutputStream extends OutputStream {
    private final byte[] bytes;
    private int count = 0;

    public MyByteArrayOutputStream(int i) {
        this.bytes = new byte[i];
    }

    public int getBytesWritten() {
        return this.count;
    }

    public byte[] toByteArray() {
        if (this.count >= this.bytes.length) {
            return this.bytes;
        }
        Object obj = new byte[this.count];
        System.arraycopy(this.bytes, 0, obj, 0, this.count);
        return obj;
    }

    public void write(int i) throws IOException {
        if (this.count >= this.bytes.length) {
            throw new IOException("Write exceeded expected length (" + this.count + ", " + this.bytes.length + ")");
        }
        this.bytes[this.count] = (byte) i;
        this.count++;
    }
}
