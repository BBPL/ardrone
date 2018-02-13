package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.BrowserClientRequestUrl;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

public class GoogleBrowserClientRequestUrl extends BrowserClientRequestUrl {
    @Key("approval_prompt")
    private String approvalPrompt;

    public GoogleBrowserClientRequestUrl(GoogleClientSecrets googleClientSecrets, String str, Iterable<String> iterable) {
        this(googleClientSecrets.getDetails().getClientId(), str, (Iterable) iterable);
    }

    public GoogleBrowserClientRequestUrl(String str, String str2, Iterable<String> iterable) {
        super(GoogleOAuthConstants.AUTHORIZATION_SERVER_URL, str);
        setRedirectUri(str2);
        setScopes((Iterable) iterable);
    }

    public GoogleBrowserClientRequestUrl clone() {
        return (GoogleBrowserClientRequestUrl) super.clone();
    }

    public final String getApprovalPrompt() {
        return this.approvalPrompt;
    }

    public GoogleBrowserClientRequestUrl set(String str, Object obj) {
        return (GoogleBrowserClientRequestUrl) super.set(str, obj);
    }

    public GoogleBrowserClientRequestUrl setApprovalPrompt(String str) {
        this.approvalPrompt = str;
        return this;
    }

    public GoogleBrowserClientRequestUrl setClientId(String str) {
        return (GoogleBrowserClientRequestUrl) super.setClientId(str);
    }

    public GoogleBrowserClientRequestUrl setRedirectUri(String str) {
        return (GoogleBrowserClientRequestUrl) super.setRedirectUri(str);
    }

    public GoogleBrowserClientRequestUrl setResponseTypes(Iterable<String> iterable) {
        return (GoogleBrowserClientRequestUrl) super.setResponseTypes((Iterable) iterable);
    }

    public GoogleBrowserClientRequestUrl setResponseTypes(String... strArr) {
        return (GoogleBrowserClientRequestUrl) super.setResponseTypes(strArr);
    }

    public GoogleBrowserClientRequestUrl setScopes(Iterable<String> iterable) {
        Preconditions.checkArgument(iterable.iterator().hasNext());
        return (GoogleBrowserClientRequestUrl) super.setScopes((Iterable) iterable);
    }

    public GoogleBrowserClientRequestUrl setScopes(String... strArr) {
        Preconditions.checkArgument(strArr.length != 0);
        return (GoogleBrowserClientRequestUrl) super.setScopes(strArr);
    }

    public GoogleBrowserClientRequestUrl setState(String str) {
        return (GoogleBrowserClientRequestUrl) super.setState(str);
    }
}
