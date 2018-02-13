package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.parrot.freeflight.service.DroneControlService;

public class DroneFirmwareCheckReceiver extends BroadcastReceiver {
    private static final String TAG = DroneFirmwareCheckReceiver.class.getSimpleName();
    private DroneFirmwareCheckReceiverDelegate delegate;

    public DroneFirmwareCheckReceiver(DroneFirmwareCheckReceiverDelegate droneFirmwareCheckReceiverDelegate) {
        this.delegate = droneFirmwareCheckReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_DRONE_FIRMWARE_CHECK);
    }

    public void onReceive(Context context, Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra(DroneControlService.EXTRA_FIRMWARE_UPDATE_REQUIRED, false);
        if (booleanExtra) {
            Log.d(TAG, "Drone firmware update required");
        }
        if (this.delegate != null) {
            this.delegate.onFirmwareChecked(booleanExtra);
        }
    }
}
