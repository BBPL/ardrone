package org.mortbay.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.mortbay.io.Buffer;
import org.mortbay.io.EndPoint;

public class StreamEndPoint implements EndPoint {
    InputStream _in;
    OutputStream _out;

    public StreamEndPoint(InputStream inputStream, OutputStream outputStream) {
        this._in = inputStream;
        this._out = outputStream;
    }

    public boolean blockReadable(long j) throws IOException {
        return true;
    }

    public boolean blockWritable(long j) throws IOException {
        return true;
    }

    public void close() throws IOException {
        if (this._in != null) {
            this._in.close();
        }
        this._in = null;
        if (this._out != null) {
            this._out.close();
        }
        this._out = null;
    }

    public int fill(Buffer buffer) throws IOException {
        if (this._in != null) {
            int space = buffer.space();
            if (space > 0) {
                return buffer.readFrom(this._in, space);
            }
            if (!buffer.hasContent()) {
                throw new IOException("FULL");
            }
        }
        return 0;
    }

    public int flush(Buffer buffer) throws IOException {
        if (this._out == null) {
            return -1;
        }
        int length = buffer.length();
        if (length > 0) {
            buffer.writeTo(this._out);
        }
        buffer.clear();
        return length;
    }

    public int flush(Buffer buffer, Buffer buffer2, Buffer buffer3) throws IOException {
        int length;
        int flush;
        int i = 0;
        if (buffer != null) {
            length = buffer.length();
            if (length > 0) {
                flush = flush(buffer);
                if (flush < length) {
                    return flush;
                }
                i = flush;
            }
        }
        if (buffer2 != null) {
            length = buffer2.length();
            if (length > 0) {
                flush = flush(buffer2);
                if (flush < 0) {
                    return i > 0 ? i : flush;
                } else {
                    i += flush;
                    if (flush < length) {
                        return i;
                    }
                }
            }
        }
        if (buffer3 == null || buffer3.length() <= 0) {
            return i;
        }
        flush = flush(buffer3);
        return flush < 0 ? i > 0 ? i : flush : flush + i;
    }

    public void flush() throws IOException {
        this._out.flush();
    }

    public InputStream getInputStream() {
        return this._in;
    }

    public String getLocalAddr() {
        return null;
    }

    public String getLocalHost() {
        return null;
    }

    public int getLocalPort() {
        return 0;
    }

    public OutputStream getOutputStream() {
        return this._out;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public int getRemotePort() {
        return 0;
    }

    public Object getTransport() {
        return null;
    }

    public boolean isBlocking() {
        return true;
    }

    public boolean isBufferingInput() {
        return false;
    }

    public boolean isBufferingOutput() {
        return false;
    }

    public boolean isBufferred() {
        return false;
    }

    public final boolean isClosed() {
        return !isOpen();
    }

    public boolean isOpen() {
        return this._in != null;
    }

    public void setInputStream(InputStream inputStream) {
        this._in = inputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this._out = outputStream;
    }

    public void shutdownOutput() throws IOException {
    }
}
