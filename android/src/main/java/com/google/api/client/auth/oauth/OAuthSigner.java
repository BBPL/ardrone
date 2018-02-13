package com.google.api.client.auth.oauth;

import java.security.GeneralSecurityException;

public interface OAuthSigner {
    String computeSignature(String str) throws GeneralSecurityException;

    String getSignatureMethod();
}
