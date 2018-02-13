package com.parrot.freeflight.academy.model;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class MediaCluster extends Cluster {
    private static final String KPHOTOTHUMBSIZE = "kPhotoThumbSize";
    private static final String REPLACE_PHOTO_SIZE = "w1024";
    private static final String REPLACE_THUMB_PHOTO = "w512";
    private static final String THUMB_VIDEO = "/0.jpg";
    private static final String URL = "url";
    private ArrayList<Flight> flights;
    private String thumbUrl;
    private String url;

    public MediaCluster() {
        this.flights = new ArrayList();
    }

    public MediaCluster(JSONObject jSONObject, boolean z) throws JSONException {
        super(jSONObject);
        StringBuilder stringBuilder = new StringBuilder();
        if (z) {
            setUrl(jSONObject.getString("url").replace(":", ":/"));
            setThumbUrl(stringBuilder.append(jSONObject.getString("url")).append(THUMB_VIDEO).toString().replace(":", ":/"));
        } else {
            setUrl(jSONObject.getString("url").replace(KPHOTOTHUMBSIZE, REPLACE_PHOTO_SIZE));
            setThumbUrl(jSONObject.getString("url").replace(KPHOTOTHUMBSIZE, REPLACE_THUMB_PHOTO));
        }
        this.flights = new ArrayList();
    }

    public void addFlight(Flight flight) {
        this.flights.add(flight);
        increment();
    }

    public ArrayList<Flight> getFlights() {
        return this.flights;
    }

    public String getThumbUrl() {
        return this.thumbUrl;
    }

    public String getUrl() {
        return this.url;
    }

    public void setFlights(ArrayList<Flight> arrayList) {
        this.flights = arrayList;
    }

    public void setThumbUrl(String str) {
        this.thumbUrl = str;
    }

    public void setUrl(String str) {
        this.url = str;
    }
}
