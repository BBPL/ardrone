package com.parrot.ardronetool;

public class NavDataPwm {
    NavDataPwm() {
    }

    public final native byte getMotor1();

    public final native int getMotor1Status();

    public final native byte getMotor2();

    public final native int getMotor2Status();

    public final native byte getMotor3();

    public final native int getMotor3Status();

    public final native byte getMotor4();

    public final native int getMotor4Status();
}
