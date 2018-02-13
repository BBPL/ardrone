package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.mortbay.jetty.security.Constraint;

@Immutable
public class RequestAuthCache implements HttpRequestInterceptor {
    private final Log log = LogFactory.getLog(getClass());

    private void doPreemptiveAuth(HttpHost httpHost, AuthScheme authScheme, AuthState authState, CredentialsProvider credentialsProvider) {
        String schemeName = authScheme.getSchemeName();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Re-using cached '" + schemeName + "' auth scheme for " + httpHost);
        }
        Credentials credentials = credentialsProvider.getCredentials(new AuthScope(httpHost, AuthScope.ANY_REALM, schemeName));
        if (credentials != null) {
            if (Constraint.__BASIC_AUTH.equalsIgnoreCase(authScheme.getSchemeName())) {
                authState.setState(AuthProtocolState.CHALLENGED);
            } else {
                authState.setState(AuthProtocolState.SUCCESS);
            }
            authState.update(authScheme, credentials);
            return;
        }
        this.log.debug("No credentials for preemptive authentication");
    }

    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        if (httpRequest == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            AuthCache authCache = (AuthCache) httpContext.getAttribute(ClientContext.AUTH_CACHE);
            if (authCache == null) {
                this.log.debug("Auth cache not set in the context");
                return;
            }
            CredentialsProvider credentialsProvider = (CredentialsProvider) httpContext.getAttribute(ClientContext.CREDS_PROVIDER);
            if (credentialsProvider == null) {
                this.log.debug("Credentials provider not set in the context");
                return;
            }
            HttpHost httpHost;
            HttpHost httpHost2 = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            if (httpHost2.getPort() < 0) {
                httpHost = new HttpHost(httpHost2.getHostName(), ((SchemeRegistry) httpContext.getAttribute(ClientContext.SCHEME_REGISTRY)).getScheme(httpHost2).resolvePort(httpHost2.getPort()), httpHost2.getSchemeName());
            } else {
                httpHost = httpHost2;
            }
            AuthState authState = (AuthState) httpContext.getAttribute(ClientContext.TARGET_AUTH_STATE);
            if (!(httpHost == null || authState == null || authState.getState() != AuthProtocolState.UNCHALLENGED)) {
                AuthScheme authScheme = authCache.get(httpHost);
                if (authScheme != null) {
                    doPreemptiveAuth(httpHost, authScheme, authState, credentialsProvider);
                }
            }
            httpHost2 = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_PROXY_HOST);
            AuthState authState2 = (AuthState) httpContext.getAttribute(ClientContext.PROXY_AUTH_STATE);
            if (httpHost2 != null && authState2 != null && authState2.getState() == AuthProtocolState.UNCHALLENGED) {
                AuthScheme authScheme2 = authCache.get(httpHost2);
                if (authScheme2 != null) {
                    doPreemptiveAuth(httpHost2, authScheme2, authState2, credentialsProvider);
                }
            }
        }
    }
}
