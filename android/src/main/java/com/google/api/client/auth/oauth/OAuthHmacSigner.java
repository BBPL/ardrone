package com.google.api.client.auth.oauth;

import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import java.security.GeneralSecurityException;
import java.security.Key;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class OAuthHmacSigner implements OAuthSigner {
    public String clientSharedSecret;
    public String tokenSharedSecret;

    public String computeSignature(String str) throws GeneralSecurityException {
        StringBuilder stringBuilder = new StringBuilder();
        String str2 = this.clientSharedSecret;
        if (str2 != null) {
            stringBuilder.append(OAuthParameters.escape(str2));
        }
        stringBuilder.append('&');
        str2 = this.tokenSharedSecret;
        if (str2 != null) {
            stringBuilder.append(OAuthParameters.escape(str2));
        }
        Key secretKeySpec = new SecretKeySpec(StringUtils.getBytesUtf8(stringBuilder.toString()), "HmacSHA1");
        Mac instance = Mac.getInstance("HmacSHA1");
        instance.init(secretKeySpec);
        return Base64.encodeBase64String(instance.doFinal(StringUtils.getBytesUtf8(str)));
    }

    public String getSignatureMethod() {
        return "HMAC-SHA1";
    }
}
