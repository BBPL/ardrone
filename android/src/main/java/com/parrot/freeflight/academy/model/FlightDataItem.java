package com.parrot.freeflight.academy.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FlightDataItem {
    private static final String ALTITUDE = "altitude";
    private static final String BATTERY_LEVEL = "battery_level";
    private static final String DEMO_PHI = "demo_phi";
    private static final String DEMO_PSI = "demo_psi";
    private static final String DEMO_THETA = "demo_theta";
    private static final String GPS_AVAILABLE = "gps_available";
    private static final String GPS_LATITUDE = "gps_latitude";
    private static final String GPS_LATITUDE_FUSED = "gps_latitude_fused";
    private static final String GPS_LONGITUDE = "gps_longitude";
    private static final String GPS_LONGITUDE_FUSED = "gps_longitude_fused";
    private static final String GPS_POSITION_ERROR = "gps_position_error";
    private static final String PITCH_RC_EMBEDDED = "pitch_rc_embedded";
    private static final String PITCH_REF_EMBEDDED_MDEG = "pitch_ref_embedded_mdeg";
    private static final String ROLL_RC_EMBEDDED = "roll_rc_embedded";
    private static final String ROLL_REF_EMBEDDED_MDEG = "roll_ref_embedded_mdeg";
    private static final String SPEED = "speed";
    private static final String TIME = "time";
    private static final String U_PITCH_PLANIF_PWM = "u_pitch_planif_pwm";
    private static final String U_PITCH_PWM = "u_pitch_pwm";
    private static final String U_ROLL_PLANIF_PWM = "u_roll_planif_pwm";
    private static final String U_ROLL_PWM = "u_roll_pwm";
    private int altitude;
    private int batteryLevel;
    public float demo_phi;
    public float demo_psi;
    public float demo_theta;
    public boolean gps_available;
    public float gps_latitude;
    public float gps_latitude_fused;
    public float gps_longitude;
    public float gps_longitude_fused;
    public float gps_position_error;
    private int pitchRcEmbedded;
    private int pitchRefEmbeddedMdeg;
    private int rollRcEmbedded;
    private int rollRefEmbeddedMdeg;
    private double speed;
    private long time;
    private int uPitchPlanifPwm;
    private int uPitchPwm;
    private int uRollPlanifPwm;
    private int uRollPwm;

    public FlightDataItem(FlightDataItem flightDataItem) {
        this.batteryLevel = flightDataItem.batteryLevel;
        this.pitchRcEmbedded = flightDataItem.pitchRcEmbedded;
        this.pitchRefEmbeddedMdeg = flightDataItem.pitchRefEmbeddedMdeg;
        this.altitude = flightDataItem.altitude;
        this.rollRcEmbedded = flightDataItem.rollRcEmbedded;
        this.uRollPlanifPwm = flightDataItem.uRollPlanifPwm;
        this.uRollPwm = flightDataItem.uRollPwm;
        this.time = flightDataItem.time;
        this.uPitchPwm = flightDataItem.uPitchPwm;
        this.speed = flightDataItem.speed;
        this.rollRefEmbeddedMdeg = flightDataItem.rollRefEmbeddedMdeg;
        this.uPitchPlanifPwm = flightDataItem.uPitchPlanifPwm;
        this.gps_available = flightDataItem.gps_available;
        this.gps_latitude = flightDataItem.gps_latitude;
        this.gps_latitude_fused = flightDataItem.gps_latitude_fused;
        this.gps_longitude = flightDataItem.gps_longitude;
        this.gps_longitude_fused = flightDataItem.gps_longitude_fused;
        this.gps_position_error = flightDataItem.gps_position_error;
        this.demo_theta = flightDataItem.demo_theta;
        this.demo_phi = flightDataItem.demo_phi;
        this.demo_psi = flightDataItem.demo_psi;
    }

    public static ArrayList<FlightDataItem> jsonArrToGraphData(JSONArray jSONArray) throws JSONException {
        ArrayList<FlightDataItem> arrayList = new ArrayList();
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            FlightDataItem flightDataItem = new FlightDataItem();
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            flightDataItem.setBatteryLevel(jSONObject.getInt(BATTERY_LEVEL));
            flightDataItem.setPitchRcEmbedded(jSONObject.getInt(PITCH_RC_EMBEDDED));
            flightDataItem.setPitchRefEmbeddedMdeg(jSONObject.getInt(PITCH_REF_EMBEDDED_MDEG));
            flightDataItem.setAltitude(jSONObject.getInt(ALTITUDE));
            flightDataItem.setRollRcEmbedded(jSONObject.getInt(ROLL_RC_EMBEDDED));
            flightDataItem.setuRollPlanifPwm(jSONObject.getInt(U_ROLL_PLANIF_PWM));
            flightDataItem.setuRollPwm(jSONObject.getInt(U_ROLL_PWM));
            flightDataItem.setTime(jSONObject.getLong(TIME) * 1000);
            flightDataItem.setuPitchPwm(jSONObject.getInt(U_PITCH_PWM));
            flightDataItem.setSpeed(jSONObject.getDouble(SPEED));
            flightDataItem.setRollRefEmbeddedMdeg(jSONObject.getInt(ROLL_REF_EMBEDDED_MDEG));
            flightDataItem.setuPitchPlanifPwm(jSONObject.getInt(U_PITCH_PLANIF_PWM));
            flightDataItem.setGps_available(jSONObject.getBoolean(GPS_AVAILABLE));
            flightDataItem.setGps_latitude((float) jSONObject.getDouble(GPS_LATITUDE));
            flightDataItem.setGps_latitude_fused((float) jSONObject.getDouble(GPS_LATITUDE_FUSED));
            flightDataItem.setGps_longitude((float) jSONObject.getDouble(GPS_LONGITUDE));
            flightDataItem.setGps_longitude_fused((float) jSONObject.getDouble(GPS_LONGITUDE_FUSED));
            flightDataItem.setGps_position_error((float) jSONObject.getDouble(GPS_POSITION_ERROR));
            flightDataItem.setDemo_theta((float) jSONObject.getDouble(DEMO_THETA));
            flightDataItem.setDemo_phi((float) jSONObject.getDouble(DEMO_PHI));
            flightDataItem.setDemo_psi((float) jSONObject.getDouble(DEMO_PSI));
            arrayList.add(flightDataItem);
        }
        return arrayList;
    }

    public int getAltitude() {
        return this.altitude;
    }

    public int getBatteryLevel() {
        return this.batteryLevel;
    }

    public float getDemo_phi() {
        return this.demo_phi;
    }

    public float getDemo_psi() {
        return this.demo_psi;
    }

    public float getDemo_theta() {
        return this.demo_theta;
    }

    public float getGps_latitude() {
        return this.gps_latitude;
    }

    public float getGps_latitude_fused() {
        return this.gps_latitude_fused;
    }

    public float getGps_longitude() {
        return this.gps_longitude;
    }

    public float getGps_longitude_fused() {
        return this.gps_longitude_fused;
    }

    public float getGps_position_error() {
        return this.gps_position_error;
    }

    public int getPitchRcEmbedded() {
        return this.pitchRcEmbedded;
    }

    public int getPitchRefEmbeddedMdeg() {
        return this.pitchRefEmbeddedMdeg;
    }

    public int getRollRcEmbedded() {
        return this.rollRcEmbedded;
    }

    public int getRollRefEmbeddedMdeg() {
        return this.rollRefEmbeddedMdeg;
    }

    public double getSpeed() {
        return this.speed;
    }

    public long getTime() {
        return this.time;
    }

    public int getuPitchPlanifPwm() {
        return this.uPitchPlanifPwm;
    }

    public int getuPitchPwm() {
        return this.uPitchPwm;
    }

    public int getuRollPlanifPwm() {
        return this.uRollPlanifPwm;
    }

    public int getuRollPwm() {
        return this.uRollPwm;
    }

    public boolean isGps_available() {
        return this.gps_available;
    }

    public void setAltitude(int i) {
        this.altitude = i;
    }

    public void setBatteryLevel(int i) {
        this.batteryLevel = i;
    }

    public void setDemo_phi(float f) {
        this.demo_phi = f;
    }

    public void setDemo_psi(float f) {
        this.demo_psi = f;
    }

    public void setDemo_theta(float f) {
        this.demo_theta = f;
    }

    public void setGps_available(boolean z) {
        this.gps_available = z;
    }

    public void setGps_latitude(float f) {
        this.gps_latitude = f;
    }

    public void setGps_latitude_fused(float f) {
        this.gps_latitude_fused = f;
    }

    public void setGps_longitude(float f) {
        this.gps_longitude = f;
    }

    public void setGps_longitude_fused(float f) {
        this.gps_longitude_fused = f;
    }

    public void setGps_position_error(float f) {
        this.gps_position_error = f;
    }

    public void setPitchRcEmbedded(int i) {
        this.pitchRcEmbedded = i;
    }

    public void setPitchRefEmbeddedMdeg(int i) {
        this.pitchRefEmbeddedMdeg = i;
    }

    public void setRollRcEmbedded(int i) {
        this.rollRcEmbedded = i;
    }

    public void setRollRefEmbeddedMdeg(int i) {
        this.rollRefEmbeddedMdeg = i;
    }

    public void setSpeed(double d) {
        this.speed = d;
    }

    public void setTime(long j) {
        this.time = j;
    }

    public void setuPitchPlanifPwm(int i) {
        this.uPitchPlanifPwm = i;
    }

    public void setuPitchPwm(int i) {
        this.uPitchPwm = i;
    }

    public void setuRollPlanifPwm(int i) {
        this.uRollPlanifPwm = i;
    }

    public void setuRollPwm(int i) {
        this.uRollPwm = i;
    }
}
