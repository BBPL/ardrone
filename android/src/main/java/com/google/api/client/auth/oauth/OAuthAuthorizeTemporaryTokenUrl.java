package com.google.api.client.auth.oauth;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

public class OAuthAuthorizeTemporaryTokenUrl extends GenericUrl {
    @Key("oauth_token")
    public String temporaryToken;

    public OAuthAuthorizeTemporaryTokenUrl(String str) {
        super(str);
    }
}
