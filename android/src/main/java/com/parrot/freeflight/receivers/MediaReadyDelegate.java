package com.parrot.freeflight.receivers;

import java.io.File;

public interface MediaReadyDelegate {
    void onMediaReady(File file);
}
