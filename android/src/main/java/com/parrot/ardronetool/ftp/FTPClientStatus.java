package com.parrot.ardronetool.ftp;

public class FTPClientStatus {

    public enum FTPStatus {
        FTP_FAIL,
        FTP_BUSY,
        FTP_SUCCESS,
        FTP_TIMEOUT,
        FTP_BADSIZE,
        FTP_SAMESIZE,
        FTP_PROGRESS,
        FTP_ABORT
    }

    public static boolean isFailure(FTPStatus fTPStatus) {
        switch (fTPStatus) {
            case FTP_ABORT:
            case FTP_BUSY:
            case FTP_BADSIZE:
            case FTP_FAIL:
            case FTP_TIMEOUT:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSuccess(FTPStatus fTPStatus) {
        switch (fTPStatus) {
            case FTP_SUCCESS:
            case FTP_SAMESIZE:
                return true;
            default:
                return false;
        }
    }

    public static FTPStatus translateStatus(int i) {
        if (i >= 0) {
            return FTPStatus.values()[i];
        }
        throw new IllegalArgumentException();
    }
}
