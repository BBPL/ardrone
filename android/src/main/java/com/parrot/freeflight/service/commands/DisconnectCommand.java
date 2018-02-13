package com.parrot.freeflight.service.commands;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.parrot.ardronetool.ARDroneEngine;
import com.parrot.ardronetool.receivers.ARDroneEngineConnectionFailedReceiverDelegate;
import com.parrot.ardronetool.receivers.ARDroneEngineDisconnectedReceiver;
import com.parrot.ardronetool.receivers.ARDroneEngineDisconnectedReceiverDelegate;
import com.parrot.freeflight.service.DroneControlService;

public class DisconnectCommand extends DroneServiceCommand implements ARDroneEngineDisconnectedReceiverDelegate, ARDroneEngineConnectionFailedReceiverDelegate {
    private LocalBroadcastManager bm;
    private ARDroneEngineDisconnectedReceiver disconnectedReceiver = new ARDroneEngineDisconnectedReceiver(this);
    private ARDroneEngine droneEngine;

    public DisconnectCommand(DroneControlService droneControlService) {
        super(droneControlService);
        this.droneEngine = ARDroneEngine.instance(droneControlService.getApplicationContext());
        this.bm = LocalBroadcastManager.getInstance(droneControlService.getApplicationContext());
    }

    private void registerListeners() {
        this.bm.registerReceiver(this.disconnectedReceiver, new IntentFilter(ARDroneEngine.ARDRONE_ENGINE_DISCONNECTED_ACTION));
    }

    private void unregisterListeners() {
        this.bm.unregisterReceiver(this.disconnectedReceiver);
    }

    public void execute() {
        registerListeners();
        this.droneEngine.resume();
        this.droneEngine.stop();
    }

    public void onToolConnectionFailed(int i) {
        unregisterListeners();
    }

    public void onToolDisconnected() {
        unregisterListeners();
        this.context.onCommandFinished(this);
    }
}
