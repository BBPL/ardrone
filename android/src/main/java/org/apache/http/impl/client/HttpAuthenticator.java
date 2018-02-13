package org.apache.http.impl.client;

import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.protocol.HttpContext;

public class HttpAuthenticator {
    private final Log log;

    public HttpAuthenticator() {
        this(null);
    }

    public HttpAuthenticator(Log log) {
        if (log == null) {
            log = LogFactory.getLog(getClass());
        }
        this.log = log;
    }

    public boolean authenticate(HttpHost httpHost, HttpResponse httpResponse, AuthenticationStrategy authenticationStrategy, AuthState authState, HttpContext httpContext) {
        try {
            if (this.log.isDebugEnabled()) {
                this.log.debug(httpHost.toHostString() + " requested authentication");
            }
            Map challenges = authenticationStrategy.getChallenges(httpHost, httpResponse, httpContext);
            if (challenges.isEmpty()) {
                this.log.debug("Response contains no authentication challenges");
                return false;
            }
            AuthScheme authScheme = authState.getAuthScheme();
            switch (authState.getState()) {
                case CHALLENGED:
                case HANDSHAKE:
                    if (authScheme == null) {
                        this.log.debug("Auth scheme is null");
                        authenticationStrategy.authFailed(httpHost, null, httpContext);
                        authState.reset();
                        authState.setState(AuthProtocolState.FAILURE);
                        return false;
                    }
                    break;
                case SUCCESS:
                    authState.reset();
                    break;
                case FAILURE:
                    return false;
                case UNCHALLENGED:
                    break;
            }
            if (authScheme != null) {
                Header header = (Header) challenges.get(authScheme.getSchemeName().toLowerCase(Locale.US));
                if (header != null) {
                    this.log.debug("Authorization challenge processed");
                    authScheme.processChallenge(header);
                    if (authScheme.isComplete()) {
                        this.log.debug("Authentication failed");
                        authenticationStrategy.authFailed(httpHost, authState.getAuthScheme(), httpContext);
                        authState.reset();
                        authState.setState(AuthProtocolState.FAILURE);
                        return false;
                    }
                    authState.setState(AuthProtocolState.HANDSHAKE);
                    return true;
                }
                authState.reset();
            }
            Queue select = authenticationStrategy.select(challenges, httpHost, httpResponse, httpContext);
            if (select == null || select.isEmpty()) {
                return false;
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Selected authentication options: " + select);
            }
            authState.setState(AuthProtocolState.CHALLENGED);
            authState.update(select);
            return true;
        } catch (MalformedChallengeException e) {
            if (this.log.isWarnEnabled()) {
                this.log.warn("Malformed challenge: " + e.getMessage());
            }
            authState.reset();
            return false;
        }
    }

    public boolean isAuthenticationRequested(HttpHost httpHost, HttpResponse httpResponse, AuthenticationStrategy authenticationStrategy, AuthState authState, HttpContext httpContext) {
        if (authenticationStrategy.isAuthenticationRequested(httpHost, httpResponse, httpContext)) {
            this.log.debug("Authentication required");
            return true;
        }
        switch (authState.getState()) {
            case CHALLENGED:
            case HANDSHAKE:
                this.log.debug("Authentication succeeded");
                authState.setState(AuthProtocolState.SUCCESS);
                authenticationStrategy.authSucceeded(httpHost, authState.getAuthScheme(), httpContext);
                break;
            case SUCCESS:
                break;
            default:
                authState.setState(AuthProtocolState.UNCHALLENGED);
                break;
        }
        return false;
    }
}
