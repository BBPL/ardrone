package com.parrot.ardronetool.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ARDroneEngineConnectedReceiver extends BroadcastReceiver {
    private ARDroneEngineConnectedReceiverDelegate delegate;

    public ARDroneEngineConnectedReceiver(ARDroneEngineConnectedReceiverDelegate aRDroneEngineConnectedReceiverDelegate) {
        this.delegate = aRDroneEngineConnectedReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onToolConnected();
        }
    }
}
