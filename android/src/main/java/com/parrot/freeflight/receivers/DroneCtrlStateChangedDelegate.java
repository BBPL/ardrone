package com.parrot.freeflight.receivers;

import com.parrot.ardronetool.academynavdata.CtrlState;

public interface DroneCtrlStateChangedDelegate {
    void onDroneCtrlStateChanged(CtrlState ctrlState);
}
