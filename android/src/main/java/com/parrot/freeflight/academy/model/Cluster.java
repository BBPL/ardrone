package com.parrot.freeflight.academy.model;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class Cluster {
    private static final String COUNT = "count";
    private static final String GPS_LATITUDE = "gps_latitude";
    private static final String GPS_LONGITUDE = "gps_longitude";
    protected int count = 0;
    protected String gps_latitude = "500.0";
    protected String gps_longitude = "500.0";

    public Cluster(JSONObject jSONObject) throws JSONException {
        setCount(jSONObject.getInt(COUNT));
        setGps_latitude(jSONObject.getString(GPS_LATITUDE));
        setGps_longitude(jSONObject.getString(GPS_LONGITUDE));
        if (this.gps_latitude == "null" || this.gps_longitude == "null") {
            Log.e("JSONParse", jSONObject.toString(1));
        }
    }

    public int getCount() {
        return this.count;
    }

    public String getGps_latitude() {
        return this.gps_latitude;
    }

    public String getGps_longitude() {
        return this.gps_longitude;
    }

    public void increment() {
        this.count++;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public void setGps_latitude(String str) {
        this.gps_latitude = str;
    }

    public void setGps_longitude(String str) {
        this.gps_longitude = str;
    }

    public String toString() {
        return "count:" + this.count + "\n" + GPS_LATITUDE + ":" + this.gps_latitude + "\n" + GPS_LONGITUDE + ":" + this.gps_longitude;
    }
}
