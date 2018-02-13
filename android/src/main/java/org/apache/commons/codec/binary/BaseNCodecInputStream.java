package org.apache.commons.codec.binary;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseNCodecInputStream extends FilterInputStream {
    private final BaseNCodec baseNCodec;
    private final boolean doEncode;
    private final byte[] singleByte = new byte[1];

    protected BaseNCodecInputStream(InputStream inputStream, BaseNCodec baseNCodec, boolean z) {
        super(inputStream);
        this.doEncode = z;
        this.baseNCodec = baseNCodec;
    }

    public boolean markSupported() {
        return false;
    }

    public int read() throws IOException {
        int read = read(this.singleByte, 0, 1);
        while (read == 0) {
            read = read(this.singleByte, 0, 1);
        }
        return read > 0 ? this.singleByte[0] < (byte) 0 ? this.singleByte[0] + 256 : this.singleByte[0] : -1;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (bArr == null) {
            throw new NullPointerException();
        } else if (i < 0 || i2 < 0) {
            throw new IndexOutOfBoundsException();
        } else if (i > bArr.length || i + i2 > bArr.length) {
            throw new IndexOutOfBoundsException();
        } else if (i2 == 0) {
            return 0;
        } else {
            int i3 = 0;
            while (i3 == 0) {
                if (!this.baseNCodec.hasData()) {
                    byte[] bArr2 = new byte[(this.doEncode ? 4096 : 8192)];
                    int read = this.in.read(bArr2);
                    if (this.doEncode) {
                        this.baseNCodec.encode(bArr2, 0, read);
                    } else {
                        this.baseNCodec.decode(bArr2, 0, read);
                    }
                }
                i3 = this.baseNCodec.readResults(bArr, i, i2);
            }
            return i3;
        }
    }
}
