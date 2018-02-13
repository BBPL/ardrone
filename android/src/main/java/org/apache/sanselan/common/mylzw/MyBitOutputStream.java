package org.apache.sanselan.common.mylzw;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.sanselan.common.BinaryConstants;

public class MyBitOutputStream extends OutputStream implements BinaryConstants {
    private int bitCache = 0;
    private int bitsInCache = 0;
    private final int byteOrder;
    private int bytesWritten = 0;
    private final OutputStream os;

    public MyBitOutputStream(OutputStream outputStream, int i) {
        this.byteOrder = i;
        this.os = outputStream;
    }

    private void actualWrite(int i) throws IOException {
        this.os.write(i);
        this.bytesWritten++;
    }

    public void flushCache() throws IOException {
        if (this.bitsInCache > 0) {
            int i = ((1 << this.bitsInCache) - 1) & this.bitCache;
            if (this.byteOrder == 77) {
                this.os.write(i << (8 - this.bitsInCache));
            } else if (this.byteOrder == 73) {
                this.os.write(i);
            }
        }
        this.bitsInCache = 0;
        this.bitCache = 0;
    }

    public int getBytesWritten() {
        return (this.bitsInCache > 0 ? 1 : 0) + this.bytesWritten;
    }

    public void write(int i) throws IOException {
        writeBits(i, 8);
    }

    public void writeBits(int i, int i2) throws IOException {
        int i3 = ((1 << i2) - 1) & i;
        if (this.byteOrder == 77) {
            this.bitCache = i3 | (this.bitCache << i2);
        } else if (this.byteOrder == 73) {
            this.bitCache = (i3 << this.bitsInCache) | this.bitCache;
        } else {
            throw new IOException("Unknown byte order: " + this.byteOrder);
        }
        this.bitsInCache += i2;
        while (this.bitsInCache >= 8) {
            if (this.byteOrder == 77) {
                actualWrite((this.bitCache >> (this.bitsInCache - 8)) & 255);
                this.bitsInCache -= 8;
            } else if (this.byteOrder == 73) {
                actualWrite(this.bitCache & 255);
                this.bitCache >>= 8;
                this.bitsInCache -= 8;
            }
            this.bitCache = ((1 << this.bitsInCache) - 1) & this.bitCache;
        }
    }
}
