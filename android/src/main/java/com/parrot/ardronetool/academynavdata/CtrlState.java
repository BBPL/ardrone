package com.parrot.ardronetool.academynavdata;

public enum CtrlState {
    Default,
    Init,
    Landed,
    Flying,
    Hovering,
    Test,
    Rescue,
    TransTakeoff,
    TransGotofix,
    TransLanding,
    TransLooping;

    public static CtrlState fromCtrlStateInt(int i) {
        return values()[i >> 16];
    }
}
