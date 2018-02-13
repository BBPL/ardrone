package com.google.api.client.util;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

public final class SslUtils {

    static final class C04651 implements X509TrustManager {
        C04651() {
        }

        public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    static final class C04662 implements HostnameVerifier {
        C04662() {
        }

        public boolean verify(String str, SSLSession sSLSession) {
            return true;
        }
    }

    private SslUtils() {
    }

    public static KeyManagerFactory getDefaultKeyManagerFactory() throws NoSuchAlgorithmException {
        return KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    }

    public static TrustManagerFactory getDefaultTrustManagerFactory() throws NoSuchAlgorithmException {
        return TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    }

    public static KeyManagerFactory getPkixKeyManagerFactory() throws NoSuchAlgorithmException {
        return KeyManagerFactory.getInstance("PKIX");
    }

    public static TrustManagerFactory getPkixTrustManagerFactory() throws NoSuchAlgorithmException {
        return TrustManagerFactory.getInstance("PKIX");
    }

    public static SSLContext getSslContext() throws NoSuchAlgorithmException {
        return SSLContext.getInstance(SSLSocketFactory.SSL);
    }

    public static SSLContext getTlsSslContext() throws NoSuchAlgorithmException {
        return SSLContext.getInstance(SSLSocketFactory.TLS);
    }

    public static SSLContext initSslContext(SSLContext sSLContext, KeyStore keyStore, TrustManagerFactory trustManagerFactory) throws GeneralSecurityException {
        trustManagerFactory.init(keyStore);
        sSLContext.init(null, trustManagerFactory.getTrustManagers(), null);
        return sSLContext;
    }

    public static HostnameVerifier trustAllHostnameVerifier() {
        return new C04662();
    }

    public static SSLContext trustAllSSLContext() throws GeneralSecurityException {
        C04651 c04651 = new C04651();
        SSLContext tlsSslContext = getTlsSslContext();
        tlsSslContext.init(null, new TrustManager[]{c04651}, null);
        return tlsSslContext;
    }
}
