package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.parrot.freeflight.service.DroneControlService;

public class DroneConfigChangedReceiver extends BroadcastReceiver {
    private DroneConfigChangedReceiverDelegate delegate;

    public DroneConfigChangedReceiver(DroneConfigChangedReceiverDelegate droneConfigChangedReceiverDelegate) {
        this.delegate = droneConfigChangedReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null && intent.getAction().equals(DroneControlService.ACTION_DRONE_CONFIG_STATE_CHANGED)) {
            this.delegate.onDroneConfigChanged();
        }
    }
}
