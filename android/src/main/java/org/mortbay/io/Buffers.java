package org.mortbay.io;

public interface Buffers {
    Buffer getBuffer(int i);

    void returnBuffer(Buffer buffer);
}
