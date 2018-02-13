package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class ByteArrayEntity extends AbstractHttpEntity implements Cloneable {
    private final byte[] f354b;
    @Deprecated
    protected final byte[] content;
    private final int len;
    private final int off;

    public ByteArrayEntity(byte[] bArr) {
        this(bArr, null);
    }

    public ByteArrayEntity(byte[] bArr, int i, int i2) {
        this(bArr, i, i2, null);
    }

    public ByteArrayEntity(byte[] bArr, int i, int i2, ContentType contentType) {
        if (bArr == null) {
            throw new IllegalArgumentException("Source byte array may not be null");
        } else if (i < 0 || i > bArr.length || i2 < 0 || i + i2 < 0 || i + i2 > bArr.length) {
            throw new IndexOutOfBoundsException("off: " + i + " len: " + i2 + " b.length: " + bArr.length);
        } else {
            this.content = bArr;
            this.f354b = bArr;
            this.off = i;
            this.len = i2;
            if (contentType != null) {
                setContentType(contentType.toString());
            }
        }
    }

    public ByteArrayEntity(byte[] bArr, ContentType contentType) {
        if (bArr == null) {
            throw new IllegalArgumentException("Source byte array may not be null");
        }
        this.content = bArr;
        this.f354b = bArr;
        this.off = 0;
        this.len = this.f354b.length;
        if (contentType != null) {
            setContentType(contentType.toString());
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public InputStream getContent() {
        return new ByteArrayInputStream(this.f354b, this.off, this.len);
    }

    public long getContentLength() {
        return (long) this.len;
    }

    public boolean isRepeatable() {
        return true;
    }

    public boolean isStreaming() {
        return false;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        outputStream.write(this.f354b, this.off, this.len);
        outputStream.flush();
    }
}
