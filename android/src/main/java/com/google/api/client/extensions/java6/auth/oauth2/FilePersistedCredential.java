package com.google.api.client.extensions.java6.auth.oauth2;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class FilePersistedCredential extends GenericJson {
    @Key("access_token")
    private String accessToken;
    @Key("expiration_time_millis")
    private Long expirationTimeMillis;
    @Key("refresh_token")
    private String refreshToken;

    public FilePersistedCredential clone() {
        return (FilePersistedCredential) super.clone();
    }

    void load(Credential credential) {
        credential.setAccessToken(this.accessToken);
        credential.setRefreshToken(this.refreshToken);
        credential.setExpirationTimeMilliseconds(this.expirationTimeMillis);
    }

    public FilePersistedCredential set(String str, Object obj) {
        return (FilePersistedCredential) super.set(str, obj);
    }

    void store(Credential credential) {
        this.accessToken = credential.getAccessToken();
        this.refreshToken = credential.getRefreshToken();
        this.expirationTimeMillis = credential.getExpirationTimeMilliseconds();
    }
}
