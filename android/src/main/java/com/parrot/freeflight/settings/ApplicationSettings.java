package com.parrot.freeflight.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.parrot.freeflight.service.DroneControlService.DroneFlipDirection;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

public class ApplicationSettings {
    private static final int DEFAULT_CONTROL_MODE = ControlMode.ACCELERO_MODE.ordinal();
    public static final boolean DEFAULT_GPS_FIRMWARE_UPDATE_ENABLED = true;
    public static final int DEFAULT_INTERFACE_OPACITY = 50;
    public static final boolean DEFAULT_LOOPING_ENABLED = false;
    private static final boolean DEFAULT_MAGNETO_ENABLED = false;
    public static final String EDGE_3G_DATA_SYNC_CHANGED = "com.parrot.edge_3g_data_sync.action.changed";
    public static final String EXTRA_3G_DATA_SYNC_STATE = "com.parrot.edge_3g_data_sync.extra.state";
    private static final String[] GPU_BLACK_LIST_GPU_FROYO = new String[]{"Adreno"};
    public static final int INTERFACE_OPACITY_MAX = 100;
    public static final int INTERFACE_OPACITY_MIN = 0;
    public static final long MEMORY_USAGE = 10;
    private static final String NAME = "Preferences";
    private static final String TAG = "ApplicationSettings";
    private Map<Integer, String[]> blacklist = new Hashtable();
    private Context context;
    private SharedPreferences prefs;
    private boolean useOpenGL;

    public enum ControlMode {
        NORMAL_MODE,
        ACCELERO_MODE,
        ACE_MODE
    }

    public static class Coords {
        public final float bearing;
        public final float latitude;
        public final float longitude;
        public final float tilt;
        public final float zoom;

        public Coords(float f, float f2, float f3, float f4, float f5) {
            this.latitude = f;
            this.longitude = f2;
            this.zoom = f3;
            this.tilt = f4;
            this.bearing = f5;
        }

