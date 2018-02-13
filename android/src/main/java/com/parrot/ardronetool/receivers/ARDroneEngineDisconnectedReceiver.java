package com.parrot.ardronetool.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ARDroneEngineDisconnectedReceiver extends BroadcastReceiver {
    private ARDroneEngineDisconnectedReceiverDelegate delegate;

    public ARDroneEngineDisconnectedReceiver(ARDroneEngineDisconnectedReceiverDelegate aRDroneEngineDisconnectedReceiverDelegate) {
        this.delegate = aRDroneEngineDisconnectedReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onToolDisconnected();
        }
    }
}
