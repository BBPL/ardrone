package com.parrot.ardronetool.ftp;

import android.util.Log;
import com.parrot.ardronetool.ftp.FTPClientStatus.FTPStatus;
import java.io.OutputStream;
import org.mortbay.jetty.HttpVersions;

public class FTPClient {
    private static final String TAG = "FTPClient";
    private boolean busy = false;
    private int connectionHandle = 0;
    private FTPOperation currOperation;
    private int ftpStatus;
    private FTPProgressListener listener;

    private void callback(int i, float f, String str) {
        this.ftpStatus = i;
        FTPStatus translateStatus = FTPClientStatus.translateStatus(i);
        if (this.listener != null) {
            this.listener.onStatusChanged(translateStatus, f, this.currOperation);
        }
        if (translateStatus != FTPStatus.FTP_PROGRESS) {
            this.busy = false;
            this.currOperation = FTPOperation.FTP_NONE;
        }
        Log.d(TAG, "Status: " + translateStatus.name() + ", progress: " + f);
    }

    private native boolean ftpAbort();

    private native boolean ftpConnect(String str, int i, String str2, String str3);

    private native boolean ftpDisconnect();

    private native boolean ftpGet(String str, String str2, boolean z);

    private native boolean ftpGetSync(String str, String str2, boolean z);

    private native boolean ftpIsConnected();

    private native boolean ftpPut(String str, String str2, boolean z);

    private native boolean ftpPutSync(String str, String str2, boolean z);

    public boolean abort() {
        boolean ftpAbort = ftpAbort();
        if (ftpAbort) {
            this.busy = false;
            this.currOperation = FTPOperation.FTP_NONE;
        }
        return ftpAbort;
    }

    public void cd() {
        throw new IllegalStateException("Not implemented");
    }

    public boolean connect(String str, int i) {
        return connect(str, i, "anonymous", HttpVersions.HTTP_0_9);
    }

    public boolean connect(String str, int i, String str2, String str3) {
        if (str != null && str2 != null && str3 != null) {
            return ftpConnect(str, i, str2, str3);
        }
        throw new IllegalArgumentException();
    }

    public boolean disconnect() {
        return ftpDisconnect();
    }

    public boolean get(String str, String str2) {
        if (this.busy) {
            Log.w(TAG, "Can't get file. FTPClient is busy at the moment. Performing " + this.currOperation.name());
            return false;
        }
        this.currOperation = FTPOperation.FTP_GET;
        this.busy = true;
        return ftpGet(str, str2, false);
    }

    public int getReplyCode() {
        return this.ftpStatus;
    }

    public FTPStatus getReplyStatus() {
        return FTPClientStatus.translateStatus(this.ftpStatus);
    }

    public boolean getSync(String str, String str2) {
        if (this.busy) {
            Log.w(TAG, "Can't get file. FTPClient is busy at the moment. Performing " + this.currOperation.name());
            return false;
        }
        this.currOperation = FTPOperation.FTP_GET;
        this.busy = true;
        boolean ftpGetSync = ftpGetSync(str, str2, false);
        this.busy = false;
        this.currOperation = FTPOperation.FTP_NONE;
        return ftpGetSync;
    }

    public boolean isConnected() {
        return ftpIsConnected();
    }

    public void list() {
        throw new IllegalStateException("Not implemented");
    }

    public void mkdir() {
        throw new IllegalStateException("Not implemented");
    }

    public boolean put(String str, String str2) {
        if (this.busy) {
            Log.w(TAG, "Can't put file. FTPClient is busy at the moment. Performing " + this.currOperation.name());
            return false;
        }
        this.currOperation = FTPOperation.FTP_PUT;
        this.busy = true;
        return ftpPut(str, str2, false);
    }

    public boolean putSync(String str, String str2) {
        if (this.busy) {
            Log.w(TAG, "Can't put file. FTPClient is busy at the moment. Performing " + this.currOperation.name());
            return false;
        }
        this.currOperation = FTPOperation.FTP_PUT;
        this.busy = true;
        try {
            boolean ftpPutSync = ftpPutSync(str, str2, false);
            return ftpPutSync;
        } finally {
            this.busy = false;
            this.currOperation = FTPOperation.FTP_NONE;
        }
    }

    public void pwd() {
        throw new IllegalStateException("Not implemented");
    }

    public void remove() {
        throw new IllegalStateException("Not implemented");
    }

    public void rename() {
        throw new IllegalStateException("Not implemented");
    }

    public boolean retrieveFile(String str, OutputStream outputStream) {
        throw new IllegalStateException("Not implemented");
    }

    public void rmdir() {
        throw new IllegalStateException("Not implemented");
    }

    public void setProgressListener(FTPProgressListener fTPProgressListener) {
        this.listener = fTPProgressListener;
    }
}
