package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.parrot.freeflight.service.DroneControlService;

public class DroneVideoRecordingStateReceiver extends BroadcastReceiver {
    private DroneVideoRecordStateReceiverDelegate delegate;

    public DroneVideoRecordingStateReceiver(DroneVideoRecordStateReceiverDelegate droneVideoRecordStateReceiverDelegate) {
        this.delegate = droneVideoRecordStateReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra(DroneControlService.EXTRA_RECORDING_STATE, false);
        int intExtra = intent.getIntExtra(DroneControlService.EXTRA_USB_REMAINING_TIME, 0);
        boolean booleanExtra2 = intent.getBooleanExtra(DroneControlService.EXTRA_USB_ACTIVE, false);
        if (this.delegate != null) {
            this.delegate.onDroneRecordVideoStateChanged(booleanExtra, booleanExtra2, intExtra);
        }
    }
}
