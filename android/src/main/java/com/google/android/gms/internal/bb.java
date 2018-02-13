package com.google.android.gms.internal;

import android.net.LocalSocket;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import com.google.android.gms.games.RealTimeSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class bb implements RealTimeSocket {
    private ParcelFileDescriptor aB;
    private final String dX;
    private final LocalSocket en;

    bb(LocalSocket localSocket, String str) {
        this.en = localSocket;
        this.dX = str;
    }

    public void close() throws IOException {
        this.en.close();
    }

    public InputStream getInputStream() throws IOException {
        return this.en.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return this.en.getOutputStream();
    }

    public ParcelFileDescriptor getParcelFileDescriptor() throws IOException {
        if (this.aB == null && !isClosed()) {
            Parcel obtain = Parcel.obtain();
            obtain.writeFileDescriptor(this.en.getFileDescriptor());
            obtain.setDataPosition(0);
            this.aB = obtain.readFileDescriptor();
        }
        return this.aB;
    }

    public boolean isClosed() {
        return (this.en.isConnected() || this.en.isBound()) ? false : true;
    }
}
