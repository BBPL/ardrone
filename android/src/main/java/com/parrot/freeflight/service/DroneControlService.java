package com.parrot.freeflight.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.os.StatFs;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.parrot.ardronetool.ARDroneAnimation;
import com.parrot.ardronetool.ARDroneEngine;
import com.parrot.ardronetool.ARDroneEngine.DroneProgressiveCommandFlag;
import com.parrot.ardronetool.ARDroneEngine.ErrorState;
import com.parrot.ardronetool.ARDroneVersion;
import com.parrot.ardronetool.AcademyMediaListener;
import com.parrot.ardronetool.ArdroneVideoChannel;
import com.parrot.ardronetool.ConfigArdroneMask;
import com.parrot.ardronetool.Configuration;
import com.parrot.ardronetool.Configuration.EventKey;
import com.parrot.ardronetool.Configuration.Source;
import com.parrot.ardronetool.ConfigurationEventDelegate;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.NavData;
import com.parrot.ardronetool.NavDataPwm;
import com.parrot.ardronetool.academynavdata.ARDroneAcademyNavdata;
import com.parrot.ardronetool.academynavdata.ARDroneAcademyNavdata.RecordCmd;
import com.parrot.ardronetool.academynavdata.FlyingState;
import com.parrot.ardronetool.tracking.DEVICE_TYPE_ENUM;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.ardronetool.ui.ARDroneInput;
import com.parrot.ardronetool.ui.ARDroneInput.ProgressiveFlag;
import com.parrot.ardronetool.utils.ARDroneVideoEncapsuler;
import com.parrot.ardronetool.video.UsbVideoDownloader;
import com.parrot.ardronetool.video.UsbVideoDownloader.Status;
import com.parrot.ardronetool.video.UsbVideoDownloader.UsbVideoDownloadProgressListener;
import com.parrot.ardronetool.video.UsbVideoDownloader.VideoItem;
import com.parrot.ardronetool.video.VideoStage;
import com.parrot.ardronetool.video.VideoStageEncodedRecorder;
import com.parrot.freeflight.media.Exif2Interface;
import com.parrot.freeflight.media.Exif2Interface.Tag;
import com.parrot.freeflight.service.commands.DroneServiceCommand;
import com.parrot.freeflight.service.intents.DroneStateManager;
import com.parrot.freeflight.service.listeners.DroneDebugListener;
import com.parrot.freeflight.service.states.ConnectedServiceState;
import com.parrot.freeflight.service.states.DisconnectedServiceState;
import com.parrot.freeflight.service.states.PausedServiceState;
import com.parrot.freeflight.settings.ApplicationSettings;
import com.parrot.freeflight.tasks.MoveFileTask;
import com.parrot.freeflight.utils.ARDroneMediaGallery;
import com.parrot.freeflight.utils.FTPUtils;
import com.parrot.freeflight.utils.FileUtils;
import com.parrot.freeflight.utils.GPSHelper;
import com.parrot.freeflight.utils.SystemUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.util.URIUtil;

public class DroneControlService extends Service implements Runnable, AcademyMediaListener, LocationListener, UsbVideoDownloadProgressListener {
    public static final String ACTION_APP_RECORD_STOPPED = "com.parrot.app.record.stopped";
    public static final String ACTION_CAMERA_READY_CHANGED = "com.parrot.camera.ready.changed";
    public static final String ACTION_DRONE_ALTITUDE_CHANGED = "com.parrot.altitude.changed";
    public static final String ACTION_DRONE_AUTORECORD_STARTED = "com.parrot.drone.autorecord.started";
    public static final String ACTION_DRONE_BATTERY_CHANGED = "com.parrot.battery.changed";
    public static final String ACTION_DRONE_CONFIG_STATE_CHANGED = "com.parrot.config.changed";
    public static final String ACTION_DRONE_CONNECTION_CHANGED = "com.parrot.drone.connection.changed";
    public static final String ACTION_DRONE_CTRLSTATE_CHANGED = "com.parrot.ctrlstate.changed";
    public static final String ACTION_DRONE_EMERGENCY_STATE_CHANGED = "com.parrot.emergency.changed";
    public static final String ACTION_DRONE_FIRMWARE_CHECK = "com.parrot.firmware.checked";
    public static final String ACTION_DRONE_FLYINGCAMERAMODE_CHANGED = "com.parrot.flyingcameramode.changed";
    public static final String ACTION_DRONE_FLYINGCAMERA_CHANGED = "com.parrot.flyingcamera.changed";
    public static final String ACTION_DRONE_FLYINGSTATE_CHANGED = "com.parrot.flyingstate.changed";
    public static final String ACTION_DRONE_FLYING_STATE_CHANGED = "com.parrot.flying.changed";
    public static final String ACTION_DRONE_MOTORS_CHANGED = "com.parrot.motors.changed";
    public static final String ACTION_DRONE_PSI_CHANGED = "com.parrot.psi.changed";
    public static final String ACTION_DRONE_SPEED_CHANGED = "com.parrot.speed.changed";
    public static final String ACTION_DRONE_STATE_READY = "com.parrot.drone.ready";
    public static final String ACTION_DRONE_TAKEOFF_CANCELLED = "com.parrot.drone.takeoff.cancelled";
    public static final String ACTION_DRONE_USB_RECORD_STOPPED = "com.parrot.usb.recording.stopped";
    public static final String ACTION_GPS_FIRMWARE_PARAMS_CHANGED = "com.parrot.gpsfirmware.changed";
    public static final String ACTION_GPS_PARAMS_CHANGED = "com.parrot.gpsparams.changed";
    public static final String ACTION_NEW_MEDIA_IS_AVAILABLE = "com.parrot.controlservice.media.available";
    public static final String ACTION_RECORD_READY_CHANGED = "com.parrot.record.ready.changed";
    public static final String ACTION_USB_STATE_CHANGED = "com.parrot.drone.usb.state.changed";
    public static final String ACTION_VIDEO_RECORDING_BUFFER_CHANGED = "com.parrot.recording.buffer.changed";
    public static final String ACTION_VIDEO_RECORDING_STATE_CHANGED = "com.parrot.recording.changed";
    public static final String EXIF_MAKE_TAG = "Parrot AR.Drone";
    public static final String EXTRA_CAMERA_READY = "com.parrot.extra.camera.ready";
    public static final String EXTRA_CONNECTION_STATE = "connection.state";
    public static final String EXTRA_DRONE_ALTITUDE = "com.parrot.extra.altitude";
    public static final String EXTRA_DRONE_BATTERY = "com.parrot.battery.extra.value";
    public static final String EXTRA_DRONE_CTRLSTATE = "com.parrot.extra.ctrlstate";
    public static final String EXTRA_DRONE_FLYING = "com.parrot.flying.extra";
    public static final String EXTRA_DRONE_FLYINGCAMERA = "com.parrot.extra.flyingcamera";
    public static final String EXTRA_DRONE_FLYINGCAMERAMODE = "com.parrot.extra.flyingcameramode";
    public static final String EXTRA_DRONE_FLYINGSTATE = "com.parrot.extra.flyingstate";
    public static final String EXTRA_DRONE_MOTORS = "com.parrot.extra.motors";
    public static final String EXTRA_DRONE_PSI = "com.parrot.extra.psi";
    public static final String EXTRA_DRONE_SPEED = "com.parrot.extra.speed";
    public static final String EXTRA_EMERGENCY_CODE = "com.parrot.emergency.extra.code";
    public static final String EXTRA_FIRMWARE_UPDATE_REQUIRED = "updateRequired";
    public static final String EXTRA_GPS_ACTIVE = "com.parrot.extra.gpsactive";
    public static final String EXTRA_GPS_EPHEMERIS = "com.parrot.extra.gpsephemeris";
    public static final String EXTRA_GPS_FIRMWARE_UPDATE_PROGRESS = "com.parrot.extra.gpsfirmwareupdateprogress";
    public static final String EXTRA_GPS_FIRMWARE_UPDATE_STATE = "com.parrot.extra.gpsfirmwareupdatestate";
    public static final String EXTRA_GPS_LATFUSED = "com.parrot.extra.gpslatfused";
    public static final String EXTRA_GPS_LONGFUSED = "com.parrot.extra.gpslongfused";
    public static final String EXTRA_GPS_PLUGGED = "com.parrot.extra.gpsplugged";
    public static final String EXTRA_GPS_PRECISION = "com.parrot.extra.gpsprecision";
    public static final String EXTRA_GPS_SATSUSED = "com.parrot.extra.gpssatsused";
    public static final String EXTRA_MEDIA_PATH = "controlservice.media.path";
    public static final String EXTRA_RECORDING_STATE = "com.parrot.recording.extra.state";
    public static final String EXTRA_RECORD_READY = "com.parrot.extra.record.ready";
    public static final String EXTRA_USB_ACTIVE = "com.parrot.extra.usbactive";
    public static final String EXTRA_USB_REMAINING_TIME = "com.parrot.extra.usbremaining";
    public static final String EXTRA_VIDEO_RECORD_PROGRESS = "com.parrot.extra.video.record.progress";
    private static final String TAG = "DroneControlService";
    private final IBinder binder = new LocalBinder();
    private Queue<DroneServiceCommand> commandQueue;
    private Object commandQueueLock = new Object();
    private final ConfigurationEventDelegate configDelegate = new C11761();
    private Object configLock;
    private ConfigState configurationState;
    private ControlData ctrlData;
    private ServiceStateBase currState;
    private DroneDebugListener debugListener;
    private ARDroneAcademyNavdata droneAcademyNavdata;
    private Configuration droneConfiguration;
    private ARDroneEngine droneEngine;
    private DroneVersion droneVersion = DroneVersion.UNKNOWN;
    private ErrorState errorState;
    private ExecutorService executor;
    private ARDroneMediaGallery gallery;
    private final ConfigurationEventDelegate gpsDelegate = new C11772();
    private ConfigState gpsState;
    private ARDroneInput inputCurr;
    private HashMap<String, Intent> intentCache;
    private Location locationCurrent;
    private boolean magnetoEnabled;
    private ArrayList<String> mediaDownloaded;
    private Object navdataThreadLock;
    private Runnable navdataUpdateRunnable = new C11783();
    private Thread navdataUpdateThread;
    private ConfigState prevConfigurationState;
    private int prevEphemerisStatus;
    private ErrorState prevErrorState;
    private int prevFirmwareUpdateProgress;
    private int prevFirmwareUpdateState;
    private boolean prevFlying;
    private int prevFlyingState;
    private boolean prevGpsActive;
    private boolean prevGpsPlugged;
    private float prevGpsPrecision;
    private int prevGpsSatsUsed;
    private ConfigState prevGpsState;
    private double prevLatFused;
    private double prevLongFused;
    private long prevVideoFrames;
    private long startTime;
    private boolean stopThreads;
    private boolean usbActive;
    private UsbVideoDownloadProgressListener usbVideoDownloadListener;
    private UsbVideoDownloader usbVideoDownloader;
    private int videoChannel;
    private WakeLock wakeLock;
    private Thread workerThread;
    private Object workerThreadLock;

