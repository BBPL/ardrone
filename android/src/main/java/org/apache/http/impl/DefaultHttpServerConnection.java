package org.apache.http.impl;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

@NotThreadSafe
public class DefaultHttpServerConnection extends SocketHttpServerConnection {
    public void bind(Socket socket, HttpParams httpParams) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("Socket may not be null");
        } else if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            assertNotOpen();
            socket.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(httpParams));
            socket.setSoTimeout(HttpConnectionParams.getSoTimeout(httpParams));
            socket.setKeepAlive(HttpConnectionParams.getSoKeepalive(httpParams));
            int linger = HttpConnectionParams.getLinger(httpParams);
            if (linger >= 0) {
                socket.setSoLinger(linger > 0, linger);
            }
            super.bind(socket, httpParams);
        }
    }
}
