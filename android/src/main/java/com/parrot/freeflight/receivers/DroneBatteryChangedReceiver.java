package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.parrot.freeflight.service.DroneControlService;

public class DroneBatteryChangedReceiver extends BroadcastReceiver {
    private DroneBatteryChangedReceiverDelegate delegate;

    public DroneBatteryChangedReceiver(DroneBatteryChangedReceiverDelegate droneBatteryChangedReceiverDelegate) {
        this.delegate = droneBatteryChangedReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        int intExtra = intent.getIntExtra(DroneControlService.EXTRA_DRONE_BATTERY, 0);
        if (this.delegate != null) {
            this.delegate.onDroneBatteryChanged(intExtra);
        }
    }
}
