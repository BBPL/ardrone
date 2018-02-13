package com.google.api.client.auth.oauth;

import com.google.api.client.util.Base64;
import com.google.api.client.util.SecurityUtils;
import com.google.api.client.util.StringUtils;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

public final class OAuthRsaSigner implements OAuthSigner {
    public PrivateKey privateKey;

    public String computeSignature(String str) throws GeneralSecurityException {
        return Base64.encodeBase64String(SecurityUtils.sign(SecurityUtils.getSha1WithRsaSignatureAlgorithm(), this.privateKey, StringUtils.getBytesUtf8(str)));
    }

    public String getSignatureMethod() {
        return "RSA-SHA1";
    }
}
