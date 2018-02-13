package com.google.common.hash;

import com.google.common.primitives.UnsignedBytes;
import java.io.Serializable;
import java.nio.ByteBuffer;

final class Murmur3_32HashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private static final long serialVersionUID = 0;
    private final int seed;

    private static final class Murmur3_32Hasher extends AbstractStreamingHasher {
        private static final int CHUNK_SIZE = 4;
        private int h1;
        private int length = 0;

        Murmur3_32Hasher(int i) {
            super(4);
            this.h1 = i;
        }

        public HashCode makeHash() {
            return Murmur3_32HashFunction.fmix(this.h1, this.length);
        }

        protected void process(ByteBuffer byteBuffer) {
            this.h1 = Murmur3_32HashFunction.mixH1(this.h1, Murmur3_32HashFunction.mixK1(byteBuffer.getInt()));
            this.length += 4;
        }

        protected void processRemaining(ByteBuffer byteBuffer) {
            int i;
            this.length += byteBuffer.remaining();
            switch (byteBuffer.remaining()) {
                case 1:
                    i = 0;
                    break;
                case 2:
                    i = 0;
                    break;
                case 3:
                    i = (UnsignedBytes.toInt(byteBuffer.get(2)) << 16) ^ 0;
                    break;
                default:
                    throw new AssertionError("Should never get here.");
            }
            i ^= UnsignedBytes.toInt(byteBuffer.get(1)) << 8;
            int toInt = UnsignedBytes.toInt(byteBuffer.get(0));
            this.h1 = Murmur3_32HashFunction.mixK1(i ^ toInt) ^ this.h1;
        }
    }

    Murmur3_32HashFunction(int i) {
        this.seed = i;
    }

    private static HashCode fmix(int i, int i2) {
        int i3 = i ^ i2;
        i3 = (i3 ^ (i3 >>> 16)) * -2048144789;
        i3 = (i3 ^ (i3 >>> 13)) * -1028477387;
        return HashCodes.fromInt(i3 ^ (i3 >>> 16));
    }

    private static int mixH1(int i, int i2) {
        return (Integer.rotateLeft(i ^ i2, 13) * 5) - 430675100;
    }

    private static int mixK1(int i) {
        return Integer.rotateLeft(C1 * i, 15) * C2;
    }

    public int bits() {
        return 32;
    }

    public HashCode hashInt(int i) {
        return fmix(mixH1(this.seed, mixK1(i)), 4);
    }

    public HashCode hashLong(long j) {
        int i = (int) (j >>> 32);
        return fmix(mixH1(mixH1(this.seed, mixK1((int) j)), mixK1(i)), 8);
    }

    public HashCode hashString(CharSequence charSequence) {
        int i = this.seed;
        for (int i2 = 1; i2 < charSequence.length(); i2 += 2) {
            i = mixH1(i, mixK1(charSequence.charAt(i2 - 1) | (charSequence.charAt(i2) << 16)));
        }
        if ((charSequence.length() & 1) == 1) {
            i ^= mixK1(charSequence.charAt(charSequence.length() - 1));
        }
        return fmix(i, charSequence.length() * 2);
    }

    public Hasher newHasher() {
        return new Murmur3_32Hasher(this.seed);
    }
}
