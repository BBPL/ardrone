package com.sony.rdis.common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpConnection implements SocketConnection {
    private static final String logTag = "RDIS_COMMON";
    private final int MAX_UDP_DATA_LENGTH = 1024;
    private InetAddress mInetAddress = null;
    private int mPortNumber = -1;
    private DatagramSocket mSocket = null;

    public boolean connect(String str, int i) {
        boolean z = true;
        synchronized (this) {
            if (isConnected()) {
                disconnect();
            }
            try {
                String[] split = str.split("\\.");
                byte parseInt = (byte) Integer.parseInt(split[0]);
                byte parseInt2 = (byte) Integer.parseInt(split[1]);
                byte parseInt3 = (byte) Integer.parseInt(split[2]);
                byte parseInt4 = (byte) Integer.parseInt(split[3]);
                this.mInetAddress = null;
                this.mInetAddress = InetAddress.getByAddress(new byte[]{parseInt, parseInt2, parseInt3, parseInt4});
                try {
                    this.mPortNumber = i;
                    Dbg.m1744i(logTag, "UDP CONNECT TO :" + this.mInetAddress.getHostAddress() + ":" + this.mPortNumber);
                    this.mSocket = new DatagramSocket();
                    this.mSocket.setReuseAddress(true);
                } catch (Exception e) {
                    Dbg.printStackTrace(e);
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
                this.mSocket.close();
                this.mSocket = null;
            }
            this.mInetAddress = null;
        }
    }

    public boolean isConnected() {
        return this.mSocket != null;
    }

    public byte[] read(int i) throws IOException {
        Dbg.m1742e(logTag, "ERROR: Udp read method is not supported!");
        throw new IOException();
    }

    public boolean write(byte[] bArr) throws IOException {
        boolean z = false;
        synchronized (this) {
            if (bArr.length <= 0 || bArr.length > 1024) {
                Dbg.m1742e(logTag, "ERROR; data is empty or large. " + bArr.length);
            } else if (isConnected()) {
                try {
                    this.mSocket.send(new DatagramPacket(bArr, bArr.length, this.mInetAddress, this.mPortNumber));
                    z = true;
                } catch (Exception e) {
                    Dbg.printStackTrace(e);
                    throw new IOException();
                }
            } else {
                Dbg.m1742e(logTag, "ERROR: udp socket disconnected.");
            }
        }
        return z;
    }
}
