package org.mortbay.io;

import java.io.IOException;

public interface EndPoint {
    boolean blockReadable(long j) throws IOException;

    boolean blockWritable(long j) throws IOException;

    void close() throws IOException;

    int fill(Buffer buffer) throws IOException;

    int flush(Buffer buffer) throws IOException;

    int flush(Buffer buffer, Buffer buffer2, Buffer buffer3) throws IOException;

    void flush() throws IOException;

    String getLocalAddr();

    String getLocalHost();

    int getLocalPort();

    String getRemoteAddr();

    String getRemoteHost();

    int getRemotePort();

    Object getTransport();

    boolean isBlocking();

    boolean isBufferingInput();

    boolean isBufferingOutput();

    boolean isBufferred();

    boolean isOpen();

    void shutdownOutput() throws IOException;
}
