package com.sony.rdis.receiver;

public interface RdisGeneralCommunicationListener {
    void onRecvData(int i, String str, String str2);
}
