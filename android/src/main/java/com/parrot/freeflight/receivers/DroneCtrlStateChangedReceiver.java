package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.ardronetool.academynavdata.CtrlState;
import com.parrot.freeflight.service.DroneControlService;

public class DroneCtrlStateChangedReceiver extends BroadcastReceiver {
    private final DroneCtrlStateChangedDelegate delegate;

    public DroneCtrlStateChangedReceiver(DroneCtrlStateChangedDelegate droneCtrlStateChangedDelegate) {
        this.delegate = droneCtrlStateChangedDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_DRONE_CTRLSTATE_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onDroneCtrlStateChanged(CtrlState.fromCtrlStateInt(intent.getIntExtra(DroneControlService.EXTRA_DRONE_CTRLSTATE, 0)));
        }
    }
}
