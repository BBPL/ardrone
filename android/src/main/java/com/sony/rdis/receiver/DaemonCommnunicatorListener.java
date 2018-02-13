package com.sony.rdis.receiver;

import android.view.MotionEvent;

public interface DaemonCommnunicatorListener {
    void daemonConnectionFail();

    void defaultSensorChange(int i);

    void fatalError(String str);

    void mobileConnection(int i, RdisClient rdisClient);

    void mobileDisconnection(int i);

    void remoconConnection(RdisRemoteController rdisRemoteController);

    void sensorEvent(int i, int i2, float[] fArr, int i3);

    void touchEvent(int i, MotionEvent motionEvent);
}
