package com.parrot.freeflight.updater.commands;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.parrot.ardronetool.ftp.FTPClient;
import com.parrot.ardronetool.ftp.FTPClientStatus;
import com.parrot.ardronetool.ftp.FTPClientStatus.FTPStatus;
import com.parrot.ardronetool.ftp.FTPOperation;
import com.parrot.ardronetool.ftp.FTPProgressListener;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.service.DroneConfig;
import com.parrot.freeflight.ui.ConnectScreenViewController.IndicatorState;
import com.parrot.freeflight.updater.UpdateManager;
import com.parrot.freeflight.updater.UpdaterCommand.UpdaterCommandId;
import com.parrot.freeflight.utils.CacheUtils;
import com.parrot.freeflight.utils.TelnetUtils;
import java.io.File;
import org.mortbay.jetty.HttpVersions;

public class UpdaterUploadFirmwareCommand extends UpdaterCommandBase implements FTPProgressListener {
    private static final int RETRY_COUNT = 2;
    private final Object lock = new Object();
    private UpdaterCommandId nextCommand;
    private boolean uploadFinished;

    public UpdaterUploadFirmwareCommand(UpdateManager updateManager) {
        super(updateManager);
    }

    private boolean uploadFirmwareToDrone(Context context, String str) {
        boolean z = false;
        TelnetUtils.executeRemotely(DroneConfig.getDroneHost(), 23, "cd /update && rm *.plf \n");
        String str2 = "firmware/" + str;
        String droneHost = DroneConfig.getDroneHost();
        int ftpPort = DroneConfig.getFtpPort();
        Log.d(getCommandName(), "Uploading file " + str2 + " to " + droneHost + ":" + ftpPort);
        File createTempFile = CacheUtils.createTempFile(context);
        FTPClient fTPClient = new FTPClient();
        if (createTempFile == null) {
            if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                Log.w(getCommandName(), "Can't delete file " + createTempFile.getAbsolutePath());
            }
            if (!fTPClient.isConnected()) {
                return false;
            }
        }
        try {
            if (!CacheUtils.copyFileFromAssetsToStorage(context.getAssets(), str2, createTempFile)) {
                Log.e(getCommandName(), "uploadFile() Can't copy file " + str2 + " to " + createTempFile.getAbsolutePath());
                if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                    Log.w(getCommandName(), "Can't delete file " + createTempFile.getAbsolutePath());
                }
                if (!fTPClient.isConnected()) {
                    return false;
                }
            } else if (fTPClient.connect(droneHost, ftpPort)) {
                fTPClient.setProgressListener(this);
                this.uploadFinished = false;
                fTPClient.put(createTempFile.getAbsolutePath(), str);
                while (!this.context.isShuttingDown() && !this.uploadFinished) {
                    synchronized (this.lock) {
                        try {
                            this.lock.wait(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (this.context.isShuttingDown()) {
                    Log.d(getCommandName(), "uploadFile() Aborting the transfer to  " + droneHost + ":" + ftpPort);
                    fTPClient.setProgressListener(null);
                    if (!fTPClient.abort()) {
                        Log.d(getCommandName(), "uploadFile() Some problem has occured during ftp abort");
                    }
                    sleep(1000);
                    if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                        Log.w(getCommandName(), "Can't delete file " + createTempFile.getAbsolutePath());
                    }
                    if (!fTPClient.isConnected()) {
                        return false;
                    }
                } else if (FTPClientStatus.isFailure(fTPClient.getReplyStatus())) {
                    Log.e(getCommandName(), "uploadFile() Failed to upload file to ftp " + droneHost + ":" + ftpPort);
                    if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                        Log.w(getCommandName(), "Can't delete file " + createTempFile.getAbsolutePath());
                    }
                    if (!fTPClient.isConnected()) {
                        return false;
                    }
                } else {
                    if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                        Log.w(getCommandName(), "Can't delete file " + createTempFile.getAbsolutePath());
                    }
                    if (!fTPClient.isConnected()) {
                        return true;
                    }
                    z = true;
                }
            } else {
                Log.e(getCommandName(), "uploadFile() Can't connect to " + droneHost + ":" + ftpPort);
                if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                    Log.w(getCommandName(), "Can't delete file " + createTempFile.getAbsolutePath());
                }
                if (!fTPClient.isConnected()) {
                    return false;
                }
            }
        } catch (Throwable th) {
            if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                Log.w(getCommandName(), "Can't delete file " + createTempFile.getAbsolutePath());
            }
            if (fTPClient.isConnected()) {
                fTPClient.disconnect();
            }
        }
        fTPClient.disconnect();
        return z;
    }

    public void execute(Context context) {
        boolean z = false;
        synchronized (this) {
            boolean z2;
            this.delegate.setSendingFileState(IndicatorState.ACTIVE, 0, HttpVersions.HTTP_0_9);
            String droneFirmwareVersion = this.context.getDroneFirmwareVersion();
            if (droneFirmwareVersion.startsWith("1.")) {
                droneFirmwareVersion = this.context.getFirmwareConfig().getFileName();
            } else if (droneFirmwareVersion.startsWith("2.")) {
                droneFirmwareVersion = this.context.getFirmwareConfig().getFileNameV2();
            } else {
                onFailure("Unable to get AR.Drone version.");
            }
            int i = 0;
            while (!z && i < 2) {
                z = uploadFirmwareToDrone(context, droneFirmwareVersion);
                if (this.context.isShuttingDown()) {
                    z2 = z;
                    break;
                } else if (!z) {
                    if (this.context.isShuttingDown()) {
                        continue;
                    } else {
                        i++;
                        try {
                            wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            z2 = z;
                        }
                    }
                }
            }
            z2 = z;
            if (z2) {
                onSuccess();
            } else {
                onFailure(null);
            }
        }
    }

    public UpdaterCommandId getId() {
        return UpdaterCommandId.UPLOAD_FIRMWARE;
    }

    public UpdaterCommandId getNextCommandId() {
        return this.nextCommand;
    }

    protected void onFailure(String str) {
        if (str == null) {
            str = String.format(this.context.getContext().getString(C0984R.string.ff_ID000006, new Object[]{Build.MANUFACTURER.toUpperCase()}), new Object[0]);
        }
        this.delegate.setSendingFileState(IndicatorState.FAILED, 100, str);
    }

    public void onProgress(int i) {
        this.delegate.setSendingFileState(IndicatorState.ACTIVE, i, HttpVersions.HTTP_0_9);
    }

    public void onStatusChanged(FTPStatus fTPStatus, float f, FTPOperation fTPOperation) {
        if (fTPStatus == FTPStatus.FTP_PROGRESS) {
            onProgress(Math.round(f));
            return;
        }
        this.uploadFinished = true;
        synchronized (this.lock) {
            this.lock.notify();
        }
    }

    protected void onSuccess() {
        this.nextCommand = UpdaterCommandId.RESTART_DRONE;
        this.errorMessage = null;
        this.status = HttpVersions.HTTP_0_9;
        this.delegate.setSendingFileState(IndicatorState.PASSED, 100, HttpVersions.HTTP_0_9);
    }
}