    class C11761 implements ConfigurationEventDelegate {
        C11761() {
        }

        public void onConfigurationEvent(boolean z) {
            if (z) {
                DroneControlService.this.configurationState = ConfigState.CONFIG_STATE_IDLE;
            }
        }
    }

    class C11772 implements ConfigurationEventDelegate {
        C11772() {
        }

        public void onConfigurationEvent(boolean z) {
            if (z) {
                DroneControlService.this.gpsState = ConfigState.CONFIG_STATE_IDLE;
            }
        }
    }

    class C11783 implements Runnable {
        private boolean prevAppStoppedRecord;
        private boolean prevArdStopsUsbRecord;
        private boolean prevAutorecordStarted;
        private int prevBatteryStatus;
        private boolean prevCameraReady;
        private boolean prevConfigWasDone;
        private boolean prevConnected;
        private int prevCtrlState;
        private boolean prevFlyingCameraEnabled;
        private int prevFlyingCameraModeVal;
        private byte prevMotor1;
        private int prevMotor1State;
        private byte prevMotor2;
        private int prevMotor2State;
        private byte prevMotor3;
        private int prevMotor3State;
        private byte prevMotor4;
        private int prevMotor4State;
        private float prevPsi;
        private boolean prevRecordReady;
        private boolean prevRecording;
        private int prevRemainingTime;
        private boolean prevTakeoffCancelled;
        private boolean prevUsbActive;
        private float prevVideoBufferProgress;

        C11783() {
        }

