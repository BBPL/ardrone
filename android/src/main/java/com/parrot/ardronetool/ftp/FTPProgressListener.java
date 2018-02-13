package com.parrot.ardronetool.ftp;

import com.parrot.ardronetool.ftp.FTPClientStatus.FTPStatus;

public interface FTPProgressListener {
    void onStatusChanged(FTPStatus fTPStatus, float f, FTPOperation fTPOperation);
}
