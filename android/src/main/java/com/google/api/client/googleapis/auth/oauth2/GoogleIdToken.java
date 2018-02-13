package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebSignature.Header;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleIdToken extends IdToken {

    public static class Payload extends com.google.api.client.auth.openidconnect.IdToken.Payload {
        @Key("token_hash")
        private String accessTokenHash;
        @Key("email")
        private String email;
        @Key("verified_email")
        private boolean emailVerified;
        @Key("hd")
        private String hostedDomain;
        @Key("cid")
        private String issuee;
        @Key("id")
        private String userId;

        @Deprecated
        public Payload(Clock clock) {
        }

        public Payload clone() {
            return (Payload) super.clone();
        }

        public String getAccessTokenHash() {
            return this.accessTokenHash;
        }

        public String getEmail() {
            return this.email;
        }

        public boolean getEmailVerified() {
            return this.emailVerified;
        }

        public String getHostedDomain() {
            return this.hostedDomain;
        }

        public String getIssuee() {
            return this.issuee;
        }

        public String getUserId() {
            return this.userId;
        }

        public Payload set(String str, Object obj) {
            return (Payload) super.set(str, obj);
        }

        public Payload setAccessTokenHash(String str) {
            this.accessTokenHash = str;
            return this;
        }

        public Payload setEmail(String str) {
            this.email = str;
            return this;
        }

        public Payload setEmailVerified(boolean z) {
            this.emailVerified = z;
            return this;
        }

        public Payload setHostedDomain(String str) {
            this.hostedDomain = str;
            return this;
        }

        public Payload setIssuee(String str) {
            this.issuee = str;
            return this;
        }

        public Payload setUserId(String str) {
            this.userId = str;
            return this;
        }
    }

    public GoogleIdToken(Header header, Payload payload, byte[] bArr, byte[] bArr2) {
        super(header, payload, bArr, bArr2);
    }

    public static GoogleIdToken parse(JsonFactory jsonFactory, String str) throws IOException {
        JsonWebSignature parse = JsonWebSignature.parser(jsonFactory).setPayloadClass(Payload.class).parse(str);
        return new GoogleIdToken(parse.getHeader(), (Payload) parse.getPayload(), parse.getSignatureBytes(), parse.getSignedContentBytes());
    }

    public Payload getPayload() {
        return (Payload) super.getPayload();
    }

    public boolean verify(GoogleIdTokenVerifier googleIdTokenVerifier) throws GeneralSecurityException, IOException {
        return googleIdTokenVerifier.verify(this);
    }
}
