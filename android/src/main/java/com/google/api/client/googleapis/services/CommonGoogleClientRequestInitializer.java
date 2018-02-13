package com.google.api.client.googleapis.services;

import java.io.IOException;

public class CommonGoogleClientRequestInitializer implements GoogleClientRequestInitializer {
    private final String key;
    private final String userIp;

    public CommonGoogleClientRequestInitializer() {
        this(null);
    }

    public CommonGoogleClientRequestInitializer(String str) {
        this(str, null);
    }

    public CommonGoogleClientRequestInitializer(String str, String str2) {
        this.key = str;
        this.userIp = str2;
    }

    public final String getKey() {
        return this.key;
    }

    public final String getUserIp() {
        return this.userIp;
    }

    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
        if (this.key != null) {
            abstractGoogleClientRequest.put("key", (Object) this.key);
        }
        if (this.userIp != null) {
            abstractGoogleClientRequest.put("userIp", (Object) this.userIp);
        }
    }
}
