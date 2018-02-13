package org.apache.http.client.fluent;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLInitializationException;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;

public class Executor {
    static final DefaultHttpClient CLIENT = new DefaultHttpClient(CONNMGR);
    static final PoolingClientConnectionManager CONNMGR;
    private final AuthCache authCache = new BasicAuthCache();
    private CookieStore cookieStore;
    private CredentialsProvider credentialsProvider;
    private final HttpClient httpclient;
    private final BasicHttpContext localContext = new BasicHttpContext();

    static {
        SchemeSocketFactory systemSocketFactory;
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        try {
            systemSocketFactory = SSLSocketFactory.getSystemSocketFactory();
        } catch (SSLInitializationException e) {
            try {
                SSLContext instance = SSLContext.getInstance(SSLSocketFactory.TLS);
                instance.init(null, null, null);
                systemSocketFactory = new SSLSocketFactory(instance);
            } catch (SecurityException e2) {
                systemSocketFactory = null;
            } catch (KeyManagementException e3) {
                systemSocketFactory = null;
            } catch (NoSuchAlgorithmException e4) {
                systemSocketFactory = null;
            }
        }
        if (systemSocketFactory != null) {
            schemeRegistry.register(new Scheme("https", 443, systemSocketFactory));
        }
        CONNMGR = new PoolingClientConnectionManager(schemeRegistry);
        CONNMGR.setDefaultMaxPerRoute(100);
        CONNMGR.setMaxTotal(200);
    }

    Executor(HttpClient httpClient) {
        this.httpclient = httpClient;
    }

    public static Executor newInstance() {
        return new Executor(CLIENT);
    }

    public static Executor newInstance(HttpClient httpClient) {
        if (httpClient == null) {
            httpClient = CLIENT;
        }
        return new Executor(httpClient);
    }

    public static void registerScheme(Scheme scheme) {
        CONNMGR.getSchemeRegistry().register(scheme);
    }

    public static void unregisterScheme(String str) {
        CONNMGR.getSchemeRegistry().unregister(str);
    }

    public Executor auth(String str, String str2) {
        return auth(new UsernamePasswordCredentials(str, str2));
    }

    public Executor auth(String str, String str2, String str3, String str4) {
        return auth(new NTCredentials(str, str2, str3, str4));
    }

    public Executor auth(HttpHost httpHost, String str, String str2) {
        return auth(httpHost, new UsernamePasswordCredentials(str, str2));
    }

    public Executor auth(HttpHost httpHost, String str, String str2, String str3, String str4) {
        return auth(httpHost, new NTCredentials(str, str2, str3, str4));
    }

    public Executor auth(HttpHost httpHost, Credentials credentials) {
        return auth(httpHost != null ? new AuthScope(httpHost) : AuthScope.ANY, credentials);
    }

    public Executor auth(AuthScope authScope, Credentials credentials) {
        if (this.credentialsProvider == null) {
            this.credentialsProvider = new BasicCredentialsProvider();
        }
        this.credentialsProvider.setCredentials(authScope, credentials);
        return this;
    }

    public Executor auth(Credentials credentials) {
        return auth(AuthScope.ANY, credentials);
    }

    public Executor authPreemptive(HttpHost httpHost) {
        this.authCache.put(httpHost, new BasicScheme(ChallengeState.TARGET));
        return this;
    }

    public Executor authPreemptiveProxy(HttpHost httpHost) {
        this.authCache.put(httpHost, new BasicScheme(ChallengeState.PROXY));
        return this;
    }

    public Executor clearAuth() {
        if (this.credentialsProvider != null) {
            this.credentialsProvider.clear();
        }
        return this;
    }

    public Executor clearCookies() {
        if (this.cookieStore != null) {
            this.cookieStore.clear();
        }
        return this;
    }

    public Executor cookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
        return this;
    }

    public Response execute(Request request) throws ClientProtocolException, IOException {
        this.localContext.setAttribute(ClientContext.CREDS_PROVIDER, this.credentialsProvider);
        this.localContext.setAttribute(ClientContext.AUTH_CACHE, this.authCache);
        this.localContext.setAttribute(ClientContext.COOKIE_STORE, this.cookieStore);
        HttpUriRequest httpRequest = request.getHttpRequest();
        httpRequest.reset();
        return new Response(this.httpclient.execute(httpRequest, this.localContext));
    }
}
