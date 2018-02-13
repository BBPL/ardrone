package com.parrot.freeflight.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.InputDevice;
import android.view.InputDevice.MotionRange;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.parrot.ardronetool.ARDroneEngine.ErrorState;
import com.parrot.ardronetool.ARDroneVersion;
import com.parrot.ardronetool.Configuration.EventKey;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.academynavdata.CtrlState;
import com.parrot.ardronetool.academynavdata.FlyingCameraModeValue;
import com.parrot.ardronetool.academynavdata.FlyingState;
import com.parrot.ardronetool.academynavdata.MotorState;
import com.parrot.ardronetool.academynavdata.RescueMode;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.ardronetool.video.VideoStageEncodedRecorder;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.FreeFlightApplication;
import com.parrot.freeflight.academy.activities.AcademyFlightDetailsActivity;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.receivers.DroneAltitudeChangedReceiver;
import com.parrot.freeflight.receivers.DroneAltitudeChangedReceiverDelegate;
import com.parrot.freeflight.receivers.DroneAppRecordStoppedReceiver;
import com.parrot.freeflight.receivers.DroneAppRecordStoppedReceiverDelegate;
import com.parrot.freeflight.receivers.DroneAutorecordStartedReceiver;
import com.parrot.freeflight.receivers.DroneAutorecordStartedReceiverDelegate;
import com.parrot.freeflight.receivers.DroneBatteryChangedReceiver;
import com.parrot.freeflight.receivers.DroneBatteryChangedReceiverDelegate;
import com.parrot.freeflight.receivers.DroneCameraReadyActionReceiverDelegate;
import com.parrot.freeflight.receivers.DroneCameraReadyChangeReceiver;
import com.parrot.freeflight.receivers.DroneCtrlStateChangedDelegate;
import com.parrot.freeflight.receivers.DroneCtrlStateChangedReceiver;
import com.parrot.freeflight.receivers.DroneEmergencyChangeReceiver;
import com.parrot.freeflight.receivers.DroneEmergencyChangeReceiverDelegate;
import com.parrot.freeflight.receivers.DroneFlyingCameraChangedDelegate;
import com.parrot.freeflight.receivers.DroneFlyingCameraChangedReceiver;
import com.parrot.freeflight.receivers.DroneFlyingCameraModeChangedDelegate;
import com.parrot.freeflight.receivers.DroneFlyingCameraModeChangedReceiver;
import com.parrot.freeflight.receivers.DroneFlyingStateChangedDelegate;
import com.parrot.freeflight.receivers.DroneFlyingStateChangedReceiver;
import com.parrot.freeflight.receivers.DroneFlyingStateReceiver;
import com.parrot.freeflight.receivers.DroneFlyingStateReceiverDelegate;
import com.parrot.freeflight.receivers.DroneMotorsChangedDelegate;
import com.parrot.freeflight.receivers.DroneMotorsChangedReceiver;
import com.parrot.freeflight.receivers.DronePsiChangedReceiver;
import com.parrot.freeflight.receivers.DronePsiChangedReceiverDelegate;
import com.parrot.freeflight.receivers.DroneRecordReadyActionReceiverDelegate;
import com.parrot.freeflight.receivers.DroneRecordReadyChangeReceiver;
import com.parrot.freeflight.receivers.DroneSpeedChangedReceiver;
import com.parrot.freeflight.receivers.DroneSpeedChangedReceiverDelegate;
import com.parrot.freeflight.receivers.DroneTakeOffCancelledReceiver;
import com.parrot.freeflight.receivers.DroneTakeOffCancelledReceiverDelegate;
import com.parrot.freeflight.receivers.DroneUsbRecordStoppedReceiver;
import com.parrot.freeflight.receivers.DroneUsbRecordStoppedReceiverDelegate;
import com.parrot.freeflight.receivers.DroneVideoRecordBufferProgressReceiver;
import com.parrot.freeflight.receivers.DroneVideoRecordBufferReceiverDelegate;
import com.parrot.freeflight.receivers.DroneVideoRecordStateReceiverDelegate;
import com.parrot.freeflight.receivers.DroneVideoRecordingStateReceiver;
import com.parrot.freeflight.receivers.GpsFirmwareParamsChangedReceiver;
import com.parrot.freeflight.receivers.GpsFirmwareParamsChangedReceiverDelegate;
import com.parrot.freeflight.receivers.GpsParamsChangeReceiver;
import com.parrot.freeflight.receivers.GpsParamsChangeReceiverDelegate;
import com.parrot.freeflight.receivers.MobileSignalStrengthListener;
import com.parrot.freeflight.receivers.MobileSignalStrengthListenerDelegate;
import com.parrot.freeflight.receivers.NetworkChangeReceiver;
import com.parrot.freeflight.receivers.NetworkChangeReceiverDelegate;
import com.parrot.freeflight.receivers.WifiSignalStrengthChangedReceiver;
import com.parrot.freeflight.receivers.WifiSignalStrengthReceiverDelegate;
import com.parrot.freeflight.sensors.DeviceOrientationChangeDelegate;
import com.parrot.freeflight.sensors.DeviceOrientationManager;
import com.parrot.freeflight.sensors.DeviceOrientationManagerFactory;
import com.parrot.freeflight.service.DroneConfig;
import com.parrot.freeflight.service.DroneControlService;
import com.parrot.freeflight.service.DroneControlService.DroneFlipDirection;
import com.parrot.freeflight.service.DroneControlService.LocalBinder;
import com.parrot.freeflight.service.DroneVersion;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.settings.ApplicationSettings.ControlMode;
import com.parrot.freeflight.settings.ApplicationSettings.EAppSettingProperty;
import com.parrot.freeflight.tasks.CheckDroneNetworkAvailabilityTask;
import com.parrot.freeflight.transcodeservice.TranscodingService;
import com.parrot.freeflight.ui.HudViewController;
import com.parrot.freeflight.ui.SettingsDialogDelegate;
import com.parrot.freeflight.ui.controlhandlers.InputEventsHandler;
import com.parrot.freeflight.ui.controlhandlers.InputEventsHandlerFactory;
import com.parrot.freeflight.ui.controlhandlers.OnDroneFlipListener;
import com.parrot.freeflight.ui.controlhandlers.OnGazYawChangedListener;
import com.parrot.freeflight.ui.controlhandlers.OnRollPitchChangedListener;
import com.parrot.freeflight.ui.controlhandlers.OnUiActionsListener;
import com.parrot.freeflight.ui.hud.InputDeviceState;
import com.parrot.freeflight.ui.widgets.SlideRuleView2.ElementDataProvider;
import com.parrot.freeflight.utils.SystemUtils;
import com.parrot.freeflight.utils.ThumbnailUtils;
import java.io.File;
import org.mortbay.jetty.HttpVersions;

