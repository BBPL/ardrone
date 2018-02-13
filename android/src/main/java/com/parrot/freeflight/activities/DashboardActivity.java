package com.parrot.freeflight.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.RatingReminder;
import com.parrot.freeflight.academy.activities.AcademyLoginActivity;
import com.parrot.freeflight.activities.base.DashboardActivityBase;
import com.parrot.freeflight.activities.base.DashboardActivityBase.EPhotoVideoState;
import com.parrot.freeflight.mediauploadservice.MediaShareService;
import com.parrot.freeflight.receivers.DroneAvailabilityDelegate;
import com.parrot.freeflight.receivers.DroneAvailabilityReceiver;
import com.parrot.freeflight.receivers.DroneConnectionChangeReceiverDelegate;
import com.parrot.freeflight.receivers.DroneConnectionChangedReceiver;
import com.parrot.freeflight.receivers.DroneFirmwareCheckReceiver;
import com.parrot.freeflight.receivers.DroneFirmwareCheckReceiverDelegate;
import com.parrot.freeflight.receivers.MediaReadyDelegate;
import com.parrot.freeflight.receivers.MediaReadyReceiver;
import com.parrot.freeflight.receivers.NetworkChangeReceiver;
import com.parrot.freeflight.receivers.NetworkChangeReceiverDelegate;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.service.DroneControlService.LocalBinder;
import com.parrot.freeflight.service.FlightUploadService;
import com.parrot.freeflight.settings.AccountChecker;
import com.parrot.freeflight.tasks.CheckAcademyAvailabilityTask;
import com.parrot.freeflight.tasks.CheckDroneNetworkAvailabilityTask;
import com.parrot.freeflight.tasks.CheckFirmwareTask;
import com.parrot.freeflight.tasks.CheckMediaAvailabilityTask;
import com.parrot.freeflight.updater.FirmwareUpdateService;
import com.parrot.freeflight.utils.InternetUtils;
import java.io.File;

public class DashboardActivity extends DashboardActivityBase implements DroneAvailabilityDelegate, NetworkChangeReceiverDelegate, DroneFirmwareCheckReceiverDelegate, DroneConnectionChangeReceiverDelegate, MediaReadyDelegate {
    private boolean academyAvailable;
    private CheckAcademyAvailabilityTask checkAcademyAvailabilityTask;
    private CheckDroneNetworkAvailabilityTask checkDroneConnectionTask;
    private CheckFirmwareTask checkFirmwareTask;
    private CheckMediaAvailabilityTask checkMediaTask;
    private BroadcastReceiver droneConnectionChangeReceiver;
    private DroneControlServiceConnection droneControlServiceConnection;
    private BroadcastReceiver droneFirmwareCheckReceiver;
    private boolean droneOnNetwork;
    private BroadcastReceiver droneStateReceiver;
    private boolean firmwareUpdateAvailable;
    private DroneControlService mService;
    private BroadcastReceiver mediaReadyReceiver;
    private EPhotoVideoState mediaState;
    private NetworkChangeReceiver networkChangeReceiver;
    private RatingReminder ratingReminder;
    private boolean servicesAreRunning;

    class C10652 extends CheckDroneNetworkAvailabilityTask {
        C10652() {
        }

        protected void onPostExecute(Boolean bool) {
            DashboardActivity.this.onDroneAvailabilityChanged(bool.booleanValue());
            DashboardActivity.this.handleServices();
        }
    }

    class C10674 extends CheckAcademyAvailabilityTask {
        C10674() {
        }