        public static Coords fromString(String str) {
            String[] split = str.split(",");
            if (split != null && split.length == 5) {
                try {
                    return new Coords(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3]), Float.parseFloat(split[4]));
                } catch (NumberFormatException e) {
                }
            }
            return null;
        }
    }

    public enum EAppSettingProperty {
        FIRST_LAUNCH_PROP("first_launch"),
        LEFT_HANDED_PROP("left_handed"),
        FORCE_COMBINED_CTRL_PROP("force_combined_control"),
        CONTROL_MODE_PROP("control_mode"),
        INTERFACE_OPACITY_PROP("interface_opacity"),
        MAGNETO_ENABLED_PROP("magneto_enabled"),
        LOOPING_ENABLED_PROP("looping_enabled"),
        ASK_FOR_GPS("ask_for_gps"),
        TIMES_APP_ASKED_USER_TO_ALLOW_GPS_LOCATION("times_app_asked_user_to_allow_gps_location"),
        TIMES_APP_ASKED_USER_TO_ALLOW_AUTO_UPLOAD("times_app_asked_user_to_allow_auto_upload"),
        AUTORECORD_PROP("autorecord"),
        GPS_FIRMWARE_UPDATE_PROP("gps_firmware_update"),
        FLIP_ORIENTATION_PROP("flip_orientation"),
        GOOGLE_ACCOUNT_NAME_PROP("pref_google_account"),
        YOUTUBE_VIDEO_CATEGORY_ID_PROP("youtube_video_category"),
        AUTO_UPLOAD_VIDEO_PROP("auto_upload_video"),
        AUTO_UPLOAD_PHOTO_PROP("auto_upload_photo"),
        EDGE_3G_DATA_SYNC_PROP("edge_3g_data_sync"),
        TRACKING_DLG_SHOWED("tracking_dlg_showed"),
        TRACKING_ENABLED("tracking_enabled"),
        RATING_REMINDER_FIRST_USE_DATE("rating_reminder_first_use_date"),
        RATING_REMINDER_USE_COUNT("rating_reminder_use_count"),
        RATING_REMINDER_DECLINED_TO_RATE("rating_reminder_declined_to_rate"),
        RATING_REMINDER_RATED_CURRENT_VERSION("rating_reminder_rated_current_version"),
        RATING_REMINDER_REMINDER_REQUESTDATE("rating_reminder_reminder_requestdate"),
        RATING_REMINDER_CURRENT_VERSION("rating_reminder_current_version"),
        APP_OPENED_COUNT("app_opened_count"),
        APP_LAST_OPENED_DATE("app_last_opened_date"),
        MAP_PILOTING_LAST_COORDS("map_go_last_coords"),
        MAP_STORING_LAST_COORDS("map_storing_last_coords");
        
        private final String propname;

        private EAppSettingProperty(String str) {
            this.propname = str;
        }

        public String toString() {
            return this.propname;
        }
    }

    public ApplicationSettings(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(NAME, 0);
        this.blacklist.put(Integer.valueOf(8), GPU_BLACK_LIST_GPU_FROYO);
    }

    public void appAskedUserToAllowAutoUpload() {
        saveProperty(EAppSettingProperty.TIMES_APP_ASKED_USER_TO_ALLOW_AUTO_UPLOAD, getTimesAppAskedUserToAllowAutoUpload() + 1);
    }

    public void appAskedUserToAllowGPSLocation() {
        saveProperty(EAppSettingProperty.TIMES_APP_ASKED_USER_TO_ALLOW_GPS_LOCATION, getTimesAppAskedUserToAllowGPSLocation() + 1);
    }

    @SuppressLint({"NewApi"})
    protected void applyChanges(Editor editor) {
        if (VERSION.SDK_INT >= 9) {
            editor.apply();
        } else if (!editor.commit()) {
            Log.w(TAG, "Can't save properties. Can't commit.");
        }
    }

    public long getAppLastOpenedDate() {
        return getProperty(EAppSettingProperty.APP_LAST_OPENED_DATE, 0);
    }

    public int getAppOpenedCount() {
        return getProperty(EAppSettingProperty.APP_OPENED_COUNT, 0);
    }

    public ControlMode getControlMode() {
        return ControlMode.values()[getProperty(EAppSettingProperty.CONTROL_MODE_PROP, DEFAULT_CONTROL_MODE)];
    }

    public DroneFlipDirection getFlipDirection() {
        return DroneFlipDirection.valueOf(getProperty(EAppSettingProperty.FLIP_ORIENTATION_PROP, DroneFlipDirection.LEFT.name()));
    }

    public String getGoogleAccountName() {
        return getProperty(EAppSettingProperty.GOOGLE_ACCOUNT_NAME_PROP, (String) null);
    }

    public String[] getGpuBlackList(int i) {
        return this.blacklist.containsKey(Integer.valueOf(i)) ? (String[]) this.blacklist.get(Integer.valueOf(i)) : null;
    }

    public int getInterfaceOpacity() {
        return getProperty(EAppSettingProperty.INTERFACE_OPACITY_PROP, 50);
    }

    public Coords getMapPilotingLastCoords() {
        String property = getProperty(EAppSettingProperty.MAP_PILOTING_LAST_COORDS, null);
        return property != null ? Coords.fromString(property) : null;
    }

    public Coords getMapStoringLastCoords() {
        String property = getProperty(EAppSettingProperty.MAP_STORING_LAST_COORDS, null);
        return property != null ? Coords.fromString(property) : null;
    }

    protected int getProperty(EAppSettingProperty eAppSettingProperty, int i) {
        return this.prefs.getInt(eAppSettingProperty.toString(), i);
    }

    protected long getProperty(EAppSettingProperty eAppSettingProperty, long j) {
        return this.prefs.getLong(eAppSettingProperty.toString(), j);
    }

    protected String getProperty(EAppSettingProperty eAppSettingProperty, String str) {
        return this.prefs.getString(eAppSettingProperty.toString(), str);
    }

    protected boolean getProperty(EAppSettingProperty eAppSettingProperty, boolean z) {
        return this.prefs.getBoolean(eAppSettingProperty.toString(), z);
    }

    public String getRatingReminderCurrentVersion() {
        return getProperty(EAppSettingProperty.RATING_REMINDER_CURRENT_VERSION, null);
    }

    public long getRatingReminderFirstUseDate() {
        return getProperty(EAppSettingProperty.RATING_REMINDER_FIRST_USE_DATE, 0);
    }

    public long getRatingReminderReminderRequestDate() {
        return getProperty(EAppSettingProperty.RATING_REMINDER_REMINDER_REQUESTDATE, 0);
    }

    public int getRatingReminderUseCount() {
        return getProperty(EAppSettingProperty.RATING_REMINDER_USE_COUNT, 0);
    }

    public int getTimesAppAskedUserToAllowAutoUpload() {
        return getProperty(EAppSettingProperty.TIMES_APP_ASKED_USER_TO_ALLOW_AUTO_UPLOAD, 0);
    }

    public int getTimesAppAskedUserToAllowGPSLocation() {
        return getProperty(EAppSettingProperty.TIMES_APP_ASKED_USER_TO_ALLOW_GPS_LOCATION, 0);
    }

    public String getYouTubeVideoCategoryId() {
        return getProperty(EAppSettingProperty.YOUTUBE_VIDEO_CATEGORY_ID_PROP, (String) null);
    }

    public boolean is3gEdgeDataSyncEnabled() {
        return getProperty(EAppSettingProperty.EDGE_3G_DATA_SYNC_PROP, false);
    }

    public boolean isAbsoluteControlEnabled() {
        return getProperty(EAppSettingProperty.MAGNETO_ENABLED_PROP, false);
    }

    public boolean isAskForGPS() {
        return getProperty(EAppSettingProperty.ASK_FOR_GPS, true);
    }

    public boolean isAutoPhotoUploadEnabled() {
        return getProperty(EAppSettingProperty.AUTO_UPLOAD_PHOTO_PROP, false);
    }

    public boolean isAutoVideoUploadEnabled() {
        return getProperty(EAppSettingProperty.AUTO_UPLOAD_VIDEO_PROP, false);
    }

    public boolean isAutorecordEnabled() {
        return getProperty(EAppSettingProperty.AUTORECORD_PROP, false);
    }

    @Deprecated
    public boolean isCombinedControlForced() {
        return getProperty(EAppSettingProperty.FORCE_COMBINED_CTRL_PROP, false);
    }

    public boolean isFirstLaunch() {
        return getProperty(EAppSettingProperty.FIRST_LAUNCH_PROP, true);
    }

    public boolean isFlipEnabled() {
        return getProperty(EAppSettingProperty.LOOPING_ENABLED_PROP, false);
    }

    public boolean isGpsFirmwareUpdateEnabled() {
        return getProperty(EAppSettingProperty.GPS_FIRMWARE_UPDATE_PROP, true);
    }

    public boolean isLeftHanded() {
        return getProperty(EAppSettingProperty.LEFT_HANDED_PROP, false);
    }

    public boolean isRatingReminderDeclinedToRate() {
        return getProperty(EAppSettingProperty.RATING_REMINDER_DECLINED_TO_RATE, false);
    }

    public boolean isRatingReminderRatedCurrentVersion() {
        return getProperty(EAppSettingProperty.RATING_REMINDER_RATED_CURRENT_VERSION, false);
    }

    public boolean isTrackingDlgShowed() {
        return getProperty(EAppSettingProperty.TRACKING_DLG_SHOWED, false);
    }

    public boolean isTrackingEnabled() {
        return getProperty(EAppSettingProperty.TRACKING_ENABLED, true);
    }

    @Deprecated
    public boolean isUseOpenGL() {
        return this.useOpenGL;
    }

    protected void saveProperty(EAppSettingProperty eAppSettingProperty, int i) {
        Editor edit = this.prefs.edit();
        edit.putInt(eAppSettingProperty.toString(), i);
        applyChanges(edit);
    }

    protected void saveProperty(EAppSettingProperty eAppSettingProperty, long j) {
        Editor edit = this.prefs.edit();
        edit.putLong(eAppSettingProperty.toString(), j);
        applyChanges(edit);
    }

    protected void saveProperty(EAppSettingProperty eAppSettingProperty, String str) {
        Editor edit = this.prefs.edit();
        edit.putString(eAppSettingProperty.toString(), str);
        applyChanges(edit);
    }

    protected void saveProperty(EAppSettingProperty eAppSettingProperty, boolean z) {
        Editor edit = this.prefs.edit();
        edit.putBoolean(eAppSettingProperty.toString(), z);
        applyChanges(edit);
    }

    public void set3gEdgeDataSyncEnabled(boolean z) {
        saveProperty(EAppSettingProperty.EDGE_3G_DATA_SYNC_PROP, z);
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this.context.getApplicationContext());
        Intent intent = new Intent(EDGE_3G_DATA_SYNC_CHANGED);
        intent.putExtra(EXTRA_3G_DATA_SYNC_STATE, z);
        instance.sendBroadcast(intent);
    }

    public void setAbsoluteControlEnabled(boolean z) {
        saveProperty(EAppSettingProperty.MAGNETO_ENABLED_PROP, z);
    }

    public void setAppLastOpenedDate(long j) {
        saveProperty(EAppSettingProperty.APP_LAST_OPENED_DATE, j);
    }

    public void setAppOpenedCount(int i) {
        saveProperty(EAppSettingProperty.APP_OPENED_COUNT, i);
    }

    public void setAskForGPS(boolean z) {
        saveProperty(EAppSettingProperty.ASK_FOR_GPS, z);
    }

    public void setAutoPhotoUploadEnabled(boolean z) {
        saveProperty(EAppSettingProperty.AUTO_UPLOAD_PHOTO_PROP, z);
    }

    public void setAutoVideoUploadEnabled(boolean z) {
        saveProperty(EAppSettingProperty.AUTO_UPLOAD_VIDEO_PROP, z);
    }

    public void setAutorecordEnabled(boolean z) {
        saveProperty(EAppSettingProperty.AUTORECORD_PROP, z);
    }

    public void setControlMode(ControlMode controlMode) {
        saveProperty(EAppSettingProperty.CONTROL_MODE_PROP, controlMode.ordinal());
    }

    public void setDroneFlipDirection(DroneFlipDirection droneFlipDirection) {
        saveProperty(EAppSettingProperty.FLIP_ORIENTATION_PROP, droneFlipDirection.name());
    }

    public void setFirstLaunch(boolean z) {
        saveProperty(EAppSettingProperty.FIRST_LAUNCH_PROP, z);
    }

    public void setFlipEnabled(boolean z) {
        saveProperty(EAppSettingProperty.LOOPING_ENABLED_PROP, z);
    }

    @Deprecated
    public void setForceCombinedControl(boolean z) {
        saveProperty(EAppSettingProperty.FORCE_COMBINED_CTRL_PROP, z);
    }

    public void setGoogleAccountName(String str) {
        saveProperty(EAppSettingProperty.GOOGLE_ACCOUNT_NAME_PROP, str);
    }

    public void setGpsFirmwareUpdateEnabled(boolean z) {
        saveProperty(EAppSettingProperty.GPS_FIRMWARE_UPDATE_PROP, z);
    }

    public void setInterfaceOpacity(int i) {
        if (i < 0 || i > 100) {
            throw new IllegalArgumentException();
        }
        saveProperty(EAppSettingProperty.INTERFACE_OPACITY_PROP, i);
    }

    public void setLeftHanded(boolean z) {
        saveProperty(EAppSettingProperty.LEFT_HANDED_PROP, z);
    }

    public void setMapPilotingLastCoords(float f, float f2, float f3, float f4, float f5) {
        saveProperty(EAppSettingProperty.MAP_PILOTING_LAST_COORDS, String.format(Locale.ENGLISH, "%f,%f,%f,%f,%f", new Object[]{Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), Float.valueOf(f5)}));
    }

    public void setMapStoringLastCoords(float f, float f2, float f3, float f4, float f5) {
        saveProperty(EAppSettingProperty.MAP_STORING_LAST_COORDS, String.format(Locale.ENGLISH, "%f,%f,%f,%f,%f", new Object[]{Float.valueOf(f), Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), Float.valueOf(f5)}));
    }

    public void setRatingReminderCurrentVersion(String str) {
        saveProperty(EAppSettingProperty.RATING_REMINDER_CURRENT_VERSION, str);
    }

    public void setRatingReminderDeclinedToRate(boolean z) {
        getProperty(EAppSettingProperty.RATING_REMINDER_DECLINED_TO_RATE, z);
    }

    public void setRatingReminderFirstUseDate(long j) {
        saveProperty(EAppSettingProperty.RATING_REMINDER_FIRST_USE_DATE, j);
    }

    public void setRatingReminderRatedCurrentVersion(boolean z) {
        saveProperty(EAppSettingProperty.RATING_REMINDER_RATED_CURRENT_VERSION, z);
    }

    public void setRatingReminderReminderRequestDate(long j) {
        saveProperty(EAppSettingProperty.RATING_REMINDER_REMINDER_REQUESTDATE, j);
    }

    public void setRatingReminderUseCount(int i) {
        saveProperty(EAppSettingProperty.RATING_REMINDER_USE_COUNT, i);
    }

    public void setTrackingDlgShowed(boolean z) {
        saveProperty(EAppSettingProperty.TRACKING_DLG_SHOWED, z);
    }

    public void setTrackingEnabled(boolean z) {
        saveProperty(EAppSettingProperty.TRACKING_ENABLED, z);
    }

    @Deprecated
    public void setUseOpenGL(boolean z) {
        this.useOpenGL = z;
    }

    public void setYouTubeVideoCategoryId(String str) {
        saveProperty(EAppSettingProperty.YOUTUBE_VIDEO_CATEGORY_ID_PROP, str);
    }
}
