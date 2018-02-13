package com.parrot.freeflight.updater.commands;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.util.Log;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.service.DroneConfig;
import com.parrot.freeflight.ui.ConnectScreenViewController.IndicatorState;
import com.parrot.freeflight.updater.UpdateManager;
import com.parrot.freeflight.updater.UpdaterCommand.UpdaterCommandId;
import com.parrot.freeflight.utils.FTPUtils;
import com.parrot.freeflight.utils.TelnetUtils;
import org.mortbay.jetty.HttpVersions;

public class UpdaterRestartDroneCommand extends UpdaterCommandBase {
    private boolean droneRestarted;

    public UpdaterRestartDroneCommand(UpdateManager updateManager) {
        super(updateManager);
    }

    private void onSuccess() {
        this.delegate.setRestartingDroneState(IndicatorState.PASSED, 100, HttpVersions.HTTP_0_9);
    }

    public void execute(Context context) {
        this.delegate.setRestartingDroneState(IndicatorState.ACTIVE, 0, HttpVersions.HTTP_0_9);
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.context.getContext().getSystemService("connectivity")).getActiveNetworkInfo();
        this.droneRestarted = false;
        Log.d(getCommandName(), "Connected via " + activeNetworkInfo.getTypeName() + " " + activeNetworkInfo.getSubtypeName());
        WifiLock wifiLock = null;
        if (activeNetworkInfo.getType() == 1) {
            wifiLock = ((WifiManager) this.context.getContext().getSystemService("wifi")).createWifiLock("DRONE_RESTART_LOCK");
            wifiLock.acquire();
        }
        if (!this.context.getDroneFirmwareVersion().startsWith("2.")) {
            this.delegate.setRestartingDroneState(IndicatorState.ACTIVE, 0, this.context.getContext().getString(C0984R.string.ff_ID000008));
        } else if (!TelnetUtils.executeRemotely(DroneConfig.getDroneHost(), 23, "reboot\n")) {
            this.delegate.setRestartingDroneState(IndicatorState.ACTIVE, 0, this.context.getContext().getString(C0984R.string.ff_ID000008));
        }
        while (!this.context.isShuttingDown()) {
            if (!this.droneRestarted) {
                if (FTPUtils.downloadFile(context, DroneConfig.getDroneHost(), DroneConfig.getFtpPort(), "version.txt") == null) {
                    this.droneRestarted = true;
                    Log.d(getCommandName(), "Connection lost. Marking as restarted");
                    onSuccess();
                    break;
                }
                Log.d(getCommandName(), "Connection still enabled");
            }
            try {
                Thread.sleep(1000);
                Log.d(getCommandName(), "Checking connection...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (wifiLock != null) {
            wifiLock.release();
        }
    }

    public UpdaterCommandId getId() {
        return UpdaterCommandId.RESTART_DRONE;
    }

    public UpdaterCommandId getNextCommandId() {
        return UpdaterCommandId.INSTALL;
    }
}
