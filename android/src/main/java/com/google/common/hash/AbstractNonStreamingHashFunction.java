package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

abstract class AbstractNonStreamingHashFunction implements HashFunction {

    private final class BufferingHasher extends AbstractHasher {
        static final int BOTTOM_BYTE = 255;
        final ExposedByteArrayOutputStream stream;

        BufferingHasher(int i) {
            this.stream = new ExposedByteArrayOutputStream(i);
        }

        public HashCode hash() {
            return AbstractNonStreamingHashFunction.this.hashBytes(this.stream.byteArray(), 0, this.stream.length());
        }

        public Hasher putByte(byte b) {
            this.stream.write(b);
            return this;
        }

        public Hasher putBytes(byte[] bArr) {
            try {
                this.stream.write(bArr);
                return this;
            } catch (Throwable e) {
                throw Throwables.propagate(e);
            }
        }

        public Hasher putBytes(byte[] bArr, int i, int i2) {
            this.stream.write(bArr, i, i2);
            return this;
        }

        public Hasher putChar(char c) {
            this.stream.write(c & 255);
            this.stream.write((c >>> 8) & 255);
            return this;
        }

        public Hasher putInt(int i) {
            this.stream.write(i & 255);
            this.stream.write((i >>> 8) & 255);
            this.stream.write((i >>> 16) & 255);
            this.stream.write((i >>> 24) & 255);
            return this;
        }

        public Hasher putLong(long j) {
            for (int i = 0; i < 64; i += 8) {
                this.stream.write((byte) ((int) ((j >>> i) & 255)));
            }
            return this;
        }

        public <T> Hasher putObject(T t, Funnel<? super T> funnel) {
            funnel.funnel(t, this);
            return this;
        }

        public Hasher putShort(short s) {
            this.stream.write(s & 255);
            this.stream.write((s >>> 8) & 255);
            return this;
        }
    }

    private static final class ExposedByteArrayOutputStream extends ByteArrayOutputStream {
        ExposedByteArrayOutputStream(int i) {
            super(i);
        }

        byte[] byteArray() {
            return this.buf;
        }

        int length() {
            return this.count;
        }
    }

    AbstractNonStreamingHashFunction() {
    }

    public HashCode hashBytes(byte[] bArr) {
        return hashBytes(bArr, 0, bArr.length);
    }

    public HashCode hashInt(int i) {
        return newHasher(4).putInt(i).hash();
    }

    public HashCode hashLong(long j) {
        return newHasher(8).putLong(j).hash();
    }

    public HashCode hashString(CharSequence charSequence) {
        int length = charSequence.length();
        Hasher newHasher = newHasher(length * 2);
        for (int i = 0; i < length; i++) {
            newHasher.putChar(charSequence.charAt(i));
        }
        return newHasher.hash();
    }

    public HashCode hashString(CharSequence charSequence, Charset charset) {
        ByteBuffer encode = charset.encode(CharBuffer.wrap(charSequence));
        if (encode.hasArray()) {
            return hashBytes(encode.array(), encode.arrayOffset() + encode.position(), encode.arrayOffset() + encode.limit());
        }
        byte[] bArr = new byte[encode.remaining()];
        encode.get(bArr);
        return hashBytes(bArr);
    }

    public Hasher newHasher() {
        return new BufferingHasher(32);
    }

    public Hasher newHasher(int i) {
        Preconditions.checkArgument(i >= 0);
        return new BufferingHasher(i);
    }
}
