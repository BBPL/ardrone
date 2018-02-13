package com.parrot.freeflight.updater.commands;

import android.content.Context;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.service.DroneConfig;
import com.parrot.freeflight.ui.ConnectScreenViewController.IndicatorState;
import com.parrot.freeflight.updater.UpdateManager;
import com.parrot.freeflight.updater.UpdaterCommand.UpdaterCommandId;
import com.parrot.freeflight.updater.utils.FirmwareConfig;
import com.parrot.freeflight.utils.FTPUtils;
import com.parrot.freeflight.utils.TelnetUtils;
import org.mortbay.jetty.HttpVersions;

public class UpdaterRepairBootloaderCommand extends UpdaterCommandBase {
    private UpdaterCommandId nextCommand = UpdaterCommandId.UPLOAD_FIRMWARE;

    public UpdaterRepairBootloaderCommand(UpdateManager updateManager) {
        super(updateManager);
    }

    private boolean modifyAccessRights(String str) {
        return TelnetUtils.executeRemotely(DroneConfig.getDroneHost(), 23, "cd `find /data -name \"" + str + "\" -exec dirname {} \\;` && chmod 755 " + str + "\n");
    }

    private void onFailure(Context context) {
        this.delegate.setCheckingRepairingState(IndicatorState.FAILED, 100, context.getString(C0984R.string.ff_ID000091));
        this.nextCommand = null;
    }

    private boolean repair(String str) {
        return TelnetUtils.executeRemotely(DroneConfig.getDroneHost(), 23, "cd `find /data -name \"" + str + "\" -exec dirname {} \\;` && ./" + str + "\n");
    }

    private boolean uploadBootldr(Context context, String str) {
        return FTPUtils.uploadFileSync(context, DroneConfig.getDroneHost(), 21, "firmware/" + str, str);
    }

    private boolean uploadRepair(Context context, String str) {
        return FTPUtils.uploadFileSync(context, DroneConfig.getDroneHost(), 21, "firmware/" + str, str);
    }

    public void execute(Context context) {
        this.delegate.setCheckingRepairingState(IndicatorState.ACTIVE, 50, HttpVersions.HTTP_0_9);
        FirmwareConfig firmwareConfig = this.context.getFirmwareConfig();
        String repairFileName = firmwareConfig.getRepairFileName();
        if (!uploadBootldr(context, firmwareConfig.getBootldrFileName())) {
            onFailure(context);
        } else if (!uploadRepair(context, repairFileName)) {
            onFailure(context);
        } else if (!modifyAccessRights(repairFileName)) {
            onFailure(context);
        } else if (repair(repairFileName)) {
            this.delegate.setCheckingRepairingState(IndicatorState.PASSED, 100, HttpVersions.HTTP_0_9);
        } else {
            onFailure(context);
        }
    }

    public UpdaterCommandId getId() {
        return UpdaterCommandId.REPAIR_BOOTLOADER;
    }

    public UpdaterCommandId getNextCommandId() {
        return this.nextCommand;
    }
}
