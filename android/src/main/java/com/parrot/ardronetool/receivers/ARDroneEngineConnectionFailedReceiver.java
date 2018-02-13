package com.parrot.ardronetool.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ARDroneEngineConnectionFailedReceiver extends BroadcastReceiver {
    private ARDroneEngineConnectionFailedReceiverDelegate delegate;

    public ARDroneEngineConnectionFailedReceiver(ARDroneEngineConnectionFailedReceiverDelegate aRDroneEngineConnectionFailedReceiverDelegate) {
        this.delegate = aRDroneEngineConnectionFailedReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onToolConnectionFailed(0);
        }
    }
}
