package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.freeflight.service.DroneControlService;

public class DroneUsbStateChangedReceiver extends BroadcastReceiver {
    private DroneUsbStateChangedReceiverDelegate delegate;

    public DroneUsbStateChangedReceiver(DroneUsbStateChangedReceiverDelegate droneUsbStateChangedReceiverDelegate) {
        this.delegate = droneUsbStateChangedReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_USB_STATE_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onDroneUsbStateChanged(intent.getBooleanExtra(DroneControlService.EXTRA_USB_ACTIVE, false));
        }
    }
}
