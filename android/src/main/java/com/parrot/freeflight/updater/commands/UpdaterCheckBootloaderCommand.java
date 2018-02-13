package com.parrot.freeflight.updater.commands;

import android.content.Context;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.service.listeners.DroneUpdaterListener.ArDroneToolError;
import com.parrot.freeflight.ui.ConnectScreenViewController.IndicatorState;
import com.parrot.freeflight.updater.UpdateManager;
import com.parrot.freeflight.updater.UpdaterCommand.UpdaterCommandId;
import com.parrot.freeflight.utils.Version;
import org.mortbay.jetty.HttpVersions;

public class UpdaterCheckBootloaderCommand extends UpdaterCommandBase {
    private UpdaterCommandId nextCommand = UpdaterCommandId.UPLOAD_FIRMWARE;

    public UpdaterCheckBootloaderCommand(UpdateManager updateManager) {
        super(updateManager);
    }

    public void execute(Context context) {
        this.delegate.setCheckingRepairingState(IndicatorState.ACTIVE, 0, HttpVersions.HTTP_0_9);
        String droneFirmwareVersion = this.context.getDroneFirmwareVersion();
        String repairVersion = this.context.getFirmwareConfig().getRepairVersion();
        if (droneFirmwareVersion == null || repairVersion == null) {
            setError(ArDroneToolError.E_UPDATE_BOOTLOADER_FAILED);
            onFailure(context.getString(C0984R.string.ff_ID000007) + "\nCheck for boot loader has failed.");
            return;
        }
        if (new Version(droneFirmwareVersion.trim()).isLower(new Version(repairVersion.trim()))) {
            this.nextCommand = UpdaterCommandId.REPAIR_BOOTLOADER;
        } else {
            this.nextCommand = UpdaterCommandId.UPLOAD_FIRMWARE;
        }
        onSuccess();
    }

    public UpdaterCommandId getId() {
        return UpdaterCommandId.CHECK_BOOT_LOADER;
    }

    public UpdaterCommandId getNextCommandId() {
        return this.nextCommand;
    }

    public void onFailure(String str) {
        this.error = ArDroneToolError.E_UPDATE_BOOTLOADER_FAILED;
        this.delegate.setCheckingRepairingState(IndicatorState.FAILED, 100, str);
        this.nextCommand = null;
    }

    public void onSuccess() {
        if (this.nextCommand != UpdaterCommandId.REPAIR_BOOTLOADER) {
            this.delegate.setCheckingRepairingState(IndicatorState.PASSED, 100, HttpVersions.HTTP_0_9);
        }
    }
}
