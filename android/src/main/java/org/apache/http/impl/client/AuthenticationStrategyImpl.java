package org.apache.http.impl.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthOption;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthCache;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;

@Immutable
class AuthenticationStrategyImpl implements AuthenticationStrategy {
    private static final List<String> DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList(new String[]{AuthPolicy.SPNEGO, AuthPolicy.KERBEROS, AuthPolicy.NTLM, AuthPolicy.DIGEST, AuthPolicy.BASIC}));
    private final int challengeCode;
    private final String headerName;
    private final Log log = LogFactory.getLog(getClass());
    private final String prefParamName;

    AuthenticationStrategyImpl(int i, String str, String str2) {
        this.challengeCode = i;
        this.headerName = str;
        this.prefParamName = str2;
    }

    public void authFailed(HttpHost httpHost, AuthScheme authScheme, HttpContext httpContext) {
        if (httpHost == null) {
            throw new IllegalArgumentException("Host may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            AuthCache authCache = (AuthCache) httpContext.getAttribute(ClientContext.AUTH_CACHE);
            if (authCache != null) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Clearing cached auth scheme for " + httpHost);
                }
                authCache.remove(httpHost);
            }
        }
    }

    public void authSucceeded(HttpHost httpHost, AuthScheme authScheme, HttpContext httpContext) {
        if (httpHost == null) {
            throw new IllegalArgumentException("Host may not be null");
        } else if (authScheme == null) {
            throw new IllegalArgumentException("Auth scheme may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else if (isCachable(authScheme)) {
            AuthCache authCache = (AuthCache) httpContext.getAttribute(ClientContext.AUTH_CACHE);
            if (authCache == null) {
                authCache = new BasicAuthCache();
                httpContext.setAttribute(ClientContext.AUTH_CACHE, authCache);
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + httpHost);
            }
            authCache.put(httpHost, authScheme);
        }
    }

    public Map<String, Header> getChallenges(HttpHost httpHost, HttpResponse httpResponse, HttpContext httpContext) throws MalformedChallengeException {
        if (httpResponse == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        }
        Header[] headers = httpResponse.getHeaders(this.headerName);
        Map<String, Header> hashMap = new HashMap(headers.length);
        for (Header header : headers) {
            CharArrayBuffer buffer;
            int valuePos;
            if (header instanceof FormattedHeader) {
                buffer = ((FormattedHeader) header).getBuffer();
                valuePos = ((FormattedHeader) header).getValuePos();
            } else {
                String value = header.getValue();
                if (value == null) {
                    throw new MalformedChallengeException("Header value is null");
                }
                CharArrayBuffer charArrayBuffer = new CharArrayBuffer(value.length());
                charArrayBuffer.append(value);
                buffer = charArrayBuffer;
                valuePos = 0;
            }
            while (valuePos < buffer.length() && HTTP.isWhitespace(buffer.charAt(valuePos))) {
                valuePos++;
            }
            int i = valuePos;
            while (i < buffer.length() && !HTTP.isWhitespace(buffer.charAt(i))) {
                i++;
            }
            hashMap.put(buffer.substring(valuePos, i).toLowerCase(Locale.US), header);
        }
        return hashMap;
    }

    public boolean isAuthenticationRequested(HttpHost httpHost, HttpResponse httpResponse, HttpContext httpContext) {
        if (httpResponse != null) {
            return httpResponse.getStatusLine().getStatusCode() == this.challengeCode;
        } else {
            throw new IllegalArgumentException("HTTP response may not be null");
        }
    }

    protected boolean isCachable(AuthScheme authScheme) {
        if (authScheme != null && authScheme.isComplete()) {
            String schemeName = authScheme.getSchemeName();
            if (schemeName.equalsIgnoreCase(AuthPolicy.BASIC) || schemeName.equalsIgnoreCase(AuthPolicy.DIGEST)) {
                return true;
            }
        }
        return false;
    }

    public Queue<AuthOption> select(Map<String, Header> map, HttpHost httpHost, HttpResponse httpResponse, HttpContext httpContext) throws MalformedChallengeException {
        if (map == null) {
            throw new IllegalArgumentException("Map of auth challenges may not be null");
        } else if (httpHost == null) {
            throw new IllegalArgumentException("Host may not be null");
        } else if (httpResponse == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            Queue<AuthOption> linkedList = new LinkedList();
            AuthSchemeRegistry authSchemeRegistry = (AuthSchemeRegistry) httpContext.getAttribute(ClientContext.AUTHSCHEME_REGISTRY);
            if (authSchemeRegistry == null) {
                this.log.debug("Auth scheme registry not set in the context");
            } else {
                CredentialsProvider credentialsProvider = (CredentialsProvider) httpContext.getAttribute(ClientContext.CREDS_PROVIDER);
                if (credentialsProvider == null) {
                    this.log.debug("Credentials provider not set in the context");
                    return linkedList;
                }
                List list = (List) httpResponse.getParams().getParameter(this.prefParamName);
                if (list == null) {
                    list = DEFAULT_SCHEME_PRIORITY;
                }
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Authentication schemes in the order of preference: " + r2);
                }
                for (String str : r2) {
                    Header header = (Header) map.get(str.toLowerCase(Locale.US));
                    if (header != null) {
                        try {
                            AuthScheme authScheme = authSchemeRegistry.getAuthScheme(str, httpResponse.getParams());
                            authScheme.processChallenge(header);
                            Credentials credentials = credentialsProvider.getCredentials(new AuthScope(httpHost.getHostName(), httpHost.getPort(), authScheme.getRealm(), authScheme.getSchemeName()));
                            if (credentials != null) {
                                linkedList.add(new AuthOption(authScheme, credentials));
                            }
                        } catch (IllegalStateException e) {
                            if (this.log.isWarnEnabled()) {
                                this.log.warn("Authentication scheme " + str + " not supported");
                            }
                        }
                    } else if (this.log.isDebugEnabled()) {
                        this.log.debug("Challenge for " + str + " authentication scheme not available");
                    }
                }
            }
            return linkedList;
        }
    }
}
