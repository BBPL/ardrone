package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.parrot.freeflight.service.intents.DroneStateManager;

public class DroneAvailabilityReceiver extends BroadcastReceiver {
    private DroneAvailabilityDelegate delegate;

    public DroneAvailabilityReceiver(DroneAvailabilityDelegate droneAvailabilityDelegate) {
        this.delegate = droneAvailabilityDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneStateManager.ACTION_DRONE_STATE_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onDroneAvailabilityChanged(intent.getBooleanExtra(DroneStateManager.DRONE_AVAILABLE_ON_NETWORK, false));
            return;
        }
        Log.w("DroneAvailabilityReceiver", "Delegate was not set");
    }
}
