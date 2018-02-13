package com.parrot.freeflight.academy.model;

import com.parrot.freeflight.academy.activities.AcademyMapActivity.MEDIA_MODE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FlightsCluster {
    private static final String GPS = "gps";
    private static final String INDEX = "index";
    private static final String MEDIAS = "medias";
    private static final String PHOTOS = "photos";
    private static final String TAG = FlightsCluster.class.getSimpleName();
    private static final String VIDEOS = "videos";
    private Cluster cluster;
    private ArrayList<Flight> flights;
    private String index;
    private Map<String, MediaCluster> mediaCluster;

    public FlightsCluster() {
        this.mediaCluster = new HashMap();
        this.flights = new ArrayList();
        this.cluster = new Cluster();
    }

    public FlightsCluster(JSONObject jSONObject) throws JSONException {
        this.mediaCluster = new HashMap();
        this.flights = new ArrayList();
        this.cluster = new Cluster(jSONObject);
        this.index = jSONObject.getString(INDEX);
        if (jSONObject.has(MEDIAS)) {
            this.mediaCluster.put(MEDIAS, new MediaCluster(jSONObject.getJSONObject(MEDIAS), true));
        }
        if (jSONObject.has(VIDEOS)) {
            this.mediaCluster.put(VIDEOS, new MediaCluster(jSONObject.getJSONObject(VIDEOS), true));
        }
        if (jSONObject.has(PHOTOS)) {
            this.mediaCluster.put(PHOTOS, new MediaCluster(jSONObject.getJSONObject(PHOTOS), false));
        }
        if (jSONObject.has(GPS)) {
            this.mediaCluster.put(GPS, new MediaCluster(jSONObject.getJSONObject(GPS), false));
        }
    }

    public static HashMap<String, FlightsCluster> jsonToFlightsClusters(JSONArray jSONArray) throws JSONException {
        HashMap<String, FlightsCluster> hashMap = new HashMap();
        for (int i = 0; i < jSONArray.length(); i++) {
            FlightsCluster flightsCluster = new FlightsCluster(jSONArray.getJSONObject(i));
            hashMap.put(flightsCluster.getIndex(), flightsCluster);
        }
        return hashMap;
    }

    public Cluster getCluster() {
        return this.cluster;
    }

    public ArrayList<Flight> getFlights() {
        return this.flights;
    }

    public ArrayList<Flight> getFlights(MEDIA_MODE media_mode) {
        switch (media_mode) {
            case ALL:
                return this.mediaCluster.get(MEDIAS) != null ? ((MediaCluster) this.mediaCluster.get(MEDIAS)).getFlights() : this.flights;
            case PHOTOS:
                if (hasPhotos()) {
                    return this.mediaCluster.get(PHOTOS) != null ? ((MediaCluster) this.mediaCluster.get(PHOTOS)).getFlights() : this.flights;
                }
                break;
            case VIDEOS:
                break;
        }
        if (hasVideos()) {
            return this.mediaCluster.get(VIDEOS) != null ? ((MediaCluster) this.mediaCluster.get(VIDEOS)).getFlights() : this.flights;
        }
        return null;
    }

    public String getIndex() {
        return this.index;
    }

    public Map<String, MediaCluster> getMediaCluster() {
        return this.mediaCluster;
    }

    public boolean hasGps() {
        return this.mediaCluster.containsKey(GPS);
    }

    public boolean hasMedias() {
        return this.mediaCluster.containsKey(PHOTOS) && this.mediaCluster.containsKey(VIDEOS);
    }

    public boolean hasPhotos() {
        return this.mediaCluster.containsKey(PHOTOS);
    }

    public boolean hasVideos() {
        return this.mediaCluster.containsKey(VIDEOS);
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void setFlights(ArrayList<Flight> arrayList) {
        this.flights = arrayList;
    }

    public void setIndex(String str) {
        this.index = str;
    }

    public void setMediaCluster(Map<String, MediaCluster> map) {
        this.mediaCluster = map;
    }

    public void switchGeneralLocation(FlightsCluster flightsCluster) {
        getCluster().setGps_latitude(flightsCluster.getCluster().getGps_latitude());
        getCluster().setGps_longitude(flightsCluster.getCluster().getGps_longitude());
    }

    public void switchGpsLocation(FlightsCluster flightsCluster) {
        if (this.mediaCluster.containsKey(GPS)) {
            ((MediaCluster) this.mediaCluster.get(GPS)).setGps_latitude(flightsCluster.getCluster().getGps_latitude());
            ((MediaCluster) this.mediaCluster.get(GPS)).setGps_longitude(flightsCluster.getCluster().getGps_longitude());
        }
    }

    public void switchPhotoLocation(FlightsCluster flightsCluster) {
        if (this.mediaCluster.containsKey(PHOTOS) && flightsCluster.getMediaCluster().get(PHOTOS) != null) {
            ((MediaCluster) this.mediaCluster.get(PHOTOS)).setGps_latitude(((MediaCluster) flightsCluster.getMediaCluster().get(PHOTOS)).getGps_latitude());
            ((MediaCluster) this.mediaCluster.get(PHOTOS)).setGps_longitude(((MediaCluster) flightsCluster.getMediaCluster().get(PHOTOS)).getGps_longitude());
        }
    }

    public void switchVideoLocation(FlightsCluster flightsCluster) {
        if (this.mediaCluster.containsKey(VIDEOS)) {
            if (flightsCluster.getMediaCluster().get(VIDEOS) != null) {
                ((MediaCluster) this.mediaCluster.get(VIDEOS)).setGps_latitude(((MediaCluster) flightsCluster.getMediaCluster().get(VIDEOS)).getGps_latitude());
            }
            if (flightsCluster.getMediaCluster().get(VIDEOS) != null) {
                ((MediaCluster) this.mediaCluster.get(VIDEOS)).setGps_longitude(((MediaCluster) flightsCluster.getMediaCluster().get(VIDEOS)).getGps_longitude());
            }
        }
    }

    public String toString() {
        return this.cluster.toString();
    }
}
