package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.freeflight.service.FlightUploadService;

public class FlightUploadStatusReceiver extends BroadcastReceiver {
    private FlightUploadStatusReceiverDelegate delegate;

    public FlightUploadStatusReceiver(FlightUploadStatusReceiverDelegate flightUploadStatusReceiverDelegate) {
        this.delegate = flightUploadStatusReceiverDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(FlightUploadService.ACTION_FLIGHT_UPLOAD_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra(FlightUploadService.EXTRA_FLIGHT_UPLOADING, false);
        if (this.delegate != null) {
            this.delegate.onFlightUploadStatusChanged(booleanExtra);
        }
    }
}
