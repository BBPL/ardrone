package org.apache.http.impl.client.cache;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.cache.Resource;

@Immutable
public class HeapResource implements Resource {
    private static final long serialVersionUID = -2078599905620463394L;
    private final byte[] f359b;

    public HeapResource(byte[] bArr) {
        this.f359b = bArr;
    }

    public void dispose() {
    }

    byte[] getByteArray() {
        return this.f359b;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.f359b);
    }

    public long length() {
        return (long) this.f359b.length;
    }
}
