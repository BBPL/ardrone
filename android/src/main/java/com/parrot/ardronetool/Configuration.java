package com.parrot.ardronetool;

public class Configuration {
    private static Configuration instance;

    public enum EventKey {
        NUM_VERSION_CONFIG(Integer.class, EventKeyPermission.READ),
        NUM_VERSION_MB(Integer.class, EventKeyPermission.READ),
        NUM_VERSION_SOFT(String.class, EventKeyPermission.READ),
        DRONE_SERIAL(String.class, EventKeyPermission.READ),
        SOFT_BUILD_DATE(String.class, EventKeyPermission.READ),
        MOTOR1_SOFT(String.class, EventKeyPermission.READ),
        MOTOR1_HARD(String.class, EventKeyPermission.READ),
        MOTOR1_SUPPLIER(String.class, EventKeyPermission.READ),
        MOTOR2_SOFT(String.class, EventKeyPermission.READ),
        MOTOR2_HARD(String.class, EventKeyPermission.READ),
        MOTOR2_SUPPLIER(String.class, EventKeyPermission.READ),
        MOTOR3_SOFT(String.class, EventKeyPermission.READ),
        MOTOR3_HARD(String.class, EventKeyPermission.READ),
        MOTOR3_SUPPLIER(String.class, EventKeyPermission.READ),
        MOTOR4_SOFT(String.class, EventKeyPermission.READ),
        MOTOR4_HARD(String.class, EventKeyPermission.READ),
        MOTOR4_SUPPLIER(String.class, EventKeyPermission.READ),
        ARDRONE_NAME(String.class, EventKeyPermission.READ_WRITE),
        FLYING_TIME(Integer.class, EventKeyPermission.READ),
        NAVDATA_DEMO(Boolean.class, EventKeyPermission.READ_WRITE),
        NAVDATA_OPTIONS(Integer.class, EventKeyPermission.READ_WRITE),
        COM_WATCHDOG(Integer.class, EventKeyPermission.READ_WRITE),
        VIDEO_ENABLE(Boolean.class, EventKeyPermission.READ_WRITE),
        VISION_ENABLE(Boolean.class, EventKeyPermission.READ_WRITE),
        VBAT_MIN(Integer.class, EventKeyPermission.READ_WRITE),
        LOCAL_TIME(Integer.class, EventKeyPermission.READ_WRITE),
        CONTROL_LEVEL(Integer.class, EventKeyPermission.READ_WRITE),
        EULER_ANGLE_MAX(Float.class, EventKeyPermission.READ_WRITE),
        ALTITUDE_MAX(Integer.class, EventKeyPermission.READ_WRITE),
        ALTITUDE_MIN(Integer.class, EventKeyPermission.READ_WRITE),
        CONTROL_IPHONE_TILT(Float.class, EventKeyPermission.READ_WRITE),
        CONTROL_VZ_MAX(Float.class, EventKeyPermission.READ_WRITE),
        CONTROL_YAW(Float.class, EventKeyPermission.READ_WRITE),
        OUTDOOR(Boolean.class, EventKeyPermission.READ_WRITE),
        FLIGHT_WITHOUT_SHELL(Boolean.class, EventKeyPermission.READ_WRITE),
        AUTONOMOUS_FLIGHT(Boolean.class, EventKeyPermission.READ_WRITE),
        MANUAL_TRIM(Boolean.class, EventKeyPermission.READ_WRITE),
        INDOOR_EULER_ANGLE_MAX(Float.class, EventKeyPermission.READ_WRITE),
        INDOOR_CONTROL_VZ_MAX(Float.class, EventKeyPermission.READ_WRITE),
        INDOOR_CONTROL_YAW(Float.class, EventKeyPermission.READ_WRITE),
        OUTDOOR_EULER_ANGLE_MAX(Float.class, EventKeyPermission.READ_WRITE),
        OUTDOOR_CONTROL_VZ_MAX(Float.class, EventKeyPermission.READ_WRITE),
        OUTDOOR_CONTROL_YAW(Float.class, EventKeyPermission.READ_WRITE),
        FLYING_MODE(Integer.class, EventKeyPermission.READ_WRITE),
        HOVERING_RANGE(Integer.class, EventKeyPermission.READ_WRITE),
        FLIGHT_ANIM(String.class, EventKeyPermission.READ_WRITE),
        SSID_SINGLE_PLAYER(String.class, EventKeyPermission.READ_WRITE),
        SSID_MULTI_PLAYER(String.class, EventKeyPermission.READ_WRITE),
        WIFI_MODE(Integer.class, EventKeyPermission.READ_WRITE),
        WIFI_RATE(Integer.class, EventKeyPermission.READ_WRITE),
        OWNER_MAC(String.class, EventKeyPermission.READ_WRITE),
        ULTRASOUND_FREQ(Integer.class, EventKeyPermission.READ_WRITE),
        ULTRASOUND_WATCHDOG(Integer.class, EventKeyPermission.READ_WRITE),
        PIC_VERSION(Integer.class, EventKeyPermission.READ),
        CODEC_FPS(Integer.class, EventKeyPermission.READ_WRITE),
        VIDEO_CODEC(Integer.class, EventKeyPermission.READ_WRITE),
        BITRATE(Integer.class, EventKeyPermission.READ_WRITE),
        MAX_BITRATE(Integer.class, EventKeyPermission.READ_WRITE),
        BITRATE_CTRL_MODE(Integer.class, EventKeyPermission.READ_WRITE),
        VIDEO_CHANNEL(Integer.class, EventKeyPermission.READ_WRITE),
        VIDEO_ON_USB(Boolean.class, EventKeyPermission.READ_WRITE),
        VIDEO_FILE_INDEX(Integer.class, EventKeyPermission.READ_WRITE),
        LEDS_ANIM(String.class, EventKeyPermission.READ_WRITE),
        ENEMY_COLORS(Integer.class, EventKeyPermission.READ_WRITE),
        GROUND_STRIPE_COLORS(Integer.class, EventKeyPermission.READ_WRITE),
        ENEMY_WITHOUT_SHELL(Integer.class, EventKeyPermission.READ_WRITE),
        DETECT_TYPE(Integer.class, EventKeyPermission.READ_WRITE),
        OUTPUT(Integer.class, EventKeyPermission.READ_WRITE),
        MAX_SIZE(Integer.class, EventKeyPermission.READ_WRITE),
        NB_FILES(Integer.class, EventKeyPermission.READ_WRITE),
        USERBOX_CMD(String.class, EventKeyPermission.READ_WRITE),
        LATITUDE(Double.class, EventKeyPermission.READ_WRITE),
        LONGITUDE(Double.class, EventKeyPermission.READ_WRITE),
        ALTITUDE(Double.class, EventKeyPermission.READ_WRITE),
        APPLICATION_ID(String.class, EventKeyPermission.READ_WRITE),
        APPLICATION_DESC(String.class, EventKeyPermission.READ_WRITE),
        PROFILE_ID(String.class, EventKeyPermission.READ_WRITE),
        PROFILE_DESC(String.class, EventKeyPermission.READ_WRITE),
        SESSION_ID(String.class, EventKeyPermission.READ_WRITE),
        SESSION_DESC(String.class, EventKeyPermission.READ_WRITE),
        RESCUE(String.class, EventKeyPermission.READ_WRITE),
        FLYING_CAMERA_MODE(String.class, EventKeyPermission.READ_WRITE),
        FLYING_CAMERA_ENABLE(Boolean.class, EventKeyPermission.READ_WRITE),
        GPS_HARD(String.class, EventKeyPermission.READ),
        GPS_SOFT(String.class, EventKeyPermission.READ),
        BATTERY_TYPE(Integer.class, EventKeyPermission.READ_WRITE),
        FW_UPLOAD_TRIGGER(Integer.class, EventKeyPermission.READ_WRITE);
        
