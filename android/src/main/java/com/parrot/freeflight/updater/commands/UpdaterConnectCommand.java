package com.parrot.freeflight.updater.commands;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.service.DroneConfig;
import com.parrot.freeflight.service.listeners.DroneUpdaterListener.ArDroneToolError;
import com.parrot.freeflight.ui.ConnectScreenViewController.IndicatorState;
import com.parrot.freeflight.updater.UpdateManager;
import com.parrot.freeflight.updater.UpdaterCommand.UpdaterCommandId;
import com.parrot.freeflight.utils.FtpDelegate;

public class UpdaterConnectCommand extends UpdaterCommandBase implements FtpDelegate {
    private String firmwareVersion;
    private UpdaterCommandId nextCommand = UpdaterCommandId.CHECK_BOOT_LOADER;
    private boolean requestSent;

    public UpdaterConnectCommand(UpdateManager updateManager) {
        super(updateManager);
    }

    private void onFailure(Context context) {
        this.error = ArDroneToolError.E_WIFI_NOT_AVAILABLE;
        this.nextCommand = null;
        this.delegate.setCheckingRepairingState(IndicatorState.FAILED, 100, String.format(context.getString(C0984R.string.ff_ID000006, new Object[]{Build.MANUFACTURER.toUpperCase()}), new Object[0]));
    }

    private void onSuccess() {
    }

    private void retrieveFirmwareVersion(Context context) {
        this.requestSent = false;
        while (this.firmwareVersion == null && !this.context.isShuttingDown()) {
            if (!this.requestSent) {
                this.context.downloadFileAsync(context, DroneConfig.getDroneHost(), DroneConfig.getFtpPort(), "version.txt", this);
                this.requestSent = true;
            }
            if (this.firmwareVersion == null) {
                try {
                    wait(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!this.requestSent) {
                return;
            }
        }
    }

    private void saveFirmwareVersion(String str) {
        this.context.setDroneFirmwareVersion(str);
    }

    public void execute(Context context) {
        synchronized (this) {
            this.firmwareVersion = null;
            retrieveFirmwareVersion(context);
            if (this.firmwareVersion == null) {
                onFailure(context);
            } else {
                saveFirmwareVersion(this.firmwareVersion);
                onSuccess();
            }
        }
    }

    public void ftpOperationFailure() {
        Log.w(getCommandName(), "Can't get file from the drone due to error.");
        this.requestSent = false;
    }

    public void ftpOperationSuccess(String str) {
        this.firmwareVersion = str;
        this.requestSent = false;
    }

    public UpdaterCommandId getId() {
        return UpdaterCommandId.CONNECT;
    }

    public UpdaterCommandId getNextCommandId() {
        return this.nextCommand;
    }
}
