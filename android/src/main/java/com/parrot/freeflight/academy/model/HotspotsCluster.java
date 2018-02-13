package com.parrot.freeflight.academy.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HotspotsCluster {
    private static final String INDEX = "index";
    private Cluster cluster;
    private String index;

    public HotspotsCluster(JSONObject jSONObject) throws JSONException {
        this.cluster = new Cluster(jSONObject);
        this.index = jSONObject.getString(INDEX);
    }

    public static ArrayList<HotspotsCluster> jsonToHotspotsClusters(JSONArray jSONArray) throws JSONException {
        ArrayList<HotspotsCluster> arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(new HotspotsCluster(jSONArray.getJSONObject(i)));
        }
        return arrayList;
    }

    public Cluster getCluster() {
        return this.cluster;
    }

    public String getIndex() {
        return this.index;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void setIndex(String str) {
        this.index = str;
    }
}