        private EventKeyPermission permission;
        private Class<?> type;

        private EventKey(Class<?> cls, EventKeyPermission eventKeyPermission) {
            this.permission = eventKeyPermission;
            this.type = cls;
        }
    }

    private enum EventKeyPermission {
        READ,
        WRITE,
        READ_WRITE
    }

    public enum Source {
        CONTROL_CONFIG_DEFAULT,
        CONTROL_CONFIG,
        APPLICATION_CONFIG
    }

    private Configuration() {
    }

    private native void addEventBoolean(int i, boolean z, ConfigurationEventDelegate configurationEventDelegate);

    private native void addEventDouble(int i, double d, ConfigurationEventDelegate configurationEventDelegate);

    private native void addEventFloat(int i, float f, ConfigurationEventDelegate configurationEventDelegate);

    private native void addEventInt(int i, int i2, ConfigurationEventDelegate configurationEventDelegate);

    private native void addEventString(int i, String str, ConfigurationEventDelegate configurationEventDelegate);

    private native boolean getBoolValue(int i, int i2);

    private native double getDoubleValue(int i, int i2);

    public static native String getDroneHost();

    private native float getFloatValue(int i, int i2);

    public static native int getFtpPort();

    private native int getIntValue(int i, int i2);

    private native String getStringValue(int i, int i2);

