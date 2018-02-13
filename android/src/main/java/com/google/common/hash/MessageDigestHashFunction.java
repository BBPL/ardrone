package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

final class MessageDigestHashFunction extends AbstractStreamingHashFunction {
    private final String algorithmName;
    private final int bits;

    private static class MessageDigestHasher implements Hasher {
        private final MessageDigest digest;
        private boolean done;
        private final ByteBuffer scratch;

        private MessageDigestHasher(MessageDigest messageDigest) {
            this.digest = messageDigest;
            this.scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        }

        private void checkNotDone() {
            Preconditions.checkState(!this.done, "Cannot use Hasher after calling #hash() on it");
        }

        public HashCode hash() {
            this.done = true;
            return HashCodes.fromBytesNoCopy(this.digest.digest());
        }

        public Hasher putBoolean(boolean z) {
            return putByte(z ? (byte) 1 : (byte) 0);
        }

        public Hasher putByte(byte b) {
            checkNotDone();
            this.digest.update(b);
            return this;
        }

        public Hasher putBytes(byte[] bArr) {
            checkNotDone();
            this.digest.update(bArr);
            return this;
        }

        public Hasher putBytes(byte[] bArr, int i, int i2) {
            checkNotDone();
            Preconditions.checkPositionIndexes(i, i + i2, bArr.length);
            this.digest.update(bArr, i, i2);
            return this;
        }

        public Hasher putChar(char c) {
            checkNotDone();
            this.scratch.putChar(c);
            this.digest.update(this.scratch.array(), 0, 2);
            this.scratch.clear();
            return this;
        }

        public Hasher putDouble(double d) {
            checkNotDone();
            this.scratch.putDouble(d);
            this.digest.update(this.scratch.array(), 0, 8);
            this.scratch.clear();
            return this;
        }

        public Hasher putFloat(float f) {
            checkNotDone();
            this.scratch.putFloat(f);
            this.digest.update(this.scratch.array(), 0, 4);
            this.scratch.clear();
            return this;
        }

        public Hasher putInt(int i) {
            checkNotDone();
            this.scratch.putInt(i);
            this.digest.update(this.scratch.array(), 0, 4);
            this.scratch.clear();
            return this;
        }

        public Hasher putLong(long j) {
            checkNotDone();
            this.scratch.putLong(j);
            this.digest.update(this.scratch.array(), 0, 8);
            this.scratch.clear();
            return this;
        }

        public <T> Hasher putObject(T t, Funnel<? super T> funnel) {
            checkNotDone();
            funnel.funnel(t, this);
            return this;
        }

        public Hasher putShort(short s) {
            checkNotDone();
            this.scratch.putShort(s);
            this.digest.update(this.scratch.array(), 0, 2);
            this.scratch.clear();
            return this;
        }

        public Hasher putString(CharSequence charSequence) {
            for (int i = 0; i < charSequence.length(); i++) {
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

    MessageDigestHashFunction(String str) {
        this.algorithmName = str;
        this.bits = getMessageDigest(str).getDigestLength() * 8;
    }

    private static MessageDigest getMessageDigest(String str) {
        try {
            return MessageDigest.getInstance(str);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    public int bits() {
        return this.bits;
    }

    public Hasher newHasher() {
        return new MessageDigestHasher(getMessageDigest(this.algorithmName));
    }
}
