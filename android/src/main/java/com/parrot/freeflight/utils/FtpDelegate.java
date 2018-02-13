package com.parrot.freeflight.utils;

public interface FtpDelegate {
    void ftpOperationFailure();

    void ftpOperationSuccess(String str);
}
