package com.parrot.freeflight.receivers;

import com.parrot.ardronetool.ARDroneEngine.ErrorState;

public interface DroneEmergencyChangeReceiverDelegate {
    void onDroneEmergencyChanged(ErrorState errorState);
}
