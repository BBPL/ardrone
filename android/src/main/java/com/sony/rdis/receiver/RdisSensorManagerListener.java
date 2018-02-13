package com.sony.rdis.receiver;

public interface RdisSensorManagerListener {
    void startSensorRequest(int i, int i2, int i3);

    void stopSensorRequest(int i, int i2);
}
