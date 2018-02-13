package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.SecurityUtils;
import com.google.api.client.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleIdTokenVerifier {
    private static final Pattern MAX_AGE_PATTERN = Pattern.compile("\\s*max-age\\s*=\\s*(\\d+)\\s*");
    private static final long TIME_SKEW_SECONDS = 300;
    @Deprecated
    private Set<String> clientIds;
    private final Clock clock;
    private long expirationTimeMilliseconds;
    private final JsonFactory jsonFactory;
    private final Lock lock;
    private List<PublicKey> publicKeys;
    private final HttpTransport transport;

    public static class Builder {
        @Deprecated
        Set<String> clientIds = new HashSet();
        Clock clock = Clock.SYSTEM;
        final JsonFactory jsonFactory;
        final HttpTransport transport;

        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory) {
            this.transport = httpTransport;
            this.jsonFactory = jsonFactory;
        }

        public GoogleIdTokenVerifier build() {
            return new GoogleIdTokenVerifier(this);
        }

        @Deprecated
        public final Set<String> getClientIds() {
            return this.clientIds;
        }

        public final Clock getClock() {
            return this.clock;
        }

        public final JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public final HttpTransport getTransport() {
            return this.transport;
        }

        @Deprecated
        public Builder setClientIds(Iterable<String> iterable) {
            this.clientIds.clear();
            for (String add : iterable) {
                this.clientIds.add(add);
            }
            return this;
        }

        @Deprecated
        public Builder setClientIds(String... strArr) {
            this.clientIds.clear();
            Collections.addAll(this.clientIds, strArr);
            return this;
        }

        public Builder setClock(Clock clock) {
            this.clock = (Clock) Preconditions.checkNotNull(clock);
            return this;
        }
    }

    protected GoogleIdTokenVerifier(Builder builder) {
        this.lock = new ReentrantLock();
        this.clientIds = builder.clientIds == null ? Collections.emptySet() : Collections.unmodifiableSet(builder.clientIds);
        this.transport = (HttpTransport) Preconditions.checkNotNull(builder.transport);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(builder.jsonFactory);
        this.clock = (Clock) Preconditions.checkNotNull(builder.clock);
    }

    public GoogleIdTokenVerifier(HttpTransport httpTransport, JsonFactory jsonFactory) {
        this(new Builder(httpTransport, jsonFactory));
    }

    @Deprecated
    protected GoogleIdTokenVerifier(Set<String> set, HttpTransport httpTransport, JsonFactory jsonFactory) {
        this(set, httpTransport, jsonFactory, Clock.SYSTEM);
    }

    @Deprecated
    protected GoogleIdTokenVerifier(Set<String> set, HttpTransport httpTransport, JsonFactory jsonFactory, Clock clock) {
        this.lock = new ReentrantLock();
        this.clientIds = set == null ? Collections.emptySet() : Collections.unmodifiableSet(set);
        this.transport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        this.clock = (Clock) Preconditions.checkNotNull(clock);
    }

    long getCacheTimeInSec(HttpHeaders httpHeaders) {
        long longValue;
        if (httpHeaders.getCacheControl() != null) {
            for (CharSequence matcher : httpHeaders.getCacheControl().split(",")) {
                Matcher matcher2 = MAX_AGE_PATTERN.matcher(matcher);
                if (matcher2.matches()) {
                    longValue = Long.valueOf(matcher2.group(1)).longValue();
                    break;
                }
            }
        }
        longValue = 0;
        if (httpHeaders.getAge() != null) {
            longValue -= httpHeaders.getAge().longValue();
        }
        return Math.max(0, longValue);
    }

    @Deprecated
    public final Set<String> getClientIds() {
        return this.clientIds;
    }

    public final long getExpirationTimeMilliseconds() {
        return this.expirationTimeMilliseconds;
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public final List<PublicKey> getPublicKeys() {
        return this.publicKeys;
    }

    public final HttpTransport getTransport() {
        return this.transport;
    }

    public GoogleIdTokenVerifier loadPublicCerts() throws GeneralSecurityException, IOException {
        JsonParser createJsonParser;
        this.lock.lock();
        try {
            this.publicKeys = new ArrayList();
            CertificateFactory x509CertificateFactory = SecurityUtils.getX509CertificateFactory();
            HttpResponse execute = this.transport.createRequestFactory().buildGetRequest(new GenericUrl("https://www.googleapis.com/oauth2/v1/certs")).execute();
            this.expirationTimeMilliseconds = this.clock.currentTimeMillis() + (getCacheTimeInSec(execute.getHeaders()) * 1000);
            createJsonParser = this.jsonFactory.createJsonParser(execute.getContent());
            JsonToken currentToken = createJsonParser.getCurrentToken();
            if (currentToken == null) {
                currentToken = createJsonParser.nextToken();
            }
            Preconditions.checkArgument(currentToken == JsonToken.START_OBJECT);
            while (createJsonParser.nextToken() != JsonToken.END_OBJECT) {
                createJsonParser.nextToken();
                this.publicKeys.add(((X509Certificate) x509CertificateFactory.generateCertificate(new ByteArrayInputStream(StringUtils.getBytesUtf8(createJsonParser.getText())))).getPublicKey());
            }
            this.publicKeys = Collections.unmodifiableList(this.publicKeys);
            createJsonParser.close();
            this.lock.unlock();
            return this;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public GoogleIdToken verify(String str) throws GeneralSecurityException, IOException {
        GoogleIdToken parse = GoogleIdToken.parse(this.jsonFactory, str);
        return verify(parse) ? parse : null;
    }

    public boolean verify(GoogleIdToken googleIdToken) throws GeneralSecurityException, IOException {
        if (!this.clientIds.isEmpty()) {
            return verify(this.clientIds, googleIdToken);
        }
        if (!googleIdToken.verifyIssuer("accounts.google.com") || !googleIdToken.verifyTime(this.clock.currentTimeMillis(), TIME_SKEW_SECONDS)) {
            return false;
        }
        this.lock.lock();
        try {
            if (this.publicKeys == null || this.clock.currentTimeMillis() + 300000 > this.expirationTimeMilliseconds) {
                loadPublicCerts();
            }
            for (PublicKey verifySignature : this.publicKeys) {
                if (googleIdToken.verifySignature(verifySignature)) {
                    return true;
                }
            }
            this.lock.unlock();
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    @Deprecated
    public boolean verify(GoogleIdToken googleIdToken, String str) throws GeneralSecurityException, IOException {
        return verify(str == null ? Collections.emptySet() : Collections.singleton(str), googleIdToken);
    }

    @Deprecated
    public boolean verify(Set<String> set, GoogleIdToken googleIdToken) throws GeneralSecurityException, IOException {
        Payload payload = googleIdToken.getPayload();
        if (!googleIdToken.verifyTime(this.clock.currentTimeMillis(), TIME_SKEW_SECONDS) || !"accounts.google.com".equals(payload.getIssuer()) || ((!set.isEmpty() && (!set.contains(payload.getAudience()) || !set.contains(payload.getIssuee()))) || !googleIdToken.getHeader().getAlgorithm().equals("RS256"))) {
            return false;
        }
        this.lock.lock();
        try {
            if (this.publicKeys == null || this.clock.currentTimeMillis() + 300000 > this.expirationTimeMilliseconds) {
                loadPublicCerts();
            }
            Signature instance = Signature.getInstance("SHA256withRSA");
            for (PublicKey initVerify : this.publicKeys) {
                instance.initVerify(initVerify);
                instance.update(googleIdToken.getSignedContentBytes());
                if (instance.verify(googleIdToken.getSignatureBytes())) {
                    return true;
                }
            }
            this.lock.unlock();
            return false;
        } finally {
            this.lock.unlock();
        }
    }
}
