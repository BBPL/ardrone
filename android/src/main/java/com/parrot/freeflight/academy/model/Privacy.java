package com.parrot.freeflight.academy.model;

import java.net.MalformedURLException;
import java.text.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

public class Privacy {
    private static final String ADDRESS = "address";
    private static final String CITY = "city";
    private static final String CIVILITY = "civility";
    private static final String CONTACT = "contact";
    private static final String EMAIL = "email";
    private int address;
    private int city;
    private int civility;
    private int contact;
    private int email;

    public Privacy(JSONObject jSONObject) throws JSONException, MalformedURLException, ParseException {
        setAddress(jSONObject.getInt(ADDRESS));
        setCity(jSONObject.getInt(CITY));
        setCivility(jSONObject.getInt(CIVILITY));
        setContact(jSONObject.getInt(CONTACT));
        setEmail(jSONObject.getInt(EMAIL));
    }

    public int getAddress() {
        return this.address;
    }

    public int getCity() {
        return this.city;
    }

    public int getCivility() {
        return this.civility;
    }

    public int getContact() {
        return this.contact;
    }

    public int getEmail() {
        return this.email;
    }

    public void setAddress(int i) {
        this.address = i;
    }

    public void setCity(int i) {
        this.city = i;
    }

    public void setCivility(int i) {
        this.civility = i;
    }

    public void setContact(int i) {
        this.contact = i;
    }

    public void setEmail(int i) {
        this.email = i;
    }

    public String toString() {
        return "city:" + this.city + "\ncivility:" + this.civility + "\nemail:" + this.email + "\naddress:" + this.address + "\ncontact:" + this.contact;
    }
}
