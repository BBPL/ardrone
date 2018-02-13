package com.parrot.ardronetool.academynavdata;

public final class ARDroneAcademyNavdata {
    private static ARDroneAcademyNavdata instance;

    public enum RecordCmd {
        SWITCH,
        START,
        STOP,
        FINISHED
    }

    private ARDroneAcademyNavdata() {
    }

    public static final ARDroneAcademyNavdata instance() {
        if (instance == null) {
            instance = new ARDroneAcademyNavdata();
        }
        return instance;
    }

    private native boolean record(int i);

    public native boolean checkAppRecordStatus();

    public native boolean checkTakeOffCancelled();

    public native boolean checkUsbRecordStatus();

    public native boolean emergency();

    public native boolean getAutorecordState();

    public native boolean getCameraState();

    public native boolean getEmergencyState();

    public FlyingState getFlyingState() {
        int flyingStateNative = getFlyingStateNative();
        if (flyingStateNative >= 0 && flyingStateNative < FlyingState.values().length) {
            return FlyingState.values()[flyingStateNative];
        }
        throw new IllegalStateException("Unknown flying state");
    }

    public native int getFlyingStateNative();

    public native float getHDVideoRecordProgress();

    public native boolean getRecordReady();

    public native int getRemainingUsbTime();

    public native boolean getTakeoffState();

    public native boolean getUsbState();

    public boolean record(RecordCmd recordCmd) {
        return record(recordCmd.ordinal());
    }

    public native boolean screenshot();

    public native void setAutorecord(boolean z);

    public native void setWifiRecordCodec(int i);

    public native boolean takeOff();
}
