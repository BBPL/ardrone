package com.google.common.io;

import com.google.common.annotations.Beta;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@Beta
public final class CountingInputStream extends FilterInputStream {
    private long count;
    private long mark = -1;

    public CountingInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public long getCount() {
        return this.count;
    }

    public void mark(int i) {
        synchronized (this) {
            this.in.mark(i);
            this.mark = this.count;
        }
    }

    public int read() throws IOException {
        int read = this.in.read();
        if (read != -1) {
            this.count++;
        }
        return read;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.in.read(bArr, i, i2);
        if (read != -1) {
            this.count += (long) read;
        }
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
                this.count = this.mark;
            }
        }
    }

    public long skip(long j) throws IOException {
        long skip = this.in.skip(j);
        this.count += skip;
        return skip;
    }
}
