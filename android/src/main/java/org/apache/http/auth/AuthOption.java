package org.apache.http.auth;

import org.apache.http.annotation.Immutable;

@Immutable
public final class AuthOption {
    private final AuthScheme authScheme;
    private final Credentials creds;

    public AuthOption(AuthScheme authScheme, Credentials credentials) {
        if (authScheme == null) {
            throw new IllegalArgumentException("Auth scheme may not be null");
        } else if (credentials == null) {
            throw new IllegalArgumentException("User credentials may not be null");
        } else {
            this.authScheme = authScheme;
            this.creds = credentials;
        }
    }

    public AuthScheme getAuthScheme() {
        return this.authScheme;
    }

    public Credentials getCredentials() {
        return this.creds;
    }

    public String toString() {
        return this.authScheme.toString();
    }
}
