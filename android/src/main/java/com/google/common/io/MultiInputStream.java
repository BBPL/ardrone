package com.google.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

final class MultiInputStream extends InputStream {
    private InputStream in;
    private Iterator<? extends InputSupplier<? extends InputStream>> it;

    public MultiInputStream(Iterator<? extends InputSupplier<? extends InputStream>> it) throws IOException {
        this.it = it;
        advance();
    }

    private void advance() throws IOException {
        close();
        if (this.it.hasNext()) {
            this.in = (InputStream) ((InputSupplier) this.it.next()).getInput();
        }
    }

    public int available() throws IOException {
        return this.in == null ? 0 : this.in.available();
    }

    public void close() throws IOException {
        if (this.in != null) {
            try {
                this.in.close();
            } finally {
                this.in = null;
            }
        }
    }

    public boolean markSupported() {
        return false;
    }

    public int read() throws IOException {
        if (this.in == null) {
            return -1;
        }
        int read = this.in.read();
        if (read != -1) {
            return read;
        }
        advance();
        return read();
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (this.in == null) {
            return -1;
        }
        int read = this.in.read(bArr, i, i2);
        if (read != -1) {
            return read;
        }
        advance();
        return read(bArr, i, i2);
    }

    public long skip(long j) throws IOException {
        long j2;
        if (this.in == null || j <= 0) {
            j2 = 0;
        } else {
            j2 = this.in.skip(j);
            if (j2 == 0) {
                return read() != -1 ? 1 + this.in.skip(j - 1) : 0;
            }
        }
        return j2;
    }
}
