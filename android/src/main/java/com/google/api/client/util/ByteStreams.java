package com.google.api.client.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ByteStreams {
    private static final int BUF_SIZE = 4096;

    private static final class LimitedInputStream extends FilterInputStream {
        private long left;
        private long mark = -1;

        LimitedInputStream(InputStream inputStream, long j) {
            super(inputStream);
            Preconditions.checkNotNull(inputStream);
            Preconditions.checkArgument(j >= 0, "limit must be non-negative");
            this.left = j;
        }

        public int available() throws IOException {
            return (int) Math.min((long) this.in.available(), this.left);
        }

        public void mark(int i) {
            synchronized (this) {
                this.in.mark(i);
                this.mark = this.left;
            }
        }

        public int read() throws IOException {
            if (this.left == 0) {
                return -1;
            }
            int read = this.in.read();
            if (read == -1) {
                return read;
            }
            this.left--;
            return read;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (this.left == 0) {
                return -1;
            }
            int read = this.in.read(bArr, i, (int) Math.min((long) i2, this.left));
            if (read == -1) {
                return read;
            }
            this.left -= (long) read;
            return read;
        }

        public void reset() throws IOException {
            synchronized (this) {
                if (!this.in.markSupported()) {
                    throw new IOException("Mark not supported");
                } else if (this.mark == -1) {
                    throw new IOException("Mark not set");
                } else {
                    this.in.reset();
                    this.left = this.mark;
                }
            }
        }

        public long skip(long j) throws IOException {
            long skip = this.in.skip(Math.min(j, this.left));
            this.left -= skip;
            return skip;
        }
    }

    private ByteStreams() {
    }

    public static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(outputStream);
        byte[] bArr = new byte[4096];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }

    public static InputStream limit(InputStream inputStream, long j) {
        return new LimitedInputStream(inputStream, j);
    }

    public static int read(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(bArr);
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        }
        int i3 = 0;
        while (i3 < i2) {
            int read = inputStream.read(bArr, i + i3, i2 - i3);
            if (read == -1) {
                break;
            }
            i3 += read;
        }
        return i3;
    }
}
