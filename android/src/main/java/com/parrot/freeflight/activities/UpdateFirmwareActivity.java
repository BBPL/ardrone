package com.parrot.freeflight.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.ui.ConnectScreenViewController;
import com.parrot.freeflight.ui.ConnectScreenViewController.IndicatorState;
import com.parrot.freeflight.updater.FirmwareUpdateService;
import com.parrot.freeflight.updater.FirmwareUpdateService.ECommand;
import com.parrot.freeflight.updater.FirmwareUpdateService.ECommandResult;
import com.parrot.freeflight.updater.receivers.FirmwareUpdateServiceReceiver;
import com.parrot.freeflight.updater.receivers.FirmwareUpdateServiceReceiverDelegate;

public class UpdateFirmwareActivity extends ParrotActivity implements ServiceConnection, FirmwareUpdateServiceReceiverDelegate {
    private FirmwareUpdateServiceReceiver firmwareUpdateServiceReceiver;
    private ConnectScreenViewController view;

    private void onInstallChanged(ECommandResult eCommandResult, int i, String str) {
        this.view.setCheckingRepairingState(IndicatorState.PASSED);
        this.view.setSendingFileState(IndicatorState.PASSED);
        this.view.setRestartingDroneState(IndicatorState.PASSED);
        this.view.setProgressVisible(false);
        this.view.setStatus(getString(C0984R.string.ff_ID000002));
        switch (eCommandResult) {
            case SUCCESS:
                this.view.setInstallingState(IndicatorState.PASSED);
                return;
            case FAILURE:
                this.view.setInstallingState(IndicatorState.FAILED);
                return;
            default:
                this.view.setInstallingState(IndicatorState.ACTIVE);
                return;
        }
    }

    private void onRestartChanged(ECommandResult eCommandResult, int i, String str) {
        this.view.setCheckingRepairingState(IndicatorState.PASSED);
        this.view.setSendingFileState(IndicatorState.PASSED);
        this.view.setInstallingState(IndicatorState.EMPTY);
        this.view.setProgressVisible(false);
        this.view.setStatus(getString(C0984R.string.ff_ID000003));
        switch (eCommandResult) {
            case SUCCESS:
                this.view.setRestartingDroneState(IndicatorState.PASSED);
                return;
            case FAILURE:
                this.view.setRestartingDroneState(IndicatorState.FAILED);
                return;
            default:
                this.view.setRestartingDroneState(IndicatorState.ACTIVE);
                return;
        }
    }

    private void onSendFileChanged(ECommandResult eCommandResult, int i, String str) {
        this.view.setCheckingRepairingState(IndicatorState.PASSED);
        this.view.setRestartingDroneState(IndicatorState.EMPTY);
        this.view.setInstallingState(IndicatorState.EMPTY);
        this.view.setStatus(getString(C0984R.string.ff_ID000001));
        if (i <= 0 || i >= 100) {
            this.view.setProgressVisible(false);
        } else {
            this.view.setProgressVisible(true);
            this.view.setProgressValue(i);
        }
        switch (eCommandResult) {
            case SUCCESS:
                this.view.setSendingFileState(IndicatorState.PASSED);
                return;
            case FAILURE:
                this.view.setSendingFileState(IndicatorState.FAILED);
                return;
            default:
                this.view.setSendingFileState(IndicatorState.ACTIVE);
                return;
        }
    }

    private void registerReceivers() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.firmwareUpdateServiceReceiver, new IntentFilter(FirmwareUpdateService.UPDATE_SERVICE_STATE_CHANGED_ACTION));
    }

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.firmwareUpdateServiceReceiver);
    }

    public void onCheckRepairChanged(ECommandResult eCommandResult, int i, String str) {
        this.view.setSendingFileState(IndicatorState.EMPTY);
        this.view.setRestartingDroneState(IndicatorState.EMPTY);
        this.view.setInstallingState(IndicatorState.EMPTY);
        this.view.setStatus(getString(C0984R.string.ff_ID000000));
        if (i == 0) {
            this.view.setCheckingRepairingState(IndicatorState.ACTIVE);
        }
        switch (eCommandResult) {
            case SUCCESS:
                this.view.setCheckingRepairingState(IndicatorState.PASSED);
                return;
            case FAILURE:
                this.view.setCheckingRepairingState(IndicatorState.FAILED);
                return;
            default:
                this.view.setCheckingRepairingState(IndicatorState.ACTIVE);
                return;
        }
    }

    public void onCommandStateChanged(ECommand eCommand, ECommandResult eCommandResult, int i, String str) {
        switch (eCommand) {
            case COMMAND_CHECK_REPAIR:
                onCheckRepairChanged(eCommandResult, i, str);
                break;
            case COMMAND_SEND_FILE:
                onSendFileChanged(eCommandResult, i, str);
                break;
            case COMMAND_INSTALL:
                onInstallChanged(eCommandResult, i, str);
                break;
            case COMMAND_RESTART_DRONE:
                onRestartChanged(eCommandResult, i, str);
                break;
        }
        if (str != null) {
            this.view.setMessage(str);
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.firmware_update_screen);
        this.firmwareUpdateServiceReceiver = new FirmwareUpdateServiceReceiver(this);
        bindService(new Intent(this, DroneControlService.class), this, 1);
        this.view = new ConnectScreenViewController(this);
        this.view.setProgressMaxValue(100);
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__UPDATE_ZONE_OPEN);
    }

    protected void onDestroy() {
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__UPDATE_ZONE_CLOSE);
        super.onDestroy();
        unbindService(this);
    }

    protected void onPause() {
        super.onPause();
        finish();
    }

    protected void onResume() {
        super.onResume();
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
    }

    public void onServiceDisconnected(ComponentName componentName) {
    }

    protected void onStart() {
        super.onStart();
        registerReceivers();
        startService(new Intent(this, FirmwareUpdateService.class));
    }

    protected void onStop() {
        super.onStop();
        unregisterReceivers();
    }
}
