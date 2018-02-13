package org.apache.http.client.fluent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;

public class Content {
    public static final Content NO_CONTENT = new Content(new byte[0], ContentType.DEFAULT_BINARY);
    private final byte[] raw;
    private final ContentType type;

    Content(byte[] bArr, ContentType contentType) {
        this.raw = bArr;
        this.type = contentType;
    }

    public byte[] asBytes() {
        return (byte[]) this.raw.clone();
    }

    public InputStream asStream() {
        return new ByteArrayInputStream(this.raw);
    }

    public String asString() {
        Charset charset = this.type.getCharset();
        try {
            return new String(this.raw, (charset == null ? HTTP.DEF_CONTENT_CHARSET : charset).name());
        } catch (UnsupportedEncodingException e) {
            return new String(this.raw);
        }
    }

    public ContentType getType() {
        return this.type;
    }

    public String toString() {
        return asString();
    }
}