    private boolean hasWritePermission(EventKey eventKey) {
        switch (eventKey.permission) {
            case WRITE:
            case READ_WRITE:
                return true;
            default:
                return false;
        }
    }

    public static Configuration instance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    private boolean isWrongValueTypeForKey(EventKey eventKey, Object obj) {
        return eventKey.type != obj.getClass();
    }

    private native void setValueBoolean(int i, int i2, boolean z);

    private native void setValueInt(int i, int i2, int i3);

    public void addEvent(EventKey eventKey, Object obj, ConfigurationEventDelegate configurationEventDelegate) {
        if (isWrongValueTypeForKey(eventKey, obj)) {
            throw new IllegalArgumentException("Can't put " + obj.getClass().getSimpleName() + " parameter to " + eventKey.type + " key");
        } else if (hasWritePermission(eventKey)) {
            Class access$000 = eventKey.type;
            if (access$000 == Integer.class && (obj instanceof Integer)) {
                addEventInt(eventKey.ordinal(), ((Integer) obj).intValue(), configurationEventDelegate);
            } else if (access$000 == Boolean.class && (obj instanceof Boolean)) {
                addEventBoolean(eventKey.ordinal(), ((Boolean) obj).booleanValue(), configurationEventDelegate);
            } else if (access$000 == Float.class && (obj instanceof Float)) {
                addEventFloat(eventKey.ordinal(), ((Float) obj).floatValue(), configurationEventDelegate);
            } else if (access$000 == Double.class && (obj instanceof Double)) {
                addEventDouble(eventKey.ordinal(), (double) ((Double) obj).floatValue(), configurationEventDelegate);
            } else if (access$000 == String.class && (obj instanceof String)) {
                addEventString(eventKey.ordinal(), (String) obj, configurationEventDelegate);
            } else {
                throw new IllegalArgumentException("Key type: " + eventKey.type.getSimpleName() + " param type: " + obj.getClass().getSimpleName());
            }
        } else {
            throw new IllegalArgumentException("Key " + eventKey.name() + " is not writable");
        }
    }

    public native boolean configurationGet(ConfigurationEventDelegate configurationEventDelegate);

    public native boolean customConfigurationGet(ConfigurationEventDelegate configurationEventDelegate);

    public Object getValue(Source source, EventKey eventKey) {
        Class access$000 = eventKey.type;
        if (access$000 == Integer.class) {
            return Integer.valueOf(getIntValue(source.ordinal(), eventKey.ordinal()));
        }
        if (access$000 == Boolean.class) {
            return Boolean.valueOf(getBoolValue(source.ordinal(), eventKey.ordinal()));
        }
        if (access$000 == Float.class) {
            return Float.valueOf(getFloatValue(source.ordinal(), eventKey.ordinal()));
        }
        if (access$000 == Double.class) {
            return Double.valueOf(getDoubleValue(source.ordinal(), eventKey.ordinal()));
        }
        if (access$000 == String.class) {
            return getStringValue(source.ordinal(), eventKey.ordinal());
        }
        throw new IllegalArgumentException("Unknown key type " + eventKey.type.getSimpleName());
    }

    public native void init();

    public native void reset();

    public native void sendApplicationDefault();

    public native void sendSessionDefault();

    public native void sendUserDefault();

    public void setValue(Source source, EventKey eventKey, int i) {
        setValueInt(source.ordinal(), eventKey.ordinal(), i);
    }

    public void setValue(Source source, EventKey eventKey, boolean z) {
        setValueBoolean(source.ordinal(), eventKey.ordinal(), z);
    }
}
