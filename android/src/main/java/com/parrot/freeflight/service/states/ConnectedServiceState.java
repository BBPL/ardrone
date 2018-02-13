package com.parrot.freeflight.service.states;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.parrot.ardronetool.ARDroneEngine;
import com.parrot.ardronetool.receivers.ARDroneEngineDisconnectedReceiver;
import com.parrot.ardronetool.receivers.ARDroneEngineDisconnectedReceiverDelegate;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.service.ServiceStateBase;
import com.parrot.freeflight.service.commands.DisconnectCommand;
import com.parrot.freeflight.service.commands.DroneServiceCommand;
import com.parrot.freeflight.service.commands.PauseCommand;
import com.parrot.freeflight.service.commands.ResumeCommand;

public class ConnectedServiceState extends ServiceStateBase implements ARDroneEngineDisconnectedReceiverDelegate {
    private LocalBroadcastManager bm;
    boolean disconnected;
    private ARDroneEngineDisconnectedReceiver disconnectedReceiver;
    private Object lock = new Object();

    public ConnectedServiceState(DroneControlService droneControlService) {
        super(droneControlService);
        this.bm = LocalBroadcastManager.getInstance(droneControlService.getApplicationContext());
        this.disconnectedReceiver = new ARDroneEngineDisconnectedReceiver(this);
    }

    public void connect() {
        Log.w(getStateName(), "Already connected. Skipped.");
    }

    public void disconnect() {
        Log.d(getStateName(), "Disconnect");
        this.disconnected = false;
        startCommand(new DisconnectCommand(this.context));
    }

    public void onCommandFinished(DroneServiceCommand droneServiceCommand) {
        if (droneServiceCommand instanceof ResumeCommand) {
            onResumed();
        } else if (droneServiceCommand instanceof PauseCommand) {
            setState(new PausedServiceState(this.context));
            onPaused();
        }
    }

    protected void onFinalize() {
        this.bm.unregisterReceiver(this.disconnectedReceiver);
    }

    protected void onPrepare() {
        this.bm.registerReceiver(this.disconnectedReceiver, new IntentFilter(ARDroneEngine.ARDRONE_ENGINE_DISCONNECTED_ACTION));
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
        startCommand(new PauseCommand(this.context));
    }

    public void resume() {
        startCommand(new ResumeCommand(this.context));
    }
}
