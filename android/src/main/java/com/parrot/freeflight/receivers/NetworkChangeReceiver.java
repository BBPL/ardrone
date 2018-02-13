package com.parrot.freeflight.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.parrot.freeflight.settings.ApplicationSettings;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkChangeReceiver.class.getSimpleName();
    private NetworkChangeReceiverDelegate delegate;
    private boolean edge3gDataSyncEnabled;
    private boolean isAutoUploadAllowed;
    private NetworkInfo networkInfo;
    private Network networkType;
    private boolean preAutoUploadPermission;

    public enum Network {
        NONE,
        NOT_FREE,
        FREE
    }

    public NetworkChangeReceiver(NetworkChangeReceiverDelegate networkChangeReceiverDelegate) {
        this.delegate = networkChangeReceiverDelegate;
    }

    private void checkAutoUploadPermissionChanged() {
        if (this.isAutoUploadAllowed != this.preAutoUploadPermission) {
            this.delegate.onAutoUploadPermissionChanged();
        }
        this.preAutoUploadPermission = this.isAutoUploadAllowed;
    }

    public static IntentFilter createLocalFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ApplicationSettings.EDGE_3G_DATA_SYNC_CHANGED);
        return intentFilter;
    }

    public static IntentFilter createSystemFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        return intentFilter;
    }

    private void updateIsAutoUploadAllowed() {
        if (this.edge3gDataSyncEnabled || this.networkType == Network.FREE) {
            this.isAutoUploadAllowed = true;
        } else {
            this.isAutoUploadAllowed = false;
        }
    }

    private void updateNetworkType() {
        this.networkType = Network.NONE;
        if (this.networkInfo == null || !this.networkInfo.isConnectedOrConnecting()) {
            this.networkType = Network.NONE;
            return;
        }
        switch (this.networkInfo.getType()) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                this.networkType = Network.NOT_FREE;
                return;
            case 1:
            case 7:
            case 8:
            case 9:
                this.networkType = Network.FREE;
                return;
            default:
                Log.e(TAG, "Connection type : " + this.networkInfo.getType() + " not known");
                this.networkType = Network.NOT_FREE;
                return;
        }
    }

    public void init(NetworkInfo networkInfo, boolean z) {
        this.edge3gDataSyncEnabled = z;
        this.networkInfo = networkInfo;
        updateNetworkType();
        updateIsAutoUploadAllowed();
        this.preAutoUploadPermission = this.isAutoUploadAllowed;
    }

    public boolean isAutoUploadAllowed() {
        return this.isAutoUploadAllowed;
    }

    public void onReceive(Context context, Intent intent) {
        if (this.delegate != null) {
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                this.networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                this.delegate.onNetworkChanged(this.networkInfo);
                updateNetworkType();
            } else if (intent.getAction().equals(ApplicationSettings.EDGE_3G_DATA_SYNC_CHANGED)) {
                this.edge3gDataSyncEnabled = intent.getBooleanExtra(ApplicationSettings.EXTRA_3G_DATA_SYNC_STATE, false);
            }
            updateIsAutoUploadAllowed();
            checkAutoUploadPermissionChanged();
        }
    }
}
