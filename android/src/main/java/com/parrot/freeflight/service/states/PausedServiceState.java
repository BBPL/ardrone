package com.parrot.freeflight.service.states;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.parrot.ardronetool.ARDroneEngine;
import com.parrot.ardronetool.receivers.ARDroneEngineConnectedReceiverDelegate;
import com.parrot.ardronetool.receivers.ARDroneEngineConnectionFailedReceiverDelegate;
import com.parrot.ardronetool.receivers.ARDroneEngineDisconnectedReceiver;
import com.parrot.ardronetool.receivers.ARDroneEngineDisconnectedReceiverDelegate;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.service.ServiceStateBase;
import com.parrot.freeflight.service.commands.DisconnectCommand;
import com.parrot.freeflight.service.commands.DroneServiceCommand;
import com.parrot.freeflight.service.commands.ResumeCommand;

public class PausedServiceState extends ServiceStateBase implements ARDroneEngineConnectedReceiverDelegate, ARDroneEngineConnectionFailedReceiverDelegate, ARDroneEngineDisconnectedReceiverDelegate {
    private LocalBroadcastManager bm;
    boolean disconnected;
    private ARDroneEngineDisconnectedReceiver disconnectedReceiver;
    private Object lock = new Object();

    public PausedServiceState(DroneControlService droneControlService) {
        super(droneControlService);
        this.bm = LocalBroadcastManager.getInstance(droneControlService.getApplicationContext());
        this.disconnectedReceiver = new ARDroneEngineDisconnectedReceiver(this);
    }

    public void connect() {
        Log.w(getStateName(), "Can't connect. Already connected. Skipped.");
    }

    public void disconnect() {
        Log.d(getStateName(), "Disconnect");
        this.disconnected = false;
        startCommand(new DisconnectCommand(this.context));
    }

    public void onCommandFinished(DroneServiceCommand droneServiceCommand) {
        if (droneServiceCommand instanceof ResumeCommand) {
            setState(new ConnectedServiceState(this.context));
            onResumed();
        }
    }

    protected void onPrepare() {
        this.bm.registerReceiver(this.disconnectedReceiver, new IntentFilter(ARDroneEngine.ARDRONE_ENGINE_DISCONNECTED_ACTION));
    }

    public void onToolConnected() {
        Log.w(getStateName(), "onToolConnected() Should not happen here");
    }

    public void onToolConnectionFailed(int i) {
        onToolDisconnected();
    }

    public void onToolDisconnected() {
        this.disconnected = true;
        synchronized (this.lock) {
            this.lock.notify();
        }
        setState(new DisconnectedServiceState(this.context));
        onDisconnected();
    }

    public void pause() {
        Log.w(getStateName(), "Can't pause. Already paused. Skipped.");
    }

    public void resume() {
        startCommand(new ResumeCommand(this.context));
    }
}
