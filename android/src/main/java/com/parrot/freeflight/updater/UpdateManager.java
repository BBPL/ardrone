package com.parrot.freeflight.updater;

import android.content.Context;
import android.util.Log;
import com.parrot.ardronetool.ftp.FTPClient;
import com.parrot.freeflight.service.listeners.DroneUpdaterListener;
import com.parrot.freeflight.updater.UpdaterCommand.UpdaterCommandId;
import com.parrot.freeflight.updater.commands.UpdaterCheckBootloaderCommand;
import com.parrot.freeflight.updater.commands.UpdaterConnectCommand;
import com.parrot.freeflight.updater.commands.UpdaterInstallCommand;
import com.parrot.freeflight.updater.commands.UpdaterRepairBootloaderCommand;
import com.parrot.freeflight.updater.commands.UpdaterRestartDroneCommand;
import com.parrot.freeflight.updater.commands.UpdaterUploadFirmwareCommand;
import com.parrot.freeflight.updater.utils.FirmwareConfig;
import com.parrot.freeflight.utils.CacheUtils;
import com.parrot.freeflight.utils.FtpDelegate;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.xmlpull.v1.XmlPullParserException;

public final class UpdateManager implements Runnable {
    private static final String TAG = "UpdateManager";
    private Queue<UpdaterCommand> cmdQueue;
    private Map<UpdaterCommandId, UpdaterCommand> commands;
    private Context context;
    private UpdaterDelegate delegate;
    private Thread downloadFileThread;
    private String fimrmwareVersion;
    private FirmwareConfig firmwareConfig;
    private FTPClient ftpClient;
    private ArrayList<DroneUpdaterListener> listeners;
    private boolean stopThreads;
    private Thread workerThread;

