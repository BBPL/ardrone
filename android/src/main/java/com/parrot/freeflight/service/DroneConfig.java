package com.parrot.freeflight.service;

import android.util.Log;
import com.parrot.ardronetool.ARDroneEngine;
import com.parrot.ardronetool.ARDroneVersion;
import com.parrot.ardronetool.Configuration;
import com.parrot.ardronetool.Configuration.EventKey;
import com.parrot.ardronetool.Configuration.Source;
import com.parrot.ardronetool.academynavdata.BatteryType;
import com.parrot.ardronetool.academynavdata.FlyingCameraModeValue;
import com.parrot.ardronetool.academynavdata.RescueMode;
import com.parrot.ardronetool.video.VariableBitrateControlMode;
import org.mortbay.jetty.HttpVersions;

public class DroneConfig {
    public static final int ALTITUDE_MAX = 100;
    public static final int ALTITUDE_MIN = 3;
    public static final int DEFAULT_TILT_MAX = 30;
    public static final int DEVICE_TILTMAX_MAX = 50;
    public static final int DEVICE_TILTMAX_MIN = 5;
    public static final int REPAIR_FTP_PORT = 21;
    public static final int TELNET_PORT = 23;
    public static final int TILT_MAX = 30;
    public static final int TILT_MIN = 5;
    public static final int VERT_SPEED_MAX = 2000;
    public static final int VERT_SPEED_MIN = 200;
    public static final int YAW_MAX = 350;
    public static final int YAW_MIN = 40;
    private Configuration config;

    public DroneConfig(ARDroneEngine aRDroneEngine) {
        this.config = aRDroneEngine.getConfiguration();
    }

    public static String getDroneHost() {
        return Configuration.getDroneHost();
    }

    public static int getFtpPort() {
        return Configuration.getFtpPort();
    }

    private void setFlyingCameraMode(String str) {
        this.config.addEvent(EventKey.FLYING_CAMERA_MODE, str, null);
    }

    public int getAltitudeLimit() {
        return ((Integer) this.config.getValue(Source.CONTROL_CONFIG, EventKey.ALTITUDE_MAX)).intValue() / 1000;
    }

    public BatteryType getBatteryType() {
        Integer num = (Integer) this.config.getValue(Source.CONTROL_CONFIG, EventKey.BATTERY_TYPE);
        return (num == null || num.intValue() < 0 || num.intValue() >= BatteryType.values().length) ? null : BatteryType.values()[num.intValue()];
    }

    public float getDeviceTiltMax() {
        float floatValue = ((Float) this.config.getValue(Source.CONTROL_CONFIG, EventKey.CONTROL_IPHONE_TILT)).floatValue();
        float toDegrees = (float) Math.toDegrees((double) floatValue);
        Log.d("DroneConfig", "Got Iphone tilt (RAD): " + floatValue + " (DEG):" + toDegrees);
        return toDegrees;
    }

