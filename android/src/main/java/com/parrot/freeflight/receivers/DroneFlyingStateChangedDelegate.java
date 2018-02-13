package com.parrot.freeflight.receivers;

import com.parrot.ardronetool.academynavdata.FlyingState;

public interface DroneFlyingStateChangedDelegate {
    void onDroneFlyingStateChanged(FlyingState flyingState);
}
