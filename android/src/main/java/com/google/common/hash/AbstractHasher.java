package com.google.common.hash;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

abstract class AbstractHasher implements Hasher {
    AbstractHasher() {
    }

    public final Hasher putBoolean(boolean z) {
        return putByte(z ? (byte) 1 : (byte) 0);
    }

    public final Hasher putDouble(double d) {
        return putLong(Double.doubleToRawLongBits(d));
    }

    public final Hasher putFloat(float f) {
        return putInt(Float.floatToRawIntBits(f));
    }

    public Hasher putString(CharSequence charSequence) {
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            putChar(charSequence.charAt(i));
        }
        return this;
    }

    public Hasher putString(CharSequence charSequence, Charset charset) {
        ByteBuffer encode = charset.encode(CharBuffer.wrap(charSequence));
        if (encode.hasArray()) {
            return putBytes(encode.array(), encode.arrayOffset() + encode.position(), encode.arrayOffset() + encode.limit());
        }
        byte[] bArr = new byte[encode.remaining()];
        encode.get(bArr);
        return putBytes(bArr);
    }
}
