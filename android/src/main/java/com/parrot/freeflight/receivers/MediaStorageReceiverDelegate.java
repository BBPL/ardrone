package com.parrot.freeflight.receivers;

public interface MediaStorageReceiverDelegate {
    void onMediaEject();

    void onMediaStorageMounted();

    void onMediaStorageUnmounted();
}
