package org.mortbay.jetty.security;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.mortbay.io.EndPoint;
import org.mortbay.io.bio.SocketEndPoint;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.log.Log;
import org.mortbay.resource.Resource;

public class SslSocketConnector extends SocketConnector {
    static final String CACHED_INFO_ATTR;
    public static final String DEFAULT_KEYSTORE = new StringBuffer().append(System.getProperty("user.home")).append(File.separator).append(".keystore").toString();
    public static final String KEYPASSWORD_PROPERTY = "jetty.ssl.keypassword";
    public static final String PASSWORD_PROPERTY = "jetty.ssl.password";
    static Class class$org$mortbay$jetty$security$SslSocketConnector$CachedInfo;
    private boolean _allowRenegotiate;
    private String[] _excludeCipherSuites = null;
    private int _handshakeTimeout;
    private transient Password _keyPassword;
    private String _keystore = DEFAULT_KEYSTORE;
    private String _keystoreType = "JKS";
    private boolean _needClientAuth = false;
    private transient Password _password;
    private String _protocol = SSLSocketFactory.TLS;
    private String _provider;
    private String _secureRandomAlgorithm;
    private String _sslKeyManagerFactoryAlgorithm;
    private String _sslTrustManagerFactoryAlgorithm;
    private transient Password _trustPassword;
    private String _truststore;
    private String _truststoreType;
    private boolean _wantClientAuth;

    private class CachedInfo {
        private X509Certificate[] _certs;
        private Integer _keySize;
        private final SslSocketConnector this$0;

        CachedInfo(SslSocketConnector sslSocketConnector, Integer num, X509Certificate[] x509CertificateArr) {
            this.this$0 = sslSocketConnector;
            this._keySize = num;
            this._certs = x509CertificateArr;
        }

        X509Certificate[] getCerts() {
            return this._certs;
        }

        Integer getKeySize() {
            return this._keySize;
        }
    }

    public class SslConnection extends Connection {
        private final SslSocketConnector this$0;

        class C13401 implements HandshakeCompletedListener {
            boolean handshook = false;
            private final SslConnection this$1;
            private final SSLSocket val$ssl;

            C13401(SslConnection sslConnection, SSLSocket sSLSocket) {
                this.this$1 = sslConnection;
                this.val$ssl = sSLSocket;
            }

            public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
                if (!this.handshook) {
                    this.handshook = true;
                } else if (!SslSocketConnector.access$100(SslConnection.access$000(this.this$1))) {
                    Log.warn(new StringBuffer().append("SSL renegotiate denied: ").append(this.val$ssl).toString());
                    try {
                        this.val$ssl.close();
                    } catch (Throwable e) {
                        Log.warn(e);
                    }
                }
            }
        }

        public SslConnection(SslSocketConnector sslSocketConnector, Socket socket) throws IOException {
            this.this$0 = sslSocketConnector;
            super(sslSocketConnector, socket);
        }

        static SslSocketConnector access$000(SslConnection sslConnection) {
            return sslConnection.this$0;
        }

        public void run() {
            try {
                int handshakeTimeout = this.this$0.getHandshakeTimeout();
                int soTimeout = this._socket.getSoTimeout();
                if (handshakeTimeout > 0) {
                    this._socket.setSoTimeout(handshakeTimeout);
                }
                SSLSocket sSLSocket = (SSLSocket) this._socket;
                sSLSocket.addHandshakeCompletedListener(new C13401(this, sSLSocket));
                sSLSocket.startHandshake();
                if (handshakeTimeout > 0) {
                    this._socket.setSoTimeout(soTimeout);
                }
                super.run();
            } catch (Throwable e) {
                Log.warn(e);
                try {
                    close();
                } catch (Throwable e2) {
                    Log.ignore(e2);
                }
            } catch (Throwable e22) {
                Log.debug(e22);
                try {
                    close();
                } catch (Throwable e222) {
                    Log.ignore(e222);
                }
            }
        }

