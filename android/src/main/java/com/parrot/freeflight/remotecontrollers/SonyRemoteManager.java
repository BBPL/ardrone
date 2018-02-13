package com.parrot.freeflight.remotecontrollers;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.sony.rdis.receiver.utility.RdisUtility;
import com.sony.rdis.receiver.utility.RdisUtilityConnectionListener;
import com.sony.rdis.receiver.utility.RdisUtilityEventListener;
import com.sony.rdis.receiver.utility.RdisUtilityGamePad;

public class SonyRemoteManager implements RdisUtilityConnectionListener {
    private RdisUtility mRdisUtility = null;
    private RdisUtilityEventListener remoteEventListener;
    private SensorEventListener sensorListener;

    class C11721 implements RdisUtilityEventListener {
        C11721() {
        }

        public void onAccuracyChanged(Sensor sensor, int i) {
            SonyRemoteManager.this.applySensorAccuracyChanged(sensor, i);
        }

        public boolean onKeyDown(int i, KeyEvent keyEvent) {
            return false;
        }

        public boolean onKeyUp(int i, KeyEvent keyEvent) {
            return false;
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            SonyRemoteManager.this.applySensorChanged(sensorEvent);
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            return false;
        }
    }

    public SonyRemoteManager(Activity activity) {
        initRemoteListener();
        this.mRdisUtility = new RdisUtility(activity, this, null);
    }

    private void initRemoteListener() {
        this.remoteEventListener = new C11721();
    }

    private void registerGamePad(RdisUtilityGamePad rdisUtilityGamePad, int i) {
        int[] sensorType = rdisUtilityGamePad.getSensorType();
        int i2 = 0;
        for (int i3 : sensorType) {
            if (i3 == 1) {
                i2 = 1;
            }
        }
        if (i2 == 1) {
            this.mRdisUtility.registerGamePad(rdisUtilityGamePad, this.remoteEventListener, new int[]{1}, 17170443);
        }
    }

    protected void applySensorAccuracyChanged(Sensor sensor, int i) {
        if (this.sensorListener != null) {
            this.sensorListener.onAccuracyChanged(sensor, i);
        }
    }

    protected void applySensorChanged(SensorEvent sensorEvent) {
        if (this.sensorListener != null) {
            this.sensorListener.onSensorChanged(sensorEvent);
        }
    }

    public void onConnected(RdisUtilityGamePad rdisUtilityGamePad) {
        if (rdisUtilityGamePad.isDefaultGamePad()) {
            registerGamePad(rdisUtilityGamePad, 0);
        }
    }

    public void onDestroy() {
        this.mRdisUtility.destroy();
    }

    public void onDisconnected(RdisUtilityGamePad rdisUtilityGamePad) {
        if (rdisUtilityGamePad.isDefaultGamePad()) {
            this.mRdisUtility.unregisterGamePad(rdisUtilityGamePad);
        }
    }

    public void onPause() {
        this.mRdisUtility.pause();
    }

    public void onResume() {
        this.mRdisUtility.resume();
    }

    public void setSensorEventListener(SensorEventListener sensorEventListener) {
        this.sensorListener = sensorEventListener;
    }
}
