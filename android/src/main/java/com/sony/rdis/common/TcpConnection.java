package com.sony.rdis.common;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpConnection implements SocketConnection {
    private static final String logTag = "RDIS_COMMON";
    private final int MAX_TCP_DATA_LENGTH = AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START;
    private final int SOCKET_TIMEOUT = 1000;
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;
    private Socket mSocket = null;

    public boolean connect(String str, int i) {
        boolean z = true;
        synchronized (this) {
            if (isConnected()) {
                disconnect();
            }
            try {
                String[] split = str.split("\\.");
                InetAddress byAddress = InetAddress.getByAddress(new byte[]{(byte) Integer.parseInt(split[0]), (byte) Integer.parseInt(split[1]), (byte) Integer.parseInt(split[2]), (byte) Integer.parseInt(split[3])});
                try {
                    Dbg.m1744i(logTag, "TCP CONNECT TO :" + byAddress.getHostAddress() + ":" + i);
                    this.mSocket = new Socket();
                    this.mSocket.setReuseAddress(true);
                    this.mSocket.connect(new InetSocketAddress(byAddress, i), 1000);
                    this.mInputStream = this.mSocket.getInputStream();
                    this.mOutputStream = this.mSocket.getOutputStream();
                } catch (Exception e) {
                    Dbg.printStackTrace(e);
                    disconnect();
                    z = false;
                }
            } catch (Exception e2) {
                Dbg.printStackTrace(e2);
                z = false;
            } catch (Exception e22) {
                Dbg.printStackTrace(e22);
                z = false;
            }
        }
        return z;
    }

    public void disconnect() {
        synchronized (this) {
            if (this.mSocket != null) {
                try {
                    this.mSocket.shutdownInput();
                    this.mSocket.shutdownOutput();
                    this.mSocket.close();
                } catch (Exception e) {
                    Dbg.printStackTrace(e);
                }
                this.mSocket = null;
            }
            this.mInputStream = null;
            this.mOutputStream = null;
        }
    }

    public boolean isConnected() {
        return this.mSocket != null && this.mSocket.isConnected();
    }

    public byte[] read(int i) throws IOException {
        byte[] bArr = null;
        if (i > AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START) {
            Dbg.m1742e(logTag, "ERROR; read request size");
        } else if (isConnected()) {
            bArr = new byte[i];
            int i2 = 0;
            while (i2 < i) {
                try {
                    int read = this.mInputStream.read(bArr, i2, i - i2);
                    if (read <= 0) {
                        throw new IOException();
                    }
                    i2 += read;
                } catch (Exception e) {
                    Dbg.printStackTrace(e);
                    disconnect();
                    throw new IOException();
                } catch (Exception e2) {
                    Dbg.printStackTrace(e2);
                    disconnect();
                    throw new IOException();
                }
            }
        } else {
            Dbg.m1742e(logTag, "ERROR; socket disconnected.");
        }
        return bArr;
    }

    public boolean write(byte[] bArr) throws IOException {
        boolean z = false;
        synchronized (this) {
            if (bArr.length <= 0 || bArr.length > AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START) {
                Dbg.m1742e(logTag, "ERROR; data is empty or large." + bArr.length);
            } else if (isConnected()) {
                try {
                    this.mOutputStream.write(bArr);
                    z = true;
                } catch (Exception e) {
                    Dbg.printStackTrace(e);
                    throw new IOException();
                } catch (Exception e2) {
                    Dbg.printStackTrace(e2);
                    throw new IOException();
                }
            } else {
                Dbg.m1742e(logTag, "ERROR: tcp socket disconnected.");
            }
        }
        return z;
    }
}
