package com.parrot.freeflight.ui.controlhandlers;

public interface OnRollPitchChangedListener {
    void onRollPitchActivated();

    void onRollPitchChanged(float f, float f2);

    void onRollPitchDeactivated();
}
