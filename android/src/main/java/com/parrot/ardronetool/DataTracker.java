package com.parrot.ardronetool;

import com.parrot.ardronetool.tracking.DEVICE_TYPE_ENUM;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;

public class DataTracker {
    public static void trackInfoAppStarted(String str, String str2, DEVICE_TYPE_ENUM device_type_enum, double d, String str3, int i, String str4, String str5, String str6) {
        DataTrackerStub.trackInfoAppStarted(str, str2, device_type_enum.getValue(), d, str3, i, str4, str5, str6);
    }

    public static void trackInfoInt(TRACK_KEY_ENUM track_key_enum, int i) {
        DataTrackerStub.trackInfoInt(track_key_enum.getValue(), i);
    }

    public static void trackInfoInt3(TRACK_KEY_ENUM track_key_enum, int i, int i2, int i3) {
        DataTrackerStub.trackInfoInt3(track_key_enum.getValue(), i, i2, i3);
    }

    public static void trackInfoStr(TRACK_KEY_ENUM track_key_enum, String str) {
        DataTrackerStub.trackInfoStr(track_key_enum.getValue(), str);
    }

    public static void trackInfoVoid(TRACK_KEY_ENUM track_key_enum) {
        DataTrackerStub.trackInfoVoid(track_key_enum.getValue());
    }
}
