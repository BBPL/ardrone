package org.apache.http.impl.io;

import com.parrot.ardronetool.ConfigArdroneMask;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.ConnectionClosedException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;

@NotThreadSafe
public class ContentLengthInputStream extends InputStream {
    private static final int BUFFER_SIZE = 2048;
    private boolean closed = false;
    private long contentLength;
    private SessionInputBuffer in = null;
    private long pos = 0;

    public ContentLengthInputStream(SessionInputBuffer sessionInputBuffer, long j) {
        if (sessionInputBuffer == null) {
            throw new IllegalArgumentException("Input stream may not be null");
        } else if (j < 0) {
            throw new IllegalArgumentException("Content length may not be negative");
        } else {
            this.in = sessionInputBuffer;
            this.contentLength = j;
        }
    }

    public int available() throws IOException {
        return this.in instanceof BufferInfo ? Math.min(((BufferInfo) this.in).length(), (int) (this.contentLength - this.pos)) : 0;
    }

    public void close() throws IOException {
        if (!this.closed) {
            try {
                if (this.pos < this.contentLength) {
                    do {
                    } while (read(new byte[2048]) >= 0);
                }
                this.closed = true;
            } catch (Throwable th) {
                this.closed = true;
            }
        }
    }

    public int read() throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        } else if (this.pos >= this.contentLength) {
            return -1;
        } else {
            int read = this.in.read();
            if (read != -1) {
                this.pos++;
                return read;
            } else if (this.pos >= this.contentLength) {
                return read;
            } else {
                throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + this.contentLength + "; received: " + this.pos);
            }
        }
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        } else if (this.pos >= this.contentLength) {
            return -1;
        } else {
            if (this.pos + ((long) i2) > this.contentLength) {
                i2 = (int) (this.contentLength - this.pos);
            }
            int read = this.in.read(bArr, i, i2);
            if (read == -1 && this.pos < this.contentLength) {
                throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + this.contentLength + "; received: " + this.pos);
            } else if (read <= 0) {
                return read;
            } else {
                this.pos += (long) read;
                return read;
            }
        }
    }

    public long skip(long j) throws IOException {
        if (j <= 0) {
            return 0;
        }
        byte[] bArr = new byte[2048];
        long min = Math.min(j, this.contentLength - this.pos);
        long j2 = 0;
        while (min > 0) {
            int read = read(bArr, 0, (int) Math.min(ConfigArdroneMask.ARDRONE_NAVDATA_BOOTSTRAP, min));
            if (read == -1) {
                break;
            }
            min -= (long) read;
            j2 = ((long) read) + j2;
        }
        return j2;
    }
}
