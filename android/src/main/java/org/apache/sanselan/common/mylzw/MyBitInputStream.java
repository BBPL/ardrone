package org.apache.sanselan.common.mylzw;

import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.common.BinaryConstants;

public class MyBitInputStream extends InputStream implements BinaryConstants {
    private int bitCache = 0;
    private int bitsInCache = 0;
    private final int byteOrder;
    private long bytesRead = 0;
    private final InputStream is;
    private boolean tiffLZWMode = false;

    public MyBitInputStream(InputStream inputStream, int i) {
        this.byteOrder = i;
        this.is = inputStream;
    }

    public void flushCache() {
        this.bitsInCache = 0;
        this.bitCache = 0;
    }

    public long getBytesRead() {
        return this.bytesRead;
    }

    public int read() throws IOException {
        return readBits(8);
    }

    public int readBits(int i) throws IOException {
        int read;
        while (this.bitsInCache < i) {
            read = this.is.read();
            if (read < 0) {
                return this.tiffLZWMode ? 257 : -1;
            } else {
                read &= 255;
                if (this.byteOrder == 77) {
                    this.bitCache = read | (this.bitCache << 8);
                } else if (this.byteOrder == 73) {
                    this.bitCache = (read << this.bitsInCache) | this.bitCache;
                } else {
                    throw new IOException("Unknown byte order: " + this.byteOrder);
                }
                this.bytesRead++;
                this.bitsInCache += 8;
            }
        }
        read = (1 << i) - 1;
        if (this.byteOrder == 77) {
            read &= this.bitCache >> (this.bitsInCache - i);
        } else if (this.byteOrder == 73) {
            read &= this.bitCache;
            this.bitCache >>= i;
        } else {
            throw new IOException("Unknown byte order: " + this.byteOrder);
        }
        this.bitsInCache -= i;
        this.bitCache = ((1 << this.bitsInCache) - 1) & this.bitCache;
        return read;
    }

    public void setTiffLZWMode() {
        this.tiffLZWMode = true;
    }
}
