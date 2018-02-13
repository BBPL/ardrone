package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.parrot.freeflight.service.DroneControlService;

public class DroneUsbRecordStoppedReceiver extends BroadcastReceiver {
    private static final String TAG = DroneUsbRecordStoppedReceiver.class.getSimpleName();
    private DroneUsbRecordStoppedReceiverDelegate delegate;

    public DroneUsbRecordStoppedReceiver(DroneUsbRecordStoppedReceiverDelegate droneUsbRecordStoppedReceiverDelegate) {
        this.delegate = droneUsbRecordStoppedReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_DRONE_USB_RECORD_STOPPED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate == null) {
            throw new IllegalStateException("Event received but delegate == null");
        } else if (intent.getAction().equals(DroneControlService.ACTION_DRONE_USB_RECORD_STOPPED)) {
            this.delegate.onDroneUsbRecordStopped();
        } else {
            Log.w(TAG, "Event received but with wrong action (" + intent.getAction() + "). Valid action is " + DroneControlService.ACTION_DRONE_USB_RECORD_STOPPED);
        }
    }
}
