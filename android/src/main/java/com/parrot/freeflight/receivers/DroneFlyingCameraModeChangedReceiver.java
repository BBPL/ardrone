package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.ardronetool.academynavdata.FlyingCameraModeValue;
import com.parrot.freeflight.service.DroneControlService;

public class DroneFlyingCameraModeChangedReceiver extends BroadcastReceiver {
    private final DroneFlyingCameraModeChangedDelegate delegate;

    public DroneFlyingCameraModeChangedReceiver(DroneFlyingCameraModeChangedDelegate droneFlyingCameraModeChangedDelegate) {
        this.delegate = droneFlyingCameraModeChangedDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_DRONE_FLYINGCAMERAMODE_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onDroneFlyingCameraModeChanged(FlyingCameraModeValue.fromInteger(intent.getIntExtra(DroneControlService.EXTRA_DRONE_FLYINGCAMERAMODE, 0)));
        }
    }
}
