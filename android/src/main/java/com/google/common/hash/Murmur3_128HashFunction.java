package com.google.common.hash;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class Murmur3_128HashFunction extends AbstractStreamingHashFunction implements Serializable {
    private static final long serialVersionUID = 0;
    private final int seed;

    private static final class Murmur3_128Hasher extends AbstractStreamingHasher {
        private static final long C1 = -8663945395140668459L;
        private static final long C2 = 5545529020109919103L;
        private static final int CHUNK_SIZE = 16;
        private long h1;
        private long h2;
        private int length = 0;

        Murmur3_128Hasher(int i) {
            super(16);
            this.h1 = (long) i;
            this.h2 = (long) i;
        }

        private void bmix64(long j, long j2) {
            this.h1 ^= mixK1(j);
            this.h1 = Long.rotateLeft(this.h1, 27);
            this.h1 += this.h2;
            this.h1 = (this.h1 * 5) + 1390208809;
            this.h2 ^= mixK2(j2);
            this.h2 = Long.rotateLeft(this.h2, 31);
            this.h2 += this.h1;
            this.h2 = (this.h2 * 5) + 944331445;
        }

        private static long fmix64(long j) {
            long j2 = ((j >>> 33) ^ j) * -49064778989728563L;
            j2 = (j2 ^ (j2 >>> 33)) * -4265267296055464877L;
            return j2 ^ (j2 >>> 33);
        }

        private static long mixK1(long j) {
            return Long.rotateLeft(C1 * j, 31) * C2;
        }

        private static long mixK2(long j) {
            return Long.rotateLeft(C2 * j, 33) * C1;
        }

        public HashCode makeHash() {
            this.h1 ^= (long) this.length;
            this.h2 ^= (long) this.length;
            this.h1 += this.h2;
            this.h2 += this.h1;
            this.h1 = fmix64(this.h1);
            this.h2 = fmix64(this.h2);
            this.h1 += this.h2;
            this.h2 += this.h1;
            return HashCodes.fromBytesNoCopy(ByteBuffer.wrap(new byte[16]).order(ByteOrder.LITTLE_ENDIAN).putLong(this.h1).putLong(this.h2).array());
        }

        protected void process(ByteBuffer byteBuffer) {
            bmix64(byteBuffer.getLong(), byteBuffer.getLong());
            this.length += 16;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected void processRemaining(java.nio.ByteBuffer r14) {
            /*
            r13 = this;
            r10 = 32;
            r9 = 24;
            r8 = 16;
            r7 = 8;
            r2 = 0;
            r0 = r13.length;
            r1 = r14.remaining();
            r0 = r0 + r1;
            r13.length = r0;
            r0 = r14.remaining();
            switch(r0) {
                case 1: goto L_0x0110;
                case 2: goto L_0x010e;
                case 3: goto L_0x010c;
                case 4: goto L_0x010a;
                case 5: goto L_0x0108;
                case 6: goto L_0x0106;
                case 7: goto L_0x0099;
                case 8: goto L_0x0103;
                case 9: goto L_0x0100;
                case 10: goto L_0x00fd;
                case 11: goto L_0x00fa;
                case 12: goto L_0x00f7;
                case 13: goto L_0x00f4;
                case 14: goto L_0x00f1;
                case 15: goto L_0x0022;
                default: goto L_0x001a;
            };
        L_0x001a:
            r0 = new java.lang.AssertionError;
            r1 = "Should never get here.";
            r0.<init>(r1);
            throw r0;
        L_0x0022:
            r0 = 14;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r0 = (long) r0;
            r4 = 48;
            r0 = r0 << r4;
            r0 = r0 ^ r2;
        L_0x0031:
            r4 = 13;
            r4 = r14.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r6 = 40;
            r4 = r4 << r6;
            r0 = r0 ^ r4;
        L_0x0040:
            r4 = 12;
            r4 = r14.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r10;
            r0 = r0 ^ r4;
        L_0x004d:
            r4 = 11;
            r4 = r14.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r9;
            r0 = r0 ^ r4;
        L_0x005a:
            r4 = 10;
            r4 = r14.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r8;
            r0 = r0 ^ r4;
        L_0x0067:
            r4 = 9;
            r4 = r14.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r7;
            r0 = r0 ^ r4;
        L_0x0074:
            r4 = r14.get(r7);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r0 = r0 ^ r4;
        L_0x007e:
            r4 = r14.getLong();
            r2 = r2 ^ r4;
            r11 = r2;
            r2 = r0;
            r0 = r11;
        L_0x0086:
            r4 = r13.h1;
            r0 = mixK1(r0);
            r0 = r0 ^ r4;
            r13.h1 = r0;
            r0 = r13.h2;
            r2 = mixK2(r2);
            r0 = r0 ^ r2;
            r13.h2 = r0;
            return;
        L_0x0099:
            r0 = 6;
            r0 = r14.get(r0);
            r0 = com.google.common.primitives.UnsignedBytes.toInt(r0);
            r0 = (long) r0;
            r4 = 48;
            r0 = r0 << r4;
            r0 = r0 ^ r2;
        L_0x00a7:
            r4 = 5;
            r4 = r14.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r6 = 40;
            r4 = r4 << r6;
            r0 = r0 ^ r4;
        L_0x00b5:
            r4 = 4;
            r4 = r14.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r10;
            r0 = r0 ^ r4;
        L_0x00c1:
            r4 = 3;
            r4 = r14.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r9;
            r0 = r0 ^ r4;
        L_0x00cd:
            r4 = 2;
            r4 = r14.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r8;
            r0 = r0 ^ r4;
        L_0x00d9:
            r4 = 1;
            r4 = r14.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r4 = r4 << r7;
            r0 = r0 ^ r4;
        L_0x00e5:
            r4 = 0;
            r4 = r14.get(r4);
            r4 = com.google.common.primitives.UnsignedBytes.toInt(r4);
            r4 = (long) r4;
            r0 = r0 ^ r4;
            goto L_0x0086;
        L_0x00f1:
            r0 = r2;
            goto L_0x0031;
        L_0x00f4:
            r0 = r2;
            goto L_0x0040;
        L_0x00f7:
            r0 = r2;
            goto L_0x004d;
        L_0x00fa:
            r0 = r2;
            goto L_0x005a;
        L_0x00fd:
            r0 = r2;
            goto L_0x0067;
        L_0x0100:
            r0 = r2;
            goto L_0x0074;
        L_0x0103:
            r0 = r2;
            goto L_0x007e;
        L_0x0106:
            r0 = r2;
            goto L_0x00a7;
        L_0x0108:
            r0 = r2;
            goto L_0x00b5;
        L_0x010a:
            r0 = r2;
            goto L_0x00c1;
        L_0x010c:
            r0 = r2;
            goto L_0x00cd;
        L_0x010e:
            r0 = r2;
            goto L_0x00d9;
        L_0x0110:
            r0 = r2;
            goto L_0x00e5;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.hash.Murmur3_128HashFunction.Murmur3_128Hasher.processRemaining(java.nio.ByteBuffer):void");
        }
    }

    Murmur3_128HashFunction(int i) {
        this.seed = i;
    }

    public int bits() {
        return 128;
    }

    public Hasher newHasher() {
        return new Murmur3_128Hasher(this.seed);
    }
}
