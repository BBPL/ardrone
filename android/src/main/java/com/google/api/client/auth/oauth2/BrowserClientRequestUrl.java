package com.google.api.client.auth.oauth2;

import java.util.Collections;

public class BrowserClientRequestUrl extends AuthorizationRequestUrl {
    public BrowserClientRequestUrl(String str, String str2) {
        super(str, str2, Collections.singleton("token"));
    }

    public BrowserClientRequestUrl clone() {
        return (BrowserClientRequestUrl) super.clone();
    }

    public BrowserClientRequestUrl set(String str, Object obj) {
        return (BrowserClientRequestUrl) super.set(str, obj);
    }

    public BrowserClientRequestUrl setClientId(String str) {
        return (BrowserClientRequestUrl) super.setClientId(str);
    }

    public BrowserClientRequestUrl setRedirectUri(String str) {
        return (BrowserClientRequestUrl) super.setRedirectUri(str);
    }

    public BrowserClientRequestUrl setResponseTypes(Iterable<String> iterable) {
        return (BrowserClientRequestUrl) super.setResponseTypes((Iterable) iterable);
    }

    public BrowserClientRequestUrl setResponseTypes(String... strArr) {
        return (BrowserClientRequestUrl) super.setResponseTypes(strArr);
    }

    public BrowserClientRequestUrl setScopes(Iterable<String> iterable) {
        return (BrowserClientRequestUrl) super.setScopes((Iterable) iterable);
    }

    public BrowserClientRequestUrl setScopes(String... strArr) {
        return (BrowserClientRequestUrl) super.setScopes(strArr);
    }

    public BrowserClientRequestUrl setState(String str) {
        return (BrowserClientRequestUrl) super.setState(str);
    }
}
