package com.parrot.freeflight.academy.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Keys {
    public static Map<String, String> jsonObjectToKeys(JSONObject jSONObject) {
        Map<String, String> hashMap = new HashMap();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            try {
                String str = (String) keys.next();
                hashMap.put(str, jSONObject.getString(str));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return hashMap;
    }
}
