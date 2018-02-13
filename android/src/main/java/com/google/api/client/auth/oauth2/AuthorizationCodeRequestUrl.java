package com.google.api.client.auth.oauth2;

import java.util.Collections;

public class AuthorizationCodeRequestUrl extends AuthorizationRequestUrl {
    public AuthorizationCodeRequestUrl(String str, String str2) {
        super(str, str2, Collections.singleton("code"));
    }

    public AuthorizationCodeRequestUrl clone() {
        return (AuthorizationCodeRequestUrl) super.clone();
    }

    public AuthorizationCodeRequestUrl set(String str, Object obj) {
        return (AuthorizationCodeRequestUrl) super.set(str, obj);
    }

    public AuthorizationCodeRequestUrl setClientId(String str) {
        return (AuthorizationCodeRequestUrl) super.setClientId(str);
    }

    public AuthorizationCodeRequestUrl setRedirectUri(String str) {
        return (AuthorizationCodeRequestUrl) super.setRedirectUri(str);
    }

    public AuthorizationCodeRequestUrl setResponseTypes(Iterable<String> iterable) {
        return (AuthorizationCodeRequestUrl) super.setResponseTypes((Iterable) iterable);
    }

    public AuthorizationCodeRequestUrl setResponseTypes(String... strArr) {
        return (AuthorizationCodeRequestUrl) super.setResponseTypes(strArr);
    }

    public AuthorizationCodeRequestUrl setScopes(Iterable<String> iterable) {
        return (AuthorizationCodeRequestUrl) super.setScopes((Iterable) iterable);
    }

    public AuthorizationCodeRequestUrl setScopes(String... strArr) {
        return (AuthorizationCodeRequestUrl) super.setScopes(strArr);
    }

    public AuthorizationCodeRequestUrl setState(String str) {
        return (AuthorizationCodeRequestUrl) super.setState(str);
    }
}
