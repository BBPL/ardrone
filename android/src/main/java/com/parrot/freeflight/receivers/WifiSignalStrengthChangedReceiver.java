package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

public class WifiSignalStrengthChangedReceiver extends BroadcastReceiver {
    private WifiSignalStrengthReceiverDelegate delegate;

    public WifiSignalStrengthChangedReceiver(WifiSignalStrengthReceiverDelegate wifiSignalStrengthReceiverDelegate) {
        this.delegate = wifiSignalStrengthReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            this.delegate.onWifiSignalStrengthChanged(WifiManager.calculateSignalLevel(intent.getIntExtra("newRssi", 0), 4));
        }
    }
}
