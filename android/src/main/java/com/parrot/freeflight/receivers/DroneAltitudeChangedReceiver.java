package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.parrot.freeflight.service.DroneControlService;

public class DroneAltitudeChangedReceiver extends BroadcastReceiver {
    private DroneAltitudeChangedReceiverDelegate delegate;

    public DroneAltitudeChangedReceiver(DroneAltitudeChangedReceiverDelegate droneAltitudeChangedReceiverDelegate) {
        this.delegate = droneAltitudeChangedReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        int intExtra = intent.getIntExtra(DroneControlService.EXTRA_DRONE_ALTITUDE, 0);
        if (this.delegate != null) {
            this.delegate.onDroneAltitudeChanged(intExtra);
        }
    }
}
