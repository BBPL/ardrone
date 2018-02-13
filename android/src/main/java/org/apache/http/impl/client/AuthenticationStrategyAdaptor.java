package org.apache.http.impl.client;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthCache;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.protocol.HttpContext;

@Immutable
@Deprecated
class AuthenticationStrategyAdaptor implements AuthenticationStrategy {
    private final AuthenticationHandler handler;
    private final Log log = LogFactory.getLog(getClass());

    public AuthenticationStrategyAdaptor(AuthenticationHandler authenticationHandler) {
        this.handler = authenticationHandler;
    }

    private boolean isCachable(AuthScheme authScheme) {
        if (authScheme != null && authScheme.isComplete()) {
            String schemeName = authScheme.getSchemeName();
            if (schemeName.equalsIgnoreCase(AuthPolicy.BASIC) || schemeName.equalsIgnoreCase(AuthPolicy.DIGEST)) {
                return true;
            }
        }
        return false;
    }

    public void authFailed(HttpHost httpHost, AuthScheme authScheme, HttpContext httpContext) {
        AuthCache authCache = (AuthCache) httpContext.getAttribute(ClientContext.AUTH_CACHE);
        if (authCache != null) {
            if (this.log.isDebugEnabled()) {
                this.log.debug("Removing from cache '" + authScheme.getSchemeName() + "' auth scheme for " + httpHost);
            }
            authCache.remove(httpHost);
        }
    }

    public void authSucceeded(HttpHost httpHost, AuthScheme authScheme, HttpContext httpContext) {
        AuthCache authCache = (AuthCache) httpContext.getAttribute(ClientContext.AUTH_CACHE);
        if (isCachable(authScheme)) {
            if (authCache == null) {
                authCache = new BasicAuthCache();
                httpContext.setAttribute(ClientContext.AUTH_CACHE, authCache);
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + httpHost);
            }
            authCache.put(httpHost, authScheme);
        }
    }

    public Map<String, Header> getChallenges(HttpHost httpHost, HttpResponse httpResponse, HttpContext httpContext) throws MalformedChallengeException {
        return this.handler.getChallenges(httpResponse, httpContext);
    }

    public AuthenticationHandler getHandler() {
        return this.handler;
    }

    public boolean isAuthenticationRequested(HttpHost httpHost, HttpResponse httpResponse, HttpContext httpContext) {
        return this.handler.isAuthenticationRequested(httpResponse, httpContext);
    }

    public Queue<AuthOption> select(Map<String, Header> map, HttpHost httpHost, HttpResponse httpResponse, HttpContext httpContext) throws MalformedChallengeException {
        if (map == null) {
            throw new IllegalArgumentException("Map of auth challenges may not be null");
        } else if (httpHost == null) {
            throw new IllegalArgumentException("Host may not be null");
        } else if (httpResponse == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            Queue<AuthOption> linkedList = new LinkedList();
            CredentialsProvider credentialsProvider = (CredentialsProvider) httpContext.getAttribute(ClientContext.CREDS_PROVIDER);
            if (credentialsProvider == null) {
                this.log.debug("Credentials provider not set in the context");
            } else {
                try {
                    AuthScheme selectScheme = this.handler.selectScheme(map, httpResponse, httpContext);
                    selectScheme.processChallenge((Header) map.get(selectScheme.getSchemeName().toLowerCase(Locale.US)));
                    Credentials credentials = credentialsProvider.getCredentials(new AuthScope(httpHost.getHostName(), httpHost.getPort(), selectScheme.getRealm(), selectScheme.getSchemeName()));
                    if (credentials != null) {
                        linkedList.add(new AuthOption(selectScheme, credentials));
                        return linkedList;
                    }
                } catch (Throwable e) {
                    if (this.log.isWarnEnabled()) {
                        this.log.warn(e.getMessage(), e);
                        return linkedList;
                    }
                }
            }
            return linkedList;
        }
    }
}
