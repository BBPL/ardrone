package com.sony.rdis.receiver;

import android.view.InputDevice;

public class HumanInterfaceDeviceInfomation {
    public static final int DEVICETYPE_KEY = 1;
    public static final int DEVICETYPE_MOUSE = 3;
    public static final int DEVICETYPE_TOUCH = 2;
    public static final int DEVICETYPE_UNKNOWN = -1;
    private int mDeviceId = -1;
    private String mDeviceName = null;
    private int mDeviceType = -1;

    public HumanInterfaceDeviceInfomation(int i, String str) {
        this.mDeviceType = i;
        this.mDeviceName = str;
        this.mDeviceId = nameToDeviceId(str);
    }

    private int nameToDeviceId(String str) {
        for (int i : InputDevice.getDeviceIds()) {
            InputDevice device = InputDevice.getDevice(i);
            if (device != null && device.getName().equals(str)) {
                return i;
            }
        }
        return -1;
    }

    public int getDeviceId() {
        return this.mDeviceId;
    }

    public int getDeviceType() {
        return this.mDeviceType;
    }
}
