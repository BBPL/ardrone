package com.parrot.freeflight.service.commands;

import com.parrot.ardronetool.ARDroneEngine;
import com.parrot.freeflight.service.DroneControlService;

public class ResumeCommand extends DroneServiceCommand {
    private ARDroneEngine droneEngine;

    public ResumeCommand(DroneControlService droneControlService) {
        super(droneControlService);
        this.droneEngine = ARDroneEngine.instance(droneControlService.getApplicationContext());
    }

    public void execute() {
        this.droneEngine.resume();
        this.context.onCommandFinished(this);
    }
}
