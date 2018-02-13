package org.apache.sanselan.common.mylzw;

import java.io.IOException;
import java.io.InputStream;

public class BitsToByteInputStream extends InputStream {
    private final int desiredDepth;
    private final MyBitInputStream is;

    public BitsToByteInputStream(MyBitInputStream myBitInputStream, int i) {
        this.is = myBitInputStream;
        this.desiredDepth = i;
    }

    public int read() throws IOException {
        return readBits(8);
    }

    public int readBits(int i) throws IOException {
        int readBits = this.is.readBits(i);
        return i < this.desiredDepth ? readBits << (this.desiredDepth - i) : i > this.desiredDepth ? readBits >> (i - this.desiredDepth) : readBits;
    }

    public int[] readBitsArray(int i, int i2) throws IOException {
        int[] iArr = new int[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            iArr[i3] = readBits(i);
        }
        return iArr;
    }
}
