package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

public class GoogleAuthorizationCodeRequestUrl extends AuthorizationCodeRequestUrl {
    @Key("access_type")
    private String accessType;
    @Key("approval_prompt")
    private String approvalPrompt;

    public GoogleAuthorizationCodeRequestUrl(GoogleClientSecrets googleClientSecrets, String str, Iterable<String> iterable) {
        this(googleClientSecrets.getDetails().getClientId(), str, (Iterable) iterable);
    }

    public GoogleAuthorizationCodeRequestUrl(String str, String str2, Iterable<String> iterable) {
        this(GoogleOAuthConstants.AUTHORIZATION_SERVER_URL, str, str2, iterable);
    }

    public GoogleAuthorizationCodeRequestUrl(String str, String str2, String str3, Iterable<String> iterable) {
        super(str, str2);
        setRedirectUri(str3);
        setScopes((Iterable) iterable);
    }

    public GoogleAuthorizationCodeRequestUrl clone() {
        return (GoogleAuthorizationCodeRequestUrl) super.clone();
    }

    public final String getAccessType() {
        return this.accessType;
    }

    public final String getApprovalPrompt() {
        return this.approvalPrompt;
    }

    public GoogleAuthorizationCodeRequestUrl set(String str, Object obj) {
        return (GoogleAuthorizationCodeRequestUrl) super.set(str, obj);
    }

    public GoogleAuthorizationCodeRequestUrl setAccessType(String str) {
        this.accessType = str;
        return this;
    }

    public GoogleAuthorizationCodeRequestUrl setApprovalPrompt(String str) {
        this.approvalPrompt = str;
        return this;
    }

    public GoogleAuthorizationCodeRequestUrl setClientId(String str) {
        return (GoogleAuthorizationCodeRequestUrl) super.setClientId(str);
    }

    public GoogleAuthorizationCodeRequestUrl setRedirectUri(String str) {
        Preconditions.checkNotNull(str);
        return (GoogleAuthorizationCodeRequestUrl) super.setRedirectUri(str);
    }

    public GoogleAuthorizationCodeRequestUrl setResponseTypes(Iterable<String> iterable) {
        return (GoogleAuthorizationCodeRequestUrl) super.setResponseTypes((Iterable) iterable);
    }

    public GoogleAuthorizationCodeRequestUrl setResponseTypes(String... strArr) {
        return (GoogleAuthorizationCodeRequestUrl) super.setResponseTypes(strArr);
    }

    public GoogleAuthorizationCodeRequestUrl setScopes(Iterable<String> iterable) {
        Preconditions.checkArgument(iterable.iterator().hasNext());
        return (GoogleAuthorizationCodeRequestUrl) super.setScopes((Iterable) iterable);
    }

    public GoogleAuthorizationCodeRequestUrl setScopes(String... strArr) {
        Preconditions.checkArgument(strArr.length != 0);
        return (GoogleAuthorizationCodeRequestUrl) super.setScopes(strArr);
    }

    public GoogleAuthorizationCodeRequestUrl setState(String str) {
        return (GoogleAuthorizationCodeRequestUrl) super.setState(str);
    }
}
