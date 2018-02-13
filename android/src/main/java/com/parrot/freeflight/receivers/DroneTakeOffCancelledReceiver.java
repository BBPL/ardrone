package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.freeflight.service.DroneControlService;

public class DroneTakeOffCancelledReceiver extends BroadcastReceiver {
    private DroneTakeOffCancelledReceiverDelegate delegate;

    public DroneTakeOffCancelledReceiver(DroneTakeOffCancelledReceiverDelegate droneTakeOffCancelledReceiverDelegate) {
        this.delegate = droneTakeOffCancelledReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_DRONE_TAKEOFF_CANCELLED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate == null) {
            throw new IllegalStateException("Message received but delegate is null");
        } else if (intent.getAction().equals(DroneControlService.ACTION_DRONE_TAKEOFF_CANCELLED)) {
            this.delegate.onDroneTakeOffCancelled();
        }
    }
}
