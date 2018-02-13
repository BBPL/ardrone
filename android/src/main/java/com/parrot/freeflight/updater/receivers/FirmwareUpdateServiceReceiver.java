package com.parrot.freeflight.updater.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.parrot.freeflight.updater.FirmwareUpdateService;
import com.parrot.freeflight.updater.FirmwareUpdateService.ECommand;
import com.parrot.freeflight.updater.FirmwareUpdateService.ECommandResult;

public class FirmwareUpdateServiceReceiver extends BroadcastReceiver {
    private FirmwareUpdateServiceReceiverDelegate delegate;

    public FirmwareUpdateServiceReceiver(FirmwareUpdateServiceReceiverDelegate firmwareUpdateServiceReceiverDelegate) {
        this.delegate = firmwareUpdateServiceReceiverDelegate;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null && intent.getAction().equals(FirmwareUpdateService.UPDATE_SERVICE_STATE_CHANGED_ACTION)) {
            Bundle extras = intent.getExtras();
            this.delegate.onCommandStateChanged((ECommand) extras.get(FirmwareUpdateService.EXT_COMMAND), (ECommandResult) extras.get(FirmwareUpdateService.EXT_COMMAND_RESULT), extras.getInt(FirmwareUpdateService.EXT_COMMAND_PROGRESS), extras.getString(FirmwareUpdateService.EXT_COMMAND_MESSAGE));
        }
    }
}
