package org.apache.http.conn.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpInetSocketAddress;
import org.apache.http.conn.scheme.HostNameResolver;
import org.apache.http.conn.scheme.LayeredSchemeSocketFactory;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.mortbay.jetty.HttpVersions;

@ThreadSafe
public class SSLSocketFactory implements SchemeLayeredSocketFactory, LayeredSchemeSocketFactory, LayeredSocketFactory {
    public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
    public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();
    private static final char[] EMPTY_PASSWORD = HttpVersions.HTTP_0_9.toCharArray();
    public static final String SSL = "SSL";
    public static final String SSLV2 = "SSLv2";
    public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = new StrictHostnameVerifier();
    public static final String TLS = "TLS";
    private volatile X509HostnameVerifier hostnameVerifier;
    private final HostNameResolver nameResolver;
    private final javax.net.ssl.SSLSocketFactory socketfactory;

    @Deprecated
    public SSLSocketFactory(String str, KeyStore keyStore, String str2, KeyStore keyStore2, SecureRandom secureRandom, HostNameResolver hostNameResolver) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this(createSSLContext(str, keyStore, str2, keyStore2, secureRandom, null), hostNameResolver);
    }

    public SSLSocketFactory(String str, KeyStore keyStore, String str2, KeyStore keyStore2, SecureRandom secureRandom, TrustStrategy trustStrategy, X509HostnameVerifier x509HostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this(createSSLContext(str, keyStore, str2, keyStore2, secureRandom, trustStrategy), x509HostnameVerifier);
    }

    public SSLSocketFactory(String str, KeyStore keyStore, String str2, KeyStore keyStore2, SecureRandom secureRandom, X509HostnameVerifier x509HostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this(createSSLContext(str, keyStore, str2, keyStore2, secureRandom, null), x509HostnameVerifier);
    }

    public SSLSocketFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this(TLS, null, null, keyStore, null, null, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }

    public SSLSocketFactory(KeyStore keyStore, String str) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this(TLS, keyStore, str, null, null, null, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }

    public SSLSocketFactory(KeyStore keyStore, String str, KeyStore keyStore2) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this(TLS, keyStore, str, keyStore2, null, null, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }

    public SSLSocketFactory(SSLContext sSLContext) {
        this(sSLContext, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }

    @Deprecated
    public SSLSocketFactory(SSLContext sSLContext, HostNameResolver hostNameResolver) {
        this.socketfactory = sSLContext.getSocketFactory();
        this.hostnameVerifier = BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
        this.nameResolver = hostNameResolver;
    }

    public SSLSocketFactory(SSLContext sSLContext, X509HostnameVerifier x509HostnameVerifier) {
        if (sSLContext == null) {
            throw new IllegalArgumentException("SSL context may not be null");
        }
        this.socketfactory = sSLContext.getSocketFactory();
        this.hostnameVerifier = x509HostnameVerifier;
        this.nameResolver = null;
    }

    public SSLSocketFactory(javax.net.ssl.SSLSocketFactory sSLSocketFactory, X509HostnameVerifier x509HostnameVerifier) {
        if (sSLSocketFactory == null) {
            throw new IllegalArgumentException("SSL socket factory may not be null");
        }
        this.socketfactory = sSLSocketFactory;
        this.hostnameVerifier = x509HostnameVerifier;
        this.nameResolver = null;
    }

    public SSLSocketFactory(TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this(TLS, null, null, null, null, trustStrategy, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
    }

    public SSLSocketFactory(TrustStrategy trustStrategy, X509HostnameVerifier x509HostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        this(TLS, null, null, null, null, trustStrategy, x509HostnameVerifier);
    }

    private static SSLContext createDefaultSSLContext() throws SSLInitializationException {
        try {
            return createSSLContext(TLS, null, null, null, null, null);
        } catch (Throwable e) {
            throw new SSLInitializationException("Failure initializing default SSL context", e);
        }
    }

    private static SSLContext createSSLContext(String str, KeyStore keyStore, String str2, KeyStore keyStore2, SecureRandom secureRandom, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        if (str == null) {
            str = TLS;
        }
        KeyManagerFactory instance = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        instance.init(keyStore, str2 != null ? str2.toCharArray() : null);
        KeyManager[] keyManagers = instance.getKeyManagers();
        TrustManagerFactory instance2 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        instance2.init(keyStore2);
        TrustManager[] trustManagers = instance2.getTrustManagers();
        if (!(trustManagers == null || trustStrategy == null)) {
            for (int i = 0; i < trustManagers.length; i++) {
                TrustManager trustManager = trustManagers[i];
                if (trustManager instanceof X509TrustManager) {
                    trustManagers[i] = new TrustManagerDecorator((X509TrustManager) trustManager, trustStrategy);
                }
            }
        }
        SSLContext instance3 = SSLContext.getInstance(str);
        instance3.init(keyManagers, trustManagers, secureRandom);
        return instance3;
    }

    private static SSLContext createSystemSSLContext() throws SSLInitializationException {
        try {
            return createSystemSSLContext(TLS, null);
        } catch (Throwable e) {
            throw new SSLInitializationException("Failure initializing default system SSL context", e);
        }
    }

    private static SSLContext createSystemSSLContext(String str, SecureRandom secureRandom) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        TrustManagerFactory instance;
        InputStream fileInputStream;
        char[] toCharArray;
        KeyManagerFactory instance2;
        TrustManager[] trustManagerArr = null;
        if (str == null) {
            str = TLS;
        }
        String property = System.getProperty("ssl.TrustManagerFactory.algorithm");
        if (property == null) {
            property = TrustManagerFactory.getDefaultAlgorithm();
        }
        String property2 = System.getProperty("javax.net.ssl.trustStoreType");
        if (property2 == null) {
            property2 = KeyStore.getDefaultType();
        }
        if ("none".equalsIgnoreCase(property2)) {
            instance = TrustManagerFactory.getInstance(property);
        } else {
            String property3 = System.getProperty("javax.net.ssl.trustStore");
            if (property3 != null) {
                File file = new File(property3);
                instance = TrustManagerFactory.getInstance(property);
                property = System.getProperty("javax.net.ssl.trustStoreProvider");
                KeyStore instance3 = property != null ? KeyStore.getInstance(property2, property) : KeyStore.getInstance(property2);
                property2 = System.getProperty("javax.net.ssl.trustStorePassword");
                fileInputStream = new FileInputStream(file);
                if (property2 != null) {
                    try {
                        toCharArray = property2.toCharArray();
                    } catch (Throwable th) {
                        fileInputStream.close();
                    }
                } else {
                    toCharArray = EMPTY_PASSWORD;
                }
                instance3.load(fileInputStream, toCharArray);
                fileInputStream.close();
                instance.init(instance3);
            } else {
                File file2 = new File(System.getProperty("java.home"));
                File file3 = new File(file2, "lib/security/jssecacerts");
                file2 = !file3.exists() ? new File(file2, "lib/security/cacerts") : file3;
                TrustManagerFactory instance4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                KeyStore instance5 = KeyStore.getInstance(KeyStore.getDefaultType());
                String property4 = System.getProperty("javax.net.ssl.trustStorePassword");
                fileInputStream = new FileInputStream(file2);
                if (property4 != null) {
                    try {
                        toCharArray = property4.toCharArray();
                    } catch (Throwable th2) {
                        fileInputStream.close();
                    }
                } else {
                    toCharArray = null;
                }
                instance5.load(fileInputStream, toCharArray);
                fileInputStream.close();
                instance4.init(instance5);
                instance = instance4;
            }
        }
        property = System.getProperty("ssl.KeyManagerFactory.algorithm");
        if (property == null) {
            property = KeyManagerFactory.getDefaultAlgorithm();
        }
        property2 = System.getProperty("javax.net.ssl.keyStoreType");
        if (property2 == null) {
            property2 = KeyStore.getDefaultType();
        }
        if ("none".equalsIgnoreCase(property2)) {
            instance2 = KeyManagerFactory.getInstance(property);
        } else {
            String property5 = System.getProperty("javax.net.ssl.keyStore");
            File file4 = property5 != null ? new File(property5) : null;
            if (file4 != null) {
                KeyManagerFactory instance6 = KeyManagerFactory.getInstance(property);
                property = System.getProperty("javax.net.ssl.keyStoreProvider");
                instance3 = property != null ? KeyStore.getInstance(property2, property) : KeyStore.getInstance(property2);
                String property6 = System.getProperty("javax.net.ssl.keyStorePassword");
                InputStream fileInputStream2 = new FileInputStream(file4);
                if (property6 != null) {
                    try {
                        toCharArray = property6.toCharArray();
                    } catch (Throwable th3) {
                        fileInputStream2.close();
                    }
                } else {
                    toCharArray = EMPTY_PASSWORD;
                }
                instance3.load(fileInputStream2, toCharArray);
                fileInputStream2.close();
                instance6.init(instance3, property6 != null ? property6.toCharArray() : EMPTY_PASSWORD);
                instance2 = instance6;
            } else {
                instance2 = null;
            }
        }
        SSLContext instance7 = SSLContext.getInstance(str);
        KeyManager[] keyManagers = instance2 != null ? instance2.getKeyManagers() : null;
        if (instance != null) {
            trustManagerArr = instance.getTrustManagers();
        }
        instance7.init(keyManagers, trustManagerArr, secureRandom);
        return instance7;
    }

    public static SSLSocketFactory getSocketFactory() throws SSLInitializationException {
        return new SSLSocketFactory(createDefaultSSLContext());
    }

    public static SSLSocketFactory getSystemSocketFactory() throws SSLInitializationException {
        return new SSLSocketFactory(createSystemSSLContext());
    }

    @Deprecated
    public Socket connectSocket(Socket socket, String str, int i, InetAddress inetAddress, int i2, HttpParams httpParams) throws IOException, UnknownHostException, ConnectTimeoutException {
        InetSocketAddress inetSocketAddress = null;
        if (inetAddress != null || i2 > 0) {
            if (i2 < 0) {
                i2 = 0;
            }
            inetSocketAddress = new InetSocketAddress(inetAddress, i2);
        }
        return connectSocket(socket, new HttpInetSocketAddress(new HttpHost(str, i), this.nameResolver != null ? this.nameResolver.resolve(str) : InetAddress.getByName(str), i), inetSocketAddress, httpParams);
    }

    public Socket connectSocket(Socket socket, InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2, HttpParams httpParams) throws IOException, UnknownHostException, ConnectTimeoutException {
        if (inetSocketAddress == null) {
            throw new IllegalArgumentException("Remote address may not be null");
        } else if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            Socket createSocket = socket != null ? socket : this.socketfactory.createSocket();
            if (inetSocketAddress2 != null) {
                createSocket.setReuseAddress(HttpConnectionParams.getSoReuseaddr(httpParams));
                createSocket.bind(inetSocketAddress2);
            }
            int connectionTimeout = HttpConnectionParams.getConnectionTimeout(httpParams);
            try {
                SSLSocket sSLSocket;
                createSocket.setSoTimeout(HttpConnectionParams.getSoTimeout(httpParams));
                createSocket.connect(inetSocketAddress, connectionTimeout);
                String hostName = inetSocketAddress instanceof HttpInetSocketAddress ? ((HttpInetSocketAddress) inetSocketAddress).getHttpHost().getHostName() : inetSocketAddress.getHostName();
                if (createSocket instanceof SSLSocket) {
                    sSLSocket = (SSLSocket) createSocket;
                } else {
                    sSLSocket = (SSLSocket) this.socketfactory.createSocket(createSocket, hostName, inetSocketAddress.getPort(), true);
                    prepareSocket(sSLSocket);
                }
                if (this.hostnameVerifier != null) {
                    try {
                        this.hostnameVerifier.verify(hostName, sSLSocket);
                    } catch (IOException e) {
                        try {
                            sSLSocket.close();
                        } catch (Exception e2) {
                        }
                        throw e;
                    }
                }
                return sSLSocket;
            } catch (SocketTimeoutException e3) {
                throw new ConnectTimeoutException("Connect to " + inetSocketAddress + " timed out");
            }
        }
    }

    public Socket createLayeredSocket(Socket socket, String str, int i, HttpParams httpParams) throws IOException, UnknownHostException {
        SSLSocket sSLSocket = (SSLSocket) this.socketfactory.createSocket(socket, str, i, true);
        prepareSocket(sSLSocket);
        if (this.hostnameVerifier != null) {
            this.hostnameVerifier.verify(str, sSLSocket);
        }
        return sSLSocket;
    }

    public Socket createLayeredSocket(Socket socket, String str, int i, boolean z) throws IOException, UnknownHostException {
        SSLSocket sSLSocket = (SSLSocket) this.socketfactory.createSocket(socket, str, i, z);
        prepareSocket(sSLSocket);
        if (this.hostnameVerifier != null) {
            this.hostnameVerifier.verify(str, sSLSocket);
        }
        return sSLSocket;
    }

    @Deprecated
    public Socket createSocket() throws IOException {
        SSLSocket sSLSocket = (SSLSocket) this.socketfactory.createSocket();
        prepareSocket(sSLSocket);
        return sSLSocket;
    }

    @Deprecated
    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException, UnknownHostException {
        return createLayeredSocket(socket, str, i, z);
    }

    public Socket createSocket(HttpParams httpParams) throws IOException {
        SSLSocket sSLSocket = (SSLSocket) this.socketfactory.createSocket();
        prepareSocket(sSLSocket);
        return sSLSocket;
    }

    public X509HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public boolean isSecure(Socket socket) throws IllegalArgumentException {
        if (socket == null) {
            throw new IllegalArgumentException("Socket may not be null");
        } else if (!(socket instanceof SSLSocket)) {
            throw new IllegalArgumentException("Socket not created by this factory");
        } else if (!socket.isClosed()) {
            return true;
        } else {
            throw new IllegalArgumentException("Socket is closed");
        }
    }

    protected void prepareSocket(SSLSocket sSLSocket) throws IOException {
    }

    @Deprecated
    public void setHostnameVerifier(X509HostnameVerifier x509HostnameVerifier) {
        if (x509HostnameVerifier == null) {
            throw new IllegalArgumentException("Hostname verifier may not be null");
        }
        this.hostnameVerifier = x509HostnameVerifier;
    }
}
