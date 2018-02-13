package com.parrot.freeflight.ui.hud;

import android.util.SparseIntArray;
import android.view.InputDevice;
import android.view.InputDevice.MotionRange;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class InputDeviceState {
    private int[] mAxes;
    private float[] mAxisValues;
    private InputDevice mDevice;
    private SparseIntArray mKeys;

    public InputDeviceState(InputDevice inputDevice) {
        int i = 0;
        this.mDevice = inputDevice;
        int i2 = 0;
        for (MotionRange source : inputDevice.getMotionRanges()) {
            if ((source.getSource() & 16) != 0) {
                i2++;
            }
        }
        this.mAxes = new int[i2];
        this.mAxisValues = new float[i2];
        this.mKeys = new SparseIntArray();
        for (MotionRange source2 : inputDevice.getMotionRanges()) {
            if ((source2.getSource() & 16) != 0) {
                this.mAxes[i] = source2.getAxis();
                i++;
            }
        }
    }

    public static float ProcessAxis(MotionRange motionRange, float f) {
        float abs = Math.abs(f);
        return abs <= motionRange.getFlat() ? 0.0f : f < 0.0f ? abs / motionRange.getMin() : abs / motionRange.getMax();
    }

    public int[] getAxes() {
        return this.mAxes;
    }

    public float[] getAxisValues() {
        return this.mAxisValues;
    }

    public InputDevice getDevice() {
        return this.mDevice;
    }

    public SparseIntArray getKeys() {
        return this.mKeys;
    }

    public boolean onJoystickMotion(MotionEvent motionEvent) {
        int i = 0;
        if ((motionEvent.getSource() & 16) == 0) {
            return false;
        }
        while (i < this.mAxes.length) {
            this.mAxisValues[i] = motionEvent.getAxisValue(this.mAxes[i]);
            i++;
        }
        return true;
    }

    public boolean onKeyDown(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyEvent.getRepeatCount() != 0 || !KeyEvent.isGamepadButton(keyCode)) {
            return false;
        }
        this.mKeys.put(keyCode, 1);
        return true;
    }

    public boolean onKeyUp(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (!KeyEvent.isGamepadButton(keyCode)) {
            return false;
        }
        this.mKeys.put(keyCode, 0);
        return true;
    }

    public void setAxes(int[] iArr) {
        this.mAxes = iArr;
    }

    public void setAxisValues(float[] fArr) {
        this.mAxisValues = fArr;
    }

    public void setDevice(InputDevice inputDevice) {
        this.mDevice = inputDevice;
    }

    public void setKeys(SparseIntArray sparseIntArray) {
        this.mKeys = sparseIntArray;
    }
}
