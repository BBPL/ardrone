package com.parrot.freeflight.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.parrot.ardronetool.ftp.FTPClient;
import com.parrot.freeflight.service.DroneConfig;
import com.parrot.freeflight.utils.CacheUtils;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class CheckDroneNetworkAvailabilityTask extends AsyncTask<Context, Integer, Boolean> {
    private static final String TAG = "CheckDroneNetworkAvailability";
    private FTPClient ftpClient = null;

    public void cancelAnyFtpOperation() {
        cancel(true);
        if (this.ftpClient != null) {
            this.ftpClient.abort();
        }
    }

    protected Boolean doInBackground(Context... contextArr) {
        Throwable th;
        Context context = contextArr[0];
        String droneHost = DroneConfig.getDroneHost();
        int ftpPort = DroneConfig.getFtpPort();
        try {
            if (!InetAddress.getByName(droneHost).isReachable(2000)) {
                return Boolean.FALSE;
            }
            if (isCancelled()) {
                return Boolean.FALSE;
            }
            File createTempFile;
            try {
                this.ftpClient = new FTPClient();
                Boolean bool;
                if (!this.ftpClient.connect(droneHost, ftpPort) || isCancelled()) {
                    Log.w(TAG, "downloadFile failed. Can't connect");
                    bool = Boolean.FALSE;
                    if (this.ftpClient == null || !this.ftpClient.isConnected()) {
                        return bool;
                    }
                    this.ftpClient.disconnect();
                    this.ftpClient = null;
                    return bool;
                }
                createTempFile = CacheUtils.createTempFile(context);
                if (createTempFile != null) {
                    try {
                        if (!isCancelled()) {
                            if (isCancelled() || !this.ftpClient.getSync("version.txt", createTempFile.getAbsolutePath())) {
                                bool = Boolean.FALSE;
                                if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                                    Log.w(TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                                }
                                if (this.ftpClient == null || !this.ftpClient.isConnected()) {
                                    return bool;
                                }
                                this.ftpClient.disconnect();
                                this.ftpClient = null;
                                return bool;
                            } else if (!createTempFile.exists() || isCancelled()) {
                                bool = Boolean.FALSE;
                                if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                                    Log.w(TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                                }
                                if (this.ftpClient == null || !this.ftpClient.isConnected()) {
                                    return bool;
                                }
                                this.ftpClient.disconnect();
                                this.ftpClient = null;
                                return bool;
                            } else {
                                String str;
                                if (isCancelled()) {
                                    str = null;
                                } else {
                                    StringBuffer readFromFile = CacheUtils.readFromFile(createTempFile);
                                    str = readFromFile != null ? readFromFile.toString() : null;
                                }
                                if (str != null) {
                                    bool = Boolean.TRUE;
                                    if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                                        Log.w(TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                                    }
                                    if (this.ftpClient == null || !this.ftpClient.isConnected()) {
                                        return bool;
                                    }
                                    this.ftpClient.disconnect();
                                    this.ftpClient = null;
                                    return bool;
                                }
                                bool = Boolean.FALSE;
                                if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                                    Log.w(TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                                }
                                if (this.ftpClient == null || !this.ftpClient.isConnected()) {
                                    return bool;
                                }
                                this.ftpClient.disconnect();
                                this.ftpClient = null;
                                return bool;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        Log.w(TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                        this.ftpClient.disconnect();
                        this.ftpClient = null;
                        throw th;
                    }
                }
                Log.w(TAG, "downloadFile failed. Can't connect");
                bool = Boolean.FALSE;
                if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                    Log.w(TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                }
                if (this.ftpClient == null || !this.ftpClient.isConnected()) {
                    return bool;
                }
                this.ftpClient.disconnect();
                this.ftpClient = null;
                return bool;
            } catch (Throwable th3) {
                th = th3;
                createTempFile = null;
                if (!(createTempFile == null || !createTempFile.exists() || createTempFile.delete())) {
                    Log.w(TAG, "Can't delete temp file " + createTempFile.getAbsolutePath());
                }
                if (this.ftpClient != null && this.ftpClient.isConnected()) {
                    this.ftpClient.disconnect();
                    this.ftpClient = null;
                }
                throw th;
            }
        } catch (UnknownHostException e) {
            return Boolean.FALSE;
        } catch (IOException e2) {
            return Boolean.FALSE;
        }
    }
}