@SuppressLint({"NewApi"})
public class ControlDroneActivity extends ParrotActivity implements DeviceOrientationChangeDelegate, WifiSignalStrengthReceiverDelegate, DroneVideoRecordStateReceiverDelegate, DroneVideoRecordBufferReceiverDelegate, DroneEmergencyChangeReceiverDelegate, DroneBatteryChangedReceiverDelegate, DroneFlyingStateReceiverDelegate, DroneCameraReadyActionReceiverDelegate, DroneRecordReadyActionReceiverDelegate, DroneAutorecordStartedReceiverDelegate, DroneAppRecordStoppedReceiverDelegate, DroneTakeOffCancelledReceiverDelegate, DroneUsbRecordStoppedReceiverDelegate, DroneAltitudeChangedReceiverDelegate, DroneSpeedChangedReceiverDelegate, DronePsiChangedReceiverDelegate, SettingsDialogDelegate, MobileSignalStrengthListenerDelegate, OnDroneFlipListener, OnGazYawChangedListener, OnRollPitchChangedListener, OnUiActionsListener, WarningDialogDelegate, NetworkChangeReceiverDelegate, DroneMotorsChangedDelegate, DroneCtrlStateChangedDelegate, DroneFlyingStateChangedDelegate, DroneFlyingCameraChangedDelegate, DroneFlyingCameraModeChangedDelegate, GpsParamsChangeReceiverDelegate, GpsFirmwareParamsChangedReceiverDelegate {
    private static final float ACCELERO_TRESHOLD = 0.034906585f;
    private static final float ACCURACY_MAX = 10.0f;
    private static final float ANGLE_MAX = 30.0f;
    private static final boolean DISPLAY_RECORD_POPUP_DRONE2 = true;
    private static final float GPS_INVALID_COORDINATES = 500.0f;
    private static final int LOW_DISK_SPACE_BYTES_LEFT = 20971520;
    protected static final int MESSAGE_CHECK_RECORD_STATE = 1;
    private static final int PITCH = 1;
    public static final int REQUEST_CHECK_GOOGLE_PLAY_SERVICES = 1;
    private static final int ROLL = 2;
    private static final float SPEED_MAX = 3.0f;
    private static final float SPEED_RATIO = 0.1f;
    private static final String TAG = "ControlDroneActivity";
    private static final int WARNING_MESSAGE_DISMISS_TIME = 5000;
    private static boolean displayARDrone1StopRecordPopup = true;
    private static boolean displayARDrone2USBDownloadPopup = true;
    private boolean acceleroEnabled;
    private final ElementDataProvider altitudeDataProvider = new C10606();
    private boolean autorecordEnabled;
    private boolean autorecordIsSwitching;
    private boolean backToHomeActivated = false;
    private int batteryLevel;
    private int batterySoundId;
    private boolean cameraReady;
    private CheckDroneNetworkAvailabilityTask checkDroneConnectionTask;
    private boolean closeActivityAfterRecord;
    private boolean combinedYawEnabled;
    private ConnectivityManager connectivityManager;
    private boolean controlLinkAvailable;
    private WarningDialog currentDialog;
    private DeviceOrientationManager deviceOrientationManager;
    private final OnClickListener downgradeGpsFirmwareListener = new C10573();
    private BroadcastReceiver droneAltitudeReceiver;
    private BroadcastReceiver droneAppRecordStoppedReceiver;
    private BroadcastReceiver droneAutorecordStartedReceiver;
    private BroadcastReceiver droneBatteryReceiver;
    private BroadcastReceiver droneCameraReadyChangedReceiver;
    private DroneConfig droneConfig;
    private DroneControlService droneControlService;
    private BroadcastReceiver droneCtrlStateChangedReceiver;
    private BroadcastReceiver droneEmergencyReceiver;
    private BroadcastReceiver droneFlyingCameraChangedReceiver;
    private BroadcastReceiver droneFlyingCameraModeChangedReceiver;
    private BroadcastReceiver droneFlyingStateChangedReceiver;
    private BroadcastReceiver droneFlyingStateReceiver;
    private BroadcastReceiver droneMotorsChangedReceiver;
    private BroadcastReceiver dronePsiReceiver;
    private BroadcastReceiver droneRecordReadyChangeReceiver;
    private BroadcastReceiver droneSpeedReceiver;
    private BroadcastReceiver droneTakeOffCancelledReceiver;
    private BroadcastReceiver droneUsbRecordStoppedReceiver;
    private BroadcastReceiver droneVideoBufferReceiver;
    private int effectsStreamId;
    private boolean flying;
    private boolean flyingCameraActive;
    private int gpsEphemeris;
    private BroadcastReceiver gpsFirmwareParamsChangedReceiver;
    private boolean gpsFirmwareUpdateEnabled;
    private int gpsFirmwareUpdateProgress;
    private GpsFirmwareUpdateState gpsFirmwareUpdateState = GpsFirmwareUpdateState.NONE;
    private int gpsFirmwareUpdateStateInt;
    private boolean gpsIsPlugged;
    private final LocationListener gpsLocationListener = new C10628();
    private BroadcastReceiver gpsParamsChangedReceiver;
    private Handler handler = new C10595();
    private InputEventsHandler inputEventsHandler;
    private boolean isClosing;
    private boolean isGoogleTV;
    private InputDeviceState joypad;
    private boolean leftJoyPressed;
    private boolean listenMotorsChanges;
    private Location location;
    private ServiceConnection mConnection = new C10584();
    private boolean magnetoAvailable;
    private boolean magnetoEnabled;
    private MobileSignalStrengthListener mobileSignalListener;
    private boolean mobileStateReceiverRegistered;
    private boolean motorChangesRegistered;
    private BroadcastReceiver networkStateChangedReceiver;
    private int networkType;
    private boolean nvidiaShieldAcceleroButtonPressed;
    private boolean nvidiaShieldLeftJoyPressed;
    private boolean oldIsRecordOnUsbValue;
    private boolean oldUsbActiveValue;
    private int oldUsbRemainingTime;
    private boolean pauseVideoWhenOnSettings;
    private float pitchBase;
    private CtrlState prevCtrlState;
    private RescueMode prevRescueMode = RescueMode.None;
    private boolean recordIsStopping;
    private boolean recording;
    private boolean rightJoyPressed;
    private float rollBase;
    private boolean running;
    private int screenRotationIndex;
    private ApplicationSettings settings;
    private SettingsDialog settingsDialog;
    private final OnClickListener skipUpdateGpsFirmwareListener = new C10562();
    private SoundPool soundPool;
    private final ElementDataProvider speedDataProvider = new C10617();
    private final OnClickListener upgradeGpsFirmwareListener = new C10551();
    private boolean usbFirstChecked;
    private int usbRemainingTime;
    private BroadcastReceiver videoRecordingStateReceiver;
    private HudViewController view;
    private BroadcastReceiver wifiSignalReceiver;
    private boolean wifiStateReceiverRegistered;

    class C10551 implements OnClickListener {
        C10551() {
        }

        public void onClick(View view) {
            ControlDroneActivity.this.dismissCurrentDialog();
            ControlDroneActivity.this.gpsFirmwareUpdateProgress(0, 16);
            ControlDroneActivity.this.droneControlService.getConfiguration().addEvent(EventKey.FW_UPLOAD_TRIGGER, Integer.valueOf(1), null);
        }
    }

    class C10562 implements OnClickListener {
        C10562() {
        }

        public void onClick(View view) {
            ControlDroneActivity.this.dismissCurrentDialog();
            ControlDroneActivity.this.getAppSettings().setGpsFirmwareUpdateEnabled(false);
            ControlDroneActivity.this.showGpsFirmwareSkipDialog();
        }
    }

    class C10573 implements OnClickListener {
        C10573() {
        }

        public void onClick(View view) {
            ControlDroneActivity.this.dismissCurrentDialog();
            ControlDroneActivity.this.droneControlService.getConfiguration().addEvent(EventKey.FW_UPLOAD_TRIGGER, Integer.valueOf(2), null);
        }
    }

    class C10584 implements ServiceConnection {
        C10584() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ControlDroneActivity.this.droneControlService = ((LocalBinder) iBinder).getService();
            ControlDroneActivity.this.droneConfig = ControlDroneActivity.this.droneControlService.getDroneConfig();
            ControlDroneActivity.this.onDroneServiceConnected();
            DataTracker.trackInfoInt(TRACK_KEY_ENUM.TRACK_KEY_EVENT__FLIGHT_SESSION_STARTED, ControlDroneActivity.this.droneConfig.getFlyingTime().intValue());
        }

