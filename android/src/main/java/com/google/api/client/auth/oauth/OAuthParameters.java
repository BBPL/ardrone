package com.google.api.client.auth.oauth;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.escape.PercentEscaper;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.TreeMap;

public final class OAuthParameters implements HttpExecuteInterceptor, HttpRequestInitializer {
    private static final PercentEscaper ESCAPER = new PercentEscaper("-_.~", false);
    private static final SecureRandom RANDOM = new SecureRandom();
    public String callback;
    public String consumerKey;
    public String nonce;
    public String realm;
    public String signature;
    public String signatureMethod;
    public OAuthSigner signer;
    public String timestamp;
    public String token;
    public String verifier;
    public String version;

    private void appendParameter(StringBuilder stringBuilder, String str, String str2) {
        if (str2 != null) {
            stringBuilder.append(' ').append(escape(str)).append("=\"").append(escape(str2)).append("\",");
        }
    }

    public static String escape(String str) {
        return ESCAPER.escape(str);
    }

    private void putParameter(TreeMap<String, String> treeMap, String str, Object obj) {
        treeMap.put(escape(str), obj == null ? null : escape(obj.toString()));
    }

    private void putParameterIfValueNotNull(TreeMap<String, String> treeMap, String str, String str2) {
        if (str2 != null) {
            putParameter(treeMap, str, str2);
        }
    }

    public void computeNonce() {
        this.nonce = Long.toHexString(Math.abs(RANDOM.nextLong()));
    }

    public void computeSignature(String str, GenericUrl genericUrl) throws GeneralSecurityException {
        Object value;
        OAuthSigner oAuthSigner = this.signer;
        String signatureMethod = oAuthSigner.getSignatureMethod();
        this.signatureMethod = signatureMethod;
        TreeMap treeMap = new TreeMap();
        putParameterIfValueNotNull(treeMap, "oauth_callback", this.callback);
        putParameterIfValueNotNull(treeMap, "oauth_consumer_key", this.consumerKey);
        putParameterIfValueNotNull(treeMap, "oauth_nonce", this.nonce);
        putParameterIfValueNotNull(treeMap, "oauth_signature_method", signatureMethod);
        putParameterIfValueNotNull(treeMap, "oauth_timestamp", this.timestamp);
        putParameterIfValueNotNull(treeMap, "oauth_token", this.token);
        putParameterIfValueNotNull(treeMap, "oauth_verifier", this.verifier);
        putParameterIfValueNotNull(treeMap, "oauth_version", this.version);
        for (Entry entry : genericUrl.entrySet()) {
            value = entry.getValue();
            if (value != null) {
                signatureMethod = (String) entry.getKey();
                if (value instanceof Collection) {
                    for (Object putParameter : (Collection) value) {
                        putParameter(treeMap, signatureMethod, putParameter);
                    }
                } else {
                    putParameter(treeMap, signatureMethod, value);
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        value = 1;
        for (Entry entry2 : treeMap.entrySet()) {
            Object obj;
            if (value != null) {
                obj = null;
            } else {
                stringBuilder.append('&');
                obj = value;
            }
            stringBuilder.append((String) entry2.getKey());
            signatureMethod = (String) entry2.getValue();
            if (signatureMethod != null) {
                stringBuilder.append('=').append(signatureMethod);
                value = obj;
            } else {
                value = obj;
            }
        }
        String stringBuilder2 = stringBuilder.toString();
        GenericUrl genericUrl2 = new GenericUrl();
        String scheme = genericUrl.getScheme();
        genericUrl2.setScheme(scheme);
        genericUrl2.setHost(genericUrl.getHost());
        genericUrl2.setPathParts(genericUrl.getPathParts());
        int port = genericUrl.getPort();
        if (("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443)) {
            port = -1;
        }
        genericUrl2.setPort(port);
        signatureMethod = genericUrl2.build();
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(escape(str)).append('&');
        stringBuilder3.append(escape(signatureMethod)).append('&');
        stringBuilder3.append(escape(stringBuilder2));
        this.signature = oAuthSigner.computeSignature(stringBuilder3.toString());
    }

    public void computeTimestamp() {
        this.timestamp = Long.toString(System.currentTimeMillis() / 1000);
    }

    public String getAuthorizationHeader() {
        StringBuilder stringBuilder = new StringBuilder("OAuth");
        appendParameter(stringBuilder, "realm", this.realm);
        appendParameter(stringBuilder, "oauth_callback", this.callback);
        appendParameter(stringBuilder, "oauth_consumer_key", this.consumerKey);
        appendParameter(stringBuilder, "oauth_nonce", this.nonce);
        appendParameter(stringBuilder, "oauth_signature", this.signature);
        appendParameter(stringBuilder, "oauth_signature_method", this.signatureMethod);
        appendParameter(stringBuilder, "oauth_timestamp", this.timestamp);
        appendParameter(stringBuilder, "oauth_token", this.token);
        appendParameter(stringBuilder, "oauth_verifier", this.verifier);
        appendParameter(stringBuilder, "oauth_version", this.version);
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public void initialize(HttpRequest httpRequest) throws IOException {
        httpRequest.setInterceptor(this);
    }

    public void intercept(HttpRequest httpRequest) throws IOException {
        computeNonce();
        computeTimestamp();
        try {
            computeSignature(httpRequest.getRequestMethod(), httpRequest.getUrl());
            httpRequest.getHeaders().setAuthorization(getAuthorizationHeader());
        } catch (Throwable e) {
            IOException iOException = new IOException();
            iOException.initCause(e);
            throw iOException;
        }
    }
}
