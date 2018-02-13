package com.parrot.freeflight.service.states;

import android.util.Log;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.service.ServiceStateBase;
import com.parrot.freeflight.service.commands.ConnectCommand;
import com.parrot.freeflight.service.commands.DroneServiceCommand;

public class DisconnectedServiceState extends ServiceStateBase {
    public DisconnectedServiceState(DroneControlService droneControlService) {
        super(droneControlService);
    }

    public void connect() {
        startCommand(new ConnectCommand(this.context));
    }

    public void disconnect() {
        Log.w(getStateName(), "Disconnect. Already disconnected. Skipped...");
    }

    public void onCommandFinished(DroneServiceCommand droneServiceCommand) {
        if (!(droneServiceCommand instanceof ConnectCommand)) {
            return;
        }
        if (((ConnectCommand) droneServiceCommand).getResult() == 0) {
            setState(new ConnectedServiceState(this.context));
            onConnected();
            return;
        }
        onDisconnected();
    }

    public void pause() {
        Log.w(getStateName(), "Can't pause while in disconnected state. Skipped.");
    }

    public void resume() {
        Log.w(getStateName(), "Can't resume while in disconnected state. Skipped.");
    }
}
