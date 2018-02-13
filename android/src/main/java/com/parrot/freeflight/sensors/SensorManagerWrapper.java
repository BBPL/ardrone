package com.parrot.freeflight.sensors;

import android.hardware.SensorEventListener;
import android.os.Handler;

public abstract class SensorManagerWrapper {
    private boolean isAcceleroAvailable;
    private boolean isGyroAvailable;
    private boolean isMagnetoAvailable;

    public boolean isAcceleroAvailable() {
        return this.isAcceleroAvailable;
    }

    public boolean isGyroAvailable() {
        return this.isGyroAvailable;
    }

    public boolean isMagnetoAvailable() {
        return this.isMagnetoAvailable;
    }

    public abstract void onCreate();

    public abstract void onDestroy();

    public abstract void onPause();

    public abstract void onResume();

    public abstract boolean registerListener(SensorEventListener sensorEventListener, int i, Handler handler);

    public void setAcceleroAvailable(boolean z) {
        this.isAcceleroAvailable = z;
    }

    public void setGyroAvailable(boolean z) {
        this.isGyroAvailable = z;
    }

    public void setMagnetoAvailable(boolean z) {
        this.isMagnetoAvailable = z;
    }

    public abstract void unregisterListener(SensorEventListener sensorEventListener);
}
