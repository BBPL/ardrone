package org.apache.http.impl.auth;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

public abstract class GGSSchemeBase extends AuthSchemeBase {
    private final Base64 base64codec;
    private final Log log;
    private State state;
    private final boolean stripPort;
    private byte[] token;

    enum State {
        UNINITIATED,
        CHALLENGE_RECEIVED,
        TOKEN_GENERATED,
        FAILED
    }

    GGSSchemeBase() {
        this(false);
    }

    GGSSchemeBase(boolean z) {
        this.log = LogFactory.getLog(getClass());
        this.base64codec = new Base64();
        this.state = State.UNINITIATED;
        this.stripPort = z;
    }

    @Deprecated
    public Header authenticate(Credentials credentials, HttpRequest httpRequest) throws AuthenticationException {
        return authenticate(credentials, httpRequest, null);
    }

    public Header authenticate(Credentials credentials, HttpRequest httpRequest, HttpContext httpContext) throws AuthenticationException {
        if (httpRequest == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        String hostName;
        switch (this.state) {
            case UNINITIATED:
                throw new AuthenticationException(getSchemeName() + " authentication has not been initiated");
            case FAILED:
                throw new AuthenticationException(getSchemeName() + " authentication has failed");
            case CHALLENGE_RECEIVED:
                try {
                    HttpHost httpHost = (HttpHost) httpContext.getAttribute(isProxy() ? ExecutionContext.HTTP_PROXY_HOST : ExecutionContext.HTTP_TARGET_HOST);
                    if (httpHost != null) {
                        hostName = (this.stripPort || httpHost.getPort() <= 0) ? httpHost.getHostName() : httpHost.toHostString();
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("init " + hostName);
                        }
                        this.token = generateToken(this.token, hostName);
                        this.state = State.TOKEN_GENERATED;
                        break;
                    }
                    throw new AuthenticationException("Authentication host is not set in the execution context");
                } catch (GSSException e) {
                    this.state = State.FAILED;
                    if (e.getMajor() == 9 || e.getMajor() == 8) {
                        throw new InvalidCredentialsException(e.getMessage(), e);
                    } else if (e.getMajor() == 13) {
                        throw new InvalidCredentialsException(e.getMessage(), e);
                    } else if (e.getMajor() == 10 || e.getMajor() == 19 || e.getMajor() == 20) {
                        throw new AuthenticationException(e.getMessage(), e);
                    } else {
                        throw new AuthenticationException(e.getMessage());
                    }
                }
                break;
            case TOKEN_GENERATED:
                break;
            default:
                throw new IllegalStateException("Illegal state: " + this.state);
        }
        hostName = new String(this.base64codec.encode(this.token));
        if (this.log.isDebugEnabled()) {
            this.log.debug("Sending response '" + hostName + "' back to the auth server");
        }
        return new BasicHeader("Authorization", "Negotiate " + hostName);
    }

    protected byte[] generateGSSToken(byte[] bArr, Oid oid, String str) throws GSSException {
        if (bArr == null) {
            bArr = new byte[0];
        }
        GSSManager manager = getManager();
        GSSContext createContext = manager.createContext(manager.createName("HTTP@" + str, GSSName.NT_HOSTBASED_SERVICE).canonicalize(oid), oid, null, 0);
        createContext.requestMutualAuth(true);
        createContext.requestCredDeleg(true);
        return createContext.initSecContext(bArr, 0, bArr.length);
    }

    protected abstract byte[] generateToken(byte[] bArr, String str) throws GSSException;

    protected GSSManager getManager() {
        return GSSManager.getInstance();
    }

    public boolean isComplete() {
        return this.state == State.TOKEN_GENERATED || this.state == State.FAILED;
    }

    protected void parseChallenge(CharArrayBuffer charArrayBuffer, int i, int i2) throws MalformedChallengeException {
        String substringTrimmed = charArrayBuffer.substringTrimmed(i, i2);
        if (this.log.isDebugEnabled()) {
            this.log.debug("Received challenge '" + substringTrimmed + "' from the auth server");
        }
        if (this.state == State.UNINITIATED) {
            this.token = this.base64codec.decode(substringTrimmed.getBytes());
            this.state = State.CHALLENGE_RECEIVED;
            return;
        }
        this.log.debug("Authentication already attempted");
        this.state = State.FAILED;
    }
}
