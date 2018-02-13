package com.parrot.freeflight.receivers;

import android.net.NetworkInfo;

public interface NetworkChangeReceiverDelegate {
    void onAutoUploadPermissionChanged();

    void onNetworkChanged(NetworkInfo networkInfo);
}
