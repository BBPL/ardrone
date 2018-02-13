package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

@Immutable
public class PlainSocketFactory implements SocketFactory, SchemeSocketFactory {
    private final HostNameResolver nameResolver;

    public PlainSocketFactory() {
        this.nameResolver = null;
    }

    @Deprecated
    public PlainSocketFactory(HostNameResolver hostNameResolver) {
        this.nameResolver = hostNameResolver;
    }

    public static PlainSocketFactory getSocketFactory() {
        return new PlainSocketFactory();
    }

    @Deprecated
    public Socket connectSocket(Socket socket, String str, int i, InetAddress inetAddress, int i2, HttpParams httpParams) throws IOException, UnknownHostException, ConnectTimeoutException {
        InetSocketAddress inetSocketAddress = null;
        if (inetAddress != null || i2 > 0) {
            if (i2 < 0) {
                i2 = 0;
            }
            inetSocketAddress = new InetSocketAddress(inetAddress, i2);
        }
        return connectSocket(socket, new InetSocketAddress(this.nameResolver != null ? this.nameResolver.resolve(str) : InetAddress.getByName(str), i), inetSocketAddress, httpParams);
    }

    public Socket connectSocket(Socket socket, InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2, HttpParams httpParams) throws IOException, ConnectTimeoutException {
        if (inetSocketAddress == null) {
            throw new IllegalArgumentException("Remote address may not be null");
        } else if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            if (socket == null) {
                socket = createSocket();
            }
            if (inetSocketAddress2 != null) {
                socket.setReuseAddress(HttpConnectionParams.getSoReuseaddr(httpParams));
                socket.bind(inetSocketAddress2);
            }
            int connectionTimeout = HttpConnectionParams.getConnectionTimeout(httpParams);
            try {
                socket.setSoTimeout(HttpConnectionParams.getSoTimeout(httpParams));
                socket.connect(inetSocketAddress, connectionTimeout);
                return socket;
            } catch (SocketTimeoutException e) {
                throw new ConnectTimeoutException("Connect to " + inetSocketAddress + " timed out");
            }
        }
    }

    public Socket createSocket() {
        return new Socket();
    }

    public Socket createSocket(HttpParams httpParams) {
        return new Socket();
    }

    public final boolean isSecure(Socket socket) throws IllegalArgumentException {
        if (socket == null) {
            throw new IllegalArgumentException("Socket may not be null.");
        } else if (!socket.isClosed()) {
            return false;
        } else {
            throw new IllegalArgumentException("Socket is closed.");
        }
    }
}
