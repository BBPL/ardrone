package com.parrot.freeflight.sensors;

import android.app.Activity;
import android.hardware.SensorEventListener;
import android.os.Handler;
import com.parrot.freeflight.remotecontrollers.SonyRemoteManager;

public class RemoteSensorManagerWrapper extends SensorManagerWrapper {
    private SonyRemoteManager sensorManager;

    public RemoteSensorManagerWrapper(Activity activity) {
        this.sensorManager = new SonyRemoteManager(activity);
        setAcceleroAvailable(true);
        setMagnetoAvailable(false);
        setGyroAvailable(false);
    }

    public void onCreate() {
    }

    public void onDestroy() {
        this.sensorManager.onDestroy();
    }

    public void onPause() {
        this.sensorManager.onPause();
    }

    public void onResume() {
        this.sensorManager.onResume();
    }

    public boolean registerListener(SensorEventListener sensorEventListener, int i, Handler handler) {
        if (i != 1) {
            return false;
        }
        this.sensorManager.setSensorEventListener(sensorEventListener);
        return true;
    }

    public void unregisterListener(SensorEventListener sensorEventListener) {
    }
}
