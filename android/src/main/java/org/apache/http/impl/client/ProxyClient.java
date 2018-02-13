package org.apache.http.impl.client;

import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.SSLSession;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestProxyAuthentication;
import org.apache.http.conn.HttpRoutedConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

public class ProxyClient {
    private final AuthSchemeRegistry authSchemeRegistry;
    private final HttpAuthenticator authenticator;
    private final HttpProcessor httpProcessor;
    private final HttpParams params;
    private final AuthState proxyAuthState;
    private final ProxyAuthenticationStrategy proxyAuthStrategy;
    private final HttpRequestExecutor requestExec;
    private final ConnectionReuseStrategy reuseStrategy;

    static class ProxyConnection extends DefaultHttpClientConnection implements HttpRoutedConnection {
        private final HttpRoute route;

        ProxyConnection(HttpRoute httpRoute) {
            this.route = httpRoute;
        }

        public HttpRoute getRoute() {
            return this.route;
        }

        public SSLSession getSSLSession() {
            return null;
        }

        public Socket getSocket() {
            return super.getSocket();
        }

        public boolean isSecure() {
            return false;
        }
    }

    public ProxyClient() {
        this(new BasicHttpParams());
    }

    public ProxyClient(HttpParams httpParams) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        this.httpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[]{new RequestContent(), new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent(), new RequestProxyAuthentication()});
        this.requestExec = new HttpRequestExecutor();
        this.proxyAuthStrategy = new ProxyAuthenticationStrategy();
        this.authenticator = new HttpAuthenticator();
        this.proxyAuthState = new AuthState();
        this.authSchemeRegistry = new AuthSchemeRegistry();
        this.authSchemeRegistry.register(AuthPolicy.BASIC, new BasicSchemeFactory());
        this.authSchemeRegistry.register(AuthPolicy.DIGEST, new DigestSchemeFactory());
        this.authSchemeRegistry.register(AuthPolicy.NTLM, new NTLMSchemeFactory());
        this.authSchemeRegistry.register(AuthPolicy.SPNEGO, new SPNegoSchemeFactory());
        this.authSchemeRegistry.register(AuthPolicy.KERBEROS, new KerberosSchemeFactory());
        this.reuseStrategy = new DefaultConnectionReuseStrategy();
        this.params = httpParams;
    }

    public AuthSchemeRegistry getAuthSchemeRegistry() {
        return this.authSchemeRegistry;
    }

    public HttpParams getParams() {
        return this.params;
    }

    public Socket tunnel(HttpHost httpHost, HttpHost httpHost2, Credentials credentials) throws IOException, HttpException {
        ProxyConnection proxyConnection = new ProxyConnection(new HttpRoute(httpHost));
        HttpContext basicHttpContext = new BasicHttpContext();
        while (true) {
            if (!proxyConnection.isOpen()) {
                proxyConnection.bind(new Socket(httpHost.getHostName(), httpHost.getPort()), this.params);
            }
            String hostName = httpHost2.getHostName();
            int port = httpHost2.getPort();
            if (port < 0) {
                port = 80;
            }
            StringBuilder stringBuilder = new StringBuilder(hostName.length() + 6);
            stringBuilder.append(hostName);
            stringBuilder.append(':');
            stringBuilder.append(Integer.toString(port));
            HttpRequest basicHttpRequest = new BasicHttpRequest("CONNECT", stringBuilder.toString(), HttpProtocolParams.getVersion(this.params));
            basicHttpRequest.setParams(this.params);
            BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
            basicCredentialsProvider.setCredentials(new AuthScope(httpHost), credentials);
            basicHttpContext.setAttribute(ExecutionContext.HTTP_TARGET_HOST, httpHost2);
            basicHttpContext.setAttribute(ExecutionContext.HTTP_PROXY_HOST, httpHost);
            basicHttpContext.setAttribute(ExecutionContext.HTTP_CONNECTION, proxyConnection);
            basicHttpContext.setAttribute(ExecutionContext.HTTP_REQUEST, basicHttpRequest);
            basicHttpContext.setAttribute(ClientContext.PROXY_AUTH_STATE, this.proxyAuthState);
            basicHttpContext.setAttribute(ClientContext.CREDS_PROVIDER, basicCredentialsProvider);
            basicHttpContext.setAttribute(ClientContext.AUTHSCHEME_REGISTRY, this.authSchemeRegistry);
            this.requestExec.preProcess(basicHttpRequest, this.httpProcessor, basicHttpContext);
            HttpResponse execute = this.requestExec.execute(basicHttpRequest, proxyConnection, basicHttpContext);
            execute.setParams(this.params);
            this.requestExec.postProcess(execute, this.httpProcessor, basicHttpContext);
            if (execute.getStatusLine().getStatusCode() < 200) {
                throw new HttpException("Unexpected response to CONNECT request: " + execute.getStatusLine());
            } else if (HttpClientParams.isAuthenticating(this.params)) {
                if (!this.authenticator.isAuthenticationRequested(httpHost, execute, this.proxyAuthStrategy, this.proxyAuthState, basicHttpContext)) {
                    break;
                }
                if (!this.authenticator.authenticate(httpHost, execute, this.proxyAuthStrategy, this.proxyAuthState, basicHttpContext)) {
                    break;
                } else if (this.reuseStrategy.keepAlive(execute, basicHttpContext)) {
                    EntityUtils.consume(execute.getEntity());
                } else {
                    proxyConnection.close();
                }
            }
        }
        if (execute.getStatusLine().getStatusCode() <= 299) {
            return proxyConnection.getSocket();
        }
        HttpEntity entity = execute.getEntity();
        if (entity != null) {
            execute.setEntity(new BufferedHttpEntity(entity));
        }
        proxyConnection.close();
        throw new TunnelRefusedException("CONNECT refused by proxy: " + execute.getStatusLine(), execute);
    }
}
