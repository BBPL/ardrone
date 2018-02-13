package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.freeflight.service.DroneControlService;

public class DroneConnectionChangedReceiver extends BroadcastReceiver {
    private DroneConnectionChangeReceiverDelegate delegate;

    public DroneConnectionChangedReceiver(DroneConnectionChangeReceiverDelegate droneConnectionChangeReceiverDelegate) {
        this.delegate = droneConnectionChangeReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_DRONE_CONNECTION_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            String stringExtra = intent.getStringExtra(DroneControlService.EXTRA_CONNECTION_STATE);
            if (stringExtra == null || !stringExtra.equals("connected")) {
                this.delegate.onDroneDisconnected();
            } else {
                this.delegate.onDroneConnected();
            }
        }
    }
}
