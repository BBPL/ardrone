package com.parrot.ardronetool;

public class NavDataDemo {
    NavDataDemo() {
    }

    public final native int getAltitude();

    public final native int getCtrlState();

    public final native int getDetectionCameraType();

    public final native float getPhi();

    public final native float getPsi();

    public final native float getTheta();

    public final native int getVBatFlyingPercentage();

    public final native float getVx();

    public final native float getVy();

    public final native float getVz();
}
