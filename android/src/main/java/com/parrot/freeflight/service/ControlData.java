package com.parrot.freeflight.service;

import android.util.Log;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.parrot.ardronetool.ui.ARDroneInput;

public class ControlData {
    private static final String TAG = ControlData.class.getSimpleName();
    public int command_flag;
    public float gaz;
    private ARDroneInput input;
    public float iphone_phi;
    public float iphone_psi;
    public float iphone_psi_accuracy;
    public float iphone_theta;
    boolean navdata_connected = false;
    public float yaw;

    public ControlData(ARDroneInput aRDroneInput) {
        this.input = aRDroneInput;
        reset();
        aRDroneInput.startReset();
        Log.d(TAG, "initControlData [OK]");
    }

    void inputGaz(float f) {
        if (GroundOverlayOptions.NO_DIMENSION <= f && f <= 1.0f) {
            this.gaz = f;
        } else if (GroundOverlayOptions.NO_DIMENSION > f) {
            this.gaz = GroundOverlayOptions.NO_DIMENSION;
        } else {
            this.gaz = 1.0f;
        }
    }

    void inputPitch(float f) {
        if (GroundOverlayOptions.NO_DIMENSION <= f && f <= 1.0f) {
            this.iphone_theta = f;
        } else if (GroundOverlayOptions.NO_DIMENSION > f) {
            this.iphone_theta = GroundOverlayOptions.NO_DIMENSION;
        } else {
            this.iphone_theta = 1.0f;
        }
    }

    void inputRoll(float f) {
        if (GroundOverlayOptions.NO_DIMENSION <= f && f <= 1.0f) {
            this.iphone_phi = f;
        } else if (GroundOverlayOptions.NO_DIMENSION > f) {
            this.iphone_phi = GroundOverlayOptions.NO_DIMENSION;
        } else {
            this.iphone_phi = 1.0f;
        }
    }

    void inputYaw(float f) {
        if (GroundOverlayOptions.NO_DIMENSION <= f && f <= 1.0f) {
            this.yaw = f;
        } else if (GroundOverlayOptions.NO_DIMENSION > f) {
            this.yaw = GroundOverlayOptions.NO_DIMENSION;
        } else {
            this.yaw = 1.0f;
        }
    }

    public void reset() {
        this.command_flag = 0;
        this.yaw = 0.0f;
        this.gaz = 0.0f;
        this.iphone_phi = 0.0f;
        this.iphone_theta = 0.0f;
        this.iphone_psi = 0.0f;
        this.iphone_psi_accuracy = 0.0f;
    }

    public void sendControls() {
        this.input.setProgressiveCmd(this.command_flag, this.iphone_phi, this.iphone_theta, this.gaz, this.yaw, this.iphone_psi, this.iphone_psi_accuracy);
    }

    public void setCommandFlag(int i, boolean z) {
        if (z) {
            this.command_flag |= 1 << i;
        } else {
            this.command_flag &= (1 << i) ^ -1;
        }
    }
}
