package com.google.api.client.googleapis.javanet;

import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport.Builder;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleNetHttpTransport {
    private GoogleNetHttpTransport() {
    }

    public static NetHttpTransport newTrustedTransport() throws GeneralSecurityException, IOException {
        return new Builder().trustCertificates(GoogleUtils.getCertificateTrustStore()).build();
    }
}
