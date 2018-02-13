package com.parrot.freeflight.service.commands;

import com.parrot.freeflight.service.DroneControlService;

public abstract class DroneServiceCommand {
    protected DroneControlService context;

    public DroneServiceCommand(DroneControlService droneControlService) {
        this.context = droneControlService;
    }

    public abstract void execute();
}
