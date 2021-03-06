package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.parrot.freeflight.service.DroneControlService;

public class DroneRecordReadyChangeReceiver extends BroadcastReceiver {
    private DroneRecordReadyActionReceiverDelegate delegate;

    public DroneRecordReadyChangeReceiver(DroneRecordReadyActionReceiverDelegate droneRecordReadyActionReceiverDelegate) {
        this.delegate = droneRecordReadyActionReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onDroneRecordReadyChanged(intent.getBooleanExtra(DroneControlService.EXTRA_RECORD_READY, false));
        }
    }
}
