package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;

abstract class RequestAuthenticationBase implements HttpRequestInterceptor {
    final Log log = LogFactory.getLog(getClass());

    private Header authenticate(AuthScheme authScheme, Credentials credentials, HttpRequest httpRequest, HttpContext httpContext) throws AuthenticationException {
        if (authScheme != null) {
            return authScheme instanceof ContextAwareAuthScheme ? ((ContextAwareAuthScheme) authScheme).authenticate(credentials, httpRequest, httpContext) : authScheme.authenticate(credentials, httpRequest);
        } else {
            throw new IllegalStateException("Auth state object is null");
        }
    }

    private void ensureAuthScheme(AuthScheme authScheme) {
        if (authScheme == null) {
            throw new IllegalStateException("Auth scheme is not set");
        }
    }

    void process(AuthState authState, HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        AuthScheme authScheme = authState.getAuthScheme();
        Credentials credentials = authState.getCredentials();
        switch (authState.getState()) {
            case FAILURE:
                return;
            case SUCCESS:
                ensureAuthScheme(authScheme);
                if (authScheme.isConnectionBased()) {
                    return;
                }
                break;
            case CHALLENGED:
                Queue authOptions = authState.getAuthOptions();
                if (authOptions == null) {
                    ensureAuthScheme(authScheme);
                    break;
                }
                while (!authOptions.isEmpty()) {
                    AuthOption authOption = (AuthOption) authOptions.remove();
                    authScheme = authOption.getAuthScheme();
                    credentials = authOption.getCredentials();
                    authState.update(authScheme, credentials);
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Generating response to an authentication challenge using " + authScheme.getSchemeName() + " scheme");
                    }
                    try {
                        httpRequest.addHeader(authenticate(authScheme, credentials, httpRequest, httpContext));
                        return;
                    } catch (AuthenticationException e) {
                        if (this.log.isWarnEnabled()) {
                            this.log.warn(authScheme + " authentication error: " + e.getMessage());
                        }
                    }
                }
                return;
        }
        if (authScheme != null) {
            try {
                httpRequest.addHeader(authenticate(authScheme, credentials, httpRequest, httpContext));
            } catch (AuthenticationException e2) {
                if (this.log.isErrorEnabled()) {
                    this.log.error(authScheme + " authentication error: " + e2.getMessage());
                }
            }
        }
    }
}
