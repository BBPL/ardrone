package com.parrot.freeflight.academy.utils;

import android.location.Address;
import com.parrot.freeflight.utils.StreamUtils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.jetty.HttpStatus;

public class GeocoderAlt {
    private static String downloadString(String str) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("GET");
        try {
            httpURLConnection.connect();
            String stream2String = StreamUtils.stream2String(httpURLConnection.getInputStream());
            return stream2String;
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public static List<Address> getFromLocationAlt(double d, double d2) throws JSONException, IOException {
        return string2AddressList(downloadString(String.format(Locale.ENGLISH, "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language=" + Locale.getDefault().getCountry(), new Object[]{Double.valueOf(d), Double.valueOf(d2)})));
    }

    public static List<Address> getFromLocationNameAlt(String str) throws IOException, JSONException {
        return string2AddressList(downloadString("http://maps.googleapis.com/maps/api/geocode/json?address=" + str + "&sensor=true"));
    }

    private static List<Address> string2AddressList(String str) throws JSONException {
        List<Address> arrayList = new ArrayList();
        JSONObject jSONObject = new JSONObject(str);
        if (HttpStatus.OK.equalsIgnoreCase(jSONObject.getString("status"))) {
            JSONArray jSONArray = jSONObject.getJSONArray("results");
            String str2 = null;
            String str3 = null;
            for (int i = 0; i < jSONArray.length(); i++) {
                int i2;
                JSONArray jSONArray2;
                JSONObject jSONObject2;
                JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                if (str3 == null) {
                    JSONArray jSONArray3 = jSONObject3.getJSONArray("types");
                    for (i2 = 0; i2 < jSONArray3.length(); i2++) {
                        if ("country".equalsIgnoreCase(jSONArray3.getString(i2))) {
                            jSONArray2 = jSONObject3.getJSONArray("address_components");
                            if (jSONArray2.length() > 0) {
                                jSONObject2 = jSONArray2.getJSONObject(0);
                                str2 = jSONObject2.getString("short_name");
                                str3 = jSONObject2.getString("long_name");
                                break;
                            }
                        }
                    }
                }
                Address address = new Address(Locale.getDefault());
                jSONArray2 = jSONObject3.getJSONArray("address_components");
                for (i2 = 0; i2 < jSONArray2.length(); i2++) {
                    address.setAddressLine(i2, jSONArray2.getJSONObject(i2).getString("long_name"));
                }
                jSONObject2 = jSONObject3.getJSONObject("geometry");
                if (jSONObject2 != null) {
                    jSONObject2 = jSONObject2.getJSONObject("location");
                    if (jSONObject2 != null) {
                        double d = jSONObject2.getDouble("lat");
                        double d2 = jSONObject2.getDouble("lng");
                        address.setLatitude(d);
                        address.setLongitude(d2);
                    }
                }
                arrayList.add(address);
            }
            if (str3 != null) {
                for (Address address2 : arrayList) {
                    address2.setCountryName(str3);
                    address2.setCountryCode(str2);
                }
            }
        }
        return arrayList;
    }
}
