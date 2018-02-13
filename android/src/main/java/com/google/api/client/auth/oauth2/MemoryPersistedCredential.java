package com.google.api.client.auth.oauth2;

class MemoryPersistedCredential {
    private String accessToken;
    private Long expirationTimeMillis;
    private String refreshToken;

    MemoryPersistedCredential() {
    }

    void load(Credential credential) {
        credential.setAccessToken(this.accessToken);
        credential.setRefreshToken(this.refreshToken);
        credential.setExpirationTimeMilliseconds(this.expirationTimeMillis);
    }

    void store(Credential credential) {
        this.accessToken = credential.getAccessToken();
        this.refreshToken = credential.getRefreshToken();
        this.expirationTimeMillis = credential.getExpirationTimeMilliseconds();
    }
}
