package org.mortbay.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class WriterOutputStream extends OutputStream {
    private byte[] _buf;
    protected String _encoding;
    protected Writer _writer;

    public WriterOutputStream(Writer writer) {
        this._buf = new byte[1];
        this._writer = writer;
    }

    public WriterOutputStream(Writer writer, String str) {
        this._buf = new byte[1];
        this._writer = writer;
        this._encoding = str;
    }

    public void close() throws IOException {
        this._writer.close();
        this._writer = null;
        this._encoding = null;
    }

    public void flush() throws IOException {
        this._writer.flush();
    }

    public void write(int i) throws IOException {
        synchronized (this) {
            this._buf[0] = (byte) i;
            write(this._buf);
        }
    }

    public void write(byte[] bArr) throws IOException {
        if (this._encoding == null) {
            this._writer.write(new String(bArr));
        } else {
            this._writer.write(new String(bArr, this._encoding));
        }
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        if (this._encoding == null) {
            this._writer.write(new String(bArr, i, i2));
        } else {
            this._writer.write(new String(bArr, i, i2, this._encoding));
        }
    }
}
