package com.parrot.freeflight.ui.controlhandlers;

public interface OnGazYawChangedListener {
    void onGazYawActivated();

    void onGazYawChanged(float f, float f2);

    void onGazYawDeactivated();
}
