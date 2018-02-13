package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.freeflight.service.DroneControlService;

public class DroneAppRecordStoppedReceiver extends BroadcastReceiver {
    private DroneAppRecordStoppedReceiverDelegate delegate;

    public DroneAppRecordStoppedReceiver(DroneAppRecordStoppedReceiverDelegate droneAppRecordStoppedReceiverDelegate) {
        this.delegate = droneAppRecordStoppedReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_APP_RECORD_STOPPED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate == null) {
            throw new IllegalStateException("Event received but delegate is null");
        } else if (intent.getAction().equals(DroneControlService.ACTION_APP_RECORD_STOPPED)) {
            this.delegate.onDroneAppRecordStopped();
        }
    }
}
