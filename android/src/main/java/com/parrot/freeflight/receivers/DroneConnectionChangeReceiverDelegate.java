package com.parrot.freeflight.receivers;

public interface DroneConnectionChangeReceiverDelegate {
    void onDroneConnected();

    void onDroneDisconnected();
}
