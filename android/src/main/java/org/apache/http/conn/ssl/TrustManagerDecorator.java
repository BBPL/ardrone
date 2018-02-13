package org.apache.http.conn.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

class TrustManagerDecorator implements X509TrustManager {
    private final X509TrustManager trustManager;
    private final TrustStrategy trustStrategy;

    TrustManagerDecorator(X509TrustManager x509TrustManager, TrustStrategy trustStrategy) {
        this.trustManager = x509TrustManager;
        this.trustStrategy = trustStrategy;
    }

    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        this.trustManager.checkClientTrusted(x509CertificateArr, str);
    }

    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        if (!this.trustStrategy.isTrusted(x509CertificateArr, str)) {
            this.trustManager.checkServerTrusted(x509CertificateArr, str);
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        return this.trustManager.getAcceptedIssuers();
    }
}
