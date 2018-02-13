package com.parrot.ardronetool;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.parrot.ardronetool.Configuration.EventKey;
import com.parrot.ardronetool.Configuration.Source;
import com.parrot.ardronetool.NavData.Options;
import com.parrot.ardronetool.academynavdata.ARDroneAcademyNavdata;
import com.parrot.ardronetool.ui.ARDroneInput;
import com.parrot.ardronetool.utils.DeviceCapabilitiesUtils;
import com.parrot.ardronetool.video.VideoStage;
import com.parrot.ardronetool.vlib.VideoCodecType;
import com.parrot.freeflight.service.DroneConfig;
import java.io.File;

public class ARDroneEngine {
    public static final String ARDRONE_ENGINE_CONNECTED_ACTION = "drone.proxy.connected.action";
    public static final String ARDRONE_ENGINE_DISCONNECTED_ACTION = "drone.proxy.disconnected.action";
    public static final int REPAIR_FTP_PORT = 21;
    private static final String TAG = ARDroneEngine.class.getSimpleName();
    public static final int TELNET_PORT = 23;
    private static ARDroneEngine instance;
    private AcademyMediaListener academyMediaListener;
    private ARDroneAcademyNavdata academyNavdata = ARDroneAcademyNavdata.instance();
    private Context applicationContext;
    private DroneConfig config;
    private Configuration configuration = Configuration.instance();
    private ARDroneInput inputCurrent = ARDroneInput.instance();
    private NavData navData = new NavData();
    private NavDataClient navdataClient = new NavDataClient();
    private VideoStage videoStageCurrent = new VideoStage();

    public enum DroneProgressiveCommandFlag {
        ARDRONE_PROGRESSIVE_CMD_ENABLE,
        ARDRONE_PROGRESSIVE_CMD_COMBINED_YAW_ACTIVE,
        ARDRONE_MAGNETO_CMD_ENABLE
    }

    public enum EVideoRecorderCapability {
        NOT_SUPPORTED,
        VIDEO_360P,
        VIDEO_720P
    }

    public enum ErrorState {
        NONE,
        NAVDATA_CONNECTION,
        START_NOT_RECEIVED,
        EMERGENCY_CUTOUT,
        EMERGENCY_MOTORS,
        EMERGENCY_CAMERA,
        EMERGENCY_PIC_WATCHDOG,
        EMERGENCY_PIC_VERSION,
        EMERGENCY_ANGLE_OUT_OF_RANGE,
        EMERGENCY_VBAT_LOW,
        EMERGENCY_USER_EL,
        EMERGENCY_ULTRASOUND,
        EMERGENCY_UNKNOWN,
        ALERT_CAMERA,
        ALERT_VBAT_LOW,
        ALERT_ULTRASOUND,
        ALERT_VISION;

        public static boolean isEmergency(ErrorState errorState) {
            switch (errorState) {
                case EMERGENCY_CUTOUT:
                case EMERGENCY_MOTORS:
                case EMERGENCY_CAMERA:
                case EMERGENCY_PIC_WATCHDOG:
                case EMERGENCY_PIC_VERSION:
                case EMERGENCY_ANGLE_OUT_OF_RANGE:
                case EMERGENCY_VBAT_LOW:
                case EMERGENCY_USER_EL:
                case EMERGENCY_ULTRASOUND:
                case EMERGENCY_UNKNOWN:
                    return true;
                default:
                    return false;
            }
        }
    }

    private ARDroneEngine(Context context) {
        this.applicationContext = context;
    }

