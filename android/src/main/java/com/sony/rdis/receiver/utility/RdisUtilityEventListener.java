package com.sony.rdis.receiver.utility;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

public interface RdisUtilityEventListener {
    void onAccuracyChanged(Sensor sensor, int i);

    boolean onKeyDown(int i, KeyEvent keyEvent);

    boolean onKeyUp(int i, KeyEvent keyEvent);

    void onSensorChanged(SensorEvent sensorEvent);

    boolean onTouchEvent(MotionEvent motionEvent);
}