    public String getFlyingCameraMode() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.FLYING_CAMERA_MODE);
    }

    public int getFlyingCameraModeValue() {
        String flyingCameraMode = getFlyingCameraMode();
        if (flyingCameraMode != null) {
            String[] split = flyingCameraMode.split(",");
            if (split.length != 0) {
                return Integer.parseInt(split[0]);
            }
        }
        return -1;
    }

    public Integer getFlyingTime() {
        return (Integer) this.config.getValue(Source.CONTROL_CONFIG, EventKey.FLYING_TIME);
    }

    public String getGpsHardware() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.GPS_HARD);
    }

    public String getGpsSoftware() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.GPS_SOFT);
    }

    public String getHardwareVersion() {
        int intValue = ((Integer) this.config.getValue(Source.CONTROL_CONFIG, EventKey.NUM_VERSION_MB)).intValue();
        return HttpVersions.HTTP_0_9 + (intValue >> 4) + "." + (intValue & 15);
    }

    public String getInertialHardwareVersion() {
        int intValue = ((Integer) this.config.getValue(Source.CONTROL_CONFIG, EventKey.PIC_VERSION)).intValue();
        return HttpVersions.HTTP_0_9 + ((intValue >> 27) + 1) + "." + ((intValue >> 24) & 7);
    }

    public String getInertialSoftwareVersion() {
        int intValue = ((Integer) this.config.getValue(Source.CONTROL_CONFIG, EventKey.PIC_VERSION)).intValue();
        return HttpVersions.HTTP_0_9 + ((16777215 & intValue) >> 16) + "." + (intValue & 65535);
    }

    public String getMotor1HardVersion() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.MOTOR1_HARD);
    }

    public String getMotor1SoftVersion() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.MOTOR1_SOFT);
    }

    public String getMotor1Vendor() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.MOTOR1_SUPPLIER);
    }

    public String getMotor2HardVersion() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.MOTOR2_HARD);
    }

    public String getMotor2SoftVersion() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.MOTOR2_SOFT);
    }

    public String getMotor2Vendor() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.MOTOR2_SUPPLIER);
    }

    public String getMotor3HardVersion() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.MOTOR3_HARD);
    }

    public String getMotor3SoftVersion() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.MOTOR3_SOFT);
    }

    public String getMotor3Vendor() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.MOTOR3_SUPPLIER);
    }

    public String getMotor4HardVersion() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.MOTOR4_HARD);
    }

    public String getMotor4SoftVersion() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.MOTOR4_SOFT);
    }

    public String getMotor4Vendor() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.MOTOR4_SUPPLIER);
    }

    public String getNetworkName() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.SSID_SINGLE_PLAYER);
    }

    public String getOwnerMac() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.OWNER_MAC);
    }

    public String getSoftwareVersion() {
        return (String) this.config.getValue(Source.CONTROL_CONFIG, EventKey.NUM_VERSION_SOFT);
    }

    public float getTilt() {
        return (float) Math.toDegrees((double) ((Float) this.config.getValue(Source.CONTROL_CONFIG, EventKey.EULER_ANGLE_MAX)).floatValue());
    }

    public float getVertSpeedMax() {
        return ((Float) this.config.getValue(Source.CONTROL_CONFIG, EventKey.CONTROL_VZ_MAX)).floatValue();
    }

    public int getVideoCodec() {
        return ((Integer) this.config.getValue(Source.CONTROL_CONFIG, EventKey.VIDEO_CODEC)).intValue();
    }

    public float getYawSpeedMax() {
        return (float) Math.toDegrees((double) ((Float) this.config.getValue(Source.CONTROL_CONFIG, EventKey.CONTROL_YAW)).floatValue());
    }

    public boolean isAdaptiveVideo() {
        return VariableBitrateControlMode.DYNAMIC.ordinal() == ((Integer) this.config.getValue(Source.CONTROL_CONFIG, EventKey.BITRATE_CTRL_MODE)).intValue();
    }

    public boolean isOutdoorFlight() {
        return ((Boolean) this.config.getValue(Source.CONTROL_CONFIG, EventKey.OUTDOOR)).booleanValue();
    }

    public boolean isOutdoorHull() {
        return ((Boolean) this.config.getValue(Source.CONTROL_CONFIG, EventKey.FLIGHT_WITHOUT_SHELL)).booleanValue();
    }

    public boolean isRecordOnUsb() {
        return ((Boolean) this.config.getValue(Source.CONTROL_CONFIG, EventKey.VIDEO_ON_USB)).booleanValue();
    }

    public void setAdaptiveVideo(boolean z) {
        if (ARDroneVersion.isArDrone2()) {
            this.config.addEvent(EventKey.BITRATE_CTRL_MODE, Integer.valueOf((z ? VariableBitrateControlMode.DYNAMIC : VariableBitrateControlMode.DISABLED).ordinal()), null);
        } else if (ARDroneVersion.isArDrone1()) {
            Object obj;
            if (z) {
                obj = VariableBitrateControlMode.DYNAMIC;
            } else {
                VariableBitrateControlMode variableBitrateControlMode = VariableBitrateControlMode.MANUAL;
            }
            int i = 32 == ((Integer) this.config.getValue(Source.CONTROL_CONFIG, EventKey.VIDEO_CODEC)).intValue() ? 20000 : 15000;
            this.config.addEvent(EventKey.BITRATE_CTRL_MODE, obj, null);
            this.config.addEvent(EventKey.BITRATE, Integer.valueOf(i), null);
        }
    }

    public void setAltitudeLimit(int i) {
        this.config.addEvent(EventKey.ALTITUDE_MAX, Integer.valueOf(i * 1000), null);
    }

    public void setBackHomeCoordinates(double d, double d2) {
        setFlyingCameraMode(String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d", new Object[]{Integer.valueOf(FlyingCameraModeValue.FLYING_CAMERA_GPS_MODE_BACKTOHOME.value), Integer.valueOf(0), Integer.valueOf((int) (d * 1.0E7d)), Integer.valueOf((int) (d2 * 1.0E7d)), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0)}));
    }

    public void setBatteryType(BatteryType batteryType) {
        this.config.addEvent(EventKey.BATTERY_TYPE, Integer.valueOf(batteryType.ordinal()), null);
    }

    public void setDeviceTiltMax(float f) {
        float toRadians = (float) Math.toRadians((double) f);
        Log.d("DroneConfig", "Setting Iphone tilt (RAD): " + toRadians + " (DEG):" + f);
        this.config.addEvent(EventKey.CONTROL_IPHONE_TILT, Float.valueOf(toRadians), null);
    }

    public void setFlyingCameraEnabled(boolean z) {
        this.config.addEvent(EventKey.FLYING_CAMERA_ENABLE, Boolean.valueOf(z), null);
    }

    public void setFlyingCameraMode(double d, double d2, int i, int i2, int i3, int i4, float f) {
        setFlyingCameraMode(String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d", new Object[]{Integer.valueOf(FlyingCameraModeValue.FLYING_CAMERA_GPS_MODE_WAYPOINT.value), Integer.valueOf(0), Integer.valueOf((int) (1.0E7d * d)), Integer.valueOf((int) (1.0E7d * d2)), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf((int) (100.0f * f)), Integer.valueOf(0)}));
    }

    public void setNetworkName(String str) {
        this.config.addEvent(EventKey.SSID_SINGLE_PLAYER, str, null);
    }

    public void setOutdoorFlight(boolean z) {
        this.config.addEvent(EventKey.OUTDOOR, Boolean.valueOf(z), null);
    }

    public void setOutdoorHull(boolean z) {
        this.config.addEvent(EventKey.FLIGHT_WITHOUT_SHELL, Boolean.valueOf(z), null);
    }

    public void setOwnerMac(String str) {
        this.config.addEvent(EventKey.OWNER_MAC, str, null);
    }

    public void setRecordOnUsb(boolean z) {
        this.config.addEvent(EventKey.VIDEO_ON_USB, Boolean.valueOf(z), null);
    }

    public void setRescueMode(RescueMode rescueMode) {
        this.config.addEvent(EventKey.RESCUE, String.format("%d,0", new Object[]{Integer.valueOf(rescueMode.ordinal())}), null);
    }

    public void setTilt(float f) {
        this.config.addEvent(EventKey.EULER_ANGLE_MAX, Float.valueOf((float) Math.toRadians((double) f)), null);
    }

    public void setVertSpeedMax(float f) {
        this.config.addEvent(EventKey.CONTROL_VZ_MAX, Float.valueOf(f), null);
    }

    public void setVideoCodec(int i) {
        this.config.addEvent(EventKey.VIDEO_CODEC, Integer.valueOf(i), null);
    }

    public void setYawSpeedMax(int i) {
        this.config.addEvent(EventKey.CONTROL_YAW, Float.valueOf((float) Math.toRadians((double) i)), null);
    }
}
