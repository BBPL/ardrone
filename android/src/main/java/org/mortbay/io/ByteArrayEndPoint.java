package org.mortbay.io;

import java.io.IOException;

public class ByteArrayEndPoint implements EndPoint {
    boolean _closed;
    boolean _growOutput;
    ByteArrayBuffer _in;
    byte[] _inBytes;
    boolean _nonBlocking;
    ByteArrayBuffer _out;

    public ByteArrayEndPoint(byte[] bArr, int i) {
        this._inBytes = bArr;
        this._in = new ByteArrayBuffer(bArr);
        this._out = new ByteArrayBuffer(i);
    }

    public boolean blockReadable(long j) {
        return true;
    }

    public boolean blockWritable(long j) {
        return true;
    }

    public void close() throws IOException {
        this._closed = true;
    }

    public int fill(Buffer buffer) throws IOException {
        if (this._closed) {
            throw new IOException("CLOSED");
        }
        if (this._in != null) {
            if (this._in.length() > 0) {
                int put = buffer.put(this._in);
                this._in.skip(put);
                return put;
            } else if (this._nonBlocking) {
                return 0;
            }
        }
        return -1;
    }

    public int flush(Buffer buffer) throws IOException {
        if (this._closed) {
            throw new IOException("CLOSED");
        }
        if (this._growOutput && buffer.length() > this._out.space()) {
            this._out.compact();
            if (buffer.length() > this._out.space()) {
                ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(this._out.putIndex() + buffer.length());
                byteArrayBuffer.put(this._out.peek(0, this._out.putIndex()));
                if (this._out.getIndex() > 0) {
                    byteArrayBuffer.mark();
                    byteArrayBuffer.setGetIndex(this._out.getIndex());
                }
                this._out = byteArrayBuffer;
            }
        }
        int put = this._out.put(buffer);
        buffer.skip(put);
        return put;
    }

    public int flush(Buffer buffer, Buffer buffer2, Buffer buffer3) throws IOException {
        if (this._closed) {
            throw new IOException("CLOSED");
        }
        int i = 0;
        if (buffer != null && buffer.length() > 0) {
            i = flush(buffer);
        }
        if (buffer != null && buffer.length() != 0) {
            return i;
        }
        if (buffer2 != null && buffer2.length() > 0) {
            i += flush(buffer2);
        }
        return ((buffer2 == null || buffer2.length() == 0) && buffer3 != null && buffer3.length() > 0) ? i + flush(buffer3) : i;
    }

    public void flush() throws IOException {
    }

    public ByteArrayBuffer getIn() {
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

    public ByteArrayBuffer getOut() {
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
        return this._inBytes;
    }

    public boolean isBlocking() {
        return !this._nonBlocking;
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

    public boolean isGrowOutput() {
        return this._growOutput;
    }

    public boolean isNonBlocking() {
        return this._nonBlocking;
    }

    public boolean isOpen() {
        return !this._closed;
    }

    public void reset() {
        this._closed = false;
        this._in.clear();
        this._out.clear();
        if (this._inBytes != null) {
            this._in.setPutIndex(this._inBytes.length);
        }
    }

    public void setGrowOutput(boolean z) {
        this._growOutput = z;
    }

    public void setIn(ByteArrayBuffer byteArrayBuffer) {
        this._in = byteArrayBuffer;
    }

    public void setNonBlocking(boolean z) {
        this._nonBlocking = z;
    }

    public void setOut(ByteArrayBuffer byteArrayBuffer) {
        this._out = byteArrayBuffer;
    }

    public void shutdownOutput() throws IOException {
    }
}
