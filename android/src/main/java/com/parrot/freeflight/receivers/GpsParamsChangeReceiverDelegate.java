package com.parrot.freeflight.receivers;

public interface GpsParamsChangeReceiverDelegate {
    void onGpsParamsChanged(boolean z, boolean z2, float f, int i, double d, double d2);
}
