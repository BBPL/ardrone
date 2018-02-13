package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.parrot.freeflight.service.DroneControlService;

public class DroneAutorecordStartedReceiver extends BroadcastReceiver {
    private static final String TAG = DroneAutorecordStartedReceiver.class.getSimpleName();
    private DroneAutorecordStartedReceiverDelegate delegate;

    public DroneAutorecordStartedReceiver(DroneAutorecordStartedReceiverDelegate droneAutorecordStartedReceiverDelegate) {
        this.delegate = droneAutorecordStartedReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_DRONE_AUTORECORD_STARTED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate == null) {
            throw new IllegalStateException("Receiver is registered but delegate is null");
        } else if (intent.getAction().equals(DroneControlService.ACTION_DRONE_AUTORECORD_STARTED)) {
            this.delegate.onDroneAutorecordStarted();
        } else {
            Log.w(TAG, "Received event with wrong action: " + intent.getAction() + ", but should be " + DroneControlService.ACTION_DRONE_AUTORECORD_STARTED);
        }
    }
}
