package org.apache.sanselan.common;

import java.io.IOException;
import java.io.InputStream;

public class BitInputStream extends InputStream implements BinaryConstants {
    private long bytes_read = 0;
    private int cache;
    private int cacheBitsRemaining = 0;
    private final InputStream is;

    public BitInputStream(InputStream inputStream) {
        this.is = inputStream;
    }

    public void flushCache() {
        this.cacheBitsRemaining = 0;
    }

    public long getBytesRead() {
        return this.bytes_read;
    }

    public int read() throws IOException {
        if (this.cacheBitsRemaining <= 0) {
            return this.is.read();
        }
        throw new IOException("BitInputStream: incomplete bit read");
    }

    public final int readBits(int i) throws IOException {
        if (i < 8) {
            if (this.cacheBitsRemaining == 0) {
                this.cache = this.is.read();
                this.cacheBitsRemaining = 8;
                this.bytes_read++;
            }
            if (i > this.cacheBitsRemaining) {
                throw new IOException("BitInputStream: can't read bit fields across bytes");
            }
            this.cacheBitsRemaining -= i;
            int i2 = this.cache >> this.cacheBitsRemaining;
            switch (i) {
                case 1:
                    return i2 & 1;
                case 2:
                    return i2 & 3;
                case 3:
                    return i2 & 7;
                case 4:
                    return i2 & 15;
                case 5:
                    return i2 & 31;
                case 6:
                    return i2 & 63;
                case 7:
                    return i2 & 127;
            }
        }
        if (this.cacheBitsRemaining > 0) {
            throw new IOException("BitInputStream: incomplete bit read");
        } else if (i == 8) {
            this.bytes_read++;
            return this.is.read();
        } else if (i == 16) {
            this.bytes_read += 2;
            return (this.is.read() << 8) | (this.is.read() << 0);
        } else if (i == 24) {
            this.bytes_read += 3;
            return ((this.is.read() << 16) | (this.is.read() << 8)) | (this.is.read() << 0);
        } else if (i == 32) {
            this.bytes_read += 4;
            return (((this.is.read() << 24) | (this.is.read() << 16)) | (this.is.read() << 8)) | (this.is.read() << 0);
        } else {
            throw new IOException("BitInputStream: unknown error");
        }
    }
}
