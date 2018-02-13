package com.parrot.ardronetool.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ARDroneEngineConfigChangedReceiver extends BroadcastReceiver {
    public ARDroneEngineConfigChangedReceiverDelegate delegate;

    public ARDroneEngineConfigChangedReceiver(ARDroneEngineConfigChangedReceiverDelegate aRDroneEngineConfigChangedReceiverDelegate) {
        this.delegate = aRDroneEngineConfigChangedReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onConfigChanged();
        }
    }
}
