package com.parrot.freeflight.service;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.parrot.ardronetool.ARDroneEngine;
import com.parrot.ardronetool.AcademyDelegate;
import com.parrot.freeflight.FreeFlightApplication;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.receivers.NetworkChangeReceiver;
import com.parrot.freeflight.receivers.NetworkChangeReceiverDelegate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlightUploadService extends Service implements NetworkChangeReceiverDelegate, AcademyDelegate {
    public static final String ACTION_FLIGHT_UPLOAD_CHANGED = "com.parrot.flights.upload";
    public static final String EXTRA_FLIGHT_UPLOADING = "com.parrot.flights.uploading";
    private static final String TAG = FlightUploadService.class.getSimpleName();
    private static boolean isUploading;
    private final IBinder binder = new LocalBinder();
    private ARDroneEngine droneEngine;
    private ExecutorService executor;
    private NetworkChangeReceiver networkChangeReceiver;

    class C11821 extends AsyncTask<Void, Void, Void> {
        C11821() {
        }

        protected Void doInBackground(Void... voidArr) {
            FlightUploadService.this.droneEngine.academyDisconnect();
            return null;
        }
    }

    class C11832 extends AsyncTask<Void, Void, Void> {
        C11832() {
        }

        protected Void doInBackground(Void... voidArr) {
            FlightUploadService.this.droneEngine.academyConnect(AcademyUtils.login, AcademyUtils.password, FlightUploadService.this);
            return null;
        }
    }

    public class LocalBinder extends Binder {
        public FlightUploadService getService() {
            return FlightUploadService.this;
        }
    }

    public FlightUploadService() {
        Log.d(TAG, "FlightUploadService");
        this.networkChangeReceiver = new NetworkChangeReceiver(this);
        Log.d(TAG, "FlightUploadService fin");
    }

    public static boolean getUploadStatus() {
        return isUploading;
    }

    private void registerBroadcastListeners() {
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.networkChangeReceiver, NetworkChangeReceiver.createLocalFilter());
        registerReceiver(this.networkChangeReceiver, NetworkChangeReceiver.createSystemFilter());
    }

    private void sendUploadingUpdateBroadcast() {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getApplicationContext());
        Intent intent = new Intent(ACTION_FLIGHT_UPLOAD_CHANGED);
        intent.putExtra(EXTRA_FLIGHT_UPLOADING, isUploading);
        instance.sendBroadcast(intent);
    }

    private void unregisterBroadcastListeners() {
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.networkChangeReceiver);
        unregisterReceiver(this.networkChangeReceiver);
    }

    public void academyStateChanged(boolean z) {
        if (z != isUploading) {
            isUploading = z;
            sendUploadingUpdateBroadcast();
        }
    }

    public void onAutoUploadPermissionChanged() {
        Log.d(TAG, "onAutoUploadPermissionChanged");
        if (!AcademyUtils.isConnectedAcademy) {
            return;
        }
        AsyncTask c11832;
        if (this.networkChangeReceiver.isAutoUploadAllowed()) {
            Log.d(TAG, "academyConnect: start of flight sending");
            c11832 = new C11832();
            if (VERSION.SDK_INT < 11) {
                c11832.execute(new Void[0]);
                return;
            } else {
                c11832.executeOnExecutor(this.executor, new Void[0]);
                return;
            }
        }
        Log.i(TAG, "Service stopped because we are on mobile.");
        Log.d(TAG, "academyDisconnect: stop of flight sending");
        c11832 = new C11821();
        if (VERSION.SDK_INT < 11) {
            c11832.execute(new Void[0]);
        } else {
            c11832.executeOnExecutor(this.executor, new Void[0]);
        }
    }

    public IBinder onBind(Intent intent) {
        Log.d(TAG, "New service connection established.");
        return this.binder;
    }

    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        this.droneEngine = ARDroneEngine.instance(getApplicationContext());
        this.executor = Executors.newSingleThreadExecutor();
        registerBroadcastListeners();
        Log.d(TAG, "Service created");
    }

    public void onDestroy() {
        this.executor.shutdownNow();
        super.onDestroy();
        unregisterBroadcastListeners();
        Log.d(TAG, "Service destroyed");
    }

    public void onNetworkChanged(NetworkInfo networkInfo) {
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d(TAG, "Starting service");
        this.networkChangeReceiver.init(((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo(), ((FreeFlightApplication) getApplication()).getAppSettings().is3gEdgeDataSyncEnabled());
        return 1;
    }
}
