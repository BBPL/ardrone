package com.google.api.client.googleapis;

import com.google.api.client.util.SecurityUtils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

public final class GoogleUtils {
    public static final Integer BUGFIX_VERSION = Integer.valueOf(1);
    public static final Integer MAJOR_VERSION = Integer.valueOf(1);
    public static final Integer MINOR_VERSION = Integer.valueOf(14);
    public static final String VERSION = (MAJOR_VERSION + "." + MINOR_VERSION + "." + BUGFIX_VERSION + "-beta").toString();
    static KeyStore certTrustStore;

    private GoogleUtils() {
    }

    public static KeyStore getCertificateTrustStore() throws IOException, GeneralSecurityException {
        KeyStore keyStore;
        synchronized (GoogleUtils.class) {
            try {
                if (certTrustStore == null) {
                    certTrustStore = SecurityUtils.getJavaKeyStore();
                }
                keyStore = certTrustStore;
            } finally {
                Class cls = GoogleUtils.class;
            }
        }
        return keyStore;
    }
}
