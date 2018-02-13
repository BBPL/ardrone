package com.parrot.freeflight.updater.receivers;

import com.parrot.freeflight.updater.FirmwareUpdateService.ECommand;
import com.parrot.freeflight.updater.FirmwareUpdateService.ECommandResult;

public interface FirmwareUpdateServiceReceiverDelegate {
    void onCommandStateChanged(ECommand eCommand, ECommandResult eCommandResult, int i, String str);
}
