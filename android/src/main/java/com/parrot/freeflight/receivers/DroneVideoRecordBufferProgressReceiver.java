package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.parrot.freeflight.service.DroneControlService;

public class DroneVideoRecordBufferProgressReceiver extends BroadcastReceiver {
    private DroneVideoRecordBufferReceiverDelegate delegate;

    public DroneVideoRecordBufferProgressReceiver(DroneVideoRecordBufferReceiverDelegate droneVideoRecordBufferReceiverDelegate) {
        this.delegate = droneVideoRecordBufferReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            float floatExtra = intent.getFloatExtra(DroneControlService.EXTRA_VIDEO_RECORD_PROGRESS, GroundOverlayOptions.NO_DIMENSION);
            if (floatExtra < 0.0f || floatExtra > 1.0f) {
                this.delegate.onDroneVideoRecordBufferChanged(false, 0);
            } else {
                this.delegate.onDroneVideoRecordBufferChanged(true, (int) (floatExtra * 100.0f));
            }
        }
    }
}