        protected void onPostExecute(Boolean bool) {
            DashboardActivity.this.academyAvailable = bool.booleanValue();
            if (DashboardActivity.this.academyAvailable) {
                DataTracker.trackInfoInt(TRACK_KEY_ENUM.TRACK_KEY_EVENT__INTERNET_CONNECTED, 1);
            } else {
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__INTERNET_DISCONNECTED);
            }
            DashboardActivity.this.requestUpdateButtonsState();
        }
    }

    class C10685 implements Runnable {
        C10685() {
        }

        public void run() {
            DashboardActivity.this.onUSBStickRemoveDialogDismissed();
        }
    }

    private class DroneControlServiceConnection implements ServiceConnection {
        private DroneControlServiceConnection() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            DashboardActivity.this.mService = ((LocalBinder) iBinder).getService();
            DataTracker.trackInfoInt(TRACK_KEY_ENUM.TRACK_KEY_EVENT__USER_ACCEPTED, DashboardActivity.this.settings.isTrackingEnabled() ? 1 : 0);
            if (DashboardActivity.this.mService.getMediaDir() == null) {
                DashboardActivity.this.mediaState = EPhotoVideoState.NO_SDCARD;
                DashboardActivity.this.requestUpdateButtonsState();
            }
            DashboardActivity.this.checkMedia();
        }

        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    @SuppressLint({"NewApi"})
    private void checkAcademyAvailability() {
        if (!taskRunning(this.checkAcademyAvailabilityTask)) {
            this.checkAcademyAvailabilityTask = new C10674();
            if (VERSION.SDK_INT >= 11) {
                this.checkAcademyAvailabilityTask.executeOnExecutor(CheckAcademyAvailabilityTask.THREAD_POOL_EXECUTOR, new Context[]{this});
                return;
            }
            this.checkAcademyAvailabilityTask.execute(new Context[]{this});
        }
    }

    @SuppressLint({"NewApi"})
    private void checkDroneConnectivity() {
        if (!(this.checkDroneConnectionTask == null || this.checkDroneConnectionTask.getStatus() == Status.FINISHED)) {
            this.checkDroneConnectionTask.cancel(true);
        }
        this.checkDroneConnectionTask = new C10652();
        if (VERSION.SDK_INT >= 11) {
            this.checkDroneConnectionTask.executeOnExecutor(CheckDroneNetworkAvailabilityTask.THREAD_POOL_EXECUTOR, new Context[]{this});
            return;
        }
        this.checkDroneConnectionTask.execute(new Context[]{this});
    }

    private void checkFirmware() {
        if (!(this.checkFirmwareTask == null || this.checkFirmwareTask.getStatus() == Status.FINISHED)) {
            this.checkFirmwareTask.cancel(true);
        }
        this.checkFirmwareTask = new CheckFirmwareTask(this) {
            protected void onPostExecute(Boolean bool) {
                DashboardActivity.this.onFirmwareChecked(bool.booleanValue());
            }
        };
        this.checkFirmwareTask.execute(new Object[0]);
    }

    private void checkMedia() {
        if (this.mService != null) {
            String externalStorageState = Environment.getExternalStorageState();
            if (externalStorageState.equals("mounted") || externalStorageState.equals("mounted_ro")) {
                this.mediaState = EPhotoVideoState.NO_MEDIA;
                if (!taskRunning(this.checkMediaTask)) {
                    this.checkMediaTask = new CheckMediaAvailabilityTask(this) {
                        protected void onPostExecute(Boolean bool) {
                            if (bool.booleanValue()) {
                                DashboardActivity.this.mediaState = EPhotoVideoState.READY;
                            } else {
                                DashboardActivity.this.mediaState = EPhotoVideoState.NO_MEDIA;
                            }
                            DashboardActivity.this.requestUpdateButtonsState();
                        }
                    };
                    this.checkMediaTask.execute(new Void[0]);
                    return;
                }
                return;
            }
            this.mediaState = EPhotoVideoState.NO_SDCARD;
            requestUpdateButtonsState();
            return;
        }
        this.mediaState = EPhotoVideoState.NO_SDCARD;
        requestUpdateButtonsState();
    }

    private void disableAllButtons() {
        this.droneOnNetwork = false;
        this.firmwareUpdateAvailable = false;
        this.mediaState = EPhotoVideoState.NO_SDCARD;
        this.academyAvailable = false;
        requestUpdateButtonsState();
    }

    private void onNotifyAboutUSBStickRemove() {
        showAlertDialog(getString(C0984R.string.ff_ID000129), getString(C0984R.string.ff_ID000130), new C10685());
    }

    private void registerBroadcastReceivers() {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getApplicationContext());
        instance.registerReceiver(this.droneStateReceiver, DroneAvailabilityReceiver.createFilter());
        instance.registerReceiver(this.droneFirmwareCheckReceiver, DroneFirmwareCheckReceiver.createFilter());
        instance.registerReceiver(this.mediaReadyReceiver, MediaReadyReceiver.createFilter());
        instance.registerReceiver(this.droneConnectionChangeReceiver, DroneConnectionChangedReceiver.createFilter());
        registerReceiver(this.networkChangeReceiver, NetworkChangeReceiver.createSystemFilter());
    }

    private void stopTasks() {
        if (taskRunning(this.checkMediaTask)) {
            this.checkMediaTask.cancel(true);
            this.checkMediaTask = null;
        }
        if (taskRunning(this.checkAcademyAvailabilityTask)) {
            this.checkAcademyAvailabilityTask.cancel(true);
            this.checkAcademyAvailabilityTask = null;
        }
        if (taskRunning(this.checkFirmwareTask)) {
            this.checkFirmwareTask.cancel(true);
            this.checkFirmwareTask = null;
        }
        if (taskRunning(this.checkDroneConnectionTask)) {
            this.checkDroneConnectionTask.cancelAnyFtpOperation();
            this.checkDroneConnectionTask = null;
        }
    }

    private boolean taskRunning(AsyncTask<?, ?, ?> asyncTask) {
        return (asyncTask == null || asyncTask.getStatus() == Status.FINISHED) ? false : true;
    }

    private void unregisterReceivers() {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getApplicationContext());
        instance.unregisterReceiver(this.droneStateReceiver);
        instance.unregisterReceiver(this.droneFirmwareCheckReceiver);
        instance.unregisterReceiver(this.mediaReadyReceiver);
        instance.unregisterReceiver(this.droneConnectionChangeReceiver);
        unregisterReceiver(this.networkChangeReceiver);
    }

    protected EPhotoVideoState getPhotoVideoState() {
        return this.mediaState;
    }

    protected void handleServices() {
        if (AccountChecker.getGoogleAccountName(getAppSettings(), getApplicationContext()) == null || !InternetUtils.isConnected(this) || this.droneOnNetwork) {
            if (this.servicesAreRunning) {
                stopService(new Intent(this, MediaShareService.class));
                stopService(new Intent(this, FlightUploadService.class));
                this.servicesAreRunning = false;
                Log.i("DashboardActivity", "handleServices OFF");
                return;
            }
            Log.i("DashboardActivity", "handleServices Already OFF");
        } else if (this.servicesAreRunning) {
            Log.i("DashboardActivity", "handleServices Already ON");
        } else {
            Log.i("DashboardActivity", "handleServices ON");
            startService(new Intent(this, MediaShareService.class));
            startService(new Intent(this, FlightUploadService.class));
            this.servicesAreRunning = true;
        }
    }

    protected void initBroadcastReceivers() {
        this.droneStateReceiver = new DroneAvailabilityReceiver(this);
        this.networkChangeReceiver = new NetworkChangeReceiver(this);
        this.droneFirmwareCheckReceiver = new DroneFirmwareCheckReceiver(this);
        this.mediaReadyReceiver = new MediaReadyReceiver(this);
        this.droneConnectionChangeReceiver = new DroneConnectionChangedReceiver(this);
    }

    protected boolean isAcademyEnabled() {
        return this.academyAvailable;
    }

    protected boolean isFirmwareUpdateEnabled() {
        return this.firmwareUpdateAvailable;
    }

    protected boolean isFreeFlightEnabled() {
        return this.droneOnNetwork;
    }

    protected boolean isGuestSpaceEnabled() {
        return true;
    }

    public void onAutoUploadPermissionChanged() {
        Log.d("DashboardActivity", "onAutoUploadPermissionChanged");
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!isFinishing()) {
            this.servicesAreRunning = false;
            this.mediaState = EPhotoVideoState.UNKNOWN;
            initBroadcastReceivers();
            this.droneControlServiceConnection = new DroneControlServiceConnection();
            bindService(new Intent(this, DroneControlService.class), this.droneControlServiceConnection, 1);
            this.ratingReminder = new RatingReminder(this, this.settings);
            this.ratingReminder.appLaunched(true);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MediaShareService.class));
        stopService(new Intent(this, FlightUploadService.class));
        unbindService(this.droneControlServiceConnection);
    }

    public void onDroneAvailabilityChanged(boolean z) {
        this.droneOnNetwork = z;
        if (z) {
            Log.d("DashboardActivity", "AR.Drone connection [CONNECTED]");
            if (z) {
                this.academyAvailable = false;
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__INTERNET_DISCONNECTED);
            }
            checkFirmware();
        } else {
            Log.d("DashboardActivity", "AR.Drone connection [DISCONNECTED]");
        }
        requestUpdateButtonsState();
    }

    public void onDroneConnected() {
        if (this.mService != null) {
            this.mService.pause();
        }
    }

    public void onDroneDisconnected() {
    }

    public void onFirmwareChecked(boolean z) {
        this.firmwareUpdateAvailable = z;
        requestUpdateButtonsState();
    }

    public void onMediaReady(File file) {
        if (!getPhotoVideoState().equals(EPhotoVideoState.READY)) {
            checkMedia();
        }
    }

    public void onMediaStorageMounted() {
        checkMedia();
    }

    public void onMediaStorageUnmounted() {
        checkMedia();
    }

    public void onNetworkChanged(NetworkInfo networkInfo) {
        if (networkInfo == null) {
            Log.w("DashboardActivity", "onNetworkChanged received null");
            return;
        }
        Log.d("DashboardActivity", "Network state has changed. State is: " + (networkInfo.isConnected() ? "CONNECTED" : "DISCONNECTED") + ", Type: " + networkInfo.getTypeName());
        if (this.mService == null || !networkInfo.isConnected()) {
            this.firmwareUpdateAvailable = false;
            this.droneOnNetwork = false;
            requestUpdateButtonsState();
            return;
        }
        checkDroneConnectivity();
        checkAcademyAvailability();
    }

    protected void onPause() {
        super.onPause();
        unregisterReceivers();
        stopTasks();
    }

    protected void onResume() {
        super.onResume();
        registerBroadcastReceivers();
        disableAllButtons();
        if (this.mService != null) {
            checkMedia();
        }
        checkAcademyAvailability();
        checkDroneConnectivity();
    }

    protected void onStart() {
        super.onStart();
    }

    protected boolean onStartAcademy() {
        startActivity(new Intent(this, AcademyLoginActivity.class));
        return true;
    }

    protected boolean onStartFirmwareUpdate() {
        if (this.firmwareUpdateAvailable) {
            if (this.mService == null || !this.mService.isUSBInserted() || FirmwareUpdateService.isRunning()) {
                startActivity(new Intent(this, UpdateFirmwareActivity.class));
            } else {
                onNotifyAboutUSBStickRemove();
            }
        }
        return this.firmwareUpdateAvailable;
    }

    protected boolean onStartFreeflight() {
        if (!this.droneOnNetwork) {
            return false;
        }
        startActivity(new Intent(this, ConnectActivity.class));
        return true;
    }

    protected boolean onStartGuestSpace() {
        Intent intent = new Intent(this, GuestSpaceActivity.class);
        intent.putExtra("from", 1);
        startActivity(intent);
        return true;
    }

    protected boolean onStartPhotosVideos() {
        MediaActivity.start(this);
        return true;
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onUSBStickRemoveDialogDismissed() {
        startActivity(new Intent(this, UpdateFirmwareActivity.class));
    }
}
