package com.parrot.freeflight.academy.model;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.jetty.HttpVersions;

public class Country {
    private static final String ISO = "iso";
    private static final String ISO3 = "iso3";
    private static final String LEVEL = "level";
    private static final String NAME = "name";
    private static final String NUMCODE = "numcode";
    private static final String PRINTABLE_NAME = "printable_name";
    private String iso;
    private String iso3;
    private int level;
    private String name;
    private int numcode;
    private String printable_name;

    public Country() {
        this.name = HttpVersions.HTTP_0_9;
    }

    public Country(JSONObject jSONObject) throws JSONException {
        setIso(jSONObject.getString(ISO));
        setIso3(jSONObject.getString(ISO3));
        setLevel(jSONObject.getInt(LEVEL));
        setName(jSONObject.getString(NAME));
        setNumcode(jSONObject.getInt(NUMCODE));
        setPrintable_name(jSONObject.getString(PRINTABLE_NAME));
    }

    public static ArrayList<Country> jsonToCountries(JSONArray jSONArray) throws JSONException, MalformedURLException, ParseException {
        ArrayList<Country> arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONArray jSONArray2 = jSONArray.getJSONArray(i);
            Country country = new Country();
            country.setIso(jSONArray2.getString(0));
            country.setName(jSONArray2.getString(1));
            arrayList.add(country);
        }
        return arrayList;
    }

    public String getIso() {
        return this.iso;
    }

    public String getIso3() {
        return this.iso3;
    }

    public int getLevel() {
        return this.level;
    }

    public String getName() {
        return !this.name.equals(HttpVersions.HTTP_0_9) ? this.name : null;
    }

    public int getNumcode() {
        return this.numcode;
    }

    public String getPrintable_name() {
        return this.printable_name;
    }

    public void setIso(String str) {
        this.iso = str;
    }

    public void setIso3(String str) {
        this.iso3 = str;
    }

    public void setLevel(int i) {
        this.level = i;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setNumcode(int i) {
        this.numcode = i;
    }

    public void setPrintable_name(String str) {
        this.printable_name = str;
    }

    public String toString() {
        return this.name.toUpperCase();
    }
}
