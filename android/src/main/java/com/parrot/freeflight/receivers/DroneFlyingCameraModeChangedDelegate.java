package com.parrot.freeflight.receivers;

import com.parrot.ardronetool.academynavdata.FlyingCameraModeValue;

public interface DroneFlyingCameraModeChangedDelegate {
    void onDroneFlyingCameraModeChanged(FlyingCameraModeValue flyingCameraModeValue);
}
