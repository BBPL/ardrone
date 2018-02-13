package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.parrot.ardronetool.ARDroneEngine.ErrorState;
import com.parrot.freeflight.service.DroneControlService;

public class DroneEmergencyChangeReceiver extends BroadcastReceiver {
    private DroneEmergencyChangeReceiverDelegate delegate;

    public DroneEmergencyChangeReceiver(DroneEmergencyChangeReceiverDelegate droneEmergencyChangeReceiverDelegate) {
        this.delegate = droneEmergencyChangeReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        ErrorState errorState = (ErrorState) intent.getSerializableExtra(DroneControlService.EXTRA_EMERGENCY_CODE);
        if (this.delegate != null) {
            this.delegate.onDroneEmergencyChanged(errorState);
        }
    }
}
