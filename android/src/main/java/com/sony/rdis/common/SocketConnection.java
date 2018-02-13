package com.sony.rdis.common;

import java.io.IOException;

public interface SocketConnection {
    boolean connect(String str, int i);

    void disconnect();

    boolean isConnected();

    byte[] read(int i) throws IOException;

    boolean write(byte[] bArr) throws IOException;
}
