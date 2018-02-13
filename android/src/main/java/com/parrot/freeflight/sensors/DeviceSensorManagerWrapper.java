package com.parrot.freeflight.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.Log;
import java.util.List;
import org.mortbay.jetty.HttpVersions;

public class DeviceSensorManagerWrapper extends SensorManagerWrapper {
    private static final String TAG = DeviceSensorManagerWrapper.class.getSimpleName();
    private SensorManager sensorManager;

    public DeviceSensorManagerWrapper(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService("sensor");
        checkSensors(this.sensorManager);
    }

    private void checkSensors(SensorManager sensorManager) {
        List sensorList = sensorManager.getSensorList(-1);
        Log.i(TAG, "Available sensors: " + getAvailableSensorsAsString(sensorList));
        for (int i = 0; i < sensorList.size(); i++) {
            Sensor sensor = (Sensor) sensorList.get(i);
            if (sensor.getType() == 1) {
                setAcceleroAvailable(true);
            } else if (sensor.getType() == 2) {
                if (Build.BRAND.equalsIgnoreCase("nook")) {
                    setMagnetoAvailable(false);
                } else {
                    setMagnetoAvailable(true);
                }
            } else if (sensor.getType() == 4 && VERSION.SDK_INT > 8) {
                setGyroAvailable(true);
            }
        }
    }

    private String getAvailableSensorsAsString(List<Sensor> list) {
        int i = 0;
        String str = HttpVersions.HTTP_0_9;
        while (i < list.size()) {
            Sensor sensor = (Sensor) list.get(i);
            i++;
            str = str + sensor.getName() + "(" + sensor.getVendor() + ", " + sensor.getVersion() + "), ";
        }
        return str;
    }

    public void onCreate() {
    }

    public void onDestroy() {
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public boolean registerListener(SensorEventListener sensorEventListener, int i, Handler handler) {
        Sensor defaultSensor = this.sensorManager.getDefaultSensor(i);
        if (defaultSensor == null) {
            return false;
        }
        this.sensorManager.registerListener(sensorEventListener, defaultSensor, 1, handler);
        return true;
    }

    public void unregisterListener(SensorEventListener sensorEventListener) {
        this.sensorManager.unregisterListener(sensorEventListener);
    }
}
