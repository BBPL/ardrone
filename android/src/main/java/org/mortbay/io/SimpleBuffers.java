package org.mortbay.io;

public class SimpleBuffers implements Buffers {
    Buffer[] _buffers;

    public SimpleBuffers(Buffer[] bufferArr) {
        this._buffers = bufferArr;
    }

    public Buffer getBuffer(int i) {
        if (this._buffers != null) {
            int i2 = 0;
            while (i2 < this._buffers.length) {
                if (this._buffers[i2] == null || this._buffers[i2].capacity() != i) {
                    i2++;
                } else {
                    Buffer buffer = this._buffers[i2];
                    this._buffers[i2] = null;
                    return buffer;
                }
            }
        }
        return new ByteArrayBuffer(i);
    }

    public void returnBuffer(Buffer buffer) {
        buffer.clear();
        if (this._buffers != null) {
            for (int i = 0; i < this._buffers.length; i++) {
                if (this._buffers[i] == null) {
                    this._buffers[i] = buffer;
                }
            }
        }
    }
}
