package com.google.api.client.auth.jsontoken;

import com.google.api.client.auth.jsontoken.JsonWebSignature.Header;
import com.google.api.client.auth.jsontoken.JsonWebToken.Payload;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.SecurityUtils;
import com.google.api.client.util.StringUtils;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

@Deprecated
public class RsaSHA256Signer {
    private RsaSHA256Signer() {
    }

    @Deprecated
    public static String sign(PrivateKey privateKey, JsonFactory jsonFactory, Header header, Payload payload) throws GeneralSecurityException, IOException {
        String str = Base64.encodeBase64URLSafeString(jsonFactory.toByteArray(header)) + "." + Base64.encodeBase64URLSafeString(jsonFactory.toByteArray(payload));
        return str + "." + Base64.encodeBase64URLSafeString(SecurityUtils.sign(SecurityUtils.getSha256WithRsaSignatureAlgorithm(), privateKey, StringUtils.getBytesUtf8(str)));
    }
}
