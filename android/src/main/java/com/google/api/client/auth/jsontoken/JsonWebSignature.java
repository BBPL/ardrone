package com.google.api.client.auth.jsontoken;

import com.google.api.client.auth.jsontoken.JsonWebToken.Payload;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Deprecated
public class JsonWebSignature extends JsonWebToken {
    private final byte[] signatureBytes;
    private final byte[] signedContentBytes;

    @Deprecated
    public static class Header extends com.google.api.client.auth.jsontoken.JsonWebToken.Header {
        @Key("alg")
        private String algorithm;
        @Key("jku")
        private String jwkUrl;
        @Key("kid")
        private String keyId;
        @Key("x5t")
        private String x509Thumbprint;
        @Key("x5u")
        private String x509Url;

        public Header clone() {
            return (Header) super.clone();
        }

        public final String getAlgorithm() {
            return this.algorithm;
        }

        public String getJwkUrl() {
            return this.jwkUrl;
        }

        public String getKeyId() {
            return this.keyId;
        }

        public String getX509Thumbprint() {
            return this.x509Thumbprint;
        }

        public String getX509Url() {
            return this.x509Url;
        }

        public Header set(String str, Object obj) {
            return (Header) super.set(str, obj);
        }

        public Header setAlgorithm(String str) {
            this.algorithm = str;
            return this;
        }

        public Header setJwkUrl(String str) {
            this.jwkUrl = str;
            return this;
        }

        public Header setKeyId(String str) {
            this.keyId = str;
            return this;
        }

        public Header setType(String str) {
            super.setType(str);
            return this;
        }

        public Header setX509Thumbprint(String str) {
            this.x509Thumbprint = str;
            return this;
        }

        public Header setX509Url(String str) {
            this.x509Url = str;
            return this;
        }
    }

    @Deprecated
    public static final class Parser {
        private Class<? extends Header> headerClass = Header.class;
        private final JsonFactory jsonFactory;
        private Class<? extends Payload> payloadClass = Payload.class;

        public Parser(JsonFactory jsonFactory) {
            this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        }

        public Class<? extends Header> getHeaderClass() {
            return this.headerClass;
        }

        public JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public Class<? extends Payload> getPayloadClass() {
            return this.payloadClass;
        }

        public JsonWebSignature parse(String str) throws IOException {
            boolean z = true;
            int indexOf = str.indexOf(46);
            Preconditions.checkArgument(indexOf != -1);
            byte[] decodeBase64 = Base64.decodeBase64(str.substring(0, indexOf));
            int indexOf2 = str.indexOf(46, indexOf + 1);
            Preconditions.checkArgument(indexOf2 != -1);
            Preconditions.checkArgument(str.indexOf(46, indexOf2 + 1) == -1);
            byte[] decodeBase642 = Base64.decodeBase64(str.substring(indexOf + 1, indexOf2));
            byte[] decodeBase643 = Base64.decodeBase64(str.substring(indexOf2 + 1));
            byte[] bytesUtf8 = StringUtils.getBytesUtf8(str.substring(0, indexOf2));
            Header header = (Header) this.jsonFactory.fromInputStream(new ByteArrayInputStream(decodeBase64), this.headerClass);
            if (header.getAlgorithm() == null) {
                z = false;
            }
            Preconditions.checkArgument(z);
            return new JsonWebSignature(header, (Payload) this.jsonFactory.fromInputStream(new ByteArrayInputStream(decodeBase642), this.payloadClass), decodeBase643, bytesUtf8);
        }

        public Parser setHeaderClass(Class<? extends Header> cls) {
            this.headerClass = cls;
            return this;
        }

        public Parser setPayloadClass(Class<? extends Payload> cls) {
            this.payloadClass = cls;
            return this;
        }
    }

    public JsonWebSignature(Header header, Payload payload, byte[] bArr, byte[] bArr2) {
        super(header, payload);
        this.signatureBytes = bArr;
        this.signedContentBytes = bArr2;
    }

    public static JsonWebSignature parse(JsonFactory jsonFactory, String str) throws IOException {
        return parser(jsonFactory).parse(str);
    }

    public static Parser parser(JsonFactory jsonFactory) {
        return new Parser(jsonFactory);
    }

    public Header getHeader() {
        return (Header) super.getHeader();
    }

    public final byte[] getSignatureBytes() {
        return this.signatureBytes;
    }

    public final byte[] getSignedContentBytes() {
        return this.signedContentBytes;
    }
}
