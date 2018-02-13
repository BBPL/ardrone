package org.apache.sanselan.common;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStreamFlexible extends InputStream implements BinaryConstants {
    private long bytesRead = 0;
    private int cache;
    private int cacheBitsRemaining = 0;
    private final InputStream is;

    public BitInputStreamFlexible(InputStream inputStream) {
        this.is = inputStream;
    }

    public void flushCache() {
        this.cacheBitsRemaining = 0;
    }

    public long getBytesRead() {
        return this.bytesRead;
    }

    public int read() throws IOException {
        if (this.cacheBitsRemaining <= 0) {
            return this.is.read();
        }
        throw new IOException("BitInputStream: incomplete bit read");
    }

    public final int readBits(int i) throws IOException {
        int i2 = 0;
        if (i <= 32) {
            if (this.cacheBitsRemaining > 0) {
                if (i >= this.cacheBitsRemaining) {
                    int i3 = ((1 << this.cacheBitsRemaining) - 1) & this.cache;
                    i -= this.cacheBitsRemaining;
                    this.cacheBitsRemaining = 0;
                    i2 = i3;
                } else {
                    this.cacheBitsRemaining -= i;
                    i = 0;
                    i2 = ((1 << i) - 1) & (this.cache >> this.cacheBitsRemaining);
                }
            }
            while (i >= 8) {
                this.cache = this.is.read();
                if (this.cache < 0) {
                    throw new IOException("couldn't read bits");
                }
                System.out.println("cache 1: " + this.cache + " (" + Integer.toHexString(this.cache) + ", " + Integer.toBinaryString(this.cache) + ")");
                this.bytesRead++;
                i2 = (i2 << 8) | (this.cache & 255);
                i -= 8;
            }
            if (i <= 0) {
                return i2;
            }
            this.cache = this.is.read();
            if (this.cache < 0) {
                throw new IOException("couldn't read bits");
            }
            System.out.println("cache 2: " + this.cache + " (" + Integer.toHexString(this.cache) + ", " + Integer.toBinaryString(this.cache) + ")");
            this.bytesRead++;
            this.cacheBitsRemaining = 8 - i;
            return (i2 << i) | (((1 << i) - 1) & (this.cache >> this.cacheBitsRemaining));
        }
        throw new IOException("BitInputStream: unknown error");
    }
}
