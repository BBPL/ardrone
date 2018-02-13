package com.parrot.ardronetool.academynavdata;

public enum MotorState {
    Dead,
    FlashOk,
    FlashKo,
    Idle,
    Ramp,
    Running,
    Stalled;

    public static MotorState fromInt(int i) {
        return values()[i];
    }
}
