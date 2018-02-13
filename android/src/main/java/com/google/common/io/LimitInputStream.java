package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@Beta
public final class LimitInputStream extends FilterInputStream {
    private long left;
    private long mark = -1;

    public LimitInputStream(InputStream inputStream, long j) {
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
