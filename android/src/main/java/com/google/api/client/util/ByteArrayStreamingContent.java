package com.google.api.client.util;

import java.io.IOException;
import java.io.OutputStream;

public class ByteArrayStreamingContent implements StreamingContent {
    private final byte[] byteArray;
    private final int length;
    private final int offset;

    public ByteArrayStreamingContent(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public ByteArrayStreamingContent(byte[] bArr, int i, int i2) {
        this.byteArray = (byte[]) Preconditions.checkNotNull(bArr);
        boolean z = i >= 0 && i2 >= 0 && i + i2 <= bArr.length;
        Preconditions.checkArgument(z);
        this.offset = i;
        this.length = i2;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.byteArray, this.offset, this.length);
        outputStream.flush();
    }
}
