package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.freeflight.service.DroneControlService;

public class DronePsiChangedReceiver extends BroadcastReceiver {
    private final DronePsiChangedReceiverDelegate delegate;

    public DronePsiChangedReceiver(DronePsiChangedReceiverDelegate dronePsiChangedReceiverDelegate) {
        this.delegate = dronePsiChangedReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_DRONE_PSI_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        float floatExtra = intent.getFloatExtra(DroneControlService.EXTRA_DRONE_PSI, 0.0f);
        if (this.delegate != null) {
            this.delegate.onDronePsiChanged(floatExtra);
        }
    }
}
