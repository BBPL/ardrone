package com.parrot.freeflight.service.commands;

import com.parrot.ardronetool.ARDroneEngine;
import com.parrot.freeflight.service.DroneControlService;

public class PauseCommand extends DroneServiceCommand {
    private ARDroneEngine droneEngine;

    public PauseCommand(DroneControlService droneControlService) {
        super(droneControlService);
        this.droneEngine = ARDroneEngine.instance(droneControlService.getApplicationContext());
    }

    public void execute() {
        this.droneEngine.pause();
        this.context.onCommandFinished(this);
    }
}