        public void shutdownOutput() throws IOException {
            close();
        }
    }

    static {
        Class class$;
        if (class$org$mortbay$jetty$security$SslSocketConnector$CachedInfo == null) {
            class$ = class$("org.mortbay.jetty.security.SslSocketConnector$CachedInfo");
            class$org$mortbay$jetty$security$SslSocketConnector$CachedInfo = class$;
        } else {
            class$ = class$org$mortbay$jetty$security$SslSocketConnector$CachedInfo;
        }
        CACHED_INFO_ATTR = class$.getName();
    }

    public SslSocketConnector() {
        this._sslKeyManagerFactoryAlgorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm") == null ? "SunX509" : Security.getProperty("ssl.KeyManagerFactory.algorithm");
        this._sslTrustManagerFactoryAlgorithm = Security.getProperty("ssl.TrustManagerFactory.algorithm") == null ? "SunX509" : Security.getProperty("ssl.TrustManagerFactory.algorithm");
        this._truststoreType = "JKS";
        this._wantClientAuth = false;
        this._handshakeTimeout = 0;
        this._allowRenegotiate = false;
    }

    static boolean access$100(SslSocketConnector sslSocketConnector) {
        return sslSocketConnector._allowRenegotiate;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    private static X509Certificate[] getCertChain(SSLSession sSLSession) {
        try {
            X509Certificate[] x509CertificateArr;
            javax.security.cert.X509Certificate[] peerCertificateChain = sSLSession.getPeerCertificateChain();
            if (peerCertificateChain == null || peerCertificateChain.length == 0) {
                x509CertificateArr = null;
            } else {
                int length = peerCertificateChain.length;
                X509Certificate[] x509CertificateArr2 = new X509Certificate[length];
                CertificateFactory instance = CertificateFactory.getInstance("X.509");
                for (int i = 0; i < length; i++) {
                    x509CertificateArr2[i] = (X509Certificate) instance.generateCertificate(new ByteArrayInputStream(peerCertificateChain[i].getEncoded()));
                }
                x509CertificateArr = x509CertificateArr2;
            }
            return x509CertificateArr;
        } catch (SSLPeerUnverifiedException e) {
            return null;
        } catch (Throwable e2) {
            Log.warn(Log.EXCEPTION, e2);
            return null;
        }
    }

    public void accept(int i) throws IOException, InterruptedException {
        try {
            Socket accept = this._serverSocket.accept();
            configure(accept);
            new SslConnection(this, accept).dispatch();
        } catch (Throwable e) {
            Log.warn(e);
            try {
                stop();
            } catch (Throwable e2) {
                Log.warn(e2);
                throw new IllegalStateException(e2.getMessage());
            }
        }
    }

    protected void configure(Socket socket) throws IOException {
        super.configure(socket);
    }

    protected SSLServerSocketFactory createFactory() throws Exception {
        Throwable th;
        Throwable th2;
        InputStream inputStream = null;
        if (this._truststore == null) {
            this._truststore = this._keystore;
            this._truststoreType = this._keystoreType;
        }
        try {
            InputStream inputStream2 = this._keystore != null ? Resource.newResource(this._keystore).getInputStream() : null;
            try {
                KeyStore instance = KeyStore.getInstance(this._keystoreType);
                instance.load(inputStream2, this._password == null ? null : this._password.toString().toCharArray());
                if (inputStream2 != null) {
                    inputStream2.close();
                }
                KeyManagerFactory instance2 = KeyManagerFactory.getInstance(this._sslKeyManagerFactoryAlgorithm);
                instance2.init(instance, this._keyPassword == null ? null : this._keyPassword.toString().toCharArray());
                KeyManager[] keyManagers = instance2.getKeyManagers();
                try {
                    inputStream2 = this._truststore != null ? Resource.newResource(this._truststore).getInputStream() : null;
                    try {
                        SecureRandom instance3;
                        KeyStore instance4 = KeyStore.getInstance(this._truststoreType);
                        instance4.load(inputStream2, this._trustPassword == null ? null : this._trustPassword.toString().toCharArray());
                        if (inputStream2 != null) {
                            inputStream2.close();
                        }
                        TrustManagerFactory instance5 = TrustManagerFactory.getInstance(this._sslTrustManagerFactoryAlgorithm);
                        instance5.init(instance4);
                        TrustManager[] trustManagers = instance5.getTrustManagers();
                        if (this._secureRandomAlgorithm != null) {
                            instance3 = SecureRandom.getInstance(this._secureRandomAlgorithm);
                        }
                        SSLContext instance6 = this._provider == null ? SSLContext.getInstance(this._protocol) : SSLContext.getInstance(this._protocol, this._provider);
                        instance6.init(keyManagers, trustManagers, instance3);
                        return instance6.getServerSocketFactory();
                    } catch (Throwable th3) {
                        th = th3;
                        inputStream = inputStream2;
                        th2 = th;
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        throw th2;
                    }
                } catch (Throwable th4) {
                    th2 = th4;
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    throw th2;
                }
            } catch (Throwable th32) {
                th = th32;
                inputStream = inputStream2;
                th2 = th;
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th2;
            }
        } catch (Throwable th5) {
            th2 = th5;
            if (inputStream != null) {
                inputStream.close();
            }
            throw th2;
        }
    }

    public void customize(EndPoint endPoint, Request request) throws IOException {
        super.customize(endPoint, request);
        request.setScheme("https");
        try {
            Object certs;
            Object obj;
            SSLSession session = ((SSLSocket) ((SocketEndPoint) endPoint).getTransport()).getSession();
            String cipherSuite = session.getCipherSuite();
            CachedInfo cachedInfo = (CachedInfo) session.getValue(CACHED_INFO_ATTR);
            if (cachedInfo != null) {
                Integer keySize = cachedInfo.getKeySize();
                certs = cachedInfo.getCerts();
                obj = keySize;
            } else {
                obj = new Integer(ServletSSL.deduceKeyLength(cipherSuite));
                certs = getCertChain(session);
                session.putValue(CACHED_INFO_ATTR, new CachedInfo(this, obj, certs));
            }
            if (certs != null) {
                request.setAttribute("javax.servlet.request.X509Certificate", certs);
            } else if (this._needClientAuth) {
                throw new IllegalStateException("no client auth");
            }
            request.setAttribute("javax.servlet.request.cipher_suite", cipherSuite);
            request.setAttribute("javax.servlet.request.key_size", obj);
        } catch (Throwable e) {
            Log.warn(Log.EXCEPTION, e);
        }
    }

    public String[] getExcludeCipherSuites() {
        return this._excludeCipherSuites;
    }

    public int getHandshakeTimeout() {
        return this._handshakeTimeout;
    }

    public String getKeystore() {
        return this._keystore;
    }

    public String getKeystoreType() {
        return this._keystoreType;
    }

    public boolean getNeedClientAuth() {
        return this._needClientAuth;
    }

    public String getProtocol() {
        return this._protocol;
    }

    public String getProvider() {
        return this._provider;
    }

    public String getSecureRandomAlgorithm() {
        return this._secureRandomAlgorithm;
    }

    public String getSslKeyManagerFactoryAlgorithm() {
        return this._sslKeyManagerFactoryAlgorithm;
    }

    public String getSslTrustManagerFactoryAlgorithm() {
        return this._sslTrustManagerFactoryAlgorithm;
    }

    public String getTruststore() {
        return this._truststore;
    }

    public String getTruststoreType() {
        return this._truststoreType;
    }

    public boolean getWantClientAuth() {
        return this._wantClientAuth;
    }

    public boolean isAllowRenegotiate() {
        return this._allowRenegotiate;
    }

    public boolean isConfidential(Request request) {
        int confidentialPort = getConfidentialPort();
        return confidentialPort == 0 || confidentialPort == request.getServerPort();
    }

    public boolean isIntegral(Request request) {
        int integralPort = getIntegralPort();
        return integralPort == 0 || integralPort == request.getServerPort();
    }

    protected ServerSocket newServerSocket(String str, int i, int i2) throws IOException {
        try {
            SSLServerSocketFactory createFactory = createFactory();
            SSLServerSocket sSLServerSocket = (SSLServerSocket) (str == null ? createFactory.createServerSocket(i, i2) : createFactory.createServerSocket(i, i2, InetAddress.getByName(str)));
            if (this._wantClientAuth) {
                sSLServerSocket.setWantClientAuth(this._wantClientAuth);
            }
            if (this._needClientAuth) {
                sSLServerSocket.setNeedClientAuth(this._needClientAuth);
            }
            if (this._excludeCipherSuites != null && this._excludeCipherSuites.length > 0) {
                List<String> asList = Arrays.asList(this._excludeCipherSuites);
                List arrayList = new ArrayList(Arrays.asList(sSLServerSocket.getEnabledCipherSuites()));
                for (String str2 : asList) {
                    if (arrayList.contains(str2)) {
                        arrayList.remove(str2);
                    }
                }
                sSLServerSocket.setEnabledCipherSuites((String[]) arrayList.toArray(new String[arrayList.size()]));
            }
            return sSLServerSocket;
        } catch (IOException e) {
            throw e;
        } catch (Throwable e2) {
            Log.warn(e2.toString());
            Log.debug(e2);
            throw new IOException(new StringBuffer().append("!JsseListener: ").append(e2).toString());
        }
    }

    public void setAllowRenegotiate(boolean z) {
        this._allowRenegotiate = z;
    }

    public void setExcludeCipherSuites(String[] strArr) {
        this._excludeCipherSuites = strArr;
    }

    public void setHandshakeTimeout(int i) {
        this._handshakeTimeout = i;
    }

    public void setKeyPassword(String str) {
        this._keyPassword = Password.getPassword(KEYPASSWORD_PROPERTY, str, null);
    }

    public void setKeystore(String str) {
        this._keystore = str;
    }

    public void setKeystoreType(String str) {
        this._keystoreType = str;
    }

    public void setNeedClientAuth(boolean z) {
        this._needClientAuth = z;
    }

    public void setPassword(String str) {
        this._password = Password.getPassword(PASSWORD_PROPERTY, str, null);
    }

    public void setProtocol(String str) {
        this._protocol = str;
    }

    public void setProvider(String str) {
        this._provider = str;
    }

    public void setSecureRandomAlgorithm(String str) {
        this._secureRandomAlgorithm = str;
    }

    public void setSslKeyManagerFactoryAlgorithm(String str) {
        this._sslKeyManagerFactoryAlgorithm = str;
    }

    public void setSslTrustManagerFactoryAlgorithm(String str) {
        this._sslTrustManagerFactoryAlgorithm = str;
    }

    public void setTrustPassword(String str) {
        this._trustPassword = Password.getPassword(PASSWORD_PROPERTY, str, null);
    }

    public void setTruststore(String str) {
        this._truststore = str;
    }

    public void setTruststoreType(String str) {
        this._truststoreType = str;
    }

    public void setWantClientAuth(boolean z) {
        this._wantClientAuth = z;
    }
}