        public void onServiceDisconnected(ComponentName componentName) {
            int majorVersion = DroneVersion.getMajorVersion(ControlDroneActivity.this.droneControlService.getDroneVersion());
            if (majorVersion != -1) {
                DataTracker.trackInfoInt(TRACK_KEY_ENUM.TRACK_KEY_EVENT__FLIGHT_SESSION_ENDED, majorVersion);
            }
            ControlDroneActivity.this.droneConfig = null;
            ControlDroneActivity.this.droneControlService = null;
        }
    }

    class C10595 extends Handler {
        C10595() {
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    ControlDroneActivity.this.checkRecordState(message.arg1 > 0);
                    return;
                default:
                    return;
            }
        }
    }

    class C10606 implements ElementDataProvider {
        C10606() {
        }

        private String formatAltitude(float f) {
            if (f >= ControlDroneActivity.ACCURACY_MAX) {
                return String.format("%d.0", new Object[]{Integer.valueOf((int) f)});
            }
            return String.format("%.1f", new Object[]{Float.valueOf(f)});
        }

        private float getAltitudeMax() {
            return ControlDroneActivity.this.droneControlService == null ? 0.0f : (float) ControlDroneActivity.this.droneConfig.getAltitudeLimit();
        }

        public float getAbsValue(float f) {
            return (float) (Math.exp(Math.log((double) (getAltitudeMax() + 1.0f)) * ((double) f)) - 1.0d);
        }

        public String getCurrElementData(float f) {
            return formatAltitude(getAbsValue(f)) + " m";
        }

        public String getElementData(int i, int i2) {
            return formatAltitude((float) (Math.exp(((double) (1.0f - (((float) i) / ((float) (i2 - 1))))) * Math.log((double) (getAltitudeMax() + 1.0f))) - 1.0d));
        }
    }

    class C10617 implements ElementDataProvider {
        C10617() {
        }

        private float getEulerAngleMax() {
            return ControlDroneActivity.this.droneControlService == null ? 0.0f : ControlDroneActivity.this.droneConfig.getTilt();
        }

        public float getAbsValue(float f) {
            return (getEulerAngleMax() * 0.1f) * f;
        }

        public String getCurrElementData(float f) {
            return String.format("%.1f", new Object[]{Float.valueOf(getAbsValue(f))}) + " m/s";
        }

        public String getElementData(int i, int i2) {
            Object[] objArr = new Object[1];
            objArr[0] = Float.valueOf((1.0f - (((float) i) / (((float) i2) - 1.0f))) * (getEulerAngleMax() * 0.1f));
            return String.format("%.1f", objArr);
        }
    }

    class C10628 implements LocationListener {
        C10628() {
        }

        public void onLocationChanged(Location location) {
            if (location.hasAccuracy()) {
                ControlDroneActivity.this.location = location;
            }
        }

        public void onProviderDisabled(String str) {
            ControlDroneActivity.this.location = null;
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    class C10639 implements OnClickListener {
        C10639() {
        }

        public void onClick(View view) {
            ControlDroneActivity.this.dismissCurrentDialog();
        }
    }

    private enum GpsFirmwareUpdateState {
        NONE,
        ERASING,
        UPLOADING
    }

    private boolean activateBackToHome() {
        this.backToHomeActivated ^= 1;
        sendBackToHomeCommand(this.backToHomeActivated);
        return this.backToHomeActivated;
    }

    private void applyExternalConfig() {
        this.pauseVideoWhenOnSettings = getResources().getBoolean(C0984R.bool.settings_pause_video_when_opened);
    }

    private boolean applyKeyEvent(int i, KeyEvent keyEvent) {
        return this.inputEventsHandler.onKey(null, i, keyEvent);
    }

    private void applySettings(ApplicationSettings applicationSettings) {
        applySettings(applicationSettings, false);
    }

    private void applySettings(ApplicationSettings applicationSettings, boolean z) {
        this.magnetoEnabled = applicationSettings.isAbsoluteControlEnabled();
        this.autorecordEnabled = applicationSettings.isAutorecordEnabled();
        if (this.droneControlService != null) {
            this.droneControlService.setAutoRecordEnabled(this.autorecordEnabled);
        }
        if (this.magnetoEnabled && (ARDroneVersion.isArDrone1() || !this.deviceOrientationManager.isMagnetoAvailable() || SystemUtils.isNook())) {
            this.magnetoEnabled = false;
            applicationSettings.setAbsoluteControlEnabled(false);
        }
        if (this.droneControlService != null) {
            this.droneControlService.setMagnetoEnabled(this.magnetoEnabled);
        }
        boolean isGpsFirmwareUpdateEnabled = applicationSettings.isGpsFirmwareUpdateEnabled();
        if (isGpsFirmwareUpdateEnabled != this.gpsFirmwareUpdateEnabled) {
            this.gpsFirmwareUpdateEnabled = isGpsFirmwareUpdateEnabled;
            if (isGpsFirmwareUpdateEnabled) {
                checkGpsFirmwareUpdate();
            }
        }
        onConfigChanged();
        this.view.setInterfaceOpacity(this.isGoogleTV ? 0.0f : (float) applicationSettings.getInterfaceOpacity());
    }

    private boolean canGo() {
        return this.flying && this.view.getSlidingSpeedValue() > 0.0f && this.view.getSlidingAltitudeValue() > 0.0f;
    }

    private boolean canGoBack() {
        return (!this.flying && this.cameraReady) || !this.controlLinkAvailable;
    }

    @SuppressLint({"NewApi"})
    private void checkDroneConnectivity() {
        if (!(this.checkDroneConnectionTask == null || this.checkDroneConnectionTask.getStatus() == Status.FINISHED)) {
            this.checkDroneConnectionTask.cancel(true);
        }
        this.checkDroneConnectionTask = new CheckDroneNetworkAvailabilityTask() {
            protected void onPostExecute(Boolean bool) {
                if (bool == Boolean.TRUE) {
                    ControlDroneActivity.this.droneControlService.requestConfigUpdate();
                }
            }
        };
        if (VERSION.SDK_INT >= 11) {
            this.checkDroneConnectionTask.executeOnExecutor(CheckDroneNetworkAvailabilityTask.THREAD_POOL_EXECUTOR, new Context[]{this});
            return;
        }
        this.checkDroneConnectionTask.execute(new Context[]{this});
    }

    private void checkGpsFirmwareUpdate() {
        if (!this.flying && getAppSettings().isGpsFirmwareUpdateEnabled() && (this.gpsEphemeris & 4) == 0) {
            if (((this.gpsFirmwareUpdateStateInt & 2) != 0 ? 1 : null) == null) {
                return;
            }
            if (this.batteryLevel >= 50) {
                showFirmwareUpdateDialog(this.upgradeGpsFirmwareListener, this.skipUpdateGpsFirmwareListener);
            } else {
                showGpsFirmwareBatteryLowDialog();
            }
        }
    }

    private void checkRecordState(boolean z) {
        int i = 1;
        if (z != this.droneControlService.isRecordReady()) {
            if (!z) {
                this.recordIsStopping = true;
            }
            this.view.setRecordButtonEnabled(false);
            this.handler.removeMessages(1);
            Message message = new Message();
            message.what = 1;
            if (!z) {
                i = 0;
            }
            message.arg1 = i;
            this.handler.sendMessageDelayed(message, 1000);
        } else if (this.droneControlService.isRecordReady()) {
            this.view.setRecording(true);
            this.view.setRecordButtonEnabled(true);
        } else {
            this.view.setRecording(false);
            this.view.setRecordButtonEnabled(true);
        }
        this.recordIsStopping = false;
        this.autorecordIsSwitching = false;
        if (!this.droneControlService.isRecordReady() && ARDroneVersion.isArDrone1()) {
            startTranscoding();
        }
        updateBackButtonState();
    }

    private void closeHudIfNeeded() {
        if (this.closeActivityAfterRecord) {
            finish();
        }
    }

    private void deinitOrientationManager() {
        this.deviceOrientationManager.destroy();
    }

    private void deinitServices() {
        unbindService(this.mConnection);
    }

    private void deinitSounds() {
        this.soundPool.release();
        this.soundPool = null;
    }

    private void deinitViewController() {
        if (this.view != null) {
            this.view.onDestroy();
        }
    }

    private void dismissCurrentDialog() {
        if (this.currentDialog != null) {
            this.currentDialog.dismiss(false);
            this.currentDialog = null;
        }
    }

    private InputDeviceState getInputDeviceState(InputEvent inputEvent) {
        InputDevice device = inputEvent.getDevice();
        if (device != null) {
            if (this.joypad == null) {
                this.joypad = new InputDeviceState(device);
            }
            if (this.joypad.getDevice() == device) {
                return this.joypad;
            }
        }
        return null;
    }

    private ApplicationSettings getSettings() {
        return ((FreeFlightApplication) getApplication()).getAppSettings();
    }

    private void gpsFirmwareUpdateCompleted(boolean z) {
        dismissCurrentDialog();
        showGpsFirmwareCompleteDialog(z);
    }

    private void gpsFirmwareUpdateProgress(int i, int i2) {
        boolean z = false;
        showGpsFirmwareUpdateProgressDlg();
        boolean z2 = (i2 & 16) != 0;
        if ((i2 & 32) != 0) {
            z = true;
        }
        GpsFirmwareUpdateState gpsFirmwareUpdateState = z2 ? GpsFirmwareUpdateState.ERASING : z ? GpsFirmwareUpdateState.UPLOADING : GpsFirmwareUpdateState.NONE;
        if (gpsFirmwareUpdateState != this.gpsFirmwareUpdateState) {
            this.gpsFirmwareUpdateState = gpsFirmwareUpdateState;
            if (gpsFirmwareUpdateState == GpsFirmwareUpdateState.ERASING) {
                setWarningDialogMessage(getString(C0984R.string.ae_ID000143));
            } else if (gpsFirmwareUpdateState == GpsFirmwareUpdateState.UPLOADING) {
                setWarningDialogMessage(getString(C0984R.string.ae_ID000144));
            }
        }
        this.currentDialog.setProgress(true, i);
    }

    private void initBroadcastReceivers() {
        this.wifiSignalReceiver = new WifiSignalStrengthChangedReceiver(this);
        this.mobileSignalListener = new MobileSignalStrengthListener(this);
        this.videoRecordingStateReceiver = new DroneVideoRecordingStateReceiver(this);
        this.droneEmergencyReceiver = new DroneEmergencyChangeReceiver(this);
        this.droneBatteryReceiver = new DroneBatteryChangedReceiver(this);
        this.droneAltitudeReceiver = new DroneAltitudeChangedReceiver(this);
        this.droneSpeedReceiver = new DroneSpeedChangedReceiver(this);
        this.dronePsiReceiver = new DronePsiChangedReceiver(this);
        this.droneFlyingStateReceiver = new DroneFlyingStateReceiver(this);
        this.droneCameraReadyChangedReceiver = new DroneCameraReadyChangeReceiver(this);
        this.droneRecordReadyChangeReceiver = new DroneRecordReadyChangeReceiver(this);
        this.droneAutorecordStartedReceiver = new DroneAutorecordStartedReceiver(this);
        this.droneAppRecordStoppedReceiver = new DroneAppRecordStoppedReceiver(this);
        this.droneTakeOffCancelledReceiver = new DroneTakeOffCancelledReceiver(this);
        this.droneUsbRecordStoppedReceiver = new DroneUsbRecordStoppedReceiver(this);
        this.droneVideoBufferReceiver = new DroneVideoRecordBufferProgressReceiver(this);
        this.networkStateChangedReceiver = new NetworkChangeReceiver(this);
        this.droneFlyingStateChangedReceiver = new DroneFlyingStateChangedReceiver(this);
        this.droneFlyingCameraChangedReceiver = new DroneFlyingCameraChangedReceiver(this);
        this.droneFlyingCameraModeChangedReceiver = new DroneFlyingCameraModeChangedReceiver(this);
        this.droneMotorsChangedReceiver = new DroneMotorsChangedReceiver(this);
        this.droneCtrlStateChangedReceiver = new DroneCtrlStateChangedReceiver(this);
        this.gpsParamsChangedReceiver = new GpsParamsChangeReceiver(this);
        this.gpsFirmwareParamsChangedReceiver = new GpsFirmwareParamsChangedReceiver(this);
    }

    private void initDeviceOrientationManager() {
        this.deviceOrientationManager = DeviceOrientationManagerFactory.createDeviceOrienationManager(this, this);
        this.deviceOrientationManager.onCreate();
        if (!this.deviceOrientationManager.isAcceleroAvailable()) {
            this.settings.setControlMode(ControlMode.NORMAL_MODE);
        }
    }

    private void initInputEventsSource() {
        this.inputEventsHandler = InputEventsHandlerFactory.create(this, this.view, this.settings);
        this.screenRotationIndex = this.inputEventsHandler.getInputSourceOrientation();
        this.inputEventsHandler.setOnGazYawChangedListener(this);
        this.inputEventsHandler.setOnRollPitchChangedListener(this);
        this.inputEventsHandler.setOnFlipListener(this);
        this.inputEventsHandler.setOnUiActionsListener(this);
    }

    private void initServices() {
        this.connectivityManager = (ConnectivityManager) getSystemService("connectivity");
        bindService(new Intent(this, DroneControlService.class), this.mConnection, 1);
    }

    private void initSounds() {
        this.soundPool = new SoundPool(2, 3, 0);
        this.batterySoundId = this.soundPool.load(this, C0984R.raw.battery, 1);
    }

    private void initViewController() {
        this.view = new HudViewController(this, this.altitudeDataProvider, this.speedDataProvider, this.settings);
        this.view.setCameraButtonEnabled(false);
        this.view.setRecordButtonEnabled(false);
    }

    private boolean isLowOnDiskSpace() {
        if (VERSION.SDK_INT >= 9) {
            DroneConfig droneConfig = this.droneConfig;
            if (!(this.recording || droneConfig.isRecordOnUsb())) {
                File mediaDir = this.droneControlService.getMediaDir();
                long j = 0;
                if (mediaDir != null) {
                    j = mediaDir.getUsableSpace();
                }
                if (VERSION.SDK_INT >= 9 && r0 < 20971520) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isNvidiaShield() {
        return Build.MODEL.equals("SHIELD");
    }

    private void playEmergencySound() {
        if (this.effectsStreamId != 0) {
            this.soundPool.stop(this.effectsStreamId);
            this.effectsStreamId = 0;
        }
        this.effectsStreamId = this.soundPool.play(this.batterySoundId, 1.0f, 1.0f, 1, -1, 1.0f);
    }

    private void processJoystickInput(InputDevice inputDevice, MotionEvent motionEvent, int i) {
        Log.i("joystick", "process");
        float f = 0.0f;
        MotionRange motionRange = inputDevice.getMotionRange(0, motionEvent.getSource());
        if (motionRange != null) {
            f = InputDeviceState.ProcessAxis(motionRange, i >= 0 ? motionEvent.getHistoricalAxisValue(0, i) : motionEvent.getAxisValue(0));
        }
        float f2 = 0.0f;
        MotionRange motionRange2 = inputDevice.getMotionRange(1, motionEvent.getSource());
        if (motionRange2 != null) {
            f2 = InputDeviceState.ProcessAxis(motionRange2, i >= 0 ? motionEvent.getHistoricalAxisValue(1, i) : motionEvent.getAxisValue(1));
        }
        float f3 = 0.0f;
        MotionRange motionRange3 = inputDevice.getMotionRange(11, motionEvent.getSource());
        if (motionRange3 != null) {
            f3 = InputDeviceState.ProcessAxis(motionRange3, i >= 0 ? motionEvent.getHistoricalAxisValue(11, i) : motionEvent.getAxisValue(11));
        }
        float f4 = 0.0f;
        MotionRange motionRange4 = inputDevice.getMotionRange(14, motionEvent.getSource());
        if (motionRange4 != null) {
            f4 = InputDeviceState.ProcessAxis(motionRange4, i >= 0 ? motionEvent.getHistoricalAxisValue(14, i) : motionEvent.getAxisValue(14));
        }
        float f5 = 0.0f;
        MotionRange motionRange5 = inputDevice.getMotionRange(15, motionEvent.getSource());
        if (motionRange5 != null) {
            f5 = InputDeviceState.ProcessAxis(motionRange5, i >= 0 ? motionEvent.getHistoricalAxisValue(15, i) : motionEvent.getAxisValue(15));
        }
        float f6 = 0.0f;
        MotionRange motionRange6 = inputDevice.getMotionRange(16, motionEvent.getSource());
        if (motionRange6 != null) {
            f6 = InputDeviceState.ProcessAxis(motionRange6, i >= 0 ? motionEvent.getHistoricalAxisValue(16, i) : motionEvent.getAxisValue(16));
        }
        if (f6 == 1.0f) {
            onFlip(DroneFlipDirection.BACK);
        } else if (f6 == GroundOverlayOptions.NO_DIMENSION) {
            onFlip(DroneFlipDirection.FRONT);
        } else if (f5 == 1.0f) {
            onFlip(DroneFlipDirection.RIGHT);
        } else if (f5 == GroundOverlayOptions.NO_DIMENSION) {
            onFlip(DroneFlipDirection.LEFT);
        }
        if (!this.nvidiaShieldAcceleroButtonPressed) {
            if (this.settings.isLeftHanded()) {
                if (((double) Math.abs(f2)) > 0.3d) {
                    if (!this.nvidiaShieldLeftJoyPressed) {
                        this.nvidiaShieldLeftJoyPressed = true;
                    }
                    if (((double) Math.abs(f)) > 0.3d) {
                        onGazYawActivated();
                        onGazYawChanged(-f2, f);
                    } else {
                        onGazYawActivated();
                        onGazYawChanged(-f2, 0.0f);
                    }
                } else if (((double) Math.abs(f)) > 0.3d) {
                    if (!this.nvidiaShieldLeftJoyPressed) {
                        this.nvidiaShieldLeftJoyPressed = true;
                    }
                    onGazYawActivated();
                    onGazYawChanged(0.0f, f);
                } else {
                    if (this.nvidiaShieldLeftJoyPressed) {
                        this.nvidiaShieldLeftJoyPressed = false;
                    }
                    onGazYawDeactivated();
                    onGazYawChanged(0.0f, 0.0f);
                }
            } else if (((double) Math.abs(f2)) > 0.3d) {
                if (!this.nvidiaShieldLeftJoyPressed) {
                    this.nvidiaShieldLeftJoyPressed = true;
                }
                if (((double) Math.abs(f)) > 0.3d) {
                    onRollPitchActivated();
                    onRollPitchChanged(-f2, f);
                } else {
                    onRollPitchActivated();
                    onRollPitchChanged(-f2, 0.0f);
                }
            } else if (((double) Math.abs(f)) > 0.3d) {
                if (!this.nvidiaShieldLeftJoyPressed) {
                    this.nvidiaShieldLeftJoyPressed = true;
                }
                onRollPitchActivated();
                onRollPitchChanged(0.0f, f);
            } else {
                if (this.nvidiaShieldLeftJoyPressed) {
                    this.nvidiaShieldLeftJoyPressed = false;
                }
                onRollPitchDeactivated();
                onRollPitchChanged(0.0f, 0.0f);
            }
        }
        if (this.settings.isLeftHanded()) {
            if (((double) Math.abs(f4)) > 0.3d) {
                if (((double) Math.abs(f3)) > 0.3d) {
                    onRollPitchActivated();
                    onRollPitchChanged(-f4, f3);
                } else {
                    onRollPitchActivated();
                    onRollPitchChanged(-f4, 0.0f);
                }
            } else if (((double) Math.abs(f3)) > 0.3d) {
                onRollPitchActivated();
                onRollPitchChanged(0.0f, f3);
            } else {
                onRollPitchDeactivated();
                onRollPitchChanged(0.0f, 0.0f);
            }
        } else if (((double) Math.abs(f4)) > 0.3d) {
            if (((double) Math.abs(f3)) > 0.3d) {
                onGazYawActivated();
                onGazYawChanged(-f4, f3);
            } else {
                onGazYawActivated();
                onGazYawChanged(-f4, 0.0f);
            }
        } else if (((double) Math.abs(f3)) > 0.3d) {
            onGazYawActivated();
            onGazYawChanged(0.0f, f3);
        } else {
            onGazYawDeactivated();
            onGazYawChanged(0.0f, 0.0f);
        }
        f = 0.0f;
        motionRange = inputDevice.getMotionRange(17, motionEvent.getSource());
        if (motionRange != null) {
            f = InputDeviceState.ProcessAxis(motionRange, i >= 0 ? motionEvent.getHistoricalAxisValue(17, i) : motionEvent.getAxisValue(17));
        }
        f2 = 0.0f;
        motionRange2 = inputDevice.getMotionRange(18, motionEvent.getSource());
        if (motionRange2 != null) {
            f2 = InputDeviceState.ProcessAxis(motionRange2, i >= 0 ? motionEvent.getHistoricalAxisValue(18, i) : motionEvent.getAxisValue(18));
        }
        if (f == 1.0f) {
            onVideoRecordClicked();
        } else if (f2 == 1.0f) {
            onPhotoTakeClicked();
        }
    }

    private void record(boolean z) {
        boolean z2 = true;
        if (this.droneControlService != null) {
            boolean z3;
            boolean z4;
            boolean isRecordReady = this.droneControlService.isRecordReady();
            if (this.autorecordIsSwitching) {
                z3 = false;
                z4 = false;
            } else {
                z3 = this.droneControlService.record();
                z4 = z3;
            }
            if (z3) {
                if (isRecordReady) {
                    DroneConfig droneConfig = this.droneConfig;
                    if (ARDroneVersion.isArDrone1() && displayARDrone1StopRecordPopup) {
                        onNotifyVideoIsProcessing();
                        displayARDrone1StopRecordPopup = false;
                    } else if (ARDroneVersion.isArDrone2() && droneConfig.isRecordOnUsb() && this.usbRemainingTime > 0 && displayARDrone2USBDownloadPopup) {
                        onNotifyNewVideoIsAvailableOnUsb();
                        displayARDrone2USBDownloadPopup = false;
                    } else if (z) {
                        onNotifyRecordIsStopping();
                    }
                }
                if (z4) {
                    if (isRecordReady) {
                        z2 = false;
                    }
                    checkRecordState(z2);
                }
            }
        }
    }

    private void registerMotorChanges(LocalBroadcastManager localBroadcastManager) {
        if (this.listenMotorsChanges && !this.motorChangesRegistered) {
            localBroadcastManager.registerReceiver(this.droneMotorsChangedReceiver, DroneMotorsChangedReceiver.createFilter());
            this.motorChangesRegistered = true;
        }
    }

    private void registerReceivers() {
        registerSignalStrengthReceiver();
        registerReceiver(this.networkStateChangedReceiver, NetworkChangeReceiver.createSystemFilter());
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getApplicationContext());
        instance.registerReceiver(this.videoRecordingStateReceiver, new IntentFilter(DroneControlService.ACTION_VIDEO_RECORDING_STATE_CHANGED));
        instance.registerReceiver(this.droneEmergencyReceiver, new IntentFilter(DroneControlService.ACTION_DRONE_EMERGENCY_STATE_CHANGED));
        instance.registerReceiver(this.droneBatteryReceiver, new IntentFilter(DroneControlService.ACTION_DRONE_BATTERY_CHANGED));
        instance.registerReceiver(this.droneAltitudeReceiver, new IntentFilter(DroneControlService.ACTION_DRONE_ALTITUDE_CHANGED));
        instance.registerReceiver(this.droneSpeedReceiver, new IntentFilter(DroneControlService.ACTION_DRONE_SPEED_CHANGED));
        instance.registerReceiver(this.dronePsiReceiver, DronePsiChangedReceiver.createFilter());
        instance.registerReceiver(this.droneFlyingStateReceiver, new IntentFilter(DroneControlService.ACTION_DRONE_FLYING_STATE_CHANGED));
        instance.registerReceiver(this.droneCameraReadyChangedReceiver, new IntentFilter(DroneControlService.ACTION_CAMERA_READY_CHANGED));
        instance.registerReceiver(this.droneRecordReadyChangeReceiver, new IntentFilter(DroneControlService.ACTION_RECORD_READY_CHANGED));
        instance.registerReceiver(this.droneVideoBufferReceiver, new IntentFilter(DroneControlService.ACTION_VIDEO_RECORDING_BUFFER_CHANGED));
        instance.registerReceiver(this.droneAutorecordStartedReceiver, DroneAutorecordStartedReceiver.createFilter());
        instance.registerReceiver(this.droneAppRecordStoppedReceiver, DroneAppRecordStoppedReceiver.createFilter());
        instance.registerReceiver(this.droneTakeOffCancelledReceiver, DroneTakeOffCancelledReceiver.createFilter());
        instance.registerReceiver(this.droneFlyingStateChangedReceiver, DroneFlyingStateChangedReceiver.createFilter());
        instance.registerReceiver(this.droneFlyingCameraChangedReceiver, DroneFlyingCameraChangedReceiver.createFilter());
        instance.registerReceiver(this.droneFlyingCameraModeChangedReceiver, DroneFlyingCameraModeChangedReceiver.createFilter());
        instance.registerReceiver(this.droneCtrlStateChangedReceiver, DroneCtrlStateChangedReceiver.createFilter());
        instance.registerReceiver(this.gpsParamsChangedReceiver, GpsParamsChangeReceiver.createFilter());
        instance.registerReceiver(this.gpsFirmwareParamsChangedReceiver, GpsFirmwareParamsChangedReceiver.createFilter());
        registerMotorChanges(instance);
    }

    private void registerSignalStrengthReceiver() {
        NetworkInfo activeNetworkInfo = this.connectivityManager.getActiveNetworkInfo();
        this.mobileStateReceiverRegistered = false;
        this.wifiStateReceiverRegistered = false;
        if (activeNetworkInfo == null) {
            Log.w(TAG, "Can't get active network info");
        } else if (activeNetworkInfo.getType() == 1) {
            registerReceiver(this.wifiSignalReceiver, new IntentFilter("android.net.wifi.RSSI_CHANGED"));
            this.wifiStateReceiverRegistered = true;
        } else if (activeNetworkInfo.getType() == 0) {
            ((TelephonyManager) getSystemService("phone")).listen(this.mobileSignalListener, MobileSignalStrengthListener.createFlags());
            this.mobileStateReceiverRegistered = true;
        }
    }

    private void sendBackToHomeCommand(boolean z) {
        double d = AcademyFlightDetailsActivity.INVALID_COORDINATES;
        if (z) {
            double d2;
            if (this.location == null || this.location.getAccuracy() <= 0.0f || this.location.getAccuracy() > ACCURACY_MAX) {
                d2 = AcademyFlightDetailsActivity.INVALID_COORDINATES;
            } else {
                d = this.location.getLatitude();
                d2 = this.location.getLongitude();
            }
            this.droneConfig.setBackHomeCoordinates(d, d2);
        }
        this.droneConfig.setFlyingCameraEnabled(z);
    }

    private void setRescueMode(RescueMode rescueMode) {
        if (rescueMode == this.prevRescueMode) {
            rescueMode = RescueMode.None;
        }
        this.droneConfig.setRescueMode(rescueMode);
        this.view.setRescueMode(rescueMode);
        this.prevRescueMode = rescueMode;
    }

    private void setWarningDialogMessage(String str) {
        if (this.currentDialog != null) {
            this.currentDialog.setMessage(str);
        }
    }

    private void showFirmwareUpdateDialog(OnClickListener onClickListener, OnClickListener onClickListener2) {
        String str = null;
        if (onClickListener2 != null) {
            str = getString(C0984R.string.ae_ID000149);
        }
        showWarningDialog2(getString(C0984R.string.ae_ID000141), false, false, getString(C0984R.string.ae_ID000147), onClickListener, str, onClickListener2, getString(C0984R.string.ae_ID000148), new C10639());
    }

    private void showGpsFirmwareBatteryLowDialog() {
        if (this.currentDialog == null) {
            showWarningDialog(getString(C0984R.string.ae_ID000142), 0, false);
        }
    }

    private void showGpsFirmwareCompleteDialog(boolean z) {
        if (this.currentDialog == null) {
            showWarningDialog(getString(z ? C0984R.string.ae_ID000145 : C0984R.string.ae_ID000146), 0, false);
        }
    }

    private void showGpsFirmwareSkipDialog() {
        if (this.currentDialog == null) {
            showWarningDialog(getString(C0984R.string.ae_ID000151), WARNING_MESSAGE_DISMISS_TIME, false);
        }
    }

    private void showGpsFirmwareUpdateProgressDlg() {
        if (this.currentDialog == null) {
            showWarningDialog2(HttpVersions.HTTP_0_9, false, false, null, null, null, null, null, null);
        }
    }

    private void showWarningDialog(String str, int i, boolean z) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(str) == null) {
            beginTransaction.addToBackStack(null);
            WarningDialog warningDialog = new WarningDialog();
            warningDialog.setMessage(str);
            warningDialog.setDismissAfter(i);
            warningDialog.setBarSizes(this.view.getTopBarHeight(), this.view.getBottomBarHeight(), this.view.getTopBarWidth(), this.view.getEmergencyWidth());
            if (z) {
                warningDialog.setDelegate(this);
            }
            warningDialog.setCancelable(false);
            warningDialog.show(beginTransaction, str);
            this.currentDialog = warningDialog;
        }
    }

    private void showWarningDialog2(String str, boolean z, boolean z2, String str2, OnClickListener onClickListener, String str3, OnClickListener onClickListener2, String str4, OnClickListener onClickListener3) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(str) == null) {
            beginTransaction.addToBackStack(null);
            WarningDialog warningDialog = new WarningDialog(true, z2);
            warningDialog.setMessage(str);
            warningDialog.setBarSizes(this.view.getTopBarHeight(), this.view.getBottomBarHeight(), this.view.getTopBarWidth(), this.view.getEmergencyWidth());
            warningDialog.setCancelable(z);
            warningDialog.setButton1(str2, onClickListener);
            warningDialog.setButton2(str3, onClickListener2);
            warningDialog.setButton3(str4, onClickListener3);
            warningDialog.show(beginTransaction, str);
            this.currentDialog = warningDialog;
        }
    }

    private void startTranscoding() {
        if (ARDroneVersion.isArDrone1()) {
            File mediaDir = this.droneControlService.getMediaDir();
            if (mediaDir != null) {
                Intent intent = new Intent(this, TranscodingService.class);
                intent.putExtra(TranscodingService.EXTRA_MEDIA_PATH, mediaDir.toString());
                startService(intent);
                return;
            }
            Log.d(TAG, "Transcoding skipped SD card is missing.");
        }
    }

    private void stopEmergencySound() {
        this.soundPool.stop(this.effectsStreamId);
        this.effectsStreamId = 0;
    }

    private void takePhoto() {
        if (this.droneControlService == null) {
            Log.w(TAG, "Can't take photo. Not connected to drone control service.");
        } else if (this.droneControlService.isMediaStorageAvailable()) {
            this.view.setCameraButtonEnabled(false);
            this.droneControlService.takePhoto();
        } else {
            onNotifyNoMediaStorageAvailable();
        }
    }

    private void unregisterMotorChanges(LocalBroadcastManager localBroadcastManager) {
        if (this.motorChangesRegistered) {
            localBroadcastManager.unregisterReceiver(this.droneMotorsChangedReceiver);
            this.motorChangesRegistered = false;
        }
    }

    private void unregisterReceivers() {
        unregisterSignalStrengthReceiver();
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(getApplicationContext());
        instance.unregisterReceiver(this.videoRecordingStateReceiver);
        instance.unregisterReceiver(this.droneEmergencyReceiver);
        instance.unregisterReceiver(this.droneBatteryReceiver);
        instance.unregisterReceiver(this.droneAltitudeReceiver);
        instance.unregisterReceiver(this.droneSpeedReceiver);
        instance.unregisterReceiver(this.droneFlyingStateReceiver);
        instance.unregisterReceiver(this.droneCameraReadyChangedReceiver);
        instance.unregisterReceiver(this.droneRecordReadyChangeReceiver);
        instance.unregisterReceiver(this.droneAutorecordStartedReceiver);
        instance.unregisterReceiver(this.droneAppRecordStoppedReceiver);
        instance.unregisterReceiver(this.droneTakeOffCancelledReceiver);
        instance.unregisterReceiver(this.droneFlyingStateChangedReceiver);
        instance.unregisterReceiver(this.droneFlyingCameraChangedReceiver);
        instance.unregisterReceiver(this.droneFlyingCameraModeChangedReceiver);
        instance.unregisterReceiver(this.droneCtrlStateChangedReceiver);
        instance.unregisterReceiver(this.gpsParamsChangedReceiver);
        instance.unregisterReceiver(this.gpsFirmwareParamsChangedReceiver);
        unregisterMotorChanges(instance);
        unregisterReceiver(this.networkStateChangedReceiver);
    }

    private void unregisterSignalStrengthReceiver() {
        try {
            if (this.wifiStateReceiverRegistered) {
                unregisterReceiver(this.wifiSignalReceiver);
                this.wifiStateReceiverRegistered = false;
            }
        } catch (IllegalStateException e) {
            Log.w(TAG, "wifiSignalReceiver could not be unregistered.");
            e.printStackTrace();
        }
        if (this.mobileStateReceiverRegistered) {
            ((TelephonyManager) getSystemService("phone")).listen(this.mobileSignalListener, 0);
            this.mobileStateReceiverRegistered = false;
        }
    }

    private void updateBackButtonState() {
        if (canGoBack()) {
            this.view.setBackButtonVisible(true);
        } else {
            this.view.setBackButtonVisible(false);
        }
    }

    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if (isNvidiaShield()) {
            if ((motionEvent.getSource() & 16) == 0 || motionEvent.getAction() != 2) {
                onGazYawDeactivated();
                onRollPitchDeactivated();
                Log.d("RollPitchGasYaw", "deactivated");
                return super.dispatchGenericMotionEvent(motionEvent);
            }
            InputDeviceState inputDeviceState = getInputDeviceState(motionEvent);
            if (inputDeviceState == null) {
                onGazYawDeactivated();
                onRollPitchDeactivated();
                Log.d("RollPitchGasYaw", "deactivated");
                return super.dispatchGenericMotionEvent(motionEvent);
            } else if (inputDeviceState.onJoystickMotion(motionEvent)) {
                int historySize = motionEvent.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    processJoystickInput(inputDeviceState.getDevice(), motionEvent, i);
                }
                processJoystickInput(inputDeviceState.getDevice(), motionEvent, -1);
                return true;
            } else {
                onGazYawDeactivated();
                onRollPitchDeactivated();
                Log.d("RollPitchGasYaw", "deactivated");
            }
        }
        return super.dispatchGenericMotionEvent(motionEvent);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (isNvidiaShield()) {
            InputDeviceState inputDeviceState = getInputDeviceState(keyEvent);
            if (inputDeviceState != null) {
                switch (keyEvent.getAction()) {
                    case 0:
                        if (inputDeviceState.onKeyDown(keyEvent)) {
                            switch (keyEvent.getKeyCode()) {
                                case 4:
                                    onBackClicked();
                                    return true;
                                case ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL /*96*/:
                                    onTakeOffLandClicked();
                                    return true;
                                case 97:
                                    if (this.flying) {
                                        return true;
                                    }
                                    onBackPressed();
                                    return true;
                                case 99:
                                    return true;
                                case 100:
                                    onSettingsClicked();
                                    return true;
                                case 102:
                                    if (this.settings.isLeftHanded()) {
                                        onCameraSwitchClicked();
                                        return true;
                                    }
                                    this.nvidiaShieldAcceleroButtonPressed = true;
                                    onRollPitchActivated();
                                    return true;
                                case 103:
                                    if (this.settings.isLeftHanded()) {
                                        this.nvidiaShieldAcceleroButtonPressed = true;
                                        onRollPitchActivated();
                                        return true;
                                    }
                                    onCameraSwitchClicked();
                                    return true;
                                case 108:
                                    onTakeOffLandClicked();
                                    return true;
                                default:
                                    return true;
                            }
                        }
                        break;
                    case 1:
                        if (inputDeviceState.onKeyDown(keyEvent)) {
                            switch (keyEvent.getKeyCode()) {
                                case 102:
                                    if (!this.settings.isLeftHanded()) {
                                        this.nvidiaShieldAcceleroButtonPressed = false;
                                        onRollPitchDeactivated();
                                        break;
                                    }
                                    break;
                                case 103:
                                    if (this.settings.isLeftHanded()) {
                                        this.nvidiaShieldAcceleroButtonPressed = false;
                                        onRollPitchDeactivated();
                                        break;
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                }
            }
            onGazYawDeactivated();
            onRollPitchDeactivated();
            return super.dispatchKeyEvent(keyEvent);
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    public float getAltitudeRelValue(float f) {
        int i = 1;
        if (this.droneConfig != null) {
            i = this.droneConfig.getAltitudeLimit();
            if (i < 0) {
                i = 0;
            }
        }
        float log = (float) (Math.log((((double) f) / 1000.0d) + 1.0d) / Math.log((double) (i + 1)));
        return log > 1.0f ? 1.0f : log;
    }

    public float getSpeedRelValue(float f) {
        float tilt = (f / 1000.0f) / (this.droneConfig != null ? this.droneConfig.getTilt() * 0.1f : 1.0f);
        return tilt > 1.0f ? 1.0f : tilt;
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        switch (i) {
            case 1:
                this.view.recheckGooglePlayServices();
                return;
            default:
                return;
        }
    }

    public void onAutoUploadPermissionChanged() {
    }

    public void onBackClicked() {
        if (this.view.isRescueShown()) {
            setRescueMode(RescueMode.None);
            this.view.showRescueScreen(false);
        } else if (this.view.isMapShown()) {
            this.view.showMap(false);
        } else if (this.recording) {
            if (!this.recordIsStopping) {
                onVideoRecordClicked();
            }
            this.closeActivityAfterRecord = true;
            this.isClosing = true;
        } else {
            this.isClosing = true;
            this.currentDialog = null;
            finish();
        }
    }

    public void onBackPressed() {
        if (canGoBack()) {
            onBackClicked();
        }
    }

    public void onBackToHomeClicked() {
        if (!this.isClosing && this.droneControlService != null) {
            activateBackToHome();
        }
    }

    public void onCameraReadyChanged(boolean z) {
        this.view.setCameraButtonEnabled(z);
        this.cameraReady = z;
        updateBackButtonState();
    }

    public void onCameraSwitchClicked() {
        if (this.droneControlService != null) {
            this.droneControlService.switchCamera();
        }
    }

    protected void onConfigChanged() {
        switch (this.settings.getControlMode()) {
            case NORMAL_MODE:
                this.acceleroEnabled = false;
                break;
            case ACCELERO_MODE:
                this.acceleroEnabled = true;
                break;
            case ACE_MODE:
                this.acceleroEnabled = true;
                break;
        }
        this.inputEventsHandler.setSettings(this.settings);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!isFinishing()) {
            this.settings = getSettings();
            this.combinedYawEnabled = true;
            this.acceleroEnabled = false;
            this.running = false;
            this.closeActivityAfterRecord = false;
            this.isClosing = false;
            this.usbFirstChecked = true;
            this.nvidiaShieldAcceleroButtonPressed = false;
            initViewController();
            initServices();
            applyExternalConfig();
            initDeviceOrientationManager();
            initInputEventsSource();
            initBroadcastReceivers();
            initSounds();
            NetworkInfo activeNetworkInfo = this.connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                this.networkType = activeNetworkInfo.getType();
            }
            if (isNvidiaShield()) {
                this.settings.setInterfaceOpacity(0);
            }
            this.settings.setFirstLaunch(false);
            DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__TAKE_OFF_ZONE_OPEN);
        }
    }

    protected void onDestroy() {
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__TAKE_OFF_ZONE_CLOSE);
        super.onDestroy();
        deinitViewController();
        deinitOrientationManager();
        deinitSounds();
        deinitServices();
        Log.d(TAG, "ControlDroneActivity destroyed");
        System.gc();
    }

    public void onDeviceOrientationChanged(float[] fArr, float f, int i) {
        float f2;
        float f3;
        if (isNvidiaShield()) {
            if (this.nvidiaShieldAcceleroButtonPressed) {
                onRollPitchActivated();
                this.running = true;
            }
            if (this.droneControlService != null) {
                if (this.magnetoEnabled && this.magnetoAvailable) {
                    f2 = 57.29578f * f;
                    if (this.screenRotationIndex == 1) {
                        f2 += 90.0f;
                    }
                    this.droneControlService.setDeviceOrientation((int) f2, 0);
                } else {
                    this.droneControlService.setDeviceOrientation(0, 0);
                }
                if (this.running) {
                    f2 = fArr[1] - this.pitchBase;
                    f3 = fArr[2] - this.rollBase;
                    if (this.screenRotationIndex == 0) {
                        if ((this.acceleroEnabled && !this.nvidiaShieldLeftJoyPressed) || this.nvidiaShieldAcceleroButtonPressed) {
                            if (Math.abs(f2) > ACCELERO_TRESHOLD || Math.abs(f3) > ACCELERO_TRESHOLD) {
                                this.droneControlService.setPitch(f2 * GroundOverlayOptions.NO_DIMENSION);
                                this.droneControlService.setRoll(f3);
                                return;
                            }
                            return;
                        }
                        return;
                    } else if (this.screenRotationIndex == 1) {
                        if ((this.acceleroEnabled && !this.nvidiaShieldLeftJoyPressed) || this.nvidiaShieldAcceleroButtonPressed) {
                            if (Math.abs(f2) > ACCELERO_TRESHOLD || Math.abs(f3) > ACCELERO_TRESHOLD) {
                                this.droneControlService.setPitch(f3 * GroundOverlayOptions.NO_DIMENSION);
                                this.droneControlService.setRoll(f2 * GroundOverlayOptions.NO_DIMENSION);
                                return;
                            }
                            return;
                        }
                        return;
                    } else if (this.screenRotationIndex != 3) {
                        return;
                    } else {
                        if ((this.acceleroEnabled && !this.nvidiaShieldLeftJoyPressed) || this.nvidiaShieldAcceleroButtonPressed) {
                            if (Math.abs(f2) > ACCELERO_TRESHOLD || Math.abs(f3) > ACCELERO_TRESHOLD) {
                                this.droneControlService.setPitch(f3);
                                this.droneControlService.setRoll(f2);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                }
                this.pitchBase = fArr[1];
                this.rollBase = fArr[2];
                this.droneControlService.setPitch(0.0f);
                this.droneControlService.setRoll(0.0f);
            }
        } else if (this.droneControlService != null) {
            if (this.magnetoEnabled && this.magnetoAvailable) {
                f2 = 57.29578f * f;
                if (this.screenRotationIndex == 1) {
                    f2 += 90.0f;
                }
                this.droneControlService.setDeviceOrientation((int) f2, 0);
            } else {
                this.droneControlService.setDeviceOrientation(0, 0);
            }
            if (this.running) {
                f2 = fArr[1] - this.pitchBase;
                f3 = fArr[2] - this.rollBase;
                if (this.screenRotationIndex == 0) {
                    if (!this.acceleroEnabled) {
                        return;
                    }
                    if (Math.abs(f2) > ACCELERO_TRESHOLD || Math.abs(f3) > ACCELERO_TRESHOLD) {
                        this.droneControlService.setPitch(f2 * GroundOverlayOptions.NO_DIMENSION);
                        this.droneControlService.setRoll(f3);
                        return;
                    }
                    return;
                } else if (this.screenRotationIndex == 1) {
                    if (!this.acceleroEnabled) {
                        return;
                    }
                    if (Math.abs(f2) > ACCELERO_TRESHOLD || Math.abs(f3) > ACCELERO_TRESHOLD) {
                        this.droneControlService.setPitch(f3 * GroundOverlayOptions.NO_DIMENSION);
                        this.droneControlService.setRoll(f2 * GroundOverlayOptions.NO_DIMENSION);
                        return;
                    }
                    return;
                } else if (this.screenRotationIndex != 3 || !this.acceleroEnabled) {
                    return;
                } else {
                    if (Math.abs(f2) > ACCELERO_TRESHOLD || Math.abs(f3) > ACCELERO_TRESHOLD) {
                        this.droneControlService.setPitch(f3);
                        this.droneControlService.setRoll(f2);
                        return;
                    }
                    return;
                }
            }
            this.pitchBase = fArr[1];
            this.rollBase = fArr[2];
            this.droneControlService.setPitch(0.0f);
            this.droneControlService.setRoll(0.0f);
        }
    }

    public void onDialogDismissed() {
        VideoStageEncodedRecorder.instance().forceStop();
        this.currentDialog = null;
        closeHudIfNeeded();
    }

    public void onDialogTimeout() {
        closeHudIfNeeded();
        this.currentDialog = null;
    }

    public void onDismissed(SettingsDialog settingsDialog) {
        this.screenRotationIndex = this.inputEventsHandler.getInputSourceOrientation();
        if (this.pauseVideoWhenOnSettings) {
            this.view.onResume();
        }
        new AsyncTask<Integer, Integer, Boolean>() {
            protected Boolean doInBackground(Integer... numArr) {
                ControlDroneActivity.this.applySettings(ControlDroneActivity.this.getSettings(), true);
                return Boolean.TRUE;
            }

            protected void onPostExecute(Boolean bool) {
                ControlDroneActivity.this.view.setSettingsButtonEnabled(true);
                ControlDroneActivity.this.view.invalidate();
            }
        }.execute(new Integer[0]);
    }

    public void onDroneAltitudeChanged(int i) {
        if (i < 0) {
            i = 0;
        }
        this.view.setAltitudeValue(i);
        this.view.setSlidingAltitudeValue(getAltitudeRelValue((float) i));
    }

    public void onDroneAppRecordStopped() {
        checkRecordState(true);
    }

    public void onDroneAutorecordStarted() {
        checkRecordState(true);
    }

    public void onDroneBatteryChanged(int i) {
        this.batteryLevel = i;
        this.view.setBatteryValue(i);
    }

    public void onDroneCtrlStateChanged(CtrlState ctrlState) {
        if (this.prevCtrlState != ctrlState) {
            if (ctrlState == CtrlState.Rescue) {
                this.listenMotorsChanges = true;
                registerMotorChanges(LocalBroadcastManager.getInstance(getApplicationContext()));
            } else if (this.prevCtrlState == CtrlState.Rescue) {
                this.listenMotorsChanges = false;
                unregisterMotorChanges(LocalBroadcastManager.getInstance(getApplicationContext()));
                this.view.setRescueMode(RescueMode.None);
            }
            this.prevCtrlState = ctrlState;
        }
    }

    public void onDroneEmergencyChanged(ErrorState errorState) {
        boolean z = true;
        this.view.setEmergency(errorState);
        if (errorState == ErrorState.EMERGENCY_VBAT_LOW || errorState == ErrorState.ALERT_VBAT_LOW) {
            playEmergencySound();
        } else {
            stopEmergencySound();
        }
        this.controlLinkAvailable = errorState != ErrorState.NAVDATA_CONNECTION;
        this.view.setRecordButtonEnabled(this.controlLinkAvailable);
        this.view.setCameraButtonEnabled(this.controlLinkAvailable);
        this.view.setSwitchCameraButtonEnabled(this.controlLinkAvailable);
        this.view.setRescueButtonEnabled(this.controlLinkAvailable);
        updateBackButtonState();
        HudViewController hudViewController = this.view;
        if (ErrorState.isEmergency(errorState)) {
            z = false;
        }
        hudViewController.setEmergencyButtonEnabled(z);
    }

    public void onDroneFlyingCameraChanged(boolean z) {
        this.flyingCameraActive = z;
        this.view.setFlyingCameraActive(z);
    }

    public void onDroneFlyingCameraModeChanged(FlyingCameraModeValue flyingCameraModeValue) {
        this.view.setBackHomeBtnActive(flyingCameraModeValue == FlyingCameraModeValue.FLYING_CAMERA_GPS_MODE_BACKTOHOME);
    }

    public void onDroneFlyingStateChanged(FlyingState flyingState) {
        this.view.updateFlyingState(flyingState);
    }

    public void onDroneFlyingStateChanged(boolean z) {
        this.flying = z;
        this.view.setIsFlying(z);
        DataTracker.trackInfoVoid(z ? TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_HUD_TAKE_OFF : TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_HUD_LANDING);
        updateBackButtonState();
    }

    public void onDroneMotorsChanged(int i, MotorState motorState, int i2, MotorState motorState2, int i3, MotorState motorState3, int i4, MotorState motorState4) {
        if (this.prevRescueMode != RescueMode.None) {
            this.view.updateMotors(i, motorState, i2, motorState2, i3, motorState3, i4, motorState4);
        }
    }

    public void onDronePsiChanged(float f) {
        this.view.setDroneHeading(f / 1000.0f);
    }

    public void onDroneRecordReadyChanged(boolean z) {
    }

    public void onDroneRecordVideoStateChanged(boolean z, boolean z2, int i) {
        this.recording = z;
        if (this.oldUsbActiveValue != z2 || this.usbFirstChecked) {
            Log.i("USB", "INIT 1");
            this.view.setUsbIndicatorEnabled(z2);
            this.oldUsbActiveValue = z2;
        }
        if (this.oldUsbRemainingTime != i || this.usbFirstChecked) {
            Log.i("USB", "INIT 2");
            this.view.setUsbRemainingTime(i);
            this.view.setUsbIndicatorColor(this.droneConfig.isRecordOnUsb(), i);
            this.oldUsbRemainingTime = i;
        }
        if (this.oldIsRecordOnUsbValue != this.droneConfig.isRecordOnUsb() || this.usbFirstChecked) {
            Log.i("USB", "INIT 2");
            this.view.setUsbIndicatorColor(this.droneConfig.isRecordOnUsb(), i);
            this.oldIsRecordOnUsbValue = this.droneConfig.isRecordOnUsb();
        }
        this.usbFirstChecked = false;
        updateBackButtonState();
    }

    protected void onDroneServiceConnected() {
        if (this.droneControlService != null) {
            this.droneControlService.resume();
            this.droneControlService.requestDroneStatus();
        } else {
            Log.w(TAG, "DroneServiceConnected event ignored as DroneControlService is null");
        }
        this.settingsDialog = new SettingsDialog(this, this, this.droneControlService, this.magnetoAvailable);
        applySettings(this.settings);
        startTranscoding();
        if (this.droneControlService.getMediaDir() != null) {
            this.view.setRecordButtonEnabled(true);
            this.view.setCameraButtonEnabled(true);
        }
    }

    public void onDroneSpeedChanged(double d) {
        this.view.setSpeedValue(d);
        this.view.setSlidingSpeedValue(getSpeedRelValue((float) d));
    }

    public void onDroneTakeOffCancelled() {
    }

    public void onDroneUsbRecordStopped() {
        checkRecordState(false);
        onNotifyLowUsbSpace();
    }

    public void onDroneVideoRecordBufferChanged(boolean z, int i) {
        if (this.currentDialog != null && !isFinishing()) {
            this.currentDialog.setProgress(z, i);
        }
    }

    public void onEmergencyClicked() {
        if (this.droneControlService != null) {
            this.droneControlService.triggerEmergency();
        }
    }

    public boolean onFlip() {
        if (!this.settings.isFlipEnabled() || this.droneControlService == null) {
            return false;
        }
        this.droneControlService.doFlip(this.settings.getFlipDirection());
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__CLICK_HUD_LOOPING);
        return true;
    }

    public boolean onFlip(DroneFlipDirection droneFlipDirection) {
        if (!this.settings.isFlipEnabled() || this.droneControlService == null) {
            return false;
        }
        this.droneControlService.doFlip(droneFlipDirection);
        return true;
    }

    public void onGazYawActivated() {
        if (this.flyingCameraActive) {
            this.droneConfig.setFlyingCameraEnabled(false);
        }
        this.rightJoyPressed = true;
        if (this.droneControlService == null) {
            return;
        }
        if (this.combinedYawEnabled && this.leftJoyPressed) {
            this.droneControlService.setProgressiveCommandCombinedYawEnabled(true);
        } else {
            this.droneControlService.setProgressiveCommandCombinedYawEnabled(false);
        }
    }

    public void onGazYawChanged(float f, float f2) {
        if (this.droneControlService != null) {
            this.droneControlService.setGaz(f);
            this.droneControlService.setYaw(f2);
        }
    }

    public void onGazYawDeactivated() {
        this.rightJoyPressed = false;
        if (this.droneControlService != null && this.combinedYawEnabled) {
            this.droneControlService.setProgressiveCommandCombinedYawEnabled(false);
        }
    }

    public void onGpsClicked() {
        if (!this.isClosing && this.droneControlService != null) {
            this.view.showMap(!this.view.isMapShown());
        }
    }

    public void onGpsFirmwareParamsChanged(int i, int i2, int i3) {
        this.gpsEphemeris = i;
        this.gpsFirmwareUpdateStateInt = i2;
        this.gpsFirmwareUpdateProgress = i3;
        if ((i2 & 8) != 0 || (i2 & 16) != 0 || (i2 & 32) != 0) {
            gpsFirmwareUpdateProgress(this.gpsFirmwareUpdateProgress, i2);
        } else if ((i2 & 64) != 0 || (i2 & 128) != 0) {
            gpsFirmwareUpdateCompleted((i2 & 128) != 0);
        }
    }

    public void onGpsParamsChanged(boolean z, boolean z2, float f, int i, double d, double d2) {
        if (z != this.gpsIsPlugged) {
            this.gpsIsPlugged = z;
            if (z && this.currentDialog == null) {
                checkGpsFirmwareUpdate();
            }
        }
        this.view.setGpsParams(z, z2, f, false, d, d2);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return applyKeyEvent(i, keyEvent) ? true : super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return applyKeyEvent(i, keyEvent) ? true : super.onKeyUp(i, keyEvent);
    }

    public void onMapGoClicked() {
        if (this.flyingCameraActive) {
            this.droneConfig.setFlyingCameraEnabled(false);
        } else {
            sendWaypointPosition();
        }
    }

    public void onMapLeftPressed() {
        this.view.rotateDroneTargetLeft();
    }

    public void onMapLeftReleased() {
        if (this.flyingCameraActive) {
            sendWaypointPosition();
        }
    }

    public void onMapLeftRepeated() {
        this.view.rotateDroneTargetLeft();
    }

    public void onMapRightPressed() {
        this.view.rotateDroneTargetRight();
    }

    public void onMapRightReleased() {
        if (this.flyingCameraActive) {
            sendWaypointPosition();
        }
    }

    public void onMapRightRepeated() {
        this.view.rotateDroneTargetRight();
    }

    public void onMobileSignalStrengthChanged(int i) {
        int i2 = 4;
        int i3 = i <= 0 ? 0 : i;
        if (i3 < 4) {
            i2 = i3;
        }
        this.view.setWifiValue(i2);
    }

    public void onNetworkChanged(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.isConnected()) {
            if (!(this.networkType == networkInfo.getType() || isFinishing())) {
                this.networkType = networkInfo.getType();
                unregisterSignalStrengthReceiver();
                registerSignalStrengthReceiver();
            }
            if (this.droneControlService != null && networkInfo.isConnected()) {
                checkDroneConnectivity();
            }
        }
    }

    protected void onNotifyLowDiskSpace() {
        showWarningDialog(getString(C0984R.string.your_device_is_low_on_disk_space), WARNING_MESSAGE_DISMISS_TIME, false);
    }

    protected void onNotifyLowUsbSpace() {
        showWarningDialog(getString(C0984R.string.ae_ID000019), WARNING_MESSAGE_DISMISS_TIME, false);
    }

    protected void onNotifyNewVideoIsAvailableOnUsb() {
        showWarningDialog(getString(C0984R.string.ae_ID000118).replace("%s", Build.MODEL), 0, false);
    }

    protected void onNotifyNoMediaStorageAvailable() {
        showWarningDialog(getString(C0984R.string.Please_insert_a_SD_card_in_your_Smartphone), WARNING_MESSAGE_DISMISS_TIME, false);
    }

    protected void onNotifyRecordIsStopping() {
        showWarningDialog(getString(C0984R.string.ae_ID000022), WARNING_MESSAGE_DISMISS_TIME, true);
    }

    protected void onNotifyVideoIsProcessing() {
        showWarningDialog(getString(C0984R.string.ae_ID000021), WARNING_MESSAGE_DISMISS_TIME, false);
    }

    public void onOptionChangedApp(SettingsDialog settingsDialog, EAppSettingProperty eAppSettingProperty, Object obj) {
        if (obj == null || obj == null) {
            throw new IllegalArgumentException("Property can not be null");
        }
        switch (eAppSettingProperty) {
            case LEFT_HANDED_PROP:
            case MAGNETO_ENABLED_PROP:
            case CONTROL_MODE_PROP:
            case INTERFACE_OPACITY_PROP:
                onConfigChanged();
                return;
            default:
                return;
        }
    }

    public void onOverBalanceClicked() {
        if (!this.isClosing && this.droneControlService != null) {
            setRescueMode(RescueMode.Overbalance);
        }
    }

    protected void onPause() {
        ((LocationManager) getSystemService("location")).removeUpdates(this.gpsLocationListener);
        super.onPause();
        if (this.view != null) {
            this.view.onPause();
        }
        if (this.droneControlService != null) {
            this.droneControlService.pause();
        }
        unregisterReceivers();
        if (this.settingsDialog != null && this.settingsDialog.isVisible()) {
            this.settingsDialog.dismiss();
        }
        this.deviceOrientationManager.pause();
        stopEmergencySound();
        System.gc();
    }

    public void onPhotoTakeClicked() {
        if (!this.isClosing) {
            takePhoto();
        }
    }

    public void onRandomShakeClicked() {
        if (!this.isClosing && this.droneControlService != null) {
            setRescueMode(RescueMode.RandomShake);
        }
    }

    public void onRescueClicked() {
        if (!this.isClosing && this.droneControlService != null) {
            this.view.showRescueScreen(true);
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.view != null) {
            this.view.onResume();
        }
        if (this.droneControlService != null) {
            this.droneControlService.resume();
        }
        registerReceivers();
        refreshWifiSignalStrength();
        this.deviceOrientationManager.resume();
        this.magnetoAvailable = this.deviceOrientationManager.isMagnetoAvailable();
        LocationManager locationManager = (LocationManager) getSystemService("location");
        try {
            if (locationManager.isProviderEnabled("gps")) {
                locationManager.requestLocationUpdates("gps", 100, 0.1f, this.gpsLocationListener);
            }
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Looks like we do not have gps on the device");
        }
    }

    public void onRollPitchActivated() {
        if (this.flyingCameraActive) {
            this.droneConfig.setFlyingCameraEnabled(false);
        }
        this.leftJoyPressed = true;
        if (this.droneControlService != null) {
            this.droneControlService.setProgressiveCommandEnabled(true);
            if (this.combinedYawEnabled && this.rightJoyPressed) {
                this.droneControlService.setProgressiveCommandCombinedYawEnabled(true);
            } else {
                this.droneControlService.setProgressiveCommandCombinedYawEnabled(false);
            }
        }
        this.running = true;
    }

    public void onRollPitchChanged(float f, float f2) {
        if (this.droneControlService != null) {
            this.droneControlService.setRoll(f2);
            this.droneControlService.setPitch(-f);
        }
    }

    public void onRollPitchDeactivated() {
        this.leftJoyPressed = false;
        if (this.droneControlService != null) {
            this.droneControlService.setProgressiveCommandEnabled(false);
            if (this.combinedYawEnabled) {
                this.droneControlService.setProgressiveCommandCombinedYawEnabled(false);
            }
        }
        this.running = false;
    }

    public void onSettingsClicked() {
        showSettingsDialog();
    }

    public void onTakeOffLandClicked() {
        if (!this.isClosing && this.droneControlService != null) {
            if (!this.flying && this.autorecordEnabled) {
                this.autorecordIsSwitching = true;
                checkRecordState(true);
            }
            this.droneControlService.triggerTakeOff();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.view.onTouch(this.view.getRootView(), motionEvent);
    }

    public void onVideoRecordClicked() {
        if (!this.isClosing && !this.autorecordIsSwitching) {
            record(ARDroneVersion.isArDrone2());
        }
    }

    public void onWifiSignalStrengthChanged(int i) {
        this.view.setWifiValue(i);
    }

    public void prepareDialog(SettingsDialog settingsDialog) {
        settingsDialog.setAcceleroAvailable(this.deviceOrientationManager.isAcceleroAvailable());
        if (SystemUtils.isNook()) {
            settingsDialog.setMagnetoAvailable(false);
        } else {
            settingsDialog.setMagnetoAvailable(this.deviceOrientationManager.isMagnetoAvailable());
        }
        settingsDialog.setFlying(this.flying);
        settingsDialog.setConnected(this.controlLinkAvailable, true);
        settingsDialog.enableAvailableSettings();
    }

    public void refreshWifiSignalStrength() {
        onWifiSignalStrengthChanged(WifiManager.calculateSignalLevel(((WifiManager) getSystemService("wifi")).getConnectionInfo().getRssi(), 4));
    }

    public void sendWaypointPosition() {
        float f = 0.0f;
        if (canGo()) {
            double targetPositionLatitude = this.view.getTargetPositionLatitude();
            double targetPositionLongitude = this.view.getTargetPositionLongitude();
            if (targetPositionLatitude != 0.0d && targetPositionLongitude != 0.0d) {
                if (this.view.getAutoHeadingInitialValue() == GroundOverlayOptions.NO_DIMENSION) {
                    f = this.view.getDroneTargetHeading() == 0.0f ? 0.1f : this.view.getDroneTargetHeading();
                }
                int absValue = (int) (this.speedDataProvider.getAbsValue(this.view.getSlidingSpeedValue()) * 1000.0f);
                this.droneConfig.setFlyingCameraMode(targetPositionLatitude, targetPositionLongitude, (int) (this.altitudeDataProvider.getAbsValue(this.view.getSlidingAltitudeValue()) * 1000.0f), absValue, absValue, (int) ((75.0f * this.droneConfig.getVertSpeedMax()) * ACCURACY_MAX), f);
                this.droneConfig.setFlyingCameraEnabled(true);
            }
        }
    }

    protected void showSettingsDialog() {
        this.view.setSettingsButtonEnabled(false);
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag("settings") == null) {
            beginTransaction.addToBackStack(null);
            this.settingsDialog.show(beginTransaction, "settings");
            if (this.pauseVideoWhenOnSettings) {
                this.view.onPause();
            }
        }
    }
}