    private void initDefaultConfig() {
        this.configuration.setValue(Source.APPLICATION_CONFIG, EventKey.NAVDATA_DEMO, true);
        this.configuration.setValue(Source.APPLICATION_CONFIG, EventKey.NAVDATA_OPTIONS, ((((((Options.mask(Options.NAVDATA_DEMO_TAG) | Options.mask(Options.NAVDATA_VISION_DETECT_TAG)) | Options.mask(Options.NAVDATA_GAMES_TAG)) | Options.mask(Options.NAVDATA_MAGNETO_TAG)) | Options.mask(Options.NAVDATA_HDVIDEO_STREAM_TAG)) | Options.mask(Options.NAVDATA_WIFI_TAG)) | Options.mask(Options.NAVDATA_PWM_TAG)) | Options.mask(Options.NAVDATA_GPS_TAG));
        if (ARDroneVersion.isArDrone2()) {
            this.configuration.setValue(Source.APPLICATION_CONFIG, EventKey.CODEC_FPS, 25);
            this.configuration.setValue(Source.APPLICATION_CONFIG, EventKey.MAX_BITRATE, 1500);
            this.configuration.setValue(Source.APPLICATION_CONFIG, EventKey.VIDEO_CODEC, (int) VideoCodecType.H264_360P_CODEC);
            this.configuration.setValue(Source.APPLICATION_CONFIG, EventKey.BITRATE_CTRL_MODE, 1);
            switch (DeviceCapabilitiesUtils.getMaxSupportedVideoRes(this.applicationContext)) {
                case VIDEO_720P:
                    this.academyNavdata.setWifiRecordCodec(VideoCodecType.MP4_360P_H264_720P_CODEC);
                    return;
                case VIDEO_360P:
                    this.academyNavdata.setWifiRecordCodec(VideoCodecType.MP4_360P_H264_360P_CODEC);
                    return;
                case NOT_SUPPORTED:
                    this.academyNavdata.setWifiRecordCodec(0);
                    return;
                default:
                    Log.w(TAG, "Unknown device capability");
                    return;
            }
        }
        this.configuration.setValue(Source.APPLICATION_CONFIG, EventKey.VIDEO_CODEC, 64);
        this.configuration.setValue(Source.APPLICATION_CONFIG, EventKey.BITRATE_CTRL_MODE, 1);
    }

    public static ARDroneEngine instance(Context context) {
        if (instance == null) {
            instance = new ARDroneEngine(context);
        }
        return instance;
    }

    public native boolean academyConnect(String str, String str2, AcademyDelegate academyDelegate);

    public native boolean academyDisconnect();

    public ARDroneAcademyNavdata getAcademyNavdata() {
        return this.academyNavdata;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public ARDroneInput getCurrentInput() {
        return this.inputCurrent;
    }

    public VideoStage getCurrentVideoStage() {
        return this.videoStageCurrent;
    }

    public NavData getNavData() {
        this.navData.refreshNavdata();
        return this.navData;
    }

    public NavDataClient getNavDataClient() {
        return this.navdataClient;
    }

    protected void onConnected() {
        this.navData.reset();
        initDefaultConfig();
        LocalBroadcastManager.getInstance(this.applicationContext).sendBroadcast(new Intent(ARDRONE_ENGINE_CONNECTED_ACTION));
    }

    protected void onNewMediaReady(String str, boolean z) {
        if (str != null && str.length() != 0) {
            Log.d(TAG, "New media file available: " + str);
            Object obj = null;
            File file = new File(str);
            if (file.exists() && file.isFile()) {
                if (0 < file.length()) {
                    obj = 1;
                } else {
                    Log.d(TAG, "New media has a size of zero --> delete it");
                    file.delete();
                }
            } else if (!file.exists()) {
                Log.w(TAG, "File " + str + " doesn't exists but reported as new media");
            }
            if (obj != null && this.academyMediaListener != null) {
                if (z) {
                    this.academyMediaListener.onNewMediaToQueue(str);
                } else {
                    this.academyMediaListener.onNewMediaIsAvailable(str);
                }
            }
        } else if (this.academyMediaListener != null) {
            this.academyMediaListener.onQueueComplete();
        }
    }

    public native void pause();

    public native void resume();

    public void setAcademyMediaListener(AcademyMediaListener academyMediaListener) {
        this.academyMediaListener = academyMediaListener;
    }

    public native void start(String str, String str2, String str3, String str4, int i, String str5);

    public native void stop();
}
