package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.freeflight.service.DroneControlService;

public class DroneFlyingCameraChangedReceiver extends BroadcastReceiver {
    private final DroneFlyingCameraChangedDelegate delegate;

    public DroneFlyingCameraChangedReceiver(DroneFlyingCameraChangedDelegate droneFlyingCameraChangedDelegate) {
        this.delegate = droneFlyingCameraChangedDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_DRONE_FLYINGCAMERA_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onDroneFlyingCameraChanged(intent.getBooleanExtra(DroneControlService.EXTRA_DRONE_FLYINGCAMERA, false));
        }
    }
}
