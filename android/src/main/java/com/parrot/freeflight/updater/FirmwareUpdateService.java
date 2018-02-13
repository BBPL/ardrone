package com.parrot.freeflight.updater;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import com.parrot.freeflight.ui.ConnectScreenViewController.IndicatorState;
import com.parrot.freeflight.utils.NetworkUtils;

public class FirmwareUpdateService extends Service implements UpdaterDelegate {
    private static final ComponentName COMPONENT_NAME = new ComponentName(FirmwareUpdateService.class.getPackage().getName(), FirmwareUpdateService.class.getName());
    public static final String EXT_COMMAND = "ext.command";
    public static final String EXT_COMMAND_MESSAGE = "ext.command.message";
    public static final String EXT_COMMAND_PROGRESS = "ext.command.progress";
    public static final String EXT_COMMAND_RESULT = "ext.command.result";
    public static final String UPDATE_SERVICE_STATE_CHANGED_ACTION = "com.parrot.update.service.state.changed.action";
    private static boolean running;
    private final IBinder binder = new LocalBinder();
    private LocalBroadcastManager broadcastManager;
    private Intent lastIntent;
    private UpdateManager updateManager;
    private WifiLock wifiLock;

    public enum ECommand {
        COMMAND_CHECK_REPAIR,
        COMMAND_SEND_FILE,
        COMMAND_RESTART_DRONE,
        COMMAND_INSTALL
    }

    public enum ECommandResult {
        UNKNOWN,
        SUCCESS,
        FAILURE
    }

    public class LocalBinder extends Binder {
        public FirmwareUpdateService getService() {
            return FirmwareUpdateService.this;
        }
    }

    public static ComponentName getComponentName() {
        return COMPONENT_NAME;
    }

    public static boolean isRunning() {
        return running;
    }

    private void notifyCommandStateChanged(ECommand eCommand, int i, ECommandResult eCommandResult, String str) {
        Intent intent = new Intent(UPDATE_SERVICE_STATE_CHANGED_ACTION);
        intent.putExtra(EXT_COMMAND, eCommand);
        intent.putExtra(EXT_COMMAND_PROGRESS, i);
        if (eCommandResult != null) {
            intent.putExtra(EXT_COMMAND_RESULT, eCommandResult);
        }
        if (str != null) {
            intent.putExtra(EXT_COMMAND_MESSAGE, str);
        }
        this.lastIntent = intent;
        this.broadcastManager.sendBroadcast(intent);
    }

    public IBinder onBind(Intent intent) {
        if (this.lastIntent != null) {
            this.broadcastManager.sendBroadcast(this.lastIntent);
        }
        if (!this.updateManager.isInProgress()) {
            this.updateManager.start();
        }
        return this.binder;
    }

    public void onCreate() {
        super.onCreate();
        this.broadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        if (NetworkUtils.getCurrentNetworkType(this) == 1) {
            this.wifiLock = ((WifiManager) getSystemService("wifi")).createWifiLock("Firmware Update Wifi Lock");
            this.wifiLock.acquire();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        running = false;
        if (this.updateManager.isInProgress()) {
            this.updateManager.stop();
        }
        if (this.wifiLock != null && this.wifiLock.isHeld()) {
            this.wifiLock.release();
        }
    }

    public void onFinished() {
        running = false;
        stopSelf();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (this.lastIntent != null) {
            this.broadcastManager.sendBroadcast(this.lastIntent);
        }
        if (this.updateManager == null) {
            this.updateManager = new UpdateManager(this, this);
            this.updateManager.start();
            running = true;
        }
        return 1;
    }

    public void setCheckingRepairingState(IndicatorState indicatorState, int i, String str) {
        switch (indicatorState) {
            case ACTIVE:
                notifyCommandStateChanged(ECommand.COMMAND_CHECK_REPAIR, 0, ECommandResult.UNKNOWN, str);
                return;
            case PASSED:
                notifyCommandStateChanged(ECommand.COMMAND_CHECK_REPAIR, 100, ECommandResult.SUCCESS, str);
                return;
            case FAILED:
                notifyCommandStateChanged(ECommand.COMMAND_CHECK_REPAIR, 100, ECommandResult.FAILURE, str);
                return;
            default:
                return;
        }
    }

    public void setInstallingState(IndicatorState indicatorState, int i, String str) {
        switch (indicatorState) {
            case ACTIVE:
                notifyCommandStateChanged(ECommand.COMMAND_INSTALL, 0, ECommandResult.UNKNOWN, str);
                return;
            case PASSED:
                notifyCommandStateChanged(ECommand.COMMAND_INSTALL, 100, ECommandResult.SUCCESS, str);
                return;
            case FAILED:
                notifyCommandStateChanged(ECommand.COMMAND_INSTALL, 100, ECommandResult.FAILURE, str);
                return;
            default:
                return;
        }
    }

    public void setRestartingDroneState(IndicatorState indicatorState, int i, String str) {
        switch (indicatorState) {
            case ACTIVE:
                notifyCommandStateChanged(ECommand.COMMAND_RESTART_DRONE, 0, ECommandResult.UNKNOWN, str);
                return;
            case PASSED:
                notifyCommandStateChanged(ECommand.COMMAND_RESTART_DRONE, 100, ECommandResult.SUCCESS, str);
                return;
            case FAILED:
                notifyCommandStateChanged(ECommand.COMMAND_RESTART_DRONE, 100, ECommandResult.FAILURE, str);
                return;
            default:
                return;
        }
    }

    public void setSendingFileState(IndicatorState indicatorState, int i, String str) {
        switch (indicatorState) {
            case ACTIVE:
                notifyCommandStateChanged(ECommand.COMMAND_SEND_FILE, i, ECommandResult.UNKNOWN, str);
                return;
            case PASSED:
                notifyCommandStateChanged(ECommand.COMMAND_SEND_FILE, 100, ECommandResult.SUCCESS, str);
                return;
            case FAILED:
                notifyCommandStateChanged(ECommand.COMMAND_SEND_FILE, 100, ECommandResult.FAILURE, str);
                return;
            default:
                return;
        }
    }
}
