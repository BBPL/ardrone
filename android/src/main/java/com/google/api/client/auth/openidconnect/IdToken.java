package com.google.api.client.auth.openidconnect;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebSignature.Header;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.util.Collection;

public class IdToken extends JsonWebSignature {

    public static class Payload extends com.google.api.client.json.webtoken.JsonWebToken.Payload {
        @Key("auth_time")
        private Long authorizationTimeSeconds;
        @Key("azp")
        private Object authorizedParty;

        public final Long getAuthorizationTimeSeconds() {
            return this.authorizationTimeSeconds;
        }

        public final Object getAuthorizedParty() {
            return this.authorizedParty;
        }

        public Payload setAuthorizationTimeSeconds(Long l) {
            this.authorizationTimeSeconds = l;
            return this;
        }

        public Payload setAuthorizedParty(Object obj) {
            this.authorizedParty = obj;
            return this;
        }
    }

    public IdToken(Header header, Payload payload, byte[] bArr, byte[] bArr2) {
        super(header, payload, bArr, bArr2);
    }

    public static IdToken parse(JsonFactory jsonFactory, String str) throws IOException {
        JsonWebSignature parse = JsonWebSignature.parser(jsonFactory).setPayloadClass(Payload.class).parse(str);
        return new IdToken(parse.getHeader(), (Payload) parse.getPayload(), parse.getSignatureBytes(), parse.getSignedContentBytes());
    }

    public Payload getPayload() {
        return (Payload) super.getPayload();
    }

    public final boolean verifyAudience(Collection<String> collection) {
        return collection.containsAll(getPayload().getAudienceAsList());
    }

    public final boolean verifyExpirationTime(long j, long j2) {
        return j <= (getPayload().getExpirationTimeSeconds().longValue() + j2) * 1000;
    }

    public final boolean verifyIssuedAtTime(long j, long j2) {
        return j >= (getPayload().getIssuedAtTimeSeconds().longValue() - j2) * 1000;
    }

    public final boolean verifyIssuer(String str) {
        return str.equals(getPayload().getIssuer());
    }

    public final boolean verifyTime(long j, long j2) {
        return verifyExpirationTime(j, j2) && verifyIssuedAtTime(j, j2);
    }
}
