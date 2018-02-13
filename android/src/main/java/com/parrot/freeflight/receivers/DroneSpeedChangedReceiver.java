package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.parrot.freeflight.service.DroneControlService;

public class DroneSpeedChangedReceiver extends BroadcastReceiver {
    private DroneSpeedChangedReceiverDelegate delegate;

    public DroneSpeedChangedReceiver(DroneSpeedChangedReceiverDelegate droneSpeedChangedReceiverDelegate) {
        this.delegate = droneSpeedChangedReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        double doubleExtra = intent.getDoubleExtra(DroneControlService.EXTRA_DRONE_SPEED, 0.0d);
        if (this.delegate != null) {
            this.delegate.onDroneSpeedChanged(doubleExtra);
        }
    }
}
