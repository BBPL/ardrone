package com.parrot.ardronetool;

public class NavDataGps {

    public static final class GpsEphemerisStatus {
        public static final int EPHEMERIS_IN_USE = 2;
        public static final int EPHEMERIS_PRESENT = 1;
        public static final int EPHEMERIS_UPLOADING = 4;
        public static final int NEW_EPHEMERIS_CURRENT_FILE_IS_NEWER = 64;
        public static final int NEW_EPHEMERIS_INTERNAL_DRONE_ERROR = 128;
        public static final int NEW_EPHEMERIS_INVALID_FORMAT = 32;
        public static final int NEW_EPHEMERIS_INVALID_MD5 = 16;
        public static final int NEW_EPHEMERIS_NOT_FOUND = 256;
        public static final int NEW_EPHEMERIS_RECEIVED = 8;
        public static final int NEW_EPHEMERIS_SUCCESS = 512;
    }

    public static final class GpsFirmwareStatus {
        public static final int FIRMWARE_AVAILABLE_TO_UPLOAD = 2;
        public static final int FIRMWARE_ERASING = 16;
        public static final int FIRMWARE_PROCESSING = 8;
        public static final int FIRMWARE_UPLOADING = 32;
        public static final int FIRMWARE_UP_TO_DATE = 1;
        public static final int NEW_FIRMWARE_SUCCESS = 128;
        public static final int NEW_FIRMWARE_UPLOAD_ERROR = 64;
        public static final int NO_FIRMWARE_PRESENT = 4;
    }

    NavDataGps() {
    }

    public final native int getEphemerisStatus();

    public final native int getFirmwareStatus();

    public final native float getGpsPrecision();

    public final native int getGpsSatsUsed();

    public final native double getLatFused();

    public final native double getLongFused();

    public final native boolean isGpsActive();

    public final native boolean isGpsPlugged();
}
