package org.mortbay.io.bio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.mortbay.io.Portable;

public class SocketEndPoint extends StreamEndPoint {
    final InetSocketAddress _local = ((InetSocketAddress) this._socket.getLocalSocketAddress());
    final InetSocketAddress _remote = ((InetSocketAddress) this._socket.getRemoteSocketAddress());
    final Socket _socket;

    public SocketEndPoint(Socket socket) throws IOException {
        super(socket.getInputStream(), socket.getOutputStream());
        this._socket = socket;
    }

    public void close() throws IOException {
        this._socket.close();
        this._in = null;
        this._out = null;
    }

    public String getLocalAddr() {
        return (this._local == null || this._local.getAddress() == null || this._local.getAddress().isAnyLocalAddress()) ? Portable.ALL_INTERFACES : this._local.getAddress().getHostAddress();
    }

    public String getLocalHost() {
        return (this._local == null || this._local.getAddress() == null || this._local.getAddress().isAnyLocalAddress()) ? Portable.ALL_INTERFACES : this._local.getAddress().getCanonicalHostName();
    }

    public int getLocalPort() {
        return this._local == null ? -1 : this._local.getPort();
    }

    public String getRemoteAddr() {
        if (this._remote != null) {
            InetAddress address = this._remote.getAddress();
            if (address != null) {
                return address.getHostAddress();
            }
        }
        return null;
    }

    public String getRemoteHost() {
        return this._remote == null ? null : this._remote.getAddress().getCanonicalHostName();
    }

    public int getRemotePort() {
        return this._remote == null ? -1 : this._remote.getPort();
    }

    public Object getTransport() {
        return this._socket;
    }

    public boolean isOpen() {
        return (!super.isOpen() || this._socket == null || this._socket.isClosed() || this._socket.isInputShutdown() || this._socket.isOutputShutdown()) ? false : true;
    }

    public void shutdownOutput() throws IOException {
        if (!this._socket.isClosed() && !this._socket.isOutputShutdown()) {
            this._socket.shutdownOutput();
        }
    }
}
