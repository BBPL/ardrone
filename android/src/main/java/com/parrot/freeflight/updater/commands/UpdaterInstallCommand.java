package com.parrot.freeflight.updater.commands;

import android.content.Context;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.ui.ConnectScreenViewController.IndicatorState;
import com.parrot.freeflight.updater.UpdateManager;
import com.parrot.freeflight.updater.UpdaterCommand.UpdaterCommandId;

public class UpdaterInstallCommand extends UpdaterCommandBase {
    public UpdaterInstallCommand(UpdateManager updateManager) {
        super(updateManager);
    }

    public void execute(Context context) {
        this.delegate.setInstallingState(IndicatorState.ACTIVE, 0, context.getString(C0984R.string.if_ardrone_led_green_reset_wifi_connection));
    }

    public UpdaterCommandId getId() {
        return UpdaterCommandId.INSTALL;
    }

    public UpdaterCommandId getNextCommandId() {
        return null;
    }
}
