package com.sony.rdis.common;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.net.LocalSocketAddress.Namespace;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LocalConnection implements SocketConnection {
    private static final String logTag = "RDIS_COMMON";
    private final int MAX_DATA_LENGTH = AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START;
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;
    private LocalSocket mSocket = null;

    public boolean connect(String str, int i) {
        if (isConnected()) {
            disconnect();
        }
        try {
            this.mSocket = new LocalSocket();
            this.mSocket.connect(new LocalSocketAddress(str, Namespace.RESERVED));
            this.mInputStream = this.mSocket.getInputStream();
            this.mOutputStream = this.mSocket.getOutputStream();
            return true;
        } catch (Exception e) {
            Dbg.printStackTrace(e);
            disconnect();
            return false;
        }
    }

    public void disconnect() {
        if (this.mSocket != null) {
            try {
                this.mSocket.close();
            } catch (Exception e) {
                Dbg.printStackTrace(e);
            }
            this.mSocket = null;
        }
        this.mInputStream = null;
        this.mOutputStream = null;
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
                }
            }
        } else {
            Dbg.m1742e(logTag, "ERROR; socket disconnected.");
        }
        return bArr;
    }

    public boolean write(byte[] bArr) throws IOException {
        if (bArr.length <= 0 || bArr.length > AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START) {
            Dbg.m1742e(logTag, "ERROR; data is empty or large." + bArr.length);
            return false;
        } else if (isConnected()) {
            try {
                this.mOutputStream.write(bArr);
                return true;
            } catch (Exception e) {
                Dbg.printStackTrace(e);
                throw new IOException();
            } catch (Exception e2) {
                Dbg.printStackTrace(e2);
                throw new IOException();
            }
        } else {
            Dbg.m1742e(logTag, "ERROR; socket disconnected.");
            return false;
        }
    }
}
