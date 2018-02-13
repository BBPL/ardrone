package com.parrot.freeflight.academy.model;

import android.util.Log;
import com.parrot.freeflight.utils.DeviceNameEncoder;
import com.parrot.freeflight.vo.MediaVO;
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
import org.mortbay.jetty.HttpVersions;

public class Flight implements Serializable {
    private static final String CRASH = "crash";
    private static final String DATETIME = "datetime";
    public static final String DATE_FORMAT = "yyyy'-'MM'-'dd' 'HH':'mm':'ss";
    private static final String DESCRIPTION = "description";
    private static final String FLIGHTCAPTURE_SET = "flightcapture_set";
    private static final String FLIGHTVIDEO_SET = "flightvideo_set";
    private static final String FLIGHT_TIME = "flight_time";
    private static final String GPS_AVAILABLE = "gps_available";
    private static final String GPS_LATITUDE = "gps_latitude";
    private static final String GPS_LONGITUDE = "gps_longitude";
    private static final String GRADE = "grade";
    private static final String ID = "id";
    private static final String PHONE_MODEL = "phone_model";
    private static final String TAG = Flight.class.getSimpleName();
    private static final String TITLE = "title";
    private static final String TOTAL_FLIGHT_TIME = "total_flight_time";
    private static final String USER = "user";
    private static final String VISIBLE = "visible";
    private static final long serialVersionUID = 8519220369477202029L;
    private Country country;
    private int crash;
    private Date dateDateTime;
    private String datetime;
    private String description;
    private ArrayList<Media> flightCaptureSet = new ArrayList();
    private String flightTag;
    private int flightTime;
    private ArrayList<Media> flightVideoSet = new ArrayList();
    private long flight_time;
    private boolean gps_available;
    private double gps_latitude;
    private double gps_longitude;
    private int grade;
    private int id;
    private List<MediaVO> localCaptureSet = new ArrayList();
    private List<MediaVO> localVideoSet = new ArrayList();
    private String phone_model;
    private String title;
    private int totalFlightTime;
    private User user;
    private boolean visible;

    public Flight(JSONObject jSONObject) throws JSONException, ParseException {
        setId(jSONObject.getInt(ID));
        if (jSONObject.isNull(PHONE_MODEL)) {
            Log.w(TAG, "Phone model not received for flight " + this.id);
        } else {
            setPhone_model(jSONObject.getString(PHONE_MODEL));
        }
        if (!jSONObject.isNull(CRASH)) {
            setCrash(jSONObject.getInt(CRASH));
        }
        if (!jSONObject.isNull("description")) {
            setDescription(jSONObject.getString("description"));
        }
        if (!jSONObject.isNull("title")) {
            setTitle(jSONObject.getString("title"));
        }
        if (!jSONObject.isNull(FLIGHTCAPTURE_SET)) {
            setFlightCaptureSet(Media.jsonArrayToMedias(jSONObject.getJSONArray(FLIGHTCAPTURE_SET)));
        }
        if (!jSONObject.isNull(FLIGHTVIDEO_SET)) {
            setFlightVideoSet(Media.jsonArrayToMedias(jSONObject.getJSONArray(FLIGHTVIDEO_SET)));
        }
        if (!jSONObject.isNull("grade")) {
            setGrade(jSONObject.getInt("grade"));
        }
        setDatetime(jSONObject.getString(DATETIME));
        setFlightTag("media_" + jSONObject.getString(DATETIME).replace("-", HttpVersions.HTTP_0_9).replace(":", HttpVersions.HTTP_0_9).replace(" ", "_"));
        if (jSONObject.isNull("visible")) {
            setVisible(true);
        } else {
            setVisible(jSONObject.getBoolean("visible"));
        }
        setGps_latitude(jSONObject.getDouble(GPS_LATITUDE));
        setGps_longitude(jSONObject.getDouble(GPS_LONGITUDE));
        this.gps_available = jSONObject.getBoolean(GPS_AVAILABLE);
        setTotalFlightTime(jSONObject.getInt(TOTAL_FLIGHT_TIME));
        setFlightTime(jSONObject.getInt(FLIGHT_TIME));
        if (jSONObject.has(USER)) {
            setUser(new User(jSONObject.getJSONObject(USER)));
        }
    }

    public static List<Flight> jsonArrToFlights(JSONArray jSONArray) throws JSONException {
        int length = jSONArray.length();
        List<Flight> arrayList = new ArrayList(length);
        for (int i = 0; i < length; i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            try {
                Flight flight = new Flight(jSONObject);
                if (flight != null) {
                    arrayList.add(flight);
                }
            } catch (JSONException e) {
                Log.w(TAG, "Flight is skipped due to JSON exception while parsing flight. JSON: " + jSONObject.toString(4) + " Exception: " + e);
            } catch (ParseException e2) {
                Log.w(TAG, "Flight object is skipped because date format is unknown. Exception: " + e2);
            }
        }
        return arrayList;
    }

    public Country getCountry() {
        return this.country;
    }

    public int getCrash() {
        return this.crash;
    }

    public Date getDateTimeAsDate() {
        return this.dateDateTime;
    }

    public String getDateTimeAsString() {
        return this.datetime;
    }

    public String getDescription() {
        return this.description;
    }

    public ArrayList<Media> getFlightCaptureSet() {
        return this.flightCaptureSet;
    }

    public String getFlightTag() {
        return this.flightTag;
    }

    public int getFlightTime() {
        return this.flightTime;
    }

    public ArrayList<Media> getFlightVideoSet() {
        return this.flightVideoSet;
    }

    public long getFlight_time() {
        return this.flight_time;
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

    public List<MediaVO> getLocalCaptureSet() {
        return this.localCaptureSet;
    }

    public List<MediaVO> getLocalVideoSet() {
        return this.localVideoSet;
    }

    public String getPhone_model() {
        return DeviceNameEncoder.decode(this.phone_model);
    }

    public String getTitle() {
        return this.title;
    }

    public int getTotalFlightTime() {
        return this.totalFlightTime;
    }

    public User getUser() {
        return this.user;
    }

    public boolean isGpsAvailable() {
        return this.gps_available;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public void setCrash(int i) {
        this.crash = i;
    }

    public void setDatetime(String str) throws ParseException {
        this.datetime = str;
        this.dateDateTime = new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH':'mm':'ss", Locale.getDefault()).parse(str);
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setFlightCaptureSet(ArrayList<Media> arrayList) {
        this.flightCaptureSet = arrayList;
    }

    public void setFlightTag(String str) {
        this.flightTag = str;
    }

    public void setFlightTime(int i) {
        this.flightTime = i;
    }

    public void setFlightVideoSet(ArrayList<Media> arrayList) {
        this.flightVideoSet = arrayList;
    }

    public void setFlight_time(long j) {
        this.flight_time = j;
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

    public void setLocalCaptureSet(List<MediaVO> list) {
        this.localCaptureSet = list;
    }

    public void setLocalVideoSet(List<MediaVO> list) {
        this.localVideoSet = list;
    }

    public void setPhone_model(String str) {
        this.phone_model = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setTotalFlightTime(int i) {
        this.totalFlightTime = i;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVisible(boolean z) {
        this.visible = z;
    }
}
