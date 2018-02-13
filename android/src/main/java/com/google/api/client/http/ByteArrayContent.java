package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public final class ByteArrayContent extends AbstractInputStreamContent {
    private final byte[] byteArray;
    private final int length;
    private final int offset;

    public ByteArrayContent(String str, byte[] bArr) {
        this(str, bArr, 0, bArr.length);
    }

    public ByteArrayContent(String str, byte[] bArr, int i, int i2) {
        super(str);
        this.byteArray = (byte[]) Preconditions.checkNotNull(bArr);
        boolean z = i >= 0 && i2 >= 0 && i + i2 <= bArr.length;
        Preconditions.checkArgument(z);
        this.offset = i;
        this.length = i2;
    }

    public static ByteArrayContent fromString(String str, String str2) {
        return new ByteArrayContent(str, StringUtils.getBytesUtf8(str2));
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.byteArray, this.offset, this.length);
    }

    public long getLength() {
        return (long) this.length;
    }

    public boolean retrySupported() {
        return true;
    }

    public ByteArrayContent setCloseInputStream(boolean z) {
        return (ByteArrayContent) super.setCloseInputStream(z);
    }

    @Deprecated
    public ByteArrayContent setEncoding(String str) {
        return (ByteArrayContent) super.setEncoding(str);
    }

    public ByteArrayContent setType(String str) {
        return (ByteArrayContent) super.setType(str);
    }
}
