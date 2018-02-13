package com.parrot.freeflight.service.commands;

import android.content.IntentFilter;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.parrot.ardronetool.ARDroneEngine;
import com.parrot.ardronetool.receivers.ARDroneEngineConnectedReceiver;
import com.parrot.ardronetool.receivers.ARDroneEngineConnectedReceiverDelegate;
import com.parrot.ardronetool.receivers.ARDroneEngineDisconnectedReceiver;
import com.parrot.ardronetool.receivers.ARDroneEngineDisconnectedReceiverDelegate;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.utils.DeviceNameEncoder;
import com.parrot.freeflight.utils.FileUtils;
import com.parrot.freeflight.utils.SystemUtils;
import java.io.File;

public class ConnectCommand extends DroneServiceCommand implements ARDroneEngineConnectedReceiverDelegate, ARDroneEngineDisconnectedReceiverDelegate {
    public static final int CONNECTED = 0;
    public static final int CONNECTION_FAILED = 2;
    public static final int DISCONNECTED = 1;
    private static final String TAG = ConnectCommand.class.getSimpleName();
    private LocalBroadcastManager bm;
    private ARDroneEngineConnectedReceiver connectedReceiver = new ARDroneEngineConnectedReceiver(this);
    private ARDroneEngineDisconnectedReceiver disconnectedReceiver;
    private ARDroneEngine droneEngine;
    private int result;

    public ConnectCommand(DroneControlService droneControlService) {
        super(droneControlService);
        this.droneEngine = ARDroneEngine.instance(droneControlService.getApplicationContext());
        this.bm = LocalBroadcastManager.getInstance(droneControlService.getApplicationContext());
        this.result = 1;
    }

    public void execute() {
        this.bm.registerReceiver(this.connectedReceiver, new IntentFilter(ARDroneEngine.ARDRONE_ENGINE_CONNECTED_ACTION));
        try {
            Log.d(TAG, "Connecting...");
            String format = String.format(".%s:%s", new Object[]{DeviceNameEncoder.encode(SystemUtils.getDeviceName()), Secure.getString(this.context.getContentResolver(), "android_id")});
            String name = getClass().getPackage().getName();
            Log.d(TAG, "AppName: " + name + ", UserID: " + format);
            File mediaFolder = FileUtils.getMediaFolder(this.context);
            if (mediaFolder != null) {
                mediaFolder.getAbsolutePath();
            }
            File externalCacheDir = this.context.getExternalCacheDir();
            File cacheDir = externalCacheDir == null ? this.context.getCacheDir() : externalCacheDir;
            if (mediaFolder == null) {
                Log.w(TAG, "Cache/Media dir is unavailable.");
                this.droneEngine.start(name.trim(), format.trim(), cacheDir.getAbsolutePath(), cacheDir.getAbsolutePath(), 0, this.context.getString(C0984R.string.url_server).trim());
                return;
            }
            StatFs statFs = new StatFs(cacheDir.getPath());
            this.droneEngine.start(name.trim(), format.trim(), cacheDir.getAbsolutePath(), mediaFolder.getAbsolutePath(), (int) Math.round((((double) (((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks()))) * 0.1d) / 1048576.0d), this.context.getString(C0984R.string.url_server).trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getResult() {
        return this.result;
    }

    protected void onCommandFinished(int i) {
        this.bm.unregisterReceiver(this.connectedReceiver);
        this.bm.unregisterReceiver(this.disconnectedReceiver);
        this.result = i;
        this.context.onCommandFinished(this);
    }

    public void onToolConnected() {
        onCommandFinished(0);
    }

    public void onToolDisconnected() {
        onCommandFinished(1);
    }
}
