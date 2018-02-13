package com.parrot.freeflight.academy.model;

import android.util.Base64;

public class AcademyCredentials {
    private String password;
    private String username;

    public AcademyCredentials(String str, String str2) {
        this.username = str;
        this.password = str2;
    }

    public String getBasicAuthorizationString() {
        return "Basic " + new String(Base64.encode((this.username + ":" + this.password).getBytes(), 2));
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }
}
