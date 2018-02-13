package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.parrot.ardronetool.academynavdata.MotorState;
import com.parrot.freeflight.service.DroneControlService;

public class DroneMotorsChangedReceiver extends BroadcastReceiver {
    private final DroneMotorsChangedDelegate delegate;

    public DroneMotorsChangedReceiver(DroneMotorsChangedDelegate droneMotorsChangedDelegate) {
        this.delegate = droneMotorsChangedDelegate;
    }

    public static IntentFilter createFilter() {
        return new IntentFilter(DroneControlService.ACTION_DRONE_MOTORS_CHANGED);
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            int[] intArrayExtra = intent.getIntArrayExtra(DroneControlService.EXTRA_DRONE_MOTORS);
            this.delegate.onDroneMotorsChanged(intArrayExtra[0], MotorState.fromInt(intArrayExtra[1]), intArrayExtra[2], MotorState.fromInt(intArrayExtra[3]), intArrayExtra[4], MotorState.fromInt(intArrayExtra[5]), intArrayExtra[6], MotorState.fromInt(intArrayExtra[7]));
        }
    }
}
