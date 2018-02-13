package com.parrot.freeflight.ui.controlhandlers;

import com.parrot.freeflight.service.DroneControlService.DroneFlipDirection;

public interface OnDroneFlipListener {
    boolean onFlip();

    boolean onFlip(DroneFlipDirection droneFlipDirection);
}
