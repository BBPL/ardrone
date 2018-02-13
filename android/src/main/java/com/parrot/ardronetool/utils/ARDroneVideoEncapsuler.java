package com.parrot.ardronetool.utils;

import android.location.Location;

public final class ARDroneVideoEncapsuler {
    public static native void setGpsInfos(double d, double d2, double d3);

    public static void setGpsInfos(Location location) {
        setGpsInfos(location.getLatitude(), location.getLongitude(), location.getAltitude());
    }

    public native boolean tryFix(String str);
}
