package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.freeflight.service.DroneControlService;

public class GpsFirmwareParamsChangedReceiver extends BroadcastReceiver {
    private final GpsFirmwareParamsChangedReceiverDelegate delegate;

    public GpsFirmwareParamsChangedReceiver(GpsFirmwareParamsChangedReceiverDelegate gpsFirmwareParamsChangedReceiverDelegate) {
        this.delegate = gpsFirmwareParamsChangedReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_GPS_FIRMWARE_PARAMS_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        this.delegate.onGpsFirmwareParamsChanged(intent.getIntExtra(DroneControlService.EXTRA_GPS_EPHEMERIS, -1), intent.getIntExtra(DroneControlService.EXTRA_GPS_FIRMWARE_UPDATE_STATE, -1), intent.getIntExtra(DroneControlService.EXTRA_GPS_FIRMWARE_UPDATE_PROGRESS, -1));
    }
}
