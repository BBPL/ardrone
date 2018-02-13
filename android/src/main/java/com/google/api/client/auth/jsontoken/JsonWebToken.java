package com.google.api.client.auth.jsontoken;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

@Deprecated
public class JsonWebToken {
    private final Header header;
    private final Payload payload;

    @Deprecated
    public static class Header extends GenericJson {
        @Key("typ")
        private String type;

        public Header clone() {
            return (Header) super.clone();
        }

        public final String getType() {
            return this.type;
        }

        public Header set(String str, Object obj) {
            return (Header) super.set(str, obj);
        }

        public Header setType(String str) {
            this.type = str;
            return this;
        }
    }

    @Deprecated
    public static class Payload extends GenericJson {
        @Key("aud")
        private String audience;
        private final Clock clock;
        @Key("exp")
        private Long expirationTimeSeconds;
        @Key("iat")
        private Long issuedAtTimeSeconds;
        @Key("iss")
        private String issuer;
        @Key("jti")
        private String jwtId;
        @Key("nbf")
        private Long notBeforeTimeSeconds;
        @Key("prn")
        private String principal;
        @Key("typ")
        private String type;

        public Payload() {
            this(Clock.SYSTEM);
        }

        public Payload(Clock clock) {
            this.clock = (Clock) Preconditions.checkNotNull(clock);
        }

        public Payload clone() {
            return (Payload) super.clone();
        }

        public String getAudience() {
            return this.audience;
        }

        public Long getExpirationTimeSeconds() {
            return this.expirationTimeSeconds;
        }

        public Long getIssuedAtTimeSeconds() {
            return this.issuedAtTimeSeconds;
        }

        public String getIssuer() {
            return this.issuer;
        }

        public String getJwtId() {
            return this.jwtId;
        }

        public Long getNotBeforeTimeSeconds() {
            return this.notBeforeTimeSeconds;
        }

        public String getPrincipal() {
            return this.principal;
        }

        public String getType() {
            return this.type;
        }

        public boolean isValidTime(long j) {
            long currentTimeMillis = this.clock.currentTimeMillis();
            return (this.expirationTimeSeconds == null || currentTimeMillis <= (this.expirationTimeSeconds.longValue() + j) * 1000) && (this.issuedAtTimeSeconds == null || currentTimeMillis >= (this.issuedAtTimeSeconds.longValue() - j) * 1000);
        }

        public Payload set(String str, Object obj) {
            return (Payload) super.set(str, obj);
        }

        public Payload setAudience(String str) {
            this.audience = str;
            return this;
        }

        public Payload setExpirationTimeSeconds(Long l) {
            this.expirationTimeSeconds = l;
            return this;
        }

        public Payload setIssuedAtTimeSeconds(Long l) {
            this.issuedAtTimeSeconds = l;
            return this;
        }

        public Payload setIssuer(String str) {
            this.issuer = str;
            return this;
        }

        public Payload setJwtId(String str) {
            this.jwtId = str;
            return this;
        }

        public Payload setNotBeforeTimeSeconds(Long l) {
            this.notBeforeTimeSeconds = l;
            return this;
        }

        public Payload setPrincipal(String str) {
            this.principal = str;
            return this;
        }

        public Payload setType(String str) {
            this.type = str;
            return this;
        }
    }

    public JsonWebToken(Header header, Payload payload) {
        this.header = (Header) Preconditions.checkNotNull(header);
        this.payload = (Payload) Preconditions.checkNotNull(payload);
    }

    public Header getHeader() {
        return this.header;
    }

    public Payload getPayload() {
        return this.payload;
    }
}
