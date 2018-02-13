package com.parrot.freeflight.service.listeners;

import com.parrot.freeflight.updater.UpdaterCommand;

public interface DroneUpdaterListener {

    public enum ArDroneToolError {
        E_NONE,
        E_UNKNOWN,
        E_WIFI_NOT_AVAILABLE,
        E_UPDATE_BOOTLOADER_FAILED,
        E_UPDATE_BOOTLOADER_CANCELED,
        E_APPLICATION_SHOULD_BE_UPDATED,
        E_FIRMWARE_SHOULD_BE_UPDATED,
        E_CHECK_VERSION_FAILED,
        E_UPDATE_FIRMWARE_FAILED
    }

    void onPostCommandExecute(UpdaterCommand updaterCommand);

    void onPreCommandExecute(UpdaterCommand updaterCommand);

    void onUpdateCommand(UpdaterCommand updaterCommand);
}
