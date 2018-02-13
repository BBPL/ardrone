package com.parrot.freeflight.academy.model;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {
    private static final long serialVersionUID = 3395478818539882536L;
    private String email;
    private String username;

    public User(String str, String str2) {
        this.email = str;
        this.username = str2;
    }

    public User(JSONObject jSONObject) throws JSONException {
        this.username = jSONObject.getString("username");
        if (!jSONObject.isNull("email")) {
            this.email = jSONObject.getString("email");
        }
    }

    public boolean equals(Object obj) {
        return obj instanceof User ? this.username.equals(((User) obj).getUsername()) : false;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public void setUsername(String str) {
        this.username = str;
    }
}
