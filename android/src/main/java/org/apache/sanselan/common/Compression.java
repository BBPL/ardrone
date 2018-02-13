package org.apache.sanselan.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.mylzw.MyLZWCompressor;
import org.apache.sanselan.common.mylzw.MyLZWDecompressor;

public class Compression {
    public byte[] compressLZW(byte[] bArr, int i, int i2, boolean z) throws IOException {
        return new MyLZWCompressor(i, i2, z).compress(bArr);
    }

    public byte[] decompressLZW(byte[] bArr, int i, int i2, int i3) throws IOException {
        return new MyLZWDecompressor(i, i3).decompress(new ByteArrayInputStream(bArr), i2);
    }

    public byte[] decompressPackBits(byte[] bArr, int i, int i2) throws ImageReadException, IOException {
        return new PackBits().decompress(bArr, i);
    }
}