        public void run() {
            while (!DroneControlService.this.stopThreads) {
                int i;
                Object obj;
                NavData navData = DroneControlService.this.droneEngine.getNavData();
                if (DroneControlService.this.errorState != DroneControlService.this.prevErrorState) {
                    DroneControlService.this.onEmergencyStateChanged(DroneControlService.this.errorState);
                    DroneControlService.this.prevErrorState = DroneControlService.this.errorState;
                }
                int vBatFlyingPercentage = navData.getNavDataDemo().getVBatFlyingPercentage();
                if (vBatFlyingPercentage != this.prevBatteryStatus) {
                    DroneControlService.this.onBatteryStateChanged(vBatFlyingPercentage);
                    this.prevBatteryStatus = vBatFlyingPercentage;
                }
                vBatFlyingPercentage = DroneControlService.this.droneAcademyNavdata.getFlyingStateNative();
                if (vBatFlyingPercentage != DroneControlService.this.prevFlyingState) {
                    DroneControlService.this.onFlyingStateChanged(vBatFlyingPercentage);
                    DroneControlService.this.prevFlyingState = vBatFlyingPercentage;
                }
                vBatFlyingPercentage = navData.getNavDataDemo().getCtrlState();
                if (vBatFlyingPercentage != this.prevCtrlState) {
                    DroneControlService.this.onCtrlStateChanged(vBatFlyingPercentage);
                    this.prevCtrlState = vBatFlyingPercentage;
                }
                NavDataPwm navDataPwm = navData.getNavDataPwm();
                byte motor1 = navDataPwm.getMotor1();
                byte motor2 = navDataPwm.getMotor2();
                byte motor3 = navDataPwm.getMotor3();
                byte motor4 = navDataPwm.getMotor4();
                int motor1Status = navDataPwm.getMotor1Status();
                int motor2Status = navDataPwm.getMotor2Status();
                int motor3Status = navDataPwm.getMotor3Status();
                int motor4Status = navDataPwm.getMotor4Status();
                if (!(motor1 == this.prevMotor1 && motor2 == this.prevMotor2 && motor3 == this.prevMotor3 && motor4 == this.prevMotor4 && motor1Status == this.prevMotor1State && motor2Status == this.prevMotor2State && motor3Status == this.prevMotor3State && motor4Status == this.prevMotor4State)) {
                    DroneControlService.this.onMotorsChanged(motor1, motor1Status, motor2, motor2Status, motor3, motor3Status, motor4, motor4Status);
                    this.prevMotor1 = motor1;
                    this.prevMotor2 = motor2;
                    this.prevMotor3 = motor3;
                    this.prevMotor4 = motor4;
                    this.prevMotor1State = motor1Status;
                    this.prevMotor2State = motor2Status;
                    this.prevMotor3State = motor3Status;
                    this.prevMotor4State = motor4Status;
                }
                double sqrt = (double) ((float) Math.sqrt((double) ((navData.getNavDataDemo().getVx() * navData.getNavDataDemo().getVx()) + (navData.getNavDataDemo().getVy() * navData.getNavDataDemo().getVy()))));
                DroneControlService.this.onAltitudeChanged(navData.getNavDataDemo().getAltitude());
                DroneControlService.this.onSpeedChanged(sqrt);
                float psi = navData.getNavDataDemo().getPsi();
                if (psi != this.prevPsi) {
                    DroneControlService.this.onPsiChanged(psi);
                    this.prevPsi = psi;
                }
                boolean recordReady = DroneControlService.this.droneAcademyNavdata.getRecordReady();
                boolean usbState = DroneControlService.this.droneAcademyNavdata.getUsbState();
                motor1Status = DroneControlService.this.droneAcademyNavdata.getRemainingUsbTime();
                if (!(recordReady == this.prevRecording && usbState == this.prevUsbActive && motor1Status == this.prevRemainingTime)) {
                    DroneControlService.this.onRecordChanged(recordReady, usbState, motor1Status);
                    this.prevRecording = recordReady;
                }
                psi = DroneControlService.this.droneAcademyNavdata.getHDVideoRecordProgress();
                if (psi != this.prevVideoBufferProgress) {
                    DroneControlService.this.onRecordBufferChanged(psi);
                    this.prevVideoBufferProgress = psi;
                }
                if (this.prevUsbActive != usbState) {
                    DroneControlService.this.onUsbStateChanged(usbState);
                    this.prevUsbActive = usbState;
                }
                boolean cameraState = DroneControlService.this.droneAcademyNavdata.getCameraState();
                boolean recordReady2 = DroneControlService.this.droneAcademyNavdata.getRecordReady();
                boolean takeoffState = DroneControlService.this.droneAcademyNavdata.getTakeoffState();
                if (takeoffState != DroneControlService.this.prevFlying) {
                    if (takeoffState) {
                        DataTracker.trackInfoStr(TRACK_KEY_ENUM.TRACK_KEY_EVENT__FLIGHT_STARTED, (String) Configuration.instance().getValue(Source.CONTROL_CONFIG, EventKey.DRONE_SERIAL));
                    } else {
                        i = ((Boolean) Configuration.instance().getValue(Source.CONTROL_CONFIG, EventKey.OUTDOOR)).booleanValue() ? 1 : 0;
                        vBatFlyingPercentage = ((Boolean) Configuration.instance().getValue(Source.CONTROL_CONFIG, EventKey.FLIGHT_WITHOUT_SHELL)).booleanValue() ? 1 : 0;
                        ApplicationSettings applicationSettings = new ApplicationSettings(DroneControlService.this);
                        if (applicationSettings.isFlipEnabled()) {
                            switch (applicationSettings.getFlipDirection()) {
                                case FRONT:
                                    motor1Status = 1;
                                    break;
                                case LEFT:
                                    motor1Status = 3;
                                    break;
                                case BACK:
                                    motor1Status = 2;
                                    break;
                                case RIGHT:
                                    motor1Status = 4;
                                    break;
                                default:
                                    motor1Status = 0;
                                    break;
                            }
                        }
                        motor1Status = 5;
                        TRACK_KEY_ENUM track_key_enum = TRACK_KEY_ENUM.TRACK_KEY_EVENT__FLIGHT_ENDED;
                        i = (i == 0 || i == 1) ? i + 1 : 0;
                        vBatFlyingPercentage = (vBatFlyingPercentage == 0 || vBatFlyingPercentage == 1) ? vBatFlyingPercentage + 1 : 0;
                        DataTracker.trackInfoInt3(track_key_enum, i, vBatFlyingPercentage, motor1Status);
                    }
                }
                if (!(cameraState == this.prevCameraReady && recordReady2 == this.prevRecordReady && takeoffState == DroneControlService.this.prevFlying)) {
                    DroneControlService.this.onCameraReadyChanged(cameraState);
                    DroneControlService.this.onRecordReadyChanged(recordReady2);
                    if (takeoffState) {
                        DroneControlService.this.onTookOff();
                    } else {
                        DroneControlService.this.onLanded();
                    }
                    DroneControlService.this.prevFlying = takeoffState;
                    this.prevCameraReady = cameraState;
                    this.prevRecordReady = recordReady2;
                }
                recordReady = (256 & navData.getArdroneState()) != 0;
                if (recordReady != this.prevFlyingCameraEnabled) {
                    DroneControlService.this.onFlyingCameraChanged(recordReady);
                    i = DroneControlService.this.getDroneConfig().getFlyingCameraModeValue();
                    if (i != this.prevFlyingCameraModeVal) {
                        DroneControlService.this.onFlyingCameraModeChanged(i);
                        this.prevFlyingCameraModeVal = i;
                    }
                    this.prevFlyingCameraEnabled = recordReady;
                }
                recordReady = navData.getConfigWasDone();
                if (recordReady != this.prevConfigWasDone && recordReady) {
                    DroneControlService.this.onDroneReady();
                    this.prevConfigWasDone = recordReady;
                }
                recordReady = DroneControlService.this.droneAcademyNavdata.getAutorecordState();
                if (recordReady && this.prevAutorecordStarted != recordReady) {
                    DroneControlService.this.onDroneAutoRecordStarted();
                    this.prevAutorecordStarted = recordReady;
                }
                recordReady = DroneControlService.this.droneAcademyNavdata.checkUsbRecordStatus();
                if (recordReady && recordReady != this.prevArdStopsUsbRecord) {
                    DroneControlService.this.onDroneUsbRecordStopped();
                    this.prevArdStopsUsbRecord = recordReady;
                }
                recordReady = DroneControlService.this.droneAcademyNavdata.checkAppRecordStatus();
                if (recordReady && recordReady != this.prevAppStoppedRecord) {
                    DroneControlService.this.onAppStoppedRecord();
                    this.prevAppStoppedRecord = recordReady;
                }
                recordReady = DroneControlService.this.droneAcademyNavdata.checkTakeOffCancelled();
                if (recordReady && this.prevTakeoffCancelled != recordReady) {
                    DroneControlService.this.onDroneTakeOffCancelled();
                    this.prevTakeoffCancelled = recordReady;
                }
                recordReady = navData.getNavDataGps().isGpsPlugged();
                usbState = navData.getNavDataGps().isGpsActive();
                float gpsPrecision = navData.getNavDataGps().getGpsPrecision();
                int gpsSatsUsed = navData.getNavDataGps().getGpsSatsUsed();
                double latFused = navData.getNavDataGps().getLatFused();
                double longFused = navData.getNavDataGps().getLongFused();
                if (recordReady != DroneControlService.this.prevGpsPlugged && recordReady) {
                    DroneControlService.this.requestConfigUpdate();
                }
                if (!(recordReady == DroneControlService.this.prevGpsPlugged && usbState == DroneControlService.this.prevGpsActive && gpsPrecision == DroneControlService.this.prevGpsPrecision && gpsSatsUsed == DroneControlService.this.prevGpsSatsUsed && latFused == DroneControlService.this.prevLatFused && longFused == DroneControlService.this.prevLongFused)) {
                    DroneControlService.this.prevGpsPlugged = recordReady;
                    DroneControlService.this.prevGpsActive = usbState;
                    DroneControlService.this.prevGpsPrecision = gpsPrecision;
                    DroneControlService.this.prevGpsSatsUsed = gpsSatsUsed;
                    DroneControlService.this.prevLatFused = latFused;
                    DroneControlService.this.prevLongFused = longFused;
                    DroneControlService.this.onGpsParamsChanged(DroneControlService.this.prevGpsPlugged, DroneControlService.this.prevGpsActive, DroneControlService.this.prevGpsPrecision, DroneControlService.this.prevGpsSatsUsed, DroneControlService.this.prevLatFused, DroneControlService.this.prevLongFused);
                }
                vBatFlyingPercentage = navData.getNavDataGps().getEphemerisStatus();
                if (vBatFlyingPercentage != DroneControlService.this.prevEphemerisStatus) {
                    DroneControlService.this.prevEphemerisStatus = vBatFlyingPercentage;
                    obj = 1;
                } else {
                    obj = null;
                }
                i = navData.getNavDataGps().getFirmwareStatus() & 65535;
                if (i != DroneControlService.this.prevFirmwareUpdateState) {
                    DroneControlService.this.prevFirmwareUpdateState = i;
                    obj = 1;
                }
                if (!((i & 8) == 0 && (i & 16) == 0 && (i & 32) == 0)) {
                    i = (navData.getNavDataGps().getFirmwareStatus() >> 16) & 65535;
                    if (i != DroneControlService.this.prevFirmwareUpdateProgress) {
                        DroneControlService.this.prevFirmwareUpdateProgress = i;
                        obj = 1;
                    }
                }
                if (obj != null) {
                    DroneControlService.this.onGpsFirmwareUpdateParamsChanged(DroneControlService.this.prevEphemerisStatus, DroneControlService.this.prevFirmwareUpdateState, DroneControlService.this.prevFirmwareUpdateProgress);
                }
                DroneControlService.this.checkErrors(navData);
                if (DroneControlService.this.ctrlData.navdata_connected != this.prevConnected) {
                    DataTracker.trackInfoVoid(DroneControlService.this.ctrlData.navdata_connected ? TRACK_KEY_ENUM.TRACK_KEY_EVENT__DRONE_CONNECTED : TRACK_KEY_ENUM.TRACK_KEY_EVENT__DRONE_DISCONNECTED);
                    this.prevConnected = DroneControlService.this.ctrlData.navdata_connected;
                }
                try {
                    Thread.sleep(100);
                    if ((DroneControlService.this.currState instanceof PausedServiceState) && !DroneControlService.this.stopThreads) {
                        synchronized (DroneControlService.this.navdataThreadLock) {
                            try {
                                DroneControlService.this.navdataThreadLock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                    return;
                }
            }
        }
    }

    public enum ConfigState {
        CONFIG_STATE_IDLE,
        CONFIG_STATE_NEEDED,
        CONFIG_STATE_IN_PROGRESS
    }

    private static class DataTrackUtils {
        private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmssZ");
        private static final SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        private DataTrackUtils() {
        }

        public static String getDataDir(Context context) throws NameNotFoundException {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir;
        }

        public static float getFreeSize() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return ((float) (((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize()))) / 1048576.0f;
        }

        public static String makeTimeStamp(Date date, boolean z) {
            return (z ? formatter2 : formatter).format(date);
        }
    }

    public enum DroneFlipDirection {
        LEFT,
        RIGHT,
        FRONT,
        BACK
    }

    public class LocalBinder extends Binder {
        public DroneControlService getService() {
            return DroneControlService.this;
        }
    }

    private static DEVICE_TYPE_ENUM getDeviceType() {
        int i = VERSION.SDK_INT;
        return i < 11 ? DEVICE_TYPE_ENUM.DEVICE_TYPE__ANDROID2 : i < 14 ? DEVICE_TYPE_ENUM.DEVICE_TYPE__ANDROID3 : DEVICE_TYPE_ENUM.DEVICE_TYPE__ANDROID4;
    }

    private void initIntents() {
        this.intentCache = new HashMap(17);
        this.intentCache.put(ACTION_VIDEO_RECORDING_STATE_CHANGED, new Intent(ACTION_VIDEO_RECORDING_STATE_CHANGED));
        this.intentCache.put(ACTION_DRONE_EMERGENCY_STATE_CHANGED, new Intent(ACTION_DRONE_EMERGENCY_STATE_CHANGED));
        this.intentCache.put(ACTION_DRONE_FLYING_STATE_CHANGED, new Intent(ACTION_DRONE_FLYING_STATE_CHANGED));
        this.intentCache.put(ACTION_DRONE_BATTERY_CHANGED, new Intent(ACTION_DRONE_BATTERY_CHANGED));
        this.intentCache.put(ACTION_DRONE_ALTITUDE_CHANGED, new Intent(ACTION_DRONE_ALTITUDE_CHANGED));
        this.intentCache.put(ACTION_DRONE_SPEED_CHANGED, new Intent(ACTION_DRONE_SPEED_CHANGED));
        this.intentCache.put(ACTION_DRONE_PSI_CHANGED, new Intent(ACTION_DRONE_PSI_CHANGED));
        this.intentCache.put(ACTION_DRONE_FIRMWARE_CHECK, new Intent(ACTION_DRONE_FIRMWARE_CHECK));
        this.intentCache.put(ACTION_DRONE_STATE_READY, new Intent(ACTION_DRONE_STATE_READY));
        this.intentCache.put(ACTION_DRONE_CONNECTION_CHANGED, new Intent(ACTION_DRONE_CONNECTION_CHANGED));
        this.intentCache.put(ACTION_NEW_MEDIA_IS_AVAILABLE, new Intent(ACTION_NEW_MEDIA_IS_AVAILABLE));
        this.intentCache.put(ACTION_DRONE_CONFIG_STATE_CHANGED, new Intent(ACTION_DRONE_CONFIG_STATE_CHANGED));
        this.intentCache.put(ACTION_RECORD_READY_CHANGED, new Intent(ACTION_RECORD_READY_CHANGED));
        this.intentCache.put(ACTION_CAMERA_READY_CHANGED, new Intent(ACTION_CAMERA_READY_CHANGED));
        this.intentCache.put(ACTION_DRONE_USB_RECORD_STOPPED, new Intent(ACTION_DRONE_USB_RECORD_STOPPED));
        this.intentCache.put(ACTION_DRONE_AUTORECORD_STARTED, new Intent(ACTION_DRONE_AUTORECORD_STARTED));
        this.intentCache.put(ACTION_APP_RECORD_STOPPED, new Intent(ACTION_APP_RECORD_STOPPED));
        this.intentCache.put(ACTION_DRONE_TAKEOFF_CANCELLED, new Intent(ACTION_DRONE_TAKEOFF_CANCELLED));
        this.intentCache.put(ACTION_USB_STATE_CHANGED, new Intent(ACTION_USB_STATE_CHANGED));
        this.intentCache.put(DroneStateManager.ACTION_DRONE_STATE_CHANGED, new Intent(DroneStateManager.ACTION_DRONE_STATE_CHANGED));
        this.intentCache.put(ACTION_VIDEO_RECORDING_BUFFER_CHANGED, new Intent(ACTION_VIDEO_RECORDING_BUFFER_CHANGED));
        this.intentCache.put(ACTION_DRONE_FLYINGSTATE_CHANGED, new Intent(ACTION_DRONE_FLYINGSTATE_CHANGED));
        this.intentCache.put(ACTION_DRONE_FLYINGCAMERA_CHANGED, new Intent(ACTION_DRONE_FLYINGCAMERA_CHANGED));
        this.intentCache.put(ACTION_DRONE_FLYINGCAMERAMODE_CHANGED, new Intent(ACTION_DRONE_FLYINGCAMERAMODE_CHANGED));
        this.intentCache.put(ACTION_DRONE_MOTORS_CHANGED, new Intent(ACTION_DRONE_MOTORS_CHANGED));
        this.intentCache.put(ACTION_DRONE_CTRLSTATE_CHANGED, new Intent(ACTION_DRONE_CTRLSTATE_CHANGED));
        this.intentCache.put(ACTION_GPS_PARAMS_CHANGED, new Intent(ACTION_GPS_PARAMS_CHANGED));
        this.intentCache.put(ACTION_GPS_FIRMWARE_PARAMS_CHANGED, new Intent(ACTION_GPS_FIRMWARE_PARAMS_CHANGED));
    }

    private void onCameraReadyChanged(boolean z) {
        Intent intent = (Intent) this.intentCache.get(ACTION_CAMERA_READY_CHANGED);
        intent.putExtra(EXTRA_CAMERA_READY, z);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void onCtrlStateChanged(int i) {
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_CTRLSTATE_CHANGED);
        intent.putExtra(EXTRA_DRONE_CTRLSTATE, i);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void onDroneReady() {
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast((Intent) this.intentCache.get(ACTION_DRONE_STATE_READY));
    }

    private void onEmergencyStateChanged(ErrorState errorState) {
        Log.d(TAG, "Notifying about " + errorState.name() + " state changed");
        switch (errorState) {
            case NAVDATA_CONNECTION:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__MESSAGE_CONTROL_LINK_NOT_AVAILABLE);
                break;
            case EMERGENCY_MOTORS:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__MESSAGE_MOTORS_EMERGENCY);
                break;
            case EMERGENCY_PIC_WATCHDOG:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__MESSAGE_PIC_WATCHDOG_EMERGENCY);
                break;
            case EMERGENCY_PIC_VERSION:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__MESSAGE_PIC_VERSION_EMERGENCY);
                break;
            case EMERGENCY_ULTRASOUND:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__MESSAGE_ULTRASOUND_EMERGENCY);
                break;
            case EMERGENCY_UNKNOWN:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__MESSAGE_UNKNOWN_EMERGENCY);
                break;
            case ALERT_CAMERA:
                DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__MESSAGE_VIDEO_CONNECTION_ALERT);
                break;
        }
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_EMERGENCY_STATE_CHANGED);
        intent.putExtra(EXTRA_EMERGENCY_CODE, errorState);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void onFlyingCameraChanged(boolean z) {
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_FLYINGCAMERA_CHANGED);
        intent.putExtra(EXTRA_DRONE_FLYINGCAMERA, z);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void onFlyingCameraModeChanged(int i) {
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_FLYINGCAMERAMODE_CHANGED);
        intent.putExtra(EXTRA_DRONE_FLYINGCAMERAMODE, i);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void onFlyingStateChanged(int i) {
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_FLYINGSTATE_CHANGED);
        intent.putExtra(EXTRA_DRONE_FLYINGSTATE, i);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void onGpsFirmwareUpdateParamsChanged(int i, int i2, int i3) {
        Intent intent = (Intent) this.intentCache.get(ACTION_GPS_FIRMWARE_PARAMS_CHANGED);
        intent.putExtra(EXTRA_GPS_EPHEMERIS, i);
        intent.putExtra(EXTRA_GPS_FIRMWARE_UPDATE_STATE, i2);
        intent.putExtra(EXTRA_GPS_FIRMWARE_UPDATE_PROGRESS, i3);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void onGpsParamsChanged(boolean z, boolean z2, float f, int i, double d, double d2) {
        Intent intent = (Intent) this.intentCache.get(ACTION_GPS_PARAMS_CHANGED);
        intent.putExtra(EXTRA_GPS_PLUGGED, z);
        intent.putExtra(EXTRA_GPS_ACTIVE, z2);
        intent.putExtra(EXTRA_GPS_PRECISION, f);
        intent.putExtra(EXTRA_GPS_SATSUSED, i);
        intent.putExtra(EXTRA_GPS_LATFUSED, d);
        intent.putExtra(EXTRA_GPS_LONGFUSED, d2);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void onMotorsChanged(byte b, int i, byte b2, int i2, byte b3, int i3, byte b4, int i4) {
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_MOTORS_CHANGED);
        intent.putExtra(EXTRA_DRONE_MOTORS, new int[]{b, i, b2, i2, b3, i3, b4, i4});
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void onRecordBufferChanged(float f) {
        Intent intent = (Intent) this.intentCache.get(ACTION_VIDEO_RECORDING_BUFFER_CHANGED);
        intent.putExtra(EXTRA_VIDEO_RECORD_PROGRESS, f);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void onRecordChanged(boolean z, boolean z2, int i) {
        this.usbActive = z2;
        Intent intent = (Intent) this.intentCache.get(ACTION_VIDEO_RECORDING_STATE_CHANGED);
        intent.putExtra(EXTRA_USB_ACTIVE, z2);
        intent.putExtra(EXTRA_RECORDING_STATE, z);
        intent.putExtra(EXTRA_USB_REMAINING_TIME, i);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void onRecordReadyChanged(boolean z) {
        Intent intent = (Intent) this.intentCache.get(ACTION_RECORD_READY_CHANGED);
        intent.putExtra(EXTRA_RECORD_READY, z);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public native void calibrateMagneto();

    protected void checkErrors(NavData navData) {
        this.errorState = ErrorState.NONE;
        ARDroneInput currentInput = this.droneEngine.getCurrentInput();
        if (this.configurationState == ConfigState.CONFIG_STATE_NEEDED) {
            this.configurationState = ConfigState.CONFIG_STATE_IN_PROGRESS;
            this.droneEngine.getConfiguration().configurationGet(this.configDelegate);
            Log.d(TAG, "CONFIGURATION GET [sent]");
        }
        if (this.prevConfigurationState != this.configurationState && this.configurationState == ConfigState.CONFIG_STATE_IDLE) {
            onConfigStateChanged();
            Log.d(TAG, "OnConfigChanged sent [OK]");
        }
        this.prevConfigurationState = this.configurationState;
        if (this.gpsState == ConfigState.CONFIG_STATE_NEEDED && navData.getConfigWasDone() && this.locationCurrent != null) {
            this.gpsState = ConfigState.CONFIG_STATE_IN_PROGRESS;
            Log.d(TAG, "Userbox latitude : " + this.locationCurrent.getLatitude());
            this.droneConfiguration.addEvent(EventKey.LATITUDE, Double.valueOf(this.locationCurrent.getLatitude()), null);
            Log.d(TAG, "Userbox longitude : " + this.locationCurrent.getLongitude());
            this.droneConfiguration.addEvent(EventKey.LONGITUDE, Double.valueOf(this.locationCurrent.getLongitude()), null);
            Log.d(TAG, "Userbox altitude : " + this.locationCurrent.getAltitude());
            this.droneConfiguration.addEvent(EventKey.LONGITUDE, Double.valueOf(this.locationCurrent.getLongitude()), this.gpsDelegate);
            ARDroneVideoEncapsuler.setGpsInfos(this.locationCurrent);
            Log.d(TAG, "GPS location sent [OK]");
        }
        this.errorState = ErrorState.NONE;
        if (this.droneEngine.getNavDataClient().getNumRetries() > 0) {
            this.ctrlData.navdata_connected = false;
            this.errorState = ErrorState.NAVDATA_CONNECTION;
            this.ctrlData.reset();
            navData.reset();
            Log.d(TAG, "NAVDATA Reset [OK]");
            return;
        }
        this.ctrlData.navdata_connected = true;
        long ardroneState = navData.getArdroneState();
        if (this.droneAcademyNavdata.getEmergencyState()) {
            if ((ConfigArdroneMask.ARDRONE_ADC_WATCHDOG_MASK & ardroneState) > 0) {
                this.errorState = ErrorState.EMERGENCY_PIC_WATCHDOG;
            } else if ((ConfigArdroneMask.ARDRONE_VBAT_LOW & ardroneState) > 0) {
                this.errorState = ErrorState.EMERGENCY_VBAT_LOW;
            } else if ((ConfigArdroneMask.ARDRONE_CUTOUT_MASK & ardroneState) > 0) {
                this.errorState = ErrorState.EMERGENCY_CUTOUT;
            } else if ((ConfigArdroneMask.ARDRONE_MOTORS_MASK & ardroneState) > 0) {
                this.errorState = ErrorState.EMERGENCY_MOTORS;
            } else if ((ConfigArdroneMask.ARDRONE_VIDEO_THREAD_ON & ardroneState) <= 0) {
                this.errorState = ErrorState.EMERGENCY_CAMERA;
            } else if ((ConfigArdroneMask.ARDRONE_PIC_VERSION_MASK & ardroneState) <= 0) {
                this.errorState = ErrorState.EMERGENCY_PIC_VERSION;
            } else if ((ConfigArdroneMask.ARDRONE_ANGLES_OUT_OF_RANGE & ardroneState) > 0) {
                this.errorState = ErrorState.EMERGENCY_ANGLE_OUT_OF_RANGE;
            } else if ((ConfigArdroneMask.ARDRONE_USER_EL & ardroneState) > 0) {
                this.errorState = ErrorState.EMERGENCY_USER_EL;
            } else if ((ConfigArdroneMask.ARDRONE_ULTRASOUND_MASK & ardroneState) > 0) {
                this.errorState = ErrorState.EMERGENCY_ULTRASOUND;
            } else {
                this.errorState = ErrorState.EMERGENCY_UNKNOWN;
            }
            if (FlyingState.LANDED == this.droneAcademyNavdata.getFlyingState()) {
                this.ctrlData.reset();
                navData.reset();
                return;
            }
            return;
        }
        if (this.droneEngine.getCurrentVideoStage().getNumRetries() > VideoStage.getVideoMaxRetries()) {
            this.errorState = ErrorState.ALERT_CAMERA;
        } else if ((ConfigArdroneMask.ARDRONE_VBAT_LOW & ardroneState) > 0) {
            this.errorState = ErrorState.ALERT_VBAT_LOW;
        } else if ((ConfigArdroneMask.ARDRONE_ULTRASOUND_MASK & ardroneState) > 0) {
            this.errorState = ErrorState.ALERT_ULTRASOUND;
        } else if ((ardroneState & 4) <= 0 && this.droneAcademyNavdata.getFlyingState() == FlyingState.FLYING) {
            this.errorState = ErrorState.ALERT_VISION;
        }
        if ((currentInput.getUserInputState() & 512) > 0 && !this.droneAcademyNavdata.getTakeoffState()) {
            this.errorState = ErrorState.START_NOT_RECEIVED;
        }
    }

    public void commitControlData() {
        this.ctrlData.sendControls();
    }

    protected void connect() {
        this.currState.connect();
    }

    protected void disconnect() {
        this.currState.disconnect();
    }

    public void doFlip(DroneFlipDirection droneFlipDirection) {
        Configuration configuration = this.droneEngine.getConfiguration();
        switch (droneFlipDirection) {
            case FRONT:
                configuration.addEvent(EventKey.FLIGHT_ANIM, ARDroneAnimation.FLIP_AHEAD.toString(), null);
                return;
            case LEFT:
                configuration.addEvent(EventKey.FLIGHT_ANIM, ARDroneAnimation.FLIP_LEFT.toString(), null);
                return;
            case BACK:
                configuration.addEvent(EventKey.FLIGHT_ANIM, ARDroneAnimation.FLIP_BEHIND.toString(), null);
                return;
            case RIGHT:
                configuration.addEvent(EventKey.FLIGHT_ANIM, ARDroneAnimation.FLIP_RIGHT.toString(), null);
                return;
            default:
                return;
        }
    }

    public native void flatTrim();

    public Configuration getConfiguration() {
        return this.droneConfiguration;
    }

    public DroneConfig getDroneConfig() {
        return new DroneConfig(this.droneEngine);
    }

    public ARDroneEngine getDroneEngine() {
        return this.droneEngine;
    }

    public DroneVersion getDroneVersion() {
        if (this.droneVersion == DroneVersion.UNKNOWN) {
            if (ARDroneVersion.isArDrone1()) {
                this.droneVersion = DroneVersion.DRONE_1;
            } else if (ARDroneVersion.isArDrone2()) {
                this.droneVersion = DroneVersion.DRONE_2;
            } else {
                DroneVersion droneVersion = this.droneVersion;
                if (droneVersion == DroneVersion.UNKNOWN) {
                    String downloadFile = FTPUtils.downloadFile(this, DroneConfig.getDroneHost(), DroneConfig.getFtpPort(), "version.txt");
                    if (downloadFile != null && downloadFile.startsWith("1.")) {
                        return DroneVersion.DRONE_1;
                    }
                    if (downloadFile != null && downloadFile.startsWith("2.")) {
                        return DroneVersion.DRONE_2;
                    }
                }
                this.droneVersion = droneVersion;
            }
        }
        return this.droneVersion;
    }

    public File getMediaDir() {
        return FileUtils.getMediaFolder(this);
    }

    public List<VideoItem> getUsbVideoList() {
        return this.usbVideoDownloader.getMediaItems();
    }

    public boolean isDroneConnected() {
        return (this.currState instanceof ConnectedServiceState) || (this.currState instanceof PausedServiceState);
    }

    public boolean isMediaStorageAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public boolean isRecordReady() {
        return this.droneAcademyNavdata.getRecordReady();
    }

    public boolean isUSBInserted() {
        return this.droneAcademyNavdata.getUsbState();
    }

    protected void onAltitudeChanged(int i) {
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_ALTITUDE_CHANGED);
        intent.putExtra(EXTRA_DRONE_ALTITUDE, i);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    protected void onAppStoppedRecord() {
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast((Intent) this.intentCache.get(ACTION_APP_RECORD_STOPPED));
    }

    protected void onBatteryStateChanged(int i) {
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_BATTERY_CHANGED);
        intent.putExtra(EXTRA_DRONE_BATTERY, i);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    public void onCommandFinished(DroneServiceCommand droneServiceCommand) {
        this.currState.onCommandFinished(droneServiceCommand);
    }

    public void onConfigStateChanged() {
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast((Intent) this.intentCache.get(ACTION_DRONE_CONFIG_STATE_CHANGED));
    }

    protected void onConnected() {
        Log.d(TAG, "====>>> DRONE CONTROL SERVICE CONNECTED");
        if (this.droneVersion == DroneVersion.UNKNOWN) {
            this.droneVersion = getDroneVersion();
        }
        if (!(this.navdataUpdateThread == null || this.navdataUpdateThread.isAlive())) {
            this.navdataUpdateThread.start();
        }
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_CONNECTION_CHANGED);
        intent.putExtra(EXTRA_CONNECTION_STATE, "connected");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void onCreate() {
        super.onCreate();
        this.executor = Executors.newSingleThreadExecutor();
        this.errorState = ErrorState.NONE;
        this.prevErrorState = ErrorState.NONE;
        this.configLock = new Object();
        this.workerThreadLock = new Object();
        this.navdataThreadLock = new Object();
        this.droneEngine = ARDroneEngine.instance(getApplicationContext());
        this.droneAcademyNavdata = this.droneEngine.getAcademyNavdata();
        this.droneConfiguration = this.droneEngine.getConfiguration();
        this.inputCurr = this.droneEngine.getCurrentInput();
        this.ctrlData = new ControlData(this.inputCurr);
        this.usbVideoDownloader = new UsbVideoDownloader();
        this.usbVideoDownloader.setVideoDownloadProgressListener(this);
        this.wakeLock = ((PowerManager) getSystemService("power")).newWakeLock(6, "DimWakeLock");
        this.wakeLock.acquire();
        this.stopThreads = false;
        this.workerThread = new Thread(this, "Drone Worker Thread");
        this.navdataUpdateThread = new Thread(this.navdataUpdateRunnable, "Navdata Update Thread");
        this.commandQueue = new LinkedList();
        setState(new DisconnectedServiceState(this));
        this.droneEngine.setAcademyMediaListener(this);
        this.workerThread.start();
        this.gallery = new ARDroneMediaGallery(this);
        GPSHelper instance = GPSHelper.getInstance(this);
        if (GPSHelper.isGpsOn(this)) {
            instance.startListening(this);
            this.locationCurrent = GPSHelper.getLastKnownLocation(this);
            requestGPSUpdate();
            Log.d(TAG, "GPS [OK]");
        } else {
            Log.d(TAG, "GPS [DISABLED]. Video will not be tagged.");
        }
        this.mediaDownloaded = new ArrayList();
        initIntents();
        connect();
        try {
            ApplicationSettings applicationSettings = new ApplicationSettings(this);
            Date date = new Date();
            String dataDir = DataTrackUtils.getDataDir(this);
            String str = DataTrackUtils.makeTimeStamp(date, false) + ".xml";
            long appLastOpenedDate = applicationSettings.getAppLastOpenedDate();
            String makeTimeStamp = appLastOpenedDate != 0 ? DataTrackUtils.makeTimeStamp(new Date(appLastOpenedDate), true) : HttpVersions.HTTP_0_9;
            int appOpenedCount = applicationSettings.getAppOpenedCount();
            DataTracker.trackInfoAppStarted(dataDir, str, getDeviceType(), (double) DataTrackUtils.getFreeSize(), makeTimeStamp, appOpenedCount, SystemUtils.getDeviceName(), String.format("Android %s", new Object[]{VERSION.RELEASE}), getPackageName());
            applicationSettings.setAppOpenedCount(appOpenedCount + 1);
            applicationSettings.setAppLastOpenedDate(date.getTime());
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        this.executor.shutdownNow();
        super.onDestroy();
        disconnect();
        if (this.wakeLock != null && this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        Log.d(TAG, "All threads have been stopped");
        stopWorkerThreads();
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__APP_CLOSED);
        Process.killProcess(Process.myPid());
    }

    protected void onDisconnected() {
        synchronized (this.navdataThreadLock) {
            this.navdataThreadLock.notify();
        }
        Log.d(TAG, "====>>> DRONE CONTROL SERVICE DISCONNECTED");
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_CONNECTION_CHANGED);
        intent.putExtra(EXTRA_CONNECTION_STATE, "disconnected");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    protected void onDroneAutoRecordStarted() {
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast((Intent) this.intentCache.get(ACTION_DRONE_AUTORECORD_STARTED));
    }

    protected void onDroneTakeOffCancelled() {
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast((Intent) this.intentCache.get(ACTION_DRONE_TAKEOFF_CANCELLED));
    }

    protected void onDroneUsbRecordStopped() {
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast((Intent) this.intentCache.get(ACTION_DRONE_USB_RECORD_STOPPED));
    }

    protected void onLanded() {
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_FLYING_STATE_CHANGED);
        intent.putExtra(EXTRA_DRONE_FLYING, false);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void onLocationChanged(Location location) {
        Log.d(TAG, "====>onLocationChanged()");
        if (!location.hasAccuracy() || location.getAccuracy() >= 100.0f) {
            Log.d(TAG, "Skipped location value as it doesn't have desired accuracy. Accuracy: " + location.getAccuracy() + " meters");
            Log.d(TAG, "Skipped location value as it doesn't have desired accuracy. Accuracy:hasAltitude " + location.hasAltitude());
            Log.d(TAG, "Skipped location value as it doesn't have desired accuracy. Accuracy:hasAccuracy " + location.hasAccuracy());
            Log.d(TAG, "Skipped location value as it doesn't have desired accuracy. Accuracy:provider " + location.getProvider());
            return;
        }
        this.locationCurrent = location;
        GPSHelper.getInstance(this).stopListening(this);
        requestGPSUpdate();
    }

    public void onLowMemory() {
        Log.w(TAG, "Low memory alert!");
        super.onLowMemory();
    }

    public void onNewMediaIsAvailable(String str) {
        final File file = new File(str);
        if (str.endsWith(".jpg")) {
            try {
                Exif2Interface exif2Interface = new Exif2Interface(file.getAbsolutePath());
                exif2Interface.setAttribute(Tag.IMAGE_DESCRIPTION, file.getParentFile().getName() + URIUtil.SLASH + file.getName());
                exif2Interface.saveAttributes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File mediaDir = getMediaDir();
        final String name = file.getParentFile().getName();
        if (mediaDir != null) {
            File file2 = new File(mediaDir, file.getName());
            MoveFileTask c11794 = new MoveFileTask() {
                protected void onPostExecute(Boolean bool) {
                    if (bool.equals(Boolean.TRUE)) {
                        file.getParentFile().delete();
                        File resultFile = getResultFile();
                        DroneControlService.this.gallery.insertMedia(resultFile, name);
                        Intent intent = (Intent) DroneControlService.this.intentCache.get(DroneControlService.ACTION_NEW_MEDIA_IS_AVAILABLE);
                        intent.putExtra(DroneControlService.EXTRA_MEDIA_PATH, resultFile.getAbsolutePath());
                        LocalBroadcastManager.getInstance(DroneControlService.this.getApplicationContext()).sendBroadcast(intent);
                    }
                }
            };
            if (VERSION.SDK_INT < 11) {
                c11794.execute(new File[]{file, file2});
                return;
            }
            c11794.executeOnExecutor(this.executor, new File[]{file, file2});
        }
    }

    public void onNewMediaToQueue(String str) {
        this.mediaDownloaded.add(str);
    }

    protected void onPaused() {
        if (this.wakeLock != null && this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        Log.d(TAG, "====>>> DRONE CONTROL SERVICE PAUSED");
    }

    public void onProgress(Status status, VideoItem videoItem, float f) {
        switch (status) {
            case OK:
                onNewMediaIsAvailable(getMediaDir().getAbsolutePath() + URIUtil.SLASH + videoItem.userboxName + URIUtil.SLASH + videoItem.videoName);
                break;
        }
        if (this.usbVideoDownloadListener != null) {
            this.usbVideoDownloadListener.onProgress(status, videoItem, f);
        }
    }

    public void onProviderDisabled(String str) {
    }

    public void onProviderEnabled(String str) {
    }

    protected void onPsiChanged(float f) {
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_PSI_CHANGED);
        intent.putExtra(EXTRA_DRONE_PSI, f);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void onQueueComplete() {
        Iterator it = this.mediaDownloaded.iterator();
        while (it.hasNext()) {
            onNewMediaIsAvailable((String) it.next());
        }
        this.mediaDownloaded.clear();
    }

    protected void onResumed() {
        synchronized (this.navdataThreadLock) {
            this.navdataThreadLock.notify();
        }
        if (!(this.wakeLock == null || this.wakeLock.isHeld())) {
            this.wakeLock.acquire();
        }
        Log.d(TAG, "====>>> DRONE CONTROL SERVICE RESUMED");
    }

    protected void onSpeedChanged(double d) {
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_SPEED_CHANGED);
        intent.putExtra(EXTRA_DRONE_SPEED, d);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    protected void onTookOff() {
        Intent intent = (Intent) this.intentCache.get(ACTION_DRONE_FLYING_STATE_CHANGED);
        intent.putExtra(EXTRA_DRONE_FLYING, true);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    protected void onUsbStateChanged(boolean z) {
        this.usbActive = z;
        Intent intent = (Intent) this.intentCache.get(ACTION_USB_STATE_CHANGED);
        intent.putExtra(EXTRA_USB_ACTIVE, z);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void pause() {
        this.currState.pause();
    }

    public boolean record() {
        boolean recordReady = this.droneAcademyNavdata.getRecordReady();
        boolean record = this.droneAcademyNavdata.record(recordReady ? RecordCmd.STOP : RecordCmd.START);
        if (record) {
            Log.d(TAG, "Switching record from state " + recordReady);
            if (recordReady) {
                VideoStageEncodedRecorder.instance().enable(false, 0);
            }
        }
        return record;
    }

    public void requestConfigUpdate() {
        Log.d(TAG, "requestConfigUpdate");
        this.configurationState = ConfigState.CONFIG_STATE_NEEDED;
    }

    public void requestDroneStatus() {
        NavData navData = this.droneEngine.getNavData();
        if (this.prevFlying) {
            onTookOff();
        } else {
            onLanded();
        }
        onFlyingStateChanged(this.prevFlyingState);
        onBatteryStateChanged(navData.getNavDataDemo().getVBatFlyingPercentage());
        double sqrt = Math.sqrt((double) ((navData.getNavDataDemo().getVx() * navData.getNavDataDemo().getVx()) + (navData.getNavDataDemo().getVy() * navData.getNavDataDemo().getVy())));
        onAltitudeChanged(navData.getNavDataDemo().getAltitude());
        onSpeedChanged(sqrt);
        onPsiChanged(navData.getNavDataDemo().getPsi());
        onRecordBufferChanged(this.droneAcademyNavdata.getHDVideoRecordProgress());
        onRecordChanged(this.droneAcademyNavdata.getRecordReady(), this.droneAcademyNavdata.getUsbState(), this.droneAcademyNavdata.getRemainingUsbTime());
        onCameraReadyChanged(this.droneAcademyNavdata.getCameraState());
        onRecordReadyChanged(this.droneAcademyNavdata.getRecordReady());
        onEmergencyStateChanged(this.errorState);
        onGpsFirmwareUpdateParamsChanged(this.prevEphemerisStatus, this.prevFirmwareUpdateState, this.prevFirmwareUpdateProgress);
        onGpsParamsChanged(this.prevGpsPlugged, this.prevGpsActive, this.prevGpsPrecision, this.prevGpsSatsUsed, this.prevLatFused, this.prevLongFused);
        if (navData.getConfigWasDone()) {
            onDroneReady();
        }
    }

    public void requestGPSUpdate() {
        Log.d(TAG, "requestGPSUpdate");
        this.gpsState = ConfigState.CONFIG_STATE_NEEDED;
    }

    public void resetConfigToDefaults() {
        this.droneConfiguration.addEvent(EventKey.INDOOR_EULER_ANGLE_MAX, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.INDOOR_EULER_ANGLE_MAX), null);
        this.droneConfiguration.addEvent(EventKey.INDOOR_CONTROL_VZ_MAX, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.INDOOR_CONTROL_VZ_MAX), null);
        this.droneConfiguration.addEvent(EventKey.INDOOR_CONTROL_YAW, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.INDOOR_CONTROL_YAW), null);
        this.droneConfiguration.addEvent(EventKey.OUTDOOR_EULER_ANGLE_MAX, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.OUTDOOR_EULER_ANGLE_MAX), null);
        this.droneConfiguration.addEvent(EventKey.OUTDOOR_CONTROL_VZ_MAX, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.OUTDOOR_CONTROL_VZ_MAX), null);
        this.droneConfiguration.addEvent(EventKey.OUTDOOR_CONTROL_YAW, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.OUTDOOR_CONTROL_YAW), null);
        this.droneConfiguration.addEvent(EventKey.OUTDOOR, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.OUTDOOR), null);
        this.droneConfiguration.addEvent(EventKey.EULER_ANGLE_MAX, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.EULER_ANGLE_MAX), null);
        this.droneConfiguration.addEvent(EventKey.CONTROL_VZ_MAX, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.CONTROL_VZ_MAX), null);
        this.droneConfiguration.addEvent(EventKey.CONTROL_YAW, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.CONTROL_YAW), null);
        this.droneConfiguration.addEvent(EventKey.CONTROL_IPHONE_TILT, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.CONTROL_IPHONE_TILT), null);
        this.droneConfiguration.addEvent(EventKey.FLIGHT_WITHOUT_SHELL, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.FLIGHT_WITHOUT_SHELL), null);
        this.droneConfiguration.addEvent(EventKey.ALTITUDE_MAX, this.droneConfiguration.getValue(Source.CONTROL_CONFIG_DEFAULT, EventKey.ALTITUDE_MAX), null);
    }

    public void resume() {
        this.currState.resume();
        setPitch(0.0f);
        setRoll(0.0f);
        setGaz(0.0f);
        setYaw(0.0f);
        setDeviceOrientation(0, 0);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r5 = this;
    L_0x0000:
        r0 = r5.stopThreads;
        if (r0 != 0) goto L_0x001d;
    L_0x0004:
        r1 = r5.commandQueueLock;
        monitor-enter(r1);
        r0 = r5.commandQueue;	 Catch:{ InterruptedException -> 0x001e }
        r0 = r0.isEmpty();	 Catch:{ InterruptedException -> 0x001e }
        if (r0 == 0) goto L_0x0018;
    L_0x000f:
        r0 = r5.stopThreads;	 Catch:{ InterruptedException -> 0x001e }
        if (r0 != 0) goto L_0x0018;
    L_0x0013:
        r0 = r5.commandQueueLock;	 Catch:{ InterruptedException -> 0x001e }
        r0.wait();	 Catch:{ InterruptedException -> 0x001e }
    L_0x0018:
        r0 = r5.stopThreads;	 Catch:{ all -> 0x0021 }
        if (r0 == 0) goto L_0x0024;
    L_0x001c:
        monitor-exit(r1);	 Catch:{ all -> 0x0021 }
    L_0x001d:
        return;
    L_0x001e:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0021 }
        goto L_0x001d;
    L_0x0021:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0021 }
        throw r0;
    L_0x0024:
        monitor-exit(r1);	 Catch:{ all -> 0x0021 }
        r1 = r5.commandQueue;
        monitor-enter(r1);
        r0 = r5.commandQueue;	 Catch:{ all -> 0x0071 }
        r0 = r0.poll();	 Catch:{ all -> 0x0071 }
        r0 = (com.parrot.freeflight.service.commands.DroneServiceCommand) r0;	 Catch:{ all -> 0x0071 }
        monitor-exit(r1);	 Catch:{ all -> 0x0071 }
        if (r0 == 0) goto L_0x0000;
    L_0x0033:
        r2 = r5.workerThreadLock;	 Catch:{ Exception -> 0x003e }
        monitor-enter(r2);	 Catch:{ Exception -> 0x003e }
        r0.execute();	 Catch:{ all -> 0x003b }
        monitor-exit(r2);	 Catch:{ all -> 0x003b }
        goto L_0x0000;
    L_0x003b:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x003b }
        throw r1;	 Catch:{ Exception -> 0x003e }
    L_0x003e:
        r1 = move-exception;
        r2 = "DroneControlService";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Commang ";
        r3 = r3.append(r4);
        r0 = r0.getClass();
        r0 = r0.getSimpleName();
        r0 = r3.append(r0);
        r3 = " has failed with exception ";
        r0 = r0.append(r3);
        r3 = r1.toString();
        r0 = r0.append(r3);
        r0 = r0.toString();
        android.util.Log.e(r2, r0);
        r1.printStackTrace();
        goto L_0x0000;
    L_0x0071:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0071 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.parrot.freeflight.service.DroneControlService.run():void");
    }

    public void setAutoRecordEnabled(boolean z) {
        this.droneAcademyNavdata.setAutorecord(z);
    }

    public void setDeviceOrientation(int i, int i2) {
        ControlData controlData;
        this.ctrlData.iphone_psi = (float) i;
        if (this.ctrlData.iphone_psi > BitmapDescriptorFactory.HUE_CYAN) {
            controlData = this.ctrlData;
            controlData.iphone_psi -= 360.0f;
        }
        controlData = this.ctrlData;
        controlData.iphone_psi /= BitmapDescriptorFactory.HUE_CYAN;
        this.ctrlData.iphone_psi_accuracy = (float) i2;
        if (!this.magnetoEnabled || this.ctrlData.iphone_psi_accuracy < 0.0f) {
            this.ctrlData.setCommandFlag(ProgressiveFlag.ARDRONE_MAGNETO_CMD_ENABLE.ordinal(), false);
        } else {
            this.ctrlData.setCommandFlag(ProgressiveFlag.ARDRONE_MAGNETO_CMD_ENABLE.ordinal(), true);
        }
    }

    public void setDroneDebugListener(DroneDebugListener droneDebugListener) {
        this.debugListener = droneDebugListener;
    }

    public void setGaz(float f) {
        this.ctrlData.inputGaz(f);
        this.ctrlData.sendControls();
    }

    public void setMagnetoEnabled(boolean z) {
        this.magnetoEnabled = z;
    }

    public void setPitch(float f) {
        this.ctrlData.inputPitch(f);
        this.ctrlData.sendControls();
    }

    public void setProgressiveCommandCombinedYawEnabled(boolean z) {
        this.ctrlData.setCommandFlag(DroneProgressiveCommandFlag.ARDRONE_PROGRESSIVE_CMD_COMBINED_YAW_ACTIVE.ordinal(), z);
    }

    public void setProgressiveCommandEnabled(boolean z) {
        this.ctrlData.setCommandFlag(DroneProgressiveCommandFlag.ARDRONE_PROGRESSIVE_CMD_ENABLE.ordinal(), z);
    }

    public void setRoll(float f) {
        this.ctrlData.inputRoll(f);
        this.ctrlData.sendControls();
    }

    protected void setState(ServiceStateBase serviceStateBase) {
        if (!(this.currState == null || serviceStateBase == null)) {
            Log.d(TAG, "== PREV STATE: " + this.currState.getStateName() + " NEW STATE: " + serviceStateBase.getStateName());
        }
        if (this.currState != null) {
            this.currState.onFinalize();
        }
        this.currState = serviceStateBase;
        if (serviceStateBase != null) {
            serviceStateBase.onPrepare();
        }
    }

    public void setYaw(float f) {
        this.ctrlData.inputYaw(f);
        this.ctrlData.sendControls();
    }

    protected void startCommand(DroneServiceCommand droneServiceCommand) {
        synchronized (this.commandQueue) {
            this.commandQueue.add(droneServiceCommand);
        }
        synchronized (this.commandQueueLock) {
            this.commandQueueLock.notify();
        }
    }

    protected void stopWorkerThreads() {
        this.stopThreads = true;
        synchronized (this.navdataThreadLock) {
            this.navdataThreadLock.notify();
        }
        try {
            this.navdataUpdateThread.join(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this.commandQueueLock) {
            this.commandQueueLock.notify();
        }
    }

    public void switchCamera() {
        int i = this.videoChannel + 1;
        this.videoChannel = i;
        if (i == ArdroneVideoChannel.values().length - 1) {
            this.videoChannel = 0;
        }
        this.droneConfiguration.addEvent(EventKey.VIDEO_CHANNEL, Integer.valueOf(this.videoChannel), null);
    }

    public void takePhoto() {
        if (!this.droneAcademyNavdata.screenshot()) {
            Log.w(TAG, "Screenshot has not been taken");
        }
    }

    public void triggerConfigUpdate() {
        this.configurationState = ConfigState.CONFIG_STATE_NEEDED;
    }

    public void triggerEmergency() {
        if (!this.droneAcademyNavdata.emergency()) {
            Log.w(TAG, "Trigger emergency skipped");
        }
    }

    public void triggerTakeOff() {
        if (!this.droneAcademyNavdata.takeOff()) {
            Log.w(TAG, "Takeoff skipped");
        }
    }

    public void usbCancellAllDownloads() {
        this.usbVideoDownloader.cancelAllDownloads();
    }

    public boolean usbDeleteVideo(VideoItem videoItem) {
        return this.usbVideoDownloader.deleteItem(videoItem);
    }

    public boolean usbDownloadVideo(VideoItem videoItem, UsbVideoDownloadProgressListener usbVideoDownloadProgressListener) {
        if (videoItem == null) {
            throw new IllegalArgumentException();
        }
        this.usbVideoDownloadListener = usbVideoDownloadProgressListener;
        File mediaDir = getMediaDir();
        if (mediaDir == null) {
            return false;
        }
        File file = new File(mediaDir, videoItem.userboxName);
        if (!file.exists()) {
            file.mkdirs();
        }
        this.usbVideoDownloader.downloadItem(videoItem, file + URIUtil.SLASH + videoItem.videoName);
        return true;
    }
}
