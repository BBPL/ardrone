package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.ardronetool.academynavdata.FlyingState;
import com.parrot.freeflight.service.DroneControlService;

public class DroneFlyingStateChangedReceiver extends BroadcastReceiver {
    private final DroneFlyingStateChangedDelegate delegate;

    public DroneFlyingStateChangedReceiver(DroneFlyingStateChangedDelegate droneFlyingStateChangedDelegate) {
        this.delegate = droneFlyingStateChangedDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_DRONE_FLYINGSTATE_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onDroneFlyingStateChanged(FlyingState.values()[intent.getIntExtra(DroneControlService.EXTRA_DRONE_FLYINGSTATE, 0)]);
        }
    }
}
