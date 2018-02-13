package com.google.api.client.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Objects;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Credential implements HttpExecuteInterceptor, HttpRequestInitializer, HttpUnsuccessfulResponseHandler {
    static final Logger LOGGER = Logger.getLogger(Credential.class.getName());
    private String accessToken;
    private final HttpExecuteInterceptor clientAuthentication;
    private final Clock clock;
    private Long expirationTimeMilliseconds;
    private final JsonFactory jsonFactory;
    private final Lock lock;
    private final AccessMethod method;
    private final List<CredentialRefreshListener> refreshListeners;
    private String refreshToken;
    private final HttpRequestInitializer requestInitializer;
    private final String tokenServerEncodedUrl;
    private final HttpTransport transport;

    public interface AccessMethod {
        String getAccessTokenFromRequest(HttpRequest httpRequest);

        void intercept(HttpRequest httpRequest, String str) throws IOException;
    }

    public static class Builder {
        HttpExecuteInterceptor clientAuthentication;
        Clock clock = Clock.SYSTEM;
        JsonFactory jsonFactory;
        final AccessMethod method;
        List<CredentialRefreshListener> refreshListeners = new ArrayList();
        HttpRequestInitializer requestInitializer;
        GenericUrl tokenServerUrl;
        HttpTransport transport;

        public Builder(AccessMethod accessMethod) {
            this.method = (AccessMethod) Preconditions.checkNotNull(accessMethod);
        }

        public Builder addRefreshListener(CredentialRefreshListener credentialRefreshListener) {
            this.refreshListeners.add(Preconditions.checkNotNull(credentialRefreshListener));
            return this;
        }

        public Credential build() {
            return new Credential(this);
        }

        public final HttpExecuteInterceptor getClientAuthentication() {
            return this.clientAuthentication;
        }

        public final Clock getClock() {
            return this.clock;
        }

        public final JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public final AccessMethod getMethod() {
            return this.method;
        }

        public final List<CredentialRefreshListener> getRefreshListeners() {
            return this.refreshListeners;
        }

        public final HttpRequestInitializer getRequestInitializer() {
            return this.requestInitializer;
        }

        public final GenericUrl getTokenServerUrl() {
            return this.tokenServerUrl;
        }

        public final HttpTransport getTransport() {
            return this.transport;
        }

        public Builder setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
            this.clientAuthentication = httpExecuteInterceptor;
            return this;
        }

        public Builder setClock(Clock clock) {
            this.clock = (Clock) Preconditions.checkNotNull(clock);
            return this;
        }

        public Builder setJsonFactory(JsonFactory jsonFactory) {
            this.jsonFactory = jsonFactory;
            return this;
        }

        public Builder setRefreshListeners(List<CredentialRefreshListener> list) {
            this.refreshListeners = list;
            return this;
        }

        public Builder setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            this.requestInitializer = httpRequestInitializer;
            return this;
        }

        public Builder setTokenServerEncodedUrl(String str) {
            this.tokenServerUrl = str == null ? null : new GenericUrl(str);
            return this;
        }

        public Builder setTokenServerUrl(GenericUrl genericUrl) {
            this.tokenServerUrl = genericUrl;
            return this;
        }

        public Builder setTransport(HttpTransport httpTransport) {
            this.transport = httpTransport;
            return this;
        }
    }

    public Credential(AccessMethod accessMethod) {
        this(new Builder(accessMethod));
    }

    @Deprecated
    protected Credential(AccessMethod accessMethod, HttpTransport httpTransport, JsonFactory jsonFactory, String str, HttpExecuteInterceptor httpExecuteInterceptor, HttpRequestInitializer httpRequestInitializer, List<CredentialRefreshListener> list) {
        this(accessMethod, httpTransport, jsonFactory, str, httpExecuteInterceptor, httpRequestInitializer, list, Clock.SYSTEM);
    }

    @Deprecated
    protected Credential(AccessMethod accessMethod, HttpTransport httpTransport, JsonFactory jsonFactory, String str, HttpExecuteInterceptor httpExecuteInterceptor, HttpRequestInitializer httpRequestInitializer, List<CredentialRefreshListener> list, Clock clock) {
        this.lock = new ReentrantLock();
        this.method = (AccessMethod) Preconditions.checkNotNull(accessMethod);
        this.transport = httpTransport;
        this.jsonFactory = jsonFactory;
        this.tokenServerEncodedUrl = str;
        this.clientAuthentication = httpExecuteInterceptor;
        this.requestInitializer = httpRequestInitializer;
        this.refreshListeners = list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
        this.clock = (Clock) Preconditions.checkNotNull(clock);
    }

    protected Credential(Builder builder) {
        this.lock = new ReentrantLock();
        this.method = (AccessMethod) Preconditions.checkNotNull(builder.method);
        this.transport = builder.transport;
        this.jsonFactory = builder.jsonFactory;
        this.tokenServerEncodedUrl = builder.tokenServerUrl == null ? null : builder.tokenServerUrl.build();
        this.clientAuthentication = builder.clientAuthentication;
        this.requestInitializer = builder.requestInitializer;
        this.refreshListeners = builder.refreshListeners == null ? Collections.emptyList() : Collections.unmodifiableList(builder.refreshListeners);
        this.clock = (Clock) Preconditions.checkNotNull(builder.clock);
    }

    protected TokenResponse executeRefreshToken() throws IOException {
        return this.refreshToken == null ? null : new RefreshTokenRequest(this.transport, this.jsonFactory, new GenericUrl(this.tokenServerEncodedUrl), this.refreshToken).setClientAuthentication(this.clientAuthentication).setRequestInitializer(this.requestInitializer).execute();
    }

    public final String getAccessToken() {
        this.lock.lock();
        try {
            String str = this.accessToken;
            return str;
        } finally {
            this.lock.unlock();
        }
    }

    public final HttpExecuteInterceptor getClientAuthentication() {
        return this.clientAuthentication;
    }

    public final Clock getClock() {
        return this.clock;
    }

    public final Long getExpirationTimeMilliseconds() {
        this.lock.lock();
        try {
            Long l = this.expirationTimeMilliseconds;
            return l;
        } finally {
            this.lock.unlock();
        }
    }

    public final Long getExpiresInSeconds() {
        this.lock.lock();
        try {
            if (this.expirationTimeMilliseconds == null) {
                return null;
            }
            long longValue = (this.expirationTimeMilliseconds.longValue() - this.clock.currentTimeMillis()) / 1000;
            this.lock.unlock();
            return Long.valueOf(longValue);
        } finally {
            this.lock.unlock();
        }
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public final AccessMethod getMethod() {
        return this.method;
    }

    public final List<CredentialRefreshListener> getRefreshListeners() {
        return this.refreshListeners;
    }

    public final String getRefreshToken() {
        this.lock.lock();
        try {
            String str = this.refreshToken;
            return str;
        } finally {
            this.lock.unlock();
        }
    }

    public final HttpRequestInitializer getRequestInitializer() {
        return this.requestInitializer;
    }

    public final String getTokenServerEncodedUrl() {
        return this.tokenServerEncodedUrl;
    }

    public final HttpTransport getTransport() {
        return this.transport;
    }

    public boolean handleResponse(HttpRequest httpRequest, HttpResponse httpResponse, boolean z) {
        if (httpResponse.getStatusCode() == 401) {
            try {
                this.lock.lock();
                boolean z2 = !Objects.equal(this.accessToken, this.method.getAccessTokenFromRequest(httpRequest)) || refreshToken();
                this.lock.unlock();
                return z2;
            } catch (Throwable e) {
                LOGGER.log(Level.SEVERE, "unable to refresh token", e);
            } catch (Throwable th) {
                this.lock.unlock();
            }
        }
        return false;
    }

    public void initialize(HttpRequest httpRequest) throws IOException {
        httpRequest.setInterceptor(this);
        httpRequest.setUnsuccessfulResponseHandler(this);
    }

    public void intercept(HttpRequest httpRequest) throws IOException {
        this.lock.lock();
        try {
            Long expiresInSeconds = getExpiresInSeconds();
            if (this.accessToken == null || (expiresInSeconds != null && expiresInSeconds.longValue() <= 60)) {
                refreshToken();
                if (this.accessToken == null) {
                    return;
                }
            }
            this.method.intercept(httpRequest, this.accessToken);
            this.lock.unlock();
        } finally {
            this.lock.unlock();
        }
    }

    public final boolean refreshToken() throws IOException {
        Object obj = 1;
        this.lock.lock();
        try {
            TokenResponse executeRefreshToken = executeRefreshToken();
            if (executeRefreshToken != null) {
                setFromTokenResponse(executeRefreshToken);
                for (CredentialRefreshListener onTokenResponse : this.refreshListeners) {
                    onTokenResponse.onTokenResponse(this, executeRefreshToken);
                }
                this.lock.unlock();
                return true;
            }
        } catch (TokenResponseException e) {
            TokenResponseException tokenResponseException = e;
            if (400 > tokenResponseException.getStatusCode() || tokenResponseException.getStatusCode() >= 500) {
                obj = null;
            }
            if (!(tokenResponseException.getDetails() == null || obj == null)) {
                setAccessToken(null);
                setExpiresInSeconds(null);
            }
            for (CredentialRefreshListener onTokenResponse2 : this.refreshListeners) {
                onTokenResponse2.onTokenErrorResponse(this, tokenResponseException.getDetails());
            }
            if (obj != null) {
                throw tokenResponseException;
            }
        } catch (Throwable th) {
            this.lock.unlock();
        }
        this.lock.unlock();
        return false;
    }

    public Credential setAccessToken(String str) {
        this.lock.lock();
        try {
            this.accessToken = str;
            return this;
        } finally {
            this.lock.unlock();
        }
    }

    public Credential setExpirationTimeMilliseconds(Long l) {
        this.lock.lock();
        try {
            this.expirationTimeMilliseconds = l;
            return this;
        } finally {
            this.lock.unlock();
        }
    }

    public Credential setExpiresInSeconds(Long l) {
        return setExpirationTimeMilliseconds(l == null ? null : Long.valueOf(this.clock.currentTimeMillis() + (l.longValue() * 1000)));
    }

    public Credential setFromTokenResponse(TokenResponse tokenResponse) {
        setAccessToken(tokenResponse.getAccessToken());
        if (tokenResponse.getRefreshToken() != null) {
            setRefreshToken(tokenResponse.getRefreshToken());
        }
        setExpiresInSeconds(tokenResponse.getExpiresInSeconds());
        return this;
    }

    public Credential setRefreshToken(String str) {
        this.lock.lock();
        if (str != null) {
            try {
                boolean z = (this.jsonFactory == null || this.transport == null || this.clientAuthentication == null || this.tokenServerEncodedUrl == null) ? false : true;
                Preconditions.checkArgument(z, "Please use the Builder and call setJsonFactory, setTransport, setClientAuthentication and setTokenServerUrl/setTokenServerEncodedUrl");
            } catch (Throwable th) {
                this.lock.unlock();
            }
        }
        this.refreshToken = str;
        this.lock.unlock();
        return this;
    }
}
