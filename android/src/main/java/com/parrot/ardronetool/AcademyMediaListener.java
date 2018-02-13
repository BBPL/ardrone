package com.parrot.ardronetool;

public interface AcademyMediaListener {
    void onNewMediaIsAvailable(String str);

    void onNewMediaToQueue(String str);

    void onQueueComplete();
}
