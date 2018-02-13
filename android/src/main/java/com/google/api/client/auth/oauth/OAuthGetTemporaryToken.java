package com.google.api.client.auth.oauth;

public class OAuthGetTemporaryToken extends AbstractOAuthGetToken {
    public String callback;

    public OAuthGetTemporaryToken(String str) {
        super(str);
    }

    public OAuthParameters createParameters() {
        OAuthParameters createParameters = super.createParameters();
        createParameters.callback = this.callback;
        return createParameters;
    }
}
