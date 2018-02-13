package com.parrot.ardronetool;

public class NavData {
    private final NavDataDemo navDataDemo = new NavDataDemo();
    private final NavDataGps navDataGps = new NavDataGps();
    private final NavDataPwm navDataPwm = new NavDataPwm();

    public enum Options {
        NAVDATA_DEMO_TAG,
        NAVDATA_TIME_TAG,
        NAVDATA_RAW_MEASURES_TAG,
        NAVDATA_PHYS_MEASURES_TAG,
        NAVDATA_GYROS_OFFSETS_TAG,
        NAVDATA_EULER_ANGLES_TAG,
        NAVDATA_REFERENCES_TAG,
        NAVDATA_TRIMS_TAG,
        NVADATA_RC_REFERENCES,
        NAVDATA_PWM_TAG,
        NAVDATA_ALTITUDE_TAG,
        NAVDATA_VISION_RAW_TAG,
        NAVDATA_VISION_OF_TAG,
        NAVDATA_VISION_TAG,
        NAVDATA_VISION_PERF_TAG,
        NAVDATA_TRACKERS_SEND_TAG,
        NAVDATA_VISION_DETECT_TAG,
        NAVDATA_WATCHDOG_TAG,
        NAVDATA_ADC_DATA_FRAME_TAG,
        NAVDATA_VIDEO_STREAM_TAG,
        NAVDATA_GAMES_TAG,
        NAVDATA_PRESSURE_RAW_TAG,
        NAVDATA_MAGNETO_TAG,
        NAVDATA_WIND_TAG,
        NAVDATA_NAVDATA_KALMAN_PRESSURE_TAG,
        NAVDATA_HDVIDEO_STREAM_TAG,
        NAVDATA_WIFI_TAG,
        NAVDATA_GPS_TAG,
        NAVDATA_CAMERA_SETTINGS_TAG,
        NAVDATA_HOST_TAG;

        public static int mask(Options options) {
            return 1 << options.ordinal();
        }
    }

    public NavData() {
        initialize();
    }

    private final native void initialize();

    public final native long getArdroneState();

    public final native boolean getConfigWasDone();

    public NavDataDemo getNavDataDemo() {
        return this.navDataDemo;
    }

    public NavDataGps getNavDataGps() {
        return this.navDataGps;
    }

    public NavDataPwm getNavDataPwm() {
        return this.navDataPwm;
    }

    final native void refreshNavdata();

    public final native void reset();
}
