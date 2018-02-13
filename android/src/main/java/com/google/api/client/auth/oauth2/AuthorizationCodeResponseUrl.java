package com.google.api.client.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

public class AuthorizationCodeResponseUrl extends GenericUrl {
    @Key
    private String code;
    @Key
    private String error;
    @Key("error_description")
    private String errorDescription;
    @Key("error_uri")
    private String errorUri;
    @Key
    private String state;

    public AuthorizationCodeResponseUrl(String str) {
        boolean z = false;
        super(str);
        if ((this.code == null) != (this.error == null)) {
            z = true;
        }
        Preconditions.checkArgument(z);
    }

    public AuthorizationCodeRequestUrl clone() {
        return (AuthorizationCodeRequestUrl) super.clone();
    }

    public final String getCode() {
        return this.code;
    }

    public final String getError() {
        return this.error;
    }

    public final String getErrorDescription() {
        return this.errorDescription;
    }

    public final String getErrorUri() {
        return this.errorUri;
    }

    public final String getState() {
        return this.state;
    }

    public AuthorizationCodeRequestUrl set(String str, Object obj) {
        return (AuthorizationCodeRequestUrl) super.set(str, obj);
    }

    public AuthorizationCodeResponseUrl setCode(String str) {
        this.code = str;
        return this;
    }

    public AuthorizationCodeResponseUrl setError(String str) {
        this.error = str;
        return this;
    }

    public AuthorizationCodeResponseUrl setErrorDescription(String str) {
        this.errorDescription = str;
        return this;
    }

    public AuthorizationCodeResponseUrl setErrorUri(String str) {
        this.errorUri = str;
        return this;
    }

    public AuthorizationCodeResponseUrl setState(String str) {
        this.state = str;
        return this;
    }
}
