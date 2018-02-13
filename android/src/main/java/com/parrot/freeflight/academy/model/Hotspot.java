package com.parrot.freeflight.academy.model;

import android.util.Log;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Hotspot implements Serializable {
    private static final String CREATION_DATE = "creation_date";
    private static final String DATE_FORMAT = "yyyy'-'MM'-'dd' 'HH':'mm':'ss";
    private static final String DESCRIPTION = "description";
    private static final String GPS_LATITUDE = "gps_latitude";
    private static final String GPS_LONGITUDE = "gps_longitude";
    private static final String GRADE = "grade";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String TAG = Hotspot.class.getSimpleName();
    private static final String TYPE = "type";
    private static final String USER = "user";
    private static final long serialVersionUID = 4544225680737648918L;
    private Date creation_DateTime;
    private String creation_date;
    private String description;
    private double gps_latitude;
    private double gps_longitude;
    private int grade;
    private int id;
    private String name;
    private int type;
    private User user;

    public Hotspot(JSONObject jSONObject) throws JSONException, ParseException {
        if (!jSONObject.isNull("description")) {
            setDescription(jSONObject.getString("description"));
        }
        if (!jSONObject.isNull(NAME)) {
            setName(jSONObject.getString(NAME));
        }
        if (!jSONObject.isNull("grade")) {
            setGrade(jSONObject.getInt("grade"));
        }
        if (!jSONObject.isNull(TYPE)) {
            setType(jSONObject.getInt(TYPE));
        }
        setCreation_date(jSONObject.getString(CREATION_DATE));
        setGps_latitude(jSONObject.getDouble(GPS_LATITUDE));
        setGps_longitude(jSONObject.getDouble(GPS_LONGITUDE));
        setId(jSONObject.getInt(ID));
        if (jSONObject.has(USER)) {
            setUser(new User(jSONObject.getJSONObject(USER)));
        }
    }

    public static List<Hotspot> jsonArrToHotspots(JSONArray jSONArray) throws JSONException {
        List<Hotspot> arrayList = new ArrayList();
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            try {
                Hotspot hotspot = new Hotspot(jSONObject);
                if (hotspot != null) {
                    arrayList.add(hotspot);
                }
            } catch (JSONException e) {
                Log.w(TAG, "Flight is skipped due to JSON exception while parsing flight. JSON: " + jSONObject.toString(4) + " Exception: " + e);
            } catch (ParseException e2) {
                Log.w(TAG, "Flight object is skipped because date format is unknown. Exception: " + e2);
            }
        }
        return arrayList;
    }

    public String getCreation_date() {
        return this.creation_date;
    }

    public Date getCreation_dateAsDate() {
        return this.creation_DateTime;
    }

    public String getDescription() {
        return this.description;
    }

    public double getGps_latitude() {
        return this.gps_latitude;
    }

    public double getGps_longitude() {
        return this.gps_longitude;
    }

    public int getGrade() {
        return this.grade;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public User getUser() {
        return this.user;
    }

    public void setCreation_date(String str) throws ParseException {
        this.creation_date = str;
        this.creation_DateTime = new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH':'mm':'ss", Locale.getDefault()).parse(str);
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setGps_latitude(double d) {
        this.gps_latitude = d;
    }

    public void setGps_longitude(double d) {
        this.gps_longitude = d;
    }

    public void setGrade(int i) {
        this.grade = i;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setType(int i) {
        this.type = i;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