    public UpdateManager(Context context, UpdaterDelegate updaterDelegate) {
        this.context = context;
        this.delegate = updaterDelegate;
        try {
            this.firmwareConfig = new FirmwareConfig(context, "firmware");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        this.listeners = new ArrayList();
        this.cmdQueue = new LinkedList();
        this.commands = new HashMap(UpdaterCommandId.values().length);
        this.commands.put(UpdaterCommandId.CONNECT, new UpdaterConnectCommand(this));
        this.commands.put(UpdaterCommandId.CHECK_BOOT_LOADER, new UpdaterCheckBootloaderCommand(this));
        this.commands.put(UpdaterCommandId.REPAIR_BOOTLOADER, new UpdaterRepairBootloaderCommand(this));
        this.commands.put(UpdaterCommandId.UPLOAD_FIRMWARE, new UpdaterUploadFirmwareCommand(this));
        this.commands.put(UpdaterCommandId.RESTART_DRONE, new UpdaterRestartDroneCommand(this));
        this.commands.put(UpdaterCommandId.INSTALL, new UpdaterInstallCommand(this));
        this.ftpClient = null;
    }

    private void cancelAnyFtpOperation() {
        if (this.ftpClient != null) {
            this.ftpClient.abort();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.ftpClient.disconnect();
            this.ftpClient = null;
        }
    }

    public void addListener(final DroneUpdaterListener droneUpdaterListener) {
        new Thread(new Runnable() {
            public void run() {
                synchronized (UpdateManager.this.listeners) {
                    if (!UpdateManager.this.listeners.contains(droneUpdaterListener)) {
                        UpdateManager.this.listeners.add(droneUpdaterListener);
                    }
                }
            }
        }).start();
    }

    public void downloadFileAsync(Context context, String str, int i, String str2, FtpDelegate ftpDelegate) {
        final String str3 = str;
        final int i2 = i;
        final String str4 = str2;
        final FtpDelegate ftpDelegate2 = ftpDelegate;
        Runnable c12493 = new Runnable() {
            public void run() {
                Throwable th;
                File createTempFile;
                try {
                    FtpDelegate ftpDelegate;
                    UpdateManager.this.ftpClient = new FTPClient();
                    if (UpdateManager.this.ftpClient.connect(str3, i2)) {
                        createTempFile = CacheUtils.createTempFile(UpdateManager.this.context);
                        if (createTempFile == null) {
                            try {
                                Log.w(UpdateManager.TAG, "downloadFile failed. Can't connect");
                                if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                                    Log.w(UpdateManager.TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                                }
                                if (UpdateManager.this.ftpClient != null && UpdateManager.this.ftpClient.isConnected()) {
                                    UpdateManager.this.ftpClient.disconnect();
                                    UpdateManager.this.ftpClient = null;
                                }
                                ftpDelegate = ftpDelegate2;
                            } catch (Throwable th2) {
                                th = th2;
                                if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                                    Log.w(UpdateManager.TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                                }
                                if (UpdateManager.this.ftpClient != null && UpdateManager.this.ftpClient.isConnected()) {
                                    UpdateManager.this.ftpClient.disconnect();
                                    UpdateManager.this.ftpClient = null;
                                }
                                ftpDelegate2.ftpOperationFailure();
                                throw th;
                            }
                        } else if (!UpdateManager.this.ftpClient.getSync(str4, createTempFile.getAbsolutePath())) {
                            if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                                Log.w(UpdateManager.TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                            }
                            if (UpdateManager.this.ftpClient != null && UpdateManager.this.ftpClient.isConnected()) {
                                UpdateManager.this.ftpClient.disconnect();
                                UpdateManager.this.ftpClient = null;
                            }
                            ftpDelegate = ftpDelegate2;
                        } else if (createTempFile.exists()) {
                            StringBuffer readFromFile = CacheUtils.readFromFile(createTempFile);
                            String stringBuffer = readFromFile != null ? readFromFile.toString() : null;
                            if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                                Log.w(UpdateManager.TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                            }
                            if (UpdateManager.this.ftpClient != null && UpdateManager.this.ftpClient.isConnected()) {
                                UpdateManager.this.ftpClient.disconnect();
                                UpdateManager.this.ftpClient = null;
                            }
                            if (stringBuffer != null) {
                                ftpDelegate2.ftpOperationSuccess(stringBuffer);
                                return;
                            }
                            ftpDelegate = ftpDelegate2;
                        } else {
                            if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                                Log.w(UpdateManager.TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                            }
                            if (UpdateManager.this.ftpClient != null && UpdateManager.this.ftpClient.isConnected()) {
                                UpdateManager.this.ftpClient.disconnect();
                                UpdateManager.this.ftpClient = null;
                            }
                            ftpDelegate = ftpDelegate2;
                        }
                    } else {
                        Log.w(UpdateManager.TAG, "downloadFile failed. Can't connect");
                        if (UpdateManager.this.ftpClient != null && UpdateManager.this.ftpClient.isConnected()) {
                            UpdateManager.this.ftpClient.disconnect();
                            UpdateManager.this.ftpClient = null;
                        }
                        ftpDelegate = ftpDelegate2;
                    }
                    ftpDelegate.ftpOperationFailure();
                } catch (Throwable th3) {
                    th = th3;
                    createTempFile = null;
                    Log.w(UpdateManager.TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                    UpdateManager.this.ftpClient.disconnect();
                    UpdateManager.this.ftpClient = null;
                    ftpDelegate2.ftpOperationFailure();
                    throw th;
                }
            }
        };
        if (this.downloadFileThread != null) {
            cancelAnyFtpOperation();
            try {
                this.downloadFileThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.downloadFileThread = new Thread(c12493);
        this.downloadFileThread.start();
    }

    public Context getContext() {
        return this.context;
    }

    public UpdaterDelegate getDelegate() {
        return this.delegate;
    }

    public String getDroneFirmwareVersion() {
        return this.fimrmwareVersion;
    }

    public FirmwareConfig getFirmwareConfig() {
        return this.firmwareConfig;
    }

    public boolean isInProgress() {
        return this.workerThread != null && this.workerThread.isAlive();
    }

    public boolean isShuttingDown() {
        return this.stopThreads;
    }

    public void notifyOnPostExecuteListeners(UpdaterCommand updaterCommand, ArrayList<DroneUpdaterListener> arrayList) {
        if (arrayList != null) {
            synchronized (arrayList) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    DroneUpdaterListener droneUpdaterListener = (DroneUpdaterListener) it.next();
                    if (droneUpdaterListener != null) {
                        droneUpdaterListener.onPostCommandExecute(updaterCommand);
                    }
                }
            }
        }
    }

    public void notifyOnPreExecuteListeners(UpdaterCommand updaterCommand) {
        if (this.listeners != null) {
            synchronized (this.listeners) {
                Iterator it = this.listeners.iterator();
                while (it.hasNext()) {
                    DroneUpdaterListener droneUpdaterListener = (DroneUpdaterListener) it.next();
                    if (droneUpdaterListener != null) {
                        droneUpdaterListener.onPreCommandExecute(updaterCommand);
                    }
                }
            }
        }
    }

    public void notifyOnUpdateListeners(UpdaterCommand updaterCommand) {
        if (this.listeners != null) {
            synchronized (this.listeners) {
                Iterator it = this.listeners.iterator();
                while (it.hasNext()) {
                    DroneUpdaterListener droneUpdaterListener = (DroneUpdaterListener) it.next();
                    if (droneUpdaterListener != null) {
                        droneUpdaterListener.onUpdateCommand(updaterCommand);
                    }
                }
            }
        }
    }

    public void onPostExecuteCommand(UpdaterCommand updaterCommand) {
        Log.d(TAG, "State " + updaterCommand.getCommandName() + " has finished.");
        notifyOnPostExecuteListeners(updaterCommand, this.listeners);
        UpdaterCommandId nextCommandId = updaterCommand.getNextCommandId();
        if (nextCommandId != null) {
            startCommand((UpdaterCommand) this.commands.get(nextCommandId));
        } else {
            this.stopThreads = true;
        }
    }

    public void onPreExecuteCommand(UpdaterCommand updaterCommand) {
        Log.d(TAG, "State " + updaterCommand.getCommandName());
        notifyOnPreExecuteListeners(updaterCommand);
    }

    public void onUpdate(UpdaterCommand updaterCommand) {
        notifyOnUpdateListeners(updaterCommand);
    }

    public void removeListener(final DroneUpdaterListener droneUpdaterListener) {
        new Thread(new Runnable() {
            public void run() {
                synchronized (UpdateManager.this.listeners) {
                    UpdateManager.this.listeners.remove(droneUpdaterListener);
                }
            }
        }).start();
    }

    public void run() {
        while (!this.stopThreads) {
            synchronized (this.cmdQueue) {
                try {
                    if (this.cmdQueue.isEmpty() && !this.stopThreads) {
                        this.cmdQueue.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!this.stopThreads) {
                ((UpdaterCommand) this.cmdQueue.poll()).executeInternal(this.context);
            } else {
                return;
            }
        }
        if (this.delegate != null) {
            this.delegate.onFinished();
        }
    }

    public void setDroneFirmwareVersion(String str) {
        this.fimrmwareVersion = str.trim();
    }

    public void start() {
        if (isInProgress()) {
            Log.w(TAG, "Updater already in progress. Start skipped");
            return;
        }
        this.workerThread = new Thread(this, "Updater Worker Thread");
        this.workerThread.start();
        startCommand((UpdaterCommand) this.commands.get(UpdaterCommandId.CONNECT));
    }

    public void startCommand(UpdaterCommand updaterCommand) {
        synchronized (this.cmdQueue) {
            this.cmdQueue.add(updaterCommand);
            this.cmdQueue.notify();
        }
    }

    public void stop() {
        this.stopThreads = true;
        try {
            synchronized (this.cmdQueue) {
                this.cmdQueue.notify();
            }
            cancelAnyFtpOperation();
            if (this.workerThread != null) {
                this.workerThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
