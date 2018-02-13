package com.parrot.freeflight.receivers;

import com.parrot.ardronetool.academynavdata.MotorState;

public interface DroneMotorsChangedDelegate {
    void onDroneMotorsChanged(int i, MotorState motorState, int i2, MotorState motorState2, int i3, MotorState motorState3, int i4, MotorState motorState4);
}
