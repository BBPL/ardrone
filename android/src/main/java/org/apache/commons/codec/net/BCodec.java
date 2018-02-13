package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Base64;

public class BCodec extends RFC1522Codec implements StringEncoder, StringDecoder {
    private final String charset;

    public BCodec() {
        this("UTF-8");
    }

    public BCodec(String str) {
        this.charset = str;
    }

    public Object decode(Object obj) throws DecoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return decode((String) obj);
        }
        throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be decoded using BCodec");
    }

    public String decode(String str) throws DecoderException {
        if (str == null) {
            return null;
        }
        try {
            return decodeText(str);
        } catch (Throwable e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }

    protected byte[] doDecoding(byte[] bArr) {
        return bArr == null ? null : Base64.decodeBase64(bArr);
    }

    protected byte[] doEncoding(byte[] bArr) {
        return bArr == null ? null : Base64.encodeBase64(bArr);
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return encode((String) obj);
        }
        throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be encoded using BCodec");
    }

    public String encode(String str) throws EncoderException {
        return str == null ? null : encode(str, getDefaultCharset());
    }

    public String encode(String str, String str2) throws EncoderException {
        if (str == null) {
            return null;
        }
        try {
            return encodeText(str, str2);
        } catch (Throwable e) {
            throw new EncoderException(e.getMessage(), e);
        }
    }

    public String getDefaultCharset() {
        return this.charset;
    }

    protected String getEncoding() {
        return "B";
    }
}
